package io.github.flemmli97.runecraftory.client;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.client.model.ArmorSimpleItemModel;
import io.github.flemmli97.runecraftory.client.model.armor.PiyoSandals;
import io.github.flemmli97.runecraftory.client.model.armor.RingsArmorModel;
import io.github.flemmli97.runecraftory.common.items.equipment.ItemArmorBase;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ArmorModels {

    public static final Map<ResourceLocation, ArmorModelGetter> armorGetter = getArmorRenderer();
    private static final Map<ResourceLocation, FirstPersonArmorRenderer> firstPersonGetter = getFirstPersonHandRenderer();
    private static final Map<Item, ResourceLocation> armorTex = new HashMap<>();

    private static final ArmorSimpleItemModel itemModel = new ArmorSimpleItemModel();
    private static PiyoSandals piyoSandalsModel;
    private static RingsArmorModel ringsModel;

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static Map<ResourceLocation, ArmorModelGetter> getArmorRenderer() {
        ImmutableMap.Builder<ResourceLocation, ArmorModelGetter> builder = ImmutableMap.builder();
        builder.put(ModItems.magicEarrings.getID(), ((entityLiving, itemStack, slot, origin) -> {
            origin.setAllVisible(false);
            origin.head.visible = true;
            origin.hat.visible = true;
            return null;
        }));
        builder.put(ModItems.cheapBracelet.getID(), ((entityLiving, itemStack, slot, origin) -> {
            origin.setAllVisible(false);
            boolean right = entityLiving.getMainArm() == HumanoidArm.RIGHT;
            origin.rightArm.visible = right;
            origin.leftArm.visible = !right;
            return null;
        }));
        builder.put(ModItems.bronzeBracelet.getID(), ((entityLiving, itemStack, slot, origin) -> {
            origin.setAllVisible(false);
            boolean right = entityLiving.getMainArm() == HumanoidArm.RIGHT;
            origin.rightArm.visible = right;
            origin.leftArm.visible = !right;
            return null;
        }));
        builder.put(ModItems.silverBracelet.getID(), ((entityLiving, itemStack, slot, origin) -> {
            origin.setAllVisible(false);
            boolean right = entityLiving.getMainArm() == HumanoidArm.RIGHT;
            origin.rightArm.visible = right;
            origin.leftArm.visible = !right;
            return null;
        }));
        builder.put(ModItems.goldBracelet.getID(), ((entityLiving, itemStack, slot, origin) -> {
            origin.setAllVisible(false);
            boolean right = entityLiving.getMainArm() == HumanoidArm.RIGHT;
            origin.rightArm.visible = right;
            origin.leftArm.visible = !right;
            return null;
        }));
        builder.put(ModItems.platinumBracelet.getID(), ((entityLiving, itemStack, slot, origin) -> {
            origin.setAllVisible(false);
            boolean right = entityLiving.getMainArm() == HumanoidArm.RIGHT;
            origin.rightArm.visible = right;
            origin.leftArm.visible = !right;
            return null;
        }));
        ArmorModelGetter ribbons = (entityLiving, itemStack, slot, origin) -> {
            itemModel.setProperties(entityLiving, itemStack, origin.getHead(), ArmorSimpleItemModel.translateToHead);
            return itemModel;
        };
        for (RegistryEntrySupplier<Item> sup : ModItems.ribbons())
            builder.put(sup.getID(), ribbons);
        builder.put(ModItems.piyoSandals.getID(), ((entityLiving, itemStack, slot, origin) -> {
            origin.copyPropertiesTo((HumanoidModel) piyoSandalsModel);
            piyoSandalsModel.setAllVisible(false);
            piyoSandalsModel.leftLeg.visible = true;
            piyoSandalsModel.rightLeg.visible = true;
            return piyoSandalsModel;
        }));
        builder.put(ModItems.silverRing.getID(), ((entityLiving, itemStack, slot, origin) -> {
            origin.copyPropertiesTo((HumanoidModel) ringsModel);
            ringsModel.setAllVisible(false);
            boolean right = entityLiving.getMainArm() == HumanoidArm.RIGHT;
            ringsModel.rightArm.visible = right;
            ringsModel.leftArm.visible = !right;
            return ringsModel;
        }));
        return builder.build();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static Map<ResourceLocation, FirstPersonArmorRenderer> getFirstPersonHandRenderer() {
        ImmutableMap.Builder<ResourceLocation, FirstPersonArmorRenderer> builder = ImmutableMap.builder();
        builder.put(ModItems.silverRing.getID(), ((player, stack, right, origin, poseStack, buffer, light) -> {
            origin.copyPropertiesTo((HumanoidModel) ringsModel);
            ringsModel.setAllVisible(false);
            ModelPart model = right ? ringsModel.rightArm : ringsModel.leftArm;
            model.visible = true;
            VertexConsumer cons = forArmor(buffer, stack, player);
            if (cons != null)
                model.render(poseStack, cons, light, OverlayTexture.NO_OVERLAY);
        }));
        return builder.build();
    }

    private static VertexConsumer forArmor(MultiBufferSource buffer, ItemStack stack, Player player) {
        if (stack.getItem() instanceof ItemArmorBase armor)
            return ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(armorTex.computeIfAbsent(stack.getItem(), i -> new ResourceLocation(armor.getArmorTexture(stack, player, armor.getSlot(), null)))), false, stack.hasFoil());
        return null;
    }

    public static ArmorModelGetter fromItemStack(ItemStack stack) {
        if (stack.getItem() instanceof ItemArmorBase armor)
            return armorGetter.get(armor.registryID);
        return null;
    }

    public static FirstPersonArmorRenderer getFirstPersonRenderer(ItemStack stack) {
        if (stack.getItem() instanceof ItemArmorBase armor)
            return firstPersonGetter.get(armor.registryID);
        return null;
    }

    public static void initArmorModels(EntityRendererProvider.Context ctx) {
        piyoSandalsModel = new PiyoSandals(ctx.bakeLayer(PiyoSandals.LAYER_LOCATION));
        ringsModel = new RingsArmorModel(ctx.bakeLayer(RingsArmorModel.LAYER_LOCATION));
    }

    public interface ArmorModelGetter {
        Model getModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot slot, HumanoidModel<?> origin);
    }

    public interface FirstPersonArmorRenderer {
        void render(Player player, ItemStack stack, boolean rightArm, PlayerModel<?> arm, PoseStack poseStack, MultiBufferSource buffer, int light);
    }
}
