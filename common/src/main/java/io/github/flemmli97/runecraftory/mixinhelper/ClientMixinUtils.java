package io.github.flemmli97.runecraftory.mixinhelper;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.api.enums.EnumSeason;
import io.github.flemmli97.runecraftory.client.ArmorModels;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.client.ItemModelProps;
import io.github.flemmli97.runecraftory.common.attachment.EntityData;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.items.BigWeapon;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemDualBladeBase;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemGloveBase;
import io.github.flemmli97.runecraftory.common.registry.ModAttackActions;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.utils.CalendarImpl;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientMixinUtils {

    //Add
    public static final int LEAVE_SPRING = 0x0d4a01;
    //Mult
    public static final int LEAVE_FALL = 0xff4646;

    private static final Map<SeasonedTint, Integer> LEAVE_TINTS = new ConcurrentHashMap<>();
    private static final Map<SeasonedTint, Integer> GRASS_TINTS = new ConcurrentHashMap<>();

    public static int modifyColoredTint(BlockAndTintGetter getter, int old) {
        CalendarImpl calendar = ClientHandlers.CLIENT_CALENDAR;
        if (calendar.currentSeason() == EnumSeason.SUMMER)
            return old;
        return LEAVE_TINTS.computeIfAbsent(new SeasonedTint(old, calendar.currentSeason()), ClientMixinUtils::getLeaveTint);
    }

    public static int modifyColoredTintGrass(BlockAndTintGetter getter, int old) {
        CalendarImpl calendar = ClientHandlers.CLIENT_CALENDAR;
        if (calendar.currentSeason() == EnumSeason.SUMMER)
            return old;
        return GRASS_TINTS.computeIfAbsent(new SeasonedTint(old, calendar.currentSeason()), ClientMixinUtils::getGrassTint);
    }

    private static int getLeaveTint(SeasonedTint tint) {
        return switch (tint.season) {
            case SPRING -> desaturate(add(tint.origin, LEAVE_SPRING), 0.1f);
            case SUMMER -> tint.origin;
            case FALL -> desaturate(add(FastColor.ARGB32.multiply(tint.origin, LEAVE_FALL), 0x3c1e00), 0.2f);
            case WINTER -> desaturate(add(FastColor.ARGB32.multiply(tint.origin, LEAVE_FALL), 0x3c1e00), 0.6f);
        };
    }

    private static int getGrassTint(SeasonedTint tint) {
        return switch (tint.season) {
            case SPRING -> desaturate(add(tint.origin, LEAVE_SPRING), 0.1f);
            case SUMMER -> tint.origin;
            case FALL -> desaturate(tint.origin, 0.25f);
            case WINTER -> desaturate(tint.origin, 0.7f);
        };
    }

    private static int desaturate(int color, float perc) {
        int r = FastColor.ARGB32.red(color);
        int g = FastColor.ARGB32.green(color);
        int b = FastColor.ARGB32.blue(color);
        int gray = Math.max(b, Math.max(r, g));
        return FastColor.ARGB32.color(FastColor.ARGB32.alpha(color),
                (int) (r + (gray - r) * perc),
                (int) (g + (gray - g) * perc),
                (int) (b + (gray - b) * perc));
    }

    private static int add(int packedColourOne, int packedColorTwo) {
        return FastColor.ARGB32.color(Math.min(255, FastColor.ARGB32.alpha(packedColourOne) + FastColor.ARGB32.alpha(packedColorTwo)),
                Math.min(255, FastColor.ARGB32.red(packedColourOne) + FastColor.ARGB32.red(packedColorTwo)),
                Math.min(255, FastColor.ARGB32.green(packedColourOne) + FastColor.ARGB32.green(packedColorTwo)),
                Math.min(255, FastColor.ARGB32.blue(packedColourOne) + FastColor.ARGB32.blue(packedColorTwo)));
    }

    public static void translateSleepingEntity(LivingEntity entity, PoseStack poseStack, float flipDegrees) {
        if (EntityData.getSleepStateFrom(entity) == EntityData.SleepState.VANILLA && flipDegrees != 0) {
            poseStack.mulPose(Vector3f.XP.rotationDegrees(flipDegrees));
            float standOffset = entity.getEyeHeight(Pose.STANDING) * 0.6f;
            poseStack.translate(0, -standOffset, 0);
        }
    }

    public static boolean shouldAnimate(LivingEntity entity) {
        return ClientHandlers.getAnimatedPlayerModel() != null && entity instanceof Player;
    }

    public static void transformHumanoidModel(LivingEntity entity, HumanoidModel<?> model) {
        InteractionHand main = entity.getMainArm() == HumanoidArm.RIGHT ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        InteractionHand off = entity.getMainArm() == HumanoidArm.RIGHT ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        if (model.rightArmPose == HumanoidModel.ArmPose.ITEM && entity.getItemInHand(main).is(ModItems.UMBRELLA.get())) {
            model.rightArm.xRot -= 70 * Mth.DEG_TO_RAD;
        }
        if (model.leftArmPose == HumanoidModel.ArmPose.ITEM && entity.getItemInHand(off).is(ModItems.UMBRELLA.get())) {
            model.leftArm.xRot -= 70 * Mth.DEG_TO_RAD;
        }
        if (ClientHandlers.getAnimatedPlayerModel() != null && entity instanceof Player player) {
            PlayerData data = Platform.INSTANCE.getPlayerData(player).orElse(null);
            if (data == null)
                return;
            boolean ignoreRiding = Platform.INSTANCE.getPlayerData(player).map(d -> d.getWeaponHandler().getCurrentAction() == ModAttackActions.DUAL_USE.get()).orElse(false);
            float partialTicks = Minecraft.getInstance().getFrameTime();
            AnimatedAction anim = data.getWeaponHandler().getCurrentAnim();
            AnimatedAction last = data.getWeaponHandler().getLastAnim();
            float interpolation = data.getWeaponHandler().interpolatedLastChange(partialTicks);

            boolean result = ClientHandlers.getAnimatedPlayerModel().setUpModel(entity, model, anim, last, partialTicks, interpolation);
            if (result)
                ClientHandlers.getAnimatedPlayerModel().copyTo(model, ignoreRiding);
        }
    }

    public static void onRenderHand(PoseStack poseStack, AbstractClientPlayer player, boolean rightArm, PlayerModel<AbstractClientPlayer> arm, MultiBufferSource buffer, int combinedLight) {
        for (ItemStack stack : player.getArmorSlots()) {
            if (!stack.isEmpty()) {
                ArmorModels.FirstPersonArmorRenderer r = ArmorModels.getFirstPersonRenderer(stack);
                if (r != null) {
                    r.render(player, stack, rightArm, arm, poseStack, buffer, combinedLight);
                }
            }
        }
    }

    public static void adjustForHeldModel(ItemStack itemStack, ItemTransforms.TransformType transformType) {
        if (itemStack.getItem() instanceof ItemGloveBase || itemStack.getItem() instanceof BigWeapon) {
            ItemModelProps.HELD_TYPE = switch (transformType) {
                case FIRST_PERSON_LEFT_HAND, THIRD_PERSON_LEFT_HAND -> 1;
                case FIRST_PERSON_RIGHT_HAND, THIRD_PERSON_RIGHT_HAND -> 2;
                default -> 0;
            };
        } else if (itemStack.getItem() instanceof ItemDualBladeBase) {
            ItemModelProps.HELD_TYPE = switch (transformType) {
                case FIRST_PERSON_LEFT_HAND, THIRD_PERSON_LEFT_HAND, FIRST_PERSON_RIGHT_HAND, THIRD_PERSON_RIGHT_HAND ->
                        1;
                default -> 0;
            };
        }
    }

    public static void resetHeldModel() {
        ItemModelProps.HELD_TYPE = 0;
    }

    public static boolean onRenderHeldItem(LivingEntity livingEntity, ItemStack itemStack, ItemTransforms.TransformType transformType, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int combinedLight) {
        if (livingEntity instanceof AbstractClientPlayer player && transformType.firstPerson()) {
            PlayerData data = Platform.INSTANCE.getPlayerData(player).orElse(null);
            if (data != null && data.getWeaponHandler().getCurrentAnim() != null) {
                if (leftHand == (livingEntity.getMainArm() == HumanoidArm.RIGHT))
                    return true;
                transformType = transformType == ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND ?
                        ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND : ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND;
                poseStack.popPose();
                poseStack.pushPose();
                poseStack.scale(-1.0f, -1.0f, 1.0f);
                poseStack.translate(0, 0.06, 0);
                poseStack.scale(0.6f, 0.6f, 0.6f);
                PlayerRenderer playerRenderer = (PlayerRenderer) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(player);
                ClientHandlers.getAnimatedPlayerModel().setUpModel(player, playerRenderer.getModel(), data.getWeaponHandler().getCurrentAnim(), null, Minecraft.getInstance().getFrameTime(), 1);
                ClientHandlers.getAnimatedPlayerModel().copyTo(playerRenderer.getModel(), true);
                playerRenderer.getModel().leftArm.render(poseStack, buffer.getBuffer(RenderType.entitySolid(player.getSkinTextureLocation())), combinedLight, OverlayTexture.NO_OVERLAY);
                playerRenderer.getModel().rightArm.render(poseStack, buffer.getBuffer(RenderType.entitySolid(player.getSkinTextureLocation())), combinedLight, OverlayTexture.NO_OVERLAY);
                if (!ItemNBT.isInvis(itemStack)) {
                    poseStack.pushPose();
                    playerRenderer.getModel().translateToHand(leftHand ? HumanoidArm.LEFT : HumanoidArm.RIGHT, poseStack);
                    poseStack.mulPose(Vector3f.XP.rotationDegrees(-90.0f));
                    poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0f));
                    poseStack.translate((float) 1 / 16.0f, 0.125, -0.625);
                    Minecraft.getInstance().getItemRenderer().renderStatic(livingEntity, itemStack, transformType, leftHand, poseStack, buffer, livingEntity.level, combinedLight, OverlayTexture.NO_OVERLAY, livingEntity.getId() + transformType.ordinal());
                    poseStack.popPose();
                }
                itemStack = player.getOffhandItem();
                if (!itemStack.isEmpty() && !ItemNBT.isInvis(itemStack)) {
                    poseStack.pushPose();
                    playerRenderer.getModel().translateToHand(leftHand ? HumanoidArm.RIGHT : HumanoidArm.LEFT, poseStack);
                    poseStack.mulPose(Vector3f.XP.rotationDegrees(-90.0f));
                    poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0f));
                    poseStack.translate((float) -1 / 16.0f, 0.125, -0.625);
                    Minecraft.getInstance().getItemRenderer().renderStatic(livingEntity, itemStack, transformType, leftHand, poseStack, buffer, livingEntity.level, combinedLight, OverlayTexture.NO_OVERLAY, livingEntity.getId() + transformType.ordinal());
                    poseStack.popPose();
                }
                poseStack.popPose();
                poseStack.pushPose();
                return true;
            }
        }
        return ItemNBT.isInvis(itemStack);
    }

    record SeasonedTint(int origin, EnumSeason season) {

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof SeasonedTint tint)
                return tint.origin == this.origin && tint.season == this.season;
            return false;
        }

        @Override
        public int hashCode() {
            return (this.season + ";" + this.origin).hashCode();
        }
    }
}
