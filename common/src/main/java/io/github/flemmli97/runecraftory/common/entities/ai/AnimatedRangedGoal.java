package io.github.flemmli97.runecraftory.common.entities.ai;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;

import java.util.function.Predicate;

public class AnimatedRangedGoal<T extends BaseMonster> extends AnimatedMeleeGoal<T> {

    private static <T extends BaseMonster> MeleeAttackCheck<T> DEFAULT_CHECK() {
        return (attacker, target, anim) -> {
            AABB aabb = attacker.attackCheckAABB(anim, target, -0.3);
            return aabb.intersects(target.getBoundingBox());
        };
    }

    private final float reach, reachSq;
    private final Predicate<T> canRanged;
    private final boolean allowStrafing;
    private int rangedMove = 40;
    private boolean moveTo, clockWise;
    private boolean canSee;

    private final MeleeAttackCheck<T> meleeAttackCheck;

    public AnimatedRangedGoal(T entity, float rangedReach, Predicate<T> canRangedAttack) {
        this(entity, rangedReach, true, canRangedAttack);
    }

    public AnimatedRangedGoal(T entity, float rangedReach, boolean allowStrafing, Predicate<T> canRangedAttack) {
        this(entity, rangedReach, allowStrafing, DEFAULT_CHECK(), canRangedAttack);
    }

    public AnimatedRangedGoal(T entity, float rangedReach, boolean allowStrafing, MeleeAttackCheck<T> meleeAttackCheck, Predicate<T> canRangedAttack) {
        super(entity);
        this.meleeAttackCheck = meleeAttackCheck;
        this.reach = rangedReach;
        this.reachSq = rangedReach * rangedReach;
        this.canRanged = canRangedAttack;
        this.allowStrafing = allowStrafing;
    }

    @Override
    public AnimatedAction randomAttack() {
        if (this.attacker.getRandom().nextFloat() < this.attacker.attackChance(AnimationType.GENERICATTACK)) {
            if (this.attacker.getRandom().nextFloat() < this.attacker.attackChance(AnimationType.MELEE)) {
                AnimatedAction anim = this.attacker.getRandomAnimation(AnimationType.MELEE);
                if (this.meleeAttackCheck.inMeleeRange(this.attacker, this.target, anim))
                    return anim;
            }
            if (this.distanceToTargetSq <= this.reachSq && this.canRanged.test(this.attacker))
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
    public void handleIdle() {
        if (this.idleMoveDelay <= 0) {
            this.idleMoveFlag = this.canSee ? this.attacker.getRandom().nextInt(5) : 3;
            this.idleMoveDelay = this.attacker.getRandom().nextInt(35) + 55 - this.idleMoveFlag * 10;
            this.clockWise = this.attacker.getRandom().nextBoolean();
        }
        switch (this.idleMoveFlag) {
            case 0, 1 -> this.circleAroundTargetFacing(this.reach - 2, this.clockWise, 1);
            case 2 -> this.moveRandomlyAround(36);
            case 3 -> {
                this.moveToWithDelay(1);
                if (this.distanceToTargetSq < this.reachSq * 0.25 && this.canSee) {
                    this.attacker.getNavigation().stop();
                    this.idleMoveDelay = 3;
                }
            }
        }
    }

    @Override
    public void setupValues() {
        super.setupValues();
        this.canSee = this.attacker.getSensing().hasLineOfSight(this.target);
    }

    @Override
    protected void circleAroundTargetFacing(float radius, boolean clockWise, float speed) {
        if (!this.allowStrafing)
            return;
        super.circleAroundTargetFacing(radius, clockWise, speed);
    }

    public interface MeleeAttackCheck<T extends BaseMonster> {

        boolean inMeleeRange(T attacker, LivingEntity target, AnimatedAction anim);
    }
}
