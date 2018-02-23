package com.flemmli97.runecraftory.common.items.weapons.shortsword;

import com.flemmli97.runecraftory.api.entities.ItemStats;
import com.flemmli97.runecraftory.common.items.weapons.ShortSwordBase;
import com.flemmli97.runecraftory.common.lib.enums.EnumElement;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class ItemBroadSword extends ShortSwordBase{

	public ItemBroadSword() {
		super("broad_sword");
	}

	@Override
	public NBTTagCompound defaultNBTStats(ItemStack stack)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagCompound stats = new NBTTagCompound();
		NBTTagList emtpyUpgrade = new NBTTagList();

		stats.setInteger(ItemStats.RFATTACK.getName(), 5);
		stats.setInteger(ItemStats.RFDIZ.getName(), 6);
		nbt.setTag("ItemStats", stats);
		nbt.setInteger("ItemLevel", 1);
		nbt.setString("Element", EnumElement.NONE.getName());
		nbt.setTag("Upgrades", emtpyUpgrade);
		return nbt;
	}

	@Override
	public int getBuyPrice(ItemStack stack) {
		return 90;
	}

	@Override
	public int getSellPrice(ItemStack stack) {
		return 23;	
	}
}
