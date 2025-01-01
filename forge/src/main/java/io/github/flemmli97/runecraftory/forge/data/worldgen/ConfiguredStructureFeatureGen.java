package io.github.flemmli97.runecraftory.forge.data.worldgen;

import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;

public class ConfiguredStructureFeatureGen extends WorldGenData<Decoder.ConfiguredJigsawStructureFeatureData> {

    public ConfiguredStructureFeatureGen(DataGenerator generator) {
        super(generator, Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, Decoder.ConfiguredJigsawStructureFeatureData.CODEC);
    }

    @Override
    protected void gen() {
    }
}
