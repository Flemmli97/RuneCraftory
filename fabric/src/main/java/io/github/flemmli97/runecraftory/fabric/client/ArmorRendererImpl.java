package io.github.flemmli97.runecraftory.fabric.client;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.client.ArmorModels;
import io.github.flemmli97.runecraftory.client.model.ArmorSimpleItemModel;
import io.github.flemmli97.tenshilib.common.item.DynamicArmorTextureItem;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.CommonColors;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.component.DyedItemColor;

import java.util.function.Supplier;

public class ArmorRendererImpl implements ArmorRenderer {

    private final Supplier<TextureAtlas> atlas = Suppliers.memoize(() ->
            Minecraft.getInstance().getModelManager().getAtlas(Sheets.ARMOR_TRIMS_SHEET));


    @SuppressWarnings("unchecked")
    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, HumanoidModel<LivingEntity> contextModel) {
        if (!(stack.getItem() instanceof ArmorItem item))
            return;
        ArmorModels.ArmorModelGetter getter = ArmorModels.fromItemStack(stack);
        Model model = getter != null ? getter.getModel(entity, stack, slot, contextModel) : null;
        if (model == null) {
            model = ArmorModels.getDefaultArmorModel(slot);
            contextModel.copyPropertiesTo((HumanoidModel<LivingEntity>) model);
            copyVisibilitySettings(contextModel, (HumanoidModel<LivingEntity>) model);
        }
        if (model instanceof ArmorSimpleItemModel)
            model.renderToBuffer(poseStack, null, light, OverlayTexture.NO_OVERLAY, CommonColors.WHITE);
        else {
            ArmorMaterial armormaterial = item.getMaterial().value();
            int color = stack.is(ItemTags.DYEABLE) ? FastColor.ARGB32.opaque(DyedItemColor.getOrDefault(stack, DyedItemColor.LEATHER_COLOR)) : CommonColors.WHITE;
            boolean inner = slot == EquipmentSlot.LEGS;
            for (ArmorMaterial.Layer layer : armormaterial.layers()) {
                int dyeColor = layer.dyeable() ? color : CommonColors.WHITE;
                ResourceLocation texture = item instanceof DynamicArmorTextureItem dynamic ?
                        dynamic.getArmorTexture(stack, entity, slot, layer, inner) : layer.texture(inner);
                this.renderModel(poseStack, buffer, light, model, dyeColor, texture);
            }

            ArmorTrim armorTrim = stack.get(DataComponents.TRIM);
            if (armorTrim != null) {
                this.renderTrim(item.getMaterial(), poseStack, buffer, light, armorTrim, model, inner);
            }

            if (stack.hasFoil()) {
                this.renderGlint(poseStack, buffer, light, model);
            }
        }
    }

    private void renderModel(PoseStack stack, MultiBufferSource source, int light, Model model, int dyeColor, ResourceLocation texture) {
        VertexConsumer vertexConsumer = source.getBuffer(RenderType.armorCutoutNoCull(texture));
        model.renderToBuffer(stack, vertexConsumer, light, OverlayTexture.NO_OVERLAY, dyeColor);
    }

    private void renderTrim(Holder<ArmorMaterial> material, PoseStack stack, MultiBufferSource source, int light, ArmorTrim trim, Model model, boolean innerTexture) {
        TextureAtlasSprite textureAtlasSprite = this.atlas.get().getSprite(innerTexture ? trim.innerTexture(material) : trim.outerTexture(material));
        VertexConsumer vertexConsumer = textureAtlasSprite.wrap(source.getBuffer(Sheets.armorTrimsSheet((trim.pattern().value()).decal())));
        model.renderToBuffer(stack, vertexConsumer, light, OverlayTexture.NO_OVERLAY);
    }

    private void renderGlint(PoseStack stack, MultiBufferSource source, int light, Model model) {
        model.renderToBuffer(stack, source.getBuffer(RenderType.armorEntityGlint()), light, OverlayTexture.NO_OVERLAY);
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
