package io.github.flemmli97.runecraftory.common.datapack.manager;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.attachment.Skills;
import io.github.flemmli97.runecraftory.api.datapack.SkillProperties;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.datapack.ListenerExtension;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SkillPropertiesManager extends SimpleJsonResourceReloadListener implements ListenerExtension {

    public static final ResourceLocation ID = RuneCraftory.modRes("skill");
    public static final String DIRECTORY = String.format("%s/%s", ID.getNamespace(), ID.getPath());

    private Map<Skills, SkillProperties> propertiesMap = new EnumMap<>(Skills.class);

    public SkillPropertiesManager() {
        super(DataPackHandler.GSON, DIRECTORY);
    }

    public SkillProperties getPropertiesFor(Skills skills) {
        return this.propertiesMap.getOrDefault(skills, SkillProperties.DEFAULT);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> data, ResourceManager manager, ProfilerFiller profiler) {
        EnumMap<Skills, SkillProperties> propertiesBuilder = new EnumMap<>(Skills.class);
        data.forEach((key, el) -> {
            try {
                SkillProperties props = SkillProperties.CODEC.parse(JsonOps.INSTANCE, el).getOrThrow();
                Skills skills = Skills.valueOf(key.getPath().toUpperCase(Locale.ROOT));
                propertiesBuilder.put(skills, props);
            } catch (Exception ex) {
                RuneCraftory.LOGGER.error("Couldn't parse skill properties json {} {}", key, ex, ex.fillInStackTrace());
            }
        });
        List<Skills> missing = new ArrayList<>();
        for (Skills skill : Skills.values()) {
            if (propertiesBuilder.containsKey(skill))
                continue;
            missing.add(skill);
        }
        if (!missing.isEmpty())
            throw new IllegalStateException("Some skills are missing their properties. " + missing);
        this.propertiesMap = propertiesBuilder;
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    @Override
    public void insertRegistryAccess(HolderLookup.Provider provider) {
    }
}
