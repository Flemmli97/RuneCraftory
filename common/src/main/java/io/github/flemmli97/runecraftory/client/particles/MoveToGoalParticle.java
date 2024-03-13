package io.github.flemmli97.runecraftory.client.particles;

import io.github.flemmli97.runecraftory.common.particles.DurationalParticleData;
import io.github.flemmli97.tenshilib.client.particles.ColoredParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.world.entity.Entity;

public class MoveToGoalParticle extends ColoredParticle {

    private final Entity anchor;
    protected double eXo;
    protected double eYo;
    protected double eZo;

    public MoveToGoalParticle(ClientLevel level, double x, double y, double z, double dirX, double dirY, double dirZ, DurationalParticleData colorData, SpriteSet sprite) {
        super(level, x, y, z, dirX, dirY, dirZ, colorData, sprite, 1, 1, 1, false, false, false);
        this.lifetime = colorData.getDuration();
        this.randomMovements = false;
        this.gravity = false;
        this.anchor = colorData.getEntityAnchor() != -1 ? level.getEntity(colorData.getEntityAnchor()) : null;
        if (this.anchor != null) {
            this.eXo = this.anchor.getX();
            this.eYo = this.anchor.getY();
            this.eZo = this.anchor.getZ();
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.anchor != null) {
            double x = this.anchor.getX();
            double mx = 0;
            if (x != this.eXo) {
                mx = x - this.eXo;
                this.eXo = x;
            }
            double y = this.anchor.getY();
            double my = 0;
            if (y != this.eYo) {
                my = -this.eYo + y;
                this.eYo = y;
            }
            double z = this.anchor.getZ();
            double mz = 0;
            if (z != this.eZo) {
                mz = -this.eZo + z;
                this.eZo = z;
            }
            if (mx != 0 || my != 0 || mz != 0)
                this.move(mx, my, mz);
        }
    }

    public record ParticleFactoryBase(
            SpriteSet sprite) implements ParticleProvider<DurationalParticleData> {

        @Override
        public Particle createParticle(DurationalParticleData data, ClientLevel level, double x, double y, double z, double motionX, double motionY, double motionZ) {
            return new MoveToGoalParticle(level, x, y, z, motionX, motionY, motionZ, data, this.sprite);
        }
    }
}
