package com.flemmli97.runecraftory.common.items.weapons.shortsword;

import com.flemmli97.runecraftory.api.entities.IRFAttributes;
import com.flemmli97.runecraftory.common.items.weapons.ShortSwordBase;

import net.minecraft.item.ItemStack;

public class ItemBroadSword extends ShortSwordBase{

	public ItemBroadSword() {
		super("broad_sword");
	}

	@Override
	protected void initStats()
	{
		this.stats.put(IRFAttributes.RFATTACK, 5);
		this.stats.put(IRFAttributes.RFDIZ, 6);
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
