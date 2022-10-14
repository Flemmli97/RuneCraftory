package io.github.flemmli97.runecraftory.common.entities;

import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

public interface RandomAttackSelectorMob {

    AABB calculateAttackAABB(AnimatedAction anim, LivingEntity target, double grow);

    AnimatedAction getRandomAnimation(AnimationType type);

    boolean isAnimOfType(AnimatedAction anim, AnimationType type);

    float attackChance(AnimationType type);

    double maxAttackRange(AnimatedAction anim);

    int animationCooldown(@Nullable AnimatedAction anim);

}
