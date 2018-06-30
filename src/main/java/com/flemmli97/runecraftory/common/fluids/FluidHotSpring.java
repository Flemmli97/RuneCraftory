package com.flemmli97.runecraftory.common.fluids;

import java.awt.Color;

import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.item.EnumRarity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class FluidHotSpring extends Fluid
{
    public FluidHotSpring() {
        super("hot_spring", new ResourceLocation(LibReference.MODID, "hot_spring_still"), new ResourceLocation(LibReference.MODID, "hot_spring_flowing"));
        this.rarity = EnumRarity.UNCOMMON;
        this.temperature = 310;
        this.setColor(Color.cyan);
        FluidRegistry.registerFluid(this);
        FluidRegistry.addBucketForFluid(this);
    }
}
