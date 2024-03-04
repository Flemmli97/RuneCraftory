package io.github.flemmli97.runecraftory.client.npc;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.entities.npc.features.NPCFeature;
import net.minecraft.client.renderer.MultiBufferSource;

public class NPCFeatureRenderer<T extends NPCFeature> {


    public <E extends EntityNPCBase> void onSetup(T feature, RenderNPC<E> renderer, E entity) {
    }

    public void onRender(T feature, EntityNPCBase entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
    }
}
