package com.flemmli97.runecraftory.client.gui;

import com.flemmli97.runecraftory.common.lib.RFReference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

public class SkillButton extends GuiButton{

	private static final ResourceLocation texturepath = new ResourceLocation(RFReference.MODID + ":textures/gui/bars.png");

	public SkillButton(int x, int y) {
		super(0, x, y, 28, 24, "");
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		super.drawButton(mc, mouseX, mouseY, partialTicks);
	}

	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		if(super.mousePressed(mc, mouseX, mouseY))
		{
			Minecraft.getMinecraft().displayGuiScreen(new GuiInformationScreen(Minecraft.getMinecraft()));
			return true;
		}
		return false;
	}	
}
