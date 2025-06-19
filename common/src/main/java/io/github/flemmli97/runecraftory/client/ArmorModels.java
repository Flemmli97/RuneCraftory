package io.github.flemmli97.runecraftory.client;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.ArmorSimpleItemModel;
import io.github.flemmli97.runecraftory.client.model.armor.CustomHumanoidArmorModel;
import io.github.flemmli97.runecraftory.common.items.equipment.ItemArmorBase;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.tenshilib.client.model.ModelPartsContainer;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Map;

public class ArmorModels {

    public static final Map<ResourceLocation, ArmorModelGetter> ARMOR_GETTER = getArmorRenderer();
    private static final Map<ResourceLocation, FirstPersonArmorRenderer> FIRST_PERSON_GETTER = getFirstPersonHandRenderer();

    private static final ArmorSimpleItemModel ITEM_MODEL = new ArmorSimpleItemModel();
    private static final CustomHumanoidArmorModel<?> PIYO_SANDALS_MODEL = new CustomHumanoidArmorModel<>(RuneCraftory.modRes("armor/piyo_sandals"));
    private static final CustomHumanoidArmorModel<?> RINGS_MODEL = new CustomHumanoidArmorModel<>(RuneCraftory.modRes("armor/rings"));

    private static HumanoidModel<?> INNER;
    private static HumanoidModel<?> OUTER;

