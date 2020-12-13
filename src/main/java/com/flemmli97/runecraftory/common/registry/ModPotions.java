package com.flemmli97.runecraftory.common.registry;

import com.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.potion.Effect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModPotions {

    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, RuneCraftory.MODID);

    public static Effect sleep;
    public static Effect poison;
    public static Effect paralysis;
    public static Effect seal;
    public static Effect fatigue;
    public static Effect cold;

}
