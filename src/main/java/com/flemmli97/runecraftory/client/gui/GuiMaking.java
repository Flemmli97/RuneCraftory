package com.flemmli97.runecraftory.client.gui;

import java.io.IOException;
import java.util.ArrayList;

import com.flemmli97.runecraftory.common.blocks.tile.TileMultiBase;
import com.flemmli97.runecraftory.common.core.handler.crafting.CraftingHandler;
import com.flemmli97.runecraftory.common.core.handler.crafting.RecipeSextuple;
import com.flemmli97.runecraftory.common.core.network.PacketCrafting;
import com.flemmli97.runecraftory.common.core.network.PacketHandler;
import com.flemmli97.runecraftory.common.inventory.ContainerMaking;
import com.flemmli97.runecraftory.common.items.misc.ItemRecipe;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.lib.enums.EnumCrafting;
import com.google.common.collect.Lists;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

public class GuiMaking  extends GuiContainer{

	private static final ResourceLocation forging = new ResourceLocation(LibReference.MODID + ":textures/gui/forgec.png");
	private static final ResourceLocation crafting = new ResourceLocation(LibReference.MODID + ":textures/gui/craftingc.png");
	private static final ResourceLocation chem = new ResourceLocation(LibReference.MODID + ":textures/gui/chemc.png");
	private static final ResourceLocation cooking = new ResourceLocation(LibReference.MODID + ":textures/gui/cookingc.png");
	private static final ResourceLocation bars = new ResourceLocation(LibReference.MODID + ":textures/gui/bars.png");

	private EnumCrafting type;
	private ButtonCraft craftButton;

	private final int texX = 176;
	private final int texY = 166;
	private int guiX;
	private int guiY;
	private TileMultiBase tileInv;
	public GuiMaking(EnumCrafting type, IInventory playerInv, TileMultiBase tileInv) {
		super(new ContainerMaking(playerInv, tileInv));
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
			case COOKING:
				this.mc.getTextureManager().bindTexture(cooking);
				this.drawTexturedModalRect(this.guiX, this.guiY, 0, 0, texX, texY);
				break;
			case FORGE:
				this.mc.getTextureManager().bindTexture(forging);
				this.drawTexturedModalRect(this.guiX, this.guiY, 0, 0, texX, texY);
				break;
			case PHARMA:
				this.mc.getTextureManager().bindTexture(chem);
				this.drawTexturedModalRect(this.guiX, this.guiY, 0, 0, texX, texY);
				break;
		}
		this.drawGhostRecipeItems();
	}
	
	private int ticker;
	private int itemIndex;
	private int recipeIndex;
	private void drawGhostRecipeItems()
	{
		ItemStack recipeItem = this.inventorySlots.getSlot(42).getStack();
		if(!recipeItem.isEmpty() && recipeItem.getItem() instanceof ItemRecipe)
		{
			ArrayList<RecipeSextuple> recipes = Lists.newArrayList(CraftingHandler.getRecipeFromID(this.type, recipeItem.getTagCompound().getString("Recipe")));
			
			NonNullList<Object> ing = recipes.get(recipeIndex).getRecipeItems();
			for(int i = 0; i < ing.size(); i++)
			{
				if(ing.get(i) instanceof ItemStack)
				{
					ItemStack stackToRender = (ItemStack) ing.get(i);
					if(!stackToRender.isEmpty() && this.inventorySlots.getSlot(i+36).getStack().isEmpty())
					{
			            this.itemRender.renderItemAndEffectIntoGUI(this.mc.player, stackToRender, this.guiX+20+(i%3)*18, this.guiY+26+i/3*18);
						this.mc.getTextureManager().bindTexture(bars);
						this.drawTexturedModalRect(this.guiX+20+(i%3)*18, this.guiY+26+i/3*18, 132, 41, 16, 16);
					}
				}
				else if(ing.get(i) instanceof String)
				{
					NonNullList<ItemStack> oredict = OreDictionary.getOres((String) ing.get(i));
					if(itemIndex>=oredict.size())
					{
						this.itemIndex=0;
						this.recipeIndex++;
						if(this.recipeIndex>=recipes.size())
							this.recipeIndex=0;
					}
					ItemStack stackToRender = oredict.get(itemIndex);
					if(!stackToRender.isEmpty() && this.inventorySlots.getSlot(i+36).getStack().isEmpty())
					{
			            this.itemRender.renderItemAndEffectIntoGUI(this.mc.player, stackToRender, this.guiX+20+(i%3)*18, this.guiY+26+i/3*18);
						this.mc.getTextureManager().bindTexture(bars);
						this.drawTexturedModalRect(this.guiX+20+(i%3)*18, this.guiY+26+i/3*18, 132, 41, 16, 16);
					}
				}
			}
			ticker++;
			if(ticker>60)
			{
				ticker=0;
				itemIndex++;
			}
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
			PacketHandler.sendToServer(new PacketCrafting(0, this.tileInv.getPos()));
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
	}
}
