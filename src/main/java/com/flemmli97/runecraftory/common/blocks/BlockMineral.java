package com.flemmli97.runecraftory.common.blocks;

import com.flemmli97.runecraftory.api.enums.EnumMineralTier;
import com.flemmli97.runecraftory.api.enums.EnumSkills;
import com.flemmli97.runecraftory.common.capability.CapabilityInsts;
import com.flemmli97.runecraftory.common.items.tools.ItemToolHammer;
import com.flemmli97.runecraftory.common.items.weapons.ItemHammerBase;
import com.flemmli97.runecraftory.common.registry.ModBlocks;
import com.flemmli97.runecraftory.common.utils.EntityUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.network.play.server.SPlaySoundEventPacket;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.BlockVoxelShape;
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
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import java.util.List;
import java.util.stream.Stream;

public class BlockMineral extends Block implements IWaterLoggable {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final VoxelShape north = Stream.of(
            Block.makeCuboidShape(8, 0, 0, 13, 6, 5),
            Block.makeCuboidShape(10, 0, 8, 13, 11, 10),
            Block.makeCuboidShape(9, 0, 10, 16, 6, 15),
            Block.makeCuboidShape(6, 0, 8, 10, 8, 10),
            Block.makeCuboidShape(3, 0, 10, 9, 5, 14),
            Block.makeCuboidShape(6, 0, 3, 8, 4, 4),
            Block.makeCuboidShape(2, 0, 1, 7, 2, 3),
            Block.makeCuboidShape(1, 0, 3, 6, 11, 10),
            Block.makeCuboidShape(0, 0, 10, 3, 2, 16),
            Block.makeCuboidShape(13, 0, 1, 15, 5, 10),
            Block.makeCuboidShape(6, 0, 4, 13, 15, 8),
            Block.makeCuboidShape(4, 5, 10, 7, 6, 11),
            Block.makeCuboidShape(11, 5.5, 10.3, 12, 6.5, 12.3),
            Block.makeCuboidShape(12.8, 4.8, 5.3, 14.8, 5.8, 9.3),
            Block.makeCuboidShape(1.4, 1.3, 9.6, 4.4, 2.3, 11.6),
            Block.makeCuboidShape(7.1, 4.5, 12.8, 10.1, 5.5, 13.8),
            Block.makeCuboidShape(3.6, 10.4, 7, 5.6, 11.4, 9),
            Block.makeCuboidShape(3.6, 6.4, 2.5, 5.6, 7.4, 3.5),
            Block.makeCuboidShape(6.6, 2.4, 2.5, 8.6, 3.4, 3.5),
            Block.makeCuboidShape(10.6, 11.4, 7.5, 11.6, 12.4, 8.5),
            Block.makeCuboidShape(8.6, 8.4, 3.5, 11.6, 10.4, 4.5),
            Block.makeCuboidShape(0.6, 5, 4.2, 1.6, 7, 8.2)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();
    public static final VoxelShape west = Stream.of(
            Block.makeCuboidShape(0, 0, 3, 5, 6, 8),
            Block.makeCuboidShape(8, 0, 3, 10, 11, 6),
            Block.makeCuboidShape(10, 0, 0, 15, 6, 7),
            Block.makeCuboidShape(8, 0, 6, 10, 8, 10),
            Block.makeCuboidShape(10, 0, 7, 14, 5, 13),
            Block.makeCuboidShape(3, 0, 8, 4, 4, 10),
            Block.makeCuboidShape(1, 0, 9, 3, 2, 14),
            Block.makeCuboidShape(3, 0, 10, 10, 11, 15),
            Block.makeCuboidShape(10, 0, 13, 16, 2, 16),
            Block.makeCuboidShape(1, 0, 1, 10, 5, 3),
            Block.makeCuboidShape(4, 0, 3, 8, 15, 10),
            Block.makeCuboidShape(10, 5, 9, 11, 6, 12),
            Block.makeCuboidShape(10.3, 5.5, 4, 12.3, 6.5, 5),
            Block.makeCuboidShape(5.3, 4.8, 1.2, 9.3, 5.8, 3.2),
            Block.makeCuboidShape(9.6, 1.3, 11.6, 11.6, 2.3, 14.6),
            Block.makeCuboidShape(12.8, 4.5, 5.9, 13.8, 5.5, 8.9),
            Block.makeCuboidShape(7, 10.4, 10.4, 9, 11.4, 12.4),
            Block.makeCuboidShape(2.5, 6.4, 10.4, 3.5, 7.4, 12.4),
            Block.makeCuboidShape(2.5, 2.4, 7.4, 3.5, 3.4, 9.4),
            Block.makeCuboidShape(7.5, 11.4, 4.4, 8.5, 12.4, 5.4),
            Block.makeCuboidShape(3.5, 8.4, 4.4, 4.5, 10.4, 7.4),
            Block.makeCuboidShape(4.2, 5, 14.4, 8.2, 7, 15.4)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();
    public static final VoxelShape south = Stream.of(
            Block.makeCuboidShape(3, 0, 11, 8, 6, 16),
            Block.makeCuboidShape(3, 0, 6, 6, 11, 8),
            Block.makeCuboidShape(0, 0, 1, 7, 6, 6),
            Block.makeCuboidShape(6, 0, 6, 10, 8, 8),
            Block.makeCuboidShape(7, 0, 2, 13, 5, 6),
            Block.makeCuboidShape(8, 0, 12, 10, 4, 13),
            Block.makeCuboidShape(9, 0, 13, 14, 2, 15),
            Block.makeCuboidShape(10, 0, 6, 15, 11, 13),
            Block.makeCuboidShape(13, 0, 0, 16, 2, 6),
            Block.makeCuboidShape(1, 0, 6, 3, 5, 15),
            Block.makeCuboidShape(3, 0, 8, 10, 15, 12),
            Block.makeCuboidShape(9, 5, 5, 12, 6, 6),
            Block.makeCuboidShape(4, 5.5, 3.7, 5, 6.5, 5.7),
            Block.makeCuboidShape(1.2, 4.8, 6.7, 3.2, 5.8, 10.7),
            Block.makeCuboidShape(11.6, 1.3, 4.4, 14.6, 2.3, 6.4),
            Block.makeCuboidShape(5.9, 4.5, 2.2, 8.9, 5.5, 3.2),
            Block.makeCuboidShape(10.4, 10.4, 7, 12.4, 11.4, 9),
            Block.makeCuboidShape(10.4, 6.4, 12.5, 12.4, 7.4, 13.5),
            Block.makeCuboidShape(7.4, 2.4, 12.5, 9.4, 3.4, 13.5),
            Block.makeCuboidShape(4.4, 11.4, 7.5, 5.4, 12.4, 8.5),
            Block.makeCuboidShape(4.4, 8.4, 11.5, 7.4, 10.4, 12.5),
            Block.makeCuboidShape(14.4, 5, 7.8, 15.4, 7, 11.8)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();
    public static final VoxelShape east = Stream.of(
            Block.makeCuboidShape(11, 0, 8, 16, 6, 13),
            Block.makeCuboidShape(6, 0, 10, 8, 11, 13),
            Block.makeCuboidShape(1, 0, 9, 6, 6, 16),
            Block.makeCuboidShape(6, 0, 6, 8, 8, 10),
            Block.makeCuboidShape(2, 0, 3, 6, 5, 9),
            Block.makeCuboidShape(12, 0, 6, 13, 4, 8),
            Block.makeCuboidShape(13, 0, 2, 15, 2, 7),
            Block.makeCuboidShape(6, 0, 1, 13, 11, 6),
            Block.makeCuboidShape(0, 0, 0, 6, 2, 3),
            Block.makeCuboidShape(6, 0, 13, 15, 5, 15),
            Block.makeCuboidShape(8, 0, 6, 12, 15, 13),
            Block.makeCuboidShape(5, 5, 4, 6, 6, 7),
            Block.makeCuboidShape(3.7, 5.5, 11, 5.7, 6.5, 12),
            Block.makeCuboidShape(6.7, 4.8, 12.8, 10.7, 5.8, 14.8),
            Block.makeCuboidShape(4.4, 1.3, 1.4, 6.4, 2.3, 4.4),
            Block.makeCuboidShape(2.23, 4.5, 7.1, 3.23, 5.5, 10.1),
            Block.makeCuboidShape(7, 10.4, 3.6, 9, 11.4, 5.6),
            Block.makeCuboidShape(12.5, 6.4, 3.6, 13.5, 7.4, 5.6),
            Block.makeCuboidShape(12.5, 2.4, 6.6, 13.5, 3.4, 8.6),
            Block.makeCuboidShape(7.5, 11.4, 10.6, 8.5, 12.4, 11.6),
            Block.makeCuboidShape(11.5, 8.4, 8.6, 12.5, 10.4, 11.6),
            Block.makeCuboidShape(7.8, 5, 0.6, 11.8, 7, 1.6)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

    public final EnumMineralTier tier;

    public BlockMineral(EnumMineralTier tier, Properties properties) {
        super(properties);
        this.tier = tier;
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH).with(WATERLOGGED, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
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
        if (!this.isValidPosition(state, world, pos))
            return Blocks.AIR.getDefaultState();
        if (state.get(WATERLOGGED)) {
            world.getPendingFluidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return super.updatePostPlacement(state, direction, state1, world, pos, pos1);
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader reader, BlockPos pos) {
        return reader.getBlockState(pos.down()).func_242698_a(reader, pos, Direction.UP, BlockVoxelShape.FULL);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest, FluidState fluid) {
        if (world.isRemote)
            return false;
        boolean hammer;
        if (player.isCreative()) {
            return super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
        } else if (hammer = player.getHeldItemMainhand().getItem() instanceof ItemToolHammer || player.getHeldItemMainhand().getItem() instanceof ItemHammerBase) {
            float breakChance = 0.5F;
            if (hammer) {
                ItemToolHammer item = (ItemToolHammer) player.getHeldItemMainhand().getItem();
                breakChance -= item.tier.getTierLevel() * 0.075F;
            }
            this.onBlockHarvested(world, pos, state, player);
            if (world.rand.nextFloat() < breakChance) {
                return world.setBlockState(pos, this.getBrokenState(state), 3);
            } else {
                if (hammer)
                    spawnDrops(state, world, pos, null, player, player.getHeldItemMainhand());
                return false;
            }
        }
        return false;
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBlockHarvested(world, pos, state, player);
        if (player instanceof ServerPlayerEntity)
            ((ServerPlayerEntity) player).connection.sendPacket(new SPlaySoundEventPacket(2001, pos, getStateId(state), false));
    }

    public BlockState getBrokenState(BlockState state) {
        BlockState blockState = ModBlocks.brokenMineralMap.get(this.tier).get().getDefaultState();
        if (blockState.hasProperty(FACING))
            blockState.with(FACING, state.get(FACING));
        if (blockState.hasProperty(WATERLOGGED))
            blockState.with(WATERLOGGED, state.get(WATERLOGGED));
        return blockState;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        Entity entity = builder.get(LootParameters.THIS_ENTITY);
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            player.getCapability(CapabilityInsts.PlayerCap).ifPresent(cap -> {
                float addChance = cap.getSkillLevel(EnumSkills.MINING)[0] * 0.1f;
                if (player.getHeldItemMainhand().getItem() instanceof ItemToolHammer) {
                    ItemToolHammer item = (ItemToolHammer) player.getHeldItemMainhand().getItem();
                    addChance *= 1 + item.tier.getTierLevel() * 0.5;
                }
                builder.withLuck(addChance + EntityUtils.playerLuck(player));
            });
        }
        return super.getDrops(state, builder);
    }
}
