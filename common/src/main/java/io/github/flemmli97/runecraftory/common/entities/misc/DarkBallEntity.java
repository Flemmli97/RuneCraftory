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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;

public class DarkBallEntity extends BaseDamageCloud {

    private static final float[] SIN_POINTS = calcSinPoints();

    private DarkBallEntity.Type type = Type.BALL;
    private Vec3 dir, side;

    public DarkBallEntity(EntityType<? extends DarkBallEntity> type, Level level) {
        super(type, level);
    }

    public DarkBallEntity(Level level, LivingEntity thrower, Type type) {
        super(RuneCraftoryEntities.DARK_BALL.get(), level, thrower);
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

    public void shootAtPos(Vec3 targetPos, float velocity, float inaccuracy) {
        Vec3 dir = (new Vec3(targetPos.x() - this.getX(), targetPos.y() - this.getY(), targetPos.z() - this.getZ()));
        this.shoot(dir.x, dir.y, dir.z, velocity, inaccuracy);
    }

    public void shoot(Entity entityThrower, float rotationPitchIn, float rotationYawIn, float pitchOffset, float velocity, float inaccuracy) {
        float f = -Mth.sin(rotationYawIn * 0.017453292F) * Mth.cos(rotationPitchIn * 0.017453292F);
        float f1 = -Mth.sin((rotationPitchIn + pitchOffset) * 0.017453292F);
        float f2 = Mth.cos(rotationYawIn * 0.017453292F) * Mth.cos(rotationPitchIn * 0.017453292F);
        this.shoot(f, f1, f2, velocity, inaccuracy);
        Vec3 throwerMotion = entityThrower.getDeltaMovement();
        this.setDeltaMovement(this.getDeltaMovement().add(throwerMotion.x, entityThrower.onGround() ? 0.0D : throwerMotion.y, throwerMotion.z));
        this.getDeltaMovement().add(throwerMotion.x, 0, throwerMotion.z);
    }

    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        Vec3 vector3d = (new Vec3(x, y, z)).normalize().add(this.random.nextGaussian() * 0.0075F * inaccuracy, this.random.nextGaussian() * 0.0075F * inaccuracy, this.random.nextGaussian() * 0.0075F * inaccuracy).scale(velocity);
        this.setDeltaMovement(vector3d);
        double f = Math.sqrt(AdvancedProjectile.horizontalMag(vector3d));
        this.setYRot((float) (Mth.atan2(vector3d.x, vector3d.z) * (180F / (float) Math.PI)));
        this.setXRot((float) (Mth.atan2(vector3d.y, f) * (180F / (float) Math.PI)));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
        Vec3 up = this.calculateUpVector(-this.getViewXRot(1), -this.getViewYRot(1)).normalize();
        this.dir = this.getDeltaMovement();
        Vector3d rot = new Vector3d(this.dir.x(), this.dir.y(), this.dir.z())
                .rotateAxis(90 * Mth.DEG_TO_RAD, up.x(), up.y(), up.z());
        this.side = new Vec3(rot.x(), rot.y(), rot.z()).normalize();
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
        if (this.level().isClientSide) {
            for (int i = 0; i < 5; i++) {
                AdvancedParticleContainer.make(RuneCraftoryParticles.LIGHT.get())
                        .addData(new ColorData(65 / 255F, 2 / 255F, 105 / 255F, 0.2f))
                        .addData(new MotionData(this.random.nextGaussian() * 0.01, Math.abs(this.random.nextGaussian() * 0.03), this.random.nextGaussian() * 0.01))
                        .addData(new ScaleData(0.6f))
                        .addData(new ParticleMetaData(10, false, 0))
                        .add(this.level(), this.getRandomX(1), this.getY(this.getRandom().nextDouble() * 0.5) + this.getBbHeight() * 0.4, this.getRandomZ(1));
            }
            for (int i = 0; i < 3; i++) {
                AdvancedParticleContainer.make(RuneCraftoryParticles.LIGHT.get())
                        .addData(new ColorData(170 / 255F, 93 / 255F, 212 / 255F, 0.2f))
                        .addData(new MotionData(this.random.nextGaussian() * 0.01, Math.abs(this.random.nextGaussian() * 0.03), this.random.nextGaussian() * 0.01))
                        .addData(new ScaleData(0.6f))
                        .addData(new ParticleMetaData(10, false, 0))
                        .add(this.level(), this.getRandomX(1), this.getY(this.getRandom().nextDouble() * 0.5) + this.getBbHeight() * 0.4, this.getRandomZ(1));
            }
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
        return CombatUtils.damageWithFaintAndCrit(this.getOwner(), target, new DynamicDamage.Builder(this, this.getOwner()).magic().noKnockback().hurtResistant(10).element(ItemElement.DARK), CombatUtils.getAttributeValue(this.getOwner(), RuneCraftoryAttributes.MAGIC_ATTACK.asHolder()) * this.damageMultiplier, null);
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
