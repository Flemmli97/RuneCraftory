package io.github.flemmli97.runecraftory.common.entities;

import net.minecraft.world.entity.LivingEntity;

import java.util.function.Predicate;

public interface TargetableOpponent {

    Predicate<LivingEntity> validTargetPredicate();
}
