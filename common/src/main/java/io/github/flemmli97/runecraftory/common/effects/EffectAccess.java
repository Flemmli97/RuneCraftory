package io.github.flemmli97.runecraftory.common.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class EffectAccess extends MobEffect {

    private EffectAccess(MobEffectCategory category, int color) {
        super(category, color);
    }

    public static MobEffect of(MobEffectCategory category, int color) {
        return new EffectAccess(category, color);
    }
}
