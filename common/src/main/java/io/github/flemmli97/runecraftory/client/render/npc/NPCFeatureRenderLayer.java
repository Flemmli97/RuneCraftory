package io.github.flemmli97.runecraftory.client.render.npc;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.api.registry.NPCFeature;
import io.github.flemmli97.runecraftory.client.model.HumanoidBasedModel;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.RenderLayer;

public class NPCFeatureRenderLayer<T extends NPCEntity> extends RenderLayer<T, HumanoidBasedModel<T>> {

    private final NPCRender<T> render;

    public NPCFeatureRenderLayer(NPCRender<T> renderer) {
        super(renderer);
        this.render = renderer;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        for (NPCFeature feature : entity.lookFeatures) {
            NPCFeatureRenderers.get(feature).render(feature, this.render, entity, poseStack, buffer, packedLight, partialTick,
                    limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        }
    }
}
