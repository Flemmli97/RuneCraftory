package io.github.flemmli97.runecraftory.common.entities.misc;

import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.tenshilib.common.utils.RayTraceUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class EntityWaterLaser extends BaseBeam {

    private static final EntityDataAccessor<Float> YAW_MOTION_VAL = SynchedEntityData.defineId(EntityWaterLaser.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> MAX_LIVING_TICK = SynchedEntityData.defineId(EntityWaterLaser.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> YAW_OFFSET = SynchedEntityData.defineId(EntityWaterLaser.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> POSITION_YAW_OFFSET = SynchedEntityData.defineId(EntityWaterLaser.class, EntityDataSerializers.FLOAT);

    public EntityWaterLaser(EntityType<? extends EntityWaterLaser> type, Level level) {
        super(type, level);
    }

    public EntityWaterLaser(Level level, LivingEntity shooter) {
        super(ModEntities.WATER_LASER.get(), level, shooter);
    }

    public EntityWaterLaser(Level level, LivingEntity shooter, float yawMotion) {
        super(ModEntities.WATER_LASER.get(), level, shooter);
        this.entityData.set(YAW_MOTION_VAL, yawMotion);
    }

    public void setRotationToDirWithOffset(double dirX, double dirY, double dirZ, float acc, float yawOffset) {
        super.setRotationToDir(dirX, dirY, dirZ, acc);
        this.setYRot(this.getYRot() + yawOffset);
    }

    public EntityWaterLaser setMaxTicks(int ticks) {
        this.entityData.set(MAX_LIVING_TICK, ticks);
        return this;
    }

    public void setYawOffset(float offset) {
        this.entityData.set(YAW_OFFSET, offset);
    }

    public void setPositionYawOffset(float offset) {
        this.entityData.set(POSITION_YAW_OFFSET, offset);
    }

    @Override
    public float getRange() {
        return 9;
    }

    @Override
    public float radius() {
        return 0.5f;
    }

    @Override
    public boolean piercing() {
        return true;
    }

    @Override
    public int livingTickMax() {
        return this.entityData.get(MAX_LIVING_TICK);
    }

    @Override
    public void updateYawPitch() {
        if (this.getHitVecFromShooter() && this.getOwner() != null) {
            Entity e = this.getOwner();
            this.setXRot(e.getXRot());
            this.setYRot(e.getYRot() + this.entityData.get(YAW_OFFSET));
            this.xRotO = e.xRotO;
            this.yRotO = e.yRotO + this.entityData.get(YAW_OFFSET);
            Vector3f vec = RayTraceUtils.rotatedAround(e.getLookAngle(), Vector3f.YP, this.entityData.get(POSITION_YAW_OFFSET));
            this.setPos(e.getX() + vec.x(), e.getY() + (double) e.getEyeHeight() - 0.10000000149011612D + vec.y(), e.getZ() + vec.z());
        }
    }

    @Override
    public boolean getHitVecFromShooter() {
        return this.getOwner() instanceof Player;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(YAW_MOTION_VAL, 0f);
        this.entityData.define(MAX_LIVING_TICK, 20);
        this.entityData.define(YAW_OFFSET, 0f);
        this.entityData.define(POSITION_YAW_OFFSET, 0f);
    }

    @Override
    public void tick() {
        if (this.entityData.get(YAW_MOTION_VAL) != 0) {
            this.setYRot(this.getYRot() + this.entityData.get(YAW_MOTION_VAL));
            this.hit = null;
        }
        super.tick();
    }

    @Override
    public void onImpact(EntityHitResult res) {
        Entity e = res.getEntity();
        CombatUtils.damage(this.getOwner(), e, new CustomDamage.Builder(this, this.getOwner()).hurtResistant(5).element(EnumElement.WATER), true, false, CombatUtils.getAttributeValue(this.getOwner(), ModAttributes.MAGIC.get()) * this.damageMultiplier, null);
    }

    @Override
    public int attackCooldown() {
        return 19;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(YAW_OFFSET, compound.getFloat("YawOffset"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putFloat("YawOffset", this.entityData.get(YAW_OFFSET));
    }
}
