package com.flemmli97.runecraftory.client.particles;

import com.flemmli97.runecraftory.common.particles.ColoredParticleData;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;

public class ColoredParticle extends SpriteTexturedParticle {

    public final IAnimatedSprite spriteProvider;

    protected boolean randomMovements, gravity;

    protected ColoredParticle(ClientWorld world, double x, double y, double z, double motionX, double motionY, double motionZ,
                              ColoredParticleData colorData, IAnimatedSprite sprite, int maxAge, float minAgeRand, float maxAgeRand,
                              boolean collide, boolean randomMovements, boolean gravity) {
        super(world, x, y, z);
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;
        this.setColor(colorData.getRed(), colorData.getGreen(), colorData.getBlue());
        this.setAlphaF(colorData.getAlpha());
        float mult = MathHelper.nextFloat(world.rand, minAgeRand, maxAgeRand);
        this.maxAge = (int) (maxAge * mult);
        this.spriteProvider = sprite;
        this.selectSpriteWithAge(this.spriteProvider);
        this.particleScale *= colorData.getScale();
        this.canCollide = collide;
        this.randomMovements = randomMovements;
        this.gravity = gravity;
    }

    public ColoredParticle setScale(float scale) {
        this.particleScale = scale;
        return this;
    }

    @Override
    public float getScale(float partialTicks) {
        return this.particleScale * MathHelper.clamp(((float) this.age + partialTicks) / (float) this.maxAge * 32.0F, 0.0F, 1.0F);
    }

    @Override
    public IParticleRenderType getRenderType() {
        return ParticleRenderTypes.TRANSLUCENTADD;
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
            if (this.randomMovements) {
                if (this.posY == this.prevPosY) {
                    this.motionX *= 1.1D;
                    this.motionZ *= 1.1D;
                }

                this.motionX *= 0.96F;
                this.motionY *= 0.96F;
                this.motionZ *= 0.96F;
                if (this.onGround) {
                    this.motionX *= 0.7F;
                    this.motionZ *= 0.7F;
                }
            }
            if (this.gravity) {
                this.motionY = this.gravity();
                this.motionY = Math.max(this.motionY, this.maxGravity());
            }
        }
    }

    protected float gravity() {
        return -0.009f;
    }

    protected float maxGravity() {
        return -0.1f;
    }

    public static class LightParticleFactory implements IParticleFactory<ColoredParticleData> {

        private final IAnimatedSprite sprite;

        public LightParticleFactory(IAnimatedSprite sprite) {
            this.sprite = sprite;
        }

        @Override
        public Particle makeParticle(ColoredParticleData data, ClientWorld world, double x, double y, double z, double motionX, double motionY, double motionZ) {
            return new ColoredParticle(world, x, y, z, motionX, motionY, motionZ, data, this.sprite, 40, 0.7f, 1.3f, false, true, false);
        }
    }

    public static class NoGravityParticleFactory implements IParticleFactory<ColoredParticleData> {

        private final IAnimatedSprite sprite;

        public NoGravityParticleFactory(IAnimatedSprite sprite) {
            this.sprite = sprite;
        }

        @Override
        public Particle makeParticle(ColoredParticleData data, ClientWorld world, double x, double y, double z, double motionX, double motionY, double motionZ) {
            return new ColoredParticle(world, x, y, z, motionX, motionY, motionZ, data, this.sprite, 20, 1, 1, false, false, false).setScale(data.getScale());
        }
    }
}
