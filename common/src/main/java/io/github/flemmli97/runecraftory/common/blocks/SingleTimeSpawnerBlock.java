package io.github.flemmli97.runecraftory.common.blocks;

import com.mojang.serialization.MapCodec;
import io.github.flemmli97.runecraftory.common.blocks.entity.SingleTimeSpawner;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class SingleTimeSpawnerBlock extends BaseEntityBlock {

    public static final MapCodec<SingleTimeSpawnerBlock> CODEC = simpleCodec(SingleTimeSpawnerBlock::new);

    public SingleTimeSpawnerBlock(BlockBehaviour.Properties props) {
        super(props);
    }

    @Override
    public MapCodec<SingleTimeSpawnerBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SingleTimeSpawner(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : BaseEntityBlock.createTickerHelper(blockEntityType, RuneCraftoryBlocks.SINGLE_SPAWNER_TILE.get(), SingleTimeSpawner::tick);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}