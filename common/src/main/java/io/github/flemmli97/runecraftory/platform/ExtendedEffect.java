package io.github.flemmli97.runecraftory.platform;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public interface ExtendedEffect {

    default void onEffectAdded(LivingEntity livingEntity, MobEffectInstance instance) {
    }

    default void onEffectRemoved(LivingEntity livingEntity, MobEffectInstance instance) {
    }

    default boolean renderIcons() {
        return false;
    }
}
