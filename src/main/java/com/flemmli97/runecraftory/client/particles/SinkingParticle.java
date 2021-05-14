package com.flemmli97.runecraftory.client.particles;

import com.flemmli97.runecraftory.common.particles.ColoredParticleData;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;

public class SinkingParticle extends ColoredParticle {

    protected SinkingParticle(ClientWorld world, double x, double y, double z, double motionX, double motionY, double motionZ, ColoredParticleData colorData, IAnimatedSprite sprite, int maxAge, float minAgeRand, float maxAgeRand, boolean collide) {
        super(world, x, y, z, motionX, motionY, motionZ, colorData, sprite, maxAge, minAgeRand, maxAgeRand, collide);
    }

    @Override
    public float getScale(float partialTicks) {
        return this.particleScale * (MathHelper.sin((float) ((this.age + partialTicks) / this.maxAge * Math.PI)) * 0.5f + 0.6f);
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
            this.motionY -= 0.009F;
            this.motionY = Math.max(this.motionY, -0.1F);
        }
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public static class Factory implements IParticleFactory<ColoredParticleData> {

        private final IAnimatedSprite sprite;

        public Factory(IAnimatedSprite sprite) {
            this.sprite = sprite;
        }

        @Override
        public Particle makeParticle(ColoredParticleData data, ClientWorld world, double x, double y, double z, double motionX, double motionY, double motionZ) {
            return new SinkingParticle(world, x, y, z, motionX, motionY, motionZ, data, this.sprite, 20, 0.8f, 1.2f, false);
        }
    }
}
