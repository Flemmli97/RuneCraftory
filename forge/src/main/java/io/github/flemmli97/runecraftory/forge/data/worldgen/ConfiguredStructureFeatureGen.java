package io.github.flemmli97.runecraftory.forge.data.worldgen;

import io.github.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;

public class ConfiguredStructureFeatureGen extends WorldGenData<Decoder.ConfiguredJigsawStructureFeatureData> {

    public ConfiguredStructureFeatureGen(DataGenerator generator) {
        super(generator, RuneCraftory.MODID, Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, Decoder.ConfiguredJigsawStructureFeatureData.CODEC);
    }

    @Override
    protected void gen() {
    }
}
