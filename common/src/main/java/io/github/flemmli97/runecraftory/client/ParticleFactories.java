package io.github.flemmli97.runecraftory.client;

import io.github.flemmli97.tenshilib.client.particles.ColoredParticle;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;

public class ParticleFactories {

    public static class ShortLightParticleFactory implements ParticleProvider<ColoredParticleData> {

        private final SpriteSet sprite;

        public ShortLightParticleFactory(SpriteSet sprite) {
            this.sprite = sprite;
        }

        @Override
        public Particle createParticle(ColoredParticleData data, ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ) {
            return new ColoredParticle(world, x, y, z, motionX, motionY, motionZ, data, this.sprite, 3, 0.8f, 1.2f, false, true, false);
        }
    }
}
