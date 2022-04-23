package io.github.flemmli97.runecraftory.common.blocks;

import io.github.flemmli97.runecraftory.api.enums.EnumCrafting;
import io.github.flemmli97.runecraftory.common.blocks.tile.ForgingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class BlockForge extends BlockCrafting {

    public BlockForge(BlockBehaviour.Properties props) {
        super(EnumCrafting.FORGE, props);
    }

    @Override
    public BlockEntity createNewBlockEntity(BlockPos pos, BlockState state) {
        return new ForgingBlockEntity(pos, state);
    }
}
