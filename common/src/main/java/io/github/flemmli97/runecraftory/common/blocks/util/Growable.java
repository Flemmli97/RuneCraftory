package io.github.flemmli97.runecraftory.common.blocks.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Function;

public interface Growable {

    int runecraftory$getGrowableMaxAge();

    BlockState runecraftory$getGrowableStateForAge(BlockState current, int age);

    boolean runecraftory$isAtMaxAge(BlockState state);

    default boolean canGrow(ServerLevel level, BlockPos pos, BlockState state) {
        return !this.runecraftory$isAtMaxAge(state);
    }

    default void onGrow(ServerLevel level, BlockPos pos, BlockState state, BlockState old) {
        level.setBlock(pos, state, Block.UPDATE_ALL);
    }

    default BlockPos getFarmlandPosition(BlockPos pos, BlockState state) {
        return pos.below();
    }

    default void onQuickHarvest(BlockState state, ServerLevel serverLevel, BlockPos pos, Entity entity, ItemStack stack, Function<ItemStack, ItemStack> stackConsumer) {
        if (stackConsumer != null) {
            Block.getDrops(state, serverLevel, pos, null, entity, stack)
                    .forEach(s -> {
                        ItemStack rest = stackConsumer.apply(s);
                        if (!rest.isEmpty())
                            Block.popResource(serverLevel, pos, rest);
                    });
            state.spawnAfterBreak(serverLevel, pos, ItemStack.EMPTY, true);
        } else
            Block.dropResources(state, serverLevel, pos, null, entity, stack);
    }
}
