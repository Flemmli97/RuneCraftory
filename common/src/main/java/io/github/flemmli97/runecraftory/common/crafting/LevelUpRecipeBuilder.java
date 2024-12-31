package io.github.flemmli97.runecraftory.common.crafting;

import com.google.gson.JsonObject;
import io.github.flemmli97.runecraftory.common.registry.ModCrafting;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class LevelUpRecipeBuilder implements FinishedRecipe {

    private final ResourceLocation id;
    private final int level;
    private final Ingredient base, material;

    private LevelUpRecipeBuilder(ResourceLocation id, int level, Ingredient base, Ingredient material) {
        this.id = id;
        this.level = level;
        this.base = base;
        this.material = material;
    }

    public static void build(Consumer<FinishedRecipe> cons, int level, Ingredient base, Ingredient material, String string) {
        build(cons, level, base, material, new ResourceLocation(string));
    }

    public static void build(Consumer<FinishedRecipe> cons, int level, Ingredient base, Ingredient material, ResourceLocation res) {
        cons.accept(new LevelUpRecipeBuilder(new ResourceLocation(res.getNamespace(), "level_upgrade/" + res.getPath()), level, base, material));
    }

    @Override
    public void serializeRecipeData(JsonObject obj) {
        obj.addProperty("current_level", this.level);
        obj.add("item", this.base.toJson());
        obj.add("material", this.material.toJson());
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getType() {
        return ModCrafting.LEVEL_UPGRADE_SERIALIZER.get();
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
