package io.github.flemmli97.runecraftory.forge.data.worldgen;

import io.github.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;

public class StructureSetGen extends WorldGenData<Decoder.StructureSetData> {

    public StructureSetGen(DataGenerator generator) {
        super(generator, RuneCraftory.MODID, Registry.STRUCTURE_SET_REGISTRY, Decoder.StructureSetData.CODEC);
    }

    @Override
    protected void gen() {
    }
}
