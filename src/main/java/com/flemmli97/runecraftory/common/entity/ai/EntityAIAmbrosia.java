package com.flemmli97.runecraftory.common.entity.ai;

import java.util.List;

import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.entity.monster.boss.EntityAmbrosia;
import com.flemmli97.runecraftory.common.lib.enums.EnumElement;
import com.google.common.base.Predicate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.monster.IMob;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class EntityAIAmbrosia extends EntityAIBase
{
    private EntityAmbrosia attacker;
    private int attackTicker;
    private int attackDelay;
    private int moveDelay;
    private EntityAmbrosia.AttackAI prevStatus;
    
    public EntityAIAmbrosia(EntityAmbrosia entity) {
        this.attacker = entity;
        this.setMutexBits(3);
    }
    
    public boolean shouldExecute() {
        EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
        return entitylivingbase != null && entitylivingbase.isEntityAlive();
    }
    
    public boolean shouldContinueExecuting() {
        EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
        return entitylivingbase != null && entitylivingbase.isEntityAlive() && this.attacker.isWithinHomeDistanceFromPosition(new BlockPos((Entity)entitylivingbase));
    }
    
    public void resetTask() {
        this.attacker.setStatus(EntityAmbrosia.AttackAI.IDDLE);
    }
    
    public void updateTask() {
        EntityLivingBase target = this.attacker.getAttackTarget();
        double dis = this.attacker.getDistanceSq((Entity)target);
        this.attackTicker = Math.max(this.attackTicker - 1, 0);
        this.moveDelay = Math.max(this.moveDelay - 1, 0);
        if (this.attacker.getStatus() == EntityAmbrosia.AttackAI.IDDLE) {
            this.attackDelay = Math.max(this.attackDelay - 1, 0);
            if (this.attackDelay == 0 && this.attackTicker == 0) {
                int i = this.attacker.isEnraged() ? 1 : 2;
                EntityAmbrosia.AttackAI ai = this.randomAIExcept(i);
                this.attacker.setStatus(ai);
                this.prevStatus = ai;
                this.moveDelay = 0;
            }
        }
        if (this.attackTicker == 0 && this.attackDelay > 0) {
            this.attacker.setStatus(EntityAmbrosia.AttackAI.IDDLE);
        }
        switch (this.attacker.getStatus()) {
            case BUTTERFLY: {
                if (dis < 25.0 && this.moveDelay == 0 && this.attackTicker == 0) {
                    BlockPos pos = this.randomPosAwayFrom(target, 8.0f);
                    this.attacker.getNavigator().tryMoveToXYZ((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), 1.5);
                    this.moveDelay = 24 + this.attacker.getRNG().nextInt(7);
                }
                else if (this.attacker.getNavigator().noPath() && this.attackTicker == 0) {
                    this.attackTicker = this.attacker.getStatus().getDuration();
                }
                if (this.attackTicker > 0) {
                    this.attacker.getLookHelper().setLookPositionWithEntity((Entity)target, 30.0f, 30.0f);
                }
                if (this.attackTicker > this.attacker.getStatus().getTime()) {
                    break;
                }
                this.attacker.summonButterfly();
                if (this.attackTicker == 1) {
                    this.attackDelay = 64 + this.attacker.getRNG().nextInt(12);
                    break;
                }
                break;
            }
            case IDDLE: {
                this.attacker.getLookHelper().setLookPositionWithEntity((Entity)target, 30.0f, 30.0f);
                if (dis <= 64.0) {
                    Vec3d rand = RandomPositionGenerator.findRandomTarget((EntityCreature)this.attacker, 5, 4);
                    if (rand != null) {
                        this.attacker.getNavigator().tryMoveToXYZ(rand.x, rand.y, rand.z, 1.5);
                        this.moveDelay = 44 + this.attacker.getRNG().nextInt(7);
                    }
                    break;
                }
                if (this.moveDelay == 0) {
                    this.attacker.getNavigator().tryMoveToEntityLiving((Entity)target, 1.5);
                    this.moveDelay = 24 + this.attacker.getRNG().nextInt(7);
                    break;
                }
                break;
            }
            case KICK1: {
                if (this.attacker.getNavigator().tryMoveToEntityLiving((Entity)target, 1.0) && this.moveDelay == 0) {
                    this.moveDelay = 60;
                }
                if (this.attackTicker == 0 && this.moveDelay == 1) {
                    this.attackTicker = this.attacker.getStatus().getDuration();
                }
                if (this.attackTicker > 0) {
                    this.attacker.getLookHelper().setLookPositionWithEntity((Entity)target, 30.0f, 30.0f);
                }
                if (this.attackTicker == 0 || this.attackTicker % this.attacker.getStatus().getTime() != 0) {
                    break;
                }
                if (dis <= this.getAttackReachSqr(target)) {
                    this.attacker.attackEntityAsMob((Entity)target);
                }
                if (this.attackTicker == this.attacker.getStatus().getTime()) {
                    this.attackDelay = 64 + this.attacker.getRNG().nextInt(12);
                    break;
                }
                break;
            }
            case KICK2: {
                if (this.attacker.getNavigator().tryMoveToEntityLiving((Entity)target, 1.0) && this.moveDelay == 0) {
                    this.moveDelay = 40;
                }
                if (this.attackTicker == 0 && this.moveDelay == 1) {
                    this.attackTicker = this.attacker.getStatus().getDuration();
                }
                if (this.attackTicker > 0) {
                    this.attacker.getLookHelper().setLookPositionWithEntity((Entity)target, 30.0f, 30.0f);
                }
                if (this.attackTicker != 0 && this.attackTicker % this.attacker.getStatus().getTime() == 0) {
                    List<EntityLivingBase> nearby = this.attacker.world.getEntitiesWithinAABB(EntityLivingBase.class, this.attacker.getEntityBoundingBox().grow(2.0), new Predicate<EntityLivingBase>() {
                        public boolean apply(EntityLivingBase input) {
                            if (EntityAIAmbrosia.this.attacker.isTamed()) {
                                return (input instanceof EntityMobBase) ? (!((EntityMobBase)input).isTamed()) : IMob.VISIBLE_MOB_SELECTOR.apply(input);
                            }
                            if (input instanceof EntityMobBase) {
                                return ((EntityMobBase)input).isTamed();
                            }
                            return !IMob.VISIBLE_MOB_SELECTOR.apply(input);
                        }
                    });
                    for (EntityLivingBase e : nearby) {
                        this.attacker.attackEntityAsMobWithElement((Entity)e, EnumElement.EARTH);
                    }
                    this.attacker.moveRelative(0.0f, 0.0f, 2.0f, 0.0f);
                    if (this.attackTicker == this.attacker.getStatus().getTime()) {
                        this.attackDelay = 64 + this.attacker.getRNG().nextInt(12);
                    }
                    break;
                }
                break;
            }
            case SLEEP: {
                if (this.moveDelay == 0 && this.attackTicker == 0) {
                    this.attacker.getNavigator().tryMoveToEntityLiving((Entity)target, 1.5);
                    this.moveDelay = 24 + this.attacker.getRNG().nextInt(7);
                }
                else if (this.attackTicker == 0 && this.moveDelay == 1) {
                    this.attackTicker = this.attacker.getStatus().getDuration();
                }
                if (this.attackTicker > 0) {
                    this.attacker.getNavigator().clearPath();
                    this.attacker.getLookHelper().setLookPositionWithEntity((Entity)target, 30.0f, 30.0f);
                }
                if (this.attackTicker == this.attacker.getStatus().getTime()) {
                    this.attacker.summonSleepBalls();
                    this.attackDelay = 64 + this.attacker.getRNG().nextInt(12);
                    break;
                }
                break;
            }
            case WAVE: {
                if (this.moveDelay == 0 && this.attackTicker == 0) {
                    this.attacker.getNavigator().tryMoveToEntityLiving((Entity)target, 1.2);
                    this.moveDelay = 24 + this.attacker.getRNG().nextInt(7);
                }
                else if (this.moveDelay == 1 && this.attackTicker == 0) {
                    this.attackTicker = this.attacker.getStatus().getDuration();
                }
                if (this.attackTicker > 0) {
                    this.attacker.getNavigator().clearPath();
                    this.attacker.getLookHelper().setLookPositionWithEntity((Entity)target, 30.0f, 30.0f);
                }
                if (this.attackTicker == this.attacker.getStatus().getTime()) {
                    this.attacker.summonWave(this.attackTicker);
                    this.attackDelay = 64 + this.attacker.getRNG().nextInt(12);
                    break;
                }
                break;
            }
        }
    }
    
    private BlockPos randomPosAwayFrom(EntityLivingBase away, float minDis) {
        double angle = Math.random() * 3.141592653589793 * 2.0;
        double x = Math.cos(angle) * minDis;
        double z = Math.sin(angle) * minDis;
        float min = minDis * minDis;
        BlockPos pos = this.attacker.getPosition().add(x, 0.0, z);
        if (away.getDistanceSq(pos) > min && (!this.attacker.hasHome() || this.attacker.getDistanceSq(pos) < this.attacker.getMaximumHomeDistance() * this.attacker.getMaximumHomeDistance())) {
            return pos;
        }
        return this.attacker.getPosition();
    }
    
    private double getAttackReachSqr(EntityLivingBase attackTarget) {
        return (this.attacker.width * 2.0f * this.attacker.width * 2.0f + attackTarget.width) * 2.5;
    }
    
    private EntityAmbrosia.AttackAI randomAIExcept(int valueOffSet) {
        EntityAmbrosia.AttackAI ai = EntityAmbrosia.AttackAI.values()[this.attacker.getRNG().nextInt(EntityAmbrosia.AttackAI.values().length - valueOffSet) + 1];
        if (ai != this.prevStatus) {
            return ai;
        }
        return this.randomAIExcept(valueOffSet);
    }
}
