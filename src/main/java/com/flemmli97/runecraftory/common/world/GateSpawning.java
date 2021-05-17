package com.flemmli97.runecraftory.common.world;

import com.google.common.collect.Lists;
import net.minecraft.entity.EntityType;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GateSpawning {

    private static Map<RegistryKey<Biome>, List<SpawnResource>> spawningMappingBiome = new HashMap<>();

    public static List<EntityType<?>> pickRandomMobs(ServerWorld world, RegistryKey<Biome> biome, Random rand, int amount, BlockPos pos) {
        List<SpawnResource> list = Lists.newArrayList(spawningMappingBiome.getOrDefault(biome, new ArrayList<>()));
        if (list.isEmpty())
            return new ArrayList<>();
        list.removeIf(w -> w.distToSpawnSq >= pos.distanceSq(world.getSpawnPos()));
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

    public static void addSpawn(ResourceLocation loc, SpawnResource s) {
        spawningMappingBiome.merge(RegistryKey.of(Registry.BIOME_KEY, loc), Lists.newArrayList(s), (old, v) -> {
            old.add(s);
            return old;
        });
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
            return pos.distanceSq(world.getSpawnPos()) < this.distToSpawnSq;
        }

        @Override
        public String toString() {
            return String.format("Entity: %s, MinSpawnSq: %d, Weight: %d", this.entity, this.distToSpawnSq, this.itemWeight);
        }
    }
}
