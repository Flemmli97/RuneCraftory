package io.github.flemmli97.runecraftory.common.particles;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Function;

public class ParticleTypeContainer<T extends ParticleOptions> extends ParticleType<T> {

    private final MapCodec<T> codec;
    private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;

    public ParticleTypeContainer(boolean alwaysShow, MapCodec<T> codec, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) {
        this(alwaysShow, t -> codec, t -> streamCodec);
    }

    public ParticleTypeContainer(boolean alwaysShow, Function<ParticleType<T>, MapCodec<T>> codecFactory,
                                 Function<ParticleType<T>, StreamCodec<RegistryFriendlyByteBuf, T>> streamCodecFactory) {
        super(alwaysShow);
        this.codec = codecFactory.apply(this);
        this.streamCodec = streamCodecFactory.apply(this);
    }

    @Override
    public MapCodec<T> codec() {
        return this.codec;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
        return this.streamCodec;
    }
}
