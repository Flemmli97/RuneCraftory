package io.github.flemmli97.runecraftory.api.datapack.provider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.GsonInstances;
import io.github.flemmli97.runecraftory.common.datapack.manager.StructureBossManager;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public abstract class StructureBossProvider implements DataProvider {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final Gson GSON = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().disableHtmlEscaping().create();

    private final Map<ResourceLocation, StructureBossManager.BossSpawnList> data = new HashMap<>();

    private final DataGenerator gen;
    private final String modid;
    private final FileVerifier verifier;

    public StructureBossProvider(DataGenerator gen, String modid, FileVerifier verifier) {
        this.gen = gen;
        this.modid = modid;
        this.verifier = verifier;
    }

    protected abstract void add();

    @Override
    public void run(HashCache cache) {
        this.add();
        this.data.forEach((res, spawnData) -> {
            Path path = this.gen.getOutputFolder().resolve("data/" + res.getNamespace() + "/" + StructureBossManager.DIRECTORY + "/" + res.getPath() + ".json");
            try {
                JsonElement obj = StructureBossManager.BossSpawnList.CODEC.encodeStart(JsonOps.INSTANCE, spawnData)
                        .getOrThrow(false, RuneCraftory.LOGGER::error);
                DataProvider.save(GsonInstances.ATTRIBUTE_SPELLS, cache, obj, path);
            } catch (IOException e) {
                LOGGER.error("Couldn't save itemstat {}", path, e);
            }
        });
    }

    @Override
    public String getName() {
        return "StructureBosses";
    }

    public void addGateSpawn(ResourceLocation key, StructureBossManager.BossSpawnList spawnData) {
        this.data.put(key, spawnData);
        this.verifier.track(key, PackType.SERVER_DATA, StructureBossManager.DIRECTORY);
    }
}