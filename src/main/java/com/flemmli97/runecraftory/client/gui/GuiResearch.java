package com.flemmli97.runecraftory.client.gui;

import java.io.IOException;

import com.flemmli97.runecraftory.common.blocks.tile.TileResearchTable;
import com.flemmli97.runecraftory.common.core.network.PacketCrafting;
import com.flemmli97.runecraftory.common.core.network.PacketHandler;
import com.flemmli97.runecraftory.common.inventory.ContainerResearch;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiResearch  extends GuiContainer{

	private static final ResourceLocation research = new ResourceLocation(LibReference.MODID + ":textures/gui/research.png");

	private ButtonResearch craftButton;
	private ButtonPage pageButton1, pageButton2;

	private final int texX = 176;
	private final int texY = 166;
	private int guiX;
	private int guiY;
	private TileResearchTable tileInv;
	private int level;
	public GuiResearch(IInventory playerInv, TileResearchTable tileInv) {
		super(new ContainerResearch(playerInv, tileInv));
		this.tileInv=tileInv;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		this.mc.getTextureManager().bindTexture(research);
		this.drawTexturedModalRect(this.guiX, this.guiY, 0, 0, texX, texY);
		this.drawLeftAlignedScaledString(""+this.level, this.guiX+55, this.guiY+58, 1, 0);
	}
	
	private void drawLeftAlignedScaledString(String string, float x, float y, float scale, int color)
	{
		GlStateManager.pushMatrix();
		GlStateManager.scale(scale, scale, scale);
		float xCenter = x-(mc.fontRenderer.getStringWidth(string));
		int xScaled = (int) (xCenter/scale);
		int yScaled = (int) (y/scale);
		this.fontRenderer.drawString(string,xScaled, yScaled, color);
		GlStateManager.popMatrix();
	}
	
	@Override
	public int getGuiLeft() {
		return guiX;
	}

	@Override
	public int getGuiTop() {
		return guiY;
	}

	@Override
	public int getXSize() {
		return this.texX;
	}

	@Override
	public int getYSize() {
		return this.texY;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		this.guiX=(this.width-this.texX)/2;
		this.guiY =(this.height -this.texY)/ 2;
		this.buttonList.add(craftButton = new ButtonResearch(this.guiX +83, this.guiY +34));
		this.buttonList.add(pageButton2 = new ButtonPage(1, this.guiX +19 , this.guiY +55, "-"));
		this.buttonList.add(pageButton1 = new ButtonPage(0, this.guiX +60 , this.guiY +55, "+"));
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if(button==this.craftButton)
		{
			PacketHandler.sendToServer(new PacketCrafting(this.level, this.tileInv.getPos()));
		}
		else if(button==this.pageButton1)
		{
			this.level=Math.min(this.level+10, 100);
		}
		else if(button==this.pageButton2)
		{
			this.level=Math.max(this.level-10, 0);
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
	}
}
