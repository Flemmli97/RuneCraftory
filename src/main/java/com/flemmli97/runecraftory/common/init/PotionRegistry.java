package com.flemmli97.runecraftory.common.init;

import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.potion.PotionSleep;

import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = LibReference.MODID)
public class PotionRegistry {
	public static Potion sleep = new PotionSleep();
	
	@SubscribeEvent
	public static final void init(RegistryEvent.Register<Potion> event)
	{
		event.getRegistry().register(sleep);
	}

}
