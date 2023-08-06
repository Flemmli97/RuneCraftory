package io.github.flemmli97.runecraftory.common.datapack.manager;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.api.datapack.SpellProperties;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashMap;
import java.util.Map;

public class SpellPropertiesManager extends SimpleJsonResourceReloadListener {

    public static final String DIRECTORY = "spells";

    private static final Gson GSON = new GsonBuilder().create();

    private Map<ResourceLocation, SpellProperties> propertiesMap = new HashMap<>();

    public SpellPropertiesManager() {
        super(GSON, DIRECTORY);
    }

    public SpellProperties getPropertiesFor(Spell spell) {
        ResourceLocation res = ModSpells.SPELL_REGISTRY.get().getIDFrom(spell);
        return this.propertiesMap.getOrDefault(res, SpellProperties.DEFAULT_PROP);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> data, ResourceManager manager, ProfilerFiller profiler) {
        ImmutableMap.Builder<ResourceLocation, SpellProperties> propertiesBuilder = new ImmutableMap.Builder<>();
        data.forEach((key, el) -> {
            try {
                SpellProperties props = SpellProperties.CODEC.parse(JsonOps.INSTANCE, el)
                        .getOrThrow(false, RuneCraftory.logger::error);
                propertiesBuilder.put(key, props);
            } catch (Exception ex) {
                RuneCraftory.logger.error("Couldnt parse spell properties json {} {}", key, ex);
                ex.fillInStackTrace();
            }
        });
        this.propertiesMap = propertiesBuilder.build();
    }
}
