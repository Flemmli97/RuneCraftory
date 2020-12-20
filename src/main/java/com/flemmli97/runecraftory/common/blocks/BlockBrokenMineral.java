package com.flemmli97.runecraftory.common.blocks;

import com.flemmli97.runecraftory.api.enums.EnumMineralTier;
import com.flemmli97.runecraftory.common.blocks.tile.TileBrokenMineral;
import com.flemmli97.runecraftory.common.registry.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.network.play.server.SPlaySoundEventPacket;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.stream.Stream;

public class BlockBrokenMineral extends Block implements IWaterLoggable {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final VoxelShape south = Stream.of(
            Block.makeCuboidShape(3, 0, 11, 8, 1, 16),
            Block.makeCuboidShape(3, 0, 6, 6, 1, 8),
            Block.makeCuboidShape(0, 0, 1, 7, 1, 6),
            Block.makeCuboidShape(6, 0, 6, 10, 3, 8),
            Block.makeCuboidShape(7, 0, 2, 13, 2, 6),
            Block.makeCuboidShape(8, 0, 12, 10, 1, 13),
            Block.makeCuboidShape(9, 0, 13, 14, 1, 15),
            Block.makeCuboidShape(10, 0, 6, 15, 3, 13),
            Block.makeCuboidShape(13, 0, 0, 16, 1, 6),
            Block.makeCuboidShape(1, 0, 6, 3, 2, 15),
            Block.makeCuboidShape(3, 0, 8, 10, 2, 12),
            Block.makeCuboidShape(4, 0.3, 6, 6, 1.3, 7),
            Block.makeCuboidShape(7.25, 1.55, 8.5, 9.25, 2.55, 11.5),
            Block.makeCuboidShape(10.25, 1.55, 4.5, 12.25, 2.55, 6.5)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();
    public static final VoxelShape north = Stream.of(
            Block.makeCuboidShape(8, 0, 0, 13, 1, 5),
            Block.makeCuboidShape(10, 0, 8, 13, 1, 10),
            Block.makeCuboidShape(9, 0, 10, 16, 1, 15),
            Block.makeCuboidShape(6, 0, 8, 10, 3, 10),
            Block.makeCuboidShape(3, 0, 10, 9, 2, 14),
            Block.makeCuboidShape(6, 0, 3, 8, 1, 4),
            Block.makeCuboidShape(2, 0, 1, 7, 1, 3),
            Block.makeCuboidShape(1, 0, 3, 6, 3, 10),
            Block.makeCuboidShape(0, 0, 10, 3, 1, 16),
            Block.makeCuboidShape(13, 0, 1, 15, 2, 10),
            Block.makeCuboidShape(6, 0, 4, 13, 2, 8),
            Block.makeCuboidShape(10, 0.3, 9, 12, 1.3, 10),
            Block.makeCuboidShape(6.75, 1.55, 4.5, 8.75, 2.55, 7.5),
            Block.makeCuboidShape(3.75, 1.55, 9.5, 5.75, 2.55, 11.5)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();
    public static final VoxelShape west = Stream.of(
            Block.makeCuboidShape(0, 0, 3, 5, 1, 8),
            Block.makeCuboidShape(8, 0, 3, 10, 1, 6),
            Block.makeCuboidShape(10, 0, 0, 15, 1, 7),
            Block.makeCuboidShape(8, 0, 6, 10, 3, 10),
            Block.makeCuboidShape(10, 0, 7, 14, 2, 13),
            Block.makeCuboidShape(3, 0, 8, 4, 1, 10),
            Block.makeCuboidShape(1, 0, 9, 3, 1, 14),
            Block.makeCuboidShape(3, 0, 10, 10, 3, 15),
            Block.makeCuboidShape(10, 0, 13, 16, 1, 16),
            Block.makeCuboidShape(1, 0, 1, 10, 2, 3),
            Block.makeCuboidShape(4, 0, 3, 8, 2, 10),
            Block.makeCuboidShape(9, 0.3, 4, 10, 1.3, 6),
            Block.makeCuboidShape(4.5, 1.55, 7.25, 7.5, 2.55, 9.25),
            Block.makeCuboidShape(9.5, 1.55, 10.25, 11.5, 2.55, 12.25)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();
    public static final VoxelShape east = Stream.of(
            Block.makeCuboidShape(11, 0, 8, 16, 1, 13),
            Block.makeCuboidShape(6, 0, 10, 8, 1, 13),
            Block.makeCuboidShape(1, 0, 9, 6, 1, 16),
            Block.makeCuboidShape(6, 0, 6, 8, 3, 10),
            Block.makeCuboidShape(2, 0, 3, 6, 2, 9),
            Block.makeCuboidShape(12, 0, 6, 13, 1, 8),
            Block.makeCuboidShape(13, 0, 2, 15, 1, 7),
            Block.makeCuboidShape(6, 0, 1, 13, 3, 6),
            Block.makeCuboidShape(0, 0, 0, 6, 1, 3),
            Block.makeCuboidShape(6, 0, 13, 15, 2, 15),
            Block.makeCuboidShape(8, 0, 6, 12, 2, 13),
            Block.makeCuboidShape(6, 0.3, 10, 7, 1.3, 12),
            Block.makeCuboidShape(8.5, 1.55, 6.75, 11.5, 2.55, 8.75),
            Block.makeCuboidShape(4.5, 1.55, 3.75, 6.5, 2.55, 5.75)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();
    public final EnumMineralTier tier;

    public BlockBrokenMineral(EnumMineralTier tier, Properties properties) {
        super(properties);
        this.tier = tier;
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH).with(WATERLOGGED, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        switch (state.get(FACING)) {
            case WEST:
                return west;
            case EAST:
                return east;
            case SOUTH:
                return south;
            default:
                return north;
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext ctx) {
        FluidState fluidstate = ctx.getWorld().getFluidState(ctx.getPos());
        return this.getDefaultState().with(FACING, ctx.getPlayer() != null ? ctx.getPlayer().getHorizontalFacing().getOpposite() : Direction.NORTH).with(WATERLOGGED, fluidstate.getFluid() == Fluids.WATER);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.with(FACING, mirror.mirror(state.get(FACING)));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    @Override
    public BlockState updatePostPlacement(BlockState state, Direction direction, BlockState state1, IWorld world, BlockPos pos, BlockPos pos1) {
        if (state.get(WATERLOGGED)) {
            world.getPendingFluidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return super.updatePostPlacement(state, direction, state1, world, pos, pos1);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest, FluidState fluid) {
        if (world.isRemote)
            return false;
        if (player.isCreative() || player.isSneaking()) {
            return super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
        }
        return false;
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBlockHarvested(world, pos, state, player);
        if (player instanceof ServerPlayerEntity)
            ((ServerPlayerEntity) player).connection.sendPacket(new SPlaySoundEventPacket(2001, pos, getStateId(state), false));
    }

    public BlockState getMineralState(BlockState state) {
        BlockState blockState = ModBlocks.mineralMap.get(this.tier).get().getDefaultState();
        if (state.contains(FACING))
            state.with(FACING, state.get(FACING));
        if (state.contains(WATERLOGGED))
            state.with(WATERLOGGED, state.get(WATERLOGGED));
        return blockState;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileBrokenMineral();
    }
}

