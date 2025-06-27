package io.github.flemmli97.runecraftory.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class GuiUtils {

    public static void drawBorderedBar(GuiGraphics graphics, ResourceLocation sprite, int x, int y,
                                       int textureWidth, int textureHeight, int width,
                                       int xBorder, int yBorder) {
        graphics.blitSprite(sprite, textureWidth, textureHeight, xBorder, yBorder, x + xBorder, y + yBorder,
                width, textureHeight - (2 * yBorder));
    }
}
