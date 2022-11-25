package io.github.flemmli97.runecraftory.common.blocks;

import io.github.flemmli97.runecraftory.common.utils.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

public class BlockMeltableSnow extends SnowLayerBlock {

    public BlockMeltableSnow(Properties properties) {
        super(properties);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
        super.randomTick(state, level, pos, random);
        if (random.nextInt(5) == 0 && !WorldUtils.coldEnoughForSnow(level, pos, level.getBiome(pos).value())) {
            SnowLayerBlock.dropResources(state, level, pos);
            level.removeBlock(pos, false);
        }
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
        int i = state.getValue(LAYERS);
        if (useContext.getItemInHand().is(Items.SNOW) && i < 8) {
            if (useContext.replacingClickedOnBlock()) {
                return useContext.getClickedFace() == Direction.UP;
            }
            return true;
        }
        return i == 1;
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return new ItemStack(Items.SNOW);
    }
}
