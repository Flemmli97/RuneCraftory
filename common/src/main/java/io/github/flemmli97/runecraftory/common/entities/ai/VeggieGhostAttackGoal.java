package io.github.flemmli97.runecraftory.common.entities.ai;

import io.github.flemmli97.runecraftory.common.entities.monster.EntityVeggieGhost;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;

import java.util.function.Predicate;

public class VeggieGhostAttackGoal<T extends EntityVeggieGhost> extends AnimatedRangedGoal<T> {

    public VeggieGhostAttackGoal(T entity, float rangedReach, boolean allowStrafing, MeleeAttackCheck<T> meleeAttackCheck, Predicate<T> canRangedAttack) {
        super(entity, rangedReach, allowStrafing, meleeAttackCheck, canRangedAttack);
    }

    @Override
    public AnimatedAction randomAttack() {
        if (this.attacker.shouldVanishNext(this.prevAnim) || this.distanceToTargetSq > 100)
            return EntityVeggieGhost.VANISH;
        return super.randomAttack();
    }
}