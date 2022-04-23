package io.github.flemmli97.runecraftory.common.particles;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleType;

public class ColoredParticle4fType extends ParticleType<ColoredParticleData4f> {

    private final Codec<ColoredParticleData4f> codec;

    public ColoredParticle4fType(boolean alwaysShow) {
        super(alwaysShow, ColoredParticleData4f.DESERIALIZER);
        this.codec = ColoredParticleData4f.codec4f(this);
    }

    @Override
    public Codec<ColoredParticleData4f> codec() {
        return this.codec;
    }
}
