package io.github.flemmli97.runecraftory.common.utils;

import io.github.flemmli97.runecraftory.api.datapack.ItemStat;
import io.github.flemmli97.runecraftory.api.items.IItemUsable;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.entities.npc.job.EnumShopResult;
import io.github.flemmli97.runecraftory.common.registry.ModCriteria;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class ItemUtils {

    public static void starterItems(Player player) {
        ItemStack broadSword = new ItemStack(ModItems.broadSword.get());
        ItemStack hammer = new ItemStack(ModItems.hammerScrap.get());
        spawnItemAtEntity(player, broadSword);
        spawnItemAtEntity(player, hammer);
    }

    public static void spawnItemAtEntity(LivingEntity entity, ItemStack stack) {
        spawnItemAt(entity.level, entity.blockPosition(), stack, entity);
    }

    public static void spawnItemAt(Level level, BlockPos pos, ItemStack stack, @Nullable LivingEntity entity) {
        if (!level.isClientSide) {
            ItemEntity item = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), stack);
            item.setPickUpDelay(0);
            if (entity != null)
                item.setOwner(entity.getUUID());
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
        return DataPackHandler.INSTANCE.itemStatManager().get(stack.getItem()).map(stat -> getSellPrice(stack, stat)).orElse(0);
    }

    public static int getSellPrice(ItemStack stack, ItemStat stat) {
        return stat.getSell() * ItemNBT.itemLevel(stack);
    }

    public static int getBuyPrice(ItemStack stack) {
        return DataPackHandler.INSTANCE.itemStatManager().get(stack.getItem()).map(stat -> getBuyPrice(stack, stat)).orElse(0);
    }

    public static EnumShopResult buyItem(Player player, EntityNPCBase npc, ItemStack stack) {
        if (sizeInv(player.getInventory(), stack) < stack.getCount()) {
            player.playSound(SoundEvents.VILLAGER_NO, 1.0f, 1.0f);
            return EnumShopResult.NOSPACE;
        }
        int price = getBuyPrice(stack) * stack.getCount();
        if (Platform.INSTANCE.getPlayerData(player).map(d -> d.useMoney(player, price)).orElse(false)) {
            if (player instanceof ServerPlayer serverPlayer)
                ModCriteria.SHOP_TRIGGER.trigger(serverPlayer, npc, stack);
            player.playSound(SoundEvents.VILLAGER_YES, 1.0f, 1.0f);
            while (stack.getCount() > 0) {
                ItemStack copy = stack.copy();
                int count = Math.min(stack.getCount(), stack.getMaxStackSize());
                copy.setCount(count);
                spawnItemAtEntity(player, copy);
                stack.setCount(stack.getCount() - count);
            }
            return EnumShopResult.SUCCESS;
        }
        player.playSound(SoundEvents.VILLAGER_NO, 1.0f, 1.0f);
        return EnumShopResult.NOMONEY;
    }

    private static int sizeInv(Inventory playerInv, ItemStack stack) {
        int amount = 0;
        for (ItemStack iStack : playerInv.items) {
            if (iStack.isEmpty())
                amount += stack.getMaxStackSize();
            else if (ItemStack.isSameItemSameTags(stack, iStack))
                amount += stack.getMaxStackSize() - iStack.getCount();
        }
        return amount;
    }

    public static int getBuyPrice(ItemStack stack, ItemStat stat) {
        return stat.getBuy();
    }

    public static float getShieldEfficiency(LivingEntity entity) {
        return getShieldEfficiency(entity.getMainHandItem());
    }

    public static float getShieldEfficiency(ItemStack stack) {
        if (stack.getItem() instanceof IItemUsable usable) {
            float eff = usable.getWeaponType().shieldEfficiency;
            if (ItemNBT.hasDragonScaleUpgrade(stack))
                eff = Math.min(0.5f, eff + 0.5f);
            return eff;
        }
        return 1;
    }

    public static EquipmentSlot slotOf(ItemStack stack) {
        if (stack.getItem() instanceof ShieldItem)
            return EquipmentSlot.OFFHAND;
        return LivingEntity.getEquipmentSlotForItem(stack);
    }
}
