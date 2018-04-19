package com.flemmli97.runecraftory.client.gui;

import com.flemmli97.runecraftory.common.core.network.PacketHandler;
import com.flemmli97.runecraftory.common.core.network.PacketOpenGuiContainer;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class ButtonSkill extends GuiButton{

	private static final ResourceLocation texturepath = new ResourceLocation(LibReference.MODID + ":textures/gui/bars.png");

	public ButtonSkill(int x, int y) {
		super(0, x, y, 24, 24, "");
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		if (this.visible)
        {
            mc.getTextureManager().bindTexture(texturepath);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            this.drawTexturedModalRect(this.x, this.y, 98+(this.hovered?25:0), 14, this.width , this.height);
            this.mouseDragged(mc, mouseX, mouseY);
        }
	}

	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		if(super.mousePressed(mc, mouseX, mouseY))
		{
			PacketHandler.sendToServer(new PacketOpenGuiContainer(LibReference.guiInfo1));
			return true;
		}
		return false;
	}	
}
