package com.flemmli97.runecraftory.common.blocks;

import com.flemmli97.runecraftory.api.enums.EnumCrafting;
import com.flemmli97.runecraftory.common.blocks.tile.TileCrafting;
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
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public abstract class BlockCrafting extends Block {

    public static final EnumProperty<EnumPart> PART = EnumProperty.create("part", EnumPart.class);
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    private final EnumCrafting type;
    private final boolean hasUpgradeScreen;

    public BlockCrafting(EnumCrafting type, Properties p_i48440_1_) {
        super(p_i48440_1_);
        this.type = type;
        this.hasUpgradeScreen = type == EnumCrafting.ARMOR || type == EnumCrafting.FORGE;
    }

    @Override
    public BlockState updatePostPlacement(BlockState state, Direction fromDir, BlockState fromState, IWorld world, BlockPos pos, BlockPos fromPos) {
        Direction dir = state.get(FACING);
        if (state.get(PART) == EnumPart.LEFT && fromDir == dir.rotateYCCW()) {
            return !this.isEqual(state, fromState) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(state, fromDir, fromState, world, pos, fromPos);
        } else if (fromDir == dir.rotateY())
            return !this.isEqual(state, fromState) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(state, fromDir, fromState, world, pos, fromPos);
        return super.updatePostPlacement(state, fromDir, fromState, world, pos, fromPos);
    }

    private boolean isEqual(BlockState one, BlockState other) {
        return other.getBlock() == one.getBlock() &&
                other.get(FACING) == one.get(FACING)
                && other.get(PART) != one.get(PART);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext p_196258_1_) {
        BlockPos blockpos = p_196258_1_.getPos();
        if (blockpos.getY() < 255 && p_196258_1_.getWorld().getBlockState(blockpos.up()).isReplaceable(p_196258_1_)) {
            World world = p_196258_1_.getWorld();
            return this.getDefaultState().with(FACING, p_196258_1_.getPlacementHorizontalFacing().getOpposite()).with(PART, EnumPart.LEFT);
        } else {
            return null;
        }
    }

    /**
     * Called by ItemBlocks after a block is set in the world, to allow post-place logic
     */
    @Override
    public void onBlockPlacedBy(World p_180633_1_, BlockPos p_180633_2_, BlockState p_180633_3_, LivingEntity living, ItemStack p_180633_5_) {
        p_180633_1_.setBlockState(p_180633_2_.offset(living.getHorizontalFacing().getOpposite().rotateYCCW()), p_180633_3_.with(PART, EnumPart.RIGTH), 3);
    }

    @Override
    public ActionResultType onUse(BlockState p_225533_1_, World world, BlockPos pos, PlayerEntity player, Hand p_225533_5_, BlockRayTraceResult p_225533_6_) {
        if (world.isRemote) {
            return ActionResultType.PASS;
        } else {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileCrafting) {
                NetworkHooks.openGui((ServerPlayerEntity) player, (TileCrafting) tile, pos);
            }
            /*
            BlockPos newPos = pos;
            if(state.getValue(PART)==EnumPartType.RIGHT)
                newPos=newPos.offset(state.getValue(FACING).rotateY());
            if(!player.isSneaking())
                player.openGui(RuneCraftory.instance, LibReference.guiMaking, world, newPos.getX(), newPos.getY(), newPos.getZ());
            else if(this.hasUpgrade())
                player.openGui(RuneCraftory.instance, LibReference.guiUpgrade, world, newPos.getX(), newPos.getY(), newPos.getZ());

             */
            return ActionResultType.success(world.isRemote);
        }
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader p_196260_2_, BlockPos p_196260_3_) {
        if (state.get(PART) == EnumPart.LEFT) {
            BlockState blockstate = p_196260_2_.getBlockState(p_196260_3_.offset(state.get(FACING).rotateYCCW()));
            return blockstate.getMaterial().isReplaceable();
        }
        BlockState blockstate = p_196260_2_.getBlockState(p_196260_3_.offset(state.get(FACING).rotateY()));
        return blockstate.isIn(this);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> p_206840_1_) {
        p_206840_1_.add(FACING, PART);
    }

    @Override
    public void onReplaced(BlockState p_196243_1_, World p_196243_2_, BlockPos p_196243_3_, BlockState p_196243_4_, boolean p_196243_5_) {
        if (!p_196243_1_.isIn(p_196243_4_.getBlock())) {
            TileEntity tileentity = p_196243_2_.getTileEntity(p_196243_3_);
            if (tileentity instanceof IInventory) {
                InventoryHelper.dropInventoryItems(p_196243_2_, p_196243_3_, (IInventory) tileentity);
                p_196243_2_.updateComparatorOutputLevel(p_196243_3_, this);
            }

            super.onReplaced(p_196243_1_, p_196243_2_, p_196243_3_, p_196243_4_, p_196243_5_);
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return state.get(PART) == EnumPart.LEFT ? this.getTile(state, world) : null;
    }

    public abstract TileEntity getTile(BlockState state, IBlockReader world);

    enum EnumPart implements IStringSerializable {
        LEFT("left"),
        RIGTH("right");

        final String s;

        EnumPart(String s) {
            this.s = s;
        }

        @Override
        public String getString() {
            return s;
        }
    }
}
