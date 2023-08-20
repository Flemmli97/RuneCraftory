package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.tenshilib.common.utils.MathUtils;
import io.github.flemmli97.tenshilib.common.utils.RayTraceUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class EntityAppleProjectile extends BaseProjectile {

    private static final EntityDataAccessor<Float> SIZE = SynchedEntityData.defineId(EntityAppleProjectile.class, EntityDataSerializers.FLOAT);

    private boolean circling;
    private int angleOffset, circleTime;

    public EntityAppleProjectile(EntityType<? extends BaseProjectile> type, Level world) {
        super(type, world);
    }

    public EntityAppleProjectile(Level world, LivingEntity shooter) {
        super(ModEntities.APPLE.get(), world, shooter);
    }

    public void setAngleOffset(int angleOffset) {
        this.angleOffset = angleOffset;
    }

    public void setCircling(boolean circling, int circleTime) {
        this.circling = circling;
        this.circleTime = circleTime;
    }

    public void withSizeInc(float size) {
        this.entityData.set(SIZE, size);
    }

    @Override
    public int livingTickMax() {
        return 400;
    }

    @Override
    public float radius() {
        return this.entityData.get(SIZE);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SIZE, 0f);
    }

    public float getScale() {
        return this.entityData.get(SIZE) + 1;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
            --this.circleTime;
            Entity owner = this.getOwner();
            if (owner == null)
                this.discard();
            else if (this.circling) {
                if (this.circleTime > 0) {
                    Vec3 ownerPos = owner.position();
                    double[] pos = MathUtils.rotate(0, 1, 0, owner.getBbWidth() + 0.5, 0, 0, Mth.DEG_TO_RAD * (13 * this.livingTicks + this.angleOffset));
                    this.setDeltaMovement(ownerPos.x + pos[0] - this.getX(), ownerPos.y + this.getOwner().getBbHeight() * 0.25 - this.getY(), ownerPos.z + pos[2] - this.getZ());
                    this.hasImpulse = true;
                } else if (this.circleTime == 0) {
                    if (owner instanceof Mob mob && mob.getTarget() != null) {
                        this.shootAtEntity(mob.getTarget(), 1, 0, 0.1f);
                    } else {
                        this.shoot(owner, owner.getXRot(), owner.getYRot(), 0, 1, 0);
                    }
                }
            }
        }
    }

    @Override
    protected float getGravityVelocity() {
        return this.circling ? 0.005f : super.getGravityVelocity();
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult result) {
        if (CombatUtils.damageWithFaintAndCrit(this.getOwner(), result.getEntity(), new CustomDamage.Builder(this, this.getOwner()).knock(CustomDamage.KnockBackType.NONE).hurtResistant(0), CombatUtils.getAttributeValue(this.getOwner(), Attributes.ATTACK_DAMAGE) * this.damageMultiplier, null)) {
            this.discard();
            return true;
        }
        return false;
    }

    @Override
    protected void onBlockHit(BlockHitResult result) {
        if (!this.circling || this.circleTime < 0)
            this.discard();
    }

    @Override
    protected EntityHitResult getEntityHit(Vec3 from, Vec3 to) {
        if (!this.isAlive())
            return null;
        if (this.attackedEntities.size() < 1)
            return RayTraceUtils.projectileRayTrace(this.level, this, from, to, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1), this::canHit, this.radius());
        return null;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.circling = compound.getBoolean("Circling");
        this.circleTime = compound.getInt("CirclingTime");
        this.angleOffset = compound.getInt("AngleOffset");
        this.withSizeInc(compound.getFloat("Size"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Circling", this.circling);
        compound.putInt("CirclingTime", this.circleTime);
        compound.putInt("AngleOffset", this.angleOffset);
        compound.putFloat("Size", this.entityData.get(SIZE));
    }
}
