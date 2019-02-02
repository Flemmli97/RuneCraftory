package com.flemmli97.runecraftory.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import com.flemmli97.runecraftory.common.blocks.tile.TileMultiBase;
import com.flemmli97.runecraftory.common.core.handler.crafting.CraftingHandler;
import com.flemmli97.runecraftory.common.core.handler.crafting.RecipeSextuple;
import com.flemmli97.runecraftory.common.inventory.ContainerMaking;
import com.flemmli97.runecraftory.common.items.misc.ItemRecipe;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.lib.enums.EnumCrafting;
import com.flemmli97.runecraftory.common.network.PacketCrafting;
import com.flemmli97.runecraftory.common.network.PacketHandler;
import com.google.common.collect.Lists;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

@SideOnly(Side.CLIENT)
public class GuiMaking extends GuiContainer
{
    private static final ResourceLocation forging = new ResourceLocation(LibReference.MODID,"textures/gui/forgec.png");
    private static final ResourceLocation crafting = new ResourceLocation(LibReference.MODID,"textures/gui/craftingc.png");
    private static final ResourceLocation chem = new ResourceLocation(LibReference.MODID,"textures/gui/chemc.png");
    private static final ResourceLocation cooking = new ResourceLocation(LibReference.MODID,"textures/gui/cookingc.png");
    private static final ResourceLocation bars = new ResourceLocation(LibReference.MODID,"textures/gui/bars.png");

    private EnumCrafting type;
    private ButtonCraft craftButton;
    private final int texX = 176;
    private final int texY = 166;
    private int guiX;
    private int guiY;
    private TileMultiBase tileInv;
    private Random rand = new Random();
    
    private ArrayList<ArrayList<ItemStack>> renderRecipes = Lists.newArrayList();
    private int ticker;
    private int[] dmg = new int[6];
    private ItemStack pre = ItemStack.EMPTY;
    
    public GuiMaking(EnumCrafting type, IInventory playerInv, TileMultiBase tileInv) {
        super(new ContainerMaking(playerInv, tileInv));
        this.tileInv = tileInv;
        this.type=tileInv.type();
        for(int i = 0; i < 6; i ++)
        	this.renderRecipes.add(new ArrayList<ItemStack>());
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        switch (this.type) 
        {
            case ARMOR:
                this.mc.getTextureManager().bindTexture(crafting);
                this.drawTexturedModalRect(this.guiX, this.guiY, 0, 0, 176, 166);
                break;
            case COOKING:
                this.mc.getTextureManager().bindTexture(cooking);
                this.drawTexturedModalRect(this.guiX, this.guiY, 0, 0, 176, 166);
                break;
            case FORGE:
                this.mc.getTextureManager().bindTexture(forging);
                this.drawTexturedModalRect(this.guiX, this.guiY, 0, 0, 176, 166);
                break;
            case PHARMA:
                this.mc.getTextureManager().bindTexture(chem);
                this.drawTexturedModalRect(this.guiX, this.guiY, 0, 0, 176, 166);
                break;
        }
        this.drawGhostRecipeItems();
    }
    
    private void drawGhostRecipeItems() 
    {
        ItemStack recipeItem = this.inventorySlots.getSlot(42).getStack();
        if (!recipeItem.isEmpty() && recipeItem.getItem() instanceof ItemRecipe) 
        {
            if (!this.isItemStackEqualNBT(this.pre, recipeItem)) 
            {
                this.updateRenderList(recipeItem);
            }
            for (int i = 0; i < this.renderRecipes.size(); ++i) 
            {
                ArrayList<ItemStack> stackList = this.renderRecipes.get(i);
                if (this.inventorySlots.getSlot(i + 36).getStack().isEmpty() && stackList != null && !stackList.isEmpty()) 
                {
                    if (this.ticker == 0) 
                    {
                        this.dmg[i] = this.rand.nextInt(stackList.size());
                    }
                    ItemStack stackToRender = stackList.get(this.dmg[i]);
                    if (stackToRender != null && !stackToRender.isEmpty()) 
                    {
                        this.itemRender.renderItemAndEffectIntoGUI((EntityLivingBase)this.mc.player, stackToRender, this.guiX + 20 + i % 3 * 18, this.guiY + 26 + i / 3 * 18);
                        this.mc.getTextureManager().bindTexture(bars);
                        this.drawTexturedModalRect(this.guiX + 20 + i % 3 * 18, this.guiY + 26 + i / 3 * 18, 132, 41, 16, 16);
                    }
                }
            }
            ++this.ticker;
            if (this.ticker > 60) {
                this.ticker = 0;
            }
        }
    }
    
    private void updateRenderList(ItemStack recipeItem) {
    	for(ArrayList<ItemStack> slotItems : this.renderRecipes)
    		slotItems.clear();
        this.pre = recipeItem.copy();
        this.ticker = 0;
        this.dmg = new int[6];
        for (RecipeSextuple r : CraftingHandler.getRecipeFromID(this.type, recipeItem.getTagCompound().getString("Recipe"))) 
        {
            NonNullList<Object> ing = r.getRecipeItems();
            for (int j = 0; j < ing.size(); ++j) 
            {
                Object o = ing.get(j);
                if (o instanceof ItemStack) 
                {
                    ItemStack stack = ((ItemStack)o).copy();
                    if (stack.getItemDamage() == OreDictionary.WILDCARD_VALUE) 
                    {
                        NonNullList<ItemStack> subs = NonNullList.create();
                        stack.getItem().getSubItems(stack.getItem().getCreativeTab(), subs);
                        this.renderRecipes.get(j).addAll(subs);
                    }
                    else 
                    {
                        this.renderRecipes.get(j).add(stack);
                    }
                }
                else if (o instanceof String) 
                {
                    NonNullList<ItemStack> oredict = OreDictionary.getOres((String)ing.get(j));
                    this.renderRecipes.get(j).addAll(oredict);
                }
            }
        }
    }
    
    private boolean isItemStackEqualNBT(ItemStack stack1, ItemStack stack2) 
    {
        if (OreDictionary.itemMatches(stack1, stack2, true)) 
        {
            if (!stack1.hasTagCompound() && !stack2.hasTagCompound()) {
                return true;
            }
            if (stack1.hasTagCompound() && !stack2.hasTagCompound()) {
                return false;
            }
            if (!stack1.hasTagCompound() && stack2.hasTagCompound()) {
                return false;
            }
            if (stack1.getTagCompound().equals(stack2.getTagCompound())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getGuiLeft() {
        return this.guiX;
    }

    @Override
    public int getGuiTop() {
        return this.guiY;
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
    public void initGui() 
    {
        super.initGui();
        this.guiX = (this.width - 176) / 2;
        this.guiY = (this.height - 166) / 2;
        this.buttonList.add(this.craftButton = new ButtonCraft(this.guiX + 112, this.guiY + 31));
    }
    
    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button == this.craftButton) 
        {
            PacketHandler.sendToServer((IMessage)new PacketCrafting(0, this.tileInv.getPos()));
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) 
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
}
