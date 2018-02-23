package com.flemmli97.runecraftory.common.blocks.tile;

import com.flemmli97.runecraftory.common.lib.enums.EnumCrafting;

public class TileAccessory extends TileMultiBase{

	public TileAccessory() {
		super(EnumCrafting.ARMOR);
	}

	@Override
	public String getName() {
		return "tile.accessory";
	}
}
