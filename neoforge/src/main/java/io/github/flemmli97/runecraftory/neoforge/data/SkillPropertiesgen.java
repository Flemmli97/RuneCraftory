package io.github.flemmli97.runecraftory.neoforge.data;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.attachment.Skills;
import io.github.flemmli97.runecraftory.api.datapack.SkillProperties;
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
    private final EnumMap<Skills, SkillProperties> skillProps = new EnumMap<>(Skills.class);

    public SkillPropertiesgen(PackOutput packOutput) {
        this.packOutput = packOutput;
    }

    protected void add() {
        this.skillProps.clear();
        this.skillProps.put(Skills.SHORTSWORD, new SkillProperties(100, 0, 0.25f, 0.25f, 0, 0, 1));
        this.skillProps.put(Skills.LONGSWORD, new SkillProperties(100, 0, 0.25f, 0.25f, 0, 0, 1));
        this.skillProps.put(Skills.SPEAR, new SkillProperties(100, 0, 0.25f, 0.25f, 0, 0, 1));
        this.skillProps.put(Skills.HAMMERAXE, new SkillProperties(100, 0, 0.25f, 0.5f, 0, 0, 1));
        this.skillProps.put(Skills.DUAL, new SkillProperties(100, 0, 0.25f, 0.25f, 0, 0, 1));
        this.skillProps.put(Skills.FIST, new SkillProperties(100, 0, 0.25f, 0.25f, 0, 0, 1));

        this.skillProps.put(Skills.FIRE, new SkillProperties(100, 0, 0.5f, 0, 0, 0.2f, 1));
        this.skillProps.put(Skills.WATER, new SkillProperties(100, 0, 0.5f, 0, 0, 0.2f, 1));
        this.skillProps.put(Skills.EARTH, new SkillProperties(100, 0, 0.5f, 0, 0, 0.2f, 1));
        this.skillProps.put(Skills.WIND, new SkillProperties(100, 0, 0.5f, 0, 0, 0.2f, 1));
        this.skillProps.put(Skills.DARK, new SkillProperties(100, 0, 0.5f, 0, 0, 0.2f, 1));
        this.skillProps.put(Skills.LIGHT, new SkillProperties(100, 0, 0.5f, 0, 0, 0.2f, 1));
        this.skillProps.put(Skills.LOVE, new SkillProperties(100, 0, 0.5f, 0, 0, 0.2f, 1));

        this.skillProps.put(Skills.FARMING, new SkillProperties(100, 1, 1, 0, 0.5f, 0, 1));
        this.skillProps.put(Skills.LOGGING, new SkillProperties(100, 1, 1, 0.1f, 0.3f, 0, 1));
        this.skillProps.put(Skills.MINING, new SkillProperties(100, 1, 1, 0.1f, 0.3f, 0, 1));
        this.skillProps.put(Skills.FISHING, new SkillProperties(100, 1, 1, 0, 0, 0.5f, 1));

        this.skillProps.put(Skills.COOKING, new SkillProperties(100, 0, 0.25f, 0, 0.1f, 0, 1));
        this.skillProps.put(Skills.FORGING, new SkillProperties(100, 0, 0.25f, 0.25f, 0.1f, 0, 1));
        this.skillProps.put(Skills.CHEMISTRY, new SkillProperties(100, 0, 0.25f, 0, 0, 0.2f, 1));
        this.skillProps.put(Skills.CRAFTING, new SkillProperties(100, 0, 0.25f, 0, 0.1f, 0.1f, 1));

        this.skillProps.put(Skills.SEARCHING, new SkillProperties(100, 0, 0.3f, 0, 0, 0.1f, 1));
        this.skillProps.put(Skills.WALKING, new SkillProperties(100, 0.5f, 0.125f, 0, 0.1f, 0, 1));
        this.skillProps.put(Skills.SLEEPING, new SkillProperties(100, 2f, 2, 0.5f, 1, 0.5f, 1));
        this.skillProps.put(Skills.EATING, new SkillProperties(100, 1, 2, 0.5f, 0.5f, 0.2f, 1));
        this.skillProps.put(Skills.DEFENCE, new SkillProperties(100, 1.5f, 0, 0, 1, 0, 1));

        this.skillProps.put(Skills.RES_POISON, new SkillProperties(100, 0, 0, 0, 0.1f, 0.05f, 1));
        this.skillProps.put(Skills.RES_SEAL, new SkillProperties(100, 0, 0, 0, 0.1f, 0.05f, 1));
        this.skillProps.put(Skills.RES_PARA, new SkillProperties(100, 0, 0, 0, 0.1f, 0.05f, 1));
        this.skillProps.put(Skills.RES_SLEEP, new SkillProperties(100, 0, 0, 0, 0.1f, 0.05f, 1));
        this.skillProps.put(Skills.RES_FATIGUE, new SkillProperties(100, 0, 0, 0, 0.1f, 0.05f, 1));
        this.skillProps.put(Skills.RES_COLD, new SkillProperties(100, 0, 0, 0, 0.1f, 0.05f, 1));

        this.skillProps.put(Skills.BATH, new SkillProperties(100, 1, 1, 0, 0.1f, 0, 1));
        this.skillProps.put(Skills.TAMING, new SkillProperties(100, 0, 0.2f, 0, 0, 0.5f, 1));
        this.skillProps.put(Skills.LEADER, new SkillProperties(100, 0, 0, 0.25f, 0, 0.1f, 1));
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        this.add();
        return CompletableFuture.allOf(this.skillProps.entrySet().stream().map((entry) -> {
            Path path = this.packOutput.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(RuneCraftory.MODID + "/" + SkillPropertiesManager.DIRECTORY + "/" + entry.getKey().name().toLowerCase(Locale.ROOT) + ".json");
            JsonElement obj = SkillProperties.CODEC.encodeStart(JsonOps.INSTANCE, entry.getValue()).getOrThrow();
            return DataProvider.saveStable(cache, obj, path);
        }).toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "SkillProperties";
    }
}