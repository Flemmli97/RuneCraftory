package io.github.flemmli97.runecraftory.common.crafting;

import com.google.gson.JsonObject;
import io.github.flemmli97.runecraftory.common.registry.ModCrafting;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.tenshilib.platform.registry.CustomRegistryEntry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.UpgradeRecipe;
import net.minecraft.world.level.Level;

public class LevelUpUpgradeRecipe extends UpgradeRecipe {

    private final ResourceLocation id;
    protected final int level;
    protected final Ingredient base;
    protected final Ingredient upgradeMaterial;

    public LevelUpUpgradeRecipe(ResourceLocation id, int level, Ingredient base, Ingredient upgradeMaterial) {
        super(id, base, upgradeMaterial, ItemStack.EMPTY);
        this.id = id;
        this.level = level;
        this.base = base;
        this.upgradeMaterial = upgradeMaterial;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public boolean matches(Container container, Level level) {
        ItemStack stack = container.getItem(0);
        if (ItemNBT.itemLevel(stack) != this.level)
            return false;
        return super.matches(container, level);
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack assemble(Container container) {
        ItemStack stack = container.getItem(0).copy();
        ItemNBT.getLeveledItem(stack, ItemNBT.itemLevel(stack) + 1);
        return stack;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModCrafting.LEVEL_UPGRADE_SERIALIZER.get();
    }

    public static class Serializer extends CustomRegistryEntry<Serializer> implements RecipeSerializer<LevelUpUpgradeRecipe> {

        @Override
        public LevelUpUpgradeRecipe fromJson(ResourceLocation id, JsonObject json) {
            int level = GsonHelper.getAsInt(json, "current_level");
            Ingredient ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "item"));
            Ingredient ingredient1 = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "material"));
            return new LevelUpUpgradeRecipe(id, level, ingredient, ingredient1);
        }

        @Override
        public LevelUpUpgradeRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            int level = buffer.readInt();
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            Ingredient ingredient1 = Ingredient.fromNetwork(buffer);
            return new LevelUpUpgradeRecipe(recipeId, level, ingredient, ingredient1);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, LevelUpUpgradeRecipe recipe) {
            buffer.writeInt(recipe.level);
            recipe.base.toNetwork(buffer);
            recipe.upgradeMaterial.toNetwork(buffer);
        }
    }
}
