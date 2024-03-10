package io.github.flemmli97.runecraftory.client.npc;

import io.github.flemmli97.runecraftory.api.registry.NPCFeature;
import io.github.flemmli97.runecraftory.api.registry.NPCFeatureType;
import io.github.flemmli97.runecraftory.common.registry.ModNPCLooks;

import java.util.HashMap;
import java.util.Map;

public class NPCFeatureRenderers {

    private static final Map<NPCFeatureType<?>, NPCFeatureRenderer<?>> RENDERERS = new HashMap<>();

    public static void init() {
        register(ModNPCLooks.SLIM.get(), new SlimLookRenderer());
        register(ModNPCLooks.SIZE.get(), new SizeRenderer());
    }

    public static synchronized <F extends NPCFeature> void register(NPCFeatureType<F> type, NPCFeatureRenderer<F> renderer) {
        RENDERERS.put(type, renderer);
    }

    @SuppressWarnings("unchecked")
    public static <T extends NPCFeature> NPCFeatureRenderer<T> get(NPCFeature feature) {
        NPCFeatureRenderer<T> renderer = (NPCFeatureRenderer<T>) RENDERERS.get(feature.getType());
        if (renderer == null)
            throw new IllegalStateException("No npc feature renderer registered for " + feature);
        return renderer;
    }
}
