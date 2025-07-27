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
import io.github.flemmli97.runecraftory.common.datapack.ReloadableHolder;
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
    public static final String DIRECTORY = String.format("%s/%s", ID.getNamespace(), ID.getPath());

    public static final ResourceLocation DEFAULT_ID = RuneCraftory.modRes("default_npc");

    private Map<ResourceLocation, ReloadableHolder<NPCData>> data = ImmutableMap.of();
    private final WeightedList<ReloadableHolder<NPCData>> view = new WeightedList<>();
    private final WeightedList<ReloadableHolder<NPCData>> viewNoProfessionDefined = new WeightedList<>();

    private HolderLookup.Provider provider;

    public NPCDataManager() {
        super(DataPackHandler.GSON, DIRECTORY);
    }

    public ReloadableHolder<NPCData> get(ResourceLocation res) {
        return this.data.getOrDefault(res, NPCData.DEFAULT);
    }

    public boolean has(ResourceLocation res) {
        return this.data.containsKey(res);
    }

    public ReloadableHolder<NPCData> getRandom(RandomSource random, Predicate<ReloadableHolder<NPCData>> func, @Nullable Predicate<ReloadableHolder<NPCData>> other) {
        return this.view.getRandom(random, NPCData.DEFAULT, func, other);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler) {
        ImmutableMap.Builder<ResourceLocation, ReloadableHolder<NPCData>> builder = ImmutableMap.builder();
        DynamicOps<JsonElement> ops = this.provider.createSerializationContext(JsonOps.INSTANCE);
        map.forEach((fres, el) -> {
            if (!fres.equals(DEFAULT_ID)) {
                try {
                    JsonObject obj = el.getAsJsonObject();
                    builder.put(fres, new ReloadableHolder<>(fres, NPCData.CODEC.parse(ops, obj).getOrThrow()));
                } catch (Exception ex) {
                    RuneCraftory.LOGGER.error("Couldn't parse npc data json {} {}", fres, ex);
                    ex.fillInStackTrace();
                }
            }
        });
        this.data = builder.build();
        this.view.setList(this.data.values().stream().map(d -> WeightedEntry.wrap(d, d.value().weight())).toList());
        this.viewNoProfessionDefined.setList(this.data.values().stream().filter(d -> d.value().profession().isEmpty()).map(d -> WeightedEntry.wrap(d, d.value().weight())).toList());
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
