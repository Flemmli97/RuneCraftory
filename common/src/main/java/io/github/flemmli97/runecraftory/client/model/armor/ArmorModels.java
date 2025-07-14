package io.github.flemmli97.runecraftory.client.model.armor;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.items.equipment.ItemArmorBase;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryItems;
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
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Map;

public class ArmorModels {

    private static final Map<Item, ArmorModelGetter> ARMOR_GETTER = getArmorRenderer();
    private static final Map<Item, FirstPersonArmorRenderer> FIRST_PERSON_GETTER = getFirstPersonHandRenderer();

    private static final SimpleItemArmorModel ITEM_MODEL = new SimpleItemArmorModel();
    private static final CustomHumanoidArmorModel<?> PIYO_SANDALS_MODEL = new CustomHumanoidArmorModel<>(RuneCraftory.modRes("armor/piyo_sandals"));
    private static final CustomHumanoidArmorModel<?> RINGS_MODEL = new CustomHumanoidArmorModel<>(RuneCraftory.modRes("armor/rings"));

    private static HumanoidModel<?> INNER;
    private static HumanoidModel<?> OUTER;

    private static Map<Item, ArmorModelGetter> getArmorRenderer() {
        ImmutableMap.Builder<Item, ArmorModelGetter> builder = ImmutableMap.builder();
        builder.put(RuneCraftoryItems.MAGIC_EARRINGS.get(), ((entityLiving, itemStack, slot, origin) -> {
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
            builder.put(sup.get(), bracelet);
        ArmorModelGetter normalItemModel = (entityLiving, itemStack, slot, origin) -> {
            ITEM_MODEL.setProperties(entityLiving, itemStack, origin.getHead(), SimpleItemArmorModel.TRANSLATE_TO_HEAD);
            return ITEM_MODEL;
        };
        for (RegistryEntrySupplier<Item, ?> sup : RuneCraftoryItems.ribbons())
            builder.put(sup.get(), normalItemModel);
        builder.put(RuneCraftoryItems.PIYO_SANDALS.get(), ((entityLiving, itemStack, slot, origin) -> {
            PIYO_SANDALS_MODEL.copyFrom(origin);
            PIYO_SANDALS_MODEL.setAllVisible(false);
            PIYO_SANDALS_MODEL.leftLeg.visible = true;
            PIYO_SANDALS_MODEL.rightLeg.visible = true;
            return PIYO_SANDALS_MODEL;
        }));
        for (RegistryEntrySupplier<Item, ?> sup : RuneCraftoryItems.hatItems())
            builder.put(sup.get(), normalItemModel);
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
            builder.put(sup.get(), rings);
        return builder.build();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static Map<Item, FirstPersonArmorRenderer> getFirstPersonHandRenderer() {
        ImmutableMap.Builder<Item, FirstPersonArmorRenderer> builder = ImmutableMap.builder();
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
            builder.put(sup.get(), bracelet);
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
            builder.put(sup.get(), rings);
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
        return ARMOR_GETTER.get(stack.getItem());
    }

    public static FirstPersonArmorRenderer getFirstPersonRenderer(ItemStack stack) {
        return FIRST_PERSON_GETTER.get(stack.getItem());
    }

    public static HumanoidModel<?> getDefaultArmorModel(EquipmentSlot slot) {
        return slot == EquipmentSlot.LEGS ? INNER : OUTER;
    }

    public static void initArmorModels(EntityRendererProvider.Context ctx) {
        INNER = new HumanoidModel<>(ctx.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR));
        OUTER = new HumanoidModel<>(ctx.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR));
    }

    private static List<RegistryEntrySupplier<Item, ?>> bracelets() {
        return List.of(RuneCraftoryItems.CHEAP_BRACELET, RuneCraftoryItems.BRONZE_BRACELET, RuneCraftoryItems.SILVER_BRACELET,
                RuneCraftoryItems.GOLD_BRACELET, RuneCraftoryItems.PLATINUM_BRACELET);
    }

    private static List<RegistryEntrySupplier<Item, ?>> rings() {
        return List.of(RuneCraftoryItems.SILVER_RING, RuneCraftoryItems.GOLD_RING, RuneCraftoryItems.PLATINUM_RING, RuneCraftoryItems.ENGAGEMENT_RING,
                RuneCraftoryItems.SHIELD_RING, RuneCraftoryItems.CRITICAL_RING, RuneCraftoryItems.SILENT_RING, RuneCraftoryItems.PARALYSIS_RING, RuneCraftoryItems.POISON_RING, RuneCraftoryItems.MAGIC_RING, RuneCraftoryItems.THROWING_RING, RuneCraftoryItems.STAY_UP_RING,
                RuneCraftoryItems.AQUAMARINE_RING, RuneCraftoryItems.AMETHYST_RING, RuneCraftoryItems.EMERALD_RING, RuneCraftoryItems.SAPPHIRE_RING, RuneCraftoryItems.RUBY_RING,
                RuneCraftoryItems.CURSED_RING, RuneCraftoryItems.DIAMOND_RING,
                RuneCraftoryItems.FIRE_RING, RuneCraftoryItems.WIND_RING, RuneCraftoryItems.WATER_RING, RuneCraftoryItems.EARTH_RING, RuneCraftoryItems.HAPPY_RING);
    }

    public interface ArmorModelGetter {

        Model getModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot slot, HumanoidModel<?> origin);
    }

    public interface FirstPersonArmorRenderer {

        void render(AbstractClientPlayer player, ItemStack stack, boolean rightArm, PlayerModel<?> arm, PoseStack poseStack, MultiBufferSource buffer, int light);
    }
}
