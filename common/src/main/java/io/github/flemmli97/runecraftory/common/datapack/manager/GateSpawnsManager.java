package io.github.flemmli97.runecraftory.common.datapack.manager;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.GateSpawnData;
import io.github.flemmli97.runecraftory.common.config.MobConfig;
import io.github.flemmli97.runecraftory.common.entities.GateEntity;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.SectionPos;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
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

    public List<EntityType<?>> pickRandomMobs(ServerLevel level, GateEntity gate, Holder<Biome> biome, Random rand, int amount, BlockPos pos) {
        List<SpawnResource> list = level.structureFeatureManager().startsForFeature(SectionPos.of(pos), this.structureSpawns::containsKey)
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
        double dist = pos.distSqr(level.getSharedSpawnPos());
        BlockState state = level.getBlockState(pos);
        list.removeIf(w -> !w.matches(level, pos, state, dist, gate));
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

    public boolean hasSpawns(ServerLevelAccessor level, BlockPos pos, BlockState state) {
        if (level.getBiome(pos).tags().anyMatch(t -> {
            List<SpawnResource> l = this.biomeSpawns.get(t);
            return l != null && l.stream().anyMatch(r -> r.canSpawn(level.getLevel(), pos, state));
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
                        SpawnResource resource = new SpawnResource(type, spawnData, weight);
                        biomeSpawns.computeIfAbsent(key, o -> new ArrayList<>())
                                .add(resource);
                    });
                    spawnData.structures().forEach((key, weight) -> {
                        Optional<ConfiguredStructureFeature<?, ?>> optFeat = BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE.getOptional(key);
                        optFeat.ifPresentOrElse(feat -> {
                            SpawnResource resource = new SpawnResource(type, spawnData, weight);
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
        private final boolean allowWater;
        private final EntityPredicate gatePredicate;

        public SpawnResource(EntityType<?> entity, GateSpawnData spawnData, int weight) {
            super(weight);
            this.entity = entity;
            this.distToSpawnSq = spawnData.minDistanceFromSpawn() * spawnData.minDistanceFromSpawn();
            this.minGateLevel = spawnData.minGateLevel();
            this.allowWater = spawnData.canSpawnInWater();
            this.gatePredicate = spawnData.gatePredicate();
        }

        public boolean canSpawn(ServerLevel serverLevel, BlockPos pos, BlockState state) {
            return pos.distSqr(serverLevel.getSharedSpawnPos()) >= this.distToSpawnSq && (state.getFluidState().isEmpty() || (this.allowWater && state.getFluidState().is(FluidTags.WATER) && serverLevel.canSeeSkyFromBelowWater(pos)));
        }

        public boolean matches(ServerLevel serverLevel, BlockPos pos, BlockState state, double dist, GateEntity gate) {
            return dist >= this.distToSpawnSq && (state.getFluidState().isEmpty() || (this.allowWater && state.getFluidState().is(FluidTags.WATER) && serverLevel.canSeeSkyFromBelowWater(pos)))
                    && (MobConfig.gateLevelType == MobConfig.GateLevelType.CONSTANT || gate.level().getLevel() >= this.minGateLevel)
                    && this.gatePredicate.matches(serverLevel, gate.position(), gate);
        }

        @Override
        public String toString() {
            return String.format("Entity: %s, MinSpawnSq: %d, Weight: %s, MinGateLevel: %s", PlatformUtils.INSTANCE.entities().getIDFrom(this.entity), this.distToSpawnSq, this.getWeight(), this.minGateLevel);
        }
    }
}