    private static Map<ResourceLocation, ArmorModelGetter> getArmorRenderer() {
        ImmutableMap.Builder<ResourceLocation, ArmorModelGetter> builder = ImmutableMap.builder();
        builder.put(ModItems.MAGIC_EARRINGS.getID(), ((entityLiving, itemStack, slot, origin) -> {
            origin.setAllVisible(false);
            origin.head.visible = true;
            origin.hat.visible = true;
            return null;
        }));
        ArmorModelGetter bracelet = ((entityLiving, itemStack, slot, origin) -> {
            origin.setAllVisible(false);
            boolean right = entityLiving.getMainArm() == HumanoidArm.RIGHT;
            ModelPart model = right ? origin.rightArm : origin.leftArm;
            model.visible = true;
            if (entityLiving instanceof AbstractClientPlayer clientPlayer && clientPlayer.getSkin().model() == PlayerSkin.Model.SLIM) {
                model.x += right ? 0.5 : -0.5;
            }
            return null;
        });
        for (RegistryEntrySupplier<Item, ?> sup : bracelets())
            builder.put(sup.getID(), bracelet);
        ArmorModelGetter normalItemModel = (entityLiving, itemStack, slot, origin) -> {
            ITEM_MODEL.setProperties(entityLiving, itemStack, origin.getHead(), ArmorSimpleItemModel.TRANSLATE_TO_HEAD);
            return ITEM_MODEL;
        };
        for (RegistryEntrySupplier<Item, ?> sup : ModItems.ribbons())
            builder.put(sup.getID(), normalItemModel);
        builder.put(ModItems.PIYO_SANDALS.getID(), ((entityLiving, itemStack, slot, origin) -> {
            PIYO_SANDALS_MODEL.copyFrom(origin);
            PIYO_SANDALS_MODEL.setAllVisible(false);
            PIYO_SANDALS_MODEL.leftLeg.visible = true;
            PIYO_SANDALS_MODEL.rightLeg.visible = true;
            return PIYO_SANDALS_MODEL;
        }));
        for (RegistryEntrySupplier<Item, ?> sup : ModItems.hatItems())
            builder.put(sup.getID(), normalItemModel);
        ArmorModelGetter rings = ((entityLiving, itemStack, slot, origin) -> {
            RINGS_MODEL.copyFrom(origin);
            RINGS_MODEL.setAllVisible(false);
            boolean right = entityLiving.getMainArm() == HumanoidArm.RIGHT;
            ModelPartsContainer.ModelPartExtended model = right ? RINGS_MODEL.rightArm : RINGS_MODEL.leftArm;
            model.visible = true;
            if (entityLiving instanceof AbstractClientPlayer clientPlayer && clientPlayer.getSkin().model() == PlayerSkin.Model.SLIM) {
                model.x += right ? 0.5 : -0.5;
            }
            return RINGS_MODEL;
        });
        for (RegistryEntrySupplier<Item, ?> sup : rings())
            builder.put(sup.getID(), rings);
        return builder.build();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static Map<ResourceLocation, FirstPersonArmorRenderer> getFirstPersonHandRenderer() {
        ImmutableMap.Builder<ResourceLocation, FirstPersonArmorRenderer> builder = ImmutableMap.builder();
        FirstPersonArmorRenderer bracelet = (player, stack, right, origin, poseStack, buffer, light) -> {
            origin.copyPropertiesTo((HumanoidModel) OUTER);
            OUTER.setAllVisible(false);
            ModelPart model = right ? OUTER.rightArm : OUTER.leftArm;
            model.visible = true;
            if (player.getSkin().model() == PlayerSkin.Model.SLIM)
                model.x += right ? 0.5 : -0.5;
            renderModelPart(OUTER, player, buffer, stack, poseStack, light);
        };
        for (RegistryEntrySupplier<Item, ?> sup : bracelets())
            builder.put(sup.getID(), bracelet);
        FirstPersonArmorRenderer rings = (player, stack, right, origin, poseStack, buffer, light) -> {
            RINGS_MODEL.copyFrom(origin);
            RINGS_MODEL.setAllVisible(false);
            ModelPartsContainer.ModelPartExtended model = right ? RINGS_MODEL.rightArm : RINGS_MODEL.leftArm;
            model.visible = true;
            if (player.getSkin().model() == PlayerSkin.Model.SLIM)
                model.x += right ? 0.5 : -0.5;
            renderModelPart(RINGS_MODEL, player, buffer, stack, poseStack, light);
        };
        for (RegistryEntrySupplier<Item, ?> sup : rings())
            builder.put(sup.getID(), rings);
        return builder.build();
    }

    private static void renderModelPart(EntityModel<?> model, AbstractClientPlayer player, MultiBufferSource buffer, ItemStack stack, PoseStack poseStack, int light) {
        if (stack.getItem() instanceof ItemArmorBase armor) {
            for (ArmorMaterial.Layer layer : armor.getMaterial().value().layers()) {
                VertexConsumer cons = ItemRenderer.getArmorFoilBuffer(buffer,
                        RenderType.armorCutoutNoCull(armor.getArmorTexture(stack, player, armor.getEquipmentSlot(), layer, false)), stack.hasFoil());
                model.renderToBuffer(poseStack, cons, light, OverlayTexture.NO_OVERLAY);
            }
        }
    }

    public static ArmorModelGetter fromItemStack(ItemStack stack) {
        if (stack.getItem() instanceof ItemArmorBase armor)
            return ARMOR_GETTER.get(armor.armorPath);
        return null;
    }

    public static FirstPersonArmorRenderer getFirstPersonRenderer(ItemStack stack) {
        if (stack.getItem() instanceof ItemArmorBase armor)
            return FIRST_PERSON_GETTER.get(armor.armorPath);
        return null;
    }

    public static HumanoidModel<?> getDefaultArmorModel(EquipmentSlot slot) {
        return slot == EquipmentSlot.LEGS ? INNER : OUTER;
    }

    public static void initArmorModels(EntityRendererProvider.Context ctx) {
        INNER = new HumanoidModel<>(ctx.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR));
        OUTER = new HumanoidModel<>(ctx.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR));
    }

    private static List<RegistryEntrySupplier<Item, ?>> bracelets() {
        return List.of(ModItems.CHEAP_BRACELET, ModItems.BRONZE_BRACELET, ModItems.SILVER_BRACELET,
                ModItems.GOLD_BRACELET, ModItems.PLATINUM_BRACELET);
    }

    private static List<RegistryEntrySupplier<Item, ?>> rings() {
        return List.of(ModItems.SILVER_RING, ModItems.GOLD_RING, ModItems.PLATINUM_RING, ModItems.ENGAGEMENT_RING,
                ModItems.SHIELD_RING, ModItems.CRITICAL_RING, ModItems.SILENT_RING, ModItems.PARALYSIS_RING, ModItems.POISON_RING, ModItems.MAGIC_RING, ModItems.THROWING_RING, ModItems.STAY_UP_RING,
                ModItems.AQUAMARINE_RING, ModItems.AMETHYST_RING, ModItems.EMERALD_RING, ModItems.SAPPHIRE_RING, ModItems.RUBY_RING,
                ModItems.CURSED_RING, ModItems.DIAMOND_RING,
                ModItems.FIRE_RING, ModItems.WIND_RING, ModItems.WATER_RING, ModItems.EARTH_RING, ModItems.HAPPY_RING);
    }

    public interface ArmorModelGetter {

        Model getModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot slot, HumanoidModel<?> origin);
    }

    public interface FirstPersonArmorRenderer {

        void render(AbstractClientPlayer player, ItemStack stack, boolean rightArm, PlayerModel<?> arm, PoseStack poseStack, MultiBufferSource buffer, int light);
    }
}
