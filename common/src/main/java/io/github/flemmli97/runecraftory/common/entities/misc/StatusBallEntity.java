package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.items.ItemElement;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttributes;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryParticles;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.DynamicDamage;
import io.github.flemmli97.tenshilib.common.entity.AdvancedProjectile;
import io.github.flemmli97.tenshilib.common.particle.AdvancedParticleContainer;
import io.github.flemmli97.tenshilib.common.particle.data.ColorData;
import io.github.flemmli97.tenshilib.common.particle.data.MotionData;
import io.github.flemmli97.tenshilib.common.particle.data.ParticleMetaData;
import io.github.flemmli97.tenshilib.common.particle.data.ScaleData;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.function.Consumer;

public class StatusBallEntity extends BaseDamageCloud {

    private static final EntityDataAccessor<Integer> TYPE_DATA = SynchedEntityData.defineId(StatusBallEntity.class, EntityDataSerializers.INT);

    private Type type = Type.SLEEP;
    private int maxLivingTicks;

    public StatusBallEntity(EntityType<? extends StatusBallEntity> type, Level level) {
        super(type, level);
    }

    public StatusBallEntity(Level level, LivingEntity shooter) {
        super(RuneCraftoryEntities.STATUS_BALL.get(), level, shooter);
        this.setPos(shooter.getX(), shooter.getY(), shooter.getZ());
    }

    @Override
    public int livingTickMax() {
        return this.maxLivingTicks;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(TYPE_DATA, 0);
    }

    public void setType(Type type) {
        this.type = type;
        this.entityData.set(TYPE_DATA, this.type.ordinal());
        this.maxLivingTicks = this.type.maxLivingTicks;
    }

    public void setLivingTicksMax(int ticks) {
        this.maxLivingTicks = ticks;
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (key == TYPE_DATA) {
            int id = this.entityData.get(TYPE_DATA);
            if (id >= 0 && id < Type.values().length)
                this.type = Type.values()[id];
        }
    }

    public void shootAtPos(Vec3 targetPos, float velocity, float inaccuracy) {
        Vec3 dir = (new Vec3(targetPos.x() - this.getX(), targetPos.y() - this.getY(), targetPos.z() - this.getZ()));
        this.shoot(dir.x, dir.y, dir.z, velocity, inaccuracy);
    }

