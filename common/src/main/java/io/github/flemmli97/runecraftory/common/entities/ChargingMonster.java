package io.github.flemmli97.runecraftory.common.entities;

import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class ChargingMonster extends BaseMonster {

    protected List<LivingEntity> hitEntity;
    protected double[] chargeMotion;
    private float prevStepHeight = -1;
    private static final EntityDataAccessor<Float> lockedYaw = SynchedEntityData.defineId(ChargingMonster.class, EntityDataSerializers.FLOAT);

    private boolean initAnim;
    private final Consumer<AnimatedAction> chargingAnim = anim -> {
        if (!this.level.isClientSide) {
            if (anim != null && this.isAnimOfType(anim, AnimationType.CHARGE)) {
                this.prevStepHeight = this.maxUpStep;
                this.maxUpStep = Math.max(1.5f, 1f + this.maxUpStep);
                if (this.isVehicle()) {
                    this.setChargeMotion(this.getChargeTo(anim, this.position().add(this.getLookAngle())));
                }
            } else if (this.prevStepHeight != -1) {
                this.maxUpStep = this.prevStepHeight;
                this.prevStepHeight = -1;
            }
            if (this.isChargingAnimation()) {
                this.hitEntity = null;
            }
        }
    };

    public ChargingMonster(EntityType<? extends ChargingMonster> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(lockedYaw, 0f);
    }

    public void setChargeMotion(double[] chargeMotion) {
        this.chargeMotion = chargeMotion;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.initAnim) {
            this.getAnimationHandler().setAnimationChangeCons(this.chargingAnim);
            this.initAnim = true;
        }
        if (this.isChargingAnimation()) {
            this.setXRot(0);
            this.setYRot(this.chargingYaw());
        }
    }

    public float chargingYaw() {
        return this.isVehicle() ? this.getYRot() : this.entityData.get(lockedYaw);
    }

    @Override
    public void push(Entity entity) {
        if (this.isChargingAnimation())
            return;
        super.push(entity);
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (this.isAnimOfType(anim, AnimationType.CHARGE)) {
            this.getNavigation().stop();
            if (anim.getTick() > anim.getAttackTime()) {
                this.handleChargeMovement();
                if (!this.handleChargeMovement())
                    return;
                if (this.hitEntity == null)
                    this.hitEntity = new ArrayList<>();
                this.mobAttack(anim, null, e -> {
                    if (!this.hitEntity.contains(e)) {
                        this.hitEntity.add(e);
                        this.doHurtTarget(e);
                    }
                });
                this.doWhileCharge();
            }
        } else {
            super.handleAttack(anim);
        }
    }

    public boolean handleChargeMovement() {
        if (this.chargeMotion != null) {
            this.setDeltaMovement(this.chargeMotion[0], this.getDeltaMovement().y, this.chargeMotion[2]);
            return true;
        }
        return false;
    }

    public void doWhileCharge() {

    }

    @Override
    public boolean adjustRotFromRider(LivingEntity rider) {
        return !this.isChargingAnimation();
    }

    public float chargingLength() {
        return 6;
    }

    public double[] getChargeTo(AnimatedAction anim, Vec3 pos) {
        int length = anim.getLength();
        Vec3 vec = pos.subtract(this.position()).normalize().scale(this.chargingLength());
        return new double[]{vec.x / length, this.getY(), vec.z / length};
    }

    public void lockYaw(float yaw) {
        this.entityData.set(lockedYaw, yaw);
    }

    private boolean isChargingAnimation() {
        AnimatedAction anim = this.getAnimationHandler().getAnimation();
        return anim != null && this.isAnimOfType(anim, AnimationType.CHARGE);
    }
}