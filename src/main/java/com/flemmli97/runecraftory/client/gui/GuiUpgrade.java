package com.flemmli97.runecraftory.client.gui;

import java.io.IOException;

import com.flemmli97.runecraftory.common.blocks.tile.TileMultiBase;
import com.flemmli97.runecraftory.common.core.network.PacketCrafting;
import com.flemmli97.runecraftory.common.core.network.PacketHandler;
import com.flemmli97.runecraftory.common.inventory.ContainerUpgrade;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.lib.enums.EnumCrafting;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiUpgrade  extends GuiContainer{

	private static final ResourceLocation forging = new ResourceLocation(LibReference.MODID + ":textures/gui/forgef.png");
	private static final ResourceLocation crafting = new ResourceLocation(LibReference.MODID + ":textures/gui/craftingf.png");

	private EnumCrafting type;
	private ButtonCraft craftButton;

	private final int texX = 176;
	private final int texY = 166;
	private int guiX;
	private int guiY;
	private TileMultiBase tileInv;
	public GuiUpgrade(EnumCrafting type, IInventory playerInv, TileMultiBase tileInv) {
		super(new ContainerUpgrade(playerInv, tileInv));
		this.type=type;
		this.tileInv=tileInv;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		switch(this.type)
		{
			case ARMOR:
				this.mc.getTextureManager().bindTexture(crafting);
				this.drawTexturedModalRect(this.guiX, this.guiY, 0, 0, texX, texY);
				break;
			case FORGE:
				this.mc.getTextureManager().bindTexture(forging);
				this.drawTexturedModalRect(this.guiX, this.guiY, 0, 0, texX, texY);
				break;
			default:
				break;
		}
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
		this.buttonList.add(craftButton = new ButtonCraft(this.guiX +112, this.guiY +31));
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if(button==this.craftButton)
		{
			PacketHandler.sendToServer(new PacketCrafting(1, this.tileInv.getPos()));
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
	}
}
