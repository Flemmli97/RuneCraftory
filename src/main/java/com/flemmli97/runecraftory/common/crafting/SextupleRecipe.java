package com.flemmli97.runecraftory.common.crafting;

import com.flemmli97.runecraftory.common.inventory.PlayerContainerInv;
import com.flemmli97.runecraftory.common.utils.ItemNBT;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.entity.player.ServerPlayerEntity;
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

import java.util.List;

public abstract class SextupleRecipe implements IRecipe<PlayerContainerInv> {

    private final ResourceLocation id;
    private final String group;
    private final ItemStack recipeOutput;
    private final NonNullList<Ingredient> recipeItems;
    private final boolean isSimple;
    private final int craftingLevel;

    public SextupleRecipe(ResourceLocation id, String group, int level, ItemStack result, NonNullList<Ingredient> ingredients) {
        this.id = id;
        this.group = group;
        this.recipeOutput = result;
        this.recipeItems = ingredients;
        this.isSimple = ingredients.stream().allMatch(Ingredient::isSimple);
        this.craftingLevel = level;
    }

    @Override
    public boolean matches(PlayerContainerInv inv, World world) {
        RecipeItemHelper recipeitemhelper = new RecipeItemHelper();
        List<ItemStack> inputs = Lists.newArrayList();
        int i = 0;

        for (int j = 0; j < inv.getSizeInventory(); ++j) {
            ItemStack itemstack = inv.getStackInSlot(j);
            System.out.println(itemstack);
            System.out.println(itemstack.getTag());
            if (!itemstack.isEmpty()) {
                ++i;
                if (isSimple)
                    recipeitemhelper.func_221264_a(itemstack, 1);
                else inputs.add(itemstack);
            }
        }
        this.getIngredients().forEach(ing -> System.out.println("ing " + ing.serialize()));
        System.out.println("ing " + this.getIngredients());
        boolean unlocked = inv.getPlayer() instanceof ServerPlayerEntity ? ((ServerPlayerEntity) inv.getPlayer()).getRecipeBook().isUnlocked(this) : false;
        return unlocked && i == this.recipeItems.size() && (isSimple ? recipeitemhelper.canCraft(this, null) : RecipeMatcher.findMatches(inputs, this.recipeItems) != null);
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
            if (nonnulllist.isEmpty()) {
                throw new JsonParseException("No ingredients for shapeless recipe");
            } else if (nonnulllist.size() > 6) {
                throw new JsonParseException("Too many ingredients for shapeless recipe the max is " + 6);
            } else {
                ItemStack itemstack = CraftingHelper.getItemStack(JSONUtils.getJsonObject(obj, "result"), true);
                return get(res, s, level, itemstack, nonnulllist);
            }
        }

        private static NonNullList<Ingredient> readIngredients(JsonArray p_199568_0_) {
            NonNullList<Ingredient> nonnulllist = NonNullList.create();

            for (int i = 0; i < p_199568_0_.size(); ++i) {
                Ingredient ingredient = Ingredient.deserialize(p_199568_0_.get(i));
                if (!ingredient.hasNoMatchingItems()) {
                    nonnulllist.add(ingredient);
                }
            }

            return nonnulllist;
        }

        @Override
        public T read(ResourceLocation res, PacketBuffer buffer) {
            int level = buffer.readInt();
            String s = buffer.readString(32767);
            int i = buffer.readVarInt();
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);
            for (int j = 0; j < nonnulllist.size(); ++j) {
                nonnulllist.set(j, Ingredient.read(buffer));
            }
            ItemStack itemstack = buffer.readItemStack();
            return get(res, s, level, itemstack, nonnulllist);
        }

        public abstract T get(ResourceLocation id, String group, int level, ItemStack result, NonNullList<Ingredient> ingredients);

        @Override
        public void write(PacketBuffer buffer, T recipe) {
            buffer.writeInt(recipe.getCraftingLevel());
            buffer.writeString(recipe.getGroup());
            buffer.writeVarInt(recipe.getIngredients().size());

            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredient.write(buffer);
            }
            buffer.writeItemStack(recipe.getRecipeOutput());
        }
    }
    //         Optional<ICraftingRecipe> optional = p_217066_1_.getServer().getRecipeManager().getRecipe(IRecipeType.CRAFTING, p_217066_3_, p_217066_1_);
}
