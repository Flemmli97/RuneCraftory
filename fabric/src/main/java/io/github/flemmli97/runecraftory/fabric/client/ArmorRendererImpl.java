package io.github.flemmli97.runecraftory.fabric.client;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.client.ArmorModels;
import io.github.flemmli97.runecraftory.client.model.ArmorSimpleItemModel;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public record ArmorRendererImpl(ArmorModels.ArmorModelGetter armorRenderer,
                                ResourceLocation texture) implements ArmorRenderer {

    @SuppressWarnings("unchecked")
    @Override
    public void render(PoseStack matrices, MultiBufferSource vertexConsumers, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, HumanoidModel<LivingEntity> contextModel) {
        Model model = this.armorRenderer.getModel(entity, stack, slot, contextModel);
        if (model == null) {
            model = ArmorModels.getDefaultArmorModel(slot);
            contextModel.copyPropertiesTo((HumanoidModel<LivingEntity>) model);
            copyVisibilitySettings(contextModel, (HumanoidModel<LivingEntity>) model);
        }
        if (model instanceof ArmorSimpleItemModel)
            model.renderToBuffer(matrices, null, light, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        else
            ArmorRenderer.renderPart(matrices, vertexConsumers, light, stack, model, this.texture);
    }

    public static void copyVisibilitySettings(HumanoidModel<?> from, HumanoidModel<?> to) {
        to.head.visible = from.head.visible;
        to.hat.visible = from.hat.visible;
        to.body.visible = from.body.visible;
        to.rightArm.visible = from.rightArm.visible;
        to.leftArm.visible = from.leftArm.visible;
        to.rightLeg.visible = from.rightLeg.visible;
        to.leftLeg.visible = from.leftLeg.visible;
    }
}
