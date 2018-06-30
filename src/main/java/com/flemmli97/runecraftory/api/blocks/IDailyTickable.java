package com.flemmli97.runecraftory.api.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Called each day. (Probably better than ticking tiles, or not). Unused for now
 */
public interface IDailyTickable
{
    void dailyUpdate(World world, BlockPos pos, IBlockState state);
}
