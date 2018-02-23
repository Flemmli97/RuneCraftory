package com.flemmli97.runecraftory.common.core.handler.event;

import com.flemmli97.runecraftory.api.items.IRpUseItem;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandlerSounds {

	@SubscribeEvent
	public void chargeSound(LivingEntityUseItemEvent.Tick event)
	{
		if(event.getEntityLiving() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			ItemStack held = event.getItem();
			if(held!=null && held.getItem() instanceof IRpUseItem)
			{
				IRpUseItem item = (IRpUseItem) held.getItem();
				if(item.getChargeTime()[0]>0)
				{
					int duration = held.getMaxItemUseDuration() - event.getDuration();
					if(duration==item.getChargeTime()[0])
					{
						player.playSound(SoundEvents.BLOCK_NOTE_XYLOPHONE, 1, 1);
					}
					else if(item.getChargeTime()[1] >= 2 && duration==item.getChargeTime()[0]*2)
					{
						player.playSound(SoundEvents.BLOCK_NOTE_XYLOPHONE, 1, 1);
					}
					else if(item.getChargeTime()[1] >= 3 && duration==item.getChargeTime()[0]*3)
					{
						player.playSound(SoundEvents.BLOCK_NOTE_XYLOPHONE, 1, 1);
					}
					else if(item.getChargeTime()[1] == 4 && duration==item.getChargeTime()[0]*4)
					{
						player.playSound(SoundEvents.BLOCK_NOTE_XYLOPHONE, 1, 1);
					}
				}
			}
		}
	}
}
