package com.flemmli97.runecraftory.integration.jei;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.crafting.ArmorRecipe;
import com.flemmli97.runecraftory.common.crafting.ChemistryRecipe;
import com.flemmli97.runecraftory.common.crafting.CookingRecipe;
import com.flemmli97.runecraftory.common.crafting.ForgingRecipe;
import com.flemmli97.runecraftory.common.registry.ModCrafting;
import com.flemmli97.runecraftory.common.registry.ModItems;
import com.flemmli97.runecraftory.integration.jei.category.SextupleRecipeCategory;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class JEI implements IModPlugin {

    private static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "jei_integration");
    public static IJeiRuntime runtime;

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModItems.itemBlockForge.get()), SextupleRecipeCategory.FORGECATEGORY);
        registration.addRecipeCatalyst(new ItemStack(ModItems.itemBlockAccess.get()), SextupleRecipeCategory.ARMORCATEGORY);
        registration.addRecipeCatalyst(new ItemStack(ModItems.itemBlockCooking.get()), SextupleRecipeCategory.COOKINGCATEGORY);
        registration.addRecipeCatalyst(new ItemStack(ModItems.itemBlockChem.get()), SextupleRecipeCategory.CHEMISTRYCATEGORY);
    }

    @Override
    public void registerRecipes(IRecipeRegistration reg) {
        if(Minecraft.getInstance() == null || Minecraft.getInstance().world == null)
            return;
        RecipeManager manager = Minecraft.getInstance().world.getRecipeManager();
        reg.addRecipes(manager.listAllOfType(ModCrafting.FORGE), SextupleRecipeCategory.FORGECATEGORY);
        reg.addRecipes(manager.listAllOfType(ModCrafting.ARMOR), SextupleRecipeCategory.ARMORCATEGORY);
        reg.addRecipes(manager.listAllOfType(ModCrafting.COOKING), SextupleRecipeCategory.COOKINGCATEGORY);
        reg.addRecipes(manager.listAllOfType(ModCrafting.CHEMISTRY), SextupleRecipeCategory.CHEMISTRYCATEGORY);
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
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(new SextupleRecipeTransfer(SextupleRecipeCategory.FORGECATEGORY));
        registration.addRecipeTransferHandler(new SextupleRecipeTransfer(SextupleRecipeCategory.COOKINGCATEGORY));
        registration.addRecipeTransferHandler(new SextupleRecipeTransfer(SextupleRecipeCategory.ARMORCATEGORY));
        registration.addRecipeTransferHandler(new SextupleRecipeTransfer(SextupleRecipeCategory.CHEMISTRYCATEGORY));
    }
}
