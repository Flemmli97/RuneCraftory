package io.github.flemmli97.runecraftory.client.npc;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.entities.npc.features.SizeFeatureType;

public class SizeRenderer extends NPCFeatureRenderer<SizeFeatureType.SizeFeature> {

    @Override
    public <E extends EntityNPCBase> void transformStack(SizeFeatureType.SizeFeature feature, RenderNPC<E> renderer, E entity, PoseStack stack, float partialTicks) {
        if (feature.size != 1)
            stack.scale(feature.size, feature.size, feature.size);
    }
}
