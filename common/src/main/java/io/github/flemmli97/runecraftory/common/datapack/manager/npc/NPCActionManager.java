package io.github.flemmli97.runecraftory.common.datapack.manager.npc;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.actions.NPCAttackActions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class NPCActionManager extends SimpleJsonResourceReloadListener {

    public static final String DIRECTORY = "npc_actions";

    private static final Gson GSON = new GsonBuilder().create();
    public static final ResourceLocation DEFAULT_ID = new ResourceLocation(RuneCraftory.MODID, "default_action");

    private Map<ResourceLocation, NPCAttackActions> keyData = ImmutableMap.of();
    private Map<NPCAttackActions, ResourceLocation> dataKey = ImmutableMap.of();
    private List<NPCAttackActions> actions = List.of();

    public NPCActionManager() {
        super(GSON, DIRECTORY);
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
        map.forEach((fres, el) -> {
            if (!fres.equals(DEFAULT_ID)) {
                try {
                    JsonObject obj = el.getAsJsonObject();
                    builder.put(fres, NPCAttackActions.CODEC.parse(JsonOps.INSTANCE, obj)
                            .getOrThrow(false, RuneCraftory.logger::error));
                } catch (Exception ex) {
                    RuneCraftory.logger.error("Couldnt parse npc actions json {} {}", fres, ex);
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
}
