package io.github.flemmli97.runecraftory.common.particles;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleType;

public class SkelefangParticleType extends ParticleType<SkelefangParticleData> {

    private final Codec<SkelefangParticleData> codec;

    public SkelefangParticleType(boolean alwaysShow) {
        super(alwaysShow, SkelefangParticleData.DESERIALIZER);
        this.codec = SkelefangParticleData.codec();
    }

    @Override
    public Codec<SkelefangParticleData> codec() {
        return this.codec;
    }
}
