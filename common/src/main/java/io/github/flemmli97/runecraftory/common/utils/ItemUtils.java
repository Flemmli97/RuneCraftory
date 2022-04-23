package io.github.flemmli97.runecraftory.common.utils;

import io.github.flemmli97.runecraftory.api.datapack.ItemStat;
import io.github.flemmli97.runecraftory.api.items.IItemUsable;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemUtils {

    public static void starterItems(Player player) {
        ItemStack broadSword = new ItemStack(ModItems.broadSword.get());
        ItemStack hammer = new ItemStack(ModItems.hammerScrap.get());
        spawnItemAtEntity(player, broadSword);
        spawnItemAtEntity(player, hammer);
    }

    public static void spawnItemAtEntity(LivingEntity entity, ItemStack stack) {
        spawnItemAt(entity.level, entity.blockPosition(), stack);
    }

    public static void spawnItemAt(Level level, BlockPos pos, ItemStack stack) {
        if (!level.isClientSide) {
            ItemNBT.initNBT(stack);
            ItemEntity item = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), stack);
            item.setPickUpDelay(0);
            level.addFreshEntity(item);
        }
    }

    public static void spawnLeveledItem(LivingEntity entity, ItemStack stack, int level) {
        if (!entity.level.isClientSide) {
            ItemEntity item = new ItemEntity(entity.level, entity.getX(), entity.getY(), entity.getZ(), ItemNBT.getLeveledItem(stack, level));
            item.setPickUpDelay(0);
            entity.level.addFreshEntity(item);
        }
    }

    public static int getSellPrice(ItemStack stack) {
        return DataPackHandler.getStats(stack.getItem()).map(stat -> getSellPrice(stack, stat)).orElse(0);
    }

    public static int getSellPrice(ItemStack stack, ItemStat stat) {
        return stat.getSell() * ItemNBT.itemLevel(stack);
    }

    public static int getBuyPrice(ItemStack stack) {
        return DataPackHandler.getStats(stack.getItem()).map(stat -> getBuyPrice(stack, stat)).orElse(0);
    }

    public static int getBuyPrice(ItemStack stack, ItemStat stat) {
        return stat.getBuy();
    }

    public static float getShieldEfficiency(Player player) {
        if (player.getMainHandItem().getItem() instanceof IItemUsable)
            return ((IItemUsable) player.getMainHandItem().getItem()).getWeaponType().getShieldEfficiency();
        return 1;
    }
}
