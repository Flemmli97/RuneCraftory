package com.flemmli97.runecraftory.common.crafting;

import com.flemmli97.runecraftory.api.enums.EnumCrafting;
import com.flemmli97.runecraftory.common.registry.ModCrafting;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.ImpossibleTrigger;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class RecipeBuilder{

    private static final Logger LOGGER = LogManager.getLogger();
    private final ItemStack result;
    private final int level;
    private final List<Ingredient> ingredients = Lists.newArrayList();
    private final Advancement.Builder advancementBuilder = Advancement.Builder.builder();
    private String group;
    private final IRecipeSerializer<?> serializer;

    private RecipeBuilder(ItemStack item, int level, IRecipeSerializer<?> serializer) {
        this.result = item;
        this.level = level;
        this.serializer = serializer;
    }

    public static RecipeBuilder create(EnumCrafting type, IItemProvider item, int level) {
        return create(type, item, 1, level);
    }

    public static RecipeBuilder create(EnumCrafting type, IItemProvider item, int count, int level) {
        return create(type, new ItemStack(item, count), level);
    }

    public static RecipeBuilder create(EnumCrafting type, ItemStack item, int level) {
        IRecipeSerializer<?> serializer;
        switch (type) {
            case FORGE: serializer = ModCrafting.FORGESERIALIZER.get();
                break;
            case ARMOR:serializer = ModCrafting.ARMORSERIALIZER.get();
                break;
            case CHEM:serializer = ModCrafting.CHEMISTRYSERIALIZER.get();
                break;
            default: COOKING:serializer = ModCrafting.COOKINGSERIALIZER.get();
                break;
        }
        return new RecipeBuilder(item, level, serializer);
    }

    public RecipeBuilder addIngredient(ITag<Item> tag) {
        return this.addIngredient(Ingredient.fromTag(tag));
    }

    public RecipeBuilder addIngredient(IItemProvider item) {
        return this.addIngredient(item, 1);
    }

    public RecipeBuilder addIngredient(IItemProvider item, int amount) {
        for(int i = 0; i < amount; ++i) {
            this.addIngredient(Ingredient.fromItems(item));
        }
        return this;
    }

    public RecipeBuilder addIngredient(Ingredient ingredient) {
        return this.addIngredient(ingredient, 1);
    }

    public RecipeBuilder addIngredient(Ingredient ingredient, int amount) {
        for(int i = 0; i < amount; ++i) {
            this.ingredients.add(ingredient);
        }

        return this;
    }

    public RecipeBuilder addCriterion(String name, ICriterionInstance criterion) {
        this.advancementBuilder.withCriterion(name, criterion);
        return this;
    }

    public RecipeBuilder dummyCriterion(String name) {
        return this.addCriterion(name, new ImpossibleTrigger.Instance());
    }

    public RecipeBuilder setGroup(String group) {
        this.group = group;
        return this;
    }

    public void build(Consumer<IFinishedRecipe> cons) {
        this.build(cons, ForgeRegistries.ITEMS.getKey(this.result.getItem()));
    }

    public void build(Consumer<IFinishedRecipe> cons, String string) {
        ResourceLocation resourcelocation = ForgeRegistries.ITEMS.getKey(this.result.getItem());
        if (resourcelocation.toString().equals(string)) {
            throw new IllegalStateException("Shapeless Recipe " + string + " should remove its 'save' argument");
        } else {
            this.build(cons, new ResourceLocation(string));
        }
    }

    public void build(Consumer<IFinishedRecipe> cons, ResourceLocation res) {
        this.validate(res);
        this.advancementBuilder.withParentId(new ResourceLocation("recipes/root")).withCriterion("has_the_recipe", RecipeUnlockedTrigger.create(res)).withRewards(AdvancementRewards.Builder.recipe(res)).withRequirementsStrategy(IRequirementsStrategy.OR);
        cons.accept(new Result(res, this.result, this.level, this.group == null ? "" : this.group, this.ingredients, this.advancementBuilder, new ResourceLocation(res.getNamespace(), "recipes/" + this.result.getItem().getGroup().getPath() + "/" + res.getPath())){
            @Override
            public IRecipeSerializer<?> getSerializer() {
                return RecipeBuilder.this.serializer;
            }
        });
    }

    private void validate(ResourceLocation res) {
        if (this.advancementBuilder.getCriteria().isEmpty())
            throw new IllegalStateException("No way of obtaining recipe " + res);
        if(this.ingredients.size()>6)
            throw new IllegalStateException("Recipe " + res +  " too big. Max size is 6");
    }

    public static class Result implements IFinishedRecipe {
        private final ResourceLocation id;
        private final ItemStack result;
        private final int level;
        private final String group;
        private final List<Ingredient> ingredients;
        private final Advancement.Builder advancementBuilder;
        private final ResourceLocation advancementId;

        public Result(ResourceLocation res, ItemStack output, int level, String group, List<Ingredient> ingredients, Advancement.Builder advancements, ResourceLocation advancementID) {
            this.id = res;
            this.result = output;
            this.level = level;
            this.group = group;
            this.ingredients = ingredients;
            this.advancementBuilder = advancements;
            this.advancementId = advancementID;
        }

        @Override
        public void serialize(JsonObject obj) {
            if (!this.group.isEmpty()) {
                obj.addProperty("group", this.group);
            }
            obj.addProperty("level", this.level);
            JsonArray jsonarray = new JsonArray();

            for(Ingredient ingredient : this.ingredients) {
                jsonarray.add(ingredient.serialize());
            }

            obj.add("ingredients", jsonarray);
            obj.add("result", this.itemStackToJson(this.result));
        }

        private JsonObject itemStackToJson(ItemStack stack){
            JsonObject obj = new JsonObject();
            obj.addProperty("item", stack.getItem().getRegistryName().toString());
            if(stack.getCount() > 1)
                obj.addProperty("count", stack.getCount());
            if(stack.hasTag())
                obj.addProperty("nbt", stack.getTag().toString());
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
            return this.advancementBuilder.serialize();
        }

        @Override
        @Nullable
        public ResourceLocation getAdvancementID() {
            return this.advancementId;
        }
    }
}
