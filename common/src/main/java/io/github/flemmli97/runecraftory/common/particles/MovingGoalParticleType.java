package io.github.flemmli97.runecraftory.common.particles;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleType;

public class MovingGoalParticleType extends ParticleType<DurationalParticleData> {

    private final Codec<DurationalParticleData> codec;

    public MovingGoalParticleType(boolean alwaysShow) {
        super(alwaysShow, DurationalParticleData.DESERIALIZER);
        this.codec = DurationalParticleData.codec();
    }

    @Override
    public Codec<DurationalParticleData> codec() {
        return this.codec;
    }
}
