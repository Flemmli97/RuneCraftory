package io.github.flemmli97.runecraftory.forge.data.worldgen;

import io.github.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class ConfiguredFeatureGen extends WorldGenData<ConfiguredFeature<?, ?>> {

    public ConfiguredFeatureGen(DataGenerator generator) {
        super(generator, RuneCraftory.MODID, Registry.CONFIGURED_FEATURE_REGISTRY, ConfiguredFeature.DIRECT_CODEC);
    }

    @Override
    protected void gen() {
    }
}