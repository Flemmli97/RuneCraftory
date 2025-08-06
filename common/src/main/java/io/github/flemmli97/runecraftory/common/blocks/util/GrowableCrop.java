package io.github.flemmli97.runecraftory.common.blocks.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface GrowableCrop {

    void onWater(Level level, BlockPos pos, BlockState crop);

    void onWither(int amount, Level level, BlockState state, BlockPos pos);
}
