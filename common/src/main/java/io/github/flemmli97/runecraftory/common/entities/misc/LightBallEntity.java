package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.items.ItemElement;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttributes;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryParticles;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.DynamicDamage;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.tenshilib.common.particle.AdvancedParticleContainer;
import io.github.flemmli97.tenshilib.common.particle.data.ColorData;
import io.github.flemmli97.tenshilib.common.particle.data.ParticleMetaData;
import io.github.flemmli97.tenshilib.common.particle.data.ScaleData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class LightBallEntity extends BaseDamageCloud {

    private Type lightType = Type.LONG;
    private Vec3 spawnPos;
    private float angleOffset;
    private int firstDmg = -1;

    public LightBallEntity(EntityType<? extends LightBallEntity> type, Level level) {
        super(type, level);
    }

    public LightBallEntity(Level level, LivingEntity thrower) {
        super(RuneCraftoryEntities.LIGHT_BALL.get(), level, thrower);
        this.setRadius(0.8f);
    }

    public static void createFrontLights(Level level, LivingEntity thrower, float dmgMod) {
        if (level.isClientSide)
            return;
        for (int i = 0; i < 2; i++) {
            LightBallEntity ball = new LightBallEntity(level, thrower);
            ball.setDamageMultiplier(dmgMod);
            ball.lightType = Type.FRONT;
            ball.setAngleOffset(i == 0 ? -15 : 15);
            level.addFreshEntity(ball);
        }
    }

    public static void createLights(Level level, LivingEntity thrower, Type type, float dmgMod, int amount) {
        if (level.isClientSide)
            return;
        float angle = 360f / amount;
        for (int i = 0; i < amount; i++) {
            LightBallEntity ball = new LightBallEntity(level, thrower);
            ball.setDamageMultiplier(dmgMod);
            ball.lightType = type;
            ball.setAngleOffset(angle * i);
            level.addFreshEntity(ball);
        }
    }

    public void setAngleOffset(float angleOffset) {
        this.angleOffset = angleOffset;
    }

    @Override
    public int maxHitCount() {
        return this.lightType == Type.FRONT ? 5 : -1;
    }

    @Override
    public int livingTickMax() {
        return switch (this.lightType) {
            case EXPAND -> 30;
            case PIERCING_SHORT -> 140;
            case PIERCING_LONG -> 240;
            case LONG, FRONT -> 144000;
        };
    }

    @Override
    public boolean canStartDamage() {
        return this.firstDmg == -1 || (this.livingTicks - this.firstDmg) % 5 == 0;
    }

    @Override
    protected AABB damageBoundingBox() {
        return this.getBoundingBox().inflate(0.25, 0.25, 0.25);
    }

    @Override
    public void tick() {
        super.tick();
        Vec3 motion = this.getDeltaMovement();
        double newX = this.getX() + motion.x;
        double newY = this.getY() + motion.y;
        double newZ = this.getZ() + motion.z;
        this.setPos(newX, newY, newZ);
        if (this.spawnPos == null)
            this.spawnPos = this.position();
        if (this.getOwner() == null)
            this.discard();
        if (this.level().isClientSide) {
            AdvancedParticleContainer.make(RuneCraftoryParticles.LIGHT.get())
                    .addData(new ColorData(246 / 255F, 252 / 255F, 197 / 255F, 0.7f))
                    .addData(new ScaleData(0.4f))
                    .addData(new ParticleMetaData(1, false, 0))
                    .add(this.level(), this.getX(), this.getY() + this.getBbHeight() * 0.5, this.getZ());
        } else {
            if (this.getOwner() != null) {
                Entity owner = this.getOwner();
                Vec3 ownerPos = owner.position();
                Vec3 pos = switch (this.lightType) {
                    case FRONT -> {
                        Vec3 look = EntityUtils.horizontalLookAngle(this.getOwner()).scale(1.2);
                        yield look.yRot(Mth.DEG_TO_RAD * this.angleOffset);
                    }
                    case LONG, PIERCING_SHORT, PIERCING_LONG -> new Vec3(owner.getBbWidth() + 0.5, 0, 0)
                            .yRot(Mth.DEG_TO_RAD * (13 * this.livingTicks + this.angleOffset));
                    case EXPAND -> new Vec3(owner.getBbWidth() + this.livingTicks * this.livingTicks * 0.01, 0, 0)
                            .yRot(Mth.DEG_TO_RAD * (13 * this.livingTicks + this.angleOffset));
                };
                this.setDeltaMovement(ownerPos.x + pos.x() - this.getX(), ownerPos.y + this.getOwner().getBbHeight() * 0.5 - this.getY(0.5), ownerPos.z + pos.z() - this.getZ());
                this.hasImpulse = true;
            }
        }
    }

    @Override
    protected boolean damageEntity(LivingEntity target) {
        if (CombatUtils.damageWithFaintAndCrit(this.getOwner(), target, new DynamicDamage.Builder(this, this.getOwner()).magic().hurtResistant(0).element(ItemElement.LIGHT), CombatUtils.getAttributeValue(this.getOwner(), RuneCraftoryAttributes.MAGIC_ATTACK.asHolder()) * this.damageMultiplier, null)) {
            if (this.lightType == Type.LONG || this.lightType == Type.EXPAND)
                this.discard();
            if (this.firstDmg == -1)
                this.firstDmg = this.livingTicks;
            return true;
        }
        return false;
    }

    @Override
    protected void onMaxEntities() {
        this.discard();
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        try {
            this.lightType = Type.valueOf(compound.getString("LightType"));
        } catch (IllegalArgumentException e) {
            this.lightType = Type.LONG;
        }
        this.angleOffset = compound.getFloat("AngleOffset");
        if (compound.contains("SpawnX"))
            this.spawnPos = new Vec3(compound.getDouble("SpawnX"), compound.getDouble("SpawnY"), compound.getDouble("SpawnZ"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putString("LightType", this.lightType.toString());
        compound.putFloat("AngleOffset", this.angleOffset);
        if (this.spawnPos != null) {
            compound.putDouble("SpawnX", this.spawnPos.x);
            compound.putDouble("SpawnY", this.spawnPos.y);
            compound.putDouble("SpawnZ", this.spawnPos.z);
        }
    }

    public enum Type {
        PIERCING_SHORT,
        PIERCING_LONG,
        LONG,
        FRONT,
        EXPAND
    }
}
