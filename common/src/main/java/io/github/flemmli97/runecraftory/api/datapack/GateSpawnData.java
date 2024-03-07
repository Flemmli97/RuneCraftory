package io.github.flemmli97.runecraftory.api.datapack;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.common.utils.CodecHelper;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.core.Registry;
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
                            EntityPredicate gatePredicate, EntityPredicate playerPredicate) {

    public static final Codec<GateSpawnData> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.unboundedMap(TagKey.codec(Registry.BIOME_REGISTRY), ExtraCodecs.POSITIVE_INT).fieldOf("biomes").forGetter(GateSpawnData::biomes),
            Codec.unboundedMap(ResourceLocation.CODEC, ExtraCodecs.POSITIVE_INT).fieldOf("structures").forGetter(GateSpawnData::structures),
            CodecHelper.ENTITY_PREDICATE_CODEC.optionalFieldOf("gatePredicate").forGetter(d -> Optional.ofNullable(d.gatePredicate == EntityPredicate.ANY ? null : d.gatePredicate)),
            CodecHelper.ENTITY_PREDICATE_CODEC.optionalFieldOf("playerPredicate").forGetter(d -> Optional.ofNullable(d.playerPredicate == EntityPredicate.ANY ? null : d.playerPredicate)),

            ResourceLocation.CODEC.fieldOf("entity").forGetter(GateSpawnData::entity),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("minDistanceFromSpawn").orElse(0).forGetter(GateSpawnData::minDistanceFromSpawn),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("minGateLevel").orElse(0).forGetter(GateSpawnData::minGateLevel),
            Codec.BOOL.fieldOf("allowUnderwater").forGetter(GateSpawnData::canSpawnInWater)
    ).apply(instance, (biomes, structures, gatePredicate, playerPredicate, entity, dist, lvl, underwater) -> new GateSpawnData(entity, dist, lvl, underwater, biomes, structures, gatePredicate.orElse(EntityPredicate.ANY), playerPredicate.orElse(EntityPredicate.ANY))));

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
            return new GateSpawnData(name, this.minDistanceFromSpawn, this.minGateLevel, this.allowUnderwater, this.biomes, this.structures, this.gatePredicate, this.playerPredicate);
        }
    }
}
