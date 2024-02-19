package io.github.flemmli97.runecraftory.common.datapack.manager;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.SkillProperties;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import net.minecraft.network.FriendlyByteBuf;
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

public class SkillPropertiesManager extends SimplePreparableReloadListener<Map<ResourceLocation, JsonElement>> {

    public static final String DIRECTORY = "skills";

    private static final Gson GSON = new GsonBuilder().create();

    private Map<EnumSkills, SkillProperties> propertiesMap = new EnumMap<>(EnumSkills.class);

    public SkillProperties getPropertiesFor(EnumSkills skills) {
        return this.propertiesMap.getOrDefault(skills, SkillProperties.DEFAULT);
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
        EnumMap<EnumSkills, SkillProperties> propertiesBuilder = new EnumMap<>(EnumSkills.class);
        data.forEach((key, el) -> {
            try {
                SkillProperties props = SkillProperties.CODEC.parse(JsonOps.INSTANCE, el)
                        .getOrThrow(false, RuneCraftory.logger::error);
                EnumSkills skills = EnumSkills.valueOf(key.getPath().toUpperCase(Locale.ROOT));
                propertiesBuilder.put(skills, props);
            } catch (Exception ex) {
                RuneCraftory.logger.error("Couldnt parse skill properties json {} {}", key, ex);
                ex.fillInStackTrace();
            }
        });
        List<EnumSkills> missing = new ArrayList<>();
        for (EnumSkills skill : EnumSkills.values()) {
            if (propertiesBuilder.containsKey(skill))
                continue;
            missing.add(skill);
        }
        if (!missing.isEmpty())
            throw new IllegalStateException("Some skills are missing their properties. " + missing);
        this.propertiesMap = propertiesBuilder;
    }

    public void toPacket(FriendlyByteBuf buffer) {
        buffer.writeMap(this.propertiesMap, FriendlyByteBuf::writeEnum,
                ((friendlyByteBuf, skillProperties) -> friendlyByteBuf.writeInt(skillProperties.maxLevel())));
    }

    public void fromPacket(FriendlyByteBuf buffer) {
        this.propertiesMap.putAll(buffer.readMap(b -> b.readEnum(EnumSkills.class), b ->
                new SkillProperties(b.readInt(), 0, 0, 0, 0, 0, 0)));
    }
}
