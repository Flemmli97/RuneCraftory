package com.flemmli97.runecraftory.common.entity.ai;

import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.entity.IRangedMob;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class EntityAIGenericRanged<T extends EntityMobBase & IRangedMob> extends EntityAIBase
{
    protected T attacker;
    double moveSpeedAmp;

    float maxAttackDistance, melee;

    private int attackTick, meleeTick;
    private int seeTime;
    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private int strafingTime = -1;

    public EntityAIGenericRanged(T entity, double speed, float range, float meleeReachModifier)
    {
        this.attacker = entity;
        this.moveSpeedAmp = speed;
        this.maxAttackDistance = range * range;
        this.melee=meleeReachModifier;
        this.setMutexBits(3);
    }

    public boolean shouldExecute()
    {
        return (this.attacker.getAttackTarget() == null || !this.attacker.isEntityAlive()) ? false : true;
    }

    public boolean shouldContinueExecuting()
    {        
        EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
        if (entitylivingbase == null || !entitylivingbase.isEntityAlive() || this.attacker.getNavigator().noPath())
        {
            return false;
        }
        else if (!this.attacker.isWithinHomeDistanceFromPosition(new BlockPos(entitylivingbase)))
        {
            return false;
        }
        else if(!this.isTargetInsideHome(entitylivingbase))
        	return false;
        return true;    
    }
    
    private boolean isTargetInsideHome(EntityLivingBase target)
    {
    	 if (this.attacker.getMaximumHomeDistance() == -1.0F)
         {
             return true;
         }
    	 else
         {
             return target.getPosition().distanceSq(this.attacker.getHomePosition())<
            		 (Math.max(this.attacker.getMaximumHomeDistance(),this.maxAttackDistance)*Math.max(this.attacker.getMaximumHomeDistance(),this.maxAttackDistance));
         }
    }

    public void startExecuting()
    {
        super.startExecuting();
    }

    public void resetTask()
    {
        super.resetTask();
    }

    public void updateTask()
    {
    		EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();

            double dis = this.attacker.getDistanceSq(entitylivingbase.posX, entitylivingbase.getEntityBoundingBox().minY, entitylivingbase.posZ);
            boolean canSee = this.attacker.getEntitySenses().canSee(entitylivingbase);
            boolean flag1 = this.seeTime > 0;
            this.checkAndPerformAttack(entitylivingbase, dis);       
            if(this.meleeTick>0)
            {
            		this.attacker.motionX=0;
            		this.attacker.motionY=0;
            		this.attacker.motionZ=0;
            		this.attacker.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0F, 30.0F);
            		return;
            }
            if (canSee != flag1)
            {
                this.seeTime = 0;
            }

            if (canSee)
            {
                ++this.seeTime;
            }
            else
            {
                --this.seeTime;
            }

            if (dis <= (double)this.maxAttackDistance && this.seeTime >= 20)
            {
                this.attacker.getNavigator().clearPath();
                ++this.strafingTime;
            }
            else
            {
                this.attacker.getNavigator().tryMoveToEntityLiving(entitylivingbase, this.moveSpeedAmp);
                this.strafingTime = -1;
                this.attacker.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0F, 30.0F);

            }

            if (this.strafingTime >= 20)
            {
                if ((double)this.attacker.getRNG().nextFloat() < 0.3D)
                {
                    this.strafingClockwise = !this.strafingClockwise;
                }

                if ((double)this.attacker.getRNG().nextFloat() < 0.3D)
                {
                    this.strafingBackwards = !this.strafingBackwards;
                }

                this.strafingTime = 0;
            }

            if (this.strafingTime > -1)
            {
                if (dis > (double)(this.maxAttackDistance * 0.75F))
                {
                    this.strafingBackwards = false;
                }
                else if (dis < (double)(this.maxAttackDistance * 0.25F))
                {
                    this.strafingBackwards = true;
                }

                this.attacker.getMoveHelper().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                this.attacker.faceEntity(entitylivingbase, 30.0F, 30.0F);
            }
            else
            {
                this.attacker.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0F, 30.0F);
            }
    }
    
    protected void checkAndPerformAttack(EntityLivingBase target, double dis)
    {
        this.attackTick = Math.max(this.attackTick - 1, 0);
        this.meleeTick = Math.max(this.meleeTick - 1, 0);

        double reach = this.maxAttackDistance;
        double melee = this.getAttackReachSqr(target);
        if(dis<=melee)
        {
	    		if(this.meleeTick <= 0)
	        		if(this.attacker.getRNG().nextFloat()<this.attacker.attackChance())
	        		{
		            this.attacker.swingArm(EnumHand.MAIN_HAND);
		            int i = this.attacker.maxAttackPatterns()-1;
		            this.attacker.setAttackTimeAndPattern((byte) this.attacker.getRNG().nextInt(i<=0?1:i));
		            this.meleeTick=this.attacker.getAttackTime();
	        		}
	        		else
	        			this.meleeTick=40;
        		else if(this.meleeTick==this.attacker.attackFromPattern())
        			this.attacker.attackEntityAsMob(target);
        }
        else if (dis <= reach)
        {
        		if(this.attackTick <= 0)
	        		if(this.attacker.getRNG().nextFloat()<this.attacker.attackChance())
	        		{
		            this.attacker.swingArm(EnumHand.MAIN_HAND);
		            int i = this.attacker.maxAttackPatterns()-1;
		            this.attacker.setAttackTimeAndPattern((byte) this.attacker.getRNG().nextInt(i<=0?1:i));
		            this.attackTick=this.attacker.getAttackTime();
	        		}
	        		else
	        			this.attackTick=40;
        		else if(this.attackTick==this.attacker.attackFromPattern())
        			((IRangedMob)this.attacker).attackRanged(target);
        }
    }
    
    protected double getAttackReachSqr(EntityLivingBase attackTarget)
    {
        return this.melee*(double)(this.attacker.width * 2.0F * this.attacker.width * 2.0F + attackTarget.width);
    }
}