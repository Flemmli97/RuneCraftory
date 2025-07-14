package io.github.flemmli97.runecraftory.common.blocks.entity;

import io.github.flemmli97.runecraftory.common.recipes.CraftingType;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class ChemistryBlockEntity extends CraftingBlockEntity {

    public ChemistryBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(RuneCraftoryBlocks.CHEMISTRY_TILE.get(), CraftingType.CHEMISTRY_SET, blockPos, blockState);
    }
}
