package io.github.flemmli97.runecraftory.common.blocks;

import io.github.flemmli97.runecraftory.api.enums.EnumCrafting;
import io.github.flemmli97.runecraftory.common.blocks.tile.TileCrafting;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class BlockCrafting extends Block {

    public static final EnumProperty<EnumPart> PART = EnumProperty.create("part", EnumPart.class);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private final EnumCrafting type;

    public BlockCrafting(EnumCrafting type, AbstractBlock.Properties props) {
        super(props);
        this.type = type;
    }

    @Override
    public BlockState updatePostPlacement(BlockState state, Direction fromDir, BlockState fromState, IWorld world, BlockPos pos, BlockPos fromPos) {
        Direction dir = state.get(FACING);
        if (state.get(PART) == EnumPart.LEFT) {
            return fromDir == dir.rotateY() && !this.isEqual(state, fromState) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(state, fromDir, fromState, world, pos, fromPos);
        } else
            return fromDir == dir.rotateYCCW() && !this.isEqual(state, fromState) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(state, fromDir, fromState, world, pos, fromPos);
    }

    private boolean isEqual(BlockState one, BlockState other) {
        return other.getBlock() == one.getBlock() &&
                other.get(FACING) == one.get(FACING)
                && other.get(PART) != one.get(PART);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext ctx) {
        BlockPos blockpos = ctx.getPos();
        if (blockpos.getY() < 255 && ctx.getWorld().getBlockState(blockpos).isReplaceable(ctx)) {
            BlockPos other = blockpos.offset(ctx.getPlacementHorizontalFacing().getOpposite().rotateYCCW());
            if (ctx.getWorld().getBlockState(other).isReplaceable(ctx))
                return this.getDefaultState().with(FACING, ctx.getPlacementHorizontalFacing().getOpposite()).with(PART, EnumPart.RIGHT);
        }
        return null;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity living, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, living, stack);
        if (!world.isRemote)
            world.setBlockState(pos.offset(living.getHorizontalFacing().getOpposite().rotateYCCW()), state.with(PART, EnumPart.LEFT), 3);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        if (world.isRemote) {
            return ActionResultType.CONSUME;
        } else {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileCrafting) {
                if (player.isSneaking() && this.hasUpgradeScreen())
                    NetworkHooks.openGui((ServerPlayerEntity) player, ((TileCrafting) tile).upgradeMenu(), pos);
                else
                    NetworkHooks.openGui((ServerPlayerEntity) player, (TileCrafting) tile, pos);
                return ActionResultType.SUCCESS;
            }
            return ActionResultType.PASS;
        }
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader reader, BlockPos pos) {
        if (state.get(PART) == EnumPart.RIGHT) {
            return true;
        }
        BlockState blockstate = reader.getBlockState(pos.offset(state.get(FACING).rotateY()));
        return blockstate.matchesBlock(this) && blockstate.get(FACING) == state.get(FACING) && blockstate.get(PART) == EnumPart.LEFT;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, PART);
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState orig, boolean moving) {
        if (!state.matchesBlock(orig.getBlock())) {
            TileEntity tileentity = world.getTileEntity(pos);
            if (tileentity instanceof IInventory) {
                InventoryHelper.dropInventoryItems(world, pos, (IInventory) tileentity);
                world.updateComparatorOutputLevel(pos, this);
            }

            super.onReplaced(state, world, pos, orig, moving);
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return state.get(PART) == EnumPart.RIGHT;
    }

    public BlockPos getOtherPos(BlockPos from, BlockState state) {
        if (state.get(PART) == EnumPart.LEFT)
            return from.offset(state.get(FACING).rotateY());
        return from.offset(state.get(FACING).rotateYCCW());
    }

    public boolean hasUpgradeScreen() {
        return this.type == EnumCrafting.ARMOR || this.type == EnumCrafting.FORGE;
    }

    public enum EnumPart implements IStringSerializable {

        LEFT("left"),
        RIGHT("right");

        private final String s;

        EnumPart(String s) {
            this.s = s;
        }

        @Override
        public String getString() {
            return this.s;
        }
    }
}
