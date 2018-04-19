package com.flemmli97.runecraftory.common.core.handler.event;

import com.flemmli97.runecraftory.client.gui.ButtonSkill;
import com.flemmli97.runecraftory.client.gui.GuiBars;
import com.flemmli97.runecraftory.client.gui.GuiInfoScreen;
import com.flemmli97.runecraftory.client.gui.GuiInfoScreenSub;
import com.flemmli97.runecraftory.client.gui.GuiSpellHotbar;
import com.flemmli97.runecraftory.common.core.network.PacketCastSpell;
import com.flemmli97.runecraftory.common.core.network.PacketHandler;
import com.flemmli97.runecraftory.common.items.weapons.DualBladeBase;
import com.flemmli97.runecraftory.common.items.weapons.GloveBase;
import com.flemmli97.runecraftory.proxy.ClientProxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
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
		if (event.isCancelable())return;
		if(event.getType()==ElementType.EXPERIENCE && !(Minecraft.getMinecraft().currentScreen instanceof GuiInfoScreen) && !(Minecraft.getMinecraft().currentScreen instanceof GuiInfoScreenSub))
			new GuiBars(Minecraft.getMinecraft());
		if(event.getType()==ElementType.HOTBAR)
			new GuiSpellHotbar(Minecraft.getMinecraft());
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
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void keyEvent(KeyInputEvent event)
	{
		if(ClientProxy.skill1.isPressed())
		{
			PacketHandler.sendToServer(new PacketCastSpell(0));
        }
		else if(ClientProxy.skill2.isPressed())
		{
			PacketHandler.sendToServer(new PacketCastSpell(1));
        }
		else if(ClientProxy.skill3.isPressed())
		{
			PacketHandler.sendToServer(new PacketCastSpell(2));
        }
		else if(ClientProxy.skill4.isPressed())
		{
			PacketHandler.sendToServer(new PacketCastSpell(3));
        }
    }

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void keyEvent(RenderSpecificHandEvent event)
	{
		ItemStack stackMain = Minecraft.getMinecraft().player.getHeldItem(EnumHand.MAIN_HAND);
		if(stackMain.getItem() instanceof DualBladeBase || stackMain.getItem() instanceof GloveBase)
		{
			if(event.getHand()==EnumHand.OFF_HAND)
			{
				event.setCanceled(true);
				Minecraft.getMinecraft().getItemRenderer().renderItemInFirstPerson(Minecraft.getMinecraft().player, event.getPartialTicks(), event.getInterpolatedPitch(), EnumHand.OFF_HAND, event.getSwingProgress(), stackMain, event.getEquipProgress());
			}
		}
	}
}
