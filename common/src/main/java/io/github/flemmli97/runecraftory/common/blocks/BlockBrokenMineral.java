package io.github.flemmli97.runecraftory.common.blocks;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.enums.EnumMineralTier;
import io.github.flemmli97.runecraftory.common.blocks.entity.BrokenMineralBlockEntity;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.platform.ExtendedBlock;
import io.github.flemmli97.tenshilib.common.utils.CodecUtils;
import io.github.flemmli97.tenshilib.common.utils.VoxelUtils;
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
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockBrokenMineral extends Block implements SimpleWaterloggedBlock, EntityBlock, ExtendedBlock {

    public static final MapCodec<BlockBrokenMineral> CODEC = RecordCodecBuilder.mapCodec(inst ->
            inst.group(CodecUtils.stringEnumCodec(EnumMineralTier.class, null).fieldOf("mineral_tier").forGetter(d -> d.tier),
                    propertiesCodec()
            ).apply(inst, BlockBrokenMineral::new));

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final VoxelShape[] SHAPES = VoxelUtils.joinedOrDirs(
            VoxelUtils.ShapeBuilder.of(8, 0, 0, 13, 1, 5),
            VoxelUtils.ShapeBuilder.of(10, 0, 8, 13, 1, 10),
            VoxelUtils.ShapeBuilder.of(9, 0, 10, 16, 1, 15),
            VoxelUtils.ShapeBuilder.of(6, 0, 8, 10, 3, 10),
            VoxelUtils.ShapeBuilder.of(3, 0, 10, 9, 2, 14),
            VoxelUtils.ShapeBuilder.of(6, 0, 3, 8, 1, 4),
            VoxelUtils.ShapeBuilder.of(2, 0, 1, 7, 1, 3),
            VoxelUtils.ShapeBuilder.of(1, 0, 3, 6, 3, 10),
            VoxelUtils.ShapeBuilder.of(0, 0, 10, 3, 1, 16),
            VoxelUtils.ShapeBuilder.of(13, 0, 1, 15, 2, 10),
            VoxelUtils.ShapeBuilder.of(6, 0, 4, 13, 2, 8),
            VoxelUtils.ShapeBuilder.of(10, 0.3, 9, 12, 1.3, 10),
            VoxelUtils.ShapeBuilder.of(6.75, 1.55, 4.5, 8.75, 2.55, 7.5),
            VoxelUtils.ShapeBuilder.of(3.75, 1.55, 9.5, 5.75, 2.55, 11.5));

    public final EnumMineralTier tier;

    public BlockBrokenMineral(EnumMineralTier tier, Properties properties) {
        super(properties);
        this.tier = tier;
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
    }

    @Override
    public MapCodec<BlockBrokenMineral> codec() {
        return CODEC;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        FluidState fluidstate = ctx.getLevel().getFluidState(ctx.getClickedPos());
        return this.defaultBlockState().setValue(FACING, ctx.getPlayer() != null ? ctx.getPlayer().getDirection().getOpposite() : Direction.NORTH).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide)
            level.levelEvent(null, LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(state));
        return super.playerWillDestroy(level, pos, state, player);
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
        return SHAPES[state.getValue(BlockCrafting.FACING).get2DDataValue()];
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
        BlockState blockState = ModBlocks.MINERAL_MAP.get(this.tier).get().defaultBlockState();
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

