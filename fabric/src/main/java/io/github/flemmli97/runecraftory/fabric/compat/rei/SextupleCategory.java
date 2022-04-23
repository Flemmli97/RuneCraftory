package io.github.flemmli97.runecraftory.fabric.compat.rei;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.enums.EnumCrafting;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
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
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SextupleCategory implements DisplayCategory<SextupleDisplay> {

    private final EnumCrafting type;
    private final ResourceLocation res;
    public static final ResourceLocation GUI = new ResourceLocation(RuneCraftory.MODID, "textures/gui/forgec.png");

    public SextupleCategory(EnumCrafting type) {
        this.type = type;
        this.res = new ResourceLocation(RuneCraftory.MODID, this.type.getId() + "_category");
    }

    @Override
    public Renderer getIcon() {
        return switch (this.type) {
            case FORGE -> EntryStacks.of(ModItems.itemBlockForge.get());
            case ARMOR -> EntryStacks.of(ModItems.itemBlockAccess.get());
            case CHEM -> EntryStacks.of(ModItems.itemBlockChem.get());
            case COOKING -> EntryStacks.of(ModItems.itemBlockCooking.get());
        };
    }

    @Override
    public Component getTitle() {
        return new TranslatableComponent("tile.crafting." + this.type.getId());
    }

    @Override
    public CategoryIdentifier<? extends SextupleDisplay> getCategoryIdentifier() {
        return CategoryIdentifier.of(this.res);
    }

    @Override
    public List<Widget> setupDisplay(SextupleDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createTexturedWidget(GUI,
                bounds.getX(), bounds.getY(), 19, 20, 119, 41));
        Player player = Minecraft.getInstance().player;
        if (Platform.INSTANCE.getPlayerData(player).map(cap -> cap.getRecipeKeeper().isUnlocked(display.recipe())).orElse(false)) {
            for (int y = 0; y < 2; y++)
                for (int x = 0; x < 3; x++) {
                    int ind = x + y * 3;
                    if (ind < display.getInputEntries().size())
                        widgets.add(Widgets.createSlot(new Point(bounds.getX() + 1 + x * 18, bounds.getY() + 6 + y * 18)).entries(display.getInputEntries().get(ind)));
                }
        } else {
            widgets.add(Widgets.createSlot(new Point(bounds.getX() + 64, bounds.getY() + 14)).entry(EntryStacks.of(new ItemStack(ModItems.unknown.get()))
                    .tooltip(new TranslatableComponent("runecraftory.jei.locked"))));
        }
        widgets.add(Widgets.createSlot(new Point(bounds.getX() + 97, bounds.getY() + 15))
                .backgroundEnabled(false).entries(display.getOutputEntries().get(0)));
        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return 41;
    }

    @Override
    public int getDisplayWidth(SextupleDisplay display) {
        return 119;
    }
}
