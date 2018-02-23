package com.flemmli97.runecraftory.common.items.weapons.haxe;

import com.flemmli97.runecraftory.api.entities.ItemStats;
import com.flemmli97.runecraftory.common.items.weapons.AxeBase;
import com.flemmli97.runecraftory.common.lib.enums.EnumElement;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class ItemBattleScythe extends AxeBase{

	public ItemBattleScythe() {
		super("battleScythe");
	}

	@Override
	public int getBuyPrice(ItemStack stack) {
		return 6590;
	}

	@Override
	public int getSellPrice(ItemStack stack) {
		return 140;
	}

	@Override
	public NBTTagCompound defaultNBTStats(ItemStack stack)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagCompound stats = new NBTTagCompound();
		NBTTagList emtpyUpgrade = new NBTTagList();

		stats.setInteger(ItemStats.RFATTACK.getName(), 70);
		stats.setInteger(ItemStats.RFDIZ.getName(), 2);
		stats.setInteger(ItemStats.RFCRIT.getName(), 15);
		
		nbt.setTag("ItemStats", stats);
		nbt.setInteger("ItemLevel", 1);
		nbt.setString("Element", EnumElement.NONE.getName());
		nbt.setTag("Upgrades", emtpyUpgrade);
		return nbt;
	}

}
