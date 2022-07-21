package io.github.flemmli97.runecraftory.common.blocks;

import io.github.flemmli97.runecraftory.api.enums.EnumMineralTier;
import io.github.flemmli97.runecraftory.common.blocks.tile.BrokenMineralBlockEntity;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.platform.ExtendedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.stream.Stream;

public class BlockBrokenMineral extends Block implements SimpleWaterloggedBlock, EntityBlock, ExtendedBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final VoxelShape north = Stream.of(
            Block.box(8, 0, 0, 13, 1, 5),
            Block.box(10, 0, 8, 13, 1, 10),
            Block.box(9, 0, 10, 16, 1, 15),
            Block.box(6, 0, 8, 10, 3, 10),
            Block.box(3, 0, 10, 9, 2, 14),
            Block.box(6, 0, 3, 8, 1, 4),
            Block.box(2, 0, 1, 7, 1, 3),
            Block.box(1, 0, 3, 6, 3, 10),
            Block.box(0, 0, 10, 3, 1, 16),
            Block.box(13, 0, 1, 15, 2, 10),
            Block.box(6, 0, 4, 13, 2, 8),
            Block.box(10, 0.3, 9, 12, 1.3, 10),
            Block.box(6.75, 1.55, 4.5, 8.75, 2.55, 7.5),
            Block.box(3.75, 1.55, 9.5, 5.75, 2.55, 11.5)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    public static final VoxelShape west = Stream.of(
            Block.box(0, 0, 3, 5, 1, 8),
            Block.box(8, 0, 3, 10, 1, 6),
            Block.box(10, 0, 0, 15, 1, 7),
            Block.box(8, 0, 6, 10, 3, 10),
            Block.box(10, 0, 7, 14, 2, 13),
            Block.box(3, 0, 8, 4, 1, 10),
            Block.box(1, 0, 9, 3, 1, 14),
            Block.box(3, 0, 10, 10, 3, 15),
            Block.box(10, 0, 13, 16, 1, 16),
            Block.box(1, 0, 1, 10, 2, 3),
            Block.box(4, 0, 3, 8, 2, 10),
            Block.box(9, 0.3, 4, 10, 1.3, 6),
            Block.box(4.5, 1.55, 7.25, 7.5, 2.55, 9.25),
            Block.box(9.5, 1.55, 10.25, 11.5, 2.55, 12.25)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    public static final VoxelShape south = Stream.of(
            Block.box(3, 0, 11, 8, 1, 16),
            Block.box(3, 0, 6, 6, 1, 8),
            Block.box(0, 0, 1, 7, 1, 6),
            Block.box(6, 0, 6, 10, 3, 8),
            Block.box(7, 0, 2, 13, 2, 6),
            Block.box(8, 0, 12, 10, 1, 13),
            Block.box(9, 0, 13, 14, 1, 15),
            Block.box(10, 0, 6, 15, 3, 13),
            Block.box(13, 0, 0, 16, 1, 6),
            Block.box(1, 0, 6, 3, 2, 15),
            Block.box(3, 0, 8, 10, 2, 12),
            Block.box(4, 0.3, 6, 6, 1.3, 7),
            Block.box(7.25, 1.55, 8.5, 9.25, 2.55, 11.5),
            Block.box(10.25, 1.55, 4.5, 12.25, 2.55, 6.5)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    public static final VoxelShape east = Stream.of(
            Block.box(11, 0, 8, 16, 1, 13),
            Block.box(6, 0, 10, 8, 1, 13),
            Block.box(1, 0, 9, 6, 1, 16),
            Block.box(6, 0, 6, 8, 3, 10),
            Block.box(2, 0, 3, 6, 2, 9),
            Block.box(12, 0, 6, 13, 1, 8),
            Block.box(13, 0, 2, 15, 1, 7),
            Block.box(6, 0, 1, 13, 3, 6),
            Block.box(0, 0, 0, 6, 1, 3),
            Block.box(6, 0, 13, 15, 2, 15),
            Block.box(8, 0, 6, 12, 2, 13),
            Block.box(6, 0.3, 10, 7, 1.3, 12),
            Block.box(8.5, 1.55, 6.75, 11.5, 2.55, 8.75),
            Block.box(4.5, 1.55, 3.75, 6.5, 2.55, 5.75)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public final EnumMineralTier tier;

    public BlockBrokenMineral(EnumMineralTier tier, BlockBehaviour.Properties properties) {
        super(properties);
        this.tier = tier;
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        FluidState fluidstate = ctx.getLevel().getFluidState(ctx.getClickedPos());
        return this.defaultBlockState().setValue(FACING, ctx.getPlayer() != null ? ctx.getPlayer().getDirection().getOpposite() : Direction.NORTH).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        super.playerWillDestroy(level, pos, state, player);
        if (!level.isClientSide)
            level.levelEvent(null, 2001, pos, Block.getId(state));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (!this.canSurvive(state, level, pos))
            return Blocks.AIR.defaultBlockState();
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, state.getFluidState().getType(), Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.setValue(FACING, mirror.mirror(state.getValue(FACING)));
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).isFaceSturdy(level, pos, Direction.UP, SupportType.FULL);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case WEST -> west;
            case EAST -> east;
            case SOUTH -> south;
            default -> north;
        };
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if (level.isClientSide)
            return false;
        if (player.isCreative() || player.isShiftKeyDown()) {
            this.playerWillDestroy(level, pos, state, player);
            return level.setBlock(pos, fluid.createLegacyBlock(), Block.UPDATE_ALL);
        }
        return false;
    }

    public BlockState getMineralState(BlockState state) {
        BlockState blockState = ModBlocks.mineralMap.get(this.tier).get().defaultBlockState();
        if (state.hasProperty(FACING))
            state.setValue(FACING, state.getValue(FACING));
        if (state.hasProperty(WATERLOGGED))
            state.setValue(WATERLOGGED, state.getValue(WATERLOGGED));
        return blockState;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BrokenMineralBlockEntity(pos, state);
    }
}

