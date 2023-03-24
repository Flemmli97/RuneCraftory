package io.github.flemmli97.runecraftory.common.entities.misc;

import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.tenshilib.common.entity.EntityProjectile;
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
        return 40;
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
        Vec3 dir = (new Vec3(target.getX() - this.getX(), target.getY(0.33) - this.getY(), target.getZ() - this.getZ()));
        double l = Math.sqrt(dir.x * dir.x + dir.z * dir.z);
        this.shoot(dir.x, dir.y + l * yOffsetModifier, dir.z, velocity, inaccuracy);
    }

    public void shootFromRotation(Entity shooter, float x, float y, float z, float velocity, float inaccuracy) {
        float f = -Mth.sin(y * ((float) Math.PI / 180)) * Mth.cos(x * ((float) Math.PI / 180));
        float g = -Mth.sin((x + z) * ((float) Math.PI / 180));
        float h = Mth.cos(y * ((float) Math.PI / 180)) * Mth.cos(x * ((float) Math.PI / 180));
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
            for (int i = 0; i < 2; i++) {
                Vector3f color = this.type.particleColor;
                this.level.addParticle(new ColoredParticleData(ModParticles.light.get(), color.x(), color.y(), color.z(), 1, 2), this.getX() + this.random.nextGaussian() * 0.15, this.getY() + 0.35 + this.random.nextGaussian() * 0.07, this.getZ() + this.random.nextGaussian() * 0.15, this.random.nextGaussian() * 0.01, Math.abs(this.random.nextGaussian() * 0.03), this.random.nextGaussian() * 0.01);
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
        }
        return false;
    }

    @Override
    protected AABB damageBoundingBox() {
        return this.getBoundingBox().inflate(0.3);
    }

    public enum Type {

        SLEEP(b -> {
            b.magic().element(EnumElement.EARTH).withChangedAttribute(ModAttributes.RF_SLEEP.get(), 100);
        }, new Vector3f(207 / 255F, 13 / 255F, 38 / 255F)),
        MUSHROOM_POISON(b -> {
            b.magic().withChangedAttribute(ModAttributes.RF_POISON.get(), 50);
        }, new Vector3f(112 / 255F, 201 / 255F, 95 / 255F));

        public final Consumer<CustomDamage.Builder> damageMod;

        public final Vector3f particleColor;

        Type(Consumer<CustomDamage.Builder> damageMod, Vector3f particleColor) {
            this.damageMod = damageMod;
            this.particleColor = particleColor;
        }
    }
}
