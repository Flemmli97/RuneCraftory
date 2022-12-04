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

import java.util.Map;
import java.util.Random;

public class NPCDataManager extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().create();

    private Map<ResourceLocation, NPCData> keyData = ImmutableMap.of();
    private Map<NPCData, ResourceLocation> dataKey = ImmutableMap.of();
    private WeightedList<NPCData> view = new WeightedList<>();

    public NPCDataManager() {
        super(GSON, "npc_data");
    }

    public NPCData get(ResourceLocation res) {
        return this.keyData.getOrDefault(res, NPCData.DEFAULT_DATA);
    }

    public ResourceLocation get(NPCData data) {
        return this.dataKey.get(data);
    }

    public NPCData getRandom(Random random) {
        return this.view.getRandom(random, NPCData.DEFAULT_DATA);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler) {
        ImmutableMap.Builder<ResourceLocation, NPCData> builder = ImmutableMap.builder();
        map.forEach((fres, el) -> {
            try {
                JsonObject obj = el.getAsJsonObject();
                builder.put(fres, NPCData.CODEC.parse(JsonOps.INSTANCE, obj)
                        .getOrThrow(false, RuneCraftory.logger::error));
            } catch (Exception ex) {
                RuneCraftory.logger.error("Couldnt parse npc data json {}", fres);
                ex.fillInStackTrace();
            }
        });
        this.keyData = builder.build();
        ImmutableMap.Builder<NPCData, ResourceLocation> reverse = ImmutableMap.builder();
        this.keyData.forEach((resourceLocation, data) -> reverse.put(data, resourceLocation));
        this.dataKey = reverse.build();
        this.view.setList(this.keyData.values().stream().map(d -> WeightedEntry.wrap(d, d.weight())).toList());
    }
}