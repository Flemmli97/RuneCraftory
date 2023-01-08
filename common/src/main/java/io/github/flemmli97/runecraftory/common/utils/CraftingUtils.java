package io.github.flemmli97.runecraftory.common.utils;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.api.datapack.ItemStat;
import io.github.flemmli97.runecraftory.api.enums.EnumCrafting;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.crafting.SextupleRecipe;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.inventory.PlayerContainerInv;
import io.github.flemmli97.runecraftory.common.registry.ModCrafting;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

public class CraftingUtils {

    public static RecipeType<SextupleRecipe> getType(EnumCrafting type) {
        return switch (type) {
            case FORGE -> ModCrafting.FORGE.get();
            case ARMOR -> ModCrafting.ARMOR.get();
            case CHEM -> ModCrafting.CHEMISTRY.get();
            default -> ModCrafting.COOKING.get();
        };
    }

    public static boolean canUpgrade(Player player, EnumCrafting type, ItemStack stack, ItemStack ingredient) {
        return upgradeCost(type, Platform.INSTANCE.getPlayerData(player).orElseThrow(EntityUtils::playerDataException), stack, ingredient, true) >= 0;
    }

    public static int upgradeCost(EnumCrafting type, PlayerData data, ItemStack stack, ItemStack ingredient) {
        return upgradeCost(type, data, stack, ingredient, false);
    }

    public static int upgradeCost(EnumCrafting type, PlayerData data, ItemStack stack, ItemStack ingredient, boolean onlyIngredient) {
        if (!GeneralConfig.useRP)
            return 0;
        return DataPackHandler.itemStatManager().get(ingredient.getItem()).map(stat -> {
            if (stat.getDiff() <= 0)
                return -1;
            if (onlyIngredient || !stack.isEmpty()) {
                int skillLevel = type == EnumCrafting.FORGE ? data.getSkillLevel(EnumSkills.FORGING).getLevel() : data.getSkillLevel(EnumSkills.CRAFTING).getLevel();
                int result;
                if (skillLevel >= stat.getDiff()) {
                    result = stat.getDiff() * 2 + (ItemNBT.itemLevel(stack) - 1) * 2;
                } else {
                    int diff = stat.getDiff() - skillLevel;
                    int equip = ItemNBT.itemLevel(stack) - 1;
                    result = (4 * diff + 2) * stat.getDiff() // Base
                            + 2 * stat.getDiff() * (2 * equip) + equip * 2; //EquipmentLvl Addition
                }
                return result;
            }
            return -1;
        }).orElse(-1);
    }

    public static int craftingCost(EnumCrafting type, PlayerData data, SextupleRecipe recipe, NonNullList<ItemStack> bonusItems, boolean unlocked) {
        if (!GeneralConfig.useRP)
            return 0;
        if (GeneralConfig.recipeSystem.baseCost) {
            int cost = recipe.getBaseCost();
            int additionalMaterial = 0;
            for (ItemStack items : bonusItems) {
                additionalMaterial += DataPackHandler.itemStatManager().get(items.getItem()).map(ItemStat::getDiff).orElse(10);
            }
            cost += additionalMaterial;
            if (!unlocked && GeneralConfig.recipeSystem.lockedCostMore)
                cost *= 3;
            return cost;
        }
        int skillLevel = switch (type) {
            case FORGE -> data.getSkillLevel(EnumSkills.FORGING).getLevel();
            case ARMOR -> data.getSkillLevel(EnumSkills.CRAFTING).getLevel();
            case CHEM -> data.getSkillLevel(EnumSkills.CHEMISTRY).getLevel();
            case COOKING -> data.getSkillLevel(EnumSkills.COOKING).getLevel();
        };
        int lvlDifference = recipe.getCraftingLevel() - skillLevel;
        int cost;
        if (lvlDifference <= 0) {
            cost = Math.max(2 * recipe.getCraftingLevel() + lvlDifference, 10);
        } else {
            cost = Math.max(2 * recipe.getCraftingLevel(), 10) * (int) ((1 + lvlDifference) * 1.5);
        }
        int additionalMaterial = 0;
        for (ItemStack items : bonusItems) {
            additionalMaterial += DataPackHandler.itemStatManager().get(items.getItem()).map(s -> {
                if (skillLevel >= s.getDiff()) {
                    return s.getDiff();
                } else {
                    return (int) ((s.getDiff() - skillLevel) * 1.5) * (s.getDiff() - 5);
                }
            }).orElse(10);
        }
        cost += additionalMaterial;
        if (!unlocked && GeneralConfig.recipeSystem.lockedCostMore)
            cost *= 3;
        return cost;
    }

