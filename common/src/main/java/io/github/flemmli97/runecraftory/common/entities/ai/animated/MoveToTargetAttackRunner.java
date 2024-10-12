package io.github.flemmli97.runecraftory.common.entities.ai.animated;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.network.S2CAttackDebug;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.ActionRun;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;

public class MoveToTargetAttackRunner<T extends BaseMonster> implements ActionRun<T> {

    private final double speed;

    public MoveToTargetAttackRunner(double speed) {
        this.speed = speed;
    }

    @Override
    public boolean run(AnimatedAttackGoal<T> goal, LivingEntity target, AnimatedAction anim) {
        if (anim == null)
            return false;
        goal.moveToTarget(this.speed);
        goal.attacker.lookAt(target, 30.0F, 30.0F);
        AABB aabb = goal.attacker.attackCheckAABB(anim, target, -0.15);
        S2CAttackDebug.sendDebugPacket(aabb, S2CAttackDebug.EnumAABBType.ATTEMPT, goal.attacker);
        if (aabb.intersects(target.getBoundingBox())) {
            goal.attacker.getLookControl().setLookAt(target, 360, 90);
            return true;
        }
        return false;
    }
}
