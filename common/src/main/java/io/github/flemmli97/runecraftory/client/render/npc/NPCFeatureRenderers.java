package io.github.flemmli97.runecraftory.client.render.npc;

import io.github.flemmli97.runecraftory.api.registry.NPCFeature;
import io.github.flemmli97.runecraftory.api.registry.NPCFeatureType;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryNPCLooks;

import java.util.HashMap;
import java.util.Map;

public class NPCFeatureRenderers {

    private static final Map<NPCFeatureType<?>, NPCFeatureRenderer<?>> RENDERERS = new HashMap<>();

    public static void init() {
        empty(RuneCraftoryNPCLooks.SLIM.get());
        register(RuneCraftoryNPCLooks.SIZE.get(), new SizeRenderer());
        empty(RuneCraftoryNPCLooks.SKIN.get());
        empty(RuneCraftoryNPCLooks.FACE.get());
        empty(RuneCraftoryNPCLooks.BLUSH.get());
        empty(RuneCraftoryNPCLooks.HAIR.get());
        empty(RuneCraftoryNPCLooks.OUTFIT.get());
        empty(RuneCraftoryNPCLooks.HAT.get());
    }

    public static synchronized <F extends NPCFeature> void empty(NPCFeatureType<F> type) {
        RENDERERS.put(type, NPCFeatureRenderer.EMPTY);
    }

    public static synchronized <F extends NPCFeature> void register(NPCFeatureType<F> type, NPCFeatureRenderer<F> renderer) {
        RENDERERS.put(type, renderer);
    }

    @SuppressWarnings("unchecked")
    public static <T extends NPCFeature> NPCFeatureRenderer<T> get(NPCFeature feature) {
        NPCFeatureRenderer<T> renderer = (NPCFeatureRenderer<T>) RENDERERS.get(feature.type());
        if (renderer == null)
            throw new IllegalStateException("No npc feature renderer registered for " + feature);
        return renderer;
    }
}
