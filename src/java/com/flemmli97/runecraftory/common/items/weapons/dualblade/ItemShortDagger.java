package com.flemmli97.runecraftory.common.items.weapons.dualblade;

import com.flemmli97.runecraftory.api.entities.IRFAttributes;
import com.flemmli97.runecraftory.common.items.weapons.DualBladeBase;

import net.minecraft.item.ItemStack;

public class ItemShortDagger extends DualBladeBase{

	public ItemShortDagger() {
		super("shortDagger");
	}

	@Override
	public int getBuyPrice(ItemStack stack) {
		return 1310;
	}

	@Override
	public int getSellPrice(ItemStack stack) {
		return 45;
	}

	@Override
	protected void initStats() {
		this.stats.put(IRFAttributes.RFATTACK, 28);
		this.stats.put(IRFAttributes.RFDIZ, 3);
	}

}
