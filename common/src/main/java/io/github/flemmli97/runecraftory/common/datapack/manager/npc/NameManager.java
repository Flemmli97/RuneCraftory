package io.github.flemmli97.runecraftory.common.datapack.manager.npc;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.datapack.ListenerExtension;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class NameManager extends SimpleJsonResourceReloadListener implements ListenerExtension {

    public static final ResourceLocation ID = RuneCraftory.modRes("names");

    private Map<String, List<String>> surnames;
    private Map<String, List<String>> maleNames;
    private Map<String, List<String>> femaleNames;

    public NameManager() {
        super(DataPackHandler.GSON, ID.toString());
    }

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
    protected void apply(Map<ResourceLocation, JsonElement> data, ResourceManager manager, ProfilerFiller profiler) {
        ImmutableMap.Builder<String, List<String>> surnames = ImmutableMap.builder();
        ImmutableMap.Builder<String, List<String>> maleNames = ImmutableMap.builder();
        ImmutableMap.Builder<String, List<String>> femaleNames = ImmutableMap.builder();
        data.forEach((res, el) -> {
            if (!el.isJsonObject()) {
                RuneCraftory.LOGGER.error("Name file {} is invalid ", res);
            } else {
                JsonObject obj = el.getAsJsonObject();
                ImmutableList.Builder<String> lang_surnames = ImmutableList.builder();
                ImmutableList.Builder<String> lang_maleNames = ImmutableList.builder();
                ImmutableList.Builder<String> lang_femaleNames = ImmutableList.builder();
                GsonHelper.getAsJsonArray(obj, "surnames", new JsonArray())
                        .forEach(e -> lang_surnames.add(e.getAsString()));
                GsonHelper.getAsJsonArray(obj, "male_names", new JsonArray())
                        .forEach(e -> lang_maleNames.add(e.getAsString()));
                GsonHelper.getAsJsonArray(obj, "female_names", new JsonArray())
                        .forEach(e -> lang_femaleNames.add(e.getAsString()));
                surnames.put(res.getPath(), lang_surnames.build());
                maleNames.put(res.getPath(), lang_maleNames.build());
                femaleNames.put(res.getPath(), lang_femaleNames.build());
            }
        });
        this.surnames = surnames.build();
        this.maleNames = maleNames.build();
        this.femaleNames = femaleNames.build();
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    @Override
    public void insertRegistryAccess(HolderLookup.Provider provider) {
    }
}
