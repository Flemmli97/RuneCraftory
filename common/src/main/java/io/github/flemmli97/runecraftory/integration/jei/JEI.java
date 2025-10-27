package io.github.flemmli97.runecraftory.integration.jei;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.gui.CraftingGui;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.recipes.SextupleRecipe;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryCrafting;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryItems;
import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
import io.github.flemmli97.tenshilib.loader.registry.AttachmentRegister;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.handlers.IGuiClickableArea;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

@JeiPlugin
public class JEI implements IModPlugin {

    private static final ResourceLocation ID = RuneCraftory.modRes("jei_integration");

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
                new SextupleRecipeCategory<>(registration.getJeiHelpers().getGuiHelper(), CraftingIdentifier.FORGING.identifier(), RuneCraftoryItems.FORGE.get()),
                new SextupleRecipeCategory<>(registration.getJeiHelpers().getGuiHelper(), CraftingIdentifier.COOKING.identifier(), RuneCraftoryItems.COOKING_TABLE.get()),
                new SextupleRecipeCategory<>(registration.getJeiHelpers().getGuiHelper(), CraftingIdentifier.ARMOR.identifier(), RuneCraftoryItems.ACCESSORY_WORKBENCH.get()),
                new SextupleRecipeCategory<>(registration.getJeiHelpers().getGuiHelper(), CraftingIdentifier.CHEMISTRY.identifier(), RuneCraftoryItems.CHEMISTRY_SET.get())
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration reg) {
        if (Minecraft.getInstance().level == null)
            return;
        RecipeManager manager = Minecraft.getInstance().level.getRecipeManager();
        reg.addRecipes(CraftingIdentifier.FORGING.identifier(), sorted(manager, RuneCraftoryCrafting.FORGE.get()));
        reg.addRecipes(CraftingIdentifier.ARMOR.identifier(), sorted(manager, RuneCraftoryCrafting.ARMOR.get()));
        reg.addRecipes(CraftingIdentifier.COOKING.identifier(), sorted(manager, RuneCraftoryCrafting.COOKING.get()));
        reg.addRecipes(CraftingIdentifier.CHEMISTRY.identifier(), sorted(manager, RuneCraftoryCrafting.CHEMISTRY.get()));
    }

    private static <T extends SextupleRecipe> List<RecipeHolder<T>> sorted(RecipeManager manager, net.minecraft.world.item.crafting.RecipeType<T> type) {
        List<RecipeHolder<T>> l = new ArrayList<>(manager.getAllRecipesFor(type));
        l.sort(Comparator.comparingInt(h -> h.value().getCraftingLevel()));
        return l;
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(new SextupleRecipeTransfer(CraftingIdentifier.FORGING));
        registration.addRecipeTransferHandler(new SextupleRecipeTransfer(CraftingIdentifier.COOKING));
        registration.addRecipeTransferHandler(new SextupleRecipeTransfer(CraftingIdentifier.ARMOR));
        registration.addRecipeTransferHandler(new SextupleRecipeTransfer(CraftingIdentifier.CHEMISTRY));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(RuneCraftoryItems.FORGE.get()), CraftingIdentifier.FORGING.identifier());
        registration.addRecipeCatalyst(new ItemStack(RuneCraftoryItems.ACCESSORY_WORKBENCH.get()), CraftingIdentifier.ARMOR.identifier());
        registration.addRecipeCatalyst(new ItemStack(RuneCraftoryItems.COOKING_TABLE.get()), CraftingIdentifier.COOKING.identifier());
        registration.addRecipeCatalyst(new ItemStack(RuneCraftoryItems.CHEMISTRY_SET.get()), CraftingIdentifier.CHEMISTRY.identifier());
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGuiContainerHandler(CraftingGui.class, new IGuiContainerHandler<>() {
            @Override
            public Collection<IGuiClickableArea> getGuiClickableAreas(CraftingGui gui, double mouseX, double mouseY) {
                RecipeType<?> type = CraftingIdentifier.get(gui.type()).identifier();
                IGuiClickableArea clickableArea = IGuiClickableArea.createBasic(80, 30, 26, 26, type);
                return List.of(clickableArea);
            }
        });
    }

    public static <T> Stream<T> filterLocked(Stream<T> recipes, IRecipeCategory<T> recipeTypeData, IFocusGroup focusGroup) {
        if (CraftingIdentifier.has(recipeTypeData.getRecipeType())) {
            Player player = Minecraft.getInstance().player;
            boolean ingredient = focusGroup.getFocuses(RecipeIngredientRole.INPUT).findAny().isPresent();
            Predicate<T> keep = recipe -> {
                if (player == null)
                    return true;
                PlayerData data = RunecraftoryAttachments.PLAYER_DATA.get().get(player);
                if (ingredient)
                    return data.getRecipeKeeper().isUnlocked((RecipeHolder<?>) recipe);
                return data.getRecipeKeeper().isUnlockedForCrafting((RecipeHolder<?>) recipe);
            };
            return recipes.filter(keep);
        }
        return recipes;
    }
}
