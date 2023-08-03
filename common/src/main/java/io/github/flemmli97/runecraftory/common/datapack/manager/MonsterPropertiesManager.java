package io.github.flemmli97.runecraftory.common.datapack.manager;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.EntityProperties;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.EntityType;

import java.util.HashMap;
import java.util.Map;

public class MonsterPropertiesManager extends SimpleJsonResourceReloadListener {

    public static final String DIRECTORY = "monster_properties";

    private static final Gson GSON = new GsonBuilder().create();

    private Map<ResourceLocation, EntityProperties> propertiesMap = new HashMap<>();

    public MonsterPropertiesManager() {
        super(GSON, DIRECTORY);
    }

    public EntityProperties getPropertiesFor(EntityType<?> type) {
        ResourceLocation res = PlatformUtils.INSTANCE.entities().getIDFrom(type);
        return this.propertiesMap.getOrDefault(res, EntityProperties.DEFAULT_PROP);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> data, ResourceManager manager, ProfilerFiller profiler) {
        ImmutableMap.Builder<ResourceLocation, EntityProperties> propertiesBuilder = new ImmutableMap.Builder<>();
        data.forEach((key, el) -> {
            try {
                EntityProperties props = EntityProperties.CODEC.parse(JsonOps.INSTANCE, el)
                        .getOrThrow(false, RuneCraftory.logger::error);
                propertiesBuilder.put(key, props);
            } catch (Exception ex) {
                RuneCraftory.logger.error("Couldnt parse entity properties json {}", key);
                ex.fillInStackTrace();
            }
        });
        this.propertiesMap = propertiesBuilder.build();
    }
}
