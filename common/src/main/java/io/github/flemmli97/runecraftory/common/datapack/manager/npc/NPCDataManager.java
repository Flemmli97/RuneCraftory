package io.github.flemmli97.runecraftory.common.datapack.manager.npc;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.npc.NPCData;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.datapack.ListenerExtension;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.util.random.WeightedEntry;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Predicate;

public class NPCDataManager extends SimpleJsonResourceReloadListener implements ListenerExtension {

    public static final ResourceLocation ID = RuneCraftory.modRes("npc_data");

    public static final ResourceLocation DEFAULT_ID = RuneCraftory.modRes("default_npc");

    private Map<ResourceLocation, NPCData> keyData = ImmutableMap.of();
    private Map<NPCData, ResourceLocation> dataKey = ImmutableMap.of();
    private final WeightedList<NPCData> view = new WeightedList<>();
    private final WeightedList<NPCData> viewNoJobDef = new WeightedList<>();

    private HolderLookup.Provider provider;

    public NPCDataManager() {
        super(DataPackHandler.GSON, ID.getPath());
    }

    public NPCData get(ResourceLocation res) {
        return this.keyData.getOrDefault(res, NPCData.DEFAULT_DATA);
    }

    public boolean has(ResourceLocation res) {
        return this.keyData.containsKey(res);
    }

    public ResourceLocation getId(NPCData data) {
        return this.dataKey.getOrDefault(data, DEFAULT_ID);
    }

    public NPCData getRandom(RandomSource random, Predicate<NPCData> func, @Nullable Predicate<NPCData> other) {
        return this.view.getRandom(random, NPCData.DEFAULT_DATA, func, other);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler) {
        ImmutableMap.Builder<ResourceLocation, NPCData> builder = ImmutableMap.builder();
        DynamicOps<JsonElement> ops = this.provider.createSerializationContext(JsonOps.INSTANCE);
        map.forEach((fres, el) -> {
            if (!fres.equals(DEFAULT_ID)) {
                try {
                    JsonObject obj = el.getAsJsonObject();
                    builder.put(fres, NPCData.CODEC.parse(ops, obj).getOrThrow());
                } catch (Exception ex) {
                    RuneCraftory.LOGGER.error("Couldn't parse npc data json {} {}", fres, ex);
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

    @Override
    public ResourceLocation id() {
        return ID;
    }

    @Override
    public void insertRegistryAccess(HolderLookup.Provider provider) {
        this.provider = provider;
    }
}
