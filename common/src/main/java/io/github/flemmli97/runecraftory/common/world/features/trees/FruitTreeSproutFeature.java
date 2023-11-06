package io.github.flemmli97.runecraftory.common.world.features.trees;

import com.mojang.serialization.Codec;
import io.github.flemmli97.runecraftory.common.blocks.tile.TreeBlockEntity;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.ArrayList;
import java.util.List;

public class FruitTreeSproutFeature extends Feature<FruitTreeSproutConfiguration> {

    public FruitTreeSproutFeature(Codec<FruitTreeSproutConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<FruitTreeSproutConfiguration> context) {
        BlockPos soil = context.origin().below();
        if (context.level().isStateAtPosition(soil, s -> !s.is(ModBlocks.treeSoil.get()))) {
            context.level().setBlock(soil, ModBlocks.treeSoil.get().defaultBlockState(), Block.UPDATE_ALL);
        }
        BlockPos above = context.origin().above();
        BlockEntity blockEntity = context.level().getBlockEntity(context.origin());
        context.level().setBlock(above, context.config().trunkProvider().getState(context.random(), above), Block.UPDATE_ALL);
        List<BlockPos> leaves = new ArrayList<>();
        for (Direction dir : Direction.values()) {
            if (dir == Direction.DOWN)
                continue;
            BlockPos pos = above.relative(dir);
            context.level().setBlock(pos, context.config().foliageProvider().getState(context.random(), pos).setValue(BlockStateProperties.DISTANCE, 1), Block.UPDATE_ALL);
            leaves.add(pos);
        }
        if (blockEntity instanceof TreeBlockEntity tree) {
            tree.updateTreeLogs(List.of(above));
            tree.updateTreeLeaves(leaves);
        }
        return true;
    }
}
