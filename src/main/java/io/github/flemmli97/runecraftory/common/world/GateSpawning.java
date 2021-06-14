package io.github.flemmli97.runecraftory.common.world;

import com.google.common.collect.Lists;
import io.github.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.entity.EntityType;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.SectionPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class GateSpawning {

    private static Map<RegistryKey<Biome>, List<SpawnResource>> spawningMappingBiome = new HashMap<>();
    private static Map<Structure<?>, List<SpawnResource>> spawningMappingStructure = new HashMap<>();

    public static List<EntityType<?>> pickRandomMobs(ServerWorld world, RegistryKey<Biome> biome, Random rand, int amount, BlockPos pos) {
        List<SpawnResource> list = spawningMappingBiome.get(biome);
        List<SpawnResource> structureList = /*getStructuresAt(world, pos).stream().filter(spawningMappingStructure::containsKey)
                .map(spawningMappingStructure::get).flatMap(List::stream).collect(Collectors.toList());*/
                spawningMappingStructure.entrySet().stream()
                        .filter(e -> world.getStructureManager().getStructureStart(pos, true, e.getKey()).isValid())
                        .map(Map.Entry::getValue).flatMap(List::stream).collect(Collectors.toList());
        if (!structureList.isEmpty())
            list = structureList;
        if (list == null || list.isEmpty())
            return new ArrayList<>();
        list.removeIf(w -> w.distToSpawnSq >= pos.distanceSq(world.getSpawnPoint()));
        List<EntityType<?>> ret = new ArrayList<>();
        if (amount > list.size()) {
            list.forEach(w -> {
                EntityType<?> type = ForgeRegistries.ENTITIES.getValue(w.entity);
                if (type != null)
                    ret.add(type);
            });
        } else {
            int i = amount;
            int totalWeight = WeightedRandom.getTotalWeight(list);
            SpawnResource el;
            while (i > 0 && !ret.contains((el = WeightedRandom.getRandomItem(rand, list, totalWeight)).entity)) {
                EntityType<?> type = ForgeRegistries.ENTITIES.getValue(el.entity);
                if (type != null)
                    ret.add(type);
                i--;
            }
        }
        return ret;
    }

    public static Set<Structure<?>> getStructuresAt(ServerWorld world, BlockPos pos) {
        return world.getChunk(pos.getX() >> 4, pos.getZ() >> 4, ChunkStatus.STRUCTURE_REFERENCES)
                .getStructureReferences().entrySet().stream().filter(e ->
                        e.getValue().stream().map(l -> SectionPos.from(new ChunkPos(l), 0))
                                .map(section -> world.getStructureManager()
                                        .getStructureStart(section, e.getKey(), world.getChunk(section.getX(), section.getZ(), ChunkStatus.STRUCTURE_STARTS)))
                                .filter(start -> start != null && start.isValid() && start.getBoundingBox().isVecInside(pos)
                                        && start.getComponents().stream().anyMatch(piece -> piece.getBoundingBox().isVecInside(pos))
                                ).count() > 0
                ).map(Map.Entry::getKey).collect(Collectors.toSet());
    }

    public static boolean hasSpawns(IWorld world, BlockPos pos) {
        List<SpawnResource> list = spawningMappingBiome.get(world.func_242406_i(pos).orElse(Biomes.PLAINS));
        if (list != null && !list.isEmpty())
            return true;
        if (world instanceof ServerWorld) {
            //return getStructuresAt((ServerWorld) world, pos).stream().filter(s->!spawningMappingStructure.getOrDefault(s, new ArrayList<>()).isEmpty())
            //        .count()>0;
            return hasStructureSpawns((ServerWorld) world, pos);
        }
        return false;
    }

    public static boolean hasStructureSpawns(ServerWorld world, BlockPos pos) {
        return !spawningMappingStructure.entrySet().stream()
                .filter(e -> world.getStructureManager().getStructureStart(pos, true, e.getKey()).isValid())
                .map(Map.Entry::getValue).flatMap(List::stream).collect(Collectors.toList()).isEmpty();
    }

    public static void addSpawn(ResourceLocation loc, SpawnResource s) {
        spawningMappingBiome.merge(RegistryKey.getOrCreateKey(Registry.BIOME_KEY, loc), Lists.newArrayList(s), (old, v) -> {
            old.add(s);
            return old;
        });
    }

    public static void setupStructureSpawns() {
        RuneCraftory.spawnConfig.getRawStructureEntities().forEach((struc, list) -> {
            Structure<?> structure = ForgeRegistries.STRUCTURE_FEATURES.getValue(new ResourceLocation(struc));
            if (struc.equals(Structure.MINESHAFT.getRegistryName().toString()) || structure != Structure.MINESHAFT) {
                spawningMappingStructure.put(structure, list);
            }
        });
    }

    public static boolean structureShouldSpawn(Structure<?> structure) {
        return spawningMappingStructure.containsKey(structure);
    }

    public static class SpawnResource extends WeightedRandom.Item {

        private final ResourceLocation entity;
        private final int distToSpawnSq;

        public SpawnResource(ResourceLocation entity, int distToSpawn, int weight) {
            super(weight);
            this.entity = entity;
            this.distToSpawnSq = distToSpawn * distToSpawn;
        }

        public boolean canSpawn(ServerWorld world, BlockPos pos) {
            return pos.distanceSq(world.getSpawnPoint()) < this.distToSpawnSq;
        }

        @Override
        public String toString() {
            return String.format("Entity: %s, MinSpawnSq: %d, Weight: %d", this.entity, this.distToSpawnSq, this.itemWeight);
        }
    }
}
