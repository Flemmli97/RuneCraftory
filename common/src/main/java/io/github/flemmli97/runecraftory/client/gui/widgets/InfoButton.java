package io.github.flemmli97.runecraftory.client.gui.widgets;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.mixin.CreativeModeInventoryScreenAccessor;
import io.github.flemmli97.tenshilib.client.gui.widget.TexturedButton;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;

public class InfoButton extends TexturedButton {

    public static final WidgetSprites SKILL_BUTTON = new WidgetSprites(RuneCraftory.modRes("widget/info_button"),
            RuneCraftory.modRes("widget/info_button_highlighted"));

    private final Screen ownerGui;

    public InfoButton(int x, int y, Screen ownerGui, OnPress press) {
        super(x, y, 12, 12, Component.empty(), press);
        this.withSprite(SKILL_BUTTON);
        this.ownerGui = ownerGui;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        if (this.ownerGui instanceof CreativeModeInventoryScreen
                && CreativeModeInventoryScreenAccessor.getSelectedTab().getType() != CreativeModeTab.Type.INVENTORY) {
            this.active = false;
            return;
        } else
            this.active = true;
        super.renderWidget(graphics, mouseX, mouseY, partialTick);
    }
}
