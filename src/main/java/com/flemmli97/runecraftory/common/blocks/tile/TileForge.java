package com.flemmli97.runecraftory.common.blocks.tile;

import com.flemmli97.runecraftory.api.enums.EnumCrafting;
import com.flemmli97.runecraftory.common.registry.ModBlocks;

public class TileForge extends TileCrafting {

    public TileForge() {
        super(ModBlocks.forgingTile.get(), EnumCrafting.FORGE);
    }
}
