package com.flemmli97.runecraftory.common.entity.ai;

import com.flemmli97.runecraftory.common.entity.EntityChargeable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

public class EntityAIGenericCharge extends EntityAIGenericMelee
{
    private int chargeDelay;
    
    public EntityAIGenericCharge(EntityChargeable creature, double speedIn, boolean useLongMemory, float rangeModifier) {
        super(creature, speedIn, useLongMemory, rangeModifier);
    }
    
    @Override
    public void updateTask() {
        EntityLivingBase target = this.attacker.getAttackTarget();
        EntityChargeable entity = (EntityChargeable)this.attacker;
        this.chargeDelay = Math.max(--this.chargeDelay, 0);
        this.attacker.getLookHelper().setLookPositionWithEntity((Entity)target, 30.0f, 30.0f);
        double dis = this.attacker.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
        if (this.chargeDelay <= 0 && dis >= 25.0 && this.attacker.getRNG().nextFloat() < 0.4f && !entity.isCharging()) {
            entity.setCharging(true);
            Vec3d moveVec = this.attacker.getPositionVector().add(target.getPositionVector().subtract(this.attacker.getPositionVector()).normalize().scale(8.0));
            this.attacker.getLookHelper().setLookPosition(moveVec.x, moveVec.y, moveVec.z, 30.0f, 30.0f);
            this.attacker.getNavigator().tryMoveToXYZ(moveVec.x, moveVec.y, moveVec.z, this.speedTowardsTarget * 1.5);
        }
        else {
            this.chargeDelay = 40;
        }
        if (!entity.isCharging()) {
            super.updateTask();
        }
        else if (dis <= this.getAttackReachSqr(target)) {
            this.attacker.attackEntityAsMob((Entity)target);
            entity.setCharging(false);
            this.attacker.getNavigator().clearPath();
        }
        else if (this.attacker.getNavigator().noPath()) {
            entity.setCharging(false);
        }
    }
}
