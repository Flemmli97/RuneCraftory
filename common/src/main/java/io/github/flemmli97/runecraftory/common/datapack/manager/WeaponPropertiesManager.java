package io.github.flemmli97.runecraftory.common.datapack.manager;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.WeaponTypeProperties;
import io.github.flemmli97.runecraftory.api.enums.EnumWeaponType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class WeaponPropertiesManager extends SimplePreparableReloadListener<Map<ResourceLocation, JsonElement>> {

    public static final String DIRECTORY = "weapon_types";

    private static final Gson GSON = new GsonBuilder().create();

    private Map<EnumWeaponType, WeaponTypeProperties> propertiesMap = new EnumMap<>(EnumWeaponType.class);

    public WeaponTypeProperties getPropertiesFor(EnumWeaponType skills) {
        return this.propertiesMap.getOrDefault(skills, WeaponTypeProperties.DEFAULT);
    }

    @Override
    protected Map<ResourceLocation, JsonElement> prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        HashMap<ResourceLocation, JsonElement> map = Maps.newHashMap();
        int i = DIRECTORY.length() + 1;
        for (ResourceLocation resourceLocation : resourceManager.listResources(DIRECTORY, string -> string.endsWith(".json"))) {
            if (!resourceLocation.getNamespace().equals(RuneCraftory.MODID))
                continue;
            String path = resourceLocation.getPath();
            path = path.substring(i, path.length() - ".json".length());
            ResourceLocation res = new ResourceLocation(resourceLocation.getNamespace(), path);
            try {
                try (Resource resource = resourceManager.getResource(resourceLocation)) {
                    try (InputStream inputStream = resource.getInputStream(); BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                        JsonElement element = GsonHelper.fromJson(GSON, reader, JsonElement.class);
                        if (element != null) {
                            map.put(res, element);
                        }
                    }
                }
            } catch (JsonParseException | IOException | IllegalArgumentException exception) {
                RuneCraftory.logger.error("Couldn't parse data file {} {}", res, exception);
            }
        }
        return map;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> data, ResourceManager manager, ProfilerFiller profiler) {
        EnumMap<EnumWeaponType, WeaponTypeProperties> propertiesBuilder = new EnumMap<>(EnumWeaponType.class);
        data.forEach((key, el) -> {
            try {
                WeaponTypeProperties props = WeaponTypeProperties.CODEC.parse(JsonOps.INSTANCE, el)
                        .getOrThrow(false, RuneCraftory.logger::error);
                EnumWeaponType skills = EnumWeaponType.valueOf(key.getPath().toUpperCase(Locale.ROOT));
                propertiesBuilder.put(skills, props);
            } catch (Exception ex) {
                RuneCraftory.logger.error("Couldnt parse weapon properties json {} {}", key, ex);
                ex.fillInStackTrace();
            }
        });
        List<EnumWeaponType> missing = new ArrayList<>();
        for (EnumWeaponType skill : EnumWeaponType.values()) {
            if (propertiesBuilder.containsKey(skill))
                continue;
            missing.add(skill);
        }
        if (!missing.isEmpty())
            throw new IllegalStateException("Some weapon types are missing their properties. " + missing);
        this.propertiesMap = propertiesBuilder;
    }
}
