package io.github.flemmli97.runecraftory.common.datapack.manager;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.EntityProperties;
import io.github.flemmli97.runecraftory.api.datapack.GsonInstances;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.EntityType;

import java.util.HashMap;
import java.util.Map;

public class MonsterPropertiesManager extends SimpleJsonResourceReloadListener {

    public static final String DIRECTORY = "monster_properties";

    private Map<ResourceLocation, EntityProperties> propertiesMap = new HashMap<>();

    public MonsterPropertiesManager() {
        super(GsonInstances.GSON, DIRECTORY);
    }

    public EntityProperties getPropertiesFor(EntityType<?> type) {
        ResourceLocation res = Registry.ENTITY_TYPE.getKey(type);
        return this.propertiesMap.getOrDefault(res, EntityProperties.DEFAULT_PROP);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> data, ResourceManager manager, ProfilerFiller profiler) {
        ImmutableMap.Builder<ResourceLocation, EntityProperties> propertiesBuilder = new ImmutableMap.Builder<>();
        data.forEach((key, el) -> {
            try {
                EntityProperties props = EntityProperties.CODEC.parse(JsonOps.INSTANCE, el)
                        .getOrThrow(false, RuneCraftory.LOGGER::error);
                propertiesBuilder.put(key, props);
            } catch (Exception ex) {
                RuneCraftory.LOGGER.error("Couldnt parse entity properties json {} {}", key, ex);
                ex.fillInStackTrace();
            }
        });
        this.propertiesMap = propertiesBuilder.build();
    }
}
