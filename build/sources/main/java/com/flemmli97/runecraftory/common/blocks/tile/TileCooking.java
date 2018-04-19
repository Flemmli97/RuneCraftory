package com.flemmli97.runecraftory.common.blocks.tile;

import com.flemmli97.runecraftory.common.lib.enums.EnumCrafting;

public class TileCooking extends TileMultiBase{

	public TileCooking() {
		super(EnumCrafting.COOKING);
	}

	@Override
	public String getName() {
		return "tile.cooking";
	}
}
