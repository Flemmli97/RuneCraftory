package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.tenshilib.common.entity.EntityUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.UUID;

public class EntityButterfly extends BaseProjectile {

    protected static final EntityDataAccessor<Float> LOCKED_YAW = SynchedEntityData.defineId(EntityButterfly.class, EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Float> LOCKED_PITCH = SynchedEntityData.defineId(EntityButterfly.class, EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Optional<UUID>> HIT = SynchedEntityData.defineId(EntityButterfly.class, EntityDataSerializers.OPTIONAL_UUID);
    protected static final EntityDataAccessor<Float> HIT_X = SynchedEntityData.defineId(EntityButterfly.class, EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Float> HIT_Y = SynchedEntityData.defineId(EntityButterfly.class, EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Float> HIT_Z = SynchedEntityData.defineId(EntityButterfly.class, EntityDataSerializers.FLOAT);
    private static final int DEFAULT_MAX_TICK = 50;

    private LivingEntity stuckEntity;
    private int livingTickMax = DEFAULT_MAX_TICK;

    public EntityButterfly(EntityType<? extends EntityButterfly> type, Level level) {
        super(type, level);
        this.damageMultiplier = 0.15f;
    }

    public EntityButterfly(Level level, LivingEntity thrower) {
        super(ModEntities.BUTTERFLY.get(), level, thrower);
        this.damageMultiplier = 0.15f;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(LOCKED_YAW, 0f);
        this.entityData.define(LOCKED_PITCH, 0f);
        this.entityData.define(HIT, Optional.empty());
        this.entityData.define(HIT_X, 0f);
        this.entityData.define(HIT_Y, 0f);
        this.entityData.define(HIT_Z, 0f);
    }

    @Override
    public boolean isPiercing() {
        return false;
    }

    @Override
    public int livingTickMax() {
        return this.livingTickMax;
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity stuck = this.getHitEntity();
        if (stuck != null) {
            this.setXRot(this.entityData.get(LOCKED_PITCH));
            this.setYRot(this.entityData.get(LOCKED_YAW));
            Vec3 pos = stuck.position().add(this.entityData.get(HIT_X), this.entityData.get(HIT_Y), this.entityData.get(HIT_Z));
            this.setPos(pos);
            if (!this.level.isClientSide && this.livingTicks % 30 == 0 && this.getOwner() != null) {
                if (this.getOwner() instanceof LivingEntity living)
                    CombatUtils.applyTempAttribute(living, ModAttributes.DRAIN.get(), 80);
                CustomDamage.Builder builder = new CustomDamage.Builder(this, this.getOwner()).magic().noKnockback().hurtResistant(0);
                stuck.hurt(builder.get(), (float) (CombatUtils.getAttributeValue(this.getOwner(), ModAttributes.MAGIC.get()) * this.damageMultiplier * 0.8));
                if (this.getOwner() instanceof LivingEntity living)
                    CombatUtils.removeTempAttribute(living, ModAttributes.DRAIN.get());
            }
        }
    }

    @Override
    protected float getGravityVelocity() {
        return 0.0f;
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult result) {
        if (CombatUtils.damageWithFaintAndCrit(this.getOwner(), result.getEntity(), new CustomDamage.Builder(this, this.getOwner()).magic().noKnockback().hurtResistant(5), CombatUtils.getAttributeValue(this.getOwner(), ModAttributes.MAGIC.get()) * this.damageMultiplier, null)) {
            if (result.getEntity() instanceof LivingEntity livingTarget) {
                this.hitEntity(livingTarget);
            } else {
                this.discard();
            }
            return true;
        }
        return false;
    }

    private void hitEntity(LivingEntity target) {
        target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 3));
        this.livingTickMax += 60;
        this.entityData.set(LOCKED_PITCH, this.getXRot());
        this.entityData.set(LOCKED_YAW, this.getYRot());
        this.entityData.set(HIT, Optional.of(target.getUUID()));
        Vec3 dir = this.position().add(this.getDeltaMovement().scale(1.5)).subtract(target.position()).scale(0.98);
        this.entityData.set(HIT_X, (float) dir.x());
        this.entityData.set(HIT_Y, (float) dir.y());
        this.entityData.set(HIT_Z, (float) dir.z());
    }

    @Override
    protected void onBlockHit(BlockHitResult blockRayTraceResult) {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(LOCKED_PITCH, compound.getFloat("LockedPitch"));
        this.entityData.set(LOCKED_YAW, compound.getFloat("LockedYaw"));
        if (compound.hasUUID("HitEntity"))
            this.entityData.set(HIT, Optional.of(compound.getUUID("HitEntity")));
        this.entityData.set(HIT_X, compound.getFloat("HitX"));
        this.entityData.set(HIT_Y, compound.getFloat("HitY"));
        this.entityData.set(HIT_Z, compound.getFloat("HitZ"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putFloat("LockedPitch", this.entityData.get(LOCKED_PITCH));
        compound.putFloat("LockedYaw", this.entityData.get(LOCKED_YAW));
        this.entityData.get(HIT).ifPresent(id -> compound.putUUID("HitEntity", id));
        compound.putFloat("HitX", this.entityData.get(HIT_X));
        compound.putFloat("HitY", this.entityData.get(HIT_Y));
        compound.putFloat("HitZ", this.entityData.get(HIT_Z));
    }

    public LivingEntity getHitEntity() {
        if (this.stuckEntity != null && !this.stuckEntity.isRemoved()) {
            return this.stuckEntity;
        }
        this.entityData.get(HIT).ifPresent(uuid -> {
            this.stuckEntity = EntityUtil.findFromUUID(LivingEntity.class, this.level, uuid);
            this.onUpdateOwner();
        });
        return this.stuckEntity;
    }
}
