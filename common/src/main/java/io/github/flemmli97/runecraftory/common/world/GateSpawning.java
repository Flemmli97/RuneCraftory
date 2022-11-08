package io.github.flemmli97.runecraftory.common.world;

import com.google.common.collect.Lists;
import io.github.flemmli97.runecraftory.common.config.SpawnConfig;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.mixinhelper.StructureModification;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.SectionPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GateSpawning {

    private static final Map<TagKey<Biome>, List<SpawnResource>> spawningMappingBiome = new HashMap<>();
    private static final Map<ConfiguredStructureFeature<?, ?>, List<SpawnResource>> spawningMappingStructure = new HashMap<>();

    public static List<EntityType<?>> pickRandomMobs(ServerLevel world, Holder<Biome> biome, Random rand, int amount, BlockPos pos, int gateLevel) {
        List<SpawnResource> list = spawningMappingStructure.entrySet().stream()
                .filter(e -> world.structureFeatureManager().getStructureAt(pos, e.getKey()).isValid())
                .map(Map.Entry::getValue).flatMap(List::stream).collect(Collectors.toList());
        if (list.isEmpty()) {
            biome.tags().forEach(tag -> {
                List<SpawnResource> l = spawningMappingBiome.get(tag);
                if (l != null)
                    list.addAll(l);
            });
        }
        if (list.isEmpty())
            return new ArrayList<>();
        list.removeIf(w -> w.distToSpawnSq >= pos.distSqr(world.getSharedSpawnPos()) || w.minGateLevel > gateLevel);
        List<EntityType<?>> ret = new ArrayList<>();
        if (amount > list.size()) {
            list.forEach(w -> {
                EntityType<?> type = PlatformUtils.INSTANCE.entities().getFromId(w.entity);
                if (type != null)
                    ret.add(type);
            });
        } else {
            int i = amount;
            int totalWeight = WeightedRandom.getTotalWeight(list);
            Function<Optional<SpawnResource>, EntityType<?>> fromData = o -> o.map(d -> PlatformUtils.INSTANCE.entities().getFromId(d.entity)).orElse(null);
            EntityType<?> type;
            while (i > 0 && !ret.contains(type = fromData.apply(WeightedRandom.getRandomItem(rand, list, totalWeight)))) {
                if (type != null)
                    ret.add(type);
                i--;
            }
        }
        return ret;
    }

    public static Set<ConfiguredStructureFeature<?, ?>> getStructuresAt(ServerLevel world, BlockPos pos) {
        return world.getChunk(pos.getX() >> 4, pos.getZ() >> 4, ChunkStatus.STRUCTURE_REFERENCES)
                .getAllReferences().entrySet().stream().filter(e ->
                        e.getValue().stream().map(l -> SectionPos.of(new ChunkPos(l), 0))
                                .map(section -> world.structureFeatureManager()
                                        .getStartForFeature(section, e.getKey(), world.getChunk(section.getX(), section.getZ(), ChunkStatus.STRUCTURE_STARTS))).anyMatch(start -> start != null && start.isValid() && start.getBoundingBox().isInside(pos)
                                        && start.getPieces().stream().anyMatch(piece -> piece.getBoundingBox().isInside(pos)))
                ).map(Map.Entry::getKey).collect(Collectors.toSet());
    }

    public static boolean hasSpawns(ServerLevelAccessor level, BlockPos pos) {
        if (level.getBiome(pos).tags().anyMatch(t -> {
            List<SpawnResource> l = spawningMappingBiome.get(t);
            return l != null && l.stream().anyMatch(r -> r.canSpawn(level.getLevel(), pos));
        }))
            return true;
        return hasStructureSpawns(level.getLevel(), pos);
    }

    public static boolean hasStructureSpawns(ServerLevel world, BlockPos pos) {
        return !(spawningMappingStructure.entrySet().stream()
                .filter(e -> world.structureFeatureManager().getStructureAt(pos, e.getKey()).isValid())
                .map(Map.Entry::getValue).mapToLong(List::size).sum() == 0);
    }

    public static void addSpawn(TagKey<Biome> tagKey, SpawnResource s) {
        spawningMappingBiome.merge(tagKey, Lists.newArrayList(s), (old, v) -> {
            old.add(s);
            return old;
        });
    }

    public static void setupStructureSpawns(MinecraftServer server) {
        Registry<ConfiguredStructureFeature<?, ?>> registry = server.registryAccess()
                .registryOrThrow(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY);
        SpawnConfig.spawnConfig.getRawStructureEntities().forEach((struc, list) -> {
            ConfiguredStructureFeature<?, ?> structure = registry.get(new ResourceLocation(struc));
            if (structure != null) {
                ((StructureModification) structure).addSpawn(MobCategory.MONSTER, new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE,
                        WeightedRandomList.create(new MobSpawnSettings.SpawnerData(ModEntities.gate.get(), 100, 1, 1))));
                spawningMappingStructure.put(structure, list);
            }
        });
    }

    public static boolean structureShouldSpawn(StructureFeature<?> structure) {
        return spawningMappingStructure.keySet().stream().map(c -> c.feature)
                .anyMatch(s -> s == structure);
    }

    public static class SpawnResource extends WeightedEntry.IntrusiveBase {

        private final ResourceLocation entity;
        private final int distToSpawnSq;
        private final int minGateLevel;

        public SpawnResource(ResourceLocation entity, int distToSpawn, int minGateLevel, int weight) {
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
            return String.format("Entity: %s, MinSpawnSq: %d, Weight: %s, MinGateLevel: %s", this.entity, this.distToSpawnSq, this.getWeight(), this.minGateLevel);
        }
    }
}
