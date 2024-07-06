package io.github.flemmli97.runecraftory.client.particles;

import io.github.flemmli97.runecraftory.common.particles.ColoredParticleData4f;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;

public class TornadoParticle extends CirclingParticle {

    public TornadoParticle(ClientLevel world, double x, double y, double z, double dirX, double dirY, double dirZ, ColoredParticleData colorData, SpriteSet sprite, int maxAge, float minAgeRand, float maxAgeRand, double radius, double speedMod, float radAdd, float radInc, float expansion) {
        super(world, x, y, z, dirX, dirY, dirZ, colorData, sprite, maxAge, minAgeRand, maxAgeRand, radius, speedMod, radAdd, radInc, expansion);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public record TornadoFactoryBase(
            SpriteSet sprite) implements ParticleProvider<ColoredParticleData4f> {

        @Override
        public Particle createParticle(ColoredParticleData4f data, ClientLevel level, double x, double y, double z, double motionX, double motionY, double motionZ) {
            return new TornadoParticle(level, x, y, z, motionX, motionY, motionZ, data, this.sprite, 25, 0.9f, 1.1f, data.getRadius(), data.getSpeed(), data.getOffset(), data.getAngleIncrease(), data.getExpansion()).setScale(data.getScale());
        }
    }
}
