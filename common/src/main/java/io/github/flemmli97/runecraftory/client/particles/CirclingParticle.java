package io.github.flemmli97.runecraftory.client.particles;

import io.github.flemmli97.runecraftory.common.particles.ColoredParticleData4f;
import io.github.flemmli97.tenshilib.client.particles.ColoredParticle;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.Mth;
import org.joml.Vector3d;

public class CirclingParticle extends ColoredParticle {

    private final double motionAX, motionAY, motionAZ, speedMod;
    private final float angleIncrease, expansion;
    // Since Vector3d is mutable make sure not to mutate!
    private final Vector3d circleVec;

    private double angle, radius;

    public CirclingParticle(ClientLevel world, double x, double y, double z, double dirX, double dirY, double dirZ, ColoredParticleData colorData, SpriteSet sprite, int maxAge, float minAgeRand, float maxAgeRand, double radius, double speedMod, float radAdd, float angleIncrease, float expansion) {
        super(world, x, y, z, 0, 0, 0, colorData, sprite, maxAge, minAgeRand, maxAgeRand, false, false, false);
        if (dirX == 0 && dirY == 0 && dirZ == 0)
            dirY = 1;
        double len = Math.sqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        this.motionAX = dirX / len;
        this.motionAY = dirY / len;
        this.motionAZ = dirZ / len;
        this.speedMod = speedMod;
        this.angleIncrease = angleIncrease * Mth.DEG_TO_RAD;
        this.expansion = expansion;
        this.circleVec = new Vector3d(-this.motionAY, this.motionAX, 0).normalize();

        this.radius = radius;
        this.angle = radAdd * Mth.DEG_TO_RAD;
        this.moveParticle();
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.moveParticle();
        }
    }

    protected void moveParticle() {
        this.setSpriteFromAge(this.spriteProvider);
        if (this.expansion == 0 && this.angleIncrease == 0) {
            this.setPos(this.x + this.motionAX * this.speedMod,
                    this.y + this.motionAY * this.speedMod,
                    this.z + this.motionAZ * this.speedMod);
        } else {
            Vector3d offset = this.expansion == 0 ? new Vector3d(this.circleVec) : this.circleVec.mul(this.radius, new Vector3d());
            offset.rotateAxis(this.angle, this.motionAX, this.motionAY, this.motionAZ);
            this.setPos(this.x + offset.x() + this.motionAX * this.speedMod,
                    this.y + offset.y() + this.motionAY * this.speedMod,
                    this.z + offset.z() + this.motionAZ * this.speedMod);
        }
        this.angle += this.angleIncrease;
        this.radius += this.expansion;
    }

    public record CirclingFactoryBase(SpriteSet sprite) implements ParticleProvider<ColoredParticleData4f> {

        @Override
        public Particle createParticle(ColoredParticleData4f data, ClientLevel level, double x, double y, double z, double motionX, double motionY, double motionZ) {
            return new CirclingParticle(level, x, y, z, motionX, motionY, motionZ, data, this.sprite,
                    1000, 0.9f, 1.1f,
                    data.getRadius(), data.getSpeed(), data.getOffset(), data.getAngleIncrease(), data.getExpansion()).setScale(data.getScale());
        }
    }
}
