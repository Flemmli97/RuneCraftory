package com.flemmli97.runecraftory.api.items;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * 
 *	Subclasses for differentiation
 */
public interface IItemBase {

	public int getBuyPrice(ItemStack stack);
	
	public int getSellPrice(ItemStack stack);
	
	public int getUpgradeDifficulty();
	
	public NBTTagCompound defaultNBTStats(ItemStack stack);
}
