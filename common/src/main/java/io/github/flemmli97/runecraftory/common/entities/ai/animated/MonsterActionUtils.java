package io.github.flemmli97.runecraftory.common.entities.ai.animated;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.BossMonster;
import io.github.flemmli97.runecraftory.common.entities.ChargingMonster;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.ActionUtils;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.KeepDistanceRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetAttackRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.StrafingRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.TimedWrappedRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.WrappedRunner;
import io.github.flemmli97.tenshilib.common.utils.OrientedBoundingBox;

public class MonsterActionUtils {

    public static <T extends ChargingMonster> GoalAttackAction.Condition<T> chargeCondition() {
        return (goal, target, previousAnim) -> {
            double heightDiff = target.getY() - goal.attacker.getY();
            return (goal.distanceToTargetSq >= 3 * 3 && heightDiff <= 1 && heightDiff >= -goal.attacker.getMaxFallDistance()) || goal.attacker.getRandom().nextFloat() < 0.5;
        };
    }

    public static <T extends BaseMonster> GoalAttackAction<T> nonRepeatableAttack(AnimatedAction anim) {
        return new GoalAttackAction<T>(anim)
                .cooldown(e -> e.animationCooldown(anim))
                .withCondition((goal, target, previous) -> goal.attacker.allowAnimation(previous, anim));
    }

    public static <T extends BossMonster> GoalAttackAction<T> enragedBossAttack(AnimatedAction anim) {
        return new GoalAttackAction<T>(anim)
                .cooldown(e -> e.animationCooldown(anim))
                .withCondition((goal, target, previous) -> goal.attacker.isEnraged() && goal.attacker.allowAnimation(previous, anim));
    }

    public static <T extends BaseMonster> GoalAttackAction.Condition<T> inAABBRange(AnimatedAction anim) {
        return (goal, target, previousAnim) -> {
            OrientedBoundingBox obb = goal.attacker.prepareAttackBox(anim, target, -0.3, false);
            return obb.intersects(target.getBoundingBox());
        };
    }

    public static <T extends BaseMonster> GoalAttackAction<T> simpleMeleeAction(AnimatedAction anim, ActionUtils.FloatGetter<T> chance) {
        return new GoalAttackAction<T>(anim)
                .cooldown(e -> e.animationCooldown(anim))
                .withCondition(ActionUtils.chanced(chance))
                .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1)));
    }

    public static <T extends BaseMonster> GoalAttackAction<T> simpleMeleeActionInRange(AnimatedAction anim, ActionUtils.FloatGetter<T> chance) {
        return simpleMeleeActionCondition(anim, chance, MonsterActionUtils.inAABBRange(anim));
    }

    public static <T extends BaseMonster> GoalAttackAction<T> simpleMeleeActionCondition(AnimatedAction anim, ActionUtils.FloatGetter<T> chance, GoalAttackAction.Condition<T> condition) {
        return new GoalAttackAction<T>(anim)
                .cooldown(e -> e.animationCooldown(anim))
                .withCondition(ActionUtils.chanced(chance, condition))
                .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1)));
    }

    public static <T extends BaseMonster> GoalAttackAction<T> simpleRangedStrafingAction(AnimatedAction anim, float radius, float speed, ActionUtils.FloatGetter<T> chance) {
        return new GoalAttackAction<T>(anim)
                .cooldown(e -> e.animationCooldown(anim))
                .withCondition(ActionUtils.chanced(chance))
                .prepare(() -> new TimedWrappedRunner<>(new StrafingRunner<>(radius, speed), e -> e.getRandom().nextInt(20) + 30));
    }

    public static <T extends BaseMonster> GoalAttackAction<T> simpleRangedEvadingAction(AnimatedAction anim, double max, double min, double speed, ActionUtils.FloatGetter<T> chance) {
        return new GoalAttackAction<T>(anim)
                .cooldown(e -> e.animationCooldown(anim))
                .withCondition(ActionUtils.chanced(chance))
                .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(min, max, speed)));
    }
}
