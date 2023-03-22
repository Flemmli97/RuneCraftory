package io.github.flemmli97.runecraftory.api.datapack;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.biome.Biome;

import java.util.HashMap;
import java.util.Map;

public record GateSpawnData(ResourceLocation entity, int minDistanceFromSpawn,
                            int minGateLevel,
                            Map<TagKey<Biome>, Integer> biomes,
                            Map<ResourceLocation, Integer> structures) {

    public static final Codec<GateSpawnData> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.unboundedMap(TagKey.codec(Registry.BIOME_REGISTRY), ExtraCodecs.POSITIVE_INT).fieldOf("biomes").forGetter(GateSpawnData::biomes),
            Codec.unboundedMap(ResourceLocation.CODEC, ExtraCodecs.POSITIVE_INT).fieldOf("structures").forGetter(GateSpawnData::structures),
            ResourceLocation.CODEC.fieldOf("entity").forGetter(GateSpawnData::entity),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("min_distance_from_spawn").orElse(0).forGetter(GateSpawnData::minDistanceFromSpawn),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("min_gate_level").orElse(0).forGetter(GateSpawnData::minGateLevel)
    ).apply(instance, (biomes, structures, entity, dist, lvl) -> new GateSpawnData(entity, dist, lvl, biomes, structures)));

    public static class Builder {

        private final Map<TagKey<Biome>, Integer> biomes = new HashMap<>();
        private final Map<ResourceLocation, Integer> structures = new HashMap<>();
        private final int minDistanceFromSpawn, minGateLevel;

        public Builder(int minDistanceFromSpawn, int minGateLevel) {
            this.minDistanceFromSpawn = minDistanceFromSpawn;
            this.minGateLevel = minGateLevel;
        }

        @SafeVarargs
        public final GateSpawnData.Builder addToBiomeTag(int weight, TagKey<Biome>... biomes) {
            for (TagKey<Biome> tag : biomes)
                this.biomes.put(tag, weight);
            return this;
        }

        public GateSpawnData.Builder addToStructures(int weight, ResourceLocation... structures) {
            for (ResourceLocation s : structures)
                this.structures.put(s, weight);
            return this;
        }

        public GateSpawnData build(ResourceLocation name) {
            return new GateSpawnData(name, this.minDistanceFromSpawn, this.minGateLevel, this.biomes, this.structures);
        }
    }
}
