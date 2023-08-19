package io.github.flemmli97.runecraftory.api.datapack;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.common.effects.PermanentEffect;
import net.minecraft.core.Registry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

public class SimpleEffect {

    public static final Codec<SimpleEffect> CODEC = RecordCodecBuilder.create(simpleEff -> simpleEff.group(
            Registry.MOB_EFFECT.byNameCodec().fieldOf("potion").forGetter(SimpleEffect::getPotion),
            Codec.INT.fieldOf("duration").forGetter(SimpleEffect::getDuration),
            Codec.INT.fieldOf("amplifier").forGetter(d -> d.amplifier)).apply(simpleEff, SimpleEffect::new));

    private final MobEffect potion;
    private final int duration;
    private final int amplifier;

    public SimpleEffect(MobEffect effect, int duration, int amplifier) {
        this.potion = effect;
        this.duration = duration;
        this.amplifier = amplifier;
    }

    public MobEffect getPotion() {
        return this.potion;
    }

    public int getDuration() {
        return this.duration;
    }

    public int getAmplifier() {
        if (this.potion instanceof PermanentEffect)
            return Integer.MAX_VALUE;
        return this.amplifier;
    }

    public MobEffectInstance create() {
        return new MobEffectInstance(this.potion, this.duration, this.amplifier);
    }
}
