package io.github.flemmli97.runecraftory.common.datapack.manager.npc;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.datapack.ListenerExtension;
import io.github.flemmli97.runecraftory.common.datapack.ReloadableHolder;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.npc.NPCAttackActions;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class NPCActionManager extends SimpleJsonResourceReloadListener implements ListenerExtension {

    public static final ResourceLocation ID = RuneCraftory.modRes("npc_action");
    public static final String DIRECTORY = String.format("%s/%s", ID.getNamespace(), ID.getPath());

    private Map<ResourceLocation, ReloadableHolder<NPCAttackActions>> data = ImmutableMap.of();
    private List<ReloadableHolder<NPCAttackActions>> actions = List.of();

    private HolderLookup.Provider provider;

    public NPCActionManager() {
        super(DataPackHandler.GSON, DIRECTORY);
    }

    public ReloadableHolder<NPCAttackActions> get(ResourceLocation res) {
        return this.data.getOrDefault(res, NPCAttackActions.DEFAULT);
    }

    public ReloadableHolder<NPCAttackActions> getRandom(Random random) {
        if (this.actions.isEmpty())
            return NPCAttackActions.DEFAULT;
        return this.actions.get(random.nextInt(this.actions.size()));
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler) {
        ImmutableMap.Builder<ResourceLocation, ReloadableHolder<NPCAttackActions>> builder = ImmutableMap.builder();
        DynamicOps<JsonElement> ops = this.provider.createSerializationContext(JsonOps.INSTANCE);
        map.forEach((fres, el) -> {
            if (!fres.equals(NPCAttackActions.DEFAULT.id())) {
                try {
                    builder.put(fres, new ReloadableHolder<>(fres, NPCAttackActions.CODEC.parse(ops, el).getOrThrow()));
                } catch (Exception ex) {
                    RuneCraftory.LOGGER.error("Couldn't parse npc actions json {} {}", fres, ex);
                    ex.fillInStackTrace();
                }
            }
        });
        this.data = builder.build();
        this.actions = this.data.values().stream().toList();
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
