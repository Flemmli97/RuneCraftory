package io.github.flemmli97.runecraftory.common.utils;

import io.github.flemmli97.runecraftory.api.enums.EnumCrafting;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.attachment.PlayerData;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.crafting.SextupleRecipe;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.registry.ModCrafting;
import io.github.flemmli97.runecraftory.platform.Platform;
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
        int level = ItemNBT.itemLevel(stack);
        return DataPackHandler.getStats(ingredient.getItem()).map(stat -> {
            if (onlyIngredient || !stack.isEmpty()) {
                int skillLevel = type == EnumCrafting.FORGE ? data.getSkillLevel(EnumSkills.FORGING)[0] : data.getSkillLevel(EnumSkills.CRAFTING)[0];
                return level * (Math.max(1, stat.getDiff() - skillLevel)) * 3;
            }
            return -1;
        }).orElse(-1);
    }

    public static int craftingCost(EnumCrafting type, PlayerData data, SextupleRecipe recipe) {
        if (!GeneralConfig.useRP)
            return 0;
        if (GeneralConfig.recipeSystem == 1 || GeneralConfig.recipeSystem == 3)
            return recipe.getBaseCost();
        int skillLevel = switch (type) {
            case FORGE -> data.getSkillLevel(EnumSkills.FORGING)[0];
            case ARMOR -> data.getSkillLevel(EnumSkills.CRAFTING)[0];
            case CHEM -> data.getSkillLevel(EnumSkills.CHEMISTRY)[0];
            case COOKING -> data.getSkillLevel(EnumSkills.COOKING)[0];
        };
        return Math.max(recipe.getCraftingLevel() - skillLevel, 1) * recipe.getBaseCost();
    }

    public static ItemStack getUpgradedStack(ItemStack stack, ItemStack ing) {
        if (ing.isEmpty() || !ItemNBT.shouldHaveStats(stack) || ItemNBT.itemLevel(stack) >= 10)
            return ItemStack.EMPTY;
        ItemStack output = stack.copy();
        ItemNBT.addUpgradeItem(output, ing);
        return output;
    }
}
