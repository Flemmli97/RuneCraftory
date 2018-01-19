package com.flemmli97.runecraftory.common.items.weapons.longsword;

import com.flemmli97.runecraftory.api.entities.IRFAttributes;
import com.flemmli97.runecraftory.common.items.weapons.LongSwordBase;

import net.minecraft.item.ItemStack;

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
	protected void initStats() {
		this.stats.put(IRFAttributes.RFATTACK, 12);
		this.stats.put(IRFAttributes.RFDIZ, 12);
	}

}
