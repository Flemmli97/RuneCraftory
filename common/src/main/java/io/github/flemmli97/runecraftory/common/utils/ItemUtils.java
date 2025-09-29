package io.github.flemmli97.runecraftory.common.utils;

import io.github.flemmli97.runecraftory.api.datapack.ItemStat;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.entities.npc.profession.ShopResult;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryCriteria;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryItems;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class ItemUtils {

    public static void starterItems(Player player) {
        ItemStack broadSword = new ItemStack(RuneCraftoryItems.BROAD_SWORD.get());
        ItemStack hammer = new ItemStack(RuneCraftoryItems.HAMMER_SCRAP.get());
        spawnItemAtEntity(player, broadSword);
        spawnItemAtEntity(player, hammer);
    }

    public static void spawnItemAtEntity(LivingEntity entity, ItemStack stack) {
        spawnItemAt(entity.level(), entity.blockPosition(), stack, entity);
    }

    public static void spawnItemAt(Level level, BlockPos pos, ItemStack stack, @Nullable LivingEntity entity) {
        if (!level.isClientSide) {
            ItemEntity item = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), stack);
            item.setPickUpDelay(0);
            if (entity != null)
                item.setThrower(entity);
            level.addFreshEntity(item);
        }
    }

    public static void spawnLeveledItem(LivingEntity entity, ItemStack stack, int level) {
        if (!entity.level().isClientSide) {
            ItemEntity item = new ItemEntity(entity.level(), entity.getX(), entity.getY(), entity.getZ(), ItemComponentUtils.getLeveledItem(stack, level));
            item.setPickUpDelay(0);
            entity.level().addFreshEntity(item);
        }
    }

    public static int getSellPrice(ItemStack stack) {
        return DataPackHandler.INSTANCE.itemStatManager().get(stack.getItem()).map(stat -> getSellPrice(stack, stat)).orElse(0);
    }

    public static int getSellPrice(ItemStack stack, ItemStat stat) {
        return stat.getSell() * ItemComponentUtils.itemLevel(stack);
    }

    public static int getBuyPrice(ItemStack stack) {
        return DataPackHandler.INSTANCE.itemStatManager().get(stack.getItem()).map(stat -> getBuyPrice(stack, stat)).orElse(0);
    }

    public static ShopResult buyItem(Player player, NPCEntity npc, ItemStack stack) {
        if (sizeInv(player.getInventory(), stack) < stack.getCount()) {
            player.playSound(RuneCraftorySounds.GENERIC_DENY.get(), 1.0f, 1.0f);
            return ShopResult.NOSPACE;
        }
        int price = getBuyPrice(stack) * stack.getCount();
        if (Platform.INSTANCE.getPlayerData(player).useMoney(price)) {
            if (player instanceof ServerPlayer serverPlayer)
                RuneCraftoryCriteria.SHOP_TRIGGER.get().trigger(serverPlayer, npc, stack);
            player.playSound(RuneCraftorySounds.GENERIC_SUCCESS.get(), 1.0f, 1.0f);
            while (stack.getCount() > 0) {
                ItemStack copy = stack.copy();
                int count = Math.min(stack.getCount(), stack.getMaxStackSize());
                copy.setCount(count);
                spawnItemAtEntity(player, copy);
                stack.setCount(stack.getCount() - count);
            }
            return ShopResult.SUCCESS;
        }
        player.playSound(RuneCraftorySounds.GENERIC_DENY.get(), 1.0f, 1.0f);
        return ShopResult.NOMONEY;
    }

    private static int sizeInv(Inventory playerInv, ItemStack stack) {
        int amount = 0;
        for (ItemStack iStack : playerInv.items) {
            if (iStack.isEmpty())
                amount += stack.getMaxStackSize();
            else if (ItemStack.isSameItemSameComponents(stack, iStack))
                amount += stack.getMaxStackSize() - iStack.getCount();
        }
        return amount;
    }

    public static int getBuyPrice(ItemStack stack, ItemStat stat) {
        return stat.getBuy();
    }
}
