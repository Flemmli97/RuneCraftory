package io.github.flemmli97.runecraftory.neoforge.data.worldgen.features;

import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.common.registry.ModFeatures;
import io.github.flemmli97.runecraftory.common.world.features.trees.FruitLeaveDecorator;
import io.github.flemmli97.runecraftory.common.world.features.trees.FruitTreeSproutConfiguration;
import io.github.flemmli97.runecraftory.common.world.features.trees.FruitTreeTrunkPlacer;
import io.github.flemmli97.tenshilib.common.data.provider.CodecBasedProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
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
        this.add(ModFeatures.APPLE_1.location(), this.fruitSprout(ModBlocks.APPLE_WOOD.get(), ModBlocks.APPLE_LEAVES.get()));
        this.add(ModFeatures.APPLE_2.location(), this.fruitTree(ModBlocks.APPLE_WOOD.get(), ModBlocks.APPLE_LEAVES.get(), ModBlocks.APPLE.get(), false));
        this.add(ModFeatures.APPLE_3.location(), this.fruitTree(ModBlocks.APPLE_WOOD.get(), ModBlocks.APPLE_LEAVES.get(), ModBlocks.APPLE.get(), true));
        this.add(ModFeatures.ORANGE_1.location(), this.fruitSprout(ModBlocks.ORANGE_WOOD.get(), ModBlocks.ORANGE_LEAVES.get()));
        this.add(ModFeatures.ORANGE_2.location(), this.fruitTree(ModBlocks.ORANGE_WOOD.get(), ModBlocks.ORANGE_LEAVES.get(), ModBlocks.ORANGE.get(), false));
        this.add(ModFeatures.ORANGE_3.location(), this.fruitTree(ModBlocks.ORANGE_WOOD.get(), ModBlocks.ORANGE_LEAVES.get(), ModBlocks.ORANGE.get(), true));
        this.add(ModFeatures.GRAPE_1.location(), this.fruitSprout(ModBlocks.GRAPE_WOOD.get(), ModBlocks.GRAPE_LEAVES.get()));
        this.add(ModFeatures.GRAPE_2.location(), this.fruitTree(ModBlocks.GRAPE_WOOD.get(), ModBlocks.GRAPE_LEAVES.get(), ModBlocks.GRAPE.get(), false));
        this.add(ModFeatures.GRAPE_3.location(), this.fruitTree(ModBlocks.GRAPE_WOOD.get(), ModBlocks.GRAPE_LEAVES.get(), ModBlocks.GRAPE.get(), true));
    }

    public void add(ResourceLocation id, ConfiguredFeature<?, ?> feature) {
        this.contents.put(id, feature);
    }

    private ConfiguredFeature<FruitTreeSproutConfiguration, ?> fruitSprout(Block log, Block leave) {
        return new ConfiguredFeature<>(ModFeatures.FRUIT_SPROUT.get(), new FruitTreeSproutConfiguration(BlockStateProvider.simple(log), BlockStateProvider.simple(leave)));
    }

    private ConfiguredFeature<TreeConfiguration, ?> fruitTree(Block log, Block leave, Block fruit, boolean max) {
        return new ConfiguredFeature<>(Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(log),
                        new FruitTreeTrunkPlacer(max ? 3 : 1, 1, max ? 2 : 1, max ? 3 : 1),
                        BlockStateProvider.simple(leave),
                        new FancyFoliagePlacer(max ? ConstantInt.of(2) : ConstantInt.of(1), ConstantInt.of(0), max ? 3 : 2),
                        new TwoLayersFeatureSize(1, 0, 2))
                        .decorators(max ? List.of(new FruitLeaveDecorator(BlockStateProvider.simple(fruit))) : List.of()).ignoreVines().build());
    }
}
