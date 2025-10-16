package io.github.flemmli97.runecraftory.common.datapack.manager;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.EntityProperties;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.datapack.ListenerExtension;
import io.github.flemmli97.runecraftory.common.utils.HolderUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.EntityType;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class MonsterPropertiesManager extends SimpleJsonResourceReloadListener implements ListenerExtension {

    public static final ResourceLocation ID = RuneCraftory.modRes("monster_property");
    public static final String DIRECTORY = String.format("%s/%s", ID.getNamespace(), ID.getPath());

    private Map<EntityType<?>, EntityProperties> propertiesMap = new HashMap<>();

    private HolderLookup.Provider provider;

    public MonsterPropertiesManager() {
        super(DataPackHandler.GSON, DIRECTORY);
    }

    public EntityProperties getPropertiesFor(EntityType<?> type) {
        return this.propertiesMap.getOrDefault(type, EntityProperties.DEFAULT_PROP);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> data, ResourceManager manager, ProfilerFiller profiler) {
        ImmutableMap.Builder<EntityType<?>, EntityProperties> propertiesBuilder = new ImmutableMap.Builder<>();
        DynamicOps<JsonElement> ops = this.provider.createSerializationContext(JsonOps.INSTANCE);
        data.forEach((key, el) -> {
            try {
                EntityType<?> type = HolderUtils.get(this.provider, Registries.ENTITY_TYPE, key)
                        .orElseThrow(() -> new NoSuchElementException("Entity with id " + key + " doesn't exist"));
                EntityProperties props = EntityProperties.CODEC.parse(ops, el).getOrThrow();
                propertiesBuilder.put(type, props);
            } catch (Exception ex) {
                RuneCraftory.LOGGER.error("Couldn't parse entity properties json {} {}", key, ex, ex.fillInStackTrace());
            }
        });
        this.propertiesMap = propertiesBuilder.build();
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
