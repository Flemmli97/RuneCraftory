package io.github.flemmli97.runecraftory.common.blocks;

import io.github.flemmli97.runecraftory.api.enums.EnumMineralTier;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolHammer;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.platform.ExtendedBlock;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;
import java.util.stream.Stream;

public class BlockMineral extends Block implements SimpleWaterloggedBlock, ExtendedBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final VoxelShape north = Stream.of(
            Block.box(8, 0, 0, 13, 6, 5),
            Block.box(10, 0, 8, 13, 11, 10),
            Block.box(9, 0, 10, 16, 6, 15),
            Block.box(6, 0, 8, 10, 8, 10),
            Block.box(3, 0, 10, 9, 5, 14),
            Block.box(6, 0, 3, 8, 4, 4),
            Block.box(2, 0, 1, 7, 2, 3),
            Block.box(1, 0, 3, 6, 11, 10),
            Block.box(0, 0, 10, 3, 2, 16),
            Block.box(13, 0, 1, 15, 5, 10),
            Block.box(6, 0, 4, 13, 15, 8),
            Block.box(4, 5, 10, 7, 6, 11),
            Block.box(11, 5.5, 10.3, 12, 6.5, 12.3),
            Block.box(12.8, 4.8, 5.3, 14.8, 5.8, 9.3),
            Block.box(1.4, 1.3, 9.6, 4.4, 2.3, 11.6),
            Block.box(7.1, 4.5, 12.8, 10.1, 5.5, 13.8),
            Block.box(3.6, 10.4, 7, 5.6, 11.4, 9),
            Block.box(3.6, 6.4, 2.5, 5.6, 7.4, 3.5),
            Block.box(6.6, 2.4, 2.5, 8.6, 3.4, 3.5),
            Block.box(10.6, 11.4, 7.5, 11.6, 12.4, 8.5),
            Block.box(8.6, 8.4, 3.5, 11.6, 10.4, 4.5),
            Block.box(0.6, 5, 4.2, 1.6, 7, 8.2)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    public static final VoxelShape west = Stream.of(
            Block.box(0, 0, 3, 5, 6, 8),
            Block.box(8, 0, 3, 10, 11, 6),
            Block.box(10, 0, 0, 15, 6, 7),
            Block.box(8, 0, 6, 10, 8, 10),
            Block.box(10, 0, 7, 14, 5, 13),
            Block.box(3, 0, 8, 4, 4, 10),
            Block.box(1, 0, 9, 3, 2, 14),
            Block.box(3, 0, 10, 10, 11, 15),
            Block.box(10, 0, 13, 16, 2, 16),
            Block.box(1, 0, 1, 10, 5, 3),
            Block.box(4, 0, 3, 8, 15, 10),
            Block.box(10, 5, 9, 11, 6, 12),
            Block.box(10.3, 5.5, 4, 12.3, 6.5, 5),
            Block.box(5.3, 4.8, 1.2, 9.3, 5.8, 3.2),
            Block.box(9.6, 1.3, 11.6, 11.6, 2.3, 14.6),
            Block.box(12.8, 4.5, 5.9, 13.8, 5.5, 8.9),
            Block.box(7, 10.4, 10.4, 9, 11.4, 12.4),
            Block.box(2.5, 6.4, 10.4, 3.5, 7.4, 12.4),
            Block.box(2.5, 2.4, 7.4, 3.5, 3.4, 9.4),
            Block.box(7.5, 11.4, 4.4, 8.5, 12.4, 5.4),
            Block.box(3.5, 8.4, 4.4, 4.5, 10.4, 7.4),
            Block.box(4.2, 5, 14.4, 8.2, 7, 15.4)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    public static final VoxelShape south = Stream.of(
            Block.box(3, 0, 11, 8, 6, 16),
            Block.box(3, 0, 6, 6, 11, 8),
            Block.box(0, 0, 1, 7, 6, 6),
            Block.box(6, 0, 6, 10, 8, 8),
            Block.box(7, 0, 2, 13, 5, 6),
            Block.box(8, 0, 12, 10, 4, 13),
            Block.box(9, 0, 13, 14, 2, 15),
            Block.box(10, 0, 6, 15, 11, 13),
            Block.box(13, 0, 0, 16, 2, 6),
            Block.box(1, 0, 6, 3, 5, 15),
            Block.box(3, 0, 8, 10, 15, 12),
            Block.box(9, 5, 5, 12, 6, 6),
            Block.box(4, 5.5, 3.7, 5, 6.5, 5.7),
            Block.box(1.2, 4.8, 6.7, 3.2, 5.8, 10.7),
            Block.box(11.6, 1.3, 4.4, 14.6, 2.3, 6.4),
            Block.box(5.9, 4.5, 2.2, 8.9, 5.5, 3.2),
            Block.box(10.4, 10.4, 7, 12.4, 11.4, 9),
            Block.box(10.4, 6.4, 12.5, 12.4, 7.4, 13.5),
            Block.box(7.4, 2.4, 12.5, 9.4, 3.4, 13.5),
            Block.box(4.4, 11.4, 7.5, 5.4, 12.4, 8.5),
            Block.box(4.4, 8.4, 11.5, 7.4, 10.4, 12.5),
            Block.box(14.4, 5, 7.8, 15.4, 7, 11.8)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    public static final VoxelShape east = Stream.of(
            Block.box(11, 0, 8, 16, 6, 13),
            Block.box(6, 0, 10, 8, 11, 13),
            Block.box(1, 0, 9, 6, 6, 16),
            Block.box(6, 0, 6, 8, 8, 10),
            Block.box(2, 0, 3, 6, 5, 9),
            Block.box(12, 0, 6, 13, 4, 8),
            Block.box(13, 0, 2, 15, 2, 7),
            Block.box(6, 0, 1, 13, 11, 6),
            Block.box(0, 0, 0, 6, 2, 3),
            Block.box(6, 0, 13, 15, 5, 15),
            Block.box(8, 0, 6, 12, 15, 13),
            Block.box(5, 5, 4, 6, 6, 7),
            Block.box(3.7, 5.5, 11, 5.7, 6.5, 12),
            Block.box(6.7, 4.8, 12.8, 10.7, 5.8, 14.8),
            Block.box(4.4, 1.3, 1.4, 6.4, 2.3, 4.4),
            Block.box(2.23, 4.5, 7.1, 3.23, 5.5, 10.1),
            Block.box(7, 10.4, 3.6, 9, 11.4, 5.6),
            Block.box(12.5, 6.4, 3.6, 13.5, 7.4, 5.6),
            Block.box(12.5, 2.4, 6.6, 13.5, 3.4, 8.6),
            Block.box(7.5, 11.4, 10.6, 8.5, 12.4, 11.6),
            Block.box(11.5, 8.4, 8.6, 12.5, 10.4, 11.6),
            Block.box(7.8, 5, 0.6, 11.8, 7, 1.6)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public final EnumMineralTier tier;

    public BlockMineral(EnumMineralTier tier, Properties properties) {
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
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        if (!this.canSurvive(state, level, currentPos))
            return Blocks.AIR.defaultBlockState();
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
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
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        Entity entity = builder.getOptionalParameter(LootContextParams.THIS_ENTITY);
        if (entity instanceof Player player) {
            Platform.INSTANCE.getPlayerData(player).ifPresent(data -> {
                float addChance = data.getSkillLevel(EnumSkills.MINING).getLevel() * 0.03f;
                if (player.getMainHandItem().getItem() instanceof ItemToolHammer item) {
                    addChance += item.tier.getTierLevel() * 0.75;
                }
                builder.withLuck(addChance + EntityUtils.playerLuck(player));
            });
        }
        return super.getDrops(state, builder);
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
        if (!(player instanceof ServerPlayer serverPlayer))
            return false;
        if (player.isCreative()) {
            this.playerWillDestroy(level, pos, state, player);
            return level.setBlock(pos, fluid.createLegacyBlock(), Block.UPDATE_ALL);
        } else if (player.hasCorrectToolForDrops(state)) {
            pos = pos.immutable();
            float breakChance = 0.45F;
            if (player.getMainHandItem().getItem() instanceof ItemToolHammer hammer) {
                breakChance -= hammer.tier.getTierLevel() * 0.075F;
            }
            this.playerWillDestroy(level, pos, state, player);
            if (level.random.nextFloat() < breakChance) {
                return level.setBlock(pos, this.getBrokenState(state), Block.UPDATE_ALL);
            } else {
                dropResources(state, level, pos, null, player, player.getMainHandItem());
                serverPlayer.connection.send(new ClientboundBlockUpdatePacket(pos, state));
                return false;
            }
        }
        return false;
    }

    public BlockState getBrokenState(BlockState state) {
        BlockState blockState = ModBlocks.brokenMineralMap.get(this.tier).get().defaultBlockState();
        if (blockState.hasProperty(FACING))
            blockState.setValue(FACING, state.getValue(FACING));
        if (blockState.hasProperty(WATERLOGGED))
            blockState.setValue(WATERLOGGED, state.getValue(WATERLOGGED));
        return blockState;
    }
}
