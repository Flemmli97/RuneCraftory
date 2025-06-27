package io.github.flemmli97.runecraftory.integration.jei;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.enums.EnumCrafting;
import io.github.flemmli97.runecraftory.common.recipes.SextupleRecipe;
import io.github.flemmli97.runecraftory.common.registry.ModCrafting;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public record CraftingIdentifier(mezz.jei.api.recipe.RecipeType<RecipeHolder<SextupleRecipe>> identifier,
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

    public static boolean has(mezz.jei.api.recipe.RecipeType<?> recipeType) {
        return IDENTIFIERS.values().stream().anyMatch(id -> id.identifier().equals(recipeType));
    }

    private CraftingIdentifier(EnumCrafting craftingType) {
        this(createHolder(RuneCraftory.MODID, craftingType.getId() + "_category"),
                craftingType,
                switch (craftingType) {
                    case FORGE -> ModCrafting.FORGE;
                    case ARMOR -> ModCrafting.ARMOR;
                    case CHEM -> ModCrafting.CHEMISTRY;
                    case COOKING -> ModCrafting.COOKING;
                });
        IDENTIFIERS.put(craftingType, this);
    }

    private static <R extends Recipe<?>> mezz.jei.api.recipe.RecipeType<RecipeHolder<R>> createHolder(String nameSpace, String path) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(nameSpace, path);
        @SuppressWarnings({"unchecked", "RedundantCast"})
        Class<? extends RecipeHolder<R>> holderClass = (Class<? extends RecipeHolder<R>>) (Object) RecipeHolder.class;
        return new mezz.jei.api.recipe.RecipeType<>(id, holderClass);
    }
}
