package io.github.flemmli97.runecraftory.fabric.client;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.client.ArmorModels;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
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
            model = VanillaArmorModels.get(slot);
        }
        if (model instanceof HumanoidModel<?> humanoidModel) {
            contextModel.copyPropertiesTo((HumanoidModel<LivingEntity>) humanoidModel);
        }
        ArmorRenderer.renderPart(matrices, vertexConsumers, light, stack, model, this.texture);
    }
}
