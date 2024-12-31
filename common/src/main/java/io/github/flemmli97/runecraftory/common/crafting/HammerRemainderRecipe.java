package io.github.flemmli97.runecraftory.common.crafting;

import com.google.gson.JsonObject;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.registry.ModCrafting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;

/**
 * Yes... i hard coded it
 */
public class HammerRemainderRecipe extends ShapelessRecipe {

    public HammerRemainderRecipe(ResourceLocation id, String group, ItemStack result, NonNullList<Ingredient> ingredients) {
        super(id, group, result, ingredients);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
        NonNullList<ItemStack> list = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);
        for (int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack itemStack = inv.getItem(i);
            if (!itemStack.isEmpty() && itemStack.is(RunecraftoryTags.HAMMER_TOOLS)) {
                list.set(i, itemStack.copy());
            }
        }
        return list;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModCrafting.HAMMER_REMAINDER_SERIALIZER.get();
    }

    public static class Serializer extends ShapelessRecipe.Serializer {

        @Override
        public ShapelessRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            ShapelessRecipe recipe = super.fromJson(recipeId, json);
            return new HammerRemainderRecipe(recipe.getId(), recipe.getGroup(), recipe.getResultItem(), recipe.getIngredients());
        }

        @Override
        public ShapelessRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            ShapelessRecipe recipe = super.fromNetwork(recipeId, buffer);
            return new HammerRemainderRecipe(recipe.getId(), recipe.getGroup(), recipe.getResultItem(), recipe.getIngredients());
        }
    }
}