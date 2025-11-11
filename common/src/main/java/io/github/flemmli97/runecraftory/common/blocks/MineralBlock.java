package io.github.flemmli97.runecraftory.common.blocks;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.attachment.Skills;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.blocks.util.MineralBlockTier;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolHammer;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryBlocks;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryDataComponentTypes;
import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.platform.ExtendedBlock;
import io.github.flemmli97.tenshilib.common.utils.CodecUtils;
import io.github.flemmli97.tenshilib.common.utils.VoxelUtils;
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
import net.minecraft.world.level.block.LevelEvent;
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
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public class MineralBlock extends Block implements SimpleWaterloggedBlock, ExtendedBlock {

    public static final MapCodec<MineralBlock> CODEC = RecordCodecBuilder.mapCodec(inst ->
            inst.group(CodecUtils.stringEnumCodec(MineralBlockTier.class, null).fieldOf("mineral_tier").forGetter(d -> d.tier),
                    propertiesCodec()
            ).apply(inst, MineralBlock::new));

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final VoxelShape[] SHAPES = VoxelUtils.joinedOrDirs(
            VoxelUtils.ShapeBuilder.of(8, 0, 0, 13, 6, 5),
            VoxelUtils.ShapeBuilder.of(10, 0, 8, 13, 11, 10),
            VoxelUtils.ShapeBuilder.of(9, 0, 10, 16, 6, 15),
            VoxelUtils.ShapeBuilder.of(6, 0, 8, 10, 8, 10),
            VoxelUtils.ShapeBuilder.of(3, 0, 10, 9, 5, 14),
            VoxelUtils.ShapeBuilder.of(6, 0, 3, 8, 4, 4),
            VoxelUtils.ShapeBuilder.of(2, 0, 1, 7, 2, 3),
            VoxelUtils.ShapeBuilder.of(1, 0, 3, 6, 11, 10),
            VoxelUtils.ShapeBuilder.of(0, 0, 10, 3, 2, 16),
            VoxelUtils.ShapeBuilder.of(13, 0, 1, 15, 5, 10),
            VoxelUtils.ShapeBuilder.of(6, 0, 4, 13, 15, 8),
            VoxelUtils.ShapeBuilder.of(4, 5, 10, 7, 6, 11),
            VoxelUtils.ShapeBuilder.of(11, 5.5, 10.3, 12, 6.5, 12.3),
            VoxelUtils.ShapeBuilder.of(12.8, 4.8, 5.3, 14.8, 5.8, 9.3),
            VoxelUtils.ShapeBuilder.of(1.4, 1.3, 9.6, 4.4, 2.3, 11.6),
            VoxelUtils.ShapeBuilder.of(7.1, 4.5, 12.8, 10.1, 5.5, 13.8),
            VoxelUtils.ShapeBuilder.of(3.6, 10.4, 7, 5.6, 11.4, 9),
            VoxelUtils.ShapeBuilder.of(3.6, 6.4, 2.5, 5.6, 7.4, 3.5),
            VoxelUtils.ShapeBuilder.of(6.6, 2.4, 2.5, 8.6, 3.4, 3.5),
            VoxelUtils.ShapeBuilder.of(10.6, 11.4, 7.5, 11.6, 12.4, 8.5),
            VoxelUtils.ShapeBuilder.of(8.6, 8.4, 3.5, 11.6, 10.4, 4.5),
            VoxelUtils.ShapeBuilder.of(0.6, 5, 4.2, 1.6, 7, 8.2));

    public final MineralBlockTier tier;

    public MineralBlock(MineralBlockTier tier, Properties properties) {
        super(properties);
        this.tier = tier;
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
    }

    @Override
    public MapCodec<MineralBlock> codec() {
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
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        Entity entity = builder.getOptionalParameter(LootContextParams.THIS_ENTITY);
        if (entity instanceof Player player) {
            PlayerData data = RunecraftoryAttachments.PLAYER_DATA.get().get(player);
            float addChance = data.getSkillLevel(Skills.MINING).getLevel() * 0.03f;
            ItemStack stack = player.getMainHandItem();
            if (stack.getItem() instanceof ItemToolHammer && stack.has(RuneCraftoryDataComponentTypes.TOOL_TIER.get())) {
                addChance += stack.get(RuneCraftoryDataComponentTypes.TOOL_TIER.get())
                        .getTierLevel() * 0.75;
            }
            builder.withLuck(addChance + EntityUtils.playerLuck(player));
        }
        return super.getDrops(state, builder);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).isFaceSturdy(level, pos, Direction.UP, SupportType.FULL);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES[state.getValue(CraftingBlock.FACING).get2DDataValue()];
    }

    @Override
    public float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        float f = super.getDestroyProgress(state, player, level, pos);
        return player.getMainHandItem().is(RunecraftoryTags.Items.HAMMER_TOOLS) ? f : f * 0.5f;
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
            float breakChance = 0.7F;
            ItemStack stack = player.getMainHandItem();
            if (stack.getItem() instanceof ItemToolHammer && stack.has(RuneCraftoryDataComponentTypes.TOOL_TIER.get())) {
                breakChance -= (stack.get(RuneCraftoryDataComponentTypes.TOOL_TIER.get())
                        .getTierLevel() + 1) * 0.05F;
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
        BlockState blockState = RuneCraftoryBlocks.BROKEN_MINERAL_MAP.get(this.tier).get().defaultBlockState();
        if (blockState.hasProperty(FACING))
            blockState.setValue(FACING, state.getValue(FACING));
        if (blockState.hasProperty(WATERLOGGED))
            blockState.setValue(WATERLOGGED, state.getValue(WATERLOGGED));
        return blockState;
    }
}
