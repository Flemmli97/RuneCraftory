package io.github.flemmli97.runecraftory.common.particles;

import com.mojang.serialization.Codec;
import net.minecraft.particles.ParticleType;

public class ColoredParticleType extends ParticleType<ColoredParticleData> {

    private final Codec<ColoredParticleData> codec;

    public ColoredParticleType(boolean alwaysShow) {
        super(alwaysShow, ColoredParticleData.DESERIALIZER);
        this.codec = ColoredParticleData.codec(this);
    }

    @Override
    public Codec<ColoredParticleData> func_230522_e_() {
        return this.codec;
    }
}
