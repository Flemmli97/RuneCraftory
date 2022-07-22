package io.github.flemmli97.runecraftory.forge.data.worldgen;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.PairCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Map;

public class Decoder {

    public record StructureSetData(
            List<Pair<ResourceLocation, Integer>> structures, StructurePlacement placement) {

        public static final Codec<StructureSetData> CODEC = RecordCodecBuilder.create((instance) ->
                instance.group(new PairCodec<>(ResourceLocation.CODEC.fieldOf("structure").codec(), ExtraCodecs.POSITIVE_INT.fieldOf("weight").codec()).listOf().fieldOf("structures").forGetter(StructureSetData::structures),
                        StructurePlacement.CODEC.fieldOf("placement").forGetter(StructureSetData::placement)).apply(instance, StructureSetData::new));

        public StructureSetData(Pair<ResourceLocation, Integer> pair, StructurePlacement placement) {
            this(List.of(pair), placement);
        }

        public StructureSetData(List<Pair<ResourceLocation, Integer>> structures, StructurePlacement placement) {
            this.structures = structures;
            this.placement = placement;
        }

    }

    public record TemplatePoolData(ResourceLocation name,
                                   ResourceLocation fallback,
                                   List<Pair<StructurePoolElementData, Integer>> rawTemplates) {

        public static final Codec<TemplatePoolData> CODEC = RecordCodecBuilder.create((instance) -> instance.group(ResourceLocation.CODEC.fieldOf("name").forGetter(d -> d.name),
                        ResourceLocation.CODEC.fieldOf("fallback").forGetter(d -> d.fallback),
                        Codec.mapPair(StructurePoolElementData.CODEC.fieldOf("element"), Codec.intRange(1, 150).fieldOf("weight")).codec().listOf().fieldOf("elements").forGetter((arg) -> arg.rawTemplates))
                .apply(instance, TemplatePoolData::new));

    }

    public static class StructurePoolElementData {

        protected final StructurePoolElementType<?> type;
        public static final Codec<StructurePoolElementData> CODEC = Registry.STRUCTURE_POOL_ELEMENT.byNameCodec()
                .dispatch("element_type", d -> d.type, StructurePoolElementData::of);
        protected final StructureTemplatePool.Projection projection;
        private static final Map<StructurePoolElementType<?>, Codec<StructurePoolElementData>> CODECS = Map.of(
                StructurePoolElementType.EMPTY, Codec.unit(StructurePoolElementData::new),
                StructurePoolElementType.SINGLE, RecordCodecBuilder.create((instance) ->
                        instance.group(ResourceLocation.CODEC.fieldOf("location").forGetter(d -> d.val),
                                StructurePoolElementData.processorsCodec(),
                                StructurePoolElementData.projectionCodec()).apply(instance, (l, proc, proj) -> new StructurePoolElementData(StructurePoolElementType.SINGLE, l, proc, proj))),
                StructurePoolElementType.LEGACY, RecordCodecBuilder.create((instance) ->
                        instance.group(ResourceLocation.CODEC.fieldOf("location").forGetter(d -> d.val),
                                StructurePoolElementData.processorsCodec(),
                                StructurePoolElementData.projectionCodec()).apply(instance, (l, proc, proj) -> new StructurePoolElementData(StructurePoolElementType.LEGACY, l, proc, proj))),
                StructurePoolElementType.FEATURE, RecordCodecBuilder.create((instance) ->
                        instance.group(ResourceLocation.CODEC.fieldOf("feature").forGetter(d -> d.val),
                                StructurePoolElementData.processorsCodec(),
                                StructurePoolElementData.projectionCodec()).apply(instance, (l, proc, proj) -> new StructurePoolElementData(StructurePoolElementType.FEATURE, l, proc, proj))),
                StructurePoolElementType.LIST, RecordCodecBuilder.create((instance) ->
                        instance.group(CODEC.listOf().fieldOf("elements").forGetter(arg -> arg.list)).apply(instance, StructurePoolElementData::new)));
        protected final ResourceLocation val;
        protected final ResourceLocation processors;
        protected final List<StructurePoolElementData> list;

