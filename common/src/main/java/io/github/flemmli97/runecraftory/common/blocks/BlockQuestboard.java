package io.github.flemmli97.runecraftory.common.blocks;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.common.integration.simplequest.SimpleQuestIntegration;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class BlockQuestboard extends HorizontalDirectionalBlock {

    public static final VoxelShape[] BOTTOM_LEFT = BlockCrafting.joinedOrDirs(BlockCrafting.ShapeBuilder.of(0, 0, 7, 2, 10, 9),
            BlockCrafting.ShapeBuilder.of(1, 11, 6.5, 16, 16, 9.5),
            BlockCrafting.ShapeBuilder.of(0, 10, 6, 1, 16, 10),
            BlockCrafting.ShapeBuilder.of(1, 10, 6, 16, 11, 10));
    public static final VoxelShape[] BOTTOM_RIGHT = BlockCrafting.joinedOrDirs(BlockCrafting.ShapeBuilder.of(14, 0, 7, 16, 10, 9),
            BlockCrafting.ShapeBuilder.of(0, 11, 6.5, 15, 16, 9.5),
            BlockCrafting.ShapeBuilder.of(15, 10, 6, 16, 16, 10),
            BlockCrafting.ShapeBuilder.of(0, 10, 6, 15, 11, 10));
    public static final VoxelShape[] TOP_LEFT = BlockCrafting.joinedOrDirs(BlockCrafting.ShapeBuilder.of(1, 0, 6.5, 16, 15, 9.5),
            BlockCrafting.ShapeBuilder.of(0, 0, 6, 1, 16, 10),
            BlockCrafting.ShapeBuilder.of(1, 15, 6, 16, 16, 10));
    public static final VoxelShape[] TOP_RIGHT = BlockCrafting.joinedOrDirs(BlockCrafting.ShapeBuilder.of(0, 0, 6.5, 15, 15, 9.5),
            BlockCrafting.ShapeBuilder.of(15, 0, 6, 16, 16, 10),
            BlockCrafting.ShapeBuilder.of(0, 15, 6, 15, 16, 10));

    public static final EnumProperty<Part> PART = EnumProperty.create("part", Part.class);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public BlockQuestboard(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(PART)) {
            case BOTTOM_LEFT -> BOTTOM_LEFT[state.getValue(BlockCrafting.FACING).get2DDataValue()];
            case BOTTOM_RIGHT -> BOTTOM_RIGHT[state.getValue(BlockCrafting.FACING).get2DDataValue()];
            case TOP_LEFT -> TOP_LEFT[state.getValue(BlockCrafting.FACING).get2DDataValue()];
            case TOP_RIGHT -> TOP_RIGHT[state.getValue(BlockCrafting.FACING).get2DDataValue()];
        };
    }

    @Override
    public BlockState updateShape(BlockState state, Direction fromDir, BlockState fromState, LevelAccessor level, BlockPos pos, BlockPos fromPos) {
        Direction dir = state.getValue(FACING);
        Part part = state.getValue(PART);
        Direction normalizedFrom = rotateBy(fromDir, dir);
        if (part.neighbors.contains(normalizedFrom) && (!fromState.is(this)
                || fromState.getValue(FACING) != dir
                || fromState.getValue(PART) != Part.getPartFor(part, normalizedFrom)))
            return Blocks.AIR.defaultBlockState();
        return super.updateShape(state, fromDir, fromState, level, pos, fromPos);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResult.CONSUME;
        } else {
            SimpleQuestIntegration.INST().openGui(serverPlayer);
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockPos blockpos = ctx.getClickedPos();
        if (ctx.getLevel().getBlockState(blockpos).canBeReplaced(ctx)) {
            BlockState state = this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
            if (getPosMap(blockpos, state).stream().allMatch(p -> ctx.getLevel().getBlockState(p.getSecond()).canBeReplaced(ctx)))
                return state;
        }
        return null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity living, ItemStack stack) {
        super.setPlacedBy(level, pos, state, living, stack);
        if (!level.isClientSide)
            getPosMap(pos, state).forEach(p -> level.setBlock(p.getSecond(), state.setValue(PART, p.getFirst()), Block.UPDATE_ALL));
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && player.isCreative()) {
            Direction facing = state.getValue(FACING);
            getPosMap(pos, state).forEach(p -> {
                BlockState other = level.getBlockState(p.getSecond());
                if (other.is(this) && other.getValue(FACING) == facing && other.getValue(PART) == p.getFirst()) {
                    level.setBlock(p.getSecond(), Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
                    level.levelEvent(player, LevelEvent.PARTICLES_DESTROY_BLOCK, p.getSecond(), Block.getId(other));
                }
            });
        }
        super.playerWillDestroy(level, pos, state, player);
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, PART);
    }

    public static List<Pair<Part, BlockPos>> getPosMap(BlockPos from, BlockState state) {
        Rotation rotation = EntityUtils.fromDirection(state.getValue(FACING));
        BlockPos offset = state.getValue(PART).offset;
        return Arrays.stream(Part.values()).map(p -> Pair.of(p, p.offset.offset(-offset.getX(), -offset.getY(), -offset.getZ()).rotate(rotation).offset(from))).toList();
    }

    private static Direction rotateBy(Direction direction, Direction rotate) {
        if (direction.getAxis() == Direction.Axis.Y)
            return direction;
        return switch (rotate) {
            case SOUTH -> direction.getOpposite();
            case EAST -> direction.getCounterClockWise();
            case WEST -> direction.getClockWise();
            default -> direction;
        };
    }

    public enum Part implements StringRepresentable {

        BOTTOM_LEFT(BlockPos.ZERO, List.of(Direction.UP, Direction.EAST)),
        BOTTOM_RIGHT(new BlockPos(1, 0, 0), List.of(Direction.UP, Direction.WEST)),
        TOP_LEFT(new BlockPos(0, 1, 0), List.of(Direction.DOWN, Direction.EAST)),
        TOP_RIGHT(new BlockPos(1, 1, 0), List.of(Direction.DOWN, Direction.WEST));

        public final BlockPos offset;
        public final List<Direction> neighbors;

        Part(BlockPos offset, List<Direction> neighbors) {
            this.offset = offset;
            this.neighbors = neighbors;
        }

        /**
         * Returns the part corresponding to the given one offset by a normalized direction.
         * Or null if no such part
         */
        @Nullable
        public static Part getPartFor(Part part, Direction direction) {
            BlockPos otherPos = part.offset.offset(direction.getNormal());
            for (Part p : Part.values()) {
                if (p.offset.equals(otherPos))
                    return p;
            }
            return null;
        }

        @Override
        public String getSerializedName() {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }
}
