package io.github.flemmli97.runecraftory.common.blocks;

import io.github.flemmli97.runecraftory.api.enums.EnumCrafting;
import io.github.flemmli97.runecraftory.common.blocks.tile.CookingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class BlockCooking extends BlockCrafting {

    public BlockCooking(BlockBehaviour.Properties props) {
        super(EnumCrafting.COOKING, props);
    }

    @Override
    public BlockEntity createNewBlockEntity(BlockPos pos, BlockState state) {
        return new CookingBlockEntity(pos, state);
    }
}
