package com.flemmli97.runecraftory.common.items.weapons.haxe;

import com.flemmli97.runecraftory.api.entities.IRFAttributes;
import com.flemmli97.runecraftory.common.items.weapons.AxeBase;

import net.minecraft.item.ItemStack;

public class ItemBattleAxe extends AxeBase{

	public ItemBattleAxe() {
		super("battleAxe");
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
	protected void initStats() {
		this.stats.put(IRFAttributes.RFATTACK, 38);
		this.stats.put(IRFAttributes.RFDIZ, 2);
		this.stats.put(IRFAttributes.RFCRIT, 5);
	}

}
