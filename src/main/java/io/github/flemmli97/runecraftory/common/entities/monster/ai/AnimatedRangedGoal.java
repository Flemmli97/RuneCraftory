package io.github.flemmli97.runecraftory.common.entities.monster.ai;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;

import java.util.function.Predicate;

public class AnimatedRangedGoal<T extends BaseMonster> extends AnimatedMeleeGoal<T> {

    private int rangedMove = 40;
    private final float reach, reachSq;
    private boolean moveTo, clockWise;
    private boolean canSee;

    private final Predicate<T> canRanged;

    public AnimatedRangedGoal(T entity, float rangedReach, Predicate<T> canRangedAttack) {
        super(entity);
        this.reach = rangedReach;
        this.reachSq = rangedReach * rangedReach;
        this.canRanged = canRangedAttack;
    }

    @Override
    public AnimatedAction randomAttack() {
        if (this.attacker.getRNG().nextFloat() < this.attacker.attackChance(AnimationType.GENERICATTACK)) {
            if (this.attacker.getRNG().nextFloat() < this.attacker.attackChance(AnimationType.MELEE)) {
                AnimatedAction anim = this.attacker.getRandomAnimation(AnimationType.MELEE);
                double reach = this.attacker.maxAttackRange(anim) + this.attacker.getWidth() * 0.5 + this.target.getWidth() * 0.5;
                if (reach * reach > this.distanceToTargetSq)
                    return anim;
                else if (this.distanceToTargetSq <= this.reachSq && this.canRanged.test(this.attacker))
                    return this.attacker.getRandomAnimation(AnimationType.RANGED);
            } else if (this.distanceToTargetSq <= this.reachSq && this.canRanged.test(this.attacker))
                return this.attacker.getRandomAnimation(AnimationType.RANGED);
        }
        return this.attacker.getRandomAnimation(AnimationType.IDLE);
    }

    @Override
    public void handlePreAttack() {
        if (this.attacker.isAnimOfType(this.next, AnimationType.MELEE))
            super.handlePreAttack();
        else {
            if (this.distanceToTargetSq > this.reachSq || !this.canSee) {
                this.moveTo = true;
                this.attacker.getNavigator().tryMoveToEntityLiving(this.target, 1);
            } else if (this.moveTo) {
                this.attacker.getNavigator().clearPath();
                this.moveTo = false;
            } else if (--this.rangedMove > 0) {
                if (this.rangedMove == 39)
                    this.clockWise = this.attacker.getRNG().nextBoolean();
                this.circleAroundTargetFacing(this.reach - 2, this.clockWise, 1);
            } else {
                this.movementDone = true;
                this.rangedMove = 40;
            }
        }
    }

    @Override
    public void handleIddle() {
        if (this.iddleMoveDelay <= 0) {
            this.iddleMoveFlag = this.canSee ? this.attacker.getRNG().nextInt(5) : 3;
            this.iddleMoveDelay = this.attacker.getRNG().nextInt(35) + 55 - this.iddleMoveFlag * 10;
            this.clockWise = this.attacker.getRNG().nextBoolean();
        }
        switch (this.iddleMoveFlag) {
            case 0:
            case 1:
                this.circleAroundTargetFacing(this.reach - 2, this.clockWise, 1);
                break;
            case 2:
                this.moveRandomlyAround(36);
                break;
            case 3:
                this.moveToWithDelay(1);
                if (this.distanceToTargetSq < this.reachSq * 0.25 && this.canSee) {
                    this.attacker.getNavigator().clearPath();
                    this.iddleMoveDelay = 3;
                }
                break;
        }
    }

    @Override
    public void setupValues() {
        super.setupValues();
        this.canSee = this.attacker.getEntitySenses().canSee(this.target);
    }
}
