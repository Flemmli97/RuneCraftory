package io.github.flemmli97.runecraftory.common.particles;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleType;

public class BlockParticleType extends ParticleType<BlockStateParticleData> {

    private final Codec<BlockStateParticleData> codec;

    public BlockParticleType(boolean alwaysShow) {
        super(alwaysShow, BlockStateParticleData.DESERIALIZER);
        this.codec = BlockStateParticleData.codec(this);
    }

    @Override
    public Codec<BlockStateParticleData> codec() {
        return this.codec;
    }
}
