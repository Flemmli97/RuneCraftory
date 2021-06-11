package com.flemmli97.runecraftory.common.world.features;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class MineralFeatures extends Feature<ChancedBlockCluster> {

    public MineralFeatures(Codec<ChancedBlockCluster> codec) {
        super(codec);
    }

    @Override
    public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos center, ChancedBlockCluster conf) {
        if (conf.amount.getMax() == 0)
            return false;
        BlockState state = conf.stateProvider.getBlockState(rand, center);
        int tries = conf.amount.generateInt(rand);
        for (int i = 0; i < tries; i++) {
            BlockPos pos = center.add(rand.nextInt(conf.spreadX) - rand.nextInt(conf.spreadX),
                    conf.spreadY, rand.nextInt(conf.spreadZ) - rand.nextInt(conf.spreadZ));
            int y = conf.spreadY;
            while (y > -conf.spreadY && !state.isValidPosition(reader, pos)) {
                pos = pos.down();
                y--;
            }
            if (reader.getBlockState(pos).getMaterial().isReplaceable() && state.isValidPosition(reader, pos)) {
                if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING))
                    state = state.with(BlockStateProperties.HORIZONTAL_FACING, Direction.Plane.HORIZONTAL.random(rand));
                if (state.hasProperty(BlockStateProperties.WATERLOGGED))
                    state = state.with(BlockStateProperties.WATERLOGGED, reader.getFluidState(pos).getFluid() == Fluids.WATER);
                reader.setBlockState(pos, state, 3);
            }
        }
        return true;
    }
}