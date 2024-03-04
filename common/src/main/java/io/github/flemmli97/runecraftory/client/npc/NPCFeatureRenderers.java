package io.github.flemmli97.runecraftory.client.npc;

import io.github.flemmli97.runecraftory.common.entities.npc.features.NPCFeature;
import io.github.flemmli97.runecraftory.common.entities.npc.features.NPCFeatureType;
import io.github.flemmli97.runecraftory.common.registry.ModNPCLooks;

import java.util.HashMap;
import java.util.Map;

public class NPCFeatureRenderers {

    private static final Map<NPCFeatureType<?>, NPCFeatureRenderer<?>> RENDERERS = new HashMap<>();

    public static void init() {
        register(ModNPCLooks.SLIM.get(), new SlimLookRenderer());
    }

    public static synchronized <T extends NPCFeature> void register(NPCFeatureType<T> type, NPCFeatureRenderer<T> renderer) {
        RENDERERS.put(type, renderer);
    }

    @SuppressWarnings("unchecked")
    public static <T extends NPCFeature> NPCFeatureRenderer<T> get(T feature) {
        NPCFeatureRenderer<T> renderer = (NPCFeatureRenderer<T>) RENDERERS.get(feature.getType());
        if (renderer == null)
            throw new IllegalStateException("No npc feature renderer registered for " + feature.getType());
        return renderer;
    }
}
