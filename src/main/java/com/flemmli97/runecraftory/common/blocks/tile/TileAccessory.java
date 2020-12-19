package com.flemmli97.runecraftory.common.blocks.tile;

import com.flemmli97.runecraftory.api.enums.EnumCrafting;
import com.flemmli97.runecraftory.common.registry.ModBlocks;

public class TileAccessory extends TileCrafting {

    public TileAccessory() {
        super(ModBlocks.accessoryTile.get(), EnumCrafting.ARMOR);
    }
}
