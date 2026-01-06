package io.github.flemmli97.runecraftory.client.render.npc;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.api.registry.NPCFeature;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import net.minecraft.client.renderer.MultiBufferSource;

public class NPCFeatureRenderer<T extends NPCFeature> {

    public static final NPCFeatureRenderer<?> EMPTY = new NPCFeatureRenderer<>();

    /**
     * Use this if you want to change the setup before anything is rendered
     */
    public <E extends NPCEntity> void onSetup(T feature, NPCRender<E> renderer, E entity, PoseStack stack) {
    }

    /**
     * Use this if want to apply tranformations to the PoseStack on the base model
     */
    public <E extends NPCEntity> void transformStack(T feature, NPCRender<E> renderer, E entity, PoseStack stack, float partialTick) {
    }

    /**
     * Do any other rendering here
     */
    public <E extends NPCEntity> void render(T feature, NPCRender<E> renderer, E entity, PoseStack poseStack, MultiBufferSource buffer,
                                             int packedLight, float partialTick,
                                             float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }
}
