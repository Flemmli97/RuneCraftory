package io.github.flemmli97.runecraftory.client.particles;

import io.github.flemmli97.tenshilib.client.particles.ColoredParticle;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.Mth;

public class SinkingParticle extends ColoredParticle {

    protected SinkingParticle(ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ, ColoredParticleData colorData, SpriteSet sprite, int maxAge, float minAgeRand, float maxAgeRand, boolean collide) {
        super(world, x, y, z, motionX, motionY, motionZ, colorData, sprite, maxAge, minAgeRand, maxAgeRand, collide, false, true);
    }

    @Override
    public float getQuadSize(float partialTicks) {
        return this.quadSize * (Mth.sin((float) ((this.age + partialTicks) / this.lifetime * Math.PI)) * 0.5f + 0.6f);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public record Factory(SpriteSet sprite) implements ParticleProvider<ColoredParticleData> {

        @Override
        public Particle createParticle(ColoredParticleData data, ClientLevel level, double x, double y, double z, double motionX, double motionY, double motionZ) {
            return new SinkingParticle(level, x, y, z, motionX, motionY, motionZ, data, this.sprite, 20, 0.8f, 1.2f, false);
        }
    }
}
