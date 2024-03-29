package io.github.flemmli97.runecraftory.common.entities;


import com.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class ChargingMonster extends BaseMonster {

    protected List<LivingEntity> hitEntity;
    protected double[] chargeMotion;
    private float prevStepHeight = -1;
    private static final DataParameter<Float> lockedYaw = EntityDataManager.createKey(ChargingMonster.class, DataSerializers.FLOAT);

    private final Function<AnimatedAction, Boolean> chargingAnim = anim -> {
        if (!this.world.isRemote) {
            if (anim != null && this.isAnimOfType(anim, AnimationType.CHARGE)) {
                this.prevStepHeight = this.stepHeight;
                this.stepHeight = Math.max(1.5f, 1f + this.stepHeight);
                if (this.isBeingRidden()) {
                    this.setChargeMotion(this.getChargeTo(anim, this.getPositionVec().add(this.getLookVec())));
                }
            } else if (this.prevStepHeight != -1) {
                this.stepHeight = this.prevStepHeight;
                this.prevStepHeight = -1;
            }
            if (this.getAnimationHandler().getAnimation().map(a -> this.isAnimOfType(a, AnimationType.CHARGE)).orElse(false)) {
                this.hitEntity = null;
            }
        }
        return false;
    };

    public ChargingMonster(EntityType<? extends ChargingMonster> type, World world) {
        super(type, world);
        this.getAnimationHandler().setAnimationChangeFunc(this.chargingAnim);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(lockedYaw, 0f);
    }

    public void setChargeMotion(double[] chargeMotion) {
        this.chargeMotion = chargeMotion;
    }

    @Override
    public void livingTick() {
        super.livingTick();
        if (this.getAnimationHandler().getAnimation().map(anim -> this.isAnimOfType(anim, AnimationType.CHARGE)).orElse(false)) {
            this.rotationPitch = 0;
            this.rotationYaw = this.chargingYaw();
        }
    }

    public float chargingYaw() {
        return this.isBeingRidden() ? this.rotationYaw : this.dataManager.get(lockedYaw);
    }

    @Override
    public void applyEntityCollision(Entity entity) {
        if (this.getAnimationHandler().getAnimation().map(anim -> this.isAnimOfType(anim, AnimationType.CHARGE)).orElse(false))
            return;
        super.applyEntityCollision(entity);
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (this.isAnimOfType(anim, AnimationType.CHARGE)) {
            this.getNavigator().clearPath();
            if (anim.getTick() > anim.getAttackTime()) {
                this.handleChargeMovement();
                if (!this.handleChargeMovement())
                    return;
                if (this.hitEntity == null)
                    this.hitEntity = new ArrayList<>();
                this.mobAttack(anim, null, e -> {
                    if (!this.hitEntity.contains(e)) {
                        this.hitEntity.add(e);
                        this.attackEntityAsMob(e);
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
            this.setMotion(this.chargeMotion[0], this.getMotion().y, this.chargeMotion[2]);
            return true;
        }
        return false;
    }

    public void doWhileCharge() {

    }

    @Override
    public boolean adjustRotFromRider(LivingEntity rider) {
        return this.getAnimationHandler().getAnimation().map(anim -> !this.isAnimOfType(anim, AnimationType.CHARGE)).orElse(true);
    }

    public float chargingLength() {
        return 6;
    }

    public double[] getChargeTo(AnimatedAction anim, Vector3d pos) {
        int length = anim.getLength();
        Vector3d vec = pos.subtract(this.getPositionVec()).normalize().scale(this.chargingLength());
        return new double[]{vec.x / length, this.getPosY(), vec.z / length};
    }

    public void lockYaw(float yaw) {
        this.dataManager.set(lockedYaw, yaw);
    }
}