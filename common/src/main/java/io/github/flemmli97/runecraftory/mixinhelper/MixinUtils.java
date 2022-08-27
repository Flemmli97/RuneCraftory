package io.github.flemmli97.runecraftory.mixinhelper;

import io.github.flemmli97.runecraftory.api.items.IItemUsable;
import io.github.flemmli97.runecraftory.common.attachment.EntityData;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.HitResult;

public class MixinUtils {

    public static boolean stop(Player player, ItemStack stack, HitResult hitResult) {
        Item item = stack.getItem();
        return hitResult.getType() != HitResult.Type.BLOCK && item instanceof IItemUsable && player.getCooldowns().isOnCooldown(item);
    }

    public static boolean playerPose(Player player) {
        if (Platform.INSTANCE.getEntityData(player).map(EntityData::isSleeping).orElse(false)) {
            if (player.getPose() != Pose.SLEEPING)
                player.setPose(Pose.SLEEPING);
            return true;
        }
        return false;
    }
}
