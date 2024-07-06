package io.github.flemmli97.runecraftory.client.particles;

import io.github.flemmli97.runecraftory.common.particles.ColoredParticleData4f;
import io.github.flemmli97.tenshilib.client.particles.ColoredParticle;
import io.github.flemmli97.tenshilib.client.particles.ParticleRenderTypes;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import io.github.flemmli97.tenshilib.common.utils.MathUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.world.phys.Vec3;

public class CirclingParticle extends ColoredParticle {

    private final double motionAX, motionAY, motionAZ, speedMod;
    private final float radInc, expansion;
    private double[] point;

    public CirclingParticle(ClientLevel world, double x, double y, double z, double dirX, double dirY, double dirZ, ColoredParticleData colorData, SpriteSet sprite, int maxAge, float minAgeRand, float maxAgeRand, double radius, double speedMod, float radAdd, float radInc, float expansion) {
        super(world, x, y, z, 0, 0, 0, colorData, sprite, maxAge, minAgeRand, maxAgeRand, false, false, false);
        if (dirX == 0 && dirY == 0 && dirZ == 0)
            dirY = 1;
        double len = Math.sqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        this.motionAX = dirX / len;
        this.motionAY = dirY / len;
        this.motionAZ = dirZ / len;
        this.speedMod = speedMod;
        len = Math.sqrt(dirX * dirX + dirY * dirY);
        this.point = new double[]{-dirY / len * radius, dirX / len * radius, 0};
        this.point = MathUtils.rotate(this.motionAX, this.motionAY, this.motionAZ, this.point[0], this.point[1], this.point[2], MathUtils.degToRad(radAdd));
        this.setPos(this.x + this.point[0] * 0.5, this.y + this.point[1] * 0.5, this.z);
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.radInc = MathUtils.degToRad(radInc);
        this.expansion = expansion;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            double[] prev = this.point;
            Vec3 dir = this.expansion == 0 ? Vec3.ZERO : new Vec3(this.point[0], this.point[1], this.point[2]).normalize().scale(this.expansion);
            this.point = MathUtils.rotate(this.motionAX, this.motionAY, this.motionAZ, this.point[0] + dir.x, this.point[1] + dir.y, this.point[2] + dir.z, this.radInc);
            this.setSpriteFromAge(this.spriteProvider);
            this.move(this.point[0] - prev[0] + this.motionAX * this.speedMod,
                    this.point[1] - prev[1] + this.motionAY * this.speedMod,
                    this.point[2] - prev[2] + this.motionAZ * this.speedMod);
        }
    }

    public record CirclingFactoryBase(
            SpriteSet sprite) implements ParticleProvider<ColoredParticleData4f> {

        @Override
        public Particle createParticle(ColoredParticleData4f data, ClientLevel level, double x, double y, double z, double motionX, double motionY, double motionZ) {
            return new CirclingParticle(level, x, y, z, motionX, motionY, motionZ, data, this.sprite, 25, 0.9f, 1.1f, data.getRadius(), data.getSpeed(), data.getOffset(), data.getAngleIncrease(), data.getExpansion()).setScale(data.getScale());
        }
    }
}
