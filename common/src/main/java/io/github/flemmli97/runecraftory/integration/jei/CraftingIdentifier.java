package io.github.flemmli97.runecraftory.integration.jei;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.recipes.CraftingType;
import io.github.flemmli97.runecraftory.common.recipes.SextupleRecipe;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryCrafting;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public record CraftingIdentifier(mezz.jei.api.recipe.RecipeType<RecipeHolder<SextupleRecipe>> identifier,
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

    public static boolean has(mezz.jei.api.recipe.RecipeType<?> recipeType) {
        return IDENTIFIERS.values().stream().anyMatch(id -> id.identifier().equals(recipeType));
    }

    private CraftingIdentifier(CraftingType craftingType) {
        this(createHolder(RuneCraftory.MODID, craftingType.getId() + "_category"),
                craftingType,
                switch (craftingType) {
                    case FORGE -> RuneCraftoryCrafting.FORGE;
                    case ACCESSORY_WORKBENCH -> RuneCraftoryCrafting.ARMOR;
                    case CHEMISTRY_SET -> RuneCraftoryCrafting.CHEMISTRY;
                    case COOKING_TABLE -> RuneCraftoryCrafting.COOKING;
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
