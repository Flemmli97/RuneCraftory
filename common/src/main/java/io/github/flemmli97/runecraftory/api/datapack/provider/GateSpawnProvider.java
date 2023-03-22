package io.github.flemmli97.runecraftory.api.datapack.provider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.GateSpawnData;
import io.github.flemmli97.runecraftory.api.datapack.GsonInstances;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public abstract class GateSpawnProvider implements DataProvider {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final Gson GSON = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().disableHtmlEscaping().create();

    private final Map<ResourceLocation, GateSpawnData> data = new HashMap<>();

    private final DataGenerator gen;
    private final String modid;

    public GateSpawnProvider(DataGenerator gen, String modid) {
        this.gen = gen;
        this.modid = modid;
    }

    protected abstract void add();

    @Override
    public void run(HashCache cache) {
        this.add();
        this.data.forEach((res, spawnData) -> {
            Path path = this.gen.getOutputFolder().resolve("data/" + res.getNamespace() + "/gate_spawning/" + res.getPath() + ".json");
            try {
                JsonElement obj = GateSpawnData.CODEC.encodeStart(JsonOps.INSTANCE, spawnData)
                        .getOrThrow(false, RuneCraftory.logger::error);
                DataProvider.save(GsonInstances.ATTRIBUTE_SPELLS, cache, obj, path);
            } catch (IOException e) {
                LOGGER.error("Couldn't save itemstat {}", path, e);
            }
        });
    }

    @Override
    public String getName() {
        return "GateSpawns";
    }

    public void addGateSpawn(GateSpawnData spawnData) {
        this.addGateSpawn(spawnData.entity(), spawnData);
    }

    public void addGateSpawn(ResourceLocation key, GateSpawnData spawnData) {
        this.data.put(key, spawnData);
    }
}