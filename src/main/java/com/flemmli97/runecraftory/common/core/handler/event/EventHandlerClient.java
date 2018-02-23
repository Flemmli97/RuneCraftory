package com.flemmli97.runecraftory.common.core.handler.event;

import com.flemmli97.runecraftory.client.gui.GuiBars;
import com.flemmli97.runecraftory.client.gui.GuiInfoScreenSub;
import com.flemmli97.runecraftory.client.gui.GuiInfoScreen;
import com.flemmli97.runecraftory.client.gui.ButtonSkill;
import com.flemmli97.runecraftory.common.init.ModItems;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
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
		if(!(Minecraft.getMinecraft().currentScreen instanceof GuiInfoScreen) && !(Minecraft.getMinecraft().currentScreen instanceof GuiInfoScreenSub))
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
				x = (event.getGui().width-174)/2-28;
				y = (event.getGui().height-166)/2;
			}
			else
			{
				x = (event.getGui().width-192)/2-28;
				y = (event.getGui().height-136)/2;
			}
			event.getButtonList().add(new ButtonSkill(x, y));
		}
	}
	//RenderHandEvent
}
