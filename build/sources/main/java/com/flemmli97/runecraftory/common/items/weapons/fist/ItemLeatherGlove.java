package com.flemmli97.runecraftory.common.items.weapons.fist;

import com.flemmli97.runecraftory.api.entities.ItemStats;
import com.flemmli97.runecraftory.common.items.weapons.GloveBase;
import com.flemmli97.runecraftory.common.lib.enums.EnumElement;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class ItemLeatherGlove extends GloveBase{

	public ItemLeatherGlove() {
		super("leather_glove");
	}

	@Override
	public int getBuyPrice(ItemStack stack) {
		return 290;
	}

	@Override
	public int getSellPrice(ItemStack stack) {
		return 13;
	}

	@Override
	public NBTTagCompound defaultNBTStats(ItemStack stack)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagCompound stats = new NBTTagCompound();
		NBTTagList emtpyUpgrade = new NBTTagList();

		stats.setInteger(ItemStats.RFATTACK.getName(), 24);
		stats.setInteger(ItemStats.RFDEFENCE.getName(), 3);
		stats.setInteger(ItemStats.RFDIZ.getName(), 3);
		
		nbt.setTag("ItemStats", stats);
		nbt.setInteger("ItemLevel", 1);
		nbt.setString("Element", EnumElement.NONE.getName());
		nbt.setTag("Upgrades", emtpyUpgrade);
		return nbt;
	}

}
