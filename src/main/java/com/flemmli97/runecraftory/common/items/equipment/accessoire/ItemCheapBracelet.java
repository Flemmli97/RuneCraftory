package com.flemmli97.runecraftory.common.items.equipment.accessoire;

import com.flemmli97.runecraftory.api.entities.ItemStats;
import com.flemmli97.runecraftory.common.items.equipment.ItemAccessoireBase;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.EnumHelper;

public class ItemCheapBracelet extends ItemAccessoireBase{

	private static ArmorMaterial mat = EnumHelper.addArmorMaterial("runeCraftory_armorMat", "", 0,new int[] {0,0,0,0}, 0, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 0);
	public ItemCheapBracelet() {
		super(mat, "cheap_bracelet");
	}

	@Override
	public int getBuyPrice(ItemStack stack) {
		return 100;
	}

	@Override
	public int getSellPrice(ItemStack stack) {
		return 35;
	}

	@Override
	public int getUpgradeDifficulty() {
		return 0;
	}

	@Override
	public NBTTagCompound defaultNBTStats(ItemStack stack) {
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagCompound stats = new NBTTagCompound();
		NBTTagList emtpyUpgrade = new NBTTagList();

		stats.setInteger(ItemStats.RFMAGICDEF.getName(), 5);
		
		nbt.setTag("ItemStats", stats);
		nbt.setInteger("ItemLevel", 1);
		nbt.setTag("Upgrades", emtpyUpgrade);
		return nbt;
	}

}
