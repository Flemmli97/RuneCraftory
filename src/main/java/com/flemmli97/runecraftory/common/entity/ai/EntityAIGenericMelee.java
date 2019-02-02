package com.flemmli97.runecraftory.common.entity.ai;

import com.flemmli97.runecraftory.common.entity.EntityMobBase;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class EntityAIGenericMelee extends EntityAIBase
{
    protected EntityMobBase attacker;
    protected int attackTick;
    double speedTowardsTarget;
    boolean longMemory;
    Path path;
    protected int delayCounter;
    private double targetX;
    private double targetY;
    private double targetZ;
    private float rangeModifier;
    
    public EntityAIGenericMelee(EntityMobBase creature, double speedIn, boolean useLongMemory, float rangeModifier) {
        this.attacker = creature;
        this.speedTowardsTarget = speedIn;
        this.longMemory = useLongMemory;
        this.rangeModifier = rangeModifier;
        this.setMutexBits(3);
    }
    
    public boolean shouldExecute() {
        EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
        if (entitylivingbase == null || !entitylivingbase.isEntityAlive() || !this.isTargetInsideHome(entitylivingbase)) {
            return false;
        }
        this.path = this.attacker.getNavigator().getPathToEntityLiving((Entity)entitylivingbase);
        return this.path != null || this.getAttackReachSqr(entitylivingbase) >= this.attacker.getDistanceSq(entitylivingbase.posX, entitylivingbase.getEntityBoundingBox().minY, entitylivingbase.posZ);
    }
    
    public boolean shouldContinueExecuting() {
        EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
        return entitylivingbase != null && entitylivingbase.isEntityAlive() && this.attacker.isWithinHomeDistanceFromPosition(new BlockPos((Entity)entitylivingbase)) && this.isTargetInsideHome(entitylivingbase) && (this.longMemory || !this.attacker.getNavigator().noPath());
    }
    
    private boolean isTargetInsideHome(EntityLivingBase target) {
        return this.attacker.getMaximumHomeDistance() == -1.0f || target.getPosition().distanceSq((Vec3i)this.attacker.getHomePosition()) < this.attacker.getMaximumHomeDistance() * this.attacker.getMaximumHomeDistance();
    }
    
    public void startExecuting() {
        this.attacker.getNavigator().setPath(this.path, this.speedTowardsTarget);
        this.delayCounter = 0;
    }
    
    public void resetTask() {
        EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
        if (entitylivingbase instanceof EntityPlayer && (((EntityPlayer)entitylivingbase).isSpectator() || ((EntityPlayer)entitylivingbase).isCreative())) {
            this.attacker.setAttackTarget((EntityLivingBase)null);
        }
        this.attacker.getNavigator().clearPath();
    }
    
    public void updateTask() {
        EntityLivingBase target = this.attacker.getAttackTarget();
        this.attacker.getLookHelper().setLookPositionWithEntity((Entity)target, 30.0f, 30.0f);
        double dis = this.attacker.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
        --this.delayCounter;
        this.checkAndPerformAttack(target, dis);
        if (this.attackTick > 0) {
            return;
        }
        if ((this.longMemory || this.attacker.getEntitySenses().canSee((Entity)target)) && this.delayCounter <= 0 && ((this.targetX == 0.0 && this.targetY == 0.0 && this.targetZ == 0.0) || target.getDistanceSq(this.targetX, this.targetY, this.targetZ) >= 1.0 || this.attacker.getRNG().nextFloat() < 0.05f)) {
            this.targetX = target.posX;
            this.targetY = target.getEntityBoundingBox().minY;
            this.targetZ = target.posZ;
            this.delayCounter = 4 + this.attacker.getRNG().nextInt(7);
            if (dis > 1024.0) {
                this.delayCounter += 10;
            }
            else if (dis > 256.0) {
                this.delayCounter += 5;
            }
            if (!this.attacker.getNavigator().tryMoveToEntityLiving((Entity)target, this.speedTowardsTarget)) {
                this.delayCounter += 15;
            }
            this.delayCounter += this.attacker.getRNG().nextInt(10) + 10;
        }
    }
    
    protected void checkAndPerformAttack(EntityLivingBase target, double dis) {
        this.attackTick = Math.max(--this.attackTick, 0);
        double d0 = this.getAttackReachSqr(target);
        if (dis <= d0) {
            if (this.attackTick <= 0) {
                if (this.attacker.getRNG().nextFloat() < this.attacker.attackChance()) {
                    this.attacker.swingArm(EnumHand.MAIN_HAND);
                    this.attacker.setAttackTimeAndPattern((byte)this.attacker.getRNG().nextInt(this.attacker.maxAttackPatterns()));
                    this.attackTick = this.attacker.getAttackTime();
                }
                else {
                    this.attackTick = 40;
                }
            }
            else if (this.attackTick == this.attacker.attackFromPattern()) {
                this.attacker.attackEntityAsMob((Entity)target);
            }
        }
    }
    
    protected double getAttackReachSqr(EntityLivingBase attackTarget) {
        return this.rangeModifier * (this.attacker.width * 2.0f * this.attacker.width * 2.0f + attackTarget.width);
    }
}
