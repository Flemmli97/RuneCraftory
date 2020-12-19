package com.flemmli97.runecraftory.common.blocks.tile;

import com.flemmli97.runecraftory.api.enums.EnumCrafting;
import com.flemmli97.runecraftory.common.registry.ModBlocks;

public class TileCooking extends TileCrafting{

    public TileCooking() {
        super(ModBlocks.cookingTile.get(), EnumCrafting.COOKING);
    }
}
