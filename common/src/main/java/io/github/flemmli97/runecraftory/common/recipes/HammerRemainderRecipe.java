package io.github.flemmli97.runecraftory.common.recipes;

import com.mojang.serialization.MapCodec;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryCrafting;
import io.github.flemmli97.runecraftory.mixin.ShapelessRecipeAccessor;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;

public class HammerRemainderRecipe extends ShapelessRecipe {

    public HammerRemainderRecipe(ShapelessRecipe wrapped) {
        this(wrapped.getGroup(), wrapped.category(), ((ShapelessRecipeAccessor) wrapped).getResult(), wrapped.getIngredients());
    }

    public HammerRemainderRecipe(String group, CraftingBookCategory category, ItemStack result, NonNullList<Ingredient> ingredients) {
        super(group, category, result, ingredients);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput inv) {
        NonNullList<ItemStack> list = NonNullList.withSize(inv.size(), ItemStack.EMPTY);
        for (int i = 0; i < inv.size(); ++i) {
            ItemStack itemStack = inv.getItem(i);
            if (!itemStack.isEmpty() && itemStack.is(RunecraftoryTags.Items.HAMMER_TOOLS)) {
                list.set(i, itemStack.copy());
            }
        }
        return list;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RuneCraftoryCrafting.HAMMER_REMAINDER_SERIALIZER.get();
    }

    public static class Serializer extends ShapelessRecipe.Serializer {

        @Override
        public MapCodec<ShapelessRecipe> codec() {
            return super.codec().xmap(HammerRemainderRecipe::new, recipe -> recipe);
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ShapelessRecipe> streamCodec() {
            return super.streamCodec().map(HammerRemainderRecipe::new, recipe -> recipe);
        }
    }
}