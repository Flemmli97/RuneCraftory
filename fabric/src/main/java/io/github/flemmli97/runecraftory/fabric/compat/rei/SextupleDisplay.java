package io.github.flemmli97.runecraftory.fabric.compat.rei;

import io.github.flemmli97.runecraftory.api.enums.EnumCrafting;
import io.github.flemmli97.runecraftory.common.crafting.SextupleRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public record SextupleDisplay(
        SextupleRecipe recipe, EnumCrafting type) implements Display {

    @Override
    public List<EntryIngredient> getInputEntries() {
        NonNullList<Ingredient> input = this.recipe.getIngredients();
        List<EntryIngredient> inputs = new ArrayList<>();
        for (Ingredient i : input)
            inputs.add(EntryIngredients.ofIngredient(i));
        return inputs;
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return List.of(EntryIngredient.of(EntryStacks.of(this.recipe.getResultItem())));
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return switch (this.type) {
            case FORGE -> ReiPlugin.FORGING;
            case ARMOR -> ReiPlugin.ARMOR;
            case CHEM -> ReiPlugin.CHEM;
            case COOKING -> ReiPlugin.COOKING;
        };
    }
}
