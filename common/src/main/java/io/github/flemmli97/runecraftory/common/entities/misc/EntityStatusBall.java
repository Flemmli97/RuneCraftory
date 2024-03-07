package io.github.flemmli97.runecraftory.common.entities.misc;

import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.particles.DurationalParticleData;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.tenshilib.common.entity.EntityProjectile;
import io.github.flemmli97.tenshilib.common.entity.EntityUtil;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
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

import java.util.function.Consumer;

public class EntityStatusBall extends BaseDamageCloud {

    private static final EntityDataAccessor<Integer> TYPE_DATA = SynchedEntityData.defineId(EntityStatusBall.class, EntityDataSerializers.INT);

    private Type type = Type.SLEEP;

    public EntityStatusBall(EntityType<? extends EntityStatusBall> type, Level world) {
        super(type, world);
    }

    public EntityStatusBall(Level world, LivingEntity shooter) {
        super(ModEntities.STATUS_BALL.get(), world, shooter);
        this.setPos(shooter.getX(), shooter.getY(), shooter.getZ());
    }

    @Override
    public int livingTickMax() {
        return this.type.maxLivingTicks;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TYPE_DATA, 0);
    }

    public void setType(Type type) {
        this.type = type;
        this.entityData.set(TYPE_DATA, this.type.ordinal());
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

    public void shootAtEntity(Entity target, float velocity, float inaccuracy, float yOffsetModifier) {
        Vec3 targetPos = EntityUtil.getStraightProjectileTarget(this.position(), target);
        Vec3 dir = (new Vec3(targetPos.x() - this.getX(), targetPos.y() - this.getY(), targetPos.z() - this.getZ()));
        this.shoot(dir.x, dir.y + yOffsetModifier, dir.z, velocity, inaccuracy);
    }

    public void shootFromRotation(Entity shooter, float pitch, float yaw, float pitchOffset, float velocity, float inaccuracy) {
        float f = -Mth.sin(yaw * ((float) Math.PI / 180)) * Mth.cos(pitch * ((float) Math.PI / 180));
        float g = -Mth.sin((pitch + pitchOffset) * ((float) Math.PI / 180));
        float h = Mth.cos(yaw * ((float) Math.PI / 180)) * Mth.cos(pitch * ((float) Math.PI / 180));
        this.shoot(f, g, h, velocity, inaccuracy);
        Vec3 vec3 = shooter.getDeltaMovement();
        this.setDeltaMovement(this.getDeltaMovement().add(vec3.x, shooter.isOnGround() ? 0.0 : vec3.y, vec3.z));
    }

    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        Vec3 vector3d = (new Vec3(x, y, z)).normalize().add(this.random.nextGaussian() * 0.0075F * inaccuracy, this.random.nextGaussian() * 0.0075F * inaccuracy, this.random.nextGaussian() * 0.0075F * inaccuracy).scale(velocity);
        this.setDeltaMovement(vector3d);
        double f = Math.sqrt(EntityProjectile.horizontalMag(vector3d));
        this.setYRot((float) (Mth.atan2(vector3d.x, vector3d.z) * (180F / (float) Math.PI)));
        this.setXRot((float) (Mth.atan2(vector3d.y, f) * (180F / (float) Math.PI)));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level.isClientSide) {
            switch (this.type) {
                case SLEEP, MUSHROOM_POISON -> {
                    for (int i = 0; i < 2; i++) {
                        Vector3f color = this.type.particleColor;
                        this.level.addParticle(new ColoredParticleData(ModParticles.light.get(), color.x(), color.y(), color.z(), 1, 2), this.getX() + this.random.nextGaussian() * 0.15, this.getY() + 0.35 + this.random.nextGaussian() * 0.07, this.getZ() + this.random.nextGaussian() * 0.15, this.random.nextGaussian() * 0.01, Math.abs(this.random.nextGaussian() * 0.03), this.random.nextGaussian() * 0.01);
                    }
                }
                case RAFFLESIA_SLEEP, RAFFLESIA_PARALYSIS, RAFFLESIA_POISON, RAFFLESIA_ALL -> {
                    for (int i = 0; i < 2; i++) {
                        Vector3f color = this.type.particleColor;
                        this.level.addParticle(new DurationalParticleData(color.x(), color.y(), color.z(), 0.8f, 2.5f, 3), this.getX() + this.random.nextGaussian() * 0.15, this.getY() + 0.35 + this.random.nextGaussian() * 0.07, this.getZ() + this.random.nextGaussian() * 0.15, 0, 0, 0);
                    }
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
        CustomDamage.Builder builder = new CustomDamage.Builder(this, this.getOwner()).noKnockback();
        this.type.damageMod.accept(builder);
        if (CombatUtils.damageWithFaintAndCrit(this.getOwner(), target, builder, CombatUtils.getAttributeValue(this.getOwner(), ModAttributes.MAGIC.get()) * this.damageMultiplier, null)) {
            this.discard();
            return true;
        }
        return false;
    }

    @Override
    protected AABB damageBoundingBox() {
        return this.getBoundingBox().inflate(0.3, 0.45, 0.3);
    }

    public enum Type {

        SLEEP(b -> b.magic().element(EnumElement.EARTH).withChangedAttribute(ModAttributes.SLEEP.get(), 100), new Vector3f(207 / 255F, 13 / 255F, 38 / 255F), 40),
        MUSHROOM_POISON(b -> b.magic().withChangedAttribute(ModAttributes.POISON.get(), 50), new Vector3f(112 / 255F, 201 / 255F, 95 / 255F), 40),
        RAFFLESIA_SLEEP(b -> b.hurtResistant(2).magic().withChangedAttribute(ModAttributes.SLEEP.get(), 10)
                .withChangedAttribute(ModAttributes.FATIGUE.get(), 2)
                .withChangedAttribute(ModAttributes.COLD.get(), 2), new Vector3f(207 / 255F, 13 / 255F, 38 / 255F), 30),
        RAFFLESIA_PARALYSIS(b -> b.hurtResistant(2).magic().withChangedAttribute(ModAttributes.PARA.get(), 10)
                .withChangedAttribute(ModAttributes.FATIGUE.get(), 2)
                .withChangedAttribute(ModAttributes.COLD.get(), 2), new Vector3f(204 / 255F, 190 / 255F, 57 / 255F), 30),
        RAFFLESIA_POISON(b -> b.hurtResistant(2).magic().withChangedAttribute(ModAttributes.POISON.get(), 10)
                .withChangedAttribute(ModAttributes.FATIGUE.get(), 2)
                .withChangedAttribute(ModAttributes.COLD.get(), 2), new Vector3f(184 / 255F, 56 / 255F, 209 / 255F), 30),
        RAFFLESIA_ALL(b -> b.hurtResistant(2).magic().withChangedAttribute(ModAttributes.SLEEP.get(), 5)
                .withChangedAttribute(ModAttributes.PARA.get(), 5)
                .withChangedAttribute(ModAttributes.POISON.get(), 5), new Vector3f(135 / 255F, 23 / 255F, 29 / 255F), 30);

        public final Consumer<CustomDamage.Builder> damageMod;

        public final Vector3f particleColor;

        public final int maxLivingTicks;

        Type(Consumer<CustomDamage.Builder> damageMod, Vector3f particleColor, int maxLivingTicks) {
            this.damageMod = damageMod;
            this.particleColor = particleColor;
            this.maxLivingTicks = maxLivingTicks;
        }
    }
}
