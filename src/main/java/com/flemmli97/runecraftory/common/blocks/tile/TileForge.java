package com.flemmli97.runecraftory.common.blocks.tile;

import com.flemmli97.runecraftory.common.lib.enums.EnumCrafting;

public class TileForge extends TileMultiBase{

	public TileForge() {
		super(EnumCrafting.FORGE);
	}

	@Override
	public String getName() {
		return "tile.forge";
	}
}
