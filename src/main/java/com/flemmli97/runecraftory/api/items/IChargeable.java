package com.flemmli97.runecraftory.api.items;

import net.minecraft.item.ItemStack;

public interface IChargeable {

    int[] getChargeTime();

    EnumToolCharge chargeType(ItemStack stack);
}
