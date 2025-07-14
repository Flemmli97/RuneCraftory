package io.github.flemmli97.runecraftory.common.blocks;

import io.github.flemmli97.runecraftory.common.blocks.entity.CraftingBlockEntity;
import io.github.flemmli97.runecraftory.common.blocks.entity.UpgradingCraftingBlockEntity;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public abstract class CraftingBlock extends HorizontalDirectionalBlock implements EntityBlock {

    public static final EnumProperty<EnumPart> PART = EnumProperty.create("part", EnumPart.class);

    public CraftingBlock(BlockBehaviour.Properties props) {
        super(props);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction fromDir, BlockState fromState, LevelAccessor level, BlockPos pos, BlockPos fromPos) {
        Direction dir = state.getValue(FACING);
        if (state.getValue(PART) == EnumPart.RIGHT) {
            return fromDir == dir.getClockWise() && !this.isEqual(state, fromState) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, fromDir, fromState, level, pos, fromPos);
        } else
            return fromDir == dir.getCounterClockWise() && !this.isEqual(state, fromState) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, fromDir, fromState, level, pos, fromPos);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState orig, boolean moving) {
        if (!state.is(orig.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof CraftingBlockEntity craftingBlock) {
                craftingBlock.dropContents(level, pos);
                level.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, level, pos, orig, moving);
        }
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (level.isClientSide) {
            return ItemInteractionResult.CONSUME;
        } else {
            BlockEntity tile = level.getBlockEntity(pos);
            if (!(tile instanceof CraftingBlockEntity)) {
                pos = this.getOtherPos(pos, state);
                tile = level.getBlockEntity(pos);
            }
            if (tile instanceof CraftingBlockEntity craftingBlock) {
                if (player.isShiftKeyDown() && craftingBlock instanceof UpgradingCraftingBlockEntity upgrading)
                    Platform.INSTANCE.openGuiMenu((ServerPlayer) player, upgrading.upgradeMenu(), pos);
                else
                    Platform.INSTANCE.openGuiMenu((ServerPlayer) player, craftingBlock, pos);
                return ItemInteractionResult.SUCCESS;
            }
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
        if (state.getValue(PART) == EnumPart.LEFT) {
            return true;
        }
        BlockState blockstate = reader.getBlockState(this.getOtherPos(pos, state));
        return blockstate.is(this) && blockstate.getValue(FACING) == state.getValue(FACING) && blockstate.getValue(PART) == EnumPart.RIGHT;
    }

    private boolean isEqual(BlockState one, BlockState other) {
        return other.getBlock() == one.getBlock() &&
                other.getValue(FACING) == one.getValue(FACING)
                && other.getValue(PART) != one.getValue(PART);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockPos blockpos = ctx.getClickedPos();
        if (ctx.getLevel().getBlockState(blockpos).canBeReplaced(ctx)) {
            BlockPos other = blockpos.relative(ctx.getHorizontalDirection().getOpposite().getCounterClockWise());
            if (ctx.getLevel().getBlockState(other).canBeReplaced(ctx))
                return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite()).setValue(PART, EnumPart.LEFT);
        }
        return null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity living, ItemStack stack) {
        super.setPlacedBy(level, pos, state, living, stack);
        if (!level.isClientSide)
            level.setBlock(pos.relative(living.getDirection().getOpposite().getCounterClockWise()), state.setValue(PART, EnumPart.RIGHT), Block.UPDATE_ALL);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        BlockPos blockPos = this.getOtherPos(pos, state);
        BlockState other;
        if (!level.isClientSide && player.isCreative() && state.getValue(PART) == EnumPart.RIGHT && (other = level.getBlockState(blockPos)).is(this) && other.getValue(PART) == EnumPart.LEFT) {
            level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
            level.levelEvent(player, LevelEvent.PARTICLES_DESTROY_BLOCK, blockPos, Block.getId(other));
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, PART);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        if (state.getValue(PART) == EnumPart.LEFT)
            return this.createNewBlockEntity(pos, state);
        return null;
    }

    protected abstract BlockEntity createNewBlockEntity(BlockPos pos, BlockState state);

    public BlockPos getOtherPos(BlockPos from, BlockState state) {
        if (state.getValue(PART) == EnumPart.RIGHT)
            return from.relative(state.getValue(FACING).getClockWise());
        return from.relative(state.getValue(FACING).getCounterClockWise());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING).getOpposite()));
    }

    public enum EnumPart implements StringRepresentable {

        LEFT("left"),
        RIGHT("right");

        private final String s;

        EnumPart(String s) {
            this.s = s;
        }

        @Override
        public String getSerializedName() {
            return this.s;
        }
    }

    public record ShapeBuilder(double x1, double y1, double z1, double x2, double y2, double z2) {

        public static ShapeBuilder of(double x1, double y1, double z1, double x2, double y2, double z2) {
            return new ShapeBuilder(x1, y1, z1, x2, y2, z2);
        }
    }
}
