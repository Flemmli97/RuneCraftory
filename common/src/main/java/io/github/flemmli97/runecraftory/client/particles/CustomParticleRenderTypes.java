package io.github.flemmli97.runecraftory.client.particles;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.texture.TextureManager;

public class CustomParticleRenderTypes {

    public static final ParticleRenderType ENTITY_MODEL_TYPE = new ParticleRenderType() {
        @Override
        public void begin(BufferBuilder builder, TextureManager manager) {
        }

        @Override
        public void end(Tesselator tessellator) {
            Minecraft.getInstance().renderBuffers().bufferSource().endLastBatch();
        }
    };
}
