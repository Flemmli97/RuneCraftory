package com.flemmli97.runecraftory.client.gui;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.enums.EnumSeason;
import com.flemmli97.runecraftory.client.ClientHandlers;
import com.flemmli97.runecraftory.common.capability.CapabilityInsts;
import com.flemmli97.runecraftory.common.capability.IPlayerCap;
import com.flemmli97.runecraftory.common.utils.CalendarImpl;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
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
        int xPos = 2;
        int yPos = 2;
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glDisable(2896);
        EnumSeason season = calendar.currentSeason();
        this.mc.getTextureManager().bindTexture(texturepath);
        this.drawTexture(stack, xPos, yPos, 0, 0, 96, 29);
        if (cap != null) {
            int healthWidth = Math.min(75, (int) (cap.getHealth(this.mc.player) / cap.getMaxHealth(this.mc.player) * 75.0f));
            int runePointsWidth = Math.min(75, (int) (cap.getRunePoints() / (float) cap.getMaxRunePoints() * 75.0f));
            this.drawTexture(stack, xPos + 18, yPos + 3, 18, 30, healthWidth, 9);
            this.drawTexture(stack, xPos + 18, yPos + 17, 18, 40, runePointsWidth, 9);
        }
        drawCenteredText(stack, this.mc.fontRenderer, new TranslationTextComponent(season.translationKey()).formatted(season.getColor()).append(" " + calendar.date() + " ").append(new TranslationTextComponent(calendar.currentDay().translation())), 50, 50, 0);
    }
}