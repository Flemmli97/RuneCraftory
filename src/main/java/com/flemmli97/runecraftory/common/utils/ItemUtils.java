package com.flemmli97.runecraftory.common.utils;

import com.flemmli97.runecraftory.api.datapack.ItemStat;
import com.flemmli97.runecraftory.api.enums.EnumCrafting;
import com.flemmli97.runecraftory.api.enums.EnumSkills;
import com.flemmli97.runecraftory.api.items.IItemUsable;
import com.flemmli97.runecraftory.common.capability.CapabilityInsts;
import com.flemmli97.runecraftory.common.capability.IPlayerCap;
import com.flemmli97.runecraftory.common.config.GeneralConfig;
import com.flemmli97.runecraftory.common.crafting.SextupleRecipe;
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
        ItemStack broadSword = new ItemStack(ModItems.broadSword.get());
        ItemStack hammer = new ItemStack(ModItems.hammerScrap.get());
        spawnItemAtEntity(player, broadSword);
        spawnItemAtEntity(player, hammer);
    }

    public static void spawnItemAtEntity(LivingEntity entity, ItemStack stack) {
        spawnItemAt(entity.world, entity.getBlockPos(), stack);
    }

    public static void spawnItemAt(World world, BlockPos pos, ItemStack stack) {
        if (!world.isRemote) {
            ItemNBT.initNBT(stack);
            ItemEntity item = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack);
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

    public static boolean canUpgrade(PlayerEntity player, EnumCrafting type, ItemStack stack, ItemStack ingredient) {
        return upgradeCost(type, player.getCapability(CapabilityInsts.PlayerCap).orElseThrow(EntityUtils::capabilityException), stack, ingredient, true) >= 0;
    }

    public static int upgradeCost(EnumCrafting type, IPlayerCap cap, ItemStack stack, ItemStack ingredient) {
        return upgradeCost(type, cap, stack, ingredient, false);
    }

    public static int upgradeCost(EnumCrafting type, IPlayerCap cap, ItemStack stack, ItemStack ingredient, boolean onlyIngredient) {
        int level = ItemNBT.itemLevel(stack);
        return DataPackHandler.getStats(ingredient.getItem()).map(stat -> {
            if (onlyIngredient || !stack.isEmpty()) {
                int skillLevel = type == EnumCrafting.FORGE ? cap.getSkillLevel(EnumSkills.FORGING)[0] : cap.getSkillLevel(EnumSkills.CRAFTING)[0];
                return level * (Math.max(1, stat.getDiff() - skillLevel)) * 3;
            }
            return -1;
        }).orElse(-1);
    }

    public static int craftingCost(EnumCrafting type, IPlayerCap cap, SextupleRecipe recipe) {
        if (GeneralConfig.recipeSystem == 1 || GeneralConfig.recipeSystem == 3)
            return recipe.getBaseCost();
        int skillLevel = 0;
        switch (type) {
            case FORGE:
                skillLevel = cap.getSkillLevel(EnumSkills.FORGING)[0];
                break;
            case ARMOR:
                skillLevel = cap.getSkillLevel(EnumSkills.CRAFTING)[0];
                break;
            case CHEM:
                skillLevel = cap.getSkillLevel(EnumSkills.CHEMISTRY)[0];
                break;
            case COOKING:
                skillLevel = cap.getSkillLevel(EnumSkills.COOKING)[0];
                break;
        }
        return Math.max(recipe.getCraftingLevel() - skillLevel, 1) * recipe.getBaseCost();
    }

    public static ItemStack getUpgradedStack(ItemStack stack, ItemStack ing) {
        if (ing.isEmpty() || !ItemNBT.shouldHaveStats(stack) || ItemNBT.itemLevel(stack) >= 10)
            return ItemStack.EMPTY;
        ItemStack output = stack.copy();
        ItemNBT.addUpgradeItem(output, ing);
        return output;
    }

    public static float getShieldEfficiency(PlayerEntity player) {
        if (player.getHeldItemMainhand().getItem() instanceof IItemUsable)
            return ((IItemUsable) player.getHeldItemMainhand().getItem()).getWeaponType().getShieldEfficiency();
        return 1;
    }
}
