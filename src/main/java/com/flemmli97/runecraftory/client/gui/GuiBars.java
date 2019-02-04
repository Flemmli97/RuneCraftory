package com.flemmli97.runecraftory.client.gui;

import org.lwjgl.opengl.GL11;

import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;
import com.flemmli97.runecraftory.common.core.handler.time.CalendarHandler;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiBars extends Gui
{
    private Minecraft mc;
    public static int width;
    private static final ResourceLocation texturepath = new ResourceLocation(LibReference.MODID,"textures/gui/bars.png");
    
    public GuiBars(Minecraft mc) {
        this.mc = mc;
        this.renderBar();
    }
    
    private void renderBar() {
        IPlayer cap = this.mc.player.getCapability(PlayerCapProvider.PlayerCap, null);
        CalendarHandler calendar = CalendarHandler.get(this.mc.player.world);
        int xPos = 2;
        int yPos = 2;
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glDisable(2896);
        CalendarHandler.EnumSeason season = calendar.currentSeason();
        this.mc.getTextureManager().bindTexture(texturepath);
        this.drawTexturedModalRect(xPos, yPos, 0, 0, 96, 29);
        int healthWidth = Math.min(75, (int)(cap.getHealth(this.mc.player) / cap.getMaxHealth(this.mc.player) * 75.0f));
        int runePointsWidth = Math.min(75, (int)(cap.getRunePoints() / (float)cap.getMaxRunePoints() * 75.0f));
        this.drawTexturedModalRect(xPos + 18, yPos + 3, 18, 30, healthWidth, 9);
        this.drawTexturedModalRect(xPos + 18, yPos + 17, 18, 40, runePointsWidth, 9);
        this.drawCenteredString(this.mc.fontRenderer, TextFormatting.getValueByName(season.getColor()) + I18n.format(season.formattingText()) + " " + calendar.date() + " " + calendar.currentDay().text(), 50, 50, 0);
    }
}
