package io.github.flemmli97.runecraftory.common.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.api.enums.EnumCrafting;
import io.github.flemmli97.runecraftory.common.registry.ModCrafting;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class RecipeBuilder {

    private final ItemStack result;
    private final int level, cost;
    private final List<Ingredient> ingredients = new ArrayList<>();
    private String group;
    private final RecipeSerializer<?> serializer;
    private final EnumCrafting type;

    private RecipeBuilder(EnumCrafting type, ItemStack item, int level, int cost, RecipeSerializer<?> serializer) {
        this.type = type;
        this.result = item;
        this.level = level;
        this.cost = cost;
        this.serializer = serializer;
    }

    public static RecipeBuilder create(EnumCrafting type, ItemLike item, int level) {
        return create(type, item, 1, level);
    }

    public static RecipeBuilder create(EnumCrafting type, ItemLike item, int count, int level) {
        return create(type, new ItemStack(item, count), level, 1);
    }

    public static RecipeBuilder create(EnumCrafting type, ItemLike item, int count, int level, int cost) {
        return create(type, new ItemStack(item, count), level, cost);
    }

    public static RecipeBuilder create(EnumCrafting type, ItemStack item, int level, int cost) {
        RecipeSerializer<?> serializer = switch (type) {
            case FORGE -> ModCrafting.FORGESERIALIZER.get();
            case ARMOR -> ModCrafting.ARMORSERIALIZER.get();
            case CHEM -> ModCrafting.CHEMISTRYSERIALIZER.get();
            default -> ModCrafting.COOKINGSERIALIZER.get();
        };
        return new RecipeBuilder(type, item, level, cost, serializer);
    }

    public RecipeBuilder addIngredient(TagKey<Item> tag) {
        return this.addIngredient(Ingredient.of(tag));
    }

    public RecipeBuilder addIngredient(ItemLike item) {
        return this.addIngredient(item, 1);
    }

    public RecipeBuilder addIngredient(ItemLike item, int amount) {
        for (int i = 0; i < amount; ++i) {
            this.addIngredient(Ingredient.of(item));
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

    public void build(Consumer<FinishedRecipe> cons) {
        this.build(cons, PlatformUtils.INSTANCE.items().getIDFrom(this.result.getItem()));
    }

    public void build(Consumer<FinishedRecipe> cons, String string) {
        this.build(cons, new ResourceLocation(string));
    }

    public void build(Consumer<FinishedRecipe> cons, ResourceLocation res) {
        this.validate(res);
        cons.accept(new Result(new ResourceLocation(res.getNamespace(), this.type.getId() + "/" + res.getPath()), this.result, this.level, this.cost, this.group == null ? "" : this.group, this.ingredients) {
            @Override
            public RecipeSerializer<?> getType() {
                return RecipeBuilder.this.serializer;
            }
        });
    }

    private void validate(ResourceLocation res) {
        if (this.ingredients.size() > 6)
            throw new IllegalStateException("Recipe " + res + " too big. Max size is 6");
    }

    public static class Result implements FinishedRecipe {

        private final ResourceLocation id;
        private final ItemStack result;
        private final int level, cost;
        private final String group;
        private final List<Ingredient> ingredients;

        public Result(ResourceLocation res, ItemStack output, int level, int cost, String group, List<Ingredient> ingredients) {
            this.id = res;
            this.result = output;
            this.level = level;
            this.cost = cost;
            this.group = group;
            this.ingredients = ingredients;
        }

        @Override
        public void serializeRecipeData(JsonObject obj) {
            if (!this.group.isEmpty()) {
                obj.addProperty("group", this.group);
            }
            obj.addProperty("level", this.level);
            obj.addProperty("cost", this.cost);
            JsonArray jsonarray = new JsonArray();

            for (Ingredient ingredient : this.ingredients) {
                jsonarray.add(ingredient.toJson());
            }

            obj.add("ingredients", jsonarray);
            obj.add("result", this.itemStackToJson(this.result));
        }

        private JsonElement itemStackToJson(ItemStack stack) {
            JsonObject obj = new JsonObject();
            obj.addProperty("item", PlatformUtils.INSTANCE.items().getIDFrom(stack.getItem()).toString());
            if (stack.getCount() > 1)
                obj.addProperty("count", stack.getCount());
            if (stack.hasTag())
                CompoundTag.CODEC.encode(stack.getTag(), JsonOps.INSTANCE, new JsonObject()).result().ifPresent(e -> obj.add("nbt", e));
            return obj;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return RecipeSerializer.SHAPELESS_RECIPE;
        }

        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Override
        @Nullable
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Override
        @Nullable
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }
}
