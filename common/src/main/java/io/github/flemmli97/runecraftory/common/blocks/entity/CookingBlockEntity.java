package io.github.flemmli97.runecraftory.common.blocks.entity;

import io.github.flemmli97.runecraftory.common.recipes.CraftingType;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class CookingBlockEntity extends CraftingBlockEntity {

    public CookingBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(RuneCraftoryBlocks.COOKING_TILE.get(), CraftingType.COOKING_TABLE, blockPos, blockState);
    }
}
