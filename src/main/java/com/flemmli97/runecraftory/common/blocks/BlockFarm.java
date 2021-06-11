package com.flemmli97.runecraftory.common.blocks;

import com.flemmli97.runecraftory.common.blocks.tile.TileFarm;
import net.minecraft.block.BlockState;
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
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {

    }

    @Override
    public void onFallenUpon(World world, BlockPos pos, Entity entity, float dmg) {
        entity.onLivingFall(dmg, 1.0F);
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
