package io.github.flemmli97.runecraftory.common.blocks;

import io.github.flemmli97.runecraftory.common.blocks.tile.FarmBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

public class BlockFarm extends FarmBlock implements EntityBlock {

    public BlockFarm(Properties properties) {
        super(properties);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random rand) {
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float f) {
        entity.causeFallDamage(f, 1.0f, DamageSource.FALL);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FarmBlockEntity(pos, state);
    }
}
