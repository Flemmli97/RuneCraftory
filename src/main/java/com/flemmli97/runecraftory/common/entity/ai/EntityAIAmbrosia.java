package com.flemmli97.runecraftory.common.entity.ai;

import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.entity.monster.boss.EntityAmbrosia;
import com.flemmli97.runecraftory.common.lib.enums.EnumElement;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import com.google.common.base.Predicate;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.util.math.BlockPos;

public class EntityAIAmbrosia extends EntityAIAttackBase<EntityAmbrosia> {

    private int moveDelay;
    private boolean moveFlag,iddleFlag,clockwise;
    private double[] butterflyTarget;
    public EntityAIAmbrosia(EntityAmbrosia entity) {
        super(entity);
    }

    @Override
    public AnimatedAction randomAttack() {
        int enraged = this.attacker.isEnraged() ? 0 : 1;
        AnimatedAction anim = this.attacker.getAnimations()[this.attacker.getRNG().nextInt(this.attacker.getAnimations().length - enraged)];
        if (!anim.getID().equals(this.prevAnim)) {
            return anim;
        }
        return this.randomAttack();
    }
    
    public int coolDown(AnimatedAction anim) {
        return 99999;
    }

    @Override
    public void resetTask() {
        super.resetTask();
        this.moveDelay=0;
        this.moveFlag=false;
    }
    
    @Override
    public void handlePreAttack() {
        this.iddleFlag = false;
        switch (this.next.getID()) {
            case "butterfly":
                if (!this.moveFlag) {
                    if (this.distanceToTargetSq < 36.0) {
                        BlockPos pos = this.randomPosAwayFrom(this.target, 8.0f);
                        this.attacker.getNavigator().tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), 1);
                    }
                    this.moveDelay = 44;
                    this.moveFlag = true;
                } else if (this.moveDelay-- <= 0 && this.attacker.getNavigator().noPath()) {
                    this.butterflyTarget=new double[] {this.target.posX, this.target.posY, this.target.posZ};
                    this.movementDone = true;
                    this.moveFlag=false;
                }
                break;
            case "kick_1":
                if (this.attacker.getNavigator().tryMoveToEntityLiving(this.target, 1.0) && this.moveDelay == 0) {
                    this.moveDelay = 50;
                } else if (this.moveDelay-- <= 0)
                    this.movementDone = true;
                break;
            case "sleep":
                if (this.attacker.getNavigator().tryMoveToEntityLiving(this.target, 1.0) && this.moveDelay == 0) {
                    this.moveDelay = 24 + this.attacker.getRNG().nextInt(7);
                } else if (this.moveDelay-- <= 0)
                    this.movementDone = true;
                break;
            case "wave":
                if (this.attacker.getNavigator().tryMoveToEntityLiving(this.target, 1.0) && this.moveDelay == 0) {
                    this.moveDelay = 24 + this.attacker.getRNG().nextInt(7);
                } else if (this.moveDelay-- <= 0)
                    this.movementDone = true;
                break;
            case "kick_2":
                if (this.attacker.getNavigator().tryMoveToEntityLiving(this.target, 1.0) && this.moveDelay == 0) {
                    this.moveDelay = 40;
                } else if (this.moveDelay-- <= 0)
                    this.movementDone = true;
                break;
        }
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        switch (anim.getID()) {
            case "butterfly":
                this.attacker.getLookHelper().setLookPositionWithEntity(this.target, 30.0f, 30.0f);
                if (anim.getTick() > anim.getAttackTime()) {
                    float yDec = -1.5f;
                    if(this.distanceToTargetSq<9)
                        yDec=1;
                    else if(this.distanceToTargetSq<25)
                        yDec=0;
                    this.attacker.summonButterfly(this.butterflyTarget[0], this.butterflyTarget[1]-yDec, this.butterflyTarget[2]);
                }
                break;
            case "kick_1":
                this.attacker.getLookHelper().setLookPositionWithEntity(this.target, 30.0f, 30.0f);
                this.attacker.getNavigator().tryMoveToEntityLiving(this.target, 1.0);
                if (anim.getTick() % anim.getAttackTime() == 0 && this.distanceToTargetSq <= this.getAttackReachSqr(this.target, 2.5f)) {
                    this.attacker.attackEntityAsMob(this.target);
                }
                break;
            case "sleep":
                this.attacker.getNavigator().clearPath();
                this.attacker.getLookHelper().setLookPositionWithEntity(this.target, 30.0f, 30.0f);
                if (anim.getTick() == anim.getAttackTime())
                    this.attacker.summonSleepBalls();
                break;
            case "wave":
                this.attacker.getNavigator().clearPath();
                this.attacker.getLookHelper().setLookPositionWithEntity(this.target, 30.0f, 30.0f);
                if (anim.getTick() == anim.getAttackTime())
                    this.attacker.summonWave(anim.getLength() - anim.getAttackTime());
                break;
            case "kick_2":
                this.attacker.getLookHelper().setLookPositionWithEntity(this.target, 30.0f, 30.0f);
                this.attacker.getNavigator().tryMoveToEntityLiving(this.target, 1.0);
                if (anim.getTick() % anim.getAttackTime() == 0) {
                    this.attacker.world.getEntitiesWithinAABB(EntityLivingBase.class, this.attacker.getEntityBoundingBox().grow(2.0), new Predicate<EntityLivingBase>() {

                        @Override
                        public boolean apply(EntityLivingBase input) {
                            if (EntityAIAmbrosia.this.attacker.isTamed()) {
                                return (input instanceof EntityMobBase) ? (!((EntityMobBase) input).isTamed()) : IMob.VISIBLE_MOB_SELECTOR.apply(input);
                            }
                            if (input instanceof EntityMobBase) {
                                return ((EntityMobBase) input).isTamed();
                            }
                            return !IMob.VISIBLE_MOB_SELECTOR.apply(input);
                        }
                    }).forEach(e -> this.attacker.attackEntityAsMobWithElement(e, EnumElement.EARTH));
                    this.attacker.moveRelative(0.0f, 0.0f, 2.0f, 0.0f);
                }
                break;
        }
    }

    @Override
    public void handleIddle() {
        if(!this.iddleFlag) {
            this.clockwise=this.attacker.getRNG().nextBoolean();
            this.iddleFlag=true;
        }
        this.circleAroundTargetFacing(7, this.clockwise, 1);
    }

}
