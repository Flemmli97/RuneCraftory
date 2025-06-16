package io.github.flemmli97.runecraftory.common.datapack.manager;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.SpellProperties;
import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.datapack.ListenerExtension;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.utils.HolderUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class SpellPropertiesManager extends SimpleJsonResourceReloadListener implements ListenerExtension {

    public static final ResourceLocation ID = RuneCraftory.modRes("spells");

    private Map<Spell, SpellProperties> propertiesMap = new HashMap<>();

    private HolderLookup.Provider provider;

    public SpellPropertiesManager() {
        super(DataPackHandler.GSON, ID.toString());
    }

    public SpellProperties getPropertiesFor(Spell spell) {
        return this.propertiesMap.getOrDefault(spell, SpellProperties.DEFAULT_PROP);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> data, ResourceManager manager, ProfilerFiller profiler) {
        ImmutableMap.Builder<Spell, SpellProperties> propertiesBuilder = new ImmutableMap.Builder<>();
        DynamicOps<JsonElement> ops = this.provider.createSerializationContext(JsonOps.INSTANCE);
        data.forEach((key, el) -> {
            try {
                Spell spell = HolderUtils.get(this.provider, ModSpells.SPELL_REGISTRY_KEY, key)
                        .orElseThrow(() -> new NoSuchElementException("Spell with id " + key + " doesn't exist"));
                SpellProperties props = SpellProperties.CODEC.parse(ops, el).getOrThrow();
                propertiesBuilder.put(spell, props);
            } catch (Exception ex) {
                RuneCraftory.LOGGER.error("Couldn't parse spell properties json {} {}", key, ex);
                ex.fillInStackTrace();
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
