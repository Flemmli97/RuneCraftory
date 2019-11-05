package com.flemmli97.runecraftory.common.entity.ai;

import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.entity.EntityMobBase.AnimationType;
import com.flemmli97.runecraftory.common.entity.IRangedMob;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class EntityAIGenericRanged<T extends EntityMobBase & IRangedMob> extends EntityAIBase
{
    protected T attacker;
    double moveSpeedAmp;
    float maxAttackDistance;
    float melee;
    private int attackTick;
    private int seeTime;
    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private int strafingTime;
    
    public EntityAIGenericRanged(T entity, double speed, float range, float meleeReachModifier) {
        this.strafingTime = -1;
        this.attacker = entity;
        this.moveSpeedAmp = speed;
        this.maxAttackDistance = range * range;
        this.melee = meleeReachModifier;
        this.setMutexBits(3);
    }
    
    @Override
	public boolean shouldExecute() {
        return this.attacker.getAttackTarget() != null && this.attacker.isEntityAlive();
    }
    
    @Override
	public boolean shouldContinueExecuting() {
        EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
        return entitylivingbase != null && entitylivingbase.isEntityAlive() && !this.attacker.getNavigator().noPath() && this.attacker.isWithinHomeDistanceFromPosition(new BlockPos(entitylivingbase)) && this.isTargetInsideHome(entitylivingbase);
    }
    
    private boolean isTargetInsideHome(EntityLivingBase target) {
        return this.attacker.getMaximumHomeDistance() == -1.0f || target.getPosition().distanceSq((Vec3i)this.attacker.getHomePosition()) < Math.max(this.attacker.getMaximumHomeDistance(), this.maxAttackDistance) * Math.max(this.attacker.getMaximumHomeDistance(), this.maxAttackDistance);
    }
    
    @Override
	public void startExecuting() {
        super.startExecuting();
    }
    
    @Override
	public void resetTask() {
        super.resetTask();
    }
    
    @Override
	public void updateTask() {
        EntityLivingBase target = this.attacker.getAttackTarget();
        double dis = this.attacker.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
        boolean canSee = this.attacker.getEntitySenses().canSee(target);
        AnimatedAction anim = this.attacker.getAnimation();       
        //Attack
        double melee = this.getAttackReachSqr(target);
        if(anim==null)
        	--this.attackTick;
        if (dis <= melee)
        {
        	if(anim==null)
        	{
        		if(this.attackTick<=0)
        		{
        			if (this.attacker.getRNG().nextFloat() < this.attacker.attackChance()) 
        			{
		        		anim = this.attacker.getRandomAnimation(AnimationType.MELEE);
		        		this.attacker.setAnimation(anim);
		        		this.attackTick=this.attacker.animationCooldown(anim);
        			}
        			else 
        			{
	                    this.attackTick = 40;
	                }
        		}
        	}
        }
        else if(dis <= this.maxAttackDistance && canSee)
        {
        	if(anim==null)
        	{
        		if(this.attackTick<=0)
        		{
        			if (this.attacker.getRNG().nextFloat() < this.attacker.attackChance()) 
        			{
        				anim = this.attacker.getRandomAnimation(AnimationType.RANGED);
		        		this.attacker.setAnimation(anim);
		        		this.attackTick=this.attacker.animationCooldown(anim);
		        		this.attacker.motionX=0;
		        		this.attacker.motionZ=0;
		        		this.attacker.getNavigator().clearPath();
        			}
        			else 
        			{
	                    this.attackTick = 40;
	                }
        		}
        	}
        	else if(anim!=null && this.attacker.isAnimOfType(anim, AnimationType.RANGED))
        	{
        		this.attacker.getNavigator().clearPath();
	        	if(anim.canAttack())
	                ((IRangedMob)this.attacker).attackRanged(target);
        	}
        }
        if(anim!=null && this.attacker.isAnimOfType(anim, AnimationType.MELEE))
        {
    		this.attacker.getNavigator().clearPath();
	        if (dis <= melee*3 && anim.canAttack())
	        {
	        	this.attacker.attackEntityAsMob(target);
	        }
        }

        if(anim==null)
        {
            boolean flag1 = this.seeTime > 0;
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
	        if (dis <= this.maxAttackDistance && this.seeTime >= 20) 
	        {
	            this.attacker.getNavigator().clearPath();
	            ++this.strafingTime;
	        }
	        else 
	        {
	            this.attacker.getNavigator().tryMoveToEntityLiving(target, this.moveSpeedAmp);
	            this.strafingTime = -1;
	            this.attacker.getLookHelper().setLookPositionWithEntity(target, 30.0f, 30.0f);
	        }
	        if (this.strafingTime >= 20) 
	        {
	            if (this.attacker.getRNG().nextFloat() < 0.3) 
	            {
	                this.strafingClockwise = !this.strafingClockwise;
	            }
	            if (this.attacker.getRNG().nextFloat() < 0.3) 
	            {
	                this.strafingBackwards = !this.strafingBackwards;
	            }
	            this.strafingTime = 0;
	        }
	        if (this.strafingTime > -1) 
	        {
	            if (dis > this.maxAttackDistance * 0.75f) 
	            {
	                this.strafingBackwards = false;
	            }
	            else if (dis < this.maxAttackDistance * 0.25f) 
	            {
	                this.strafingBackwards = true;
	            }
	            this.attacker.getMoveHelper().strafe(this.strafingBackwards ? -0.5f : 0.5f, this.strafingClockwise ? 0.5f : -0.5f);
	            this.attacker.faceEntity(target, 30.0f, 30.0f);
	        }
	        else 
	        {
	            this.attacker.getLookHelper().setLookPositionWithEntity(target, 30.0f, 30.0f);
	        }
        }
    }
   
    protected double getAttackReachSqr(EntityLivingBase attackTarget) {
        return this.melee * (this.attacker.width * 2.0f * this.attacker.width * 2.0f + attackTarget.width);
    }
}
