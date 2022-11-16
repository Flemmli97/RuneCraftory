package io.github.flemmli97.runecraftory.common.datapack.manager.npc;

import com.google.common.collect.ImmutableList;
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

import java.util.List;
import java.util.Map;
import java.util.Random;

public class NPCLookManager extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().create();

    private Map<ResourceLocation, NPCData.NPCLook> data = ImmutableMap.of();
    private List<NPCData.NPCLook> selectable = ImmutableList.of();

    public NPCLookManager() {
        super(GSON, "npc_looks");
    }

    public NPCData.NPCLook get(ResourceLocation res) {
        return this.data.getOrDefault(res, NPCData.NPCLook.DEFAULT_LOOK);
    }

    public NPCData.NPCLook getRandom(Random random, boolean male) {
        if (this.selectable.isEmpty())
            return NPCData.NPCLook.DEFAULT_LOOK;
        return this.selectable.get(random.nextInt(this.selectable.size()));
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler) {
        ImmutableMap.Builder<ResourceLocation, NPCData.NPCLook> builder = ImmutableMap.builder();
        map.forEach((fres, el) -> {
            try {
                JsonObject obj = el.getAsJsonObject();
                builder.put(fres, NPCData.NPCLook.CODEC.parse(JsonOps.INSTANCE, obj)
                        .getOrThrow(false, RuneCraftory.logger::error));
            } catch (Exception ex) {
                RuneCraftory.logger.error("Couldnt parse npc look json {}", fres);
                ex.fillInStackTrace();
            }
        });
        this.data = builder.build();
        ImmutableList.Builder<NPCData.NPCLook> selectable = ImmutableList.builder();
        this.data.entrySet().stream().filter(e -> e.getValue().weight() > 0)
                .forEach(e -> selectable.add(e.getValue()));
        this.selectable = selectable.build();
    }
}
