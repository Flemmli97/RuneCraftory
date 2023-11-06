package io.github.flemmli97.runecraftory.common.registry;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.world.features.ChancedBlockClusterConfig;
import io.github.flemmli97.runecraftory.common.world.features.HerbFeature;
import io.github.flemmli97.runecraftory.common.world.features.HerbFeatureConfig;
import io.github.flemmli97.runecraftory.common.world.features.MineralFeature;
import io.github.flemmli97.runecraftory.common.world.features.trees.FruitLeaveDecorator;
import io.github.flemmli97.runecraftory.common.world.features.trees.FruitTreeSproutConfiguration;
import io.github.flemmli97.runecraftory.common.world.features.trees.FruitTreeSproutFeature;
import io.github.flemmli97.runecraftory.common.world.features.trees.FruitTreeTrunkPlacer;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import io.github.flemmli97.tenshilib.platform.registry.PlatformRegistry;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.RandomSpreadFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraft.world.level.levelgen.placement.CountOnEveryLayerPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class ModFeatures {

    public static final PlatformRegistry<Feature<?>> FEATURES = PlatformUtils.INSTANCE.of(Registry.FEATURE_REGISTRY, RuneCraftory.MODID);
    public static final PlatformRegistry<TrunkPlacerType<?>> TRUNK_PLACER = PlatformUtils.INSTANCE.of(Registry.TRUNK_PLACER_TYPE_REGISTRY, RuneCraftory.MODID);
    public static final PlatformRegistry<TreeDecoratorType<?>> TREE_DECORATORS = PlatformUtils.INSTANCE.of(Registry.TREE_DECORATOR_TYPE_REGISTRY, RuneCraftory.MODID);

    public static final RegistryEntrySupplier<MineralFeature> MINERALFEATURE = FEATURES.register("mineral_feature", () -> new MineralFeature(ChancedBlockClusterConfig.CODEC));

    public static final RegistryEntrySupplier<HerbFeature> HERBFEATURE = FEATURES.register("herb_feature", () -> new HerbFeature(HerbFeatureConfig.CODEC));
    public static final RegistryEntrySupplier<FruitTreeSproutFeature> FRUIT_SPROUT = FEATURES.register("fruit_tree_sprout", () -> new FruitTreeSproutFeature(FruitTreeSproutConfiguration.CODEC));

    public static final RegistryEntrySupplier<TrunkPlacerType<?>> FRUIT_TRUNK_PLACER = TRUNK_PLACER.register("fruit_tree_trunk", () -> createTrunkPlacerType(FruitTreeTrunkPlacer.CODEC));
    public static final RegistryEntrySupplier<TreeDecoratorType<?>> FRUIT_DECORATOR = TREE_DECORATORS.register("fruit_decorator", () -> createTreeDecoratorType(FruitLeaveDecorator.CODEC));

    public static Holder<ConfiguredFeature<?, ?>> APPLE_1;
    public static Holder<ConfiguredFeature<TreeConfiguration, ?>> APPLE_2;
    public static Holder<ConfiguredFeature<TreeConfiguration, ?>> APPLE_3;

    public static Holder<PlacedFeature> PLACEDHERBFEATURE;
    public static Holder<PlacedFeature> PLACEDNETHERHERBFEATURE;
    public static Holder<PlacedFeature> PLACEDENDHERBFEATURE;
    public static List<Holder<PlacedFeature>> PLACEDMINERALFEATURES;
    public static List<Holder<PlacedFeature>> PLACEDNETHERMINERALFEATURES;

    public static void registerConfiguredFeatures() {
        Holder<ConfiguredFeature<?, ?>> CONFIGUREDHERBFEATURE = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, "configured_herb_feature", new ConfiguredFeature<>(HERBFEATURE.get(),
                new HerbFeatureConfig(70, 8, 9, build())));
        PLACEDHERBFEATURE = BuiltinRegistries.register(BuiltinRegistries.PLACED_FEATURE, "placed_herb_feature", new PlacedFeature(CONFIGUREDHERBFEATURE, List.of(
                RarityFilter.onAverageOnceEvery(5),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP
        )));
        PLACEDNETHERHERBFEATURE = BuiltinRegistries.register(BuiltinRegistries.PLACED_FEATURE, "placed_nether_herb_feature", new PlacedFeature(CONFIGUREDHERBFEATURE, List.of(
                CountOnEveryLayerPlacement.of(6),
                RarityFilter.onAverageOnceEvery(8)
        )));
        PLACEDENDHERBFEATURE = BuiltinRegistries.register(BuiltinRegistries.PLACED_FEATURE, "placed_end_herb_feature", new PlacedFeature(CONFIGUREDHERBFEATURE, List.of(
                RarityFilter.onAverageOnceEvery(3),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP
        )));
        ImmutableList.Builder<Holder<PlacedFeature>> builder = new ImmutableList.Builder<>();
        ImmutableList.Builder<Holder<PlacedFeature>> nether = new ImmutableList.Builder<>();
        registerMineralFeatures(builder, nether, ModBlocks.mineralIron, null, ModTags.WATER_NETHER_END, 15, 2, 5);
        registerMineralFeatures(builder, nether, ModBlocks.mineralBronze, null, ModTags.WATER_NETHER_END, 20, 2, 4);
        registerMineralFeatures(builder, nether, ModBlocks.mineralSilver, null, ModTags.WATER_NETHER_END, 40, 2, 3);
        registerMineralFeatures(builder, nether, ModBlocks.mineralGold, null, ModTags.WATER_NETHER_END, 60, 2, 3);
        registerMineralFeatures(builder, nether, ModBlocks.mineralPlatinum, null, ModTags.WATER_NETHER_END, 100, 1, 3);
        registerMineralFeatures(builder, nether, ModBlocks.mineralOrichalcum, null, ModTags.WATER_NETHER_END, 175, 1, 3);
        registerMineralFeatures(builder, nether, ModBlocks.mineralDiamond, null, ModTags.WATER_NETHER_END, 133, 1, 3);
        registerMineralFeatures(builder, nether, ModBlocks.mineralDragonic, ModTags.IS_END, null, 25, 1, 2);
        registerMineralFeatures(builder, nether, ModBlocks.mineralAquamarine, ModTags.AQUAMARINE_GEN, ModTags.NETHER_END, 25, 2, 3);
        registerMineralFeatures(builder, nether, ModBlocks.mineralAmethyst, ModTags.AMETHYST_GEN, ModTags.WATER_NETHER_END, 66, 2, 3);
        registerMineralFeatures(builder, nether, ModBlocks.mineralRuby, ModTags.RUBY_GEN, null, 50, 2, 3);
        registerMineralFeatures(builder, nether, ModBlocks.mineralEmerald, ModTags.EMERALD_GEN, ModTags.WATER_NETHER_END, 66, 1, 3);
        registerMineralFeatures(builder, nether, ModBlocks.mineralSapphire, ModTags.SAPPHIRE_GEN, ModTags.WATER_NETHER_END, 66, 2, 3);
        PLACEDMINERALFEATURES = builder.build();
        PLACEDNETHERMINERALFEATURES = nether.build();

        APPLE_1 = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, RuneCraftory.MODID + ":apple_stage_1", fruitSprout(ModBlocks.appleWood.get(), ModBlocks.appleLeaves.get()));
        APPLE_2 = BuiltinRegistries.registerExact(BuiltinRegistries.CONFIGURED_FEATURE, RuneCraftory.MODID + ":apple_stage_2", fruitTree(ModBlocks.appleWood.get(), ModBlocks.appleLeaves.get(), ModBlocks.apple.get(), false));
        APPLE_3 = BuiltinRegistries.registerExact(BuiltinRegistries.CONFIGURED_FEATURE, RuneCraftory.MODID + ":apple_stage_3", fruitTree(ModBlocks.appleWood.get(), ModBlocks.appleLeaves.get(), ModBlocks.apple.get(), true));
    }

    public static List<HerbFeature.Entry> build() {
        ImmutableList.Builder<HerbFeature.Entry> builder = new ImmutableList.Builder<>();
        builder.add(new HerbFeature.Entry(ModBlocks.weeds.get(), null, ModTags.WATER_NETHER_END, 100));
        builder.add(new HerbFeature.Entry(ModBlocks.mushroom.get(), ModTags.MUSHROOM_GEN, ModTags.WATER_NETHER_END, 40));
        builder.add(new HerbFeature.Entry(ModBlocks.monarchMushroom.get(), ModTags.MUSHROOM_GEN, ModTags.WATER_NETHER_END, 10));
        builder.add(new HerbFeature.Entry(ModBlocks.witheredGrass.get(), null, ModTags.WATER_NETHER_END, 50));
        builder.add(new HerbFeature.Entry(ModBlocks.whiteGrass.get(), ModTags.IS_SNOWY, ModTags.WATER_NETHER_END, 30));
        builder.add(new HerbFeature.Entry(ModBlocks.indigoGrass.get(), ModTags.INDIGO_GEN, ModTags.WATER_NETHER_END, 30));
        builder.add(new HerbFeature.Entry(ModBlocks.purpleGrass.get(), ModTags.PURPLE_GEN, ModTags.WATER_NETHER_END, 30));
        builder.add(new HerbFeature.Entry(ModBlocks.greenGrass.get(), ModTags.GENERAL_HERBS, ModTags.WATER_NETHER_END, 30));
        builder.add(new HerbFeature.Entry(ModBlocks.blueGrass.get(), ModTags.BLUE_GEN, ModTags.NETHER_END, 30));
        builder.add(new HerbFeature.Entry(ModBlocks.yellowGrass.get(), ModTags.YELLOW_GEN, ModTags.WATER_END, 30));
        builder.add(new HerbFeature.Entry(ModBlocks.redGrass.get(), BiomeTags.IS_NETHER, null, 30));
        builder.add(new HerbFeature.Entry(ModBlocks.orangeGrass.get(), ModTags.ORANGE_GEN, ModTags.WATER_END, 30));
        builder.add(new HerbFeature.Entry(ModBlocks.blackGrass.get(), ModTags.IS_END, null, 75));
        builder.add(new HerbFeature.Entry(ModBlocks.elliLeaves.get(), ModTags.IS_END, null, 10));
        builder.add(new HerbFeature.Entry(ModBlocks.antidoteGrass.get(), ModTags.GENERAL_HERBS, ModTags.WATER_NETHER_END, 75));
        builder.add(new HerbFeature.Entry(ModBlocks.medicinalHerb.get(), ModTags.GENERAL_HERBS, ModTags.WATER_NETHER_END, 75));
        builder.add(new HerbFeature.Entry(ModBlocks.bambooSprout.get(), ModTags.BAMBOO_GEN, ModTags.WATER_NETHER_END, 66));
        return builder.build();
    }

    private static void registerMineralFeatures(ImmutableList.Builder<Holder<PlacedFeature>> builder, ImmutableList.Builder<Holder<PlacedFeature>> nether,
                                                RegistryEntrySupplier<Block> block, TagKey<Biome> whitelist, TagKey<Biome> blacklist, int chance, int min, int max) {
        Holder<ConfiguredFeature<?, ?>> CONFIGUREDMINERALFEATURE = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, RuneCraftory.MODID + ":configured_mineral_feature" + block.getID().getPath(), new ConfiguredFeature<>(MINERALFEATURE.get(),
                new ChancedBlockClusterConfig(BlockStateProvider.simple(block.get()), whitelist, blacklist, min, max, 3, 64)));
        builder.add(BuiltinRegistries.register(BuiltinRegistries.PLACED_FEATURE, RuneCraftory.MODID + ":placed_mineral_feature_" + block.getID().getPath(), new PlacedFeature(CONFIGUREDMINERALFEATURE, List.of(
                RarityFilter.onAverageOnceEvery(chance),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_TOP_SOLID
        ))));
        nether.add(BuiltinRegistries.register(BuiltinRegistries.PLACED_FEATURE, RuneCraftory.MODID + ":placed_nether_mineral_feature" + block.getID().getPath(), new PlacedFeature(CONFIGUREDMINERALFEATURE, List.of(
                CountOnEveryLayerPlacement.of(5),
                RarityFilter.onAverageOnceEvery(chance),
                InSquarePlacement.spread()
        ))));
    }

    private static ConfiguredFeature<FruitTreeSproutConfiguration, ?> fruitSprout(Block log, Block leave) {
        return new ConfiguredFeature<>(FRUIT_SPROUT.get(), new FruitTreeSproutConfiguration(BlockStateProvider.simple(log), BlockStateProvider.simple(leave)));
    }

    private static ConfiguredFeature<TreeConfiguration, ?> fruitTree(Block log, Block leave, Block fruit, boolean max) {
        return new ConfiguredFeature<>(Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(log),
                        new FruitTreeTrunkPlacer(max ? 3 : 1, 1, max ? 2 : 1, max ? 3 : 1),
                        BlockStateProvider.simple(leave),
                        new FancyFoliagePlacer(max ? ConstantInt.of(2) : ConstantInt.of(1), ConstantInt.of(0), max ? 3 : 2),
                        new TwoLayersFeatureSize(1, 0, 2))
                        .decorators(max ? List.of(new FruitLeaveDecorator(BlockStateProvider.simple(fruit))) : List.of()).ignoreVines().build());
    }

    @SuppressWarnings("rawtypes")
    private static TrunkPlacerType<?> createTrunkPlacerType(Codec<? extends TrunkPlacer> codec) {
        try {
            Constructor<TrunkPlacerType> cons = TrunkPlacerType.class.getDeclaredConstructor(Codec.class);
            cons.setAccessible(true);
            return cons.newInstance(codec);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("rawtypes")
    private static TreeDecoratorType<?> createTreeDecoratorType(Codec<? extends TreeDecorator> codec) {
        try {
            Constructor<TreeDecoratorType> cons = TreeDecoratorType.class.getDeclaredConstructor(Codec.class);
            cons.setAccessible(true);
            return cons.newInstance(codec);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
