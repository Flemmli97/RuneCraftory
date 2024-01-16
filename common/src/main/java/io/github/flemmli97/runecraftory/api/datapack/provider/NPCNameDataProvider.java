package io.github.flemmli97.runecraftory.api.datapack.provider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import io.github.flemmli97.runecraftory.common.datapack.manager.npc.NameManager;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class NPCNameDataProvider implements DataProvider {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private Map<String, NameStructure> names = new HashMap<>();

    private final DataGenerator gen;
    private final String modid;

    public NPCNameDataProvider(DataGenerator gen, String modid) {
        this.gen = gen;
        this.modid = modid;
    }

    protected abstract void add();

    @Override
    public void run(HashCache cache) {
        this.add();
        this.names.forEach((key, vals) -> {
            Path dataPath = this.gen.getOutputFolder()
                    .resolve(String.format("data/%1$s/%2$s/%3$s.json", this.modid, NameManager.DIRECTORY, key));
            try {
                JsonElement obj = GSON.toJsonTree(vals);
                DataProvider.save(GSON, cache, obj, dataPath);
            } catch (IOException e) {
                LOGGER.error("Couldn't save npc names {}", dataPath, e);
            }
        });
    }

    @Override
    public String getName() {
        return "NPCNameData";
    }

    public void addFemaleName(String lang, String... name) {
        this.names.computeIfAbsent(lang, k -> new NameStructure())
                .female_names.addAll(List.of(name));
    }

    public void addMaleName(String lang, String... name) {
        this.names.computeIfAbsent(lang, k -> new NameStructure())
                .male_names.addAll(List.of(name));
    }

    public void addSurname(String lang, String... name) {
        this.names.computeIfAbsent(lang, k -> new NameStructure())
                .surnames.addAll(List.of(name));
    }

    private record NameStructure(List<String> surnames, List<String> male_names, List<String> female_names) {
        private NameStructure() {
            this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        }
    }
}
