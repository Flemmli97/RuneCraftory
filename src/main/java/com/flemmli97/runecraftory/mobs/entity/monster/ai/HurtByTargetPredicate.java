package com.flemmli97.runecraftory.mobs.entity.monster.ai;

import com.flemmli97.runecraftory.mobs.entity.BaseMonster;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;

import java.util.function.Predicate;

public class HurtByTargetPredicate extends HurtByTargetGoal {

    private final Predicate<LivingEntity> pred;

    public HurtByTargetPredicate(BaseMonster mob, Predicate<LivingEntity> pred) {
        super(mob);
        this.pred = pred;
    }

    @Override
    protected boolean isSuitableTarget(LivingEntity target, EntityPredicate pred) {
        return (this.goalOwner.getAttackTarget() == null || this.goalOwner.getAttackTarget() != target) &&
                (this.pred == null || this.pred.test(target)) && super.isSuitableTarget(target, pred);
    }
}
