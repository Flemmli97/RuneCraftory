package com.flemmli97.runecraftory.client.gui;

import org.lwjgl.opengl.GL11;

import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;
import com.flemmli97.runecraftory.common.core.handler.time.CalendarHandler;
import com.flemmli97.runecraftory.common.core.handler.time.CalendarHandler.EnumSeason;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiBars extends Gui {
	private Minecraft mc;
	public static int width;

	private static final ResourceLocation texturepath = new ResourceLocation(LibReference.MODID + ":textures/gui/bars.png");

	public GuiBars(Minecraft mc)
	{
		this.mc = mc;
		this.renderBar();
	}
	private void renderBar() {
		//if (this.mc.playerController.shouldDrawHUD())
        {
			IPlayer cap = this.mc.player.getCapability(PlayerCapProvider.PlayerCap, null);
			CalendarHandler calendar = CalendarHandler.get(mc.player.world);
			if (cap == null)
			{
				return;
			}
			int xPos = 2;
			int yPos = 2;
	
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	
			GL11.glDisable(GL11.GL_LIGHTING);
			EnumSeason season = calendar.currentSeason();
			
			this.mc.getTextureManager().bindTexture(texturepath);
			this.drawTexturedModalRect(xPos, yPos, 0, 0, 96, 29);
			int healthWidth = (int) ((cap.getHealth()+mc.player.getHealth())/(cap.getMaxHealth()+mc.player.getMaxHealth()) *75);
			int runePointsWidth = (int)((cap.getRunePoints() / (float)cap.getMaxRunePoints()) * 75);
			this.drawTexturedModalRect(xPos+18, yPos+3, 18, 30, healthWidth, 9);
			this.drawTexturedModalRect(xPos+18, yPos+17, 18, 40, runePointsWidth, 9);
			this.drawCenteredString(mc.fontRenderer, TextFormatting.getValueByName(season.getColor())+season.formattingText()+ " " + calendar.date()+" " + calendar.currentDay().text(), 50, 50, 0);
	        }
	}
}
