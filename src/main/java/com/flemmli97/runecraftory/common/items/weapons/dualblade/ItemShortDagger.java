package com.flemmli97.runecraftory.common.items.weapons.dualblade;

import com.flemmli97.runecraftory.api.entities.ItemStats;
import com.flemmli97.runecraftory.common.items.weapons.DualBladeBase;
import com.flemmli97.runecraftory.common.lib.enums.EnumElement;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class ItemShortDagger extends DualBladeBase{

	public ItemShortDagger() {
		super("shortDagger");
	}

	@Override
	public int getBuyPrice(ItemStack stack) {
		return 1310;
	}

	@Override
	public int getSellPrice(ItemStack stack) {
		return 45;
	}

	@Override
	public NBTTagCompound defaultNBTStats(ItemStack stack)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagCompound stats = new NBTTagCompound();
		NBTTagList emtpyUpgrade = new NBTTagList();
		stats.setInteger(ItemStats.RFATTACK.getName(), 28);
		stats.setInteger(ItemStats.RFDIZ.getName(), 3);
		nbt.setTag("ItemStats", stats);
		nbt.setInteger("ItemLevel", 1);
		nbt.setString("Element", EnumElement.NONE.getName());
		nbt.setTag("Upgrades", emtpyUpgrade);
		return nbt;
	}

}
