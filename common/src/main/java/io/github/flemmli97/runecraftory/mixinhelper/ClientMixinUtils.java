package io.github.flemmli97.runecraftory.mixinhelper;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.github.flemmli97.runecraftory.api.calendar.Season;
import io.github.flemmli97.runecraftory.client.ClientCalendarHolder;
import io.github.flemmli97.runecraftory.client.ClientCalls;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.client.model.armor.ArmorModels;
import io.github.flemmli97.runecraftory.common.attachment.EntityData;
import io.github.flemmli97.runecraftory.common.attachment.WeaponHandler;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.config.ClientConfig;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttackActions;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryDataComponentTypes;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryItems;
import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
import io.github.flemmli97.runecraftory.common.world.data.Calendar;
import io.github.flemmli97.tenshilib.client.model.ModelPartsContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimatedEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientMixinUtils {

    //Add
    public static final int LEAVE_SPRING = 0x0d4a01;
    //Mult
    public static final int LEAVE_FALL = 0xff4646;

    private static final Map<SeasonedTint, Integer> LEAVE_TINTS = new ConcurrentHashMap<>();
    private static final Map<SeasonedTint, Integer> GRASS_TINTS = new ConcurrentHashMap<>();

    private static boolean AnimatedItemHandRendering;

    public static int modifyColoredTint(BlockAndTintGetter getter, int old) {
        Calendar calendar = ClientCalendarHolder.CLIENT_CALENDAR;
        if (calendar.currentSeason() == Season.SUMMER)
            return old;
        return LEAVE_TINTS.computeIfAbsent(new SeasonedTint(old, calendar.currentSeason()), ClientMixinUtils::getLeaveTint);
    }

    public static int modifyColoredTintGrass(BlockAndTintGetter getter, int old) {
        Calendar calendar = ClientCalendarHolder.CLIENT_CALENDAR;
        if (calendar.currentSeason() == Season.SUMMER)
            return old;
        return GRASS_TINTS.computeIfAbsent(new SeasonedTint(old, calendar.currentSeason()), ClientMixinUtils::getGrassTint);
    }

    private static int getLeaveTint(SeasonedTint tint) {
        return switch (tint.season) {
            case SPRING -> desaturate(add(tint.origin, LEAVE_SPRING), 0.1f);
            case SUMMER -> tint.origin;
            case AUTUMN -> desaturate(add(FastColor.ARGB32.multiply(tint.origin, LEAVE_FALL), 0x3c1e00), 0.2f);
            case WINTER -> desaturate(add(FastColor.ARGB32.multiply(tint.origin, LEAVE_FALL), 0x3c1e00), 0.6f);
        };
    }

    private static int getGrassTint(SeasonedTint tint) {
        return switch (tint.season) {
            case SPRING -> desaturate(add(tint.origin, LEAVE_SPRING), 0.1f);
            case SUMMER -> tint.origin;
            case AUTUMN -> desaturate(tint.origin, 0.25f);
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
            ClientCalls.SLEEP_ROTATED_TYPES.add(entity.getType());
            poseStack.translate(0, entity.getBbWidth() * 0.15, 0);
            if (entity.getPose() == Pose.SLEEPING) {
                poseStack.mulPose(Axis.ZP.rotationDegrees(entity.getYHeadRot() + 90));
            } else {
                poseStack.mulPose(Axis.YP.rotationDegrees(entity.yBodyRot - entity.getYHeadRot()));
                poseStack.mulPose(Axis.XP.rotationDegrees(flipDegrees));
            }
            float standOffset = entity.getEyeHeight(Pose.STANDING) * 0.6f;
            poseStack.translate(0, -standOffset, 0);
        }
    }

    public static boolean shouldAnimate(LivingEntity entity) {
        return entity instanceof Player || entity instanceof AnimatedEntity;
    }

    public static void transformHumanoidModel(LivingEntity entity, HumanoidModel<?> model, float partialTick) {
        InteractionHand main = entity.getMainArm() == HumanoidArm.RIGHT ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        InteractionHand off = entity.getMainArm() == HumanoidArm.RIGHT ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        if (model.rightArmPose == HumanoidModel.ArmPose.ITEM && entity.getItemInHand(main).is(RuneCraftoryItems.UMBRELLA.get())) {
            model.rightArm.xRot -= 70 * Mth.DEG_TO_RAD;
        }
        if (model.leftArmPose == HumanoidModel.ArmPose.ITEM && entity.getItemInHand(off).is(RuneCraftoryItems.UMBRELLA.get())) {
            model.leftArm.xRot -= 70 * Mth.DEG_TO_RAD;
        }
        if (!(entity instanceof Player player))
            return;
        WeaponHandler<Player> weaponHandler = RunecraftoryAttachments.PLAYER_DATA.get().get(player).getWeaponHandler();
        boolean ignoreRiding = weaponHandler.getCurrentAction() == RuneCraftoryAttackActions.DUAL_USE.get();
        boolean result = ClientHandlers.getAnimatedPlayerModel().setUpModel(player, model, weaponHandler.getAnimationHandler(), partialTick);
        if (result) {
            ClientHandlers.getAnimatedPlayerModel().copyTo(model);
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

    public static boolean onRenderHeldItem(LivingEntity livingEntity, ItemStack stack, ItemDisplayContext transformType, boolean leftHand, MultiBufferSource buffer, int combinedLight, float partialTick) {
        if (livingEntity instanceof AbstractClientPlayer player && transformType.firstPerson()) {
            leftHand = leftHand == (livingEntity.getMainArm() == HumanoidArm.RIGHT);
            if (leftHand) {
                return AnimatedItemHandRendering;
            }
            PlayerData data = RunecraftoryAttachments.PLAYER_DATA.get().get(player);
            if (data != null) {
                PlayerRenderer renderer = (PlayerRenderer) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(player);
                AnimatedItemHandRendering = ClientHandlers.getAnimatedPlayerModel().setUpModel(player, null, data.getWeaponHandler().getAnimationHandler(), partialTick);
                if (!AnimatedItemHandRendering) {
                    return false;
                }
                player.resetAttackStrengthTicker();
                PoseStack poseStack = new PoseStack();
                poseStack.pushPose();
                poseStack.scale(-0.5f, -0.5f, 0.5f);
                poseStack.translate(0, 0.1, 0);
                poseStack.mulPose(Axis.YP.rotationDegrees(Minecraft.getInstance().gameRenderer.getMainCamera().getYRot() - 180));
                PlayerModel<?> model = renderer.getModel();
                ClientHandlers.getAnimatedPlayerModel().copyTo(model);
                if (ClientConfig.renderHand) {
                    model.leftSleeve.visible = true;
                    model.leftSleeve.copyFrom(model.leftArm);
                    model.rightSleeve.visible = true;
                    model.rightSleeve.copyFrom(model.rightArm);
                    VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(player.getSkin().texture()));
                    model.leftArm.render(poseStack, consumer, combinedLight, OverlayTexture.NO_OVERLAY);
                    model.leftSleeve.render(poseStack, consumer, combinedLight, OverlayTexture.NO_OVERLAY);
                    model.rightArm.render(poseStack, consumer, combinedLight, OverlayTexture.NO_OVERLAY);
                    model.rightSleeve.render(poseStack, consumer, combinedLight, OverlayTexture.NO_OVERLAY);
                }
                ItemStack rightStack = player.getMainArm() == HumanoidArm.RIGHT ? stack : player.getOffhandItem();
                if (!rightStack.isEmpty() && !rightStack.has(RuneCraftoryDataComponentTypes.INVISIBLE.get())) {
                    poseStack.pushPose();
                    model.translateToHand(HumanoidArm.RIGHT, poseStack);
                    poseStack.mulPose(Axis.XP.rotationDegrees(-90.0f));
                    poseStack.mulPose(Axis.YP.rotationDegrees(180.0f));
                    poseStack.translate(1 / 16.0, 0.125, -0.625);
                    transformType = ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
                    Minecraft.getInstance().getItemRenderer().renderStatic(livingEntity, rightStack, transformType, false, poseStack, buffer, livingEntity.level(), combinedLight, OverlayTexture.NO_OVERLAY, livingEntity.getId() + transformType.ordinal());
                    poseStack.popPose();
                }
                ItemStack leftStack = player.getMainArm() == HumanoidArm.RIGHT ? player.getOffhandItem() : stack;
                if (!leftStack.isEmpty() && !stack.has(RuneCraftoryDataComponentTypes.INVISIBLE.get())) {
                    poseStack.pushPose();
                    model.translateToHand(HumanoidArm.LEFT, poseStack);
                    poseStack.mulPose(Axis.XP.rotationDegrees(-90.0f));
                    poseStack.mulPose(Axis.YP.rotationDegrees(180.0f));
                    poseStack.translate(-1 / 16.0, 0.125, -0.625);
                    transformType = ItemDisplayContext.THIRD_PERSON_LEFT_HAND;
                    Minecraft.getInstance().getItemRenderer().renderStatic(livingEntity, leftStack, transformType, true, poseStack, buffer, livingEntity.level(), combinedLight, OverlayTexture.NO_OVERLAY, livingEntity.getId() + transformType.ordinal());
                    poseStack.popPose();
                }
                poseStack.popPose();
                return true;
            }
        }
        AnimatedItemHandRendering = false;
        return stack.has(RuneCraftoryDataComponentTypes.INVISIBLE.get());
    }

    public static ModelPartsContainer.ModelPartExtended createPlayerItemPart(boolean left) {
        ModelPart item = new ModelPart(List.of(), Map.of());
        item.loadPose(PartPose.offset(left ? -1.0F : 1.0F, -8.0F, 0.0F));
        ModelPart root = new ModelPart(List.of(), Map.of(left ? "LeftItem" : "RightItem", item));
        root.loadPose(PartPose.offset(left ? 1.0F : -1.0F, 8.0F, 0.0F));
        return new ModelPartsContainer.ModelPartExtended("root", null, root);
    }

    record SeasonedTint(int origin, Season season) {

    }
}
