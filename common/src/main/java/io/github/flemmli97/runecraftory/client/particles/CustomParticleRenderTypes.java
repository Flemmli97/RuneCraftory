package io.github.flemmli97.runecraftory.client.particles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;

import java.util.HashSet;
import java.util.Set;

public class CustomParticleRenderTypes {

    private static final Set<RenderType> MODEL_TYPES = new HashSet<>();

    public static void batchType(RenderType type) {
        MODEL_TYPES.add(type);
    }

    public static void endBatch() {
        for (RenderType type : MODEL_TYPES) {
            Minecraft.getInstance().renderBuffers().bufferSource().endBatch(type);
        }
        MODEL_TYPES.clear();
    }
}
