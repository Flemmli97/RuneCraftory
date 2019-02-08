package com.flemmli97.runecraftory.common.entity.ai;

import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.entity.IRangedMob;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class EntityAIGenericRanged<T extends EntityMobBase & IRangedMob> extends EntityAIBase
{
    protected T attacker;
    double moveSpeedAmp;
    float maxAttackDistance;
    float melee;
    private int attackTick;
    private int meleeTick;
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
        return entitylivingbase != null && entitylivingbase.isEntityAlive() && !this.attacker.getNavigator().noPath() && this.attacker.isWithinHomeDistanceFromPosition(new BlockPos((Entity)entitylivingbase)) && this.isTargetInsideHome(entitylivingbase);
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
        EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
        double dis = this.attacker.getDistanceSq(entitylivingbase.posX, entitylivingbase.getEntityBoundingBox().minY, entitylivingbase.posZ);
        boolean canSee = this.attacker.getEntitySenses().canSee((Entity)entitylivingbase);
        boolean flag1 = this.seeTime > 0;
        this.checkAndPerformAttack(entitylivingbase, dis);
        if (this.meleeTick > 0) {
            this.attacker.motionX = 0.0;
            this.attacker.motionY = 0.0;
            this.attacker.motionZ = 0.0;
            this.attacker.getLookHelper().setLookPositionWithEntity((Entity)entitylivingbase, 30.0f, 30.0f);
            return;
        }
        if (canSee != flag1) {
            this.seeTime = 0;
        }
        if (canSee) {
            ++this.seeTime;
        }
        else {
            --this.seeTime;
        }
        if (dis <= this.maxAttackDistance && this.seeTime >= 20) {
            this.attacker.getNavigator().clearPath();
            ++this.strafingTime;
        }
        else {
            this.attacker.getNavigator().tryMoveToEntityLiving((Entity)entitylivingbase, this.moveSpeedAmp);
            this.strafingTime = -1;
            this.attacker.getLookHelper().setLookPositionWithEntity((Entity)entitylivingbase, 30.0f, 30.0f);
        }
        if (this.strafingTime >= 20) {
            if (this.attacker.getRNG().nextFloat() < 0.3) {
                this.strafingClockwise = !this.strafingClockwise;
            }
            if (this.attacker.getRNG().nextFloat() < 0.3) {
                this.strafingBackwards = !this.strafingBackwards;
            }
            this.strafingTime = 0;
        }
        if (this.strafingTime > -1) {
            if (dis > this.maxAttackDistance * 0.75f) {
                this.strafingBackwards = false;
            }
            else if (dis < this.maxAttackDistance * 0.25f) {
                this.strafingBackwards = true;
            }
            this.attacker.getMoveHelper().strafe(this.strafingBackwards ? -0.5f : 0.5f, this.strafingClockwise ? 0.5f : -0.5f);
            this.attacker.faceEntity((Entity)entitylivingbase, 30.0f, 30.0f);
        }
        else {
            this.attacker.getLookHelper().setLookPositionWithEntity((Entity)entitylivingbase, 30.0f, 30.0f);
        }
    }
    
    protected void checkAndPerformAttack(EntityLivingBase target, double dis) {
        this.attackTick = Math.max(--this.attackTick, 0);
        this.meleeTick = Math.max(--this.meleeTick, 0);
        double reach = this.maxAttackDistance;
        double melee = this.getAttackReachSqr(target);
        if (dis <= melee) {
            if (this.meleeTick <= 0) {
                if (this.attacker.getRNG().nextFloat() < this.attacker.attackChance()) {
                    this.attacker.swingArm(EnumHand.MAIN_HAND);
                    int i = this.attacker.maxAttackPatterns() - 1;
                    this.attacker.setAttackTimeAndPattern((byte)this.attacker.getRNG().nextInt((i <= 0) ? 1 : i));
                    this.meleeTick = this.attacker.getAttackTime();
                }
                else {
                    this.meleeTick = 40;
                }
            }
            else if (this.meleeTick == this.attacker.attackFromPattern()) {
                this.attacker.attackEntityAsMob((Entity)target);
            }
        }
        else if (dis <= reach) {
            if (this.attackTick <= 0) {
                if (this.attacker.getRNG().nextFloat() < this.attacker.attackChance()) {
                    this.attacker.swingArm(EnumHand.MAIN_HAND);
                    int i = this.attacker.maxAttackPatterns() - 1;
                    this.attacker.setAttackTimeAndPattern((byte)this.attacker.getRNG().nextInt((i <= 0) ? 1 : i));
                    this.attackTick = this.attacker.getAttackTime();
                }
                else {
                    this.attackTick = 40;
                }
            }
            else if (this.attackTick == this.attacker.attackFromPattern()) {
                ((IRangedMob)this.attacker).attackRanged(target);
            }
        }
    }
    
    protected double getAttackReachSqr(EntityLivingBase attackTarget) {
        return this.melee * (this.attacker.width * 2.0f * this.attacker.width * 2.0f + attackTarget.width);
    }
}
