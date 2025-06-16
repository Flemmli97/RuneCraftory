package io.github.flemmli97.runecraftory.common.datapack.manager;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.SkillProperties;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
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

    public static final ResourceLocation ID = RuneCraftory.modRes("skills");

    private Map<EnumSkills, SkillProperties> propertiesMap = new EnumMap<>(EnumSkills.class);

    public SkillPropertiesManager() {
        super(DataPackHandler.GSON, ID.toString());
    }

    public SkillProperties getPropertiesFor(EnumSkills skills) {
        return this.propertiesMap.getOrDefault(skills, SkillProperties.DEFAULT);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> data, ResourceManager manager, ProfilerFiller profiler) {
        EnumMap<EnumSkills, SkillProperties> propertiesBuilder = new EnumMap<>(EnumSkills.class);
        data.forEach((key, el) -> {
            try {
                SkillProperties props = SkillProperties.CODEC.parse(JsonOps.INSTANCE, el).getOrThrow();
                EnumSkills skills = EnumSkills.valueOf(key.getPath().toUpperCase(Locale.ROOT));
                propertiesBuilder.put(skills, props);
            } catch (Exception ex) {
                RuneCraftory.LOGGER.error("Couldn't parse skill properties json {} {}", key, ex);
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

    @Override
    public ResourceLocation id() {
        return ID;
    }

    @Override
    public void insertRegistryAccess(HolderLookup.Provider provider) {
    }
}
