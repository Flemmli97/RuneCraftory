package com.flemmli97.runecraftory.common.entity.ai;

import java.util.List;

import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.entity.monster.boss.EntityAmbrosia;
import com.flemmli97.runecraftory.common.entity.monster.boss.EntityAmbrosia.AttackAI;
import com.flemmli97.runecraftory.common.lib.enums.EnumElement;
import com.google.common.base.Predicate;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.monster.IMob;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class EntityAIAmbrosia extends EntityAIBase{

	private EntityAmbrosia attacker;
	private int attackTicker, attackDelay, moveDelay;
	private AttackAI prevStatus;
	public EntityAIAmbrosia(EntityAmbrosia entity) {
		this.attacker=entity;
		this.setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
        if (entitylivingbase == null ||!entitylivingbase.isEntityAlive())
        {
            return false;
        }
        return true;
	}
	
	@Override
	public boolean shouldContinueExecuting()
    {        
        EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
        if (entitylivingbase == null || !entitylivingbase.isEntityAlive() || !this.attacker.isWithinHomeDistanceFromPosition(new BlockPos(entitylivingbase)))
        {
            return false;
        }
        return true;    
    }

	@Override
    public void resetTask()
    {
        this.attacker.setStatus(AttackAI.IDDLE);
    }

	@Override
	public void updateTask() {
        EntityLivingBase target = this.attacker.getAttackTarget();
        double dis = this.attacker.getDistanceSq(target);
        this.attackTicker = Math.max(this.attackTicker - 1, 0);
        this.moveDelay = Math.max(this.moveDelay - 1, 0);

        if(this.attacker.getStatus()==AttackAI.IDDLE)
        {
            this.attackDelay = Math.max(this.attackDelay - 1, 0);
            if(this.attackDelay==0 && this.attackTicker==0)
            {
            		
        			int i = this.attacker.isEnraged()?1:2;
        			AttackAI ai = this.randomAIExcept(i);
        			this.attacker.setStatus(ai);
        			this.prevStatus=ai;
        			this.moveDelay=0;
            }
        }
		if(this.attackTicker==0 && this.attackDelay>0)
			this.attacker.setStatus(AttackAI.IDDLE);
        	switch(this.attacker.getStatus())
        	{
			case BUTTERFLY:
				if(dis<25 && this.moveDelay==0 && this.attackTicker==0)
				{
					BlockPos pos = this.randomPosAwayFrom(target, 8);
					this.attacker.getNavigator().tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), 1.5);
					this.moveDelay = 24 + this.attacker.getRNG().nextInt(7);
				}
				else if(this.attacker.getNavigator().noPath() && this.attackTicker==0)
				{
					this.attackTicker=this.attacker.getStatus().getDuration();
				}
				if(this.attackTicker>0)
			        this.attacker.getLookHelper().setLookPositionWithEntity(target, 30.0F, 30.0F);
				if(this.attackTicker==this.attacker.getStatus().getTime())
				{
					this.attacker.summonButterfly();
					this.attackDelay = 64 + this.attacker.getRNG().nextInt(12);
				}
				break;
			case IDDLE:
		        this.attacker.getLookHelper().setLookPositionWithEntity(target, 30.0F, 30.0F);
		        if(dis>64)
	        		{
	        			if(this.moveDelay==0)
	        			{
			        		this.attacker.getNavigator().tryMoveToEntityLiving(target, 1.5);
			            	this.moveDelay = 24 + this.attacker.getRNG().nextInt(7);
	        			}
	        		}
		        else
		        {
			        	Vec3d rand = RandomPositionGenerator.findRandomTarget(this.attacker, 5, 4);
	    				if(rand!=null)
	    				{
	    					this.attacker.getNavigator().tryMoveToXYZ(rand.x, rand.y, rand.z, 1.5);
	    					this.moveDelay = 44 + this.attacker.getRNG().nextInt(7);
	    				}
		        }
	        		//else
	        		//	this.attacker.getNavigator().clearPath();
				break;
			case KICK1:
        			this.attacker.getNavigator().tryMoveToEntityLiving(target, 1);
				if(this.attackTicker==0)
				{
					this.attackTicker=this.attacker.getStatus().getDuration();
				}
				if(this.attackTicker>0)
			        this.attacker.getLookHelper().setLookPositionWithEntity(target, 30.0F, 30.0F);
				if(this.attackTicker!=0 && this.attackTicker % this.attacker.getStatus().getTime()==0)
				{
					if(dis<=this.getAttackReachSqr(target))
						this.attacker.attackEntityAsMob(target);
					if(this.attackTicker==this.attacker.getStatus().getTime())
						this.attackDelay = 64 + this.attacker.getRNG().nextInt(12);
				}
				break;
			case KICK2:
        			this.attacker.getNavigator().tryMoveToEntityLiving(target, 1);
				if(this.attackTicker==0)
				{
					this.attackTicker=this.attacker.getStatus().getDuration();
				}
				if(this.attackTicker>0)
			        this.attacker.getLookHelper().setLookPositionWithEntity(target, 30.0F, 30.0F);
				if(this.attackTicker!=0 && this.attackTicker % this.attacker.getStatus().getTime()==0)
				{
					List<EntityLivingBase> nearby = this.attacker.world.getEntitiesWithinAABB(EntityLivingBase.class, this.attacker.getEntityBoundingBox().grow(2), new Predicate<EntityLivingBase>() {
						@Override
						public boolean apply(EntityLivingBase input) {
							if(!EntityAIAmbrosia.this.attacker.isTamed())
							{
								if(input instanceof EntityMobBase)
									return ((EntityMobBase)input).isTamed();
								return !IMob.VISIBLE_MOB_SELECTOR.apply(input);
							}
							return input instanceof EntityMobBase?!((EntityMobBase)input).isTamed():IMob.VISIBLE_MOB_SELECTOR.apply(input);
						}});
					for(EntityLivingBase e : nearby)
						this.attacker.attackEntityAsMobWithElement(e, EnumElement.EARTH);
					if(this.attackTicker==this.attacker.getStatus().getTime())
						this.attackDelay = 64 + this.attacker.getRNG().nextInt(12);
				}
				break;
			case SLEEP:			
				if(this.moveDelay==0 && this.attackTicker==0)
				{
					this.attacker.getNavigator().tryMoveToEntityLiving(target, 1.5);
					this.moveDelay = 24 + this.attacker.getRNG().nextInt(7);
				}
				else if(this.attackTicker==0 && this.moveDelay==1)
				{
					this.attackTicker=this.attacker.getStatus().getDuration();
				}
				if(this.attackTicker>0)
				{
					this.attacker.getNavigator().clearPath();
			        this.attacker.getLookHelper().setLookPositionWithEntity(target, 30.0F, 30.0F);
				}
				if(this.attackTicker==this.attacker.getStatus().getTime())
				{
					this.attacker.summonSleepBalls();
					this.attackDelay = 64 + this.attacker.getRNG().nextInt(12);
				}
				break;
			case WAVE:				
				if(this.moveDelay==0 && this.attackTicker==0)
				{
					this.attacker.getNavigator().tryMoveToEntityLiving(target, 1.2);
					this.moveDelay = 24 + this.attacker.getRNG().nextInt(7);
				}
				else if(this.moveDelay ==1 && this.attackTicker==0)
				{
					this.attackTicker=this.attacker.getStatus().getDuration();
				}
				if(this.attackTicker>0)
				{
					this.attacker.getNavigator().clearPath();
			        this.attacker.getLookHelper().setLookPositionWithEntity(target, 30.0F, 30.0F);
				}
				if(this.attackTicker==this.attacker.getStatus().getTime())
				{
					this.attacker.summonWave(this.attackTicker);
					this.attackDelay = 64 + this.attacker.getRNG().nextInt(12);
				}
				break;
        	}
	}
	
	private BlockPos randomPosAwayFrom(EntityLivingBase away, float minDis)
	{
		double angle = Math.random()*Math.PI*2;
		double x = Math.cos(angle)*minDis;
		double z = Math.sin(angle)*minDis;
		float min=minDis*minDis;	
		BlockPos pos = this.attacker.getPosition().add(x, 0, z);
		if(away.getDistanceSq(pos)>min && (!this.attacker.hasHome() || this.attacker.getDistanceSq(pos)<this.attacker.getMaximumHomeDistance()*this.attacker.getMaximumHomeDistance()))
			return pos;
		return this.attacker.getPosition();
	}
	
	private double getAttackReachSqr(EntityLivingBase attackTarget)
    {
        return (double)(this.attacker.width * 2.0F * this.attacker.width * 2.0F + attackTarget.width)*2.5;
    }
	
	private AttackAI randomAIExcept(int valueOffSet)
	{
		AttackAI ai = AttackAI.values()[this.attacker.getRNG().nextInt(AttackAI.values().length-valueOffSet)+1];
		if(ai!=this.prevStatus)
			return ai;
		return randomAIExcept(valueOffSet);
	}
}