        public StructurePoolElementData() {
            this.type = StructurePoolElementType.EMPTY;
            this.projection = null;
            this.val = null;
            this.list = null;
            this.processors = null;
        }

        public StructurePoolElementData(ResourceLocation val, ResourceLocation processors, StructureTemplatePool.Projection projection) {
            this.type = StructurePoolElementType.SINGLE;
            this.val = val;
            this.projection = projection;
            this.list = null;
            this.processors = processors;
        }

        public StructurePoolElementData(StructurePoolElementType<?> type, ResourceLocation val, ResourceLocation processors, StructureTemplatePool.Projection projection) {
            this.type = type;
            this.val = val;
            this.projection = projection;
            this.list = null;
            this.processors = processors;
        }

        public StructurePoolElementData(List<StructurePoolElementData> list) {
            this.type = StructurePoolElementType.LIST;
            this.list = list;
            this.projection = null;
            this.val = null;
            this.processors = null;
        }

        private static Codec<StructurePoolElementData> of(StructurePoolElementType<?> type) {
            return CODECS.get(type);
        }

        private static <E extends StructurePoolElementData> RecordCodecBuilder<E, ResourceLocation> processorsCodec() {
            return ResourceLocation.CODEC.fieldOf("processors").forGetter(d -> d.processors);
        }

        private static <E extends StructurePoolElementData> RecordCodecBuilder<E, StructureTemplatePool.Projection> projectionCodec() {
            return StructureTemplatePool.Projection.CODEC.fieldOf("projection").forGetter(d -> d.projection);
        }


    }

    public static class ConfiguredJigsawStructureFeatureData {

        public static final Codec<ConfiguredJigsawStructureFeatureData> CODEC = ForgeRegistries.STRUCTURE_FEATURES.getCodec()
                .dispatch(d -> d.feature, f -> RecordCodecBuilder.create((instance) -> instance.group(
                                Codec.mapPair(ResourceLocation.CODEC.fieldOf("start_pool"), Codec.intRange(0, 7).fieldOf("size")).codec().fieldOf("config").forGetter((arg) -> arg.start),
                                Codec.STRING.fieldOf("biomes").forGetter(d -> "#" + d.biomes.location()),
                                Codec.BOOL.optionalFieldOf("adapt_noise", false).forGetter((arg) -> arg.adaptNoise),
                                Codec.simpleMap(MobCategory.CODEC, StructureSpawnOverride.CODEC, StringRepresentable.keys(MobCategory.values())).fieldOf("spawn_overrides").forGetter((arg) -> arg.spawnOverrides))
                        .apply(instance, (start, biomes, noise, override) -> new ConfiguredJigsawStructureFeatureData(f, start.getFirst(), start.getSecond(), TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(biomes)), override, noise))));

        protected final StructureFeature<?> feature;
        protected final Pair<ResourceLocation, Integer> start;
        protected final TagKey<Biome> biomes;
        protected final Map<MobCategory, StructureSpawnOverride> spawnOverrides;
        protected final boolean adaptNoise;

        public ConfiguredJigsawStructureFeatureData(StructureFeature<?> feature,
                                                    ResourceLocation startPool, int maxDepth,
                                                    TagKey<Biome> biomes,
                                                    Map<MobCategory, StructureSpawnOverride> spawnOverrides, boolean adaptNoise) {
            this.feature = feature;
            this.start = Pair.of(startPool, maxDepth);
            this.biomes = biomes;
            this.spawnOverrides = spawnOverrides;
            this.adaptNoise = adaptNoise;
        }

        public ConfiguredJigsawStructureFeatureData(StructureFeature<?> feature,
                                                    ResourceLocation startPool, int maxDepth,
                                                    TagKey<Biome> biomes,
                                                    Map<MobCategory, StructureSpawnOverride> spawnOverrides) {
            this(feature, startPool, maxDepth, biomes, spawnOverrides, false);
        }

        public ConfiguredJigsawStructureFeatureData(StructureFeature<?> feature,
                                                    ResourceLocation startPool, int maxDepth,
                                                    TagKey<Biome> biomes) {
            this(feature, startPool, maxDepth, biomes, Map.of(), false);
        }
    }
}
