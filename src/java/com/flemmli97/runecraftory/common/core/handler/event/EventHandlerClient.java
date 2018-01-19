package com.flemmli97.runecraftory.common.core.handler.event;

import com.flemmli97.runecraftory.client.gui.GuiBars;
import com.flemmli97.runecraftory.client.gui.SkillButton;
import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;
import com.flemmli97.runecraftory.common.init.ModItems;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.model.ModelBiped.ArmPose;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 
 * health, rune point bar etc.
 *
 */
public class EventHandlerClient{
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void renderRunePoints(RenderGameOverlayEvent.Post event)
	{				
		if (event.isCancelable() || event.getType() != ElementType.EXPERIENCE)return;
		//if(!Minecraft.getMinecraft().player.capabilities.isCreativeMode)
			new GuiBars(Minecraft.getMinecraft());
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void initSkillTab(InitGuiEvent.Post event)
	{
		if(event.getGui() instanceof GuiInventory || event.getGui() instanceof GuiContainerCreative)
		{
			int x = 0;
			int y = 0;
			if(event.getGui() instanceof GuiInventory)
			{
				IPlayer capSync = Minecraft.getMinecraft().player.getCapability(PlayerCapProvider.PlayerCap, null);

				GuiInventory inv = (GuiInventory) event.getGui();
				//inv.inventorySlots.inventorySlots.add(new Slot(capSync.getInv(), 0, 0, 0));
				x = (event.getGui().width-176)/2-28;
				y = (event.getGui().height-166)/2;
			}
			else
			{
				x = (event.getGui().width-194)/2-28;
				y = (event.getGui().height-136)/2;
			}
			event.getButtonList().add(new SkillButton(x, y));
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void addAnimation(RenderPlayerEvent.Pre event)
	{
		if(event.getEntityPlayer().getHeldItemMainhand().getItem()==ModItems.battleAxe)
		{
		}
		
	}
}
