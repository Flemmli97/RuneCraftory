package io.github.flemmli97.runecraftory.api.datapack;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

public record SimpleEffect(Holder<MobEffect> effect, int duration, int amplifier) {

    public static final Codec<SimpleEffect> CODEC = RecordCodecBuilder.create(simpleEff ->
            simpleEff.group(BuiltInRegistries.MOB_EFFECT.holderByNameCodec().fieldOf("effect").forGetter(SimpleEffect::effect),
                    Codec.INT.fieldOf("duration").forGetter(SimpleEffect::duration),
                    Codec.INT.fieldOf("amplifier").forGetter(d -> d.amplifier)
            ).apply(simpleEff, SimpleEffect::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, SimpleEffect> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.holderRegistry(Registries.MOB_EFFECT),
            SimpleEffect::effect, ByteBufCodecs.INT, SimpleEffect::duration, ByteBufCodecs.INT, SimpleEffect::amplifier, SimpleEffect::new);

    public MobEffectInstance create() {
        return new MobEffectInstance(this.effect, this.duration, this.amplifier);
    }
}
