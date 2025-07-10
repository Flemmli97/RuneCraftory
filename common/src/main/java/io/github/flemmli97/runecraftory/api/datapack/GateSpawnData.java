package io.github.flemmli97.runecraftory.api.datapack;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.biome.Biome;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public record GateSpawnData(ResourceLocation entity, int minDistanceFromSpawn,
                            int minGateLevel, boolean canSpawnInWater,
                            Map<TagKey<Biome>, Integer> biomes,
                            Map<ResourceLocation, Integer> structures,
                            Optional<EntityPredicate> gatePredicate, Optional<EntityPredicate> playerPredicate) {

    public static final Codec<GateSpawnData> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            ResourceLocation.CODEC.fieldOf("entity").forGetter(GateSpawnData::entity),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("min_distance_from_spawn").orElse(0).forGetter(GateSpawnData::minDistanceFromSpawn),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("min_gate_level").orElse(0).forGetter(GateSpawnData::minGateLevel),
            Codec.BOOL.fieldOf("allow_underwater").forGetter(GateSpawnData::canSpawnInWater),
            Codec.unboundedMap(TagKey.codec(Registries.BIOME), ExtraCodecs.POSITIVE_INT).fieldOf("biomes").forGetter(GateSpawnData::biomes),
            Codec.unboundedMap(ResourceLocation.CODEC, ExtraCodecs.POSITIVE_INT).fieldOf("structures").forGetter(GateSpawnData::structures),
            EntityPredicate.CODEC.optionalFieldOf("gate_predicate").forGetter(d -> d.gatePredicate),
            EntityPredicate.CODEC.optionalFieldOf("player_predicate").forGetter(d -> d.playerPredicate)
    ).apply(instance, GateSpawnData::new));

    public static class Builder {

        private final Map<TagKey<Biome>, Integer> biomes = new LinkedHashMap<>();
        private final Map<ResourceLocation, Integer> structures = new LinkedHashMap<>();
        private final int minDistanceFromSpawn, minGateLevel;
        private boolean allowUnderwater;
        private EntityPredicate gatePredicate, playerPredicate;

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

        public GateSpawnData.Builder canSpawnUnderwater() {
            this.allowUnderwater = true;
            return this;
        }

        public GateSpawnData.Builder withGatePredicate(EntityPredicate.Builder builder) {
            this.gatePredicate = builder.build();
            return this;
        }

        public GateSpawnData.Builder withPlayerPredicate(EntityPredicate.Builder builder) {
            this.playerPredicate = builder.build();
            return this;
        }

        public GateSpawnData build(ResourceLocation name) {
            return new GateSpawnData(name, this.minDistanceFromSpawn, this.minGateLevel, this.allowUnderwater, this.biomes, this.structures, Optional.ofNullable(this.gatePredicate), Optional.ofNullable(this.playerPredicate));
        }
    }
}
