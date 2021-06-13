package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.crafting.ArmorRecipe;
import io.github.flemmli97.runecraftory.common.crafting.ChemistryRecipe;
import io.github.flemmli97.runecraftory.common.crafting.CookingRecipe;
import io.github.flemmli97.runecraftory.common.crafting.ForgingRecipe;
import io.github.flemmli97.runecraftory.common.crafting.SextupleRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModCrafting {

    public static final DeferredRegister<IRecipeSerializer<?>> RECIPESERIALIZER = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, RuneCraftory.MODID);

    public static final RegistryObject<IRecipeSerializer<?>> ARMORSERIALIZER = RECIPESERIALIZER.register("armor", ArmorRecipe.Serializer::new);
    public static final RegistryObject<IRecipeSerializer<?>> CHEMISTRYSERIALIZER = RECIPESERIALIZER.register("chemistry", ChemistryRecipe.Serializer::new);
    public static final RegistryObject<IRecipeSerializer<?>> COOKINGSERIALIZER = RECIPESERIALIZER.register("cooking", CookingRecipe.Serializer::new);
    public static final RegistryObject<IRecipeSerializer<?>> FORGESERIALIZER = RECIPESERIALIZER.register("forge", ForgingRecipe.Serializer::new);

    public static IRecipeType<SextupleRecipe> CHEMISTRY;
    public static IRecipeType<SextupleRecipe> ARMOR;
    public static IRecipeType<SextupleRecipe> COOKING;
    public static IRecipeType<SextupleRecipe> FORGE;

    public static void register(RegistryEvent.Register<IRecipeSerializer<?>> event) {
        CHEMISTRY = reg("chemistry_recipe");
        ARMOR = reg("armor_recipe");
        COOKING = reg("cooking_recipe");
        FORGE = reg("forge_recipe");
    }

    private static <T extends IRecipe<?>> IRecipeType<T> reg(String name) {
        return Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(RuneCraftory.MODID, name), new IRecipeType<T>() {
            @Override
            public String toString() {
                return name;
            }
        });
    }
}
