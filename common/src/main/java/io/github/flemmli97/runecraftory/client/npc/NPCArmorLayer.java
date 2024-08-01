package io.github.flemmli97.runecraftory.client.npc;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;

public class NPCArmorLayer<T extends EntityNPCBase, M extends PlayerModel<T>, A extends HumanoidModel<T>> extends HumanoidArmorLayer<T, M, A> {

    private boolean render;

    public NPCArmorLayer(RenderLayerParent<T, M> renderer, A innerModel, A outerModel) {
        super(renderer, innerModel, outerModel);
    }

    public void setRender(boolean render) {
        this.render = render;
    }

    @Override
    public void render(PoseStack matrixStack, MultiBufferSource buffer, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!this.render)
            return;
        super.render(matrixStack, buffer, packedLight, livingEntity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
    }
}
