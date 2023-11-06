package io.github.flemmli97.runecraftory.common.blocks;

import io.github.flemmli97.runecraftory.common.world.farming.FarmlandHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

public class BlockTreeSapling extends BushBlock implements Growable {

    private final Supplier<BlockTreeBase> treeBase;
    protected static final VoxelShape SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 12.0, 14.0);

    public BlockTreeSapling(Properties properties, Supplier<BlockTreeBase> treeBase) {
        super(properties);
        this.treeBase = treeBase;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return FarmlandHandler.isFarmBlock(state);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (level instanceof ServerLevel serverLevel) {
            FarmlandHandler.get(serverLevel.getServer()).getData(serverLevel, pos.below())
                    .ifPresent(d -> d.onCropRemove(serverLevel, pos, newState));
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public int getGrowableMaxAge() {
        return BlockTreeBase.MAX_AGE;
    }

    @Override
    public BlockState getGrowableStateForAge(BlockState current, int age) {
        if (age == 0)
            return this.defaultBlockState();
        return this.treeBase.get().defaultBlockState().setValue(BlockTreeBase.AGE, 0);
    }

    @Override
    public boolean isAtMaxAge(BlockState state) {
        return false;
    }

    @Override
    public boolean canGrow(ServerLevel level, BlockPos pos, BlockState state) {
        return BlockPos.betweenClosedStream(pos.offset(-1, 0, -1), pos.offset(1, 2, 1))
                .allMatch(p -> p.equals(pos) || BlockTreeBase.isAirOrReplaceable(level.getBlockState(p)));
    }
}
