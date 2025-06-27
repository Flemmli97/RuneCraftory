package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.recipes.ArmorRecipe;
import io.github.flemmli97.runecraftory.common.recipes.ChemistryRecipe;
import io.github.flemmli97.runecraftory.common.recipes.CookingRecipe;
import io.github.flemmli97.runecraftory.common.recipes.ForgingRecipe;
import io.github.flemmli97.runecraftory.common.recipes.HammerRemainderRecipe;
import io.github.flemmli97.runecraftory.common.recipes.LevelUpUpgradeRecipe;
import io.github.flemmli97.runecraftory.common.recipes.SextupleRecipe;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.registry.LoaderRegister;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapelessRecipe;

public class ModCrafting {

    public static final LoaderRegister<RecipeSerializer<?>> RECIPESERIALIZER = LoaderRegistryAccess.INSTANCE.of(Registries.RECIPE_SERIALIZER, RuneCraftory.MODID);
    public static final LoaderRegister<RecipeType<?>> RECIPETYPE = LoaderRegistryAccess.INSTANCE.of(Registries.RECIPE_TYPE, RuneCraftory.MODID);

    public static final RegistryEntrySupplier<RecipeSerializer<?>, RecipeSerializer<ArmorRecipe>> ARMOR_SERIALIZER = RECIPESERIALIZER.register("armor", () -> new SextupleRecipe.Serializer<>(ArmorRecipe::new));
    public static final RegistryEntrySupplier<RecipeSerializer<?>, RecipeSerializer<ChemistryRecipe>> CHEMISTRY_SERIALIZER = RECIPESERIALIZER.register("chemistry", () -> new SextupleRecipe.Serializer<>(ChemistryRecipe::new));
    public static final RegistryEntrySupplier<RecipeSerializer<?>, RecipeSerializer<CookingRecipe>> COOKING_SERIALIZER = RECIPESERIALIZER.register("cooking", () -> new SextupleRecipe.Serializer<>(CookingRecipe::new));
    public static final RegistryEntrySupplier<RecipeSerializer<?>, RecipeSerializer<ForgingRecipe>> FORGING_SERIALIZER = RECIPESERIALIZER.register("forge", () -> new SextupleRecipe.Serializer<>(ForgingRecipe::new));
    public static final RegistryEntrySupplier<RecipeSerializer<?>, RecipeSerializer<LevelUpUpgradeRecipe>> LEVEL_UPGRADE_SERIALIZER = RECIPESERIALIZER.register("level_upgrade", LevelUpUpgradeRecipe.Serializer::new);
    public static final RegistryEntrySupplier<RecipeSerializer<?>, RecipeSerializer<ShapelessRecipe>> HAMMER_REMAINDER_SERIALIZER = RECIPESERIALIZER.register("hammer_remainder", HammerRemainderRecipe.Serializer::new);

    public static final RegistryEntrySupplier<RecipeType<?>, RecipeType<SextupleRecipe>> CHEMISTRY = registerType("chemistry_recipe");
    public static final RegistryEntrySupplier<RecipeType<?>, RecipeType<SextupleRecipe>> ARMOR = registerType("armor_recipe");
    public static final RegistryEntrySupplier<RecipeType<?>, RecipeType<SextupleRecipe>> COOKING = registerType("cooking_recipe");
    public static final RegistryEntrySupplier<RecipeType<?>, RecipeType<SextupleRecipe>> FORGE = registerType("forge_recipe");
    public static final RegistryEntrySupplier<RecipeType<?>, RecipeType<SextupleRecipe>> LEVEL_UP = registerType("level_up");

    private static <T extends Recipe<?>> RegistryEntrySupplier<RecipeType<?>, RecipeType<T>> registerType(String name) {
        return RECIPETYPE.register(name, () -> new RecipeType<>() {
            @Override
            public String toString() {
                return name;
            }
        });
    }
}
