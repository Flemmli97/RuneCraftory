package io.github.flemmli97.runecraftory.forge.mixin;

import io.github.flemmli97.runecraftory.forge.compat.jei.JEI;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.common.recipes.PluginManager;
import mezz.jei.common.recipes.collect.RecipeTypeData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.stream.Stream;

@Pseudo
@Mixin(targets = "mezz/jei/common/recipes/PluginManager")
public abstract class CompatJei {

    /**
     * Hides recipe if player has not unlocked them.
     * Otherway would be to use IJeiRuntime and set the recipes as hidden/unhidden if player locks/unlocks them but that's not ideal either.
     */
    @ModifyVariable(method = "getRecipes(Lmezz/jei/common/recipes/collect/RecipeTypeData;Lmezz/jei/api/recipe/IFocusGroup;Z)Ljava/util/stream/Stream;", remap = false, at = @At(value = "INVOKE", target = "Lmezz/jei/common/recipes/collect/RecipeTypeData;getHiddenRecipes()Ljava/util/Set;", shift = At.Shift.BEFORE, by = -2))
    private <T> Stream<T> runecraftory_filter_recipes(Stream<T> recipes, RecipeTypeData<T> recipeTypeData, IFocusGroup focusGroup) {
        return JEI.filterLocked(recipes, recipeTypeData, focusGroup);
    }
}
