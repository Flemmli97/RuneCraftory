package com.flemmli97.runecraftory.client.particles;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;

public class PuffParticle extends SpriteTexturedParticle {

    protected PuffParticle(ClientWorld world, double x, double y, double z, IAnimatedSprite sprite) {
        super(world, x, y, z);
        this.particleGravity = 0;
        this.maxAge = 40;
        this.canCollide = false;
        this.selectSpriteRandomly(sprite);
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Factory  implements IParticleFactory<BasicParticleType> {

        private final IAnimatedSprite sprite;

        public Factory(IAnimatedSprite sprite) {
            this.sprite = sprite;
        }

        @Override
        public Particle makeParticle(BasicParticleType data, ClientWorld world, double x, double y, double z, double speedX, double speedY, double speedZ) {
            return new PuffParticle(world, x, y, z, this.sprite);
        }
    }
}
