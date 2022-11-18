package io.github.flemmli97.runecraftory.mixinhelper;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.github.flemmli97.runecraftory.api.items.IItemUsable;
import io.github.flemmli97.runecraftory.common.attachment.EntityData;
import io.github.flemmli97.runecraftory.common.lib.LibConstants;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
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

    public static Multimap<Attribute, AttributeModifier> getStats(ItemStack stack, Multimap<Attribute, AttributeModifier> map, EquipmentSlot slot) {
        if (ItemNBT.shouldHaveStats(stack) && Mob.getEquipmentSlotForItem(stack) == slot) {
            Multimap<Attribute, AttributeModifier> multimap = HashMultimap.create();
            ItemNBT.statIncrease(stack).forEach((att, d) -> multimap.put(att, new AttributeModifier(LibConstants.EQUIPMENT_MODIFIERS[slot.ordinal()], "rf.stat_increase", d, AttributeModifier.Operation.ADDITION)));
            return multimap;
        }
        return map;
    }
}
