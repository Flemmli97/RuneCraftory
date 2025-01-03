package io.github.flemmli97.runecraftory.common.particles;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

public class GenericParticleType<T extends ParticleOptions> extends ParticleType<T> {

    private final Codec<T> codec;

    public GenericParticleType(boolean alwaysShow, ParticleOptions.Deserializer<T> deserializer, Codec<T> codec) {
        super(alwaysShow, deserializer);
        this.codec = codec;
    }

    @Override
    public Codec<T> codec() {
        return this.codec;
    }
}
