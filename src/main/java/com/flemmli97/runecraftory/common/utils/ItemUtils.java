package com.flemmli97.runecraftory.common.utils;

import com.flemmli97.runecraftory.api.datapack.ItemStat;
import com.flemmli97.runecraftory.common.datapack.DataPackHandler;
import com.flemmli97.runecraftory.common.registry.ModItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemUtils {

    public static void starterItems(PlayerEntity player) {
        //ItemStack broadSword = new ItemStack(ModItems.broadSword);
        ItemStack hammer = new ItemStack(ModItems.hammerScrap.get());
        //spawnItemAtEntity(player, broadSword);
        spawnItemAtEntity(player, hammer);
    }

    public static void spawnItemAtEntity(LivingEntity entity, ItemStack stack) {
        spawnItemAt(entity.world, entity.getBlockPos(), stack);
    }

    public static void spawnItemAt(World world, BlockPos pos, ItemStack stack) {
        if (!world.isRemote) {
            ItemNBT.initNBT(stack);
            ItemEntity item = new ItemEntity(world, (double) pos.getX(), (double) pos.getY(), (double) pos.getZ(), stack);
            item.setPickupDelay(0);
            world.addEntity(item);
        }
    }

    public static void spawnLeveledItem(LivingEntity entity, ItemStack stack, int level) {
        if (!entity.world.isRemote) {
            ItemEntity item = new ItemEntity(entity.world, entity.getX(), entity.getY(), entity.getZ(), ItemNBT.getLeveledItem(stack, level));
            item.setPickupDelay(0);
            entity.world.addEntity(item);
        }
    }

    public static int getSellPrice(ItemStack stack) {
        ItemStat price = DataPackHandler.getStats(stack.getItem());
        if (price != null) {
            return price.getSell() * ItemNBT.itemLevel(stack);
        }
        return 0;
    }

    public static int getBuyPrice(ItemStack stack) {
        ItemStat price = DataPackHandler.getStats(stack.getItem());
        if (price != null) {
            return price.getBuy();
        }
        return 0;
    }
}
