package io.github.flemmli97.runecraftory.common.datapack.manager.npc;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.datapack.ListenerExtension;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.actions.NPCAttackActions;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class NPCActionManager extends SimpleJsonResourceReloadListener implements ListenerExtension {

    public static final ResourceLocation ID = RuneCraftory.modRes("npc_actions");

    public static final ResourceLocation DEFAULT_ID = RuneCraftory.modRes("default_action");

    private Map<ResourceLocation, NPCAttackActions> keyData = ImmutableMap.of();
    private Map<NPCAttackActions, ResourceLocation> dataKey = ImmutableMap.of();
    private List<NPCAttackActions> actions = List.of();

    private HolderLookup.Provider provider;

    public NPCActionManager() {
        super(DataPackHandler.GSON, ID.toString());
    }

    public NPCAttackActions get(ResourceLocation res) {
        return this.keyData.getOrDefault(res, NPCAttackActions.DEFAULT);
    }

    public ResourceLocation getId(NPCAttackActions data) {
        return this.dataKey.getOrDefault(data, DEFAULT_ID);
    }

    public NPCAttackActions getRandom(Random random) {
        if (this.actions.isEmpty())
            return NPCAttackActions.DEFAULT;
        return this.actions.get(random.nextInt(this.actions.size()));
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler) {
        ImmutableMap.Builder<ResourceLocation, NPCAttackActions> builder = ImmutableMap.builder();
        DynamicOps<JsonElement> ops = this.provider.createSerializationContext(JsonOps.INSTANCE);
        map.forEach((fres, el) -> {
            if (!fres.equals(DEFAULT_ID)) {
                try {
                    JsonObject obj = el.getAsJsonObject();
                    builder.put(fres, NPCAttackActions.CODEC.parse(ops, obj).getOrThrow());
                } catch (Exception ex) {
                    RuneCraftory.LOGGER.error("Couldn't parse npc actions json {} {}", fres, ex);
                    ex.fillInStackTrace();
                }
            }
        });
        this.keyData = builder.build();
        ImmutableMap.Builder<NPCAttackActions, ResourceLocation> reverse = ImmutableMap.builder();
        this.keyData.forEach((resourceLocation, data) -> reverse.put(data, resourceLocation));
        this.dataKey = reverse.build();
        this.actions = this.keyData.values().stream().toList();
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
