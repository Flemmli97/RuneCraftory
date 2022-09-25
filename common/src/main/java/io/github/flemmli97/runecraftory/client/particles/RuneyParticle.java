package io.github.flemmli97.runecraftory.client.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class RuneyParticle extends TextureSheetParticle {

    RuneyParticle(ClientLevel clientLevel, double d, double e, double f) {
        super(clientLevel, d, e, f, 0.0, 0.0, 0.0);
        this.alpha = 0.8f;
        this.friction = 0.86f;
        this.xd *= 0.01f;
        this.yd = 0.1;
        this.zd *= 0.01f;
        this.quadSize *= 1.3f;
        this.lifetime = 16;
        this.hasPhysics = false;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        this.alpha = Mth.sin((this.age + partialTicks) * 0.2f) * 0.4f + 0.2f;
        super.render(buffer, renderInfo, partialTicks);
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprite;

        public Provider(SpriteSet spriteSet) {
            this.sprite = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            RuneyParticle particle = new RuneyParticle(level, x, y, z);
            particle.pickSprite(this.sprite);
            return particle;
        }
    }
}