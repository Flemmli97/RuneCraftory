package io.github.flemmli97.runecraftory.forge.data;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.SkillProperties;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.datapack.manager.SkillPropertiesManager;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class SkillPropertiesgen implements DataProvider {

    private final PackOutput packOutput;
    private final EnumMap<EnumSkills, SkillProperties> skillProps = new EnumMap<>(EnumSkills.class);

    public SkillPropertiesgen(PackOutput packOutput) {
        this.packOutput = packOutput;
    }

    protected void add() {
        this.skillProps.clear();
        this.skillProps.put(EnumSkills.SHORTSWORD, new SkillProperties(100, 0, 0.25f, 0.25f, 0, 0, 1));
        this.skillProps.put(EnumSkills.LONGSWORD, new SkillProperties(100, 0, 0.25f, 0.25f, 0, 0, 1));
        this.skillProps.put(EnumSkills.SPEAR, new SkillProperties(100, 0, 0.25f, 0.25f, 0, 0, 1));
        this.skillProps.put(EnumSkills.HAMMERAXE, new SkillProperties(100, 0, 0.25f, 0.5f, 0, 0, 1));
        this.skillProps.put(EnumSkills.DUAL, new SkillProperties(100, 0, 0.25f, 0.25f, 0, 0, 1));
        this.skillProps.put(EnumSkills.FIST, new SkillProperties(100, 0, 0.25f, 0.25f, 0, 0, 1));

        this.skillProps.put(EnumSkills.FIRE, new SkillProperties(100, 0, 0.5f, 0, 0, 0.2f, 1));
        this.skillProps.put(EnumSkills.WATER, new SkillProperties(100, 0, 0.5f, 0, 0, 0.2f, 1));
        this.skillProps.put(EnumSkills.EARTH, new SkillProperties(100, 0, 0.5f, 0, 0, 0.2f, 1));
        this.skillProps.put(EnumSkills.WIND, new SkillProperties(100, 0, 0.5f, 0, 0, 0.2f, 1));
        this.skillProps.put(EnumSkills.DARK, new SkillProperties(100, 0, 0.5f, 0, 0, 0.2f, 1));
        this.skillProps.put(EnumSkills.LIGHT, new SkillProperties(100, 0, 0.5f, 0, 0, 0.2f, 1));
        this.skillProps.put(EnumSkills.LOVE, new SkillProperties(100, 0, 0.5f, 0, 0, 0.2f, 1));

        this.skillProps.put(EnumSkills.FARMING, new SkillProperties(100, 1, 1, 0, 0.5f, 0, 1));
        this.skillProps.put(EnumSkills.LOGGING, new SkillProperties(100, 1, 1, 0.1f, 0.3f, 0, 1));
        this.skillProps.put(EnumSkills.MINING, new SkillProperties(100, 1, 1, 0.1f, 0.3f, 0, 1));
        this.skillProps.put(EnumSkills.FISHING, new SkillProperties(100, 1, 1, 0, 0, 0.5f, 1));

        this.skillProps.put(EnumSkills.COOKING, new SkillProperties(100, 0, 0.25f, 0, 0.1f, 0, 1));
        this.skillProps.put(EnumSkills.FORGING, new SkillProperties(100, 0, 0.25f, 0.25f, 0.1f, 0, 1));
        this.skillProps.put(EnumSkills.CHEMISTRY, new SkillProperties(100, 0, 0.25f, 0, 0, 0.2f, 1));
        this.skillProps.put(EnumSkills.CRAFTING, new SkillProperties(100, 0, 0.25f, 0, 0.1f, 0.1f, 1));

        this.skillProps.put(EnumSkills.SEARCHING, new SkillProperties(100, 0, 0.3f, 0, 0, 0.1f, 1));
        this.skillProps.put(EnumSkills.WALKING, new SkillProperties(100, 0.5f, 0.125f, 0, 0.1f, 0, 1));
        this.skillProps.put(EnumSkills.SLEEPING, new SkillProperties(100, 2f, 2, 0.5f, 1, 0.5f, 1));
        this.skillProps.put(EnumSkills.EATING, new SkillProperties(100, 1, 2, 0.5f, 0.5f, 0.2f, 1));
        this.skillProps.put(EnumSkills.DEFENCE, new SkillProperties(100, 1.5f, 0, 0, 1, 0, 1));

        this.skillProps.put(EnumSkills.RES_POISON, new SkillProperties(100, 0, 0, 0, 0.1f, 0.05f, 1));
        this.skillProps.put(EnumSkills.RES_SEAL, new SkillProperties(100, 0, 0, 0, 0.1f, 0.05f, 1));
        this.skillProps.put(EnumSkills.RES_PARA, new SkillProperties(100, 0, 0, 0, 0.1f, 0.05f, 1));
        this.skillProps.put(EnumSkills.RES_SLEEP, new SkillProperties(100, 0, 0, 0, 0.1f, 0.05f, 1));
        this.skillProps.put(EnumSkills.RES_FATIGUE, new SkillProperties(100, 0, 0, 0, 0.1f, 0.05f, 1));
        this.skillProps.put(EnumSkills.RES_COLD, new SkillProperties(100, 0, 0, 0, 0.1f, 0.05f, 1));

        this.skillProps.put(EnumSkills.BATH, new SkillProperties(100, 1, 1, 0, 0.1f, 0, 1));
        this.skillProps.put(EnumSkills.TAMING, new SkillProperties(100, 0, 0.2f, 0, 0, 0.5f, 1));
        this.skillProps.put(EnumSkills.LEADER, new SkillProperties(100, 0, 0, 0.25f, 0, 0.1f, 1));
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        this.add();
        return CompletableFuture.allOf(this.skillProps.entrySet().stream().map((entry) -> {
            Path path = this.packOutput.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(RuneCraftory.MODID + "/" + SkillPropertiesManager.ID + "/" + entry.getKey().name().toLowerCase(Locale.ROOT) + ".json");
            JsonElement obj = SkillProperties.CODEC.encodeStart(JsonOps.INSTANCE, entry.getValue()).getOrThrow();
            return DataProvider.saveStable(cache, obj, path);
        }).toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "SkillProperties";
    }
}