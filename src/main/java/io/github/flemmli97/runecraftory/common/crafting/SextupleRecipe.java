package io.github.flemmli97.runecraftory.common.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import io.github.flemmli97.runecraftory.common.capability.CapabilityInsts;
import io.github.flemmli97.runecraftory.common.inventory.PlayerContainerInv;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.ArrayList;
import java.util.List;

public abstract class SextupleRecipe implements IRecipe<PlayerContainerInv> {

    private final ResourceLocation id;
    private final String group;
    private final ItemStack recipeOutput;
    private final NonNullList<Ingredient> recipeItems;
    private final boolean isSimple;
    private final int craftingLevel;
    private final int baseCost;

    public SextupleRecipe(ResourceLocation id, String group, int level, int baseCost, ItemStack result, NonNullList<Ingredient> ingredients) {
        this.id = id;
        this.group = group;
        this.recipeOutput = result;
        this.recipeItems = ingredients;
        this.isSimple = ingredients.stream().allMatch(Ingredient::isSimple);
        this.craftingLevel = level;
        this.baseCost = baseCost;
    }

    @Override
    public boolean matches(PlayerContainerInv inv, World world) {
        RecipeItemHelper recipeitemhelper = new RecipeItemHelper();
        List<ItemStack> inputs = new ArrayList<>();
        int i = 0;
        int max = Math.min(6, inv.getSizeInventory());
        for (int j = 0; j < max; ++j) {
            ItemStack itemstack = inv.getStackInSlot(j);
            if (!itemstack.isEmpty()) {
                ++i;
                if (this.isSimple)
                    recipeitemhelper.func_221264_a(itemstack, 1);
                else inputs.add(itemstack);
            }
        }
        boolean unlocked = inv.getPlayer().getCapability(CapabilityInsts.PlayerCap).map(cap -> cap.getRecipeKeeper().isUnlocked(this)).orElse(false);
        return unlocked && i == this.recipeItems.size() && (this.isSimple ? recipeitemhelper.canCraft(this, null) : RecipeMatcher.findMatches(inputs, this.recipeItems) != null);
    }

    @Override
    public ItemStack getCraftingResult(PlayerContainerInv inv) {
        ItemStack stack = this.recipeOutput.copy();
        ItemNBT.initNBT(stack);
        return stack;
    }

    @Override
    public boolean canFit(int x, int y) {
        return x * y > this.recipeItems.size();
    }

    @Override
    public ItemStack getRecipeOutput() {
        ItemStack stack = this.recipeOutput.copy();
        ItemNBT.initNBT(stack);
        return stack;
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
    public ResourceLocation getId() {
        return this.id;
    }

    public int getCraftingLevel() {
        return this.craftingLevel;
    }

    public int getBaseCost() {
        return this.baseCost;
    }

    @Override
    public abstract IRecipeSerializer<?> getSerializer();

    @Override
    public abstract ItemStack getIcon();

    @Override
    public abstract IRecipeType<?> getType();

    @Override
    public String toString() {
        return String.format("Result: %s; Required Level: %d; ID: %s", this.recipeOutput, this.craftingLevel, this.id);
    }

    public static abstract class Serializer<T extends SextupleRecipe> extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<T> {

        @Override
        public T read(ResourceLocation res, JsonObject obj) {
            String s = JSONUtils.getString(obj, "group", "");
            NonNullList<Ingredient> nonnulllist = readIngredients(JSONUtils.getJsonArray(obj, "ingredients"));
            int level = JSONUtils.getInt(obj, "level", 1);
            int baseCost = JSONUtils.getInt(obj, "cost", 1);
            if (nonnulllist.isEmpty()) {
                throw new JsonParseException("No ingredients for shapeless recipe");
            } else if (nonnulllist.size() > 6) {
                throw new JsonParseException("Too many ingredients for shapeless recipe the max is " + 6);
            } else {
                ItemStack itemstack = CraftingHelper.getItemStack(JSONUtils.getJsonObject(obj, "result"), true);
                return this.get(res, s, level, baseCost, itemstack, nonnulllist);
            }
        }

        private static NonNullList<Ingredient> readIngredients(JsonArray ingredientArray) {
            NonNullList<Ingredient> nonnulllist = NonNullList.create();

            for (int i = 0; i < ingredientArray.size(); ++i) {
                Ingredient ingredient = Ingredient.deserialize(ingredientArray.get(i));
                if (!ingredient.hasNoMatchingItems()) {
                    nonnulllist.add(ingredient);
                }
            }

            return nonnulllist;
        }

        @Override
        public T read(ResourceLocation res, PacketBuffer buffer) {
            int level = buffer.readInt();
            int cost = buffer.readInt();
            String s = buffer.readString(32767);
            int i = buffer.readVarInt();
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);
            for (int j = 0; j < nonnulllist.size(); ++j) {
                nonnulllist.set(j, Ingredient.read(buffer));
            }
            ItemStack itemstack = buffer.readItemStack();
            return this.get(res, s, level, cost, itemstack, nonnulllist);
        }

        public abstract T get(ResourceLocation id, String group, int level, int cost, ItemStack result, NonNullList<Ingredient> ingredients);

        @Override
        public void write(PacketBuffer buffer, T recipe) {
            buffer.writeInt(recipe.getCraftingLevel());
            buffer.writeInt(recipe.getBaseCost());
            buffer.writeString(recipe.getGroup());
            buffer.writeVarInt(recipe.getIngredients().size());

            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredient.write(buffer);
            }
            buffer.writeItemStack(recipe.getRecipeOutput());
        }
    }
    //         Optional<ICraftingRecipe> optional = world.getServer().getRecipeManager().getRecipe(IRecipeType.CRAFTING, inventory, world);
}
