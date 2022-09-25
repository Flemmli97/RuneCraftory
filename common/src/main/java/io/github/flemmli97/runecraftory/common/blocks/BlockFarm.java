package io.github.flemmli97.runecraftory.common.blocks;

import io.github.flemmli97.runecraftory.api.enums.EnumWeather;
import io.github.flemmli97.runecraftory.common.blocks.tile.FarmBlockEntity;
import io.github.flemmli97.runecraftory.common.world.WorldHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

public class BlockFarm extends FarmBlock implements EntityBlock, BonemealableBlock {

    public BlockFarm(Properties properties) {
        super(properties);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random rand) {
        if (level.isRainingAt(pos.above())) {
            level.setBlock(pos, state.setValue(FarmBlock.MOISTURE, 7), 2);
        }
        if (level.random.nextBoolean() && WorldHandler.get(level.getServer()).currentWeather() == EnumWeather.STORM) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof FarmBlockEntity farm) {
                farm.onStorming();
            }
        }
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float f) {
        entity.causeFallDamage(f, 1.0f, DamageSource.FALL);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FarmBlockEntity(pos, state);
    }

    @Override
    public boolean isValidBonemealTarget(BlockGetter level, BlockPos pos, BlockState state, boolean isClient) {
        return level.getBlockEntity(pos) instanceof FarmBlockEntity farm && farm.canUseBonemeal();
    }

    @Override
    public boolean isBonemealSuccess(Level level, Random rand, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, Random rand, BlockPos pos, BlockState state) {
        if (level.getBlockEntity(pos) instanceof FarmBlockEntity farm)
            farm.applyBonemeal();
    }
}
