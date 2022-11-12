package io.github.flemmli97.runecraftory.common.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.inventory.PlayerContainerInv;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.utils.CraftingUtils;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.platform.registry.CustomRegistryEntry;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

public abstract class SextupleRecipe implements Recipe<PlayerContainerInv> {

    private final ResourceLocation id;
    private final String group;
    private final ItemStack recipeOutput;
    private final NonNullList<Ingredient> recipeItems;
    private final int craftingLevel;
    private final int baseCost;

    public SextupleRecipe(ResourceLocation id, String group, int level, int baseCost, ItemStack result, NonNullList<Ingredient> ingredients) {
        this.id = id;
        this.group = group;
        this.recipeOutput = result;
        this.recipeItems = ingredients;
        this.craftingLevel = level;
        this.baseCost = baseCost;
    }

    @Override
    public boolean matches(PlayerContainerInv inv, Level world) {
        return this.checkMatch(inv, world, false);
    }

    public boolean checkMatch(PlayerContainerInv inv, Level world, boolean exact) {
        if (inv.getContainerSize() < 6)
            return false;
        boolean unlocked = GeneralConfig.recipeSystem.allowLocked || Platform.INSTANCE.getPlayerData(inv.getPlayer()).map(cap -> cap.getRecipeKeeper().isUnlocked(this)).orElse(false);
        if (!unlocked)
            return false;
        NonNullList<ItemStack> stacks = NonNullList.create();
        for (int j = 0; j < 6; ++j) {
            ItemStack itemStack = inv.getItem(j);
            if (!itemStack.isEmpty())
                stacks.add(itemStack);
        }
        int matches = matchingStacks(this, stacks).getFirst().size();
        if (!exact)
            return matches >= this.getIngredients().size();
        return matches == this.getIngredients().size();
    }

    @Override
    public ItemStack assemble(PlayerContainerInv inv) {
        return this.getCraftingOutput(inv).serverResult();
    }

    public RecipeOutput getCraftingOutput(PlayerContainerInv inv) {
        if (inv.getContainerSize() < 6)
            return new RecipeOutput(ItemStack.EMPTY, ItemStack.EMPTY, NonNullList.create());
        boolean unlocked = Platform.INSTANCE.getPlayerData(inv.getPlayer()).map(cap -> cap.getRecipeKeeper().isUnlockedForCrafting(this)).orElse(false);
        NonNullList<ItemStack> stacks = NonNullList.create();
        for (int j = 0; j < 6; ++j) {
            ItemStack itemStack = inv.getItem(j);
            if (!itemStack.isEmpty())
                stacks.add(itemStack);
        }
        Pair<NonNullList<ItemStack>, NonNullList<ItemStack>> ing = matchingStacks(this, stacks);
        ItemStack trueOutput = CraftingUtils.getCraftingOutput(this.recipeOutput.copy(), inv, ing);
        return new RecipeOutput(trueOutput, unlocked ? trueOutput : new ItemStack(ModItems.unknown.get()), ing.getSecond());
    }

    public static Pair<NonNullList<ItemStack>, NonNullList<ItemStack>> matchingStacks(SextupleRecipe recipe, NonNullList<ItemStack> inv) {
        if (inv.size() > 6)
            return Pair.of(NonNullList.create(), NonNullList.create());
        NonNullList<ItemStack> list = NonNullList.create();
        NonNullList<ItemStack> bonus = NonNullList.create();
        for (Ingredient ing : recipe.getIngredients()) {
            for (ItemStack stack : inv) {
                if (ing.test(stack) && !list.contains(stack)) {
                    list.add(stack);
                    break;
                }
            }
        }
        for (ItemStack stack : inv) {
            if (!list.contains(stack))
                bonus.add(stack);
        }
        return Pair.of(list, bonus);
    }

    @Override
    public boolean canCraftInDimensions(int x, int y) {
        return x * y > this.recipeItems.size();
    }

    @Override
    public ItemStack getResultItem() {
        return this.recipeOutput.copy();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.recipeItems;
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    @Override
    public abstract ItemStack getToastSymbol();

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public abstract RecipeSerializer<?> getSerializer();

    @Override
    public abstract RecipeType<?> getType();

    public int getCraftingLevel() {
        return this.craftingLevel;
    }

    public int getBaseCost() {
        return this.baseCost;
    }

    @Override
    public String toString() {
        return String.format("Result: %s; Required Level: %d; ID: %s", this.recipeOutput, this.craftingLevel, this.id);
    }

    public record RecipeOutput(ItemStack serverResult, ItemStack clientResult, NonNullList<ItemStack> bonusItems) {
    }

    public static abstract class Serializer<T extends SextupleRecipe> extends CustomRegistryEntry<Serializer<T>> implements RecipeSerializer<T> {

        private static NonNullList<Ingredient> readIngredients(JsonArray ingredientArray) {
            NonNullList<Ingredient> nonnulllist = NonNullList.create();

            for (int i = 0; i < ingredientArray.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(ingredientArray.get(i));
                if (!ingredient.isEmpty()) {
                    nonnulllist.add(ingredient);
                }
            }

            return nonnulllist;
        }

        @Override
        public T fromJson(ResourceLocation res, JsonObject obj) {
            String s = GsonHelper.getAsString(obj, "group", "");
            NonNullList<Ingredient> nonnulllist = readIngredients(GsonHelper.getAsJsonArray(obj, "ingredients"));
            int level = GsonHelper.getAsInt(obj, "level", 1);
            int baseCost = GsonHelper.getAsInt(obj, "cost", 1);
            if (nonnulllist.isEmpty()) {
                throw new JsonParseException("No ingredients for shapeless recipe");
            } else if (nonnulllist.size() > 6) {
                throw new JsonParseException("Too many ingredients for shapeless recipe the max is " + 6);
            } else {
                ItemStack itemstack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(obj, "result"));
                return this.get(res, s, level, baseCost, itemstack, nonnulllist);
            }
        }

        @Override
        public T fromNetwork(ResourceLocation res, FriendlyByteBuf buffer) {
            int level = buffer.readInt();
            int cost = buffer.readInt();
            String s = buffer.readUtf(32767);
            int i = buffer.readVarInt();
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);
            for (int j = 0; j < nonnulllist.size(); ++j) {
                nonnulllist.set(j, Ingredient.fromNetwork(buffer));
            }
            ItemStack itemstack = buffer.readItem();
            return this.get(res, s, level, cost, itemstack, nonnulllist);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, T recipe) {
            buffer.writeInt(recipe.getCraftingLevel());
            buffer.writeInt(recipe.getBaseCost());
            buffer.writeUtf(recipe.getGroup());
            buffer.writeVarInt(recipe.getIngredients().size());

            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredient.toNetwork(buffer);
            }
            buffer.writeItem(recipe.getResultItem());
        }

        public abstract T get(ResourceLocation id, String group, int level, int cost, ItemStack result, NonNullList<Ingredient> ingredients);
    }
}
