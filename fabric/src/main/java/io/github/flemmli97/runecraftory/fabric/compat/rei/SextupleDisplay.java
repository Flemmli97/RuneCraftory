package io.github.flemmli97.runecraftory.fabric.compat.rei;

import io.github.flemmli97.runecraftory.api.enums.EnumCrafting;
import io.github.flemmli97.runecraftory.common.crafting.SextupleRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class SextupleDisplay extends BasicDisplay {

    private final EnumCrafting type;
    private SextupleRecipe recipe;

    public SextupleDisplay(SextupleRecipe recipe, EnumCrafting type) {
        super(recipe.getIngredients().stream().map(EntryIngredients::ofIngredient).toList(), List.of(EntryIngredient.of(EntryStacks.of(recipe.getResultItem()))), Optional.of(recipe.getId()));
        this.type = type;
        this.recipe = recipe;
    }

    private SextupleDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<ResourceLocation> location, CompoundTag tag) {
        super(inputs, outputs, location);
        this.type = EnumCrafting.values()[tag.getInt("CraftingType")];
    }

    @Nullable
    public SextupleRecipe recipe() {
        return this.recipe;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return switch (this.type) {
            case FORGE -> ReiClientPlugin.FORGING;
            case ARMOR -> ReiClientPlugin.ARMOR;
            case CHEM -> ReiClientPlugin.CHEM;
            case COOKING -> ReiClientPlugin.COOKING;
        };
    }

    public static BasicDisplay.Serializer<SextupleDisplay> serializer() {
        return BasicDisplay.Serializer.of(SextupleDisplay::new, ((display, tag) -> tag.putInt("CraftingType", display.type.ordinal())));
    }
}
