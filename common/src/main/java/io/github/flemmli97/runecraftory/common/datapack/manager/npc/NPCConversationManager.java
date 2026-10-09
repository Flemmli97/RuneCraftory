package io.github.flemmli97.runecraftory.common.datapack.manager.npc;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.npc.ConversationSet;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.datapack.ListenerExtension;
import io.github.flemmli97.runecraftory.common.datapack.ReloadableHolder;
import io.github.flemmli97.runecraftory.common.lib.LibConstants;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;
import java.util.function.Function;

public class NPCConversationManager extends SimpleJsonResourceReloadListener implements ListenerExtension {

    public static final ResourceLocation ID = RuneCraftory.modRes("conversation");
    public static final String DIRECTORY = String.format("%s/%s", ID.getNamespace(), ID.getPath());
    public static final Function<ResourceLocation, ConversationSet> MISSING = id -> new ConversationSet("npc.conversation.missing", id, Map.of());

    private Map<ResourceLocation, ReloadableHolder<ConversationSet>> data = ImmutableMap.of();

    private HolderLookup.Provider provider;

    public NPCConversationManager() {
        super(DataPackHandler.GSON, DIRECTORY);
    }

    public ReloadableHolder<ConversationSet> get(ResourceLocation res) {
        return this.get(res, MISSING.apply(res));
    }

    public ReloadableHolder<ConversationSet> get(ResourceLocation res, ConversationSet fallback) {
        return this.data.getOrDefault(res, new ReloadableHolder<>(LibConstants.EMPTY, fallback));
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler) {
        ImmutableMap.Builder<ResourceLocation, ReloadableHolder<ConversationSet>> builder = ImmutableMap.builder();
        DynamicOps<JsonElement> ops = this.provider.createSerializationContext(JsonOps.INSTANCE);
        map.forEach((fres, el) -> {
            try {
                JsonObject obj = el.getAsJsonObject();
                ConversationSet conversations = ConversationSet.CODEC.parse(ops, obj).getOrThrow();
                builder.put(fres, new ReloadableHolder<>(fres, conversations));
            } catch (Exception ex) {
                RuneCraftory.LOGGER.error("Couldn't parse npc data json {} {}", fres, ex, ex.fillInStackTrace());
            }
        });
        this.data = builder.build();
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
