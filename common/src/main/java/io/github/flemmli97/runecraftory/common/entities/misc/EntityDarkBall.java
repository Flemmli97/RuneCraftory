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
import io.github.flemmli97.tenshilib.common.utils.RayTraceUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityDarkBall extends BaseDamageCloud {

    private static final float[] SIN_POINTS = calcSinPoints();

    private EntityDarkBall.Type type = Type.BALL;
    private Vec3 dir, side;

    public EntityDarkBall(EntityType<? extends EntityDarkBall> type, Level level) {
        super(type, level);
    }

    public EntityDarkBall(Level level, LivingEntity thrower, Type type) {
        super(ModEntities.DARK_BALL.get(), level, thrower);
        this.setPos(this.getX(), this.getY() + thrower.getBbHeight() * 0.5, this.getZ());
        this.type = type;
        this.setRadius(1.5f);
    }

    private static float[] calcSinPoints() {
        float[] arr = new float[16];
        float step = 2 * Mth.PI / 16;
        for (int i = 0; i < 16; i++)
            arr[i] = Mth.cos((i + 8) * step) * 0.2f;
        return arr;
    }

    public void shootAtEntity(Entity target, float velocity, float inaccuracy, float yOffsetModifier, double heighMod) {
        Vec3 dir = (new Vec3(target.getX() - this.getX(), target.getY(heighMod) - this.getY(), target.getZ() - this.getZ()));
        double l = Math.sqrt(dir.x * dir.x + dir.z * dir.z);
        this.shoot(dir.x, dir.y + l * yOffsetModifier, dir.z, velocity, inaccuracy);
    }

    public void shoot(Entity entityThrower, float rotationPitchIn, float rotationYawIn, float pitchOffset, float velocity, float inaccuracy) {
        float f = -Mth.sin(rotationYawIn * 0.017453292F) * Mth.cos(rotationPitchIn * 0.017453292F);
        float f1 = -Mth.sin((rotationPitchIn + pitchOffset) * 0.017453292F);
        float f2 = Mth.cos(rotationYawIn * 0.017453292F) * Mth.cos(rotationPitchIn * 0.017453292F);
        this.shoot(f, f1, f2, velocity, inaccuracy);
        Vec3 throwerMotion = entityThrower.getDeltaMovement();
        this.setDeltaMovement(this.getDeltaMovement().add(throwerMotion.x, entityThrower.isOnGround() ? 0.0D : throwerMotion.y, throwerMotion.z));
        this.getDeltaMovement().add(throwerMotion.x, 0, throwerMotion.z);
    }

    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        Vec3 vector3d = (new Vec3(x, y, z)).normalize().add(this.random.nextGaussian() * 0.0075F * inaccuracy, this.random.nextGaussian() * 0.0075F * inaccuracy, this.random.nextGaussian() * 0.0075F * inaccuracy).scale(velocity);
        this.setDeltaMovement(vector3d);
        double f = Math.sqrt(EntityProjectile.horizontalMag(vector3d));
        this.setYRot((float) (Mth.atan2(vector3d.x, vector3d.z) * (180F / (float) Math.PI)));
        this.setXRot((float) (Mth.atan2(vector3d.y, f) * (180F / (float) Math.PI)));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
        Vec3 up = this.calculateUpVector(-this.getViewXRot(1), -this.getViewYRot(1)).normalize();
        this.dir = this.getDeltaMovement();
        this.side = new Vec3(RayTraceUtils.rotatedAround(this.dir, new Vector3f(up), 90)).normalize();
    }

    @Override
    public int livingTickMax() {
        return this.type == Type.BALL ? 100 : 60;
    }

    @Override
    public void tick() {
        super.tick();
        Vec3 motion = this.getDeltaMovement();
        double newX = this.getX() + motion.x;
        double newY = this.getY() + motion.y;
        double newZ = this.getZ() + motion.z;
        this.setPos(newX, newY, newZ);
        if (this.level.isClientSide) {
            for (int i = 0; i < 5; i++) {
                this.level.addParticle(new ColoredParticleData(ModParticles.SHORT_LIGHT.get(), 65 / 255F, 2 / 255F, 105 / 255F, 0.2f, 5.5f), this.getX() + this.random.nextGaussian() * 0.15, this.getY() + this.random.nextGaussian() * 0.07, this.getZ() + this.random.nextGaussian() * 0.15, this.random.nextGaussian() * 0.01, Math.abs(this.random.nextGaussian() * 0.03), this.random.nextGaussian() * 0.01);
            }
            for (int i = 0; i < 3; i++)
                this.level.addParticle(new ColoredParticleData(ModParticles.SHORT_LIGHT.get(), 170 / 255F, 93 / 255F, 212 / 255F, 0.2f, 5.5f), this.getX() + this.random.nextGaussian() * 0.15, this.getY() + this.random.nextGaussian() * 0.07, this.getZ() + this.random.nextGaussian() * 0.15, this.random.nextGaussian() * 0.01, Math.abs(this.random.nextGaussian() * 0.03), this.random.nextGaussian() * 0.01);
        } else {
            if (this.type == Type.SNAKE && this.dir != null && this.side != null) {
                int t = this.livingTicks % 16;
                float sT = SIN_POINTS[t];
                this.setDeltaMovement(this.dir.x + this.side.x * sT, this.dir.y + this.side.y * sT, this.dir.z + this.side.z * sT);
                this.hasImpulse = true;
            }
        }
    }

    @Override
    protected boolean damageEntity(LivingEntity target) {
        return CombatUtils.damageWithFaintAndCrit(this.getOwner(), target, new CustomDamage.Builder(this, this.getOwner()).magic().noKnockback().hurtResistant(10).element(EnumElement.DARK), CombatUtils.getAttributeValue(this.getOwner(), ModAttributes.MAGIC.get()) * this.damageMultiplier, null);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        try {
            this.type = Type.valueOf(compound.getString("Type"));
        } catch (IllegalArgumentException e) {
            this.type = Type.BALL;
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putString("Type", this.type.toString());
    }

    public enum Type {
        BALL,
        SNAKE
    }
}
