package io.github.flemmli97.runecraftory.common.events;

import com.google.common.collect.ImmutableList;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.blocks.BlockMineral;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModFeatures;
import io.github.flemmli97.runecraftory.common.world.features.config.BiomeFilteredConfig;
import io.github.flemmli97.runecraftory.common.world.features.config.ChancedBlockClusterConfig;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.CountOnEveryLayerPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class WorldRegistrationCalls {

    public static List<HerbFeatureEntry> defaultHerbEntries() {
        ImmutableList.Builder<HerbFeatureEntry> builder = new ImmutableList.Builder<>();
        builder.add(new HerbFeatureEntry(ModBlocks.WEEDS, 100));
        builder.add(new HerbFeatureEntry(ModBlocks.MUSHROOM, 40));
        builder.add(new HerbFeatureEntry(ModBlocks.MONARCH_MUSHROOM, 10));
        builder.add(new HerbFeatureEntry(ModBlocks.WITHERED_GRASS, 50));
        builder.add(new HerbFeatureEntry(ModBlocks.WHITE_GRASS, 30));
        builder.add(new HerbFeatureEntry(ModBlocks.INDIGO_GRASS, 30));
        builder.add(new HerbFeatureEntry(ModBlocks.PURPLE_GRASS, 30));
        builder.add(new HerbFeatureEntry(ModBlocks.GREEN_GRASS, 30));
        builder.add(new HerbFeatureEntry(ModBlocks.BLUE_GRASS, 30));
        builder.add(new HerbFeatureEntry(ModBlocks.YELLOW_GRASS, 30));
        builder.add(new HerbFeatureEntry(ModBlocks.RED_GRASS, 30));
        builder.add(new HerbFeatureEntry(ModBlocks.ORANGE_GRASS, 30));

        builder.add(new HerbFeatureEntry(ModBlocks.BLACK_GRASS, 75));
        builder.add(new HerbFeatureEntry(ModBlocks.ELLI_LEAVES, 10));
        builder.add(new HerbFeatureEntry(ModBlocks.ANTIDOTE_GRASS, 75));
        builder.add(new HerbFeatureEntry(ModBlocks.MEDICINAL_HERB, 75));
        builder.add(new HerbFeatureEntry(ModBlocks.BAMBOO_SPROUT, 66));
        return builder.build();
    }

    /**
     * On NeoForge biome modification is used which is datagenned
     * On Fabric datagen is not possible as BiomeModifications is used during init. Features are not created/registered there
     * This code is in common to make modifications easier
     *
     * @param placedFeatureHandler Placed features should use this in order to add the feature to biomes on respective loaders
     */
    public static void createFeatures(@Nullable FeatureRegister register,
                                      Consumer<FeatureBiomeModifier> placedFeatureHandler) {
        ResourceLocation herbs = ModFeatures.CONFIGRED_HERB_FEATURE.location();
        if (register != null) {
            List<HerbFeatureEntry> herbEntries = defaultHerbEntries();
            herbEntries.forEach(entry -> register.registerConfigured(entry.getId(),
                    provider -> new ConfiguredFeature<>(Feature.RANDOM_PATCH, new RandomPatchConfiguration(64, 8, 8,
                            Holder.direct(new PlacedFeature(Holder.direct(new ConfiguredFeature<>(Feature.SIMPLE_BLOCK,
                                    new SimpleBlockConfiguration(BlockStateProvider.simple(entry.block().get()))
                            )), List.of(BlockPredicateFilter.forPredicate(BlockPredicate.matchesTag(BlockTags.AIR)))))))));
            // Group all together
            register.registerConfigured(herbs,
                    provider -> {
                        List<BiomeFilteredConfig.BiomeFilteredEntry> filtered = herbEntries.stream().map(entry ->
                                new BiomeFilteredConfig.BiomeFilteredEntry(Holder.direct(
                                        new PlacedFeature(Holder.Reference.createStandAlone(provider.lookupOrThrow(Registries.CONFIGURED_FEATURE),
                                                ResourceKey.create(Registries.CONFIGURED_FEATURE, entry.getId())), List.of())),
                                        RunecraftoryTags.Biomes.getBlockBasedGenerationTag(entry.block(), true),
                                        RunecraftoryTags.Biomes.getBlockBasedGenerationTag(entry.block(), false), entry.weight())).toList();
                        return new ConfiguredFeature<>(ModFeatures.BIOME_FILTERED_RANDOM_FEATURES.get(), new BiomeFilteredConfig(filtered));
                    });
            register.registerPlaced(herbs,
                    (provider, feat) -> new PlacedFeature(feat, List.of(RarityFilter.onAverageOnceEvery(4),
                            InSquarePlacement.spread(),
                            PlacementUtils.HEIGHTMAP)));
        }
        placedFeatureHandler.accept(FeatureBiomeModifier.of(herbs));
        List<FeatureBiomeModifier> placedFeatures = new ArrayList<>();
        placedFeatures.addAll(registerMineralFeatures(register, ModBlocks.MINERAL_IRON, 15, 2, 5));
        placedFeatures.addAll(registerMineralFeatures(register, ModBlocks.MINERAL_TIN, 20, 2, 4));
        placedFeatures.addAll(registerMineralFeatures(register, ModBlocks.MINERAL_SILVER, 40, 2, 3));
        placedFeatures.addAll(registerMineralFeatures(register, ModBlocks.MINERAL_GOLD, 60, 2, 3));
        placedFeatures.addAll(registerMineralFeatures(register, ModBlocks.MINERAL_PLATINUM, 100, 1, 3));
        placedFeatures.addAll(registerMineralFeatures(register, ModBlocks.MINERAL_ORICHALCUM, 175, 1, 3));
        placedFeatures.addAll(registerMineralFeatures(register, ModBlocks.MINERAL_DIAMOND, 133, 1, 3));
        placedFeatures.addAll(registerMineralFeatures(register, ModBlocks.MINERAL_DRAGONIC, 25, 1, 2));
        placedFeatures.addAll(registerMineralFeatures(register, ModBlocks.MINERAL_AQUAMARINE, 25, 2, 3));
        placedFeatures.addAll(registerMineralFeatures(register, ModBlocks.MINERAL_AMETHYST, 66, 2, 3));
        placedFeatures.addAll(registerMineralFeatures(register, ModBlocks.MINERAL_RUBY, 50, 2, 3));
        placedFeatures.addAll(registerMineralFeatures(register, ModBlocks.MINERAL_EMERALD, 66, 1, 3));
        placedFeatures.addAll(registerMineralFeatures(register, ModBlocks.MINERAL_SAPPHIRE, 66, 2, 3));
        placedFeatures.forEach(placedFeatureHandler);
    }

    private static List<FeatureBiomeModifier> registerMineralFeatures(@Nullable FeatureRegister register, RegistryEntrySupplier<Block, ? extends BlockMineral> block,
                                                                      int chance, int min, int max) {
        ResourceLocation id = RuneCraftory.modRes("mineral_" + block.getID().getPath().replace("ore_", ""));
        ResourceLocation netherID = ResourceLocation.fromNamespaceAndPath(id.getNamespace(), id.getPath() + "_nether");
        if (register != null) {
            register.registerConfigured(id, provider -> {
                BlockMineral mineral = block.get();
                return new ConfiguredFeature<>(ModFeatures.MINERAL_FEATURE.get(),
                        new ChancedBlockClusterConfig(mineral,
                                RunecraftoryTags.Biomes.getMineralGenTag(mineral.tier, true), RunecraftoryTags.Biomes.getMineralGenTag(mineral.tier, false),
                                UniformInt.of(min, max), 4, 32));
            });
            register.registerPlaced(id, (provider, feat) -> new PlacedFeature(feat, List.of(
                    RarityFilter.onAverageOnceEvery(chance),
                    InSquarePlacement.spread(),
                    PlacementUtils.RANGE_4_4
            )));
            register.registerPlaced(netherID, id, (provider, feat) -> new PlacedFeature(feat, List.of(
                    CountOnEveryLayerPlacement.of(5),
                    RarityFilter.onAverageOnceEvery(chance),
                    InSquarePlacement.spread()
            )));
        }
        return List.of(FeatureBiomeModifier.of(id),
                new FeatureBiomeModifier(BiomeTags.IS_NETHER, GenerationStep.Decoration.VEGETAL_DECORATION, netherID));
    }

    public static MobSpawnSettings.SpawnerData gateSetting() {
        return new MobSpawnSettings.SpawnerData(ModEntities.GATE.get(), 100, 1, 1);
    }

    public interface FeatureRegister {

        void registerConfigured(ResourceLocation id, Function<HolderLookup.Provider, ConfiguredFeature<?, ?>> feature);

        default void registerPlaced(ResourceLocation id, BiFunction<HolderLookup.Provider, Holder<ConfiguredFeature<?, ?>>, PlacedFeature> placed) {
            this.registerPlaced(id, id, placed);
        }

        void registerPlaced(ResourceLocation id, ResourceLocation configuredID, BiFunction<HolderLookup.Provider, Holder<ConfiguredFeature<?, ?>>, PlacedFeature> placed);
    }

    public record FeatureBiomeModifier(TagKey<Biome> tag, GenerationStep.Decoration decoration,
                                       ResourceLocation placedFeature) {

        public static FeatureBiomeModifier of(ResourceLocation id) {
            return new FeatureBiomeModifier(RunecraftoryTags.Biomes.VANILLA_DIMENSIONS, GenerationStep.Decoration.VEGETAL_DECORATION, id);
        }
    }

    public record HerbFeatureEntry(RegistryEntrySupplier<Block, ?> block, int weight) {

        public ResourceLocation getId() {
            return RuneCraftory.modRes(this.block.getID().getPath());
        }
    }
}