    public static ItemStack getUpgradedStack(ItemStack stack, ItemStack ing, EnumCrafting type) {
        return ItemNBT.addUpgradeItem(stack.copy(), ing, false, type);
    }

    private static float xpForCrafting(EnumSkills skill, SextupleRecipe recipe, int skillLevel) {
        float mult = LevelCalc.getSkillXpMultiplier(skill);
        float xp = (recipe.getCraftingLevel() * 2 + 10) * mult;
        if (skillLevel > recipe.getCraftingLevel())
            xp -= 2 * skillLevel - recipe.getCraftingLevel();
        else
            xp += (recipe.getCraftingLevel() - skillLevel) * recipe.getCraftingLevel();
        if (xp < 1)
            xp = 1;
        return xp;
    }

    public static void giveCraftingXPTo(ServerPlayer serverPlayer, PlayerData data, EnumSkills skill, SextupleRecipe recipe) {
        if (GeneralConfig.skillXpMultiplier == 0)
            return;
        data.increaseSkill(skill, serverPlayer, xpForCrafting(skill, recipe, data.getSkillLevel(skill).getLevel()) * GeneralConfig.skillXpMultiplier);
    }

    private static float xpForUpgrade(EnumSkills skill, ItemStack equip, ItemStack upgrade, int skillLevel) {
        float mult = LevelCalc.getSkillXpMultiplier(skill) * 1.5f;
        int difficulty = DataPackHandler.itemStatManager().get(upgrade.getItem()).map(ItemStat::getDiff).orElse(0);
        float xp = mult * (10 + ItemNBT.itemLevel(equip));
        if (skillLevel < difficulty)
            xp += 2 * difficulty - skillLevel;
        return xp;
    }

    public static void giveUpgradeXPTo(ServerPlayer serverPlayer, PlayerData data, EnumSkills skill, ItemStack equip, ItemStack upgrade) {
        if (GeneralConfig.skillXpMultiplier == 0)
            return;
        data.increaseSkill(skill, serverPlayer, xpForUpgrade(skill, equip, upgrade, data.getSkillLevel(skill).getLevel()) * GeneralConfig.skillXpMultiplier);
    }

    public static ItemStack getCraftingOutput(ItemStack stack, PlayerContainerInv inv, Pair<NonNullList<ItemStack>, NonNullList<ItemStack>> materials, EnumCrafting type) {
        if (type == EnumCrafting.COOKING) {
            for (ItemStack base : materials.getFirst()) {
                ItemNBT.addFoodBonusItem(stack, base);
            }
            for (ItemStack bonus : materials.getSecond()) {
                ItemNBT.addFoodBonusItem(stack, bonus);
            }
            return stack;
        }
        int i = 0;
        for (ItemStack bonus : materials.getSecond()) {
            i++;
            ItemNBT.addUpgradeItem(stack, bonus, true, type);
            if (i == 3)
                break;
        }
        NonNullList<ItemStack> recipeStacks = materials.getFirst();
        if (recipeStacks.size() > 3) {
            while (i < 3) {
                ItemStack rand = recipeStacks.get(inv.getPlayer().getRandom().nextInt(recipeStacks.size()));
                if (!rand.isEmpty()) {
                    recipeStacks.remove(rand);
                    ItemNBT.addUpgradeItem(stack, rand, true, type);
                }
                i++;
            }
        }
        return stack;
    }
}
