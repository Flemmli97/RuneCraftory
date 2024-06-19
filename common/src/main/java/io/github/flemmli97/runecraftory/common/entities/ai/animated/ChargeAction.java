package io.github.flemmli97.runecraftory.common.entities.ai.animated;

import io.github.flemmli97.runecraftory.common.entities.ChargingMonster;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.ActionStart;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import net.minecraft.world.entity.LivingEntity;

public class ChargeAction<T extends ChargingMonster> implements ActionStart<T> {

    @Override
    public GoalAttackAction.IntProvider<T> timeout() {
        return e -> 5;
    }

    @Override
    public boolean start(AnimatedAttackGoal<T> goal, LivingEntity target) {
        if (goal.current == null)
            return false;
        goal.attacker.setChargeMotion(goal.attacker.getChargeTo(goal.current.anim(), target.position()));
        goal.attacker.lookAt(target, 360, 10);
        goal.attacker.lockYaw(goal.attacker.getYRot());
        return true;
    }
}
