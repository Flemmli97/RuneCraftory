package io.github.flemmli97.runecraftory.common.datapack.manager.npc;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.NPCData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.util.random.WeightedEntry;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Random;
import java.util.function.Predicate;

public class NPCDataManager extends SimpleJsonResourceReloadListener {

    public static final String DIRECTORY = "npc_data";

    private static final Gson GSON = new GsonBuilder().create();
    public static final ResourceLocation DEFAULT_ID = new ResourceLocation(RuneCraftory.MODID, "default_npc");

    private Map<ResourceLocation, NPCData> keyData = ImmutableMap.of();
    private Map<NPCData, ResourceLocation> dataKey = ImmutableMap.of();
    private final WeightedList<NPCData> view = new WeightedList<>();
    private final WeightedList<NPCData> viewNoJobDef = new WeightedList<>();

    public NPCDataManager() {
        super(GSON, DIRECTORY);
    }

    public NPCData get(ResourceLocation res) {
        return this.keyData.getOrDefault(res, NPCData.DEFAULT_DATA);
    }

    public ResourceLocation getId(NPCData data) {
        return this.dataKey.getOrDefault(data, DEFAULT_ID);
    }

    public NPCData getRandom(Random random, Predicate<NPCData> func, @Nullable Predicate<NPCData> other) {
        return this.view.getRandom(random, NPCData.DEFAULT_DATA, func, other);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler) {
        ImmutableMap.Builder<ResourceLocation, NPCData> builder = ImmutableMap.builder();
        map.forEach((fres, el) -> {
            if (!fres.equals(DEFAULT_ID)) {
                try {
                    JsonObject obj = el.getAsJsonObject();
                    builder.put(fres, NPCData.CODEC.parse(JsonOps.INSTANCE, obj)
                            .getOrThrow(false, RuneCraftory.logger::error));
                } catch (Exception ex) {
                    RuneCraftory.logger.error("Couldnt parse npc data json {} {}", fres, ex);
                    ex.fillInStackTrace();
                }
            }
        });
        this.keyData = builder.build();
        ImmutableMap.Builder<NPCData, ResourceLocation> reverse = ImmutableMap.builder();
        this.keyData.forEach((resourceLocation, data) -> reverse.put(data, resourceLocation));
        this.dataKey = reverse.build();
        this.view.setList(this.keyData.values().stream().map(d -> WeightedEntry.wrap(d, d.weight())).toList());
        this.viewNoJobDef.setList(this.keyData.values().stream().filter(d -> d.profession().isEmpty()).map(d -> WeightedEntry.wrap(d, d.weight())).toList());
    }
}
