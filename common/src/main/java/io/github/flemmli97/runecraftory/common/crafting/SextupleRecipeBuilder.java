package io.github.flemmli97.runecraftory.common.crafting;

import io.github.flemmli97.runecraftory.api.enums.EnumCrafting;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class SextupleRecipeBuilder {

    private final ItemStack result;
    private final int level, addCost;
    private final NonNullList<Ingredient> ingredients = NonNullList.withSize(6, Ingredient.EMPTY);
    private final SextupleRecipe.Serializer.Factory<?> factory;
    private final EnumCrafting type;
    private String group;

    private SextupleRecipeBuilder(EnumCrafting type, ItemStack item, int level, int addCost, SextupleRecipe.Serializer.Factory<?> factory) {
        this.type = type;
        this.result = item;
        this.level = level;
        this.addCost = addCost;
        this.factory = factory;
    }

    public static SextupleRecipeBuilder create(EnumCrafting type, ItemLike item, int level) {
        return create(type, item, 1, level);
    }

    public static SextupleRecipeBuilder create(EnumCrafting type, ItemLike item, int count, int level) {
        return create(type, new ItemStack(item, count), level, 1);
    }

    public static SextupleRecipeBuilder create(EnumCrafting type, ItemLike item, int count, int level, int addCost) {
        return create(type, new ItemStack(item, count), level, addCost);
    }

    public static SextupleRecipeBuilder create(EnumCrafting type, ItemStack item, int level, int addCost) {
        SextupleRecipe.Serializer.Factory<?> factory = switch (type) {
            case FORGE -> ForgingRecipe::new;
            case ARMOR -> ArmorRecipe::new;
            case CHEM -> ChemistryRecipe::new;
            default -> CookingRecipe::new;
        };
        return new SextupleRecipeBuilder(type, item, level, addCost, factory);
    }

    public SextupleRecipeBuilder addIngredient(TagKey<Item> tag) {
        return this.addIngredient(Ingredient.of(tag));
    }

    public SextupleRecipeBuilder addIngredient(ItemLike item) {
        return this.addIngredient(item, 1);
    }

    public SextupleRecipeBuilder addIngredient(ItemLike item, int amount) {
        for (int i = 0; i < amount; ++i) {
            this.addIngredient(Ingredient.of(item));
        }
        return this;
    }

    public SextupleRecipeBuilder addIngredient(Ingredient ingredient) {
        return this.addIngredient(ingredient, 1);
    }

    public SextupleRecipeBuilder addIngredient(Ingredient ingredient, int amount) {
        for (int i = 0; i < amount; ++i) {
            this.ingredients.add(ingredient);
        }
        return this;
    }

    public SextupleRecipeBuilder setGroup(String group) {
        this.group = group;
        return this;
    }

    public void build(RecipeOutput output) {
        this.build(output, BuiltInRegistries.ITEM.getKey(this.result.getItem()));
    }

    public void build(RecipeOutput output, String string) {
        this.build(output, ResourceLocation.parse(string));
    }

    public void build(RecipeOutput output, ResourceLocation res) {
        res = ResourceLocation.fromNamespaceAndPath(res.getNamespace(), this.type.getId() + "/" + res.getPath());
        this.validate(res);
        output.accept(res, this.factory.get(this.group == null ? "" : this.group, this.level, this.addCost, this.result, this.ingredients), null);
    }

    private void validate(ResourceLocation res) {
        if (this.ingredients.size() > 6)
            throw new IllegalStateException("Recipe " + res + " too big. Max size is 6");
    }
}
