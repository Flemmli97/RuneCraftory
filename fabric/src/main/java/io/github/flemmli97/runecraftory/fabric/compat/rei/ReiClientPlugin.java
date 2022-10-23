package io.github.flemmli97.runecraftory.fabric.compat.rei;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.enums.EnumCrafting;
import io.github.flemmli97.runecraftory.client.gui.CraftingGui;
import io.github.flemmli97.runecraftory.common.crafting.SextupleRecipe;
import io.github.flemmli97.runecraftory.common.registry.ModCrafting;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.entry.EntryRegistry;
import me.shedaniel.rei.api.client.registry.screen.ClickArea;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import me.shedaniel.rei.api.common.registry.RecipeManagerContext;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

public class ReiClientPlugin implements REIClientPlugin {

    public static final CategoryIdentifier<SextupleDisplay> FORGING = CategoryIdentifier.of(new ResourceLocation(RuneCraftory.MODID, "forge_category"));
    public static final CategoryIdentifier<SextupleDisplay> CHEM = CategoryIdentifier.of(new ResourceLocation(RuneCraftory.MODID, "chemistry_category"));
    public static final CategoryIdentifier<SextupleDisplay> COOKING = CategoryIdentifier.of(new ResourceLocation(RuneCraftory.MODID, "cooking_category"));
    public static final CategoryIdentifier<SextupleDisplay> ARMOR = CategoryIdentifier.of(new ResourceLocation(RuneCraftory.MODID, "armor_category"));

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new SextupleCategory(EnumCrafting.FORGE));
        registry.addWorkstations(FORGING, EntryStacks.of(ModItems.itemBlockForge.get()));
        registry.add(new SextupleCategory(EnumCrafting.CHEM));
        registry.addWorkstations(CHEM, EntryStacks.of(ModItems.itemBlockChem.get()));
        registry.add(new SextupleCategory(EnumCrafting.COOKING));
        registry.addWorkstations(COOKING, EntryStacks.of(ModItems.itemBlockCooking.get()));
        registry.add(new SextupleCategory(EnumCrafting.ARMOR));
        registry.addWorkstations(ARMOR, EntryStacks.of(ModItems.itemBlockAccess.get()));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        for (SextupleRecipe r : sorted(RecipeManagerContext.getInstance().getRecipeManager(), ModCrafting.FORGE.get()))
            registry.add(new SextupleDisplay(r, EnumCrafting.FORGE), r);
        for (SextupleRecipe r : sorted(RecipeManagerContext.getInstance().getRecipeManager(), ModCrafting.ARMOR.get()))
            registry.add(new SextupleDisplay(r, EnumCrafting.ARMOR), r);
        for (SextupleRecipe r : sorted(RecipeManagerContext.getInstance().getRecipeManager(), ModCrafting.COOKING.get()))
            registry.add(new SextupleDisplay(r, EnumCrafting.COOKING), r);
        for (SextupleRecipe r : sorted(RecipeManagerContext.getInstance().getRecipeManager(), ModCrafting.CHEMISTRY.get()))
            registry.add(new SextupleDisplay(r, EnumCrafting.CHEM), r);
    }

    private static <T extends SextupleRecipe> List<T> sorted(RecipeManager manager, RecipeType<T> type) {
        List<T> l = manager.getAllRecipesFor(type);
        l.sort(Comparator.comparingInt(SextupleRecipe::getCraftingLevel));
        return l;
    }

    @Override
    public void registerEntries(EntryRegistry registry) {
        List<Item> list = ModItems.NOTEX.stream().map(Supplier::get).toList();
        registry.removeEntryIf(e -> e.getType() == VanillaEntryTypes.ITEM && list.contains(((ItemStack) e.castValue()).getItem()));
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerClickArea(CraftingGui.class, clickAreaFor(EnumCrafting.FORGE, new Rectangle(80, 30, 26, 26), FORGING));
        registry.registerClickArea(CraftingGui.class, clickAreaFor(EnumCrafting.COOKING, new Rectangle(80, 30, 26, 26), COOKING));
        registry.registerClickArea(CraftingGui.class, clickAreaFor(EnumCrafting.ARMOR, new Rectangle(80, 30, 26, 26), ARMOR));
        registry.registerClickArea(CraftingGui.class, clickAreaFor(EnumCrafting.CHEM, new Rectangle(80, 30, 26, 26), CHEM));
    }

    private static ClickArea<CraftingGui> clickAreaFor(EnumCrafting type, Rectangle rect, CategoryIdentifier<?>... categories) {
        return ctx -> {
            CraftingGui screen = ctx.getScreen();
            Rectangle rectangle = rect.clone();
            rectangle.translate(screen.getLeft(), screen.getTop());
            if (ctx.getScreen().type() == type && rectangle.contains(ctx.getMousePosition())) {
                return ClickArea.Result.success().categories(Arrays.asList(categories));
            }
            return ClickArea.Result.fail();
        };
    }
}
