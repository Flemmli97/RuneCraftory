package io.github.flemmli97.runecraftory.client.npc;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.api.datapack.NPCData;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.entities.npc.features.SlimLookFeatureType;

public class SlimLookRenderer extends NPCFeatureRenderer<SlimLookFeatureType.SlimLookFeature> {

    @Override
    public <E extends EntityNPCBase> void onSetup(SlimLookFeatureType.SlimLookFeature feature, RenderNPC<E> renderer, E entity, PoseStack stack) {
        if (!NPCData.NPCLook.DEFAULT_SKIN.equals(renderer.getTextureLocation(entity)))
            renderer.setModel(renderer.slim);
    }
}
