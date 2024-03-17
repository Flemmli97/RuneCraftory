package io.github.flemmli97.runecraftory.common.entities;

import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public interface RandomAttackSelectorMob {

    AABB calculateAttackAABB(AnimatedAction anim, @Nullable Vec3 target, double grow);

    default AABB attackCheckAABB(AnimatedAction anim, LivingEntity target, double grow) {
        return this.calculateAttackAABB(anim, target.position(), grow);
    }

    AnimatedAction getRandomAnimation(AnimationType type);

    boolean isAnimOfType(AnimatedAction anim, AnimationType type);

    float attackChance(AnimationType type);

    double maxAttackRange(AnimatedAction anim);

    int animationCooldown(@Nullable AnimatedAction anim);

}
