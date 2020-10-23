package com.flemmli97.runecraftory.mobs.entity;

import com.flemmli97.runecraftory.mobs.network.S2CAttackDebug;
import com.flemmli97.runecraftory.network.PacketHandler;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;

public abstract class ChargingMonster extends BaseMonster {

    private List<LivingEntity> hitEntity;
    private double[] chargeMotion;

    public ChargingMonster(EntityType<? extends ChargingMonster> type, World world) {
        super(type, world);
    }

    public void setChargeMotion(double[] chargeMotion) {
        this.chargeMotion = chargeMotion;
    }

    @Override
    public void livingTick() {
        super.livingTick();
        AnimatedAction anim = this.getAnimation();
        if (anim != null && this.isAnimOfType(anim, AnimationType.CHARGE)) {
            this.rotationPitch = 0;
            this.rotationYaw = (float) (MathHelper.atan2(this.getMotion().x, -this.getMotion().z) * (180D / Math.PI)) + 180;
        }
    }

    @Override
    public void applyEntityCollision(Entity entity) {
        if (this.getAnimation() != null && this.isAnimOfType(this.getAnimation(), AnimationType.CHARGE))
            return;
        super.applyEntityCollision(entity);
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (this.isAnimOfType(anim, AnimationType.CHARGE)) {
            if (this.chargeMotion == null)
                return;
            this.getNavigator().clearPath();
            if (anim.getTick() > anim.getAttackTime()) {
                this.setMotion(this.chargeMotion[0], this.getMotion().y, this.chargeMotion[2]);
                if (this.hitEntity == null)
                    this.hitEntity = Lists.newArrayList();
                this.mobAttack(anim, null, e -> {
                    if (!this.hitEntity.contains(e)) {
                        this.attackEntityAsMob(e);
                        this.hitEntity.add(e);
                    }
                });
            }
        } else
            super.handleAttack(anim);
    }

    @Override
    public void setAnimation(AnimatedAction anim) {
        if (!this.world.isRemote) {
            if (this.getAnimation() != null && this.isAnimOfType(this.getAnimation(), AnimationType.CHARGE))
                this.hitEntity = null;
        }
        super.setAnimation(anim);
    }

    public float chargingLength() {
        return 6;
    }
}