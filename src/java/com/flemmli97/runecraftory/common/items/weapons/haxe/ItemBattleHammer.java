package com.flemmli97.runecraftory.common.items.weapons.haxe;

import com.flemmli97.runecraftory.api.entities.IRFAttributes;
import com.flemmli97.runecraftory.common.items.weapons.HammerBase;

import net.minecraft.item.ItemStack;

public class ItemBattleHammer extends HammerBase{

	public ItemBattleHammer() {
		super("battleHammer");
	}

	@Override
	public int getBuyPrice(ItemStack stack) {
		return 340;
	}

	@Override
	public int getSellPrice(ItemStack stack) {
		return 42;
	}

	@Override
	protected void initStats() {
		this.stats.put(IRFAttributes.RFATTACK, 29);	
		this.stats.put(IRFAttributes.RFDIZ, 35);
		this.stats.put(IRFAttributes.RFSTUN, 10);
	}

	
}
