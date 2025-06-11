package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.crafting.ArmorRecipe;
import io.github.flemmli97.runecraftory.common.crafting.ChemistryRecipe;
import io.github.flemmli97.runecraftory.common.crafting.CookingRecipe;
import io.github.flemmli97.runecraftory.common.crafting.ForgingRecipe;
import io.github.flemmli97.runecraftory.common.crafting.HammerRemainderRecipe;
import io.github.flemmli97.runecraftory.common.crafting.LevelUpUpgradeRecipe;
import io.github.flemmli97.runecraftory.common.crafting.SextupleRecipe;
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

    public static final RegistryEntrySupplier<RecipeSerializer<?>, RecipeSerializer<ArmorRecipe>> ARMOR_SERIALIZER = RECIPESERIALIZER.register("armor", ArmorRecipe.Serializer::new);
    public static final RegistryEntrySupplier<RecipeSerializer<?>, RecipeSerializer<ChemistryRecipe>> CHEMISTRY_SERIALIZER = RECIPESERIALIZER.register("chemistry", ChemistryRecipe.Serializer::new);
    public static final RegistryEntrySupplier<RecipeSerializer<?>, RecipeSerializer<CookingRecipe>> COOKING_SERIALIZER = RECIPESERIALIZER.register("cooking", CookingRecipe.Serializer::new);
    public static final RegistryEntrySupplier<RecipeSerializer<?>, RecipeSerializer<ForgingRecipe>> FORGING_SERIALIZER = RECIPESERIALIZER.register("forge", ForgingRecipe.Serializer::new);
    public static final RegistryEntrySupplier<RecipeSerializer<?>, RecipeSerializer<LevelUpUpgradeRecipe>> LEVEL_UPGRADE_SERIALIZER = RECIPESERIALIZER.register("level_upgrade", LevelUpUpgradeRecipe.Serializer::new);
    public static final RegistryEntrySupplier<RecipeSerializer<?>, RecipeSerializer<ShapelessRecipe>> HAMMER_REMAINDER_SERIALIZER = RECIPESERIALIZER.register("hammer_remainder", HammerRemainderRecipe.Serializer::new);

    public static final LoaderRegister<RecipeType<?>> RECIPETYPE = LoaderRegistryAccess.INSTANCE.of(Registries.RECIPE_TYPE, RuneCraftory.MODID);

    public static final RegistryEntrySupplier<RecipeType<?>, RecipeType<SextupleRecipe>> CHEMISTRY = reg("chemistry_recipe");
    public static final RegistryEntrySupplier<RecipeType<?>, RecipeType<SextupleRecipe>> ARMOR = reg("armor_recipe");
    public static final RegistryEntrySupplier<RecipeType<?>, RecipeType<SextupleRecipe>> COOKING = reg("cooking_recipe");
    public static final RegistryEntrySupplier<RecipeType<?>, RecipeType<SextupleRecipe>> FORGE = reg("forge_recipe");

    private static <T extends Recipe<?>> RegistryEntrySupplier<RecipeType<?>, RecipeType<T>> reg(String name) {
        return RECIPETYPE.register(name, () -> new RecipeType<>() {
            @Override
            public String toString() {
                return name;
            }
        });
    }
}
