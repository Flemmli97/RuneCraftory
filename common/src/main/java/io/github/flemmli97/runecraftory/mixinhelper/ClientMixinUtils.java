package io.github.flemmli97.runecraftory.mixinhelper;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.api.enums.EnumSeason;
import io.github.flemmli97.runecraftory.client.ArmorModels;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.attachment.EntityData;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerWeaponHandler;
import io.github.flemmli97.runecraftory.common.utils.CalendarImpl;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class ClientMixinUtils {

    //Add
    public static int LEAVE_SPRING = 0x0d4a01;
    //Mult
    public static int LEAVE_FALL = 0xff4646;

    private static final Map<SeasonedTint, Integer> LEAVE_TINTS = new ConcurrentHashMap<>();
    private static final Map<SeasonedTint, Integer> GRASS_TINTS = new ConcurrentHashMap<>();

    public static int modifyColoredTint(BlockAndTintGetter getter, int old) {
        CalendarImpl calendar = ClientHandlers.clientCalendar;
        if (calendar.currentSeason() == EnumSeason.SUMMER)
            return old;
        return LEAVE_TINTS.computeIfAbsent(new SeasonedTint(old, calendar.currentSeason()), ClientMixinUtils::getLeaveTint);
    }

    public static int modifyColoredTintGrass(BlockAndTintGetter getter, int old) {
        CalendarImpl calendar = ClientHandlers.clientCalendar;
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

    public static void translateSleepingEntity(LivingEntity entity, PoseStack poseStack, float partialTicks) {
        if (EntityData.getSleepState(entity) == EntityData.SleepState.VANILLA) {
            float standOffset = entity.getEyeHeight(Pose.STANDING) - 0.1f;
            float f = Mth.rotLerp(partialTicks, entity.yBodyRotO, entity.yBodyRot);
            float xDir = -Mth.cos(-f * ((float) Math.PI / 180) - (float) Math.PI);
            float zDir = -Mth.sin(-f * ((float) Math.PI / 180) - (float) Math.PI);
            poseStack.translate(xDir * standOffset, 0.0, zDir * standOffset);
        }
    }

    public static void transFormPre(LivingEntity entity, HumanoidModel<?> model, Consumer<AnimatedAction> cons) {
        if (entity instanceof Player player && ClientHandlers.getAnimatedPlayerModel() != null) {
            AnimatedAction anim = Platform.INSTANCE.getPlayerData(player).map(d -> d.getWeaponHandler().getCurrentAnim()).orElse(null);
            cons.accept(anim);
            ClientHandlers.getAnimatedPlayerModel().setUpModel(anim, Minecraft.getInstance().getFrameTime());
            if (anim == null)
                ClientHandlers.getAnimatedPlayerModel().copyTo(entity, model, true, false);
        }
    }

    public static void transformHumanoidModel(LivingEntity entity, HumanoidModel<?> model, AnimatedAction anim) {
        if (entity instanceof Player && ClientHandlers.getAnimatedPlayerModel() != null && anim != null) {
            ClientHandlers.getAnimatedPlayerModel().copyTo(entity, model, false, anim.getID().equals(PlayerWeaponHandler.dualBladeUse.getID()));
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
