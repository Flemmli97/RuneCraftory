package io.github.flemmli97.runecraftory.client.npc;

import io.github.flemmli97.runecraftory.api.datapack.NPCData;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.entities.npc.features.SlimLookFeature;

public class SlimLookRenderer extends NPCFeatureRenderer<SlimLookFeature> {

    @Override
    public <E extends EntityNPCBase> void onSetup(SlimLookFeature feature, RenderNPC<E> renderer, E entity) {
        if (!NPCData.NPCLook.DEFAULT_SKIN.equals(renderer.getTextureLocation(entity)))
            renderer.setModel(renderer.slim);
    }
}
