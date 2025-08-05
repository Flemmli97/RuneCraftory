package io.github.flemmli97.runecraftory.neoforge.data.worldgen.features;

import io.github.flemmli97.runecraftory.common.blocks.TreeLogBlock;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryBlocks;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryFeatures;
import io.github.flemmli97.runecraftory.common.world.features.trees.FruitLeaveDecorator;
import io.github.flemmli97.runecraftory.common.world.features.trees.FruitTreeSproutConfiguration;
import io.github.flemmli97.runecraftory.common.world.features.trees.FruitTreeTrunkPlacer;
import io.github.flemmli97.tenshilib.common.data.provider.CodecBasedProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
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
import java.util.concurrent.CompletableFuture;

public class ConfiguredFeatureGen extends CodecBasedProvider<ConfiguredFeature<?, ?>> {

    public ConfiguredFeatureGen(PackOutput output, String modid, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, PackOutput.Target.DATA_PACK, "Configured Features", modid, Registries.CONFIGURED_FEATURE.location().getPath(), ConfiguredFeature.DIRECT_CODEC, provider);
    }

    @Override
    protected void add(HolderLookup.Provider provider) {
        this.add(RuneCraftoryFeatures.APPLE_1.location(), this.fruitSprout(RuneCraftoryBlocks.APPLE_WOOD.get(), RuneCraftoryBlocks.APPLE_LEAVES.get()));
        this.add(RuneCraftoryFeatures.APPLE_2.location(), this.fruitTree(RuneCraftoryBlocks.APPLE_WOOD.get(), RuneCraftoryBlocks.APPLE_LEAVES.get(), RuneCraftoryBlocks.APPLE.get(), false));
        this.add(RuneCraftoryFeatures.APPLE_3.location(), this.fruitTree(RuneCraftoryBlocks.APPLE_WOOD.get(), RuneCraftoryBlocks.APPLE_LEAVES.get(), RuneCraftoryBlocks.APPLE.get(), true));
        this.add(RuneCraftoryFeatures.ORANGE_1.location(), this.fruitSprout(RuneCraftoryBlocks.ORANGE_WOOD.get(), RuneCraftoryBlocks.ORANGE_LEAVES.get()));
        this.add(RuneCraftoryFeatures.ORANGE_2.location(), this.fruitTree(RuneCraftoryBlocks.ORANGE_WOOD.get(), RuneCraftoryBlocks.ORANGE_LEAVES.get(), RuneCraftoryBlocks.ORANGE.get(), false));
        this.add(RuneCraftoryFeatures.ORANGE_3.location(), this.fruitTree(RuneCraftoryBlocks.ORANGE_WOOD.get(), RuneCraftoryBlocks.ORANGE_LEAVES.get(), RuneCraftoryBlocks.ORANGE.get(), true));
        this.add(RuneCraftoryFeatures.GRAPE_1.location(), this.fruitSprout(RuneCraftoryBlocks.GRAPE_WOOD.get(), RuneCraftoryBlocks.GRAPE_LEAVES.get()));
        this.add(RuneCraftoryFeatures.GRAPE_2.location(), this.fruitTree(RuneCraftoryBlocks.GRAPE_WOOD.get(), RuneCraftoryBlocks.GRAPE_LEAVES.get(), RuneCraftoryBlocks.GRAPE.get(), false));
        this.add(RuneCraftoryFeatures.GRAPE_3.location(), this.fruitTree(RuneCraftoryBlocks.GRAPE_WOOD.get(), RuneCraftoryBlocks.GRAPE_LEAVES.get(), RuneCraftoryBlocks.GRAPE.get(), true));
    }

    public void add(ResourceLocation id, ConfiguredFeature<?, ?> feature) {
        this.contents.put(id, feature);
    }

    private ConfiguredFeature<FruitTreeSproutConfiguration, ?> fruitSprout(Block log, Block leave) {
        return new ConfiguredFeature<>(RuneCraftoryFeatures.FRUIT_SPROUT.get(), new FruitTreeSproutConfiguration(BlockStateProvider.simple(log.defaultBlockState().setValue(TreeLogBlock.IS_TREE_PART, true)), BlockStateProvider.simple(leave)));
    }

    private ConfiguredFeature<TreeConfiguration, ?> fruitTree(Block log, Block leave, Block fruit, boolean max) {
        return new ConfiguredFeature<>(Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(log.defaultBlockState().setValue(TreeLogBlock.IS_TREE_PART, true)),
                        new FruitTreeTrunkPlacer(max ? 3 : 1, 1, max ? 2 : 1, max ? 3 : 1),
                        BlockStateProvider.simple(leave),
                        new FancyFoliagePlacer(max ? ConstantInt.of(2) : ConstantInt.of(1), ConstantInt.of(0), max ? 3 : 2),
                        new TwoLayersFeatureSize(1, 0, 2))
                        .decorators(max ? List.of(new FruitLeaveDecorator(BlockStateProvider.simple(fruit), UniformInt.of(4, 9))) : List.of()).ignoreVines().build());
    }
}
