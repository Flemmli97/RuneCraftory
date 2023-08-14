package io.github.flemmli97.runecraftory.forge.compat.jei;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.gui.CraftingGui;
import io.github.flemmli97.runecraftory.common.crafting.SextupleRecipe;
import io.github.flemmli97.runecraftory.common.registry.ModCrafting;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.platform.Platform;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.handlers.IGuiClickableArea;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import mezz.jei.recipes.RecipeTypeData;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

@JeiPlugin
public class JEI implements IModPlugin {

    private static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "jei_integration");
    public static IJeiRuntime JEI_RUNTIME;

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
                new SextupleRecipeCategory<>(registration.getJeiHelpers().getGuiHelper(), SextupleRecipeCategory.FORGECATEGORY, ModItems.itemBlockForge.get()),
                new SextupleRecipeCategory<>(registration.getJeiHelpers().getGuiHelper(), SextupleRecipeCategory.COOKINGCATEGORY, ModItems.itemBlockCooking.get()),
                new SextupleRecipeCategory<>(registration.getJeiHelpers().getGuiHelper(), SextupleRecipeCategory.ARMORCATEGORY, ModItems.itemBlockAccess.get()),
                new SextupleRecipeCategory<>(registration.getJeiHelpers().getGuiHelper(), SextupleRecipeCategory.CHEMISTRYCATEGORY, ModItems.itemBlockChem.get())
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration reg) {
        if (Minecraft.getInstance() == null || Minecraft.getInstance().level == null)
            return;
        reg.getIngredientManager().removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, ModItems.NOTEX.stream()
                .map(sup -> new ItemStack(sup.get())).toList());
        RecipeManager manager = Minecraft.getInstance().level.getRecipeManager();
        reg.addRecipes(SextupleRecipeCategory.FORGECATEGORY, sorted(manager, ModCrafting.FORGE.get()));
        reg.addRecipes(SextupleRecipeCategory.ARMORCATEGORY, sorted(manager, ModCrafting.ARMOR.get()));
        reg.addRecipes(SextupleRecipeCategory.COOKINGCATEGORY, sorted(manager, ModCrafting.COOKING.get()));
        reg.addRecipes(SextupleRecipeCategory.CHEMISTRYCATEGORY, sorted(manager, ModCrafting.CHEMISTRY.get()));
    }

    private static <T extends SextupleRecipe> List<T> sorted(RecipeManager manager, net.minecraft.world.item.crafting.RecipeType<T> type) {
        List<T> l = manager.getAllRecipesFor(type);
        l.sort(Comparator.comparingInt(SextupleRecipe::getCraftingLevel));
        return l;
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

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        JEI_RUNTIME = jeiRuntime;
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGuiContainerHandler(CraftingGui.class, new IGuiContainerHandler<>() {
            @Override
            public Collection<IGuiClickableArea> getGuiClickableAreas(CraftingGui gui, double mouseX, double mouseY) {
                RecipeType<?> type = switch (gui.type()) {
                    case FORGE -> SextupleRecipeCategory.FORGECATEGORY;
                    case COOKING -> SextupleRecipeCategory.COOKINGCATEGORY;
                    case ARMOR -> SextupleRecipeCategory.ARMORCATEGORY;
                    case CHEM -> SextupleRecipeCategory.CHEMISTRYCATEGORY;
                };
                IGuiClickableArea clickableArea = IGuiClickableArea.createBasic(80, 30, 26, 26, type);
                return List.of(clickableArea);
            }
        });
    }

    public static <T> Stream<T> filterLocked(Stream<T> recipes, RecipeTypeData<T> recipeTypeData) {
        if (recipeTypeData.getRecipeCategory().getRecipeType().equals(SextupleRecipeCategory.FORGECATEGORY)
                || recipeTypeData.getRecipeCategory().getRecipeType().equals(SextupleRecipeCategory.ARMORCATEGORY)
                || recipeTypeData.getRecipeCategory().getRecipeType().equals(SextupleRecipeCategory.CHEMISTRYCATEGORY)
                || recipeTypeData.getRecipeCategory().getRecipeType().equals(SextupleRecipeCategory.COOKINGCATEGORY)) {
            Player player = Minecraft.getInstance().player;
            Predicate<T> match = recipe -> Platform.INSTANCE.getPlayerData(player).map(cap -> cap.getRecipeKeeper().isUnlocked((Recipe<?>) recipe)).orElse(false);
            return recipes.filter(match);
        }
        return recipes;
    }
}
