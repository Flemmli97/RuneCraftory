package io.github.flemmli97.runecraftory.common.datapack.manager.npc;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.npc.NPCData;
import io.github.flemmli97.runecraftory.api.datapack.npc.NPCLook;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.datapack.ListenerExtension;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.List;
import java.util.Map;

public class NPCLookManager extends SimpleJsonResourceReloadListener implements ListenerExtension {

    public static final ResourceLocation ID = RuneCraftory.modRes("npc_looks");
    public static final String DIRECTORY = String.format("%s/%s", ID.getNamespace(), ID.getPath());

    public static final ResourceLocation DEFAULT_ID = RuneCraftory.modRes("default_look");

    private Map<ResourceLocation, NPCLook> keyData = ImmutableMap.of();
    private Map<NPCLook, ResourceLocation> dataKey = ImmutableMap.of();
    private List<NPCLook> selectable = ImmutableList.of();

    private HolderLookup.Provider provider;

    public NPCLookManager() {
        super(DataPackHandler.GSON, DIRECTORY);
    }

    public NPCLook get(ResourceLocation res) {
        return this.keyData.getOrDefault(res, NPCLook.DEFAULT_LOOK);
    }

    public ResourceLocation getId(NPCLook data) {
        return this.dataKey.getOrDefault(data, DEFAULT_ID);
    }

    public NPCLook getRandom(RandomSource random, boolean male) {
        if (this.selectable.isEmpty())
            return NPCLook.DEFAULT_LOOK;
        List<NPCLook> looks = this.selectable.stream().filter(l ->
                l.gender() == NPCData.Gender.UNDEFINED
                        || l.gender() == (male ? NPCData.Gender.MALE : NPCData.Gender.FEMALE)).toList();
        if (looks.isEmpty())
            return NPCLook.DEFAULT_LOOK;
        return looks.get(random.nextInt(looks.size()));
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler) {
        ImmutableMap.Builder<ResourceLocation, NPCLook> builder = ImmutableMap.builder();
        DynamicOps<JsonElement> ops = this.provider.createSerializationContext(JsonOps.INSTANCE);
        map.forEach((fres, el) -> {
            if (!fres.equals(DEFAULT_ID)) {
                try {
                    JsonObject obj = el.getAsJsonObject();
                    builder.put(fres, NPCLook.CODEC.parse(ops, obj).getOrThrow());
                } catch (Exception ex) {
                    RuneCraftory.LOGGER.error("Couldn't parse npc look json {} {}", fres, ex);
                    ex.fillInStackTrace();
                }
            }
        });
        builder.put(NPCLook.DEFAULT_LOOK_ID, NPCLook.DEFAULT_LOOK);
        this.keyData = builder.build();
        ImmutableMap.Builder<NPCLook, ResourceLocation> reverse = ImmutableMap.builder();
        this.keyData.forEach((resourceLocation, data) -> reverse.put(data, resourceLocation));
        this.dataKey = reverse.build();
        ImmutableList.Builder<NPCLook> selectable = ImmutableList.builder();
        this.keyData.entrySet().stream().filter(e -> e.getValue().weight() > 0)
                .forEach(e -> selectable.add(e.getValue()));
        this.selectable = selectable.build();
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    @Override
    public void insertRegistryAccess(HolderLookup.Provider provider) {
        this.provider = provider;
    }
}
