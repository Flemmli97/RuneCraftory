package io.github.flemmli97.runecraftory.common.config;

import io.github.flemmli97.runecraftory.RuneCraftory;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ConfigHolder<T> {

    public static final Map<ForgeConfigSpec, ConfigHolder<?>> configs = new LinkedHashMap<>();

    static {
        configs.put(GeneralConfigSpec.spec.getRight(), new ConfigHolder<>(ModConfig.Type.COMMON, RuneCraftory.MODID + File.separator + "general.toml",
                GeneralConfigSpec.spec.getLeft(), GeneralConfig::load));
        configs.put(ClientConfigSpec.spec.getRight(), new ConfigHolder<>(ModConfig.Type.CLIENT, RuneCraftory.MODID + File.separator + "client.toml",
                ClientConfigSpec.spec.getLeft(), ClientConfig::load));
        configs.put(MobConfigSpec.spec.getRight(), new ConfigHolder<>(ModConfig.Type.COMMON, RuneCraftory.MODID + File.separator + "mobs.toml",
                MobConfigSpec.spec.getLeft(), MobConfig::load));
        configs.put(GenerationConfigSpec.spec.getRight(), new ConfigHolder<>(ModConfig.Type.COMMON, RuneCraftory.MODID + File.separator + "generation.toml",
                GenerationConfigSpec.spec.getLeft(), GenerationConfig::load));
    }

    public final ModConfig.Type configType;
    public final String configName;
    public final T configSpec;
    private final Consumer<T> loader;

    public ConfigHolder(ModConfig.Type configType, String configName, T configSpec, Consumer<T> loader) {
        this.configType = configType;
        this.configName = configName;
        this.configSpec = configSpec;
        this.loader = loader;
    }

    public void reloadConfig() {
        this.loader.accept(this.configSpec);
    }
}
