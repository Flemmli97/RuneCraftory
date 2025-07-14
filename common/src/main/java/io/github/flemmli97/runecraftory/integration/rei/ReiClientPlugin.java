package io.github.flemmli97.runecraftory.integration.rei;

import io.github.flemmli97.runecraftory.client.gui.CraftingGui;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryItems;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ClickArea;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;

import java.util.List;

public class ReiClientPlugin implements REIClientPlugin {

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new SextupleCategory(CraftingIdentifier.FORGING));
        registry.addWorkstations(CraftingIdentifier.FORGING.identifier(), EntryStacks.of(RuneCraftoryItems.FORGE.get()));
        registry.add(new SextupleCategory(CraftingIdentifier.CHEMISTRY));
        registry.addWorkstations(CraftingIdentifier.CHEMISTRY.identifier(), EntryStacks.of(RuneCraftoryItems.CHEMISTRY_SET.get()));
        registry.add(new SextupleCategory(CraftingIdentifier.COOKING));
        registry.addWorkstations(CraftingIdentifier.COOKING.identifier(), EntryStacks.of(RuneCraftoryItems.COOKING_TABLE.get()));
        registry.add(new SextupleCategory(CraftingIdentifier.ARMOR));
        registry.addWorkstations(CraftingIdentifier.ARMOR.identifier(), EntryStacks.of(RuneCraftoryItems.ACCESSORY_WORKBENCH.get()));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerDisplayGenerator(CraftingIdentifier.FORGING.identifier(),
                new SextupleDisplayGenerator(CraftingIdentifier.FORGING));
        registry.registerDisplayGenerator(CraftingIdentifier.ARMOR.identifier(),
                new SextupleDisplayGenerator(CraftingIdentifier.ARMOR));
        registry.registerDisplayGenerator(CraftingIdentifier.CHEMISTRY.identifier(),
                new SextupleDisplayGenerator(CraftingIdentifier.CHEMISTRY));
        registry.registerDisplayGenerator(CraftingIdentifier.COOKING.identifier(),
                new SextupleDisplayGenerator(CraftingIdentifier.COOKING));
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerClickArea(CraftingGui.class, clickAreaFor(CraftingIdentifier.FORGING, new Rectangle(80, 30, 26, 26)));
        registry.registerClickArea(CraftingGui.class, clickAreaFor(CraftingIdentifier.COOKING, new Rectangle(80, 30, 26, 26)));
        registry.registerClickArea(CraftingGui.class, clickAreaFor(CraftingIdentifier.ARMOR, new Rectangle(80, 30, 26, 26)));
        registry.registerClickArea(CraftingGui.class, clickAreaFor(CraftingIdentifier.CHEMISTRY, new Rectangle(80, 30, 26, 26)));
    }

    private static ClickArea<CraftingGui> clickAreaFor(CraftingIdentifier identifier, Rectangle rect) {
        return ctx -> {
            CraftingGui screen = ctx.getScreen();
            Rectangle rectangle = rect.clone();
            rectangle.translate(screen.getRectangle().left(), screen.getRectangle().top());
            if (ctx.getScreen().type() == identifier.craftingType() && rectangle.contains(ctx.getMousePosition())) {
                return ClickArea.Result.success().categories(List.of(identifier.identifier()));
            }
            return ClickArea.Result.fail();
        };
    }
}
