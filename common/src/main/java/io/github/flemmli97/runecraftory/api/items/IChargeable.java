package io.github.flemmli97.runecraftory.api.items;

import io.github.flemmli97.runecraftory.api.enums.EnumToolCharge;
import net.minecraft.world.item.ItemStack;

public interface IChargeable {

    int getChargeTime(ItemStack stack);

    int chargeAmount(ItemStack stack);

    EnumToolCharge chargeType(ItemStack stack);
}
