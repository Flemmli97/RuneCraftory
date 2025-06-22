package io.github.flemmli97.runecraftory.common.events;

import com.google.common.collect.ImmutableList;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModFeatures;
import io.github.flemmli97.runecraftory.common.world.features.config.ChancedBlockClusterConfig;
import io.github.flemmli97.runecraftory.common.world.features.config.HerbFeatureConfig;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.CountOnEveryLayerPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class WorldRegistrationCalls {

    public static List<HerbFeatureConfig.HerbEntry> defaultHerbEntries(HolderLookup.Provider provider) {
        ImmutableList.Builder<HerbFeatureConfig.HerbEntry> builder = new ImmutableList.Builder<>();
        builder.add(new HerbFeatureConfig.HerbEntry(ModBlocks.WEEDS.get(), provider, null, RunecraftoryTags.Biomes.WATER_NETHER_END, 100));
        builder.add(new HerbFeatureConfig.HerbEntry(ModBlocks.MUSHROOM.get(), provider, RunecraftoryTags.Biomes.MUSHROOM_GEN, RunecraftoryTags.Biomes.WATER_NETHER_END, 40));
        builder.add(new HerbFeatureConfig.HerbEntry(ModBlocks.MONARCH_MUSHROOM.get(), provider, RunecraftoryTags.Biomes.MUSHROOM_GEN, RunecraftoryTags.Biomes.WATER_NETHER_END, 10));
        builder.add(new HerbFeatureConfig.HerbEntry(ModBlocks.WITHERED_GRASS.get(), provider, null, RunecraftoryTags.Biomes.WATER_NETHER_END, 50));
        builder.add(new HerbFeatureConfig.HerbEntry(ModBlocks.WHITE_GRASS.get(), provider, RunecraftoryTags.Biomes.IS_SNOWY, RunecraftoryTags.Biomes.WATER_NETHER_END, 30));
        builder.add(new HerbFeatureConfig.HerbEntry(ModBlocks.INDIGO_GRASS.get(), provider, RunecraftoryTags.Biomes.INDIGO_GEN, RunecraftoryTags.Biomes.WATER_NETHER_END, 30));
        builder.add(new HerbFeatureConfig.HerbEntry(ModBlocks.PURPLE_GRASS.get(), provider, RunecraftoryTags.Biomes.PURPLE_GEN, RunecraftoryTags.Biomes.WATER_NETHER_END, 30));
        builder.add(new HerbFeatureConfig.HerbEntry(ModBlocks.GREEN_GRASS.get(), provider, RunecraftoryTags.Biomes.GENERAL_HERBS, RunecraftoryTags.Biomes.WATER_NETHER_END, 30));
        builder.add(new HerbFeatureConfig.HerbEntry(ModBlocks.BLUE_GRASS.get(), provider, RunecraftoryTags.Biomes.BLUE_GEN, RunecraftoryTags.Biomes.NETHER_END, 30));
        builder.add(new HerbFeatureConfig.HerbEntry(ModBlocks.YELLOW_GRASS.get(), provider, RunecraftoryTags.Biomes.YELLOW_GEN, RunecraftoryTags.Biomes.WATER_END, 30));
        builder.add(new HerbFeatureConfig.HerbEntry(ModBlocks.RED_GRASS.get(), provider, BiomeTags.IS_NETHER, null, 30));
        builder.add(new HerbFeatureConfig.HerbEntry(ModBlocks.ORANGE_GRASS.get(), provider, RunecraftoryTags.Biomes.ORANGE_GEN, RunecraftoryTags.Biomes.WATER_END, 30));
        builder.add(new HerbFeatureConfig.HerbEntry(ModBlocks.BLACK_GRASS.get(), provider, BiomeTags.IS_END, null, 75));
        builder.add(new HerbFeatureConfig.HerbEntry(ModBlocks.ELLI_LEAVES.get(), provider, BiomeTags.IS_END, null, 10));
        builder.add(new HerbFeatureConfig.HerbEntry(ModBlocks.ANTIDOTE_GRASS.get(), provider, RunecraftoryTags.Biomes.GENERAL_HERBS, RunecraftoryTags.Biomes.WATER_NETHER_END, 75));
        builder.add(new HerbFeatureConfig.HerbEntry(ModBlocks.MEDICINAL_HERB.get(), provider, RunecraftoryTags.Biomes.GENERAL_HERBS, RunecraftoryTags.Biomes.WATER_NETHER_END, 75));
        builder.add(new HerbFeatureConfig.HerbEntry(ModBlocks.BAMBOO_SPROUT.get(), provider, RunecraftoryTags.Biomes.BAMBOO_GEN, RunecraftoryTags.Biomes.WATER_NETHER_END, 66));
        return builder.build();
    }

    /**
     * On NeoForge biome modification is used which is datagenned
     * On Fabric datagen is not possible as BiomeModifications is used during init. Features are not created/registered there
     * This code is in common to make modifications easier
     */
    public static void createFeatures(@Nullable FeatureRegister register,
                                      Consumer<FeatureBiomeModifier> placedFeatureHandler) {
        ResourceLocation herbs = RuneCraftory.modRes("herb_feature");
        if (register != null) {
            register.registerConfigured(herbs,
                    provider -> new ConfiguredFeature<>(ModFeatures.HERB_FEATURE.get(),
                            new HerbFeatureConfig(70, 8, 9, defaultHerbEntries(provider))));
            register.registerPlaced(herbs,
                    (provider, feat) -> new PlacedFeature(feat, List.of(RarityFilter.onAverageOnceEvery(4),
                            InSquarePlacement.spread(),
                            PlacementUtils.HEIGHTMAP)));
        }
        placedFeatureHandler.accept(FeatureBiomeModifier.of(herbs));
        List<FeatureBiomeModifier> placedFeatures = new ArrayList<>();
        placedFeatures.addAll(registerMineralFeatures(register, ModBlocks.MINERAL_IRON, null, RunecraftoryTags.Biomes.WATER_NETHER_END, 15, 2, 5));
        placedFeatures.addAll(registerMineralFeatures(register, ModBlocks.MINERAL_TIN, null, RunecraftoryTags.Biomes.WATER_NETHER_END, 20, 2, 4));
        placedFeatures.addAll(registerMineralFeatures(register, ModBlocks.MINERAL_SILVER, null, RunecraftoryTags.Biomes.WATER_NETHER_END, 40, 2, 3));
        placedFeatures.addAll(registerMineralFeatures(register, ModBlocks.MINERAL_GOLD, null, RunecraftoryTags.Biomes.WATER_NETHER_END, 60, 2, 3));
        placedFeatures.addAll(registerMineralFeatures(register, ModBlocks.MINERAL_PLATINUM, null, RunecraftoryTags.Biomes.WATER_NETHER_END, 100, 1, 3));
        placedFeatures.addAll(registerMineralFeatures(register, ModBlocks.MINERAL_ORICHALCUM, null, RunecraftoryTags.Biomes.WATER_NETHER_END, 175, 1, 3));
        placedFeatures.addAll(registerMineralFeatures(register, ModBlocks.MINERAL_DIAMOND, null, RunecraftoryTags.Biomes.WATER_NETHER_END, 133, 1, 3));
        placedFeatures.addAll(registerMineralFeatures(register, ModBlocks.MINERAL_DRAGONIC, BiomeTags.IS_END, null, 25, 1, 2));
        placedFeatures.addAll(registerMineralFeatures(register, ModBlocks.MINERAL_AQUAMARINE, RunecraftoryTags.Biomes.AQUAMARINE_GEN, RunecraftoryTags.Biomes.NETHER_END, 25, 2, 3));
        placedFeatures.addAll(registerMineralFeatures(register, ModBlocks.MINERAL_AMETHYST, RunecraftoryTags.Biomes.AMETHYST_GEN, RunecraftoryTags.Biomes.WATER_NETHER_END, 66, 2, 3));
        placedFeatures.addAll(registerMineralFeatures(register, ModBlocks.MINERAL_RUBY, RunecraftoryTags.Biomes.RUBY_GEN, null, 50, 2, 3));
        placedFeatures.addAll(registerMineralFeatures(register, ModBlocks.MINERAL_EMERALD, RunecraftoryTags.Biomes.EMERALD_GEN, RunecraftoryTags.Biomes.WATER_NETHER_END, 66, 1, 3));
        placedFeatures.addAll(registerMineralFeatures(register, ModBlocks.MINERAL_SAPPHIRE, RunecraftoryTags.Biomes.SAPPHIRE_GEN, RunecraftoryTags.Biomes.WATER_NETHER_END, 66, 2, 3));
        placedFeatures.forEach(placedFeatureHandler);
    }

    private static List<FeatureBiomeModifier> registerMineralFeatures(@Nullable FeatureRegister register, RegistryEntrySupplier<Block, ?> block, TagKey<Biome> whitelist, TagKey<Biome> blacklist, int chance, int min, int max) {
        ResourceLocation id = RuneCraftory.modRes("mineral_feature_" + block.getID().getPath().replace("ore_", ""));
        ResourceLocation netherID = ResourceLocation.fromNamespaceAndPath(id.getNamespace(), id.getPath() + "_nether");
        if (register != null) {
            register.registerConfigured(id, provider -> new ConfiguredFeature<>(ModFeatures.MINERAL_FEATURE.get(),
                    new ChancedBlockClusterConfig(block.get(), provider, whitelist, blacklist, UniformInt.of(min, max), 3, 64)));
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
                new FeatureBiomeModifier(Optional.of(BiomeTags.IS_NETHER), GenerationStep.Decoration.VEGETAL_DECORATION, netherID));
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

    public record FeatureBiomeModifier(Optional<TagKey<Biome>> tag, GenerationStep.Decoration decoration,
                                       ResourceLocation placedFeature) {

        public static FeatureBiomeModifier of(ResourceLocation id) {
            return new FeatureBiomeModifier(Optional.empty(), GenerationStep.Decoration.VEGETAL_DECORATION, id);
        }
    }
}
