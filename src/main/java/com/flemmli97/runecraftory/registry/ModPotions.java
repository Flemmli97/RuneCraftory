package com.flemmli97.runecraftory.registry;

import net.minecraft.potion.Effect;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ModPotions {

    public static Effect sleep;
    public static Effect poison;
    public static Effect paralysis;
    public static Effect seal;
    public static Effect fatigue;
    public static Effect cold;

    @SubscribeEvent
    public static final void init(RegistryEvent.Register<Effect> event) {

    }
}