    public void shootFromRotation(Entity shooter, float pitch, float yaw, float pitchOffset, float velocity, float inaccuracy) {
        float f = -Mth.sin(yaw * ((float) Math.PI / 180)) * Mth.cos(pitch * ((float) Math.PI / 180));
        float g = -Mth.sin((pitch + pitchOffset) * ((float) Math.PI / 180));
        float h = Mth.cos(yaw * ((float) Math.PI / 180)) * Mth.cos(pitch * ((float) Math.PI / 180));
        this.shoot(f, g, h, velocity, inaccuracy);
        Vec3 vec3 = shooter.getDeltaMovement();
        this.setDeltaMovement(this.getDeltaMovement().add(vec3.x, shooter.onGround() ? 0.0 : vec3.y, vec3.z));
    }

    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        Vec3 vector3d = (new Vec3(x, y, z)).normalize().add(this.random.nextGaussian() * 0.0075F * inaccuracy, this.random.nextGaussian() * 0.0075F * inaccuracy, this.random.nextGaussian() * 0.0075F * inaccuracy).scale(velocity);
        this.setDeltaMovement(vector3d);
        double f = Math.sqrt(AdvancedProjectile.horizontalMag(vector3d));
        this.setYRot((float) (Mth.atan2(vector3d.x, vector3d.z) * (180F / (float) Math.PI)));
        this.setXRot((float) (Mth.atan2(vector3d.y, f) * (180F / (float) Math.PI)));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            switch (this.type) {
                case SLEEP, PARALYSIS, MUSHROOM_POISON -> {
                    Vector3f color = this.type.particleColor;
                    AdvancedParticleContainer.make(RuneCraftoryParticles.LIGHT.get())
                            .addData(new ColorData(color.x(), color.y(), color.z(), 0.6f))
                            .addData(new MotionData(this.random.nextGaussian() * 0.01, Math.abs(this.random.nextGaussian() * 0.03), this.random.nextGaussian() * 0.01))
                            .addData(new ScaleData(0.4f))
                            .addData(new ParticleMetaData(10, false, 0))
                            .build().add(this.level(), this.getRandomX(1), this.getY(this.getRandom().nextDouble() * 0.5) + this.getBbHeight() * 0.4, this.getRandomZ(1));
                }
                case RAFFLESIA_SLEEP, RAFFLESIA_PARALYSIS, RAFFLESIA_POISON, RAFFLESIA_ALL -> {
                    Vector3f color = this.type.particleColor;
                    AdvancedParticleContainer.make(new DustParticleOptions(color, 1))
                            .addData(new ScaleData(0.2f))
                            .addData(new ParticleMetaData(10, false, 0))
                            .build().add(this.level(), this.getRandomX(1), this.getY(this.getRandom().nextDouble() * 0.5) + this.getBbHeight() * 0.4, this.getRandomZ(1));
                }
            }
        }
        Vec3 motion = this.getDeltaMovement();
        double newX = this.getX() + motion.x;
        double newY = this.getY() + motion.y;
        double newZ = this.getZ() + motion.z;
        this.setPos(newX, newY, newZ);
    }

    @Override
    protected boolean damageEntity(LivingEntity target) {
        DynamicDamage.Builder builder = new DynamicDamage.Builder(this, this.getOwner()).noKnockback();
        this.type.damageMod.accept(builder);
        if (CombatUtils.damageWithFaintAndCrit(this.getOwner(), target, builder, CombatUtils.getAttributeValue(this.getOwner(), RuneCraftoryAttributes.MAGIC_ATTACK.asHolder()) * this.damageMultiplier, null)) {
            this.discard();
            return true;
        }
        return false;
    }

    @Override
    protected AABB damageBoundingBox() {
        return this.getBoundingBox().inflate(0.3, 0.45, 0.3);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("StatusType", this.type.ordinal());
        compound.putInt("MaxTicks", this.maxLivingTicks);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.type = Type.values()[compound.getInt("StatusType")];
        this.maxLivingTicks = compound.getInt("MaxTicks");
    }

    public enum Type {

        SLEEP(b -> b.magic().element(ItemElement.EARTH).withChangedAttribute(RuneCraftoryAttributes.SLEEP.asHolder(), 100), new Vector3f(207 / 255F, 13 / 255F, 38 / 255F), 80),
        MUSHROOM_POISON(b -> b.magic().withChangedAttribute(RuneCraftoryAttributes.POISON.asHolder(), 50), new Vector3f(112 / 255F, 201 / 255F, 95 / 255F), 40),
        PARALYSIS(b -> b.magic().withChangedAttribute(RuneCraftoryAttributes.PARALYSIS.asHolder(), 50), new Vector3f(196 / 255F, 186 / 255F, 35 / 255F), 40),
        RAFFLESIA_SLEEP(b -> b.hurtResistant(2).magic().withChangedAttribute(RuneCraftoryAttributes.SLEEP.asHolder(), 10)
                .withChangedAttribute(RuneCraftoryAttributes.FATIGUE.asHolder(), 2)
                .withChangedAttribute(RuneCraftoryAttributes.COLD.asHolder(), 2), new Vector3f(207 / 255F, 13 / 255F, 38 / 255F), 30),
        RAFFLESIA_PARALYSIS(b -> b.hurtResistant(2).magic().withChangedAttribute(RuneCraftoryAttributes.PARALYSIS.asHolder(), 7)
                .withChangedAttribute(RuneCraftoryAttributes.FATIGUE.asHolder(), 2)
                .withChangedAttribute(RuneCraftoryAttributes.COLD.asHolder(), 2), new Vector3f(204 / 255F, 190 / 255F, 57 / 255F), 30),
        RAFFLESIA_POISON(b -> b.hurtResistant(2).magic().withChangedAttribute(RuneCraftoryAttributes.POISON.asHolder(), 7)
                .withChangedAttribute(RuneCraftoryAttributes.FATIGUE.asHolder(), 2)
                .withChangedAttribute(RuneCraftoryAttributes.COLD.asHolder(), 2), new Vector3f(184 / 255F, 56 / 255F, 209 / 255F), 30),
        RAFFLESIA_ALL(b -> b.hurtResistant(2).magic().withChangedAttribute(RuneCraftoryAttributes.SLEEP.asHolder(), 5)
                .withChangedAttribute(RuneCraftoryAttributes.PARALYSIS.asHolder(), 5)
                .withChangedAttribute(RuneCraftoryAttributes.POISON.asHolder(), 5), new Vector3f(135 / 255F, 23 / 255F, 29 / 255F), 30);

        public final Consumer<DynamicDamage.Builder> damageMod;

        public final Vector3f particleColor;

        public final int maxLivingTicks;

        Type(Consumer<DynamicDamage.Builder> damageMod, Vector3f particleColor, int maxLivingTicks) {
            this.damageMod = damageMod;
            this.particleColor = particleColor;
            this.maxLivingTicks = maxLivingTicks;
        }
    }
}
