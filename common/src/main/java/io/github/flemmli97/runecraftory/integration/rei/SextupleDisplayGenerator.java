package io.github.flemmli97.runecraftory.integration.rei;

import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;
import io.github.flemmli97.runecraftory.common.crafting.SextupleRecipe;
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DynamicDisplayGenerator;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.registry.RecipeManagerContext;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class SextupleDisplayGenerator implements DynamicDisplayGenerator<SextupleDisplay> {

    private final CraftingIdentifier identifier;
    private final List<SextupleDisplay> displays;
    private final SetMultimap<EntryStack<?>, SextupleDisplay> displaysByInput = createSetMultimap();
    private final SetMultimap<EntryStack<?>, SextupleDisplay> displaysByOutput = createSetMultimap();

    public SextupleDisplayGenerator(CraftingIdentifier identifier) {
        this.identifier = identifier;
        this.displays = sorted(identifier);
        this.displays.forEach(this::process);
    }

    private static SetMultimap<EntryStack<?>, SextupleDisplay> createSetMultimap() {
        return Multimaps.newSetMultimap(
                new Object2ObjectOpenCustomHashMap<>(5000, new Hash.Strategy<>() {
                    @Override
                    public int hashCode(EntryStack<?> stack) {
                        return Long.hashCode(EntryStacks.hashFuzzy(stack));
                    }

                    @Override
                    public boolean equals(EntryStack<?> o1, EntryStack<?> o2) {
                        return EntryStacks.equalsFuzzy(o1, o2);
                    }
                }),
                ReferenceOpenHashSet::new
        );
    }

    private static List<SextupleDisplay> sorted(CraftingIdentifier identifier) {
        List<RecipeHolder<SextupleRecipe>> l = new ArrayList<>(RecipeManagerContext.getInstance().getRecipeManager().getAllRecipesFor(identifier.recipeType().get()));
        l.sort(Comparator.comparingInt(h -> h.value().getCraftingLevel()));
        return l.stream().map(r -> new SextupleDisplay(r, identifier)).toList();
    }

    private void process(SextupleDisplay display) {
        for (EntryIngredient input : display.getInputEntries()) {
            for (EntryStack<?> stack : input) {
                this.displaysByInput.put(stack, display);
            }
        }
        for (EntryIngredient output : display.getOutputEntries()) {
            for (EntryStack<?> stack : output) {
                this.displaysByOutput.put(stack, display);
            }
        }
    }

    @Override
    public Optional<List<SextupleDisplay>> getRecipeFor(EntryStack<?> entry) {
        Set<SextupleDisplay> displays = this.displaysByOutput.get(entry);
        return Optional.of(displays.stream().filter(display -> display.shouldShowDisplay(false)).toList());
    }

    @Override
    public Optional<List<SextupleDisplay>> getUsageFor(EntryStack<?> entry) {
        if (this.isStackWorkStationOfCategory(entry)) {
            return Optional.of(this.displays.stream().filter(display -> display.shouldShowDisplay(false)).toList());
        }
        Set<SextupleDisplay> displays = this.displaysByInput.get(entry);
        return Optional.of(displays.stream().filter(display -> display.shouldShowDisplay(true)).toList());
    }

    private boolean isStackWorkStationOfCategory(EntryStack<?> stack) {
        for (EntryIngredient ingredient : CategoryRegistry.getInstance().get(this.identifier.identifier()).getWorkstations()) {
            if (EntryIngredients.testFuzzy(ingredient, stack)) {
                return true;
            }
        }
        return false;
    }
}
