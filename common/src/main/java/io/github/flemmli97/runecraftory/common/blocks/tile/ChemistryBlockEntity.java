package io.github.flemmli97.runecraftory.common.blocks.tile;

import io.github.flemmli97.runecraftory.api.enums.EnumCrafting;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class ChemistryBlockEntity extends CraftingBlockEntity {

    public ChemistryBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlocks.chemistryTile.get(), EnumCrafting.CHEM, blockPos, blockState);
    }
}
