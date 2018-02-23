package com.flemmli97.runecraftory.common.entity.ai;

import com.flemmli97.runecraftory.common.entity.EntityMobBase;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public abstract class EntityAIGenericAttack extends EntityAIBase{

    protected EntityMobBase attackingEntity;

    protected int doAttackTime, animationDuration, attackCoolDown, failedPenalty;

    protected double speedTowardsTarget;

    /** The PathEntity of our entity. */
    protected Path entityPathEntity;
	protected boolean evade;
    
    protected int moveDelay;
    protected double posX,posY,posZ,rangeModifier;
	
    /**doAttackTime is the time during animationDuration, when the entity should deal damage. Should be smaller than animationduration;
     * duration is in tick (20 tick = 1 sec); minecraft mob default attack speed is 20 tick;
     * default rangeModifier should be 1;
     * for ranged=true --> rangeModifier = range*/
	public EntityAIGenericAttack(EntityMobBase selectedEntity,boolean ranged, double speedToTarget, double rangeModifier) 
	{
		this.attackingEntity = selectedEntity;
		this.speedTowardsTarget = speedToTarget;  

        selectedEntity.attackTimerValue=animationDuration;
        this.setMutexBits(3);
        this.rangeModifier = rangeModifier*rangeModifier; 
	}

	@Override
	public boolean shouldExecute()
    {
        EntityLivingBase var1 = this.attackingEntity.getAttackTarget();

        if (var1 == null)
        {
            return false;
        }
        else if(!var1.isEntityAlive())
        {
            return false;
        }
        else if(var1.isEntityInvulnerable(DamageSource.causeMobDamage(attackingEntity)))
        {
        		return false;
        }
        else
        {
            this.entityPathEntity = this.attackingEntity.getNavigator().getPathToEntityLiving(var1);
            return this.entityPathEntity != null;
        }
    }
	
	@Override
	public boolean shouldContinueExecuting()
    {
        EntityLivingBase var1 = this.attackingEntity.getAttackTarget();
        return var1 == null ? false : (!var1.isEntityAlive() ? false : true);
    }
	
	@Override
	public void startExecuting()
    {
        this.attackingEntity.getNavigator().setPath(this.entityPathEntity, this.speedTowardsTarget);
        this.moveDelay = 0;
    }
	
	@Override
	public void resetTask()
    {
        this.attackingEntity.getNavigator().clearPath();
    }
	
	@Override
	public void updateTask()
    {
		this.moveTowards();
		this.meleeAttack(); 
    }
	
	//attacking pattern
	public void meleeAttack()
	{
		EntityLivingBase target = this.attackingEntity.getAttackTarget();
        double distanceToTarget = this.attackingEntity.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
        double attackRange = rangeModifier*(double)(this.attackingEntity.width * 2.0F * this.attackingEntity.width * 2.0F + target.width);

        if (distanceToTarget <= attackRange)
        {
	        	this.attackingEntity.getNavigator().clearPath();
	        	if(this.attackCoolDown == 0)
	        	{
	        		this.attackCoolDown = this.animationDuration;
	        		attackingEntity.attackTimerValue = animationDuration;
	        		attackingEntity.world.setEntityState(attackingEntity, (byte)4);
	        	}
	        	else
            {
	        		attackCoolDown --;        		
	            	if(this.attackCoolDown == this.doAttackTime)
	            	{
	            		this.attackingEntity.attackEntityAsMob(target);
	            	}       		
            }
        }
        else
		{
			this.attackCoolDown = 0;
		} 
	}
	
	public void rangedAttack()
	{
		EntityLivingBase target = this.attackingEntity.getAttackTarget();
		boolean canSee = this.attackingEntity.getEntitySenses().canSee(target);
        double distanceToTarget = this.attackingEntity.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
        if (distanceToTarget <= rangeModifier && canSee)
        {
         	if(this.attackCoolDown == 0)
         	{
         		this.attackCoolDown = this.animationDuration;
         		attackingEntity.attackTimerValue = animationDuration;
         		attackingEntity.world.setEntityState(attackingEntity, (byte)4);
         	}
         	else
             {
         		attackCoolDown --;
         		
             	if(this.attackCoolDown == this.doAttackTime)
             	{
             		attackingEntity.doRangedAttack(target);
             	}       		
             }
         }
         else
 		{
 			this.attackCoolDown = 0;
 		}
	}
	
	//movement pattern
	public void moveTowards()
	{
		EntityLivingBase target = this.attackingEntity.getAttackTarget();
        this.attackingEntity.getLookHelper().setLookPositionWithEntity(target, 30.0F, 30.0F);
        --this.moveDelay;

        if ((this.attackingEntity.getEntitySenses().canSee(target)) && this.moveDelay <= 0 && (this.posX == 0.0D && this.posY == 0.0D && this.posZ == 0.0D || this.attackingEntity.getRNG().nextFloat() < 0.05F))
        {
            this.posX = target.posX;
            this.posY = target.getEntityBoundingBox().minY;
            this.posZ = target.posZ;
            this.moveDelay = this.attackingEntity.getRNG().nextInt(5);

            if (!this.attackingEntity.getNavigator().tryMoveToEntityLiving(target, this.speedTowardsTarget))
            {
                this.moveDelay += 15;
            }
        } 
	}
	public void strafe()
	{
		
	}
	public void standAtDistance()
	{
		
	}
}
