package io.github.flemmli97.runecraftory.integration.rei;

import io.github.flemmli97.runecraftory.api.enums.EnumCrafting;
import io.github.flemmli97.runecraftory.common.crafting.SextupleRecipe;
import io.github.flemmli97.runecraftory.platform.Platform;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.registry.RecipeManagerContext;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class SextupleDisplay extends BasicDisplay {

    private final CraftingIdentifier type;
    private final RecipeHolder<SextupleRecipe> recipe;

    @SuppressWarnings("UnstableApiUsage")
    public SextupleDisplay(RecipeHolder<SextupleRecipe> recipe, CraftingIdentifier identifier) {
        super(recipe.value().getIngredients().stream().map(EntryIngredients::ofIngredient).toList(),
                List.of(EntryIngredient.of(EntryStacks.of(recipe.value().getResultItem(BasicDisplay.registryAccess())))),
                Optional.of(recipe.id()));
        this.recipe = recipe;
        this.type = identifier;
    }

    @SuppressWarnings("unchecked")
    private SextupleDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<ResourceLocation> location, CompoundTag tag) {
        super(inputs, outputs, location);
        this.recipe = (RecipeHolder<SextupleRecipe>) location.flatMap(resourceLocation -> RecipeManagerContext.getInstance().getRecipeManager().byKey(resourceLocation))
                .orElse(null);
        this.type = CraftingIdentifier.get(EnumCrafting.values()[tag.getInt("CraftingType")]);
    }

    public boolean shouldShowDisplay(boolean asIngredient) {
        return Minecraft.getInstance().player == null ||
                this.recipe() == null ||
                (asIngredient ? Platform.INSTANCE.getPlayerData(Minecraft.getInstance().player).getRecipeKeeper().isUnlocked(this.recipe())
                        : Platform.INSTANCE.getPlayerData(Minecraft.getInstance().player).getRecipeKeeper().isUnlockedForCrafting(this.recipe()));
    }

    @Nullable
    public RecipeHolder<SextupleRecipe> recipe() {
        return this.recipe;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return this.type.identifier();
    }

    public static BasicDisplay.Serializer<SextupleDisplay> serializer() {
        return BasicDisplay.Serializer.of(SextupleDisplay::new, ((display, tag) -> tag.putInt("CraftingType", display.type.craftingType().ordinal())));
    }
}
