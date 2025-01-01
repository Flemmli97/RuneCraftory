package io.github.flemmli97.runecraftory.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public interface Growable {

    int runecraftory$getGrowableMaxAge();

    BlockState runecraftory$getGrowableStateForAge(BlockState current, int age);

    boolean runecraftory$isAtMaxAge(BlockState state);

    default boolean canGrow(ServerLevel level, BlockPos pos, BlockState state) {
        return !this.runecraftory$isAtMaxAge(state);
    }

    default void onGrow(ServerLevel level, BlockPos pos, BlockState state, BlockState old) {
        level.setBlock(pos, state, Block.UPDATE_ALL);
    }
}
