package io.github.flemmli97.runecraftory.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class LightningParticle extends TextureSheetParticle {

    protected LightningParticle(ClientLevel clientLevel, double d, double e, double f, double motX, double motY, double motZ) {
        super(clientLevel, d, e, f, 0.0, 0.0, 0.0);
        this.xd = (Math.random() * 2.0 - 1.0) * motX;
        this.yd = (Math.random() * 2.0 - 1.0) * motY;
        this.zd = (Math.random() * 2.0 - 1.0) * motZ;
        this.lifetime = 16;
        this.hasPhysics = false;
        this.quadSize *= 0.6f;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public float getQuadSize(float scaleFactor) {
        return this.quadSize * Mth.clamp(((float) this.age + scaleFactor) / (float) this.lifetime * 32.0f, 0.0f, 1.0f);
    }

    public record Factory(SpriteSet sprite) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType data, ClientLevel level, double x, double y, double z, double motX, double motY, double motZ) {
            LightningParticle particle = new LightningParticle(level, x, y, z, motX, motY, motZ);
            particle.pickSprite(this.sprite);
            return particle;
        }
    }
}
