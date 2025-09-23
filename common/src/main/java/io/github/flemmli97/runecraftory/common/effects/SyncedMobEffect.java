package io.github.flemmli97.runecraftory.common.effects;

import io.github.flemmli97.tenshilib.common.effect.ExtendedMobEffect;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.function.Function;

public class SyncedMobEffect extends MobEffect implements ExtendedMobEffect {

    private final Function<MobEffectInstance, ParticleOptions> factory;

    public SyncedMobEffect(MobEffectCategory type, int color) {
        this(type, color, null);
    }

    public SyncedMobEffect(MobEffectCategory category, int color, Function<MobEffectInstance, ParticleOptions> factory) {
        super(category, color);
        this.factory = factory;
    }

    @Override
    public ParticleOptions createParticleOptions(MobEffectInstance effect) {
        return this.factory != null ? this.factory.apply(effect) : super.createParticleOptions(effect);
    }
}
