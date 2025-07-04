package io.github.flemmli97.runecraftory.integration.rei;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.enums.CraftingType;
import io.github.flemmli97.runecraftory.common.recipes.SextupleRecipe;
import io.github.flemmli97.runecraftory.common.registry.ModCrafting;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public record CraftingIdentifier(CategoryIdentifier<SextupleDisplay> identifier,
                                 CraftingType craftingType,
                                 Supplier<RecipeType<SextupleRecipe>> recipeType) {

    private static final Map<CraftingType, CraftingIdentifier> IDENTIFIERS = new HashMap<>();

    public static final CraftingIdentifier FORGING = new CraftingIdentifier(CraftingType.FORGE);
    public static final CraftingIdentifier CHEMISTRY = new CraftingIdentifier(CraftingType.CHEMISTRY_SET);
    public static final CraftingIdentifier COOKING = new CraftingIdentifier(CraftingType.COOKING_TABLE);
    public static final CraftingIdentifier ARMOR = new CraftingIdentifier(CraftingType.ACCESSORY_WORKBENCH);

    public static CraftingIdentifier get(CraftingType craftingType) {
        return IDENTIFIERS.get(craftingType);
    }

    private CraftingIdentifier(CraftingType craftingType) {
        this(CategoryIdentifier.of(RuneCraftory.modRes(craftingType.getId() + "_category")),
                craftingType,
                switch (craftingType) {
                    case FORGE -> ModCrafting.FORGE;
                    case ACCESSORY_WORKBENCH -> ModCrafting.ARMOR;
                    case CHEMISTRY_SET -> ModCrafting.CHEMISTRY;
                    case COOKING_TABLE -> ModCrafting.COOKING;
                });
        IDENTIFIERS.put(craftingType, this);
    }
}
