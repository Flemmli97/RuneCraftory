package io.github.flemmli97.runecraftory.common.entities.monster.ai;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.world.phys.AABB;

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
        if (this.attacker.getRandom().nextFloat() < this.attacker.attackChance(AnimationType.GENERICATTACK)) {
            if (this.attacker.getRandom().nextFloat() < this.attacker.attackChance(AnimationType.MELEE)) {
                AnimatedAction anim = this.attacker.getRandomAnimation(AnimationType.MELEE);
                AABB aabb = this.attacker.calculateAttackAABB(anim, this.target, 1);
                if (aabb.intersects(this.target.getBoundingBox()))
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
                this.attacker.getNavigation().moveTo(this.target, 1);
            } else if (this.moveTo) {
                this.attacker.getNavigation().stop();
                this.moveTo = false;
            } else if (--this.rangedMove > 0) {
                if (this.rangedMove == 39)
                    this.clockWise = this.attacker.getRandom().nextBoolean();
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
            this.iddleMoveFlag = this.canSee ? this.attacker.getRandom().nextInt(5) : 3;
            this.iddleMoveDelay = this.attacker.getRandom().nextInt(35) + 55 - this.iddleMoveFlag * 10;
            this.clockWise = this.attacker.getRandom().nextBoolean();
        }
        switch (this.iddleMoveFlag) {
            case 0, 1 -> this.circleAroundTargetFacing(this.reach - 2, this.clockWise, 1);
            case 2 -> this.moveRandomlyAround(36);
            case 3 -> {
                this.moveToWithDelay(1);
                if (this.distanceToTargetSq < this.reachSq * 0.25 && this.canSee) {
                    this.attacker.getNavigation().stop();
                    this.iddleMoveDelay = 3;
                }
            }
        }
    }

    @Override
    public void setupValues() {
        super.setupValues();
        this.canSee = this.attacker.getSensing().hasLineOfSight(this.target);
    }
}
