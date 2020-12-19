package com.flemmli97.runecraftory.common.blocks.tile;

import com.flemmli97.runecraftory.api.enums.EnumCrafting;
import com.flemmli97.runecraftory.common.registry.ModBlocks;

public class TileChemistry extends TileCrafting {

    public TileChemistry() {
        super(ModBlocks.chemistryTile.get(), EnumCrafting.CHEM);
    }
}
