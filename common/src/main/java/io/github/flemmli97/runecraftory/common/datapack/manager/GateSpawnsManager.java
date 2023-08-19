package io.github.flemmli97.runecraftory.common.datapack.manager;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.GateSpawnData;
import io.github.flemmli97.runecraftory.common.config.MobConfig;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.SectionPos;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class GateSpawnsManager extends SimpleJsonResourceReloadListener {

    public static final String DIRECTORY = "gate_spawning";

    private static final Gson GSON = new GsonBuilder().create();

    private Map<TagKey<Biome>, List<SpawnResource>> biomeSpawns = new HashMap<>();
    private Map<ConfiguredStructureFeature<?, ?>, List<SpawnResource>> structureSpawns = new HashMap<>();

    public GateSpawnsManager() {
        super(GSON, DIRECTORY);
    }

    public List<EntityType<?>> pickRandomMobs(ServerLevel world, Holder<Biome> biome, Random rand, int amount, BlockPos pos, int gateLevel) {
        List<SpawnResource> list = world.structureFeatureManager().startsForFeature(SectionPos.of(pos), this.structureSpawns::containsKey)
                .stream().filter(start -> start.getBoundingBox().isInside(pos))
                .map(start -> this.structureSpawns.get(start.getFeature()))
                .flatMap(List::stream).collect(Collectors.toList());
        if (list.isEmpty()) {
            biome.tags().forEach(tag -> {
                List<SpawnResource> l = this.biomeSpawns.get(tag);
                if (l != null)
                    list.addAll(l);
            });
        }
        if (list.isEmpty())
            return new ArrayList<>();
        list.removeIf(w -> w.distToSpawnSq >= pos.distSqr(world.getSharedSpawnPos()) || (MobConfig.gateLevelType != MobConfig.GateLevelType.CONSTANT && w.minGateLevel > gateLevel));
        List<EntityType<?>> ret = new ArrayList<>();
        if (amount > list.size()) {
            list.forEach(w -> ret.add(w.entity));
        } else {
            int i = amount;
            int totalWeight = WeightedRandom.getTotalWeight(list);
            EntityType<?> type;
            while (i > 0 && !ret.contains(type = WeightedRandom.getRandomItem(rand, list, totalWeight).map(w -> w.entity).orElse(null))) {
                if (type != null)
                    ret.add(type);
                i--;
            }
        }
        return ret;
    }

    public boolean hasSpawns(ServerLevelAccessor level, BlockPos pos) {
        if (level.getBiome(pos).tags().anyMatch(t -> {
            List<SpawnResource> l = this.biomeSpawns.get(t);
            return l != null && l.stream().anyMatch(r -> r.canSpawn(level.getLevel(), pos));
        }))
            return true;
        return this.hasStructureSpawns(level.getLevel(), pos);
    }

    public boolean hasStructureSpawns(ServerLevel world, BlockPos pos) {
        return world.structureFeatureManager().startsForFeature(SectionPos.of(pos), this.structureSpawns::containsKey)
                .stream().anyMatch(start -> start.getBoundingBox().isInside(pos));
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> data, ResourceManager manager, ProfilerFiller profiler) {
        Map<TagKey<Biome>, List<SpawnResource>> biomeSpawns = new LinkedHashMap<>();
        Map<ConfiguredStructureFeature<?, ?>, List<SpawnResource>> structureSpawns = new LinkedHashMap<>();
        data.forEach((fres, el) -> {
            try {
                GateSpawnData spawnData = GateSpawnData.CODEC.parse(JsonOps.INSTANCE, el)
                        .getOrThrow(false, RuneCraftory.logger::error);
                Optional<EntityType<?>> optType = PlatformUtils.INSTANCE.entities().getOptionalFromId(spawnData.entity());
                optType.ifPresentOrElse(type -> {
                    spawnData.biomes().forEach((key, weight) -> {
                        SpawnResource resource = new SpawnResource(type, spawnData.minDistanceFromSpawn(), spawnData.minGateLevel(), weight);
                        biomeSpawns.computeIfAbsent(key, o -> new ArrayList<>())
                                .add(resource);
                    });
                    spawnData.structures().forEach((key, weight) -> {
                        Optional<ConfiguredStructureFeature<?, ?>> optFeat = BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE.getOptional(key);
                        optFeat.ifPresentOrElse(feat -> {
                            SpawnResource resource = new SpawnResource(type, spawnData.minDistanceFromSpawn(), spawnData.minGateLevel(), weight);
                            structureSpawns.computeIfAbsent(feat, o -> new ArrayList<>())
                                    .add(resource);
                        }, () -> RuneCraftory.logger.error("No such feature " + key + " for spawn data " + fres));
                    });
                }, () -> RuneCraftory.logger.error("No such entity " + spawnData.entity() + " for spawn data " + fres));
            } catch (Exception ex) {
                RuneCraftory.logger.error("Couldnt parse spawn data json {} {}", fres, ex);
                ex.fillInStackTrace();
            }
        });
        this.biomeSpawns = ImmutableMap.copyOf(biomeSpawns);
        this.structureSpawns = ImmutableMap.copyOf(structureSpawns);
    }

    public static class SpawnResource extends WeightedEntry.IntrusiveBase {

        private final EntityType<?> entity;
        private final int distToSpawnSq;
        private final int minGateLevel;

        public SpawnResource(EntityType<?> entity, int distToSpawn, int minGateLevel, int weight) {
            super(weight);
            this.entity = entity;
            this.distToSpawnSq = distToSpawn * distToSpawn;
            this.minGateLevel = minGateLevel;
        }

        public boolean canSpawn(ServerLevel world, BlockPos pos) {
            return pos.distSqr(world.getSharedSpawnPos()) >= this.distToSpawnSq;
        }

        @Override
        public String toString() {
            return String.format("Entity: %s, MinSpawnSq: %d, Weight: %s, MinGateLevel: %s", PlatformUtils.INSTANCE.entities().getIDFrom(this.entity), this.distToSpawnSq, this.getWeight(), this.minGateLevel);
        }
    }
}
