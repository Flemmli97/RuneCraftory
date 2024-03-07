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

    public static final String DIRECTORY = "npc_looks";

    private static final Gson GSON = new GsonBuilder().create();
    public static final ResourceLocation DEFAULT_ID = new ResourceLocation(RuneCraftory.MODID, "default_look");

    private Map<ResourceLocation, NPCData.NPCLook> keyData = ImmutableMap.of();
    private Map<NPCData.NPCLook, ResourceLocation> dataKey = ImmutableMap.of();
    private List<NPCData.NPCLook> selectable = ImmutableList.of();

    public NPCLookManager() {
        super(GSON, DIRECTORY);
    }

    public NPCData.NPCLook get(ResourceLocation res) {
        return this.keyData.getOrDefault(res, NPCData.NPCLook.DEFAULT_LOOK);
    }

    public ResourceLocation getId(NPCData.NPCLook data) {
        return this.dataKey.getOrDefault(data, DEFAULT_ID);
    }

    public NPCData.NPCLook getRandom(Random random, boolean male) {
        if (this.selectable.isEmpty())
            return NPCData.NPCLook.DEFAULT_LOOK;
        List<NPCData.NPCLook> looks = this.selectable.stream().filter(l ->
                l.gender() == NPCData.Gender.UNDEFINED
                        || l.gender() == (male ? NPCData.Gender.MALE : NPCData.Gender.FEMALE)).toList();
        if (looks.isEmpty())
            return NPCData.NPCLook.DEFAULT_LOOK;
        return looks.get(random.nextInt(looks.size()));
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler) {
        ImmutableMap.Builder<ResourceLocation, NPCData.NPCLook> builder = ImmutableMap.builder();
        map.forEach((fres, el) -> {
            if (!fres.equals(DEFAULT_ID)) {
                try {
                    JsonObject obj = el.getAsJsonObject();
                    builder.put(fres, NPCData.NPCLook.CODEC.parse(JsonOps.INSTANCE, obj)
                            .getOrThrow(false, RuneCraftory.LOGGER::error));
                } catch (Exception ex) {
                    RuneCraftory.LOGGER.error("Couldnt parse npc look json {} {}", fres, ex);
                    ex.fillInStackTrace();
                }
            }
        });
        builder.put(NPCData.NPCLook.DEFAULT_LOOK_ID, NPCData.NPCLook.DEFAULT_LOOK);
        this.keyData = builder.build();
        ImmutableMap.Builder<NPCData.NPCLook, ResourceLocation> reverse = ImmutableMap.builder();
        this.keyData.forEach((resourceLocation, data) -> reverse.put(data, resourceLocation));
        this.dataKey = reverse.build();
        ImmutableList.Builder<NPCData.NPCLook> selectable = ImmutableList.builder();
        this.keyData.entrySet().stream().filter(e -> e.getValue().weight() > 0)
                .forEach(e -> selectable.add(e.getValue()));
        this.selectable = selectable.build();
    }
}
