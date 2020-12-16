package com.flemmli97.runecraftory.common.blocks;

import com.flemmli97.runecraftory.common.blocks.tile.TileFarm;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class BlockFarm extends FarmlandBlock {

    public BlockFarm(Properties properties) {
        super(properties);
    }

    @Override
    public void randomTick(BlockState p_225542_1_, ServerWorld p_225542_2_, BlockPos p_225542_3_, Random p_225542_4_) {

    }

    @Override
    public void onFallenUpon(World p_180658_1_, BlockPos p_180658_2_, Entity p_180658_3_, float p_180658_4_) {
        if (!p_180658_1_.isRemote && net.minecraftforge.common.ForgeHooks.onFarmlandTrample(p_180658_1_, p_180658_2_, Blocks.DIRT.getDefaultState(), p_180658_4_, p_180658_3_)) { // Forge: Move logic to Entity#canTrample
            turnToDirt(p_180658_1_.getBlockState(p_180658_2_), p_180658_1_, p_180658_2_);
        }

        super.onFallenUpon(p_180658_1_, p_180658_2_, p_180658_3_, p_180658_4_);
    }

    @Override
    public void fillWithRain(World world, BlockPos pos) {
        world.setBlockState(pos, this.getDefaultState().with(FarmlandBlock.MOISTURE, 7));
        super.fillWithRain(world, pos);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileFarm();
    }
}
