package io.github.flemmli97.runecraftory.common.entities.monster.ai;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import java.util.function.Predicate;

public class HurtByTargetPredicate extends HurtByTargetGoal {

    private final Predicate<LivingEntity> pred;

    public HurtByTargetPredicate(BaseMonster mob, Predicate<LivingEntity> pred) {
        super(mob);
        this.pred = pred;
    }

    @Override
    protected boolean canAttack(LivingEntity target, TargetingConditions pred) {
        return (this.mob.getTarget() == null || this.mob.getTarget() != target) &&
                (this.pred == null || this.pred.test(target)) && super.canAttack(target, pred);
    }
}
