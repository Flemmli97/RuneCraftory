package com.flemmli97.runecraftory.client.gui;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.enums.EnumSeason;
import com.flemmli97.runecraftory.client.ClientHandlers;
import com.flemmli97.runecraftory.common.capability.CapabilityInsts;
import com.flemmli97.runecraftory.common.capability.IPlayerCap;
import com.flemmli97.runecraftory.common.config.ClientConfig;
import com.flemmli97.runecraftory.common.utils.CalendarImpl;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.lwjgl.opengl.GL11;

public class OverlayGui extends AbstractGui {

    private static final ResourceLocation texturepath = new ResourceLocation(RuneCraftory.MODID, "textures/gui/bars.png");
    private final Minecraft mc;

    public OverlayGui(Minecraft mc) {
        this.mc = mc;
    }

    public void renderBar(MatrixStack stack) {
        IPlayerCap cap = this.mc.player.getCapability(CapabilityInsts.PlayerCap).orElse(null);
        CalendarImpl calendar = ClientHandlers.clientCalendar;
        int xPos = ClientConfig.healthBarWidgetX;
        int yPos = ClientConfig.healthBarWidgetY;
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glDisable(2896);
        EnumSeason season = calendar.currentSeason();
        this.mc.getTextureManager().bindTexture(texturepath);
        if (cap != null && !this.mc.player.abilities.disableDamage) {
            this.drawTexture(stack, xPos, yPos, 0, 0, 96, 29);
            int healthWidth = Math.min(75, (int) (cap.getHealth(this.mc.player) / cap.getMaxHealth(this.mc.player) * 75.0f));
            int runePointsWidth = Math.min(75, (int) (cap.getRunePoints() / (float) cap.getMaxRunePoints() * 75.0f));
            this.drawTexture(stack, xPos + 18, yPos + 3, 18, 30, healthWidth, 9);
            this.drawTexture(stack, xPos + 18, yPos + 17, 18, 40, runePointsWidth, 9);
        }

        this.drawTexture(stack, ClientConfig.seasonDisplayX, ClientConfig.seasonDisplayY, 50, 176, 37, 36);
        this.drawTexture(stack, ClientConfig.seasonDisplayX + 3, ClientConfig.seasonDisplayY + 3, 0 + season.ordinal() * 32, 226, 32, 30);
        this.drawTexture(stack, ClientConfig.seasonDisplayX, ClientConfig.seasonDisplayY + 39, 0, 176, 48, 17);

        drawStringCenter(stack, this.mc.fontRenderer, new TranslationTextComponent(calendar.currentDay().translation()).append(new TranslationTextComponent(" " + calendar.date())), ClientConfig.seasonDisplayX + 26, ClientConfig.seasonDisplayY + 39 + 5, 0xbd1600);
    }

    public static void drawStringCenter(MatrixStack stack, FontRenderer fontRenderer, ITextComponent component, float x, float y, int color) {
        fontRenderer.draw(stack, component, x - fontRenderer.getWidth(component) / 2f, y, color);
    }
}