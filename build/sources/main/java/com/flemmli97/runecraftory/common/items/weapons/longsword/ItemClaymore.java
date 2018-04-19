package com.flemmli97.runecraftory.common.items.weapons.longsword;

import com.flemmli97.runecraftory.api.entities.ItemStats;
import com.flemmli97.runecraftory.common.items.weapons.LongSwordBase;
import com.flemmli97.runecraftory.common.lib.enums.EnumElement;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class ItemClaymore extends LongSwordBase{

	public ItemClaymore() {
		super("claymore");
	}

	@Override
	public int getBuyPrice(ItemStack stack) {
		return 160;
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

		stats.setInteger(ItemStats.RFATTACK.getName(), 12);
		stats.setInteger(ItemStats.RFDIZ.getName(), 12);
		nbt.setTag("ItemStats", stats);
		nbt.setInteger("ItemLevel", 1);
		nbt.setString("Element", EnumElement.NONE.getName());
		nbt.setTag("Upgrades", emtpyUpgrade);
		return nbt;
	}

}
