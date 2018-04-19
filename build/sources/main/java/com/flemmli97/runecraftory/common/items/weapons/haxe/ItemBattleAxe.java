package com.flemmli97.runecraftory.common.items.weapons.haxe;

import com.flemmli97.runecraftory.api.entities.ItemStats;
import com.flemmli97.runecraftory.common.items.weapons.AxeBase;
import com.flemmli97.runecraftory.common.lib.enums.EnumElement;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class ItemBattleAxe extends AxeBase{

	public ItemBattleAxe() {
		super("battle_axe");
	}

	@Override
	public int getBuyPrice(ItemStack stack) {
		return 1380;
	}

	@Override
	public int getSellPrice(ItemStack stack) {
		return 42;
	}

	@Override
	public NBTTagCompound defaultNBTStats(ItemStack stack)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagCompound stats = new NBTTagCompound();
		NBTTagList emtpyUpgrade = new NBTTagList();

		stats.setInteger(ItemStats.RFATTACK.getName(), 38);
		stats.setInteger(ItemStats.RFDIZ.getName(), 2);
		stats.setInteger(ItemStats.RFCRIT.getName(), 5);
		
		nbt.setTag("ItemStats", stats);
		nbt.setInteger("ItemLevel", 1);
		nbt.setString("Element", EnumElement.NONE.getName());
		nbt.setTag("Upgrades", emtpyUpgrade);
		return nbt;
	}

}
