package io.github.flemmli97.runecraftory.common.blocks;

import io.github.flemmli97.runecraftory.api.enums.EnumCrafting;
import io.github.flemmli97.runecraftory.common.blocks.tile.CraftingBlockEntity;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public abstract class BlockCrafting extends Block implements EntityBlock {

    public static final EnumProperty<EnumPart> PART = EnumProperty.create("part", EnumPart.class);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private final EnumCrafting type;

    public BlockCrafting(EnumCrafting type, BlockBehaviour.Properties props) {
        super(props);
        this.type = type;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction fromDir, BlockState fromState, LevelAccessor level, BlockPos pos, BlockPos fromPos) {
        Direction dir = state.getValue(FACING);
        if (state.getValue(PART) == EnumPart.LEFT) {
            return fromDir == dir.getClockWise() && !this.isEqual(state, fromState) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, fromDir, fromState, level, pos, fromPos);
        } else
            return fromDir == dir.getCounterClockWise() && !this.isEqual(state, fromState) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, fromDir, fromState, level, pos, fromPos);
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
        if (blockpos.getY() < 255 && ctx.getLevel().getBlockState(blockpos).canBeReplaced(ctx)) {
            BlockPos other = blockpos.relative(ctx.getHorizontalDirection().getOpposite().getCounterClockWise());
            if (ctx.getLevel().getBlockState(other).canBeReplaced(ctx))
                return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite()).setValue(PART, EnumPart.RIGHT);
        }
        return null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity living, ItemStack stack) {
        super.setPlacedBy(level, pos, state, living, stack);
        if (!level.isClientSide)
            level.setBlock(pos.relative(living.getDirection().getOpposite().getCounterClockWise()), state.setValue(PART, EnumPart.LEFT), 3);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (level.isClientSide) {
            return InteractionResult.CONSUME;
        } else {
            BlockEntity tile = level.getBlockEntity(pos);
            if (tile instanceof CraftingBlockEntity) {
                if (player.isShiftKeyDown() && this.hasUpgradeScreen())
                    Platform.INSTANCE.openGuiMenu((ServerPlayer) player, ((CraftingBlockEntity) tile).upgradeMenu(), pos);
                else
                    Platform.INSTANCE.openGuiMenu((ServerPlayer) player, (CraftingBlockEntity) tile, pos);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
        if (state.getValue(PART) == EnumPart.RIGHT) {
            return true;
        }
        BlockState blockstate = reader.getBlockState(pos.relative(state.getValue(FACING).getClockWise()));
        return blockstate.is(this) && blockstate.getValue(FACING) == state.getValue(FACING) && blockstate.getValue(PART) == EnumPart.LEFT;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, PART);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState orig, boolean moving) {
        if (!state.is(orig.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof CraftingBlockEntity craftingBlock) {
                Containers.dropContents(level, pos, craftingBlock.getInventory());
                level.updateNeighbourForOutputSignal(pos, this);
            }

            super.onRemove(state, level, pos, orig, moving);
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        if (state.getValue(PART) == EnumPart.RIGHT)
            return this.createNewBlockEntity(pos, state);
        return null;
    }

    protected abstract BlockEntity createNewBlockEntity(BlockPos pos, BlockState state);

    public BlockPos getOtherPos(BlockPos from, BlockState state) {
        if (state.getValue(PART) == EnumPart.LEFT)
            return from.relative(state.getValue(FACING).getClockWise());
        return from.relative(state.getValue(FACING).getCounterClockWise());
    }

    public boolean hasUpgradeScreen() {
        return this.type == EnumCrafting.ARMOR || this.type == EnumCrafting.FORGE;
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
}