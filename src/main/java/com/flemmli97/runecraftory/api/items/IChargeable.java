package com.flemmli97.runecraftory.api.items;

import com.flemmli97.runecraftory.client.render.EnumToolCharge;

import net.minecraft.item.ItemStack;

public interface IChargeable
{
    public int[] getChargeTime();
    
    public  EnumToolCharge chargeType(ItemStack stack);
}
