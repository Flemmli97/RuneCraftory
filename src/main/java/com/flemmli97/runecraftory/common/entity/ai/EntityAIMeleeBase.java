package com.flemmli97.runecraftory.common.entity.ai;

import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.entity.EntityMobBase.AnimationType;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;

public class EntityAIMeleeBase<T extends EntityMobBase> extends EntityAIAttackBase<T> {

    protected float rangeModifier;
    protected int iddleMoveDelay, iddleMoveFlag;

    public EntityAIMeleeBase(T entity, float reachMod) {
        super(entity);
        this.rangeModifier = reachMod;
    }

    @Override
    public AnimatedAction randomAttack() {
        if (this.attacker.getRNG().nextFloat() < this.attacker.attackChance())
            return this.attacker.getRandomAnimation(AnimationType.MELEE);
        return this.attacker.getRandomAnimation(AnimationType.IDLE);
    }

    @Override
    public int coolDown(AnimatedAction anim) {
        return anim == null ? 40 : super.coolDown(anim);
    }

    @Override
    public void handlePreAttack() {
        this.moveToWithDelay(1);
        if (this.distanceToTargetSq <= this.getAttackReachSqr(this.target, this.rangeModifier)) {
            this.movementDone = true;
        }
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (this.attacker.isAnimOfType(anim, AnimationType.MELEE)) {
            this.attacker.getNavigator().clearPath();
            if (this.distanceToTargetSq <= this.getAttackReachSqr(this.target, this.rangeModifier) && anim.canAttack()) {
                this.attacker.attackEntityAsMob(this.target);
            }
        }
    }

    @Override
    public void handleIddle() {
        if(this.iddleMoveDelay<=0) {
            this.iddleMoveFlag=this.attacker.getRNG().nextInt(3);
            this.iddleMoveDelay=this.attacker.getRNG().nextInt(35)+55-this.iddleMoveFlag*10;
        }
        switch(this.iddleMoveFlag) {
            case 0:
                this.moveToWithDelay(1);
                break;
            case 1:
                this.moveRandomlyAround(36);
                break;
        }
    }

}
