package io.github.flemmli97.runecraftory.common.datapack.manager.npc;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import io.github.flemmli97.runecraftory.RuneCraftory;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class NameManager extends SimplePreparableReloadListener<Map<ResourceLocation, JsonObject>> {

    private static final Gson GSON = new GsonBuilder().create();

    public static final String DIRECTORY = "names";

    private Map<String, List<String>> surnames;
    private Map<String, List<String>> maleNames;
    private Map<String, List<String>> femaleNames;

    public String getRandomSurname(Random random, String lang) {
        if (this.surnames.isEmpty())
            return null;
        List<String> names = this.surnames.get(lang);
        if (names == null)
            return null;
        return names.get(random.nextInt(names.size()));
    }

    public String getRandomName(Random random, String lang, boolean male) {
        List<String> names;
        if (male) {
            if (this.maleNames.isEmpty())
                return null;
            names = this.maleNames.get(lang);
        } else {
            if (this.femaleNames.isEmpty())
                return null;
            names = this.femaleNames.get(lang);
        }
        if (names == null)
            return null;
        return names.get(random.nextInt(names.size()));
    }

    public String getRandomFullName(Random random, boolean male) {
        List<String> langs;
        if (male) {
            langs = this.maleNames.keySet().stream().toList();
            if (langs.isEmpty())
                return null;
        } else {
            langs = this.femaleNames.keySet().stream().toList();
        }
        if (langs.isEmpty())
            return null;
        String lang = langs.get(random.nextInt(langs.size()));
        return this.getRandomName(random, lang, male) + " " + this.getRandomSurname(random, lang);
    }

    @Override
    protected Map<ResourceLocation, JsonObject> prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        HashMap<ResourceLocation, JsonObject> map = Maps.newHashMap();
        int i = DIRECTORY.length() + 1;
        for (ResourceLocation fileRes : resourceManager.listResources(DIRECTORY, string -> string.endsWith(".json"))) {
            String path = fileRes.getPath();
            path = path.substring(i, path.length() - ".json".length());
            ResourceLocation res = new ResourceLocation(fileRes.getNamespace(), path);
            try {
                try (Resource resource = resourceManager.getResource(fileRes)) {
                    try (InputStream inputStream = resource.getInputStream(); BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                        JsonObject obj = GsonHelper.fromJson(GSON, reader, JsonObject.class);
                        if (obj != null) {
                            map.put(res, obj);
                        }
                    }
                }
            } catch (JsonParseException | IOException | IllegalArgumentException exception) {
                RuneCraftory.LOGGER.error("Couldn't parse data file {} {}", res, exception);
            }
        }
        return map;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonObject> data, ResourceManager manager, ProfilerFiller profiler) {
        ImmutableMap.Builder<String, List<String>> surnames = ImmutableMap.builder();
        ImmutableMap.Builder<String, List<String>> maleNames = ImmutableMap.builder();
        ImmutableMap.Builder<String, List<String>> femaleNames = ImmutableMap.builder();
        data.forEach((res, arr) -> {
            ImmutableList.Builder<String> lang_surnames = ImmutableList.builder();
            ImmutableList.Builder<String> lang_maleNames = ImmutableList.builder();
            ImmutableList.Builder<String> lang_femaleNames = ImmutableList.builder();
            GsonHelper.getAsJsonArray(arr, "surnames", new JsonArray())
                    .forEach(e -> lang_surnames.add(e.getAsString()));
            GsonHelper.getAsJsonArray(arr, "male_names", new JsonArray())
                    .forEach(e -> lang_maleNames.add(e.getAsString()));
            GsonHelper.getAsJsonArray(arr, "female_names", new JsonArray())
                    .forEach(e -> lang_femaleNames.add(e.getAsString()));
            surnames.put(res.getPath(), lang_surnames.build());
            maleNames.put(res.getPath(), lang_maleNames.build());
            femaleNames.put(res.getPath(), lang_femaleNames.build());
        });
        this.surnames = surnames.build();
        this.maleNames = maleNames.build();
        this.femaleNames = femaleNames.build();
    }
}
