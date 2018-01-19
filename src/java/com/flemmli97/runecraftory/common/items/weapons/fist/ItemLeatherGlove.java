package com.flemmli97.runecraftory.common.items.weapons.fist;

import com.flemmli97.runecraftory.api.entities.IRFAttributes;
import com.flemmli97.runecraftory.common.items.weapons.GloveBase;

import net.minecraft.item.ItemStack;

public class ItemLeatherGlove extends GloveBase{

	public ItemLeatherGlove() {
		super("leatherGlove");
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
	protected void initStats() {
		this.stats.put(IRFAttributes.RFATTACK, 24);
		this.stats.put(IRFAttributes.RFDEFENCE, 3);
		this.stats.put(IRFAttributes.RFDIZ, 3);
	}

}
