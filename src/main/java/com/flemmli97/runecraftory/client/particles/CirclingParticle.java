package com.flemmli97.runecraftory.client.particles;

import com.flemmli97.runecraftory.common.particles.ColoredParticleData;
import com.flemmli97.tenshilib.common.utils.MathUtils;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;

public class CirclingParticle extends ColoredParticle{

    private double motionAX, motionAY, motionAZ, speedMod;
    private double[] point;
    private final float radInc;

    public CirclingParticle(ClientWorld world, double x, double y, double z, double dirX, double dirY, double dirZ, ColoredParticleData colorData, IAnimatedSprite sprite, int maxAge, float minAgeRand, float maxAgeRand, double radius, double speedMod, float radAdd, float radInc) {
        super(world, x, y, z, 0, 0, 0, colorData, sprite, maxAge, minAgeRand, maxAgeRand, false, false, false);
        if(dirX == 0 && dirY == 0 && dirZ == 0)
            dirY = 1;
        double len = Math.sqrt(dirX*dirX + dirY * dirY + dirZ * dirZ);
        this.motionAX = dirX/len;
        this.motionAY = dirY/len;
        this.motionAZ = dirZ/len;
        this.speedMod = speedMod;
        len = Math.sqrt(dirX*dirX + dirY * dirY);
        this.point = new double[]{-dirY/len * radius, dirX/len * radius, 0};
        this.point = MathUtils.rotate(this.motionAX, this.motionAY, this.motionAZ, this.point[0], this.point[1], this.point[2], MathUtils.degToRad(radAdd));
        this.setPosition(this.posX + this.point[0] * 0.5, this.posY + this.point[1] * 0.5, this.posZ);
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.radInc = MathUtils.degToRad(radInc);
    }

    @Override
    public void tick() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.age++ >= this.maxAge) {
            this.setExpired();
        } else {
            double[] prev = this.point;
            this.point = MathUtils.rotate(this.motionAX, this.motionAY, this.motionAZ, this.point[0], this.point[1], this.point[2], this.radInc);
            this.selectSpriteWithAge(this.spriteProvider);
            this.move(this.point[0] - prev[0] + this.motionAX* this.speedMod,
                    this.point[1] - prev[1] + this.motionAY* this.speedMod,
                    this.point[2] - prev[2] + this.motionAZ* this.speedMod);
        }
    }

    public static class CirclingFactoryBase implements IParticleFactory<ColoredParticleData> {

        private final IAnimatedSprite sprite;

        public CirclingFactoryBase(IAnimatedSprite sprite) {
            this.sprite = sprite;
        }

        @Override
        public Particle makeParticle(ColoredParticleData data, ClientWorld world, double x, double y, double z, double motionX, double motionY, double motionZ) {
            return new CirclingParticle(world, x, y, z, motionX, motionY, motionZ, data, this.sprite, 25, 1, 1, 0.6, 0.24, 0, 80).setScale(data.getScale());
        }
    }
}
