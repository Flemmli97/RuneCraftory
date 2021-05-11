package com.flemmli97.runecraftory.client.particles;

import com.flemmli97.runecraftory.common.particles.ColoredParticleData;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;

public class WaveParticle extends ColoredParticle {

    protected WaveParticle(ClientWorld world, double x, double y, double z, double motionX, double motionY, double motionZ, ColoredParticleData colorData, IAnimatedSprite sprite, int maxAge, float minAgeRand, float maxAgeRand, boolean collide) {
        super(world, x, y, z, motionX, motionY, motionZ, colorData, sprite, maxAge, minAgeRand, maxAgeRand, collide);
        this.particleScale = colorData.getScale();
    }

    @Override
    public void tick() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.age++ >= this.maxAge) {
            this.setExpired();
        } else {
            this.selectSpriteWithAge(this.spriteProvider);
            this.move(this.motionX, this.motionY, this.motionZ);
        }
    }

    public static class Factory implements IParticleFactory<ColoredParticleData> {

        private final IAnimatedSprite sprite;

        public Factory(IAnimatedSprite sprite) {
            this.sprite = sprite;
        }

        @Override
        public Particle makeParticle(ColoredParticleData data, ClientWorld world, double x, double y, double z, double motionX, double motionY, double motionZ) {
            return new WaveParticle(world, x, y, z, motionX, motionY, motionZ, data, this.sprite, 20, 1, 1, false);
        }
    }
}
