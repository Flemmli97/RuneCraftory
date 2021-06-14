package io.github.flemmli97.runecraftory.common.config;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.world.GateSpawning;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.BiomeDictionary;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpawnConfig {

    public static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping()
            .registerTypeAdapter(ResourceLocation.class, new ResourceLocAdapter()).create();

    private final File directory;
    private static final List<SpawnData> defaultData = new ArrayList<>();

    private Map<String, List<GateSpawning.SpawnResource>> rawBiomeEntitiesMap = new HashMap<>();
    private Map<String, List<GateSpawning.SpawnResource>> rawBiomeTypeEntitiesMap = new HashMap<>();
    private Map<String, List<GateSpawning.SpawnResource>> rawStructureEntities = new HashMap<>();

    public SpawnConfig(Path file) {
        this.directory = file.resolve("spawn").toFile();
        if (!this.directory.exists())
            this.directory.mkdirs();
        for (SpawnData data : this.defaultData) {
            File dataFile = new File(this.directory, data.entity.getPath() + ".json");
            if (!dataFile.exists()) {
                try {
                    dataFile.createNewFile();
                    FileWriter writer = new FileWriter(dataFile);
                    GSON.toJson(data, writer);
                    writer.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }

        for (File dataFile : this.directory.listFiles()) {
            if (dataFile.isFile() && dataFile.getName().endsWith(".json")) {
                try {
                    FileReader reader = new FileReader(dataFile);
                    try {
                        SpawnData data = GSON.fromJson(reader, SpawnData.class);
                        reader.close();
                        if (data.biomes != null)
                            data.biomes.forEach((biome, weight) ->
                                    this.rawBiomeEntitiesMap.merge(biome, Lists.newArrayList(new GateSpawning.SpawnResource(data.entity, data.minDistanceFromSpawn, weight)), (old, v) -> {
                                        old.add(new GateSpawning.SpawnResource(data.entity, data.minDistanceFromSpawn, weight));
                                        return old;
                                    }));
                        data.biomeTypes.forEach((biome, weight) ->
                                this.rawBiomeTypeEntitiesMap.merge(biome, Lists.newArrayList(new GateSpawning.SpawnResource(data.entity, data.minDistanceFromSpawn, weight)), (old, v) -> {
                                    old.add(new GateSpawning.SpawnResource(data.entity, data.minDistanceFromSpawn, weight));
                                    return old;
                                }));
                        data.structures.forEach((biome, weight) ->
                                this.rawStructureEntities.merge(biome, Lists.newArrayList(new GateSpawning.SpawnResource(data.entity, data.minDistanceFromSpawn, weight)), (old, v) -> {
                                    old.add(new GateSpawning.SpawnResource(data.entity, data.minDistanceFromSpawn, weight));
                                    return old;
                                }));
                    } catch (NullPointerException e) {
                        RuneCraftory.logger.error("Couldnt fully read config of file {} ", dataFile.getName());
                    }
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    public List<GateSpawning.SpawnResource> getEntityFromBiome(ResourceLocation loc) {
        return this.rawBiomeEntitiesMap.getOrDefault(loc.toString(), new ArrayList<>());
    }

    public List<GateSpawning.SpawnResource> getEntityFromBiomeType(BiomeDictionary.Type type) {
        return this.rawBiomeTypeEntitiesMap.getOrDefault(type.getName(), new ArrayList<>());
    }

    public Map<String, List<GateSpawning.SpawnResource>> getRawStructureEntities() {
        return this.rawStructureEntities;
    }

    public static void addDefaultData(SpawnData data) {
        defaultData.add(data);
    }

    public static class SpawnData {

        private final Map<String, Integer> biomes;
        private final Map<String, Integer> biomeTypes;
        private final Map<String, Integer> structures;
        private final ResourceLocation entity;
        private final int minDistanceFromSpawn;

        private SpawnData(ResourceLocation entity, int minDistanceFromSpawn, Map<String, Integer> biomes, Map<String, Integer> biomeTypes, Map<String, Integer> structures) {
            this.entity = entity;
            this.minDistanceFromSpawn = minDistanceFromSpawn;
            this.biomes = biomes;
            this.biomeTypes = biomeTypes;
            this.structures = structures;
        }

        public static class Builder {

            private final Map<String, Integer> biomes = new HashMap<>();
            private final Map<String, Integer> biomeTypes = new HashMap<>();
            private final Map<String, Integer> structures = new HashMap<>();
            private ResourceLocation entity;
            private final int minDistanceFromSpawn;

            public Builder(int minDistanceFromSpawn) {
                this.minDistanceFromSpawn = minDistanceFromSpawn;
            }

            public Builder addToBiome(int weight, String... biomes) {
                for (String s : biomes)
                    this.biomes.put(s, weight);
                return this;
            }

            public Builder addToBiomeTypes(int weight, String... biomeTypes) {
                for (String s : biomeTypes)
                    this.biomeTypes.put(s, weight);
                return this;
            }

            public Builder addToStructures(int weight, String... structures) {
                for (String s : structures)
                    this.structures.put(s, weight);
                return this;
            }

            public SpawnData build(ResourceLocation name) {
                return new SpawnData(name, this.minDistanceFromSpawn, this.biomes, this.biomeTypes, this.structures);
            }
        }
    }

    private static class ResourceLocAdapter extends TypeAdapter<ResourceLocation> {
        @Override
        public void write(JsonWriter out, ResourceLocation value) throws IOException {
            out.value(value.toString());
        }

        @Override
        public ResourceLocation read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            return new ResourceLocation(in.nextString());
        }
    }
}