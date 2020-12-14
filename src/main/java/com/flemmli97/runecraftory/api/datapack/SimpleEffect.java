package com.flemmli97.runecraftory.api.datapack;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;

public class SimpleEffect {

    private final Effect potion;
    private final int duration;
    private final int amplifier;

    public SimpleEffect(Effect effect, int duration, int amplifier) {
        this.potion = effect;
        this.duration = duration;
        this.amplifier = amplifier;
    }

    public EffectInstance create() {
        return new EffectInstance(this.potion, this.duration, this.amplifier);
    }
}
