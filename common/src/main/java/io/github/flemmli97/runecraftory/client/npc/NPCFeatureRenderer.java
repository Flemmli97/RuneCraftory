package io.github.flemmli97.runecraftory.client.npc;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.api.registry.NPCFeature;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import net.minecraft.client.renderer.MultiBufferSource;

public class NPCFeatureRenderer<T extends NPCFeature> {

    /**
     * Use this if you want to change the setup before anything is rendered
     */
    public <E extends EntityNPCBase> void onSetup(T feature, RenderNPC<E> renderer, E entity, PoseStack stack) {
    }

    /**
     * Use this if want to apply tranformations to the PoseStack on the base model
     */
    public <E extends EntityNPCBase> void transformStack(T feature, RenderNPC<E> renderer, E entity, PoseStack stack, float partialTicks) {
    }

    /**
     * Do any other rendering here
     */
    public void render(T feature, EntityNPCBase entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
    }
}
