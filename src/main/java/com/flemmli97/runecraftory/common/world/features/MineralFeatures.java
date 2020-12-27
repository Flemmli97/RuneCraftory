package com.flemmli97.runecraftory.common.world.features;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class MineralFeatures extends Feature<WeightedClusterProvider> {

    public MineralFeatures(Codec<WeightedClusterProvider> codec) {
        super(codec);
    }

    @Override
    public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos center, WeightedClusterProvider conf) {
        ChancedBlockCluster cb = conf.get(rand);
        BlockState state = cb.getCluster().stateProvider.getBlockState(rand, center);
        if(cb.getChance() == 0)
            return false;
        if(rand.nextInt(cb.getChance()) != 0 || state.getBlock() == Blocks.AIR)
            return false;
        BlockClusterFeatureConfig cluster = cb.getCluster();
        int tries = rand.nextInt(cluster.tries)+1;
        for(int i = 0; i < tries; i++){
            BlockPos pos = center.add(rand.nextInt(cluster.spreadX)-rand.nextInt(cluster.spreadX),
                    cluster.spreadY, rand.nextInt(cluster.spreadZ)-rand.nextInt(cluster.spreadZ));
            int y = cluster.spreadY*2;
            while(y > 0 && !state.isValidPosition(reader, pos)){
                pos = pos.down();
                y--;
            }
            if(reader.isAirBlock(pos) && state.isValidPosition(reader, pos)) {
                if(state.contains(BlockStateProperties.HORIZONTAL_FACING))
                    state = state.with(BlockStateProperties.HORIZONTAL_FACING, Direction.Plane.HORIZONTAL.random(rand));
                if(state.contains(BlockStateProperties.WATERLOGGED))
                    state = state.with(BlockStateProperties.WATERLOGGED, reader.getFluidState(pos).getFluid() == Fluids.WATER);
                cluster.blockPlacer.generate(reader, pos, state, rand);
            }
        }
        return true;
    }
}