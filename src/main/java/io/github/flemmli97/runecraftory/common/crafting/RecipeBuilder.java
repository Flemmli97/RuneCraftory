package io.github.flemmli97.runecraftory.common.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.api.enums.EnumCrafting;
import io.github.flemmli97.runecraftory.common.registry.ModCrafting;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class RecipeBuilder {

    private final ItemStack result;
    private final int level, cost;
    private final List<Ingredient> ingredients = new ArrayList<>();
    private String group;
    private final IRecipeSerializer<?> serializer;

    private RecipeBuilder(ItemStack item, int level, int cost, IRecipeSerializer<?> serializer) {
        this.result = item;
        this.level = level;
        this.cost = cost;
        this.serializer = serializer;
    }

    public static RecipeBuilder create(EnumCrafting type, IItemProvider item, int level) {
        return create(type, item, 1, level);
    }

    public static RecipeBuilder create(EnumCrafting type, IItemProvider item, int count, int level) {
        return create(type, new ItemStack(item, count), level, 1);
    }

    public static RecipeBuilder create(EnumCrafting type, IItemProvider item, int count, int level, int cost) {
        return create(type, new ItemStack(item, count), level, cost);
    }

    public static RecipeBuilder create(EnumCrafting type, ItemStack item, int level, int cost) {
        IRecipeSerializer<?> serializer;
        switch (type) {
            case FORGE:
                serializer = ModCrafting.FORGESERIALIZER.get();
                break;
            case ARMOR:
                serializer = ModCrafting.ARMORSERIALIZER.get();
                break;
            case CHEM:
                serializer = ModCrafting.CHEMISTRYSERIALIZER.get();
                break;
            default:
                COOKING:
                serializer = ModCrafting.COOKINGSERIALIZER.get();
                break;
        }
        return new RecipeBuilder(item, level, cost, serializer);
    }

    public RecipeBuilder addIngredient(ITag<Item> tag) {
        return this.addIngredient(Ingredient.fromTag(tag));
    }

    public RecipeBuilder addIngredient(IItemProvider item) {
        return this.addIngredient(item, 1);
    }

    public RecipeBuilder addIngredient(IItemProvider item, int amount) {
        for (int i = 0; i < amount; ++i) {
            this.addIngredient(Ingredient.fromItems(item));
        }
        return this;
    }

    public RecipeBuilder addIngredient(Ingredient ingredient) {
        return this.addIngredient(ingredient, 1);
    }

    public RecipeBuilder addIngredient(Ingredient ingredient, int amount) {
        for (int i = 0; i < amount; ++i) {
            this.ingredients.add(ingredient);
        }

        return this;
    }

    public RecipeBuilder setGroup(String group) {
        this.group = group;
        return this;
    }

    public void build(Consumer<IFinishedRecipe> cons) {
        this.build(cons, ForgeRegistries.ITEMS.getKey(this.result.getItem()));
    }

    public void build(Consumer<IFinishedRecipe> cons, String string) {
        this.build(cons, new ResourceLocation(string));
    }

    public void build(Consumer<IFinishedRecipe> cons, ResourceLocation res) {
        this.validate(res);
        cons.accept(new Result(res, this.result, this.level, this.cost, this.group == null ? "" : this.group, this.ingredients, new ResourceLocation(res.getNamespace(), "recipes/" + this.result.getItem().getGroup().getPath() + "/" + res.getPath())) {
            @Override
            public IRecipeSerializer<?> getSerializer() {
                return RecipeBuilder.this.serializer;
            }
        });
    }

    private void validate(ResourceLocation res) {
        if (this.ingredients.size() > 6)
            throw new IllegalStateException("Recipe " + res + " too big. Max size is 6");
    }

    public static class Result implements IFinishedRecipe {

        private final ResourceLocation id;
        private final ItemStack result;
        private final int level, cost;
        private final String group;
        private final List<Ingredient> ingredients;
        private final ResourceLocation advancementId;

        public Result(ResourceLocation res, ItemStack output, int level, int cost, String group, List<Ingredient> ingredients, ResourceLocation advancementID) {
            this.id = res;
            this.result = output;
            this.level = level;
            this.cost = cost;
            this.group = group;
            this.ingredients = ingredients;
            this.advancementId = advancementID;
        }

        @Override
        public void serialize(JsonObject obj) {
            if (!this.group.isEmpty()) {
                obj.addProperty("group", this.group);
            }
            obj.addProperty("level", this.level);
            obj.addProperty("cost", this.cost);
            JsonArray jsonarray = new JsonArray();

            for (Ingredient ingredient : this.ingredients) {
                jsonarray.add(ingredient.serialize());
            }

            obj.add("ingredients", jsonarray);
            obj.add("result", this.itemStackToJson(this.result));
        }

        private JsonElement itemStackToJson(ItemStack stack) {
            JsonObject obj = new JsonObject();
            obj.addProperty("item", stack.getItem().getRegistryName().toString());
            if (stack.getCount() > 1)
                obj.addProperty("count", stack.getCount());
            if (stack.hasTag())
                CompoundNBT.CODEC.encode(stack.getTag(), JsonOps.INSTANCE, new JsonObject()).result().ifPresent(e -> obj.add("nbt", e));
            return obj;
        }

        @Override
        public IRecipeSerializer<?> getSerializer() {
            return IRecipeSerializer.CRAFTING_SHAPELESS;
        }

        @Override
        public ResourceLocation getID() {
            return this.id;
        }

        @Override
        @Nullable
        public JsonObject getAdvancementJson() {
            return null;
        }

        @Override
        @Nullable
        public ResourceLocation getAdvancementID() {
            return this.advancementId;
        }
    }
}
