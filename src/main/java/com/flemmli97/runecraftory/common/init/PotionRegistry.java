package com.flemmli97.runecraftory.common.init;

import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.potion.PotionCold;
import com.flemmli97.runecraftory.common.potion.PotionFatigue;
import com.flemmli97.runecraftory.common.potion.PotionParalysis;
import com.flemmli97.runecraftory.common.potion.PotionPoison;
import com.flemmli97.runecraftory.common.potion.PotionSeal;
import com.flemmli97.runecraftory.common.potion.PotionSleep;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = LibReference.MODID)
public class PotionRegistry
{
    public static final Potion sleep = new PotionSleep();
    public static final Potion poison = new PotionPoison();
    public static final Potion paralysis = new PotionParalysis();
    public static final Potion seal = new PotionSeal();
    public static final Potion fatigue = new PotionFatigue();
    public static final Potion cold = new PotionCold();
    
    @SubscribeEvent
    public static final void init(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(PotionRegistry.sleep);
        event.getRegistry().register(PotionRegistry.poison);
        event.getRegistry().register(PotionRegistry.paralysis);
        event.getRegistry().register(PotionRegistry.seal);
        event.getRegistry().register(PotionRegistry.fatigue);
        event.getRegistry().register(PotionRegistry.cold);
    }
    
    /**
     * Registers a potion bottle with the effect
     */
    @SubscribeEvent
    public static final void types(RegistryEvent.Register<PotionType> event) {
        event.getRegistry().register(new PotionType(new PotionEffect[] { new PotionEffect(PotionRegistry.sleep, 300) }).setRegistryName(new ResourceLocation("runecraftory:sleep")));
    }
}
