package com.flemmli97.runecraftory.common.items.weapons.haxe;

import com.flemmli97.runecraftory.api.entities.IRFAttributes;
import com.flemmli97.runecraftory.common.items.weapons.AxeBase;

import net.minecraft.item.ItemStack;

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
	protected void initStats() {
		this.stats.put(IRFAttributes.RFATTACK, 70);
		this.stats.put(IRFAttributes.RFDIZ, 2);
		this.stats.put(IRFAttributes.RFCRIT, 15);
	}

}
