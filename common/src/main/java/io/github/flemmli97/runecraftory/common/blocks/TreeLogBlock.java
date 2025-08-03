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
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if (!(player instanceof ServerPlayer serverPlayer))
            return false;
        if (player.isCreative() || !state.getValue(IS_TREE_PART)
                || !(level.getBlockEntity(pos) instanceof TreeLogBlockEntity log)
                || !(level.getBlockEntity(log.treeBase()) instanceof TreeBlockEntity tree)) {
            this.playerWillDestroy(level, pos, state, player);
            return level.setBlock(pos, fluid.createLegacyBlock(), Block.UPDATE_ALL);
        }
        if (tree.getHealth() <= 0) {
            tree.onRemove(level, true);
            return level.setBlock(tree.getBlockPos(), fluid.createLegacyBlock(), Block.UPDATE_ALL);
        }
        tree.onBreak();
        dropResources(state, level, pos, null, player, player.getMainHandItem());
        serverPlayer.connection.send(new ClientboundBlockUpdatePacket(pos, state));
        return false;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return state.getValue(IS_TREE_PART) ? new TreeLogBlockEntity(pos, state) : null;
    }
}
