package com.flemmli97.runecraftory.common.items.weapons.spear;

import com.flemmli97.runecraftory.api.entities.ItemStats;
import com.flemmli97.runecraftory.common.items.weapons.SpearBase;
import com.flemmli97.runecraftory.common.lib.enums.EnumElement;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class ItemSpear extends SpearBase{

	public ItemSpear() {
		super("spear");
	}

	@Override
	public int getBuyPrice(ItemStack stack) {
		return 200;
	}

	@Override
	public int getSellPrice(ItemStack stack) {
		return 23;
	}

	@Override
	public NBTTagCompound defaultNBTStats(ItemStack stack)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagCompound stats = new NBTTagCompound();
		NBTTagList emtpyUpgrade = new NBTTagList();

		stats.setInteger(ItemStats.RFATTACK.getName(), 14);
		stats.setInteger(ItemStats.RFDEFENCE.getName(), 1);
		stats.setInteger(ItemStats.RFDIZ.getName(), 8);
		nbt.setTag("ItemStats", stats);
		nbt.setInteger("ItemLevel", 1);
		nbt.setString("Element", EnumElement.NONE.getName());
		nbt.setTag("Upgrades", emtpyUpgrade);
		return nbt;
	}

}
