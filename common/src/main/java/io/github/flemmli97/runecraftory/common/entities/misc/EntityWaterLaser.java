package io.github.flemmli97.runecraftory.common.entities.misc;

import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.tenshilib.common.entity.EntityBeam;
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
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public class EntityWaterLaser extends EntityBeam {

    private static final EntityDataAccessor<Float> yawMotionVal = SynchedEntityData.defineId(EntityWaterLaser.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> maxLivingTick = SynchedEntityData.defineId(EntityWaterLaser.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> yawOffset = SynchedEntityData.defineId(EntityWaterLaser.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> positionYawOffset = SynchedEntityData.defineId(EntityWaterLaser.class, EntityDataSerializers.FLOAT);

    private float damageMultiplier = 1;

    private Predicate<LivingEntity> pred;

    public EntityWaterLaser(EntityType<? extends EntityBeam> type, Level level) {
        super(type, level);
    }

    public EntityWaterLaser(Level level, LivingEntity shooter) {
        super(ModEntities.waterLaser.get(), level, shooter);
    }

    public EntityWaterLaser(Level level, LivingEntity shooter, float yawMotion) {
        super(ModEntities.waterLaser.get(), level, shooter);
        this.entityData.set(yawMotionVal, yawMotion);
        if (shooter instanceof BaseMonster)
            this.pred = ((BaseMonster) shooter).hitPred;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(yawMotionVal, 0f);
        this.entityData.define(maxLivingTick, 20);
        this.entityData.define(yawOffset, 0f);
        this.entityData.define(positionYawOffset, 0f);
    }

    public void setRotationToDirWithOffset(double dirX, double dirY, double dirZ, float acc, float yawOffset) {
        super.setRotationToDir(dirX, dirY, dirZ, acc);
        this.setYRot(this.getYRot() + yawOffset);
    }

    public EntityWaterLaser setMaxTicks(int ticks) {
        this.entityData.set(maxLivingTick, ticks);
        return this;
    }

    public void setYawOffset(float offset) {
        this.entityData.set(yawOffset, offset);
    }

    public void setPositionYawOffset(float offset) {
        this.entityData.set(positionYawOffset, offset);
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
    public float getRange() {
        return 9;
    }

    @Override
    public boolean getHitVecFromShooter() {
        return this.getOwner() instanceof Player;
    }

    @Override
    public int livingTickMax() {
        return this.entityData.get(maxLivingTick);
    }

    @Override
    public int attackCooldown() {
        return 19;
    }

    @Override
    public void updateYawPitch() {
        if (this.getHitVecFromShooter() && this.getOwner() != null) {
            Entity e = this.getOwner();
            this.setXRot(e.getXRot());
            this.setYRot(e.getYRot() + this.entityData.get(yawOffset));
            this.xRotO = e.xRotO;
            this.yRotO = e.yRotO + this.entityData.get(yawOffset);
            Vector3f vec = RayTraceUtils.rotatedAround(e.getLookAngle(), Vector3f.YP, this.entityData.get(positionYawOffset));
            this.setPos(e.getX() + vec.x(), e.getY() + (double) e.getEyeHeight() - 0.10000000149011612D + vec.y(), e.getZ() + vec.z());
        }
    }

    @Override
    public void tick() {
        if (this.entityData.get(yawMotionVal) != 0) {
            this.setYRot(this.getYRot() + this.entityData.get(yawMotionVal));
            this.hit = null;
        }
        super.tick();
    }

    @Override
    protected boolean check(Entity e, Vec3 from, Vec3 to) {
        return (!(e instanceof LivingEntity) || this.pred == null || this.pred.test((LivingEntity) e)) && super.check(e, from, to);
    }

    @Override
    public void onImpact(EntityHitResult res) {
        Entity e = res.getEntity();
        CombatUtils.damage(this.getOwner(), e, new CustomDamage.Builder(this, this.getOwner()).hurtResistant(5).element(EnumElement.WATER).get(), CombatUtils.getAttributeValueRaw(this.getOwner(), ModAttributes.RF_MAGIC.get()) * this.damageMultiplier, null);
    }

    public void setDamageMultiplier(float damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.damageMultiplier = compound.getFloat("DamageMultiplier");
        this.entityData.set(yawOffset, compound.getFloat("YawOffset"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putFloat("DamageMultiplier", this.damageMultiplier);
        compound.putFloat("YawOffset", this.entityData.get(yawOffset));
    }

    @Override
    public Entity getOwner() {
        Entity owner = super.getOwner();
        if (owner instanceof BaseMonster)
            this.pred = ((BaseMonster) owner).hitPred;
        return owner;
    }
}
