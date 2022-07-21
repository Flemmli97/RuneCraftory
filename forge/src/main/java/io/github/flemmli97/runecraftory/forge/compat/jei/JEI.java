package io.github.flemmli97.runecraftory.forge.compat.jei;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.crafting.ArmorRecipe;
import io.github.flemmli97.runecraftory.common.crafting.ChemistryRecipe;
import io.github.flemmli97.runecraftory.common.crafting.CookingRecipe;
import io.github.flemmli97.runecraftory.common.crafting.ForgingRecipe;
import io.github.flemmli97.runecraftory.common.registry.ModCrafting;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.forge.compat.jei.category.SextupleRecipeCategory;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;

@SuppressWarnings("removal")
@JeiPlugin
public class JEI implements IModPlugin {

    private static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "jei_integration");
    public static IJeiRuntime runtime;

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
                new SextupleRecipeCategory<>(registration.getJeiHelpers().getGuiHelper(), ForgingRecipe.class, SextupleRecipeCategory.FORGECATEGORY, ModItems.itemBlockForge.get()),
                new SextupleRecipeCategory<>(registration.getJeiHelpers().getGuiHelper(), CookingRecipe.class, SextupleRecipeCategory.COOKINGCATEGORY, ModItems.itemBlockCooking.get()),
                new SextupleRecipeCategory<>(registration.getJeiHelpers().getGuiHelper(), ArmorRecipe.class, SextupleRecipeCategory.ARMORCATEGORY, ModItems.itemBlockAccess.get()),
                new SextupleRecipeCategory<>(registration.getJeiHelpers().getGuiHelper(), ChemistryRecipe.class, SextupleRecipeCategory.CHEMISTRYCATEGORY, ModItems.itemBlockChem.get())
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration reg) {
        if (Minecraft.getInstance() == null || Minecraft.getInstance().level == null)
            return;
        reg.getIngredientManager().removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, ModItems.NOTEX.stream()
                .map(sup -> new ItemStack(sup.get())).toList());
        RecipeManager manager = Minecraft.getInstance().level.getRecipeManager();
        reg.addRecipes(manager.getAllRecipesFor(ModCrafting.FORGE.get()), SextupleRecipeCategory.FORGECATEGORY);
        reg.addRecipes(manager.getAllRecipesFor(ModCrafting.ARMOR.get()), SextupleRecipeCategory.ARMORCATEGORY);
        reg.addRecipes(manager.getAllRecipesFor(ModCrafting.COOKING.get()), SextupleRecipeCategory.COOKINGCATEGORY);
        reg.addRecipes(manager.getAllRecipesFor(ModCrafting.CHEMISTRY.get()), SextupleRecipeCategory.CHEMISTRYCATEGORY);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(new SextupleRecipeTransfer(SextupleRecipeCategory.FORGECATEGORY));
        registration.addRecipeTransferHandler(new SextupleRecipeTransfer(SextupleRecipeCategory.COOKINGCATEGORY));
        registration.addRecipeTransferHandler(new SextupleRecipeTransfer(SextupleRecipeCategory.ARMORCATEGORY));
        registration.addRecipeTransferHandler(new SextupleRecipeTransfer(SextupleRecipeCategory.CHEMISTRYCATEGORY));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModItems.itemBlockForge.get()), SextupleRecipeCategory.FORGECATEGORY);
        registration.addRecipeCatalyst(new ItemStack(ModItems.itemBlockAccess.get()), SextupleRecipeCategory.ARMORCATEGORY);
        registration.addRecipeCatalyst(new ItemStack(ModItems.itemBlockCooking.get()), SextupleRecipeCategory.COOKINGCATEGORY);
        registration.addRecipeCatalyst(new ItemStack(ModItems.itemBlockChem.get()), SextupleRecipeCategory.CHEMISTRYCATEGORY);
    }
}
