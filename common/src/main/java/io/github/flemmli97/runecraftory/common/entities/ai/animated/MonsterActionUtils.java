package io.github.flemmli97.runecraftory.common.entities.ai.animated;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.ChargingMonster;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.ActionUtils;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.EvadingRangedRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.StrafingRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.TimedWrappedRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.WrappedRunner;
import net.minecraft.world.phys.AABB;

public class MonsterActionUtils {

    public static <T extends ChargingMonster> GoalAttackAction.Condition<T> chargeCondition() {
        return (goal, target, previousAnim) -> {
            double heightDiff = target.getY() - goal.attacker.getY();
            return goal.distanceToTargetSq <= (goal.attacker.chargingLength() * goal.attacker.chargingLength() + 1) && heightDiff <= 1 && heightDiff >= -goal.attacker.getMaxFallDistance();
        };
    }

    public static <T extends BaseMonster> GoalAttackAction<T> nonRepeatableAttack(AnimatedAction anim) {
        return new GoalAttackAction<T>(anim)
                .cooldown(e -> e.animationCooldown(anim))
                .withCondition((goal, target, previous) -> !goal.attacker.isAnimEqual(previous, anim));
    }

    public static <T extends BaseMonster> GoalAttackAction.Condition<T> inAABBRange(AnimatedAction anim) {
        return (goal, target, previousAnim) -> {
            AABB aabb = goal.attacker.attackCheckAABB(anim, target, -0.3);
            return aabb.intersects(target.getBoundingBox());
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
                .prepare(() -> new WrappedRunner<>(new EvadingRangedRunner<>(max, min, speed)));
    }
}
