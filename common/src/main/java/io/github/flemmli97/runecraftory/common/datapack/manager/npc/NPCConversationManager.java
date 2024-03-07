package io.github.flemmli97.runecraftory.common.datapack.manager.npc;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.NPCData;
import io.github.flemmli97.runecraftory.api.datapack.NPCData.ConversationSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;

public class NPCConversationManager extends SimpleJsonResourceReloadListener {

    public static final String DIRECTORY = "conversations";

    private static final Gson GSON = new GsonBuilder().create();

    private Map<ResourceLocation, NPCData.ConversationSet> keyData = ImmutableMap.of();
    private Map<NPCData.ConversationSet, ResourceLocation> dataKey = ImmutableMap.of();

    public NPCConversationManager() {
        super(GSON, DIRECTORY);
    }

    public NPCData.ConversationSet get(ResourceLocation res, ConversationSet fallback) {
        return this.keyData.getOrDefault(res, fallback);
    }

    public ResourceLocation getId(NPCData.ConversationSet data) {
        return this.dataKey.get(data);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler) {
        ImmutableMap.Builder<ResourceLocation, NPCData.ConversationSet> builder = ImmutableMap.builder();
        map.forEach((fres, el) -> {
            try {
                JsonObject obj = el.getAsJsonObject();
                builder.put(fres, NPCData.ConversationSet.CODEC.parse(JsonOps.INSTANCE, obj)
                        .getOrThrow(false, RuneCraftory.LOGGER::error));
            } catch (Exception ex) {
                RuneCraftory.LOGGER.error("Couldnt parse npc data json {} {}", fres, ex);
                ex.fillInStackTrace();
            }
        });
        this.keyData = builder.build();
        ImmutableMap.Builder<NPCData.ConversationSet, ResourceLocation> reverse = ImmutableMap.builder();
        this.keyData.forEach((resourceLocation, data) -> reverse.put(data, resourceLocation));
        this.dataKey = reverse.build();
    }
}
