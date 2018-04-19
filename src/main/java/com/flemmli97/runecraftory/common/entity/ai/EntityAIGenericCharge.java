package com.flemmli97.runecraftory.common.entity.ai;

import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.entity.IChargeAttack;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

public class EntityAIGenericCharge<T extends EntityMobBase & IChargeAttack> extends EntityAIGenericMelee{

	private int chargeDelay;
	public EntityAIGenericCharge(T creature, double speedIn, boolean useLongMemory, float rangeModifier) {
		super(creature, speedIn, useLongMemory, rangeModifier);
	}

	@Override
	public void updateTask() {
        EntityLivingBase target = this.attacker.getAttackTarget();
        IChargeAttack entity = (IChargeAttack) this.attacker;
        this.chargeDelay=Math.max(chargeDelay--, 0);
        this.attacker.getLookHelper().setLookPositionWithEntity(target, 30.0F, 30.0F);
        double dis = this.attacker.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
		if(this.chargeDelay <= 0 && dis >=25  && this.attacker.getRNG().nextFloat()<0.4F && !entity.isCharging())
		{
        	entity.setCharging(true);
			Vec3d moveVec = this.attacker.getPositionVector().add(target.getPositionVector().subtract(this.attacker.getPositionVector()).normalize().scale(8));
	        this.attacker.getLookHelper().setLookPosition(moveVec.x, moveVec.y, moveVec.z, 30.0F, 30.0F);
			this.attacker.getNavigator().tryMoveToXYZ(moveVec.x, moveVec.y, moveVec.z, this.speedTowardsTarget*1.5);
		}
		else
			this.chargeDelay=40;
		if(!entity.isCharging())
		{
			super.updateTask();
		}
		else if(dis<=this.getAttackReachSqr(target))
		{
			this.attacker.attackEntityAsMob(target);
			entity.setCharging(false);
			this.attacker.getNavigator().clearPath();
		}
		else if(this.attacker.getNavigator().noPath())
			entity.setCharging(false);
	}

}
