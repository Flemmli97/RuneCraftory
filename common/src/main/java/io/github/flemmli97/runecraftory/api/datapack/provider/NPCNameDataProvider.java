package io.github.flemmli97.runecraftory.api.datapack.provider;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.common.datapack.manager.npc.NameManager;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class NPCNameDataProvider implements DataProvider {

    private static final Logger LOGGER = LogManager.getLogger();

    private final Map<String, NameStructure> names = new HashMap<>();

    private final PackOutput packOutput;
    private final String modid;

    public NPCNameDataProvider(PackOutput packOutput, String modid) {
        this.packOutput = packOutput;
        this.modid = modid;
    }

    protected abstract void add();

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        this.add();
        return CompletableFuture.allOf(this.names.entrySet().stream().map(e -> {
            Path dataPath = this.packOutput.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(String.format("%1$s/%2$s/%3$s.json", this.modid, NameManager.ID, e.getKey()));
            JsonElement obj = NameStructure.CODEC.encodeStart(JsonOps.INSTANCE, e.getValue()).getOrThrow();
            return DataProvider.saveStable(cache, obj, dataPath);
        }).toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "NPCNameData for " + this.modid;
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

        public static final Codec<NameStructure> CODEC = RecordCodecBuilder.create(inst ->
                inst.group(Codec.STRING.listOf().fieldOf("surnames").forGetter(NameStructure::surnames),
                        Codec.STRING.listOf().fieldOf("male_names").forGetter(NameStructure::male_names),
                        Codec.STRING.listOf().fieldOf("female_names").forGetter(NameStructure::female_names)
                ).apply(inst, NameStructure::new)
        );

        private NameStructure() {
            this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        }
    }
}
