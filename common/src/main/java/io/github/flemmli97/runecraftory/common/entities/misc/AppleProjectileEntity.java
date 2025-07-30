package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.DynamicDamage;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class AppleProjectileEntity extends BaseProjectile {

    private static final EntityDataAccessor<Float> SCALE = SynchedEntityData.defineId(AppleProjectileEntity.class, EntityDataSerializers.FLOAT);

    private boolean circling;
    private int angleOffset, circleTime;

    public AppleProjectileEntity(EntityType<? extends BaseProjectile> type, Level world) {
        super(type, world);
    }

    public AppleProjectileEntity(Level world, LivingEntity shooter) {
        super(RuneCraftoryEntities.APPLE.get(), world, shooter);
    }

    public void setAngleOffset(int angleOffset) {
        this.angleOffset = angleOffset;
    }

    public void setCircling(boolean circling, int circleTime) {
        this.circling = circling;
        this.circleTime = circleTime;
    }

    public void withSizeInc(float size) {
        this.entityData.set(SCALE, size);
    }

    @Override
    public int livingTickMax() {
        return 300;
    }

    @Override
    public float radius() {
        return this.getBbWidth() * this.getScale();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SCALE, 1f);
    }

    public float getScale() {
        return Math.max(0.2f, this.entityData.get(SCALE));
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            --this.circleTime;
            if (this.circling && this.getOwner() != null) {
                Entity owner = this.getOwner();
                if (this.circleTime > 0) {
                    Vec3 ownerPos = owner.position();
                    Vec3 pos = new Vec3(owner.getBbWidth() + 0.5, 0, 0)
                            .yRot((13 * this.livingTicks + this.angleOffset));
                    this.setDeltaMovement(ownerPos.x + pos.x() - this.getX(), ownerPos.y + this.getOwner().getBbHeight() * 0.25 - this.getY(), ownerPos.z + pos.z() - this.getZ());
                    this.hasImpulse = true;
                    this.checkedEntities.clear();
                } else if (this.circleTime == 0) {
                    if (owner instanceof Mob mob && mob.getTarget() != null) {
                        this.shootAtEntity(mob.getTarget(), 1, 0);
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
        if (CombatUtils.damageWithFaintAndCrit(this.getOwner(), result.getEntity(), new DynamicDamage.Builder(this, this.getOwner()).noKnockback().hurtResistant(4).projectile(), CombatUtils.getAttributeValue(this.getOwner(), Attributes.ATTACK_DAMAGE) * this.damageMultiplier, null)) {
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
        if (this.attackedEntities.isEmpty()) {
            OrientedBoundingBox obb = new OrientedBoundingBox(new AABB(-this.radius(), -this.radius(), -this.radius(), this.radius(), this.radius(), this.radius())
                    .expandTowards(to.subtract(from)),
                    0, 0, this.position());
            List<Entity> list = this.level().getEntities(this, obb.getEncompassingBox());
            for (Entity e : list) {
                if (this.canHit(e) && obb.intersects(e.getBoundingBox())) {
                    AABB outer = obb.getEncompassingBox();
                    Vec3 hit = new Vec3(Mth.clamp(e.position().x, outer.minX, outer.maxX),
                            Mth.clamp(e.position().y, outer.minY, outer.maxY),
                            Mth.clamp(e.position().z, outer.minZ, outer.maxZ));
                    return new EntityHitResult(e, hit);
                }
            }
            return null;
        }
        return null;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.circling = compound.getBoolean("Circling");
        this.circleTime = compound.getInt("CirclingTime");
        this.angleOffset = compound.getInt("AngleOffset");
        this.withSizeInc(compound.contains("Scale") ? compound.getFloat("Scale") : 1);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Circling", this.circling);
        compound.putInt("CirclingTime", this.circleTime);
        compound.putInt("AngleOffset", this.angleOffset);
        compound.putFloat("Scale", this.entityData.get(SCALE));
    }
}
