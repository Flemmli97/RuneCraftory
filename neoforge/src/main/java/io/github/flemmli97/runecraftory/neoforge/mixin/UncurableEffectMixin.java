package io.github.flemmli97.runecraftory.neoforge.mixin;

import io.github.flemmli97.runecraftory.common.effects.UncurableEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.neoforge.common.EffectCure;
import net.neoforged.neoforge.common.extensions.IMobEffectExtension;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Set;

@Mixin(UncurableEffect.class)
public class UncurableEffectMixin implements IMobEffectExtension {

    @Override
    public void fillEffectCures(Set<EffectCure> cures, MobEffectInstance effectInstance) {
    }
}
