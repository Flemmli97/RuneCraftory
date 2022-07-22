package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.tenshilib.common.entity.EntityDamageCloud;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import io.github.flemmli97.tenshilib.common.utils.MathUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public class EntityLightBall extends EntityDamageCloud {

    private Type lightType = Type.LONG;
    private Predicate<LivingEntity> pred;
    private float damageMultiplier = 1;
    private int angleOffset;
    private int firstDmg = -1;

    public EntityLightBall(EntityType<? extends EntityLightBall> type, Level level) {
        super(type, level);
    }

    public EntityLightBall(Level level, double x, double y, double z) {
        super(ModEntities.lightBall.get(), level, x, y, z);
        this.setRadius(0.8f);
    }

    public EntityLightBall(Level level, LivingEntity thrower) {
        super(ModEntities.lightBall.get(), level, thrower);
        if (thrower instanceof BaseMonster)
            this.pred = ((BaseMonster) thrower).hitPred;
        this.setRadius(0.8f);
    }

    public static void createFrontLights(Level level, LivingEntity thrower, float dmgMod) {
        if (level.isClientSide)
            return;
        for (int i = 0; i < 2; i++) {
            EntityLightBall ball = new EntityLightBall(level, thrower);
            ball.setDamageMultiplier(dmgMod);
            ball.lightType = Type.FRONT;
            ball.setAngleOffset(i == 0 ? -15 : 15);
            level.addFreshEntity(ball);
        }
    }

    public static void createQuadLights(Level level, LivingEntity thrower, boolean shortLived, float dmgMod) {
        if (level.isClientSide)
            return;
        for (int i = 0; i < 4; i++) {
            EntityLightBall ball = new EntityLightBall(level, thrower);
            ball.setDamageMultiplier(dmgMod);
            ball.lightType = shortLived ? Type.SHORT : Type.LONG;
            ball.setAngleOffset(90 * i);
            level.addFreshEntity(ball);
        }
    }

    public void setAngleOffset(int angleOffset) {
        this.angleOffset = angleOffset;
    }

    public void setDamageMultiplier(float damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
    }

    @Override
    public int maxHitCount() {
        return this.lightType == Type.FRONT ? 5 : -1;
    }

    @Override
    public int livingTickMax() {
        return switch (this.lightType) {
            case SHORT -> 200;
            case LONG, FRONT -> 144000;
        };
    }

    @Override
    public boolean canStartDamage() {
        return this.firstDmg == -1 || (this.livingTicks - this.firstDmg) % 5 == 0;
    }

    @Override
    public void tick() {
        super.tick();
        Vec3 motion = this.getDeltaMovement();
        double newX = this.getX() + motion.x;
        double newY = this.getY() + motion.y;
        double newZ = this.getZ() + motion.z;
        this.setPos(newX, newY, newZ);
        if (this.getOwner() == null)
            this.remove(RemovalReason.KILLED);
        if (this.level.isClientSide) {
            for (int i = 0; i < 2; i++)
                this.level.addParticle(new ColoredParticleData(ModParticles.shortLight.get(), 246 / 255F, 252 / 255F, 197 / 255F, 0.5f, 3f), this.getX(), this.getY(), this.getZ(), 0, 0, 0);
        } else {
            if (this.getOwner() != null) {
                Entity owner = this.getOwner();
                Vec3 ownerPos = owner.position();
                double[] pos;
                if (this.lightType == Type.FRONT) {
                    Vec3 look = this.getOwner().getLookAngle();
                    look = new Vec3(look.x, 0, look.z).scale(1.2);
                    pos = MathUtils.rotate(0, 1, 0, look.x, 0, look.z, Mth.DEG_TO_RAD * this.angleOffset);
                } else {
                    pos = MathUtils.rotate(0, 1, 0, owner.getBbWidth() + 0.5, 0, 0, Mth.DEG_TO_RAD * (13 * this.livingTicks + this.angleOffset));
                }
                this.setDeltaMovement(ownerPos.x + pos[0] - this.getX(), ownerPos.y + this.getOwner().getBbHeight() * 0.6 - this.getY(), ownerPos.z + pos[2] - this.getZ());
                this.hasImpulse = true;
            }
        }
    }

    @Override
    protected boolean canHit(LivingEntity entity) {
        return super.canHit(entity) && (this.pred == null || this.pred.test(entity));
    }

    @Override
    protected boolean damageEntity(LivingEntity target) {
        if (CombatUtils.damage(this.getOwner(), target, new CustomDamage.Builder(this, this.getOwner()).hurtResistant(0).element(EnumElement.LIGHT).get(), CombatUtils.getAttributeValueRaw(this.getOwner(), ModAttributes.RF_MAGIC.get()) * this.damageMultiplier, null)) {
            if (this.lightType == Type.LONG)
                this.remove(RemovalReason.KILLED);
            if (this.firstDmg == -1)
                this.firstDmg = this.livingTicks;
            return true;
        }
        return false;
    }

    @Override
    protected void onMaxEntities() {
        this.remove(RemovalReason.KILLED);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        try {
            this.lightType = Type.valueOf(compound.getString("LightType"));
        } catch (IllegalArgumentException e) {
            this.lightType = Type.LONG;
        }
        this.angleOffset = compound.getInt("AngleOffset");
        this.damageMultiplier = compound.getFloat("DamageMultiplier");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putString("LightType", this.lightType.toString());
        compound.putInt("AngleOffset", this.angleOffset);
        compound.putFloat("DamageMultiplier", this.damageMultiplier);
    }

    @Override
    public Entity getOwner() {
        Entity owner = super.getOwner();
        if (owner instanceof BaseMonster)
            this.pred = ((BaseMonster) owner).hitPred;
        return owner;
    }

    public enum Type {
        SHORT,
        LONG,
        FRONT
    }
}
