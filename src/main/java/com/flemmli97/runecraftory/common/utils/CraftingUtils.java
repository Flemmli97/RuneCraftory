package com.flemmli97.runecraftory.common.utils;

import com.flemmli97.runecraftory.api.enums.EnumCrafting;
import com.flemmli97.runecraftory.common.crafting.SextupleRecipe;
import com.flemmli97.runecraftory.common.registry.ModCrafting;
import net.minecraft.item.crafting.IRecipeType;

public class CraftingUtils {

    public static IRecipeType<SextupleRecipe> getType(EnumCrafting type) {
        switch (type) {
            case FORGE:
                return ModCrafting.FORGE;
            case ARMOR:
                return ModCrafting.ARMOR;
            case CHEM:
                return ModCrafting.CHEMISTRY;
            default:
                COOKING:
                return ModCrafting.COOKING;
        }
    }
}
