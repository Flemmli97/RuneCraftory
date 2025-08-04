package io.github.flemmli97.runecraftory.mixinhelper;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.creativetab.SubTab;
import io.github.flemmli97.tenshilib.client.gui.widget.list.SelectableEntry;
import io.github.flemmli97.tenshilib.client.gui.widget.list.SelectableListWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

/**
 * Injects subscreen buttons to the creative mode screen
 */
public class CreativeScreenHandler {

    public static final ResourceLocation SCROLLBAR = RuneCraftory.modRes("widget/sub_tab_scroll");
    public static final ResourceLocation BACKGROUND = RuneCraftory.modRes("widget/sub_tab_background");

    public static final int WIDGET_WIDTH = 42;

    public static SelectableListWidget onSelectTab(CreativeModeTab tab, int leftPos, int topPos, @Nullable SelectableListWidget current,
                                                   Consumer<AbstractWidget> remove, Consumer<AbstractWidget> add, Runnable refresh) {
        if (current != null)
            remove.accept(current);
        List<SubTab> subTabs = ((CreativeTabExtension) tab).runecraftory$getSubTabs();
        if (subTabs != null) {
            SelectableListWidget selection = new SelectableListWidget(leftPos - WIDGET_WIDTH, topPos + 12, WIDGET_WIDTH, 107, Minecraft.getInstance().font,
                    subTabs.stream().<SelectableEntry>map(t -> new CreativeTabEntry(t, () -> {
                        if (!Screen.hasShiftDown() && (!t.selected() || subTabs.stream().filter(SubTab::selected).count() > 1)) {
                            subTabs.forEach(sub -> sub.select(false));
                        }
                        t.select(!t.selected());
                        refresh.run();
                    })).toList())
                    .setEntryHeight(26, 1)
                    .withTexture(BACKGROUND)
                    .scrollbar(new SelectableListWidget.Scrollbar(SCROLLBAR, SCROLLBAR,
                            6, 25, 0, 0));
            if (current != null)
                selection.scrollTo(current.getScrollValue());
            add.accept(selection);
            return selection;
        }
        return null;
    }

    private static class CreativeTabEntry implements SelectableEntry {

        private final SubTab tab;
        private final Runnable onClick;

        private int width;

        private CreativeTabEntry(SubTab tab, Runnable onClick) {
            this.tab = tab;
            this.onClick = onClick;
        }

        @Override
        public void updateDimensions(int width, int height) {
            this.width = width;
        }

        @Override
        public void render(SelectableListWidget widget, GuiGraphics graphics, int mouseX, int mouseY, float partialTick, int x, int y, boolean selected, boolean hovered) {
            ResourceLocation texture;
            if (this.tab.selected())
                texture = this.tab.buttonInfo().buttonSelectedTexture();
            else if (hovered)
                texture = this.tab.buttonInfo().buttonHighlightTexture();
            else
                texture = this.tab.
                        buttonInfo().buttonTexture();
            x += this.width - this.tab.buttonInfo().width();
            graphics.blitSprite(texture, x, y, this.tab.buttonInfo().width(), this.tab.buttonInfo().height());
            Font font = widget.getFont();
            int itemX = x + 17;
            int itemY = y + 5;
            graphics.renderItem(this.tab.icon(), itemX, itemY);
            graphics.renderItemDecorations(font, this.tab.icon(), itemX, itemY);
            if (hovered)
                graphics.renderTooltip(font, this.tab.title(), mouseX, mouseY);
        }

        @Override
        public boolean onClick(double relativeMouseX, double relativeMouseY, boolean selected) {
            this.onClick.run();
            return true;
        }
    }
}
