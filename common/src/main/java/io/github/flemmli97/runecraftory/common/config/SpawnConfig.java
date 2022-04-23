package io.github.flemmli97.runecraftory.common.config;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.world.GateSpawning;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.biome.Biome;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpawnConfig {

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static SpawnConfig spawnConfig;

    private final Path directory;
    private static final List<SpawnData> defaultData = new ArrayList<>();

    private final Map<TagKey<Biome>, List<GateSpawning.SpawnResource>> biomeTagEntitiesMap = new HashMap<>();
    private final Map<String, List<GateSpawning.SpawnResource>> rawStructureEntities = new HashMap<>();

    public SpawnConfig(Path file) {
        this.directory = file.resolve("spawn");
        if (!Files.exists(this.directory))
            this.directory.toFile().mkdirs();
        for (SpawnData data : defaultData) {
            File dataFile = this.directory.resolve(data.entity.getPath() + ".json").toFile();
            if (!dataFile.exists()) {
                try {
                    dataFile.createNewFile();
                    FileWriter writer = new FileWriter(dataFile);
                    JsonElement e = SpawnData.CODEC.encode(data, JsonOps.INSTANCE, new JsonObject()).getOrThrow(false, s -> RuneCraftory.logger.error("Failed saving default spawn data for " + data.entity + ": " + s));
                    GSON.toJson(e, writer);
                    writer.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }
        try {
            Files.walk(this.directory)
                    .filter(p -> p.toString().endsWith(".json"))
                    .forEach(p -> {
                        try {
                            BufferedReader s = Files.newBufferedReader(p);
                            JsonElement element = JsonParser.parseReader(s);
                            SpawnData data = SpawnData.CODEC.parse(JsonOps.INSTANCE, element)
                                    .getOrThrow(false, e -> RuneCraftory.logger.error("Failed reading spawn data for " + p.getFileName() + ": " + e));
                            data.biomes.forEach((biome, weight) ->
                                    this.biomeTagEntitiesMap.merge(biome, Lists.newArrayList(new GateSpawning.SpawnResource(data.entity, data.minDistanceFromSpawn, weight)), (old, v) -> {
                                        old.add(new GateSpawning.SpawnResource(data.entity, data.minDistanceFromSpawn, weight));
                                        return old;
                                    }));
                            data.structures.forEach((biome, weight) ->
                                    this.rawStructureEntities.merge(biome, Lists.newArrayList(new GateSpawning.SpawnResource(data.entity, data.minDistanceFromSpawn, weight)), (old, v) -> {
                                        old.add(new GateSpawning.SpawnResource(data.entity, data.minDistanceFromSpawn, weight));
                                        return old;
                                    }));
                        } catch (Exception e) {
                            RuneCraftory.logger.error("Error reading spawn data file for " + p.getFileName() + ". Skipping!!!");
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.biomeTagEntitiesMap.forEach((tag, list) -> list.forEach(res -> GateSpawning.addSpawn(tag, res)));
    }

    public Map<String, List<GateSpawning.SpawnResource>> getRawStructureEntities() {
        return this.rawStructureEntities;
    }

    public static void addDefaultData(SpawnData data) {
        defaultData.add(data);
    }

    public record SpawnData(ResourceLocation entity, int minDistanceFromSpawn,
                            Map<TagKey<Biome>, Integer> biomes,
                            Map<String, Integer> structures) {

        public static final Codec<SpawnData> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                        ResourceLocation.CODEC.fieldOf("entity").forGetter(SpawnData::entity),
                        ExtraCodecs.NON_NEGATIVE_INT.fieldOf("min_distance_from_spawn").forGetter(SpawnData::minDistanceFromSpawn),
                        Codec.unboundedMap(TagKey.codec(Registry.BIOME_REGISTRY), ExtraCodecs.POSITIVE_INT).fieldOf("biomes").forGetter(SpawnData::biomes),
                        Codec.unboundedMap(Codec.STRING, ExtraCodecs.POSITIVE_INT).fieldOf("structures").forGetter(SpawnData::structures))
                .apply(instance, SpawnData::new));

        public static class Builder {

            private final Map<TagKey<Biome>, Integer> biomes = new HashMap<>();
            private final Map<String, Integer> structures = new HashMap<>();
            private final int minDistanceFromSpawn;

            public Builder(int minDistanceFromSpawn) {
                this.minDistanceFromSpawn = minDistanceFromSpawn;
            }

            @SafeVarargs
            public final Builder addToBiomeTag(int weight, TagKey<Biome>... biomes) {
                for (TagKey<Biome> tag : biomes)
                    this.biomes.put(tag, weight);
                return this;
            }

            public Builder addToStructures(int weight, String... structures) {
                for (String s : structures)
                    this.structures.put(s, weight);
                return this;
            }

            public SpawnData build(ResourceLocation name) {
                return new SpawnData(name, this.minDistanceFromSpawn, this.biomes, this.structures);
            }
        }
    }
}