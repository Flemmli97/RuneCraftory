package com.flemmli97.runecraftory.api.items;

import net.minecraft.item.ItemStack;

public interface IItemBase {

	public int getBuyPrice(ItemStack stack);
	
	public int getSellPrice(ItemStack stack);
	
	public int getUpgradeDifficulty(ItemStack stack);
	
	public int itemLevel();
	
	public void addItemLevel();
	
	//nbt handler?
}
