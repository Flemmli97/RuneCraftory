package io.github.flemmli97.runecraftory.neoforge.data.worldgen.features;

import io.github.flemmli97.runecraftory.common.blocks.TreeLogBlock;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryBlocks;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryFeatures;
import io.github.flemmli97.runecraftory.common.world.features.trees.FruitLeaveDecorator;
import io.github.flemmli97.runecraftory.common.world.features.trees.FruitTreeSproutConfiguration;
import io.github.flemmli97.runecraftory.common.world.features.trees.FruitTreeTrunkPlacer;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import java.util.List;

public class ConfiguredFeatureGen {

    public static void bootStrap(BootstrapContext<ConfiguredFeature<?, ?>> ctx) {
        ctx.register(RuneCraftoryFeatures.APPLE_1, fruitSprout(RuneCraftoryBlocks.APPLE_WOOD.get(), RuneCraftoryBlocks.APPLE_LEAVES.get()));
        ctx.register(RuneCraftoryFeatures.APPLE_2, fruitTree(RuneCraftoryBlocks.APPLE_WOOD.get(), RuneCraftoryBlocks.APPLE_LEAVES.get(), RuneCraftoryBlocks.APPLE.get(), false));
        ctx.register(RuneCraftoryFeatures.APPLE_3, fruitTree(RuneCraftoryBlocks.APPLE_WOOD.get(), RuneCraftoryBlocks.APPLE_LEAVES.get(), RuneCraftoryBlocks.APPLE.get(), true));
        ctx.register(RuneCraftoryFeatures.ORANGE_1, fruitSprout(RuneCraftoryBlocks.ORANGE_WOOD.get(), RuneCraftoryBlocks.ORANGE_LEAVES.get()));
        ctx.register(RuneCraftoryFeatures.ORANGE_2, fruitTree(RuneCraftoryBlocks.ORANGE_WOOD.get(), RuneCraftoryBlocks.ORANGE_LEAVES.get(), RuneCraftoryBlocks.ORANGE.get(), false));
        ctx.register(RuneCraftoryFeatures.ORANGE_3, fruitTree(RuneCraftoryBlocks.ORANGE_WOOD.get(), RuneCraftoryBlocks.ORANGE_LEAVES.get(), RuneCraftoryBlocks.ORANGE.get(), true));
        ctx.register(RuneCraftoryFeatures.GRAPE_1, fruitSprout(RuneCraftoryBlocks.GRAPE_WOOD.get(), RuneCraftoryBlocks.GRAPE_LEAVES.get()));
        ctx.register(RuneCraftoryFeatures.GRAPE_2, fruitTree(RuneCraftoryBlocks.GRAPE_WOOD.get(), RuneCraftoryBlocks.GRAPE_LEAVES.get(), RuneCraftoryBlocks.GRAPE.get(), false));
        ctx.register(RuneCraftoryFeatures.GRAPE_3, fruitTree(RuneCraftoryBlocks.GRAPE_WOOD.get(), RuneCraftoryBlocks.GRAPE_LEAVES.get(), RuneCraftoryBlocks.GRAPE.get(), true));
    }

    private static ConfiguredFeature<FruitTreeSproutConfiguration, ?> fruitSprout(Block log, Block leave) {
        return new ConfiguredFeature<>(RuneCraftoryFeatures.FRUIT_SPROUT.get(), new FruitTreeSproutConfiguration(BlockStateProvider.simple(log.defaultBlockState().setValue(TreeLogBlock.IS_TREE_PART, true)), BlockStateProvider.simple(leave)));
    }

    private static ConfiguredFeature<TreeConfiguration, ?> fruitTree(Block log, Block leave, Block fruit, boolean max) {
        return new ConfiguredFeature<>(Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(log.defaultBlockState().setValue(TreeLogBlock.IS_TREE_PART, true)),
                        new FruitTreeTrunkPlacer(max ? 3 : 1, 1, max ? 2 : 1, max ? 3 : 1),
                        BlockStateProvider.simple(leave),
                        new FancyFoliagePlacer(max ? ConstantInt.of(2) : ConstantInt.of(1), ConstantInt.of(0), max ? 3 : 2),
                        new TwoLayersFeatureSize(1, 0, 2))
                        .decorators(max ? List.of(new FruitLeaveDecorator(BlockStateProvider.simple(fruit), UniformInt.of(4, 9))) : List.of()).ignoreVines().build());
    }
}
