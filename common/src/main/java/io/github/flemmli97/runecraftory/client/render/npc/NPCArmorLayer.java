package io.github.flemmli97.runecraftory.client.render.npc;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.client.model.HumanoidBasedModel;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;

public class NPCArmorLayer<T extends NPCEntity> extends RenderLayer<T, HumanoidBasedModel<T>> {

    private final HumanoidArmorLayer<T, HumanoidModel<T>, HumanoidModel<T>> wrapped;
    private final HumanoidArmorLayer<T, HumanoidModel<T>, HumanoidModel<T>> wrappedSlim;

    private boolean slim;

    public NPCArmorLayer(RenderLayerParent<T, HumanoidBasedModel<T>> renderer, RenderLayerParent<T, HumanoidModel<T>> modelFetcher, EntityRendererProvider.Context ctx) {
        super(renderer);
        this.wrapped = new HumanoidArmorLayer<>(modelFetcher, new HumanoidModel<>(ctx.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)),
                new HumanoidModel<>(ctx.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)), ctx.getModelManager()) {
            @Override
            protected void setPartVisibility(HumanoidModel model, EquipmentSlot slot) {
                NPCArmorLayer.stateRespectingVisibility(this.getParentModel(), model, slot);
            }
        };
        this.wrappedSlim = new HumanoidArmorLayer<>(modelFetcher, new HumanoidModel<>(ctx.bakeLayer(ModelLayers.PLAYER_SLIM_INNER_ARMOR)),
                new HumanoidModel<>(ctx.bakeLayer(ModelLayers.PLAYER_SLIM_OUTER_ARMOR)), ctx.getModelManager()) {
            @Override
            protected void setPartVisibility(HumanoidModel model, EquipmentSlot slot) {
                NPCArmorLayer.stateRespectingVisibility(this.getParentModel(), model, slot);
            }
        };
    }

    public static void stateRespectingVisibility(HumanoidModel<?> source, HumanoidModel<?> model, EquipmentSlot slot) {
        model.setAllVisible(false);
        switch (slot) {
            case HEAD:
                model.head.visible = source.head.visible;
                model.hat.visible = source.head.visible;
                break;
            case CHEST:
                model.body.visible = source.body.visible;
                model.rightArm.visible = source.rightArm.visible;
                model.leftArm.visible = source.leftArm.visible;
                break;
            case LEGS:
                model.body.visible = source.body.visible;
                model.rightLeg.visible = source.rightLeg.visible;
                model.leftLeg.visible = source.leftLeg.visible;
                break;
            case FEET:
                model.rightLeg.visible = source.rightLeg.visible;
                model.leftLeg.visible = source.leftLeg.visible;
        }
    }

    public void setSlim(boolean slim) {
        this.slim = slim;
    }

    @Override
    public void render(PoseStack matrixStack, MultiBufferSource buffer, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!this.slim)
            this.wrapped.render(matrixStack, buffer, packedLight, livingEntity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
        else
            this.wrappedSlim.render(matrixStack, buffer, packedLight, livingEntity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
    }
}
