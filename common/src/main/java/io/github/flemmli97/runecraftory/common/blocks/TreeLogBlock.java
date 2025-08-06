package io.github.flemmli97.runecraftory.common.blocks;

import io.github.flemmli97.runecraftory.common.blocks.entity.TreeBlockEntity;
import io.github.flemmli97.runecraftory.common.blocks.entity.TreeLogBlockEntity;
import io.github.flemmli97.runecraftory.platform.ExtendedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;

public class TreeLogBlock extends RotatedPillarBlock implements EntityBlock, ExtendedBlock {

    public static final BooleanProperty IS_TREE_PART = BooleanProperty.create("is_tree_part");

    public TreeLogBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(IS_TREE_PART, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(IS_TREE_PART);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        TreeBlockEntity tree = this.resolveTreeForBreak(state, level, pos, null);
        if (tree != null) {
            tree.onRemove(level, pos, true);
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if (!(player instanceof ServerPlayer serverPlayer))
            return false;
        TreeBlockEntity tree = this.resolveTreeForBreak(state, level, pos, player);
        if (tree == null || !(tree.getBlockState().getBlock() instanceof TreeBaseBlock block) || !block.runecraftory$isAtMaxAge(tree.getBlockState())) {
            this.playerWillDestroy(level, pos, state, player);
            return level.setBlock(pos, fluid.createLegacyBlock(), Block.UPDATE_ALL);
        }
        if (tree.getHealth() <= 0) {
            tree.onRemove(level, pos, true);
            return true;
        }
        tree.onBreak(10);
        dropResources(state, level, pos, null, player, player.getMainHandItem());
        serverPlayer.connection.send(new ClientboundBlockUpdatePacket(pos, state));
        return false;
    }

    protected TreeBlockEntity resolveTreeForBreak(BlockState state, Level level, BlockPos pos, @Nullable Player player) {
        if ((player != null && player.isCreative()) || !state.getValue(IS_TREE_PART) || !(level.getBlockEntity(pos) instanceof TreeLogBlockEntity log))
            return null;
        if (!(level.getBlockEntity(log.treeBase()) instanceof TreeBlockEntity tree) || !tree.isPartOf(log))
            return null;
        return tree;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return state.getValue(IS_TREE_PART) ? new TreeLogBlockEntity(pos, state) : null;
    }
}
