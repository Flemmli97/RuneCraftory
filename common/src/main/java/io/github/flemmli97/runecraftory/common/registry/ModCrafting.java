package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.crafting.ArmorRecipe;
import io.github.flemmli97.runecraftory.common.crafting.ChemistryRecipe;
import io.github.flemmli97.runecraftory.common.crafting.CookingRecipe;
import io.github.flemmli97.runecraftory.common.crafting.ForgingRecipe;
import io.github.flemmli97.runecraftory.common.crafting.SextupleRecipe;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import io.github.flemmli97.tenshilib.platform.registry.PlatformRegistry;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class ModCrafting {

    public static final PlatformRegistry<RecipeSerializer<?>> RECIPESERIALIZER = PlatformUtils.INSTANCE.of(Registry.RECIPE_SERIALIZER_REGISTRY, RuneCraftory.MODID);

    public static final RegistryEntrySupplier<RecipeSerializer<?>> ARMORSERIALIZER = RECIPESERIALIZER.register("armor", ArmorRecipe.Serializer::new);
    public static final RegistryEntrySupplier<RecipeSerializer<?>> CHEMISTRYSERIALIZER = RECIPESERIALIZER.register("chemistry", ChemistryRecipe.Serializer::new);
    public static final RegistryEntrySupplier<RecipeSerializer<?>> COOKINGSERIALIZER = RECIPESERIALIZER.register("cooking", CookingRecipe.Serializer::new);
    public static final RegistryEntrySupplier<RecipeSerializer<?>> FORGESERIALIZER = RECIPESERIALIZER.register("forge", ForgingRecipe.Serializer::new);

    public static final PlatformRegistry<RecipeType<?>> RECIPETYPE = PlatformUtils.INSTANCE.of(Registry.RECIPE_TYPE_REGISTRY, RuneCraftory.MODID);

    public static final RegistryEntrySupplier<RecipeType<SextupleRecipe>> CHEMISTRY = reg("chemistry_recipe");
    public static final RegistryEntrySupplier<RecipeType<SextupleRecipe>> ARMOR = reg("armor_recipe");
    public static final RegistryEntrySupplier<RecipeType<SextupleRecipe>> COOKING = reg("cooking_recipe");
    public static final RegistryEntrySupplier<RecipeType<SextupleRecipe>> FORGE = reg("forge_recipe");

    private static <T extends Recipe<?>> RegistryEntrySupplier<RecipeType<T>> reg(String name) {
        return RECIPETYPE.register(name, () -> new RecipeType<>() {
            @Override
            public String toString() {
                return name;
            }
        });
    }
}
