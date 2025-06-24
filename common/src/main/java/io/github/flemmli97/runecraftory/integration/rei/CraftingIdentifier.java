package io.github.flemmli97.runecraftory.integration.rei;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.enums.EnumCrafting;
import io.github.flemmli97.runecraftory.common.crafting.SextupleRecipe;
import io.github.flemmli97.runecraftory.common.registry.ModCrafting;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public record CraftingIdentifier(CategoryIdentifier<SextupleDisplay> identifier,
                                 EnumCrafting craftingType,
                                 Supplier<RecipeType<SextupleRecipe>> recipeType) {

    private static final Map<EnumCrafting, CraftingIdentifier> IDENTIFIERS = new HashMap<>();

    public static final CraftingIdentifier FORGING = new CraftingIdentifier(EnumCrafting.FORGE);
    public static final CraftingIdentifier CHEMISTRY = new CraftingIdentifier(EnumCrafting.CHEM);
    public static final CraftingIdentifier COOKING = new CraftingIdentifier(EnumCrafting.COOKING);
    public static final CraftingIdentifier ARMOR = new CraftingIdentifier(EnumCrafting.ARMOR);

    public static CraftingIdentifier get(EnumCrafting craftingType) {
        return IDENTIFIERS.get(craftingType);
    }

    private CraftingIdentifier(EnumCrafting craftingType) {
        this(CategoryIdentifier.of(RuneCraftory.modRes(craftingType.getId() + "_category")),
                craftingType,
                switch (craftingType) {
                    case FORGE -> ModCrafting.FORGE;
                    case ARMOR -> ModCrafting.ARMOR;
                    case CHEM -> ModCrafting.CHEMISTRY;
                    case COOKING -> ModCrafting.COOKING;
                });
        IDENTIFIERS.put(craftingType, this);
    }
}
