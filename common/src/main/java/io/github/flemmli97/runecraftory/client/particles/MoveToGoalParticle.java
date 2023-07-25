package io.github.flemmli97.runecraftory.client.particles;

import io.github.flemmli97.runecraftory.common.particles.DurationalParticleData;
import io.github.flemmli97.tenshilib.client.particles.ColoredParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;

public class MoveToGoalParticle extends ColoredParticle {

    public MoveToGoalParticle(ClientLevel world, double x, double y, double z, double dirX, double dirY, double dirZ, DurationalParticleData colorData, SpriteSet sprite) {
        super(world, x, y, z, dirX, dirY, dirZ, colorData, sprite, 1, 1, 1, false, false, false);
        this.lifetime = colorData.getDuration();
        this.randomMovements = false;
        this.gravity = false;
    }

    public record ParticleFactoryBase(
            SpriteSet sprite) implements ParticleProvider<DurationalParticleData> {

        @Override
        public Particle createParticle(DurationalParticleData data, ClientLevel level, double x, double y, double z, double motionX, double motionY, double motionZ) {
            return new MoveToGoalParticle(level, x, y, z, motionX, motionY, motionZ, data, this.sprite);
        }
    }
}
