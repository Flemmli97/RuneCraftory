package com.flemmli97.runecraftory.common.blocks.tile;

import com.flemmli97.runecraftory.common.lib.enums.EnumCrafting;

import net.minecraft.item.ItemStack;

public class TileChem extends TileMultiBase{

	public TileChem() {
		super(EnumCrafting.PHARMA);
	}

	@Override
	public String getName() {
		return "tile.chemistry";
	}

	@Override
	protected boolean validItem(ItemStack stack) {
		return false;
	}
}
