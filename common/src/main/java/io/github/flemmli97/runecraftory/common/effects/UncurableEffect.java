package io.github.flemmli97.runecraftory.common.effects;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.function.Function;

public class UncurableEffect extends SyncedMobEffect {

    public UncurableEffect(MobEffectCategory type, int color) {
        super(type, color);
    }

    public UncurableEffect(MobEffectCategory category, int color, Function<MobEffectInstance, ParticleOptions> factory) {
        super(category, color, factory);
    }
}
