package io.github.flemmli97.runecraftory.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

import java.util.function.Supplier;

public class BlockFruitTreeLeaf extends LeavesBlock {

    public static final BooleanProperty HAS_FRUIT = BooleanProperty.create("has_fruit");

    private final Supplier<Item> fruit;

    public BlockFruitTreeLeaf(Properties properties, Supplier<Item> fruit) {
        super(properties);
        this.fruit = fruit;
    }

    @Override
    public float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        return super.getDestroyProgress(state, player, level, pos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HAS_FRUIT);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide)
            return InteractionResult.CONSUME;
        if (state.getValue(HAS_FRUIT)) {
            Block.popResource(level, pos.below(), new ItemStack(this.fruit.get()));
            level.setBlock(pos, state.setValue(HAS_FRUIT, false), Block.UPDATE_ALL);
            return InteractionResult.CONSUME;
        }
        return super.use(state, level, pos, player, hand, hit);
    }
}

