package com.flemmli97.runecraftory.mobs.spawning;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.entity.EntityType;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class GateSpawning {

    private static Map<RegistryKey<Biome>, List<SpawnResource>> spawningMappingBiome = Maps.newHashMap();

    public static List<EntityType<?>> pickRandomMobs(ServerWorld world, RegistryKey<Biome> biome, Random rand, int amount, BlockPos pos) {
        List<SpawnResource> list = Lists.newArrayList(spawningMappingBiome.getOrDefault(biome, Lists.newArrayList()));
        if (list.isEmpty())
            return Lists.newArrayList();
        list.removeIf(w->w.distToSpawnSq >= pos.distanceSq(world.getSpawnPos()));
        List<EntityType<?>> ret = Lists.newArrayList();
        if (amount > list.size()) {
            list.forEach(w -> {
                EntityType<?> type = ForgeRegistries.ENTITIES.getValue(w.loc);
                if (type != null)
                    ret.add(type);
            });
        } else {
            int i = amount;
            int totalWeight = WeightedRandom.getTotalWeight(list);
            SpawnResource el;
            while (i > 0 && !ret.contains((el = WeightedRandom.getRandomItem(rand, list, totalWeight)).loc)) {
                EntityType<?> type = ForgeRegistries.ENTITIES.getValue(el.loc);
                if (type != null)
                    ret.add(type);
                i--;
            }
        }
        return ret;
    }

    public static void addSpawn(ResourceLocation loc, String s){
        String[] sub = s.split(",");
        if(sub.length<3)
            return;
        SpawnResource w = new SpawnResource(new ResourceLocation(sub[0]), Integer.parseInt(sub[1]), Integer.parseInt(sub[2]));
        spawningMappingBiome.merge(RegistryKey.of(Registry.BIOME_KEY, loc), Lists.newArrayList(), (old, v)->{
            old.add(w);
            return old;
        });
    }

    private static class SpawnResource extends WeightedRandom.Item{

        private final ResourceLocation loc;
        private final int distToSpawnSq;

        public SpawnResource(ResourceLocation loc, int distToSpawn, int weight) {
            super(weight);
            this.loc = loc;
            this.distToSpawnSq = distToSpawn * distToSpawn;
        }

        public boolean canSpawn(ServerWorld world, BlockPos pos) {
            return pos.distanceSq(world.getSpawnPos()) < this.distToSpawnSq;
        }
    }
}
