package io.github.flemmli97.runecraftory.mixinhelper;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

/**
 * Vanillas draw only supports int positions despite the internals allowing floats...
 */
public interface GuiGraphicsExtension {

    static void drawCenteredString(GuiGraphics graphics, Font font, Component component, float x, float y, int color, boolean dropShadow) {
        x -= font.width(component) * 0.5;
        ((GuiGraphicsExtension) graphics).runeraftory$drawString(font, component, x, y, color, dropShadow);
    }

    static void drawCenteredString(GuiGraphics graphics, Font font, String text, float x, float y, int color, boolean dropShadow) {
        x -= font.width(text) * 0.5;
        ((GuiGraphicsExtension) graphics).runeraftory$drawString(font, text, x, y, color, dropShadow);
    }

    static void drawRightAlignedString(GuiGraphics graphics, Font font, Component component, float x, float y, int color, boolean dropShadow) {
        x -= font.width(component);
        ((GuiGraphicsExtension) graphics).runeraftory$drawString(font, component, x, y, color, dropShadow);
    }

    static void drawRightAlignedString(GuiGraphics graphics, Font font, String text, float x, float y, int color, boolean dropShadow) {
        x -= font.width(text);
        ((GuiGraphicsExtension) graphics).runeraftory$drawString(font, text, x, y, color, dropShadow);
    }

    int runeraftory$drawString(Font font, @Nullable String text, float x, float y, int color, boolean dropShadow);

    int runeraftory$drawString(Font font, Component text, float x, float y, int color, boolean dropShadow);
}
