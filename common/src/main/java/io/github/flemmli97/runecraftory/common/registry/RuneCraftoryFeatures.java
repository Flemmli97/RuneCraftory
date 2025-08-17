package io.github.flemmli97.runecraftory.common.registry;

import com.mojang.serialization.MapCodec;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.world.features.BiomeFilteredRandomFeature;
import io.github.flemmli97.runecraftory.common.world.features.MineralFeature;
import io.github.flemmli97.runecraftory.common.world.features.config.BiomeFilteredConfig;
import io.github.flemmli97.runecraftory.common.world.features.config.ChancedBlockClusterConfig;
import io.github.flemmli97.runecraftory.common.world.features.trees.FruitLeaveDecorator;
import io.github.flemmli97.runecraftory.common.world.features.trees.FruitTreeSproutConfiguration;
import io.github.flemmli97.runecraftory.common.world.features.trees.FruitTreeSproutFeature;
import io.github.flemmli97.runecraftory.common.world.features.trees.FruitTreeTrunkPlacer;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.registry.LoaderRegister;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class RuneCraftoryFeatures {

    public static final LoaderRegister<Feature<?>> FEATURES = LoaderRegistryAccess.INSTANCE.of(Registries.FEATURE, RuneCraftory.MODID);
    public static final LoaderRegister<TrunkPlacerType<?>> TRUNK_PLACER = LoaderRegistryAccess.INSTANCE.of(Registries.TRUNK_PLACER_TYPE, RuneCraftory.MODID);
    public static final LoaderRegister<TreeDecoratorType<?>> TREE_DECORATORS = LoaderRegistryAccess.INSTANCE.of(Registries.TREE_DECORATOR_TYPE, RuneCraftory.MODID);

    public static final RegistryEntrySupplier<Feature<?>, MineralFeature> MINERAL_FEATURE = FEATURES.register("mineral_feature", () -> new MineralFeature(ChancedBlockClusterConfig.CODEC));
    public static final RegistryEntrySupplier<Feature<?>, BiomeFilteredRandomFeature> BIOME_FILTERED_RANDOM_FEATURES = FEATURES.register("biome_filtered_random_features", () -> new BiomeFilteredRandomFeature(BiomeFilteredConfig.CODEC));

    public static final ResourceKey<ConfiguredFeature<?, ?>> CONFIGRED_HERB_FEATURE = ResourceKey.create(Registries.CONFIGURED_FEATURE, RuneCraftory.modRes("herb_feature"));
    public static final ResourceKey<PlacedFeature> HERB_FEATURE = ResourceKey.create(Registries.PLACED_FEATURE, CONFIGRED_HERB_FEATURE.location());
    public static final RegistryEntrySupplier<Feature<?>, FruitTreeSproutFeature> FRUIT_SPROUT = FEATURES.register("fruit_tree_sprout", () -> new FruitTreeSproutFeature(FruitTreeSproutConfiguration.CODEC));

    public static final RegistryEntrySupplier<TrunkPlacerType<?>, TrunkPlacerType<?>> FRUIT_TRUNK_PLACER = TRUNK_PLACER.register("fruit_tree_trunk", () -> createTrunkPlacerType(FruitTreeTrunkPlacer.CODEC));
    public static final RegistryEntrySupplier<TreeDecoratorType<?>, TreeDecoratorType<?>> FRUIT_DECORATOR = TREE_DECORATORS.register("fruit_decorator", () -> createTreeDecoratorType(FruitLeaveDecorator.CODEC));

    public static ResourceKey<ConfiguredFeature<?, ?>> APPLE_1 = ResourceKey.create(Registries.CONFIGURED_FEATURE, RuneCraftory.modRes("apple_stage_1"));
    public static ResourceKey<ConfiguredFeature<?, ?>> APPLE_2 = ResourceKey.create(Registries.CONFIGURED_FEATURE, RuneCraftory.modRes("apple_stage_2"));
    public static ResourceKey<ConfiguredFeature<?, ?>> APPLE_3 = ResourceKey.create(Registries.CONFIGURED_FEATURE, RuneCraftory.modRes("apple_stage_3"));
    public static ResourceKey<ConfiguredFeature<?, ?>> ORANGE_1 = ResourceKey.create(Registries.CONFIGURED_FEATURE, RuneCraftory.modRes("orange_stage_1"));
    public static ResourceKey<ConfiguredFeature<?, ?>> ORANGE_2 = ResourceKey.create(Registries.CONFIGURED_FEATURE, RuneCraftory.modRes("orange_stage_2"));
    public static ResourceKey<ConfiguredFeature<?, ?>> ORANGE_3 = ResourceKey.create(Registries.CONFIGURED_FEATURE, RuneCraftory.modRes("orange_stage_3"));
    public static ResourceKey<ConfiguredFeature<?, ?>> GRAPE_1 = ResourceKey.create(Registries.CONFIGURED_FEATURE, RuneCraftory.modRes("grape_stage_1"));
    public static ResourceKey<ConfiguredFeature<?, ?>> GRAPE_2 = ResourceKey.create(Registries.CONFIGURED_FEATURE, RuneCraftory.modRes("grape_stage_2"));
    public static ResourceKey<ConfiguredFeature<?, ?>> GRAPE_3 = ResourceKey.create(Registries.CONFIGURED_FEATURE, RuneCraftory.modRes("grape_stage_3"));

    public static ResourceKey<ConfiguredFeature<?, ?>> HOT_SPRING_LAKE = ResourceKey.create(Registries.CONFIGURED_FEATURE, RuneCraftory.modRes("hot_spring_lake"));
    public static ResourceKey<PlacedFeature> PLACED_HOT_SPRING_LAKE = ResourceKey.create(Registries.PLACED_FEATURE, HOT_SPRING_LAKE.location());

    @SuppressWarnings("rawtypes")
    private static TrunkPlacerType<?> createTrunkPlacerType(MapCodec<? extends TrunkPlacer> codec) {
        try {
            Constructor<TrunkPlacerType> cons = TrunkPlacerType.class.getDeclaredConstructor(MapCodec.class);
            cons.setAccessible(true);
            return cons.newInstance(codec);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            RuneCraftory.LOGGER.error(e);
        }
        return null;
    }

    @SuppressWarnings("rawtypes")
    private static TreeDecoratorType<?> createTreeDecoratorType(MapCodec<? extends TreeDecorator> codec) {
        try {
            Constructor<TreeDecoratorType> cons = TreeDecoratorType.class.getDeclaredConstructor(MapCodec.class);
            cons.setAccessible(true);
            return cons.newInstance(codec);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            RuneCraftory.LOGGER.error(e);
        }
        return null;
    }
}
