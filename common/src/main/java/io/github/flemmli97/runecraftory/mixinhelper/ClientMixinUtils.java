package io.github.flemmli97.runecraftory.mixinhelper;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.flemmli97.runecraftory.api.enums.EnumSeason;
import io.github.flemmli97.runecraftory.client.ArmorModels;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.client.ItemModelProps;
import io.github.flemmli97.runecraftory.common.attachment.EntityData;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerWeaponHandler;
import io.github.flemmli97.runecraftory.common.items.BigWeapon;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemDualBladeBase;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemGloveBase;
import io.github.flemmli97.runecraftory.common.registry.ModAttackActions;
import io.github.flemmli97.runecraftory.common.registry.ModDataComponentTypes;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.utils.CalendarImpl;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.client.model.ModelPartsContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimatedEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
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
import net.minecraft.world.phys.Vec3;

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

    private static boolean ItemRenderContext;

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
            poseStack.mulPose(Axis.XP.rotationDegrees(flipDegrees));
            float standOffset = entity.getEyeHeight(Pose.STANDING) * 0.6f;
            poseStack.translate(0, -standOffset, 0);
        }
    }

    public static boolean shouldAnimate(LivingEntity entity) {
        return entity instanceof Player || entity instanceof AnimatedEntity;
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
        float partialTicks = ClientHandlers.getPartialTicks();
        if (entity instanceof AnimatedEntity) {
            boolean result = ClientHandlers.getAnimatedPlayerModel().setUpModel(entity, model, null, partialTicks);
            if (result)
                ClientHandlers.getAnimatedPlayerModel().copyTo(model);
            return;
        }
        PlayerWeaponHandler weaponHandler = entity instanceof Player player ? Platform.INSTANCE.getPlayerData(player).getWeaponHandler() : null;
        if (weaponHandler == null)
            return;
        boolean ignoreRiding = weaponHandler.getCurrentAction() == ModAttackActions.DUAL_USE.get();
        boolean result = ClientHandlers.getAnimatedPlayerModel().setUpModel(entity, model, weaponHandler, partialTicks);
        if (result) {
            ClientHandlers.getAnimatedPlayerModel().copyTo(model);
            if (ItemRenderContext) {
                model.setAllVisible(false);
                model.leftArm.visible = true;
                model.rightArm.visible = true;
                if (model instanceof PlayerModel<?> playerModel) {
                    playerModel.leftSleeve.copyFrom(model.leftArm);
                    playerModel.rightSleeve.copyFrom(model.rightArm);
                }
            }
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

    public static void adjustForHeldModel(ItemStack itemStack, ItemDisplayContext transformType) {
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

    public static boolean onRenderHeldItem(LivingEntity livingEntity, ItemStack stack, ItemDisplayContext transformType, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int combinedLight) {
        if (livingEntity instanceof AbstractClientPlayer player && transformType.firstPerson()) {
            PlayerData data = Platform.INSTANCE.getPlayerData(player);
            if (data != null) {
                PlayerRenderer playerRenderer = (PlayerRenderer) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(player);
                float partialTicks = ClientHandlers.getPartialTicks();
                boolean animated = ClientHandlers.getAnimatedPlayerModel().setUpModel(player, playerRenderer.getModel(), data.getWeaponHandler(), partialTicks);
                if (!animated)
                    return false;
                if (leftHand == (livingEntity.getMainArm() == HumanoidArm.RIGHT))
                    return true;
                player.resetAttackStrengthTicker();
                poseStack = new PoseStack();
                poseStack.pushPose();
                Vec3 camPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
                double camX = camPos.x();
                double camY = camPos.y();
                double camZ = camPos.z();
                Vec3 vec3 = playerRenderer.getRenderOffset(player, partialTicks);
                double x = Mth.lerp(partialTicks, player.xOld, player.getX());
                double y = Mth.lerp(partialTicks, player.yOld, player.getY());
                double z = Mth.lerp(partialTicks, player.zOld, player.getZ());
                x += vec3.x() - camX;
                y += vec3.y() - camY;
                z += vec3.z() - camZ;
                poseStack.translate(x, y, z);
                poseStack.translate(0, 0.1, 0.1);
                ItemRenderContext = true;
                playerRenderer.render(player, 0, partialTicks, poseStack, buffer, combinedLight);
                ItemRenderContext = false;
                poseStack.popPose();
                return true;
            }
        }
        return stack.has(ModDataComponentTypes.INVISIBLE.get());
    }

    public static ModelPartsContainer.ModelPartExtended createPlayerItemPart(boolean left) {
        ModelPart item = new ModelPart(List.of(), Map.of());
        item.loadPose(PartPose.offset(left ? -1.0F : 1.0F, -8.0F, 0.0F));
        ModelPart root = new ModelPart(List.of(), Map.of(left ? "LeftItem" : "RightItem", item));
        root.loadPose(PartPose.offset(left ? 1.0F : -1.0F, 8.0F, 0.0F));
        return new ModelPartsContainer.ModelPartExtended("root", null, root);
    }

    record SeasonedTint(int origin, EnumSeason season) {

    }
}
