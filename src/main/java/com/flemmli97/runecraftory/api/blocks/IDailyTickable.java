package com.flemmli97.runecraftory.api.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 
 *
 */
public interface IDailyTickable {

	//**TODO way to register it do dailyblocktickhandler
	public void dailyUpdate(World world, BlockPos pos, IBlockState state);
}
