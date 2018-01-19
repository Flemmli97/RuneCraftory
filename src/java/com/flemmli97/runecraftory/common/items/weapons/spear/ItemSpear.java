package com.flemmli97.runecraftory.common.items.weapons.spear;

import com.flemmli97.runecraftory.api.entities.IRFAttributes;
import com.flemmli97.runecraftory.common.items.weapons.SpearBase;

import net.minecraft.item.ItemStack;

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
	protected void initStats() {
		this.stats.put(IRFAttributes.RFATTACK, 14);
		this.stats.put(IRFAttributes.RFDEFENCE, 1);
		this.stats.put(IRFAttributes.RFDIZ, 8);
	}

}
