package io.github.flemmli97.runecraftory.common.entities.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class NearestTargetHorizontal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {

    public NearestTargetHorizontal(Mob mob, Class<T> clss, int i, boolean mustSee, boolean mustReach, @Nullable Predicate<LivingEntity> predicate) {
        super(mob, clss, i, mustSee, mustReach, predicate);
    }

    @Override
    protected AABB getTargetSearchArea(double targetDistance) {
        return this.mob.getBoundingBox().inflate(targetDistance, targetDistance, targetDistance);
    }
}