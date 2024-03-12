package io.github.flemmli97.runecraftory.client.particles;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureManager;

import java.util.HashSet;
import java.util.Set;

public class CustomParticleRenderTypes {

    private static final Set<RenderType> MODEL_TYPES = new HashSet<>();

    public static void batchType(RenderType type) {
        MODEL_TYPES.add(type);
    }

    public static final ParticleRenderType ENTITY_MODEL_TYPE = new ParticleRenderType() {
        @Override
        public void begin(BufferBuilder builder, TextureManager manager) {
        }

        @Override
        public void end(Tesselator tessellator) {
            for (RenderType type : MODEL_TYPES) {
                Minecraft.getInstance().renderBuffers().bufferSource().endBatch(type);
            }
            MODEL_TYPES.clear();
        }
    };
}
