package com.flemmli97.runecraftory.common.blocks.tile;

import com.flemmli97.runecraftory.common.lib.enums.EnumCrafting;

public class TileChem extends TileMultiBase{

	public TileChem() {
		super(EnumCrafting.PHARMA);
	}

	@Override
	public String getName() {
		return "tile.chemistry";
	}
}
