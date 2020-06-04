package com.flemmli97.runecraftory.common.entity.ai;

import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.entity.EntityMobBase.AnimationType;
import com.flemmli97.runecraftory.common.entity.IRangedMob;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;

public class EntityAIRangedBase<T extends EntityMobBase & IRangedMob> extends EntityAIMeleeBase<T> {

    private int rangedMove=40;
    private float rangedReach;
    private boolean moveTo, clockWise;
    public EntityAIRangedBase(T entity, float meleeReachMod, float rangedReach) {
        super(entity);
        this.rangedReach=rangedReach;
    }

    @Override
    public AnimatedAction randomAttack() {
        if (this.attacker.getRNG().nextFloat() < this.attacker.attackChance())
            if(this.attacker.getRNG().nextFloat() < this.attacker.meleeChance() && this.distanceToTargetSq <= 0)//this.attacker.getAttackReach())
                return this.attacker.getRandomAnimation(AnimationType.MELEE);
            else
                return this.attacker.getRandomAnimation(AnimationType.RANGED);
        return this.attacker.getRandomAnimation(AnimationType.IDLE);
    }
    
    @Override
    public void handlePreAttack() {
        if(this.attacker.isAnimOfType(this.next, AnimationType.MELEE))
            super.handlePreAttack();
        else {
            if(this.distanceToTargetSq>this.rangedReach*this.rangedReach) {
                this.moveTo=true;
                this.attacker.getNavigator().tryMoveToEntityLiving(this.target, 1);
            }
            else if(this.moveTo) {
                this.attacker.getNavigator().clearPath();
                this.moveTo=false;
            }
            else if(--this.rangedMove>0) {
                if(this.rangedMove==39)
                    this.clockWise=this.attacker.getRNG().nextBoolean();
                this.circleAroundTargetFacing(this.rangedReach-2, this.clockWise, 1);
            }
            else {
                this.movementDone=true;
                this.rangedMove=40;
            }
        }
    }

    /*@Override
    public void handleAttack(AnimatedAction anim) {
        if (this.attacker.isAnimOfType(anim, AnimationType.RANGED)) {
            this.attacker.getNavigator().clearPath();
            if(anim.canAttack())
                this.attacker.attackRanged(this.target);
        }
        else
            super.handleAttack(anim);
    }*/

    @Override
    public void handleIddle() {
        if(this.iddleMoveDelay<=0) {
            this.iddleMoveFlag=this.attacker.getRNG().nextInt(4);
            this.iddleMoveDelay=this.attacker.getRNG().nextInt(35)+55-this.iddleMoveFlag*10;
            this.clockWise=this.attacker.getRNG().nextBoolean();
        }
        switch(this.iddleMoveFlag) {
            case 0:
                this.circleAroundTargetFacing(this.rangedReach-2, this.clockWise, 1);
                break;
            case 1:
                this.moveRandomlyAround(36);
                break;
            case 2:
                this.moveToWithDelay(1);
                break;
        }
    }
}
