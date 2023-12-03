package io.github.flemmli97.runecraftory.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

public class BlockGiantCrop extends BlockCrop {

    public static final IntegerProperty AGE = BlockStateProperties.AGE_1;
    public static final EnumProperty<Direction> DIRECTION = BlockStateProperties.HORIZONTAL_FACING;
    private static final List<Direction> DIRECTIONS = Direction.Plane.HORIZONTAL.stream().sorted(Comparator.comparingInt(Direction::get2DDataValue)).toList();

    private static final VoxelShape[] SHAPE = BlockCrafting.joinedOrDirs(BlockCrafting.ShapeBuilder.of(0.0D, 0.0D, 0.0D, 13.0D, 12.0D, 13.0D));

    public BlockGiantCrop(Properties prop, Supplier<Item> giant, Supplier<Item> seed) {
        super(prop, giant, seed);
        this.registerDefaultState(this.defaultBlockState().setValue(DIRECTION, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE[state.getValue(BlockGiantCrop.DIRECTION).get2DDataValue()];
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return 0;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        Direction dir = state.getValue(DIRECTION);
        if (direction == dir && !(neighborState.is(this) && neighborState.getValue(DIRECTION).getClockWise() == dir))
            return Blocks.AIR.defaultBlockState();
        return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && player.isCreative()) {
            BlockPos.MutableBlockPos mut = pos.mutable();
            BlockState blockAt;
            Direction dir = state.getValue(DIRECTION);
            for (int i = 0; i < 3; i++) {
                blockAt = level.getBlockState(mut.move(dir));
                if (blockAt.is(this) && blockAt.getValue(AGE) == 1) {
                    Direction blockDir = blockAt.getValue(DIRECTION);
                    if (blockDir == DIRECTIONS.get((i + state.getValue(DIRECTION).get2DDataValue()) % 4)) {
                        level.setBlock(mut, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
                        level.levelEvent(player, LevelEvent.PARTICLES_DESTROY_BLOCK, mut, Block.getId(blockAt));
                    }
                }
            }
        }
        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE).add(BlockCrop.WILTED).add(DIRECTION);
    }

    @Override
    public int getGiantAge() {
        return 0;
    }

    @Override
    public void onWither(int amount, Level level, BlockState state, BlockPos pos) {
        super.onWither(amount, level, state, pos);
        BlockPos.MutableBlockPos mut = pos.mutable();
        BlockState blockAt;
        Direction dir = state.getValue(DIRECTION);
        for (int i = 0; i < 3; i++) {
            blockAt = level.getBlockState(mut.move(dir));
            if (blockAt.is(this) && blockAt.getValue(AGE) == 1) {
                Direction blockDir = blockAt.getValue(DIRECTION);
                if (blockDir == DIRECTIONS.get((i + state.getValue(DIRECTION).get2DDataValue()) % 4)) {
                    dir = blockDir;
                    super.onWither(amount, level, blockAt, mut);
                }
            }
        }
    }

    @Override
    public void onWiltedWatering(Level level, BlockPos pos, BlockState state) {
        super.onWiltedWatering(level, pos, state);
        BlockPos.MutableBlockPos mut = pos.mutable();
        BlockState blockAt;
        Direction dir = state.getValue(DIRECTION);
        for (int i = 0; i < 3; i++) {
            blockAt = level.getBlockState(mut.move(dir));
            if (blockAt.is(this) && blockAt.getValue(AGE) == 1) {
                Direction blockDir = blockAt.getValue(DIRECTION);
                if (blockDir == DIRECTIONS.get((i + state.getValue(DIRECTION).get2DDataValue()) % 4)) {
                    dir = blockDir;
                    level.setBlock(mut, blockAt.setValue(BlockCrop.WILTED, false), Block.UPDATE_ALL);
                }
            }
        }
    }
}