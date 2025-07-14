package io.github.flemmli97.runecraftory.integration.rei;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.blocks.entity.CraftingBlockEntity;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryItems;
import io.github.flemmli97.runecraftory.platform.Platform;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SextupleCategory implements DisplayCategory<SextupleDisplay> {

    public static final ResourceLocation GUI = RuneCraftory.modRes("textures/gui/crafting.png");
    private final CraftingIdentifier identifier;

    private static final int X_SIZE = 119;
    private static final int Y_SIZE = 42;

    public SextupleCategory(CraftingIdentifier identifier) {
        this.identifier = identifier;
    }

    @Override
    public Renderer getIcon() {
        return switch (this.identifier.craftingType()) {
            case FORGE -> EntryStacks.of(RuneCraftoryItems.FORGE.get());
            case ACCESSORY_WORKBENCH -> EntryStacks.of(RuneCraftoryItems.ACCESSORY_WORKBENCH.get());
            case CHEMISTRY_SET -> EntryStacks.of(RuneCraftoryItems.CHEMISTRY_SET.get());
            case COOKING_TABLE -> EntryStacks.of(RuneCraftoryItems.COOKING_TABLE.get());
        };
    }

    @Override
    public Component getTitle() {
        return Component.translatable(CraftingBlockEntity.DISPLAY_PREFIX + this.identifier.craftingType().getId());
    }

    @Override
    public List<Widget> setupDisplay(SextupleDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createTexturedWidget(GUI,
                bounds.getX(), bounds.getY(), 19, 20, X_SIZE, Y_SIZE));
        Player player = Minecraft.getInstance().player;
        if (display.recipe() != null && Platform.INSTANCE.getPlayerData(player).getRecipeKeeper().isUnlocked(display.recipe())) {
            for (int y = 0; y < 2; y++) {
                for (int x = 0; x < 3; x++) {
                    int ind = x + y * 3;
                    if (ind < display.getInputEntries().size())
                        widgets.add(Widgets.createSlot(new Point(bounds.getX() + 1 + x * 18, bounds.getY() + 6 + y * 18)).entries(display.getInputEntries().get(ind)));
                }
            }
            Component level = Component.translatable("runecraftory.recipe_integration.crafting_level", display.recipe().value().getCraftingLevel());
            widgets.add(Widgets.createLabel(new Point(bounds.getX() + bounds.getWidth(), bounds.getY()), level).noShadow().rightAligned().color(0xFF404040, 0xFFBBBBBB));
        } else {
            widgets.add(Widgets.createSlot(new Point(bounds.getX() + 64, bounds.getY() + 14)).entry(EntryStacks.of(new ItemStack(RuneCraftoryItems.UNKNOWN.get()))
                    .tooltip(Component.translatable("runecraftory.recipe_integration.locked"))));
        }
        widgets.add(Widgets.createSlot(new Point(bounds.getX() + 97, bounds.getY() + 15))
                .backgroundEnabled(false).entries(display.getOutputEntries().getFirst()));
        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return Y_SIZE;
    }

    @Override
    public int getDisplayWidth(SextupleDisplay display) {
        return X_SIZE;
    }

    @Override
    public CategoryIdentifier<? extends SextupleDisplay> getCategoryIdentifier() {
        return this.identifier.identifier();
    }
}
