package io.github.flemmli97.runecraftory.forge.data.worldgen;

import io.github.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;

public class TemplatePoolGen extends WorldGenData<Decoder.TemplatePoolData> {

    public TemplatePoolGen(DataGenerator generator) {
        super(generator, RuneCraftory.MODID, Registry.TEMPLATE_POOL_REGISTRY, Decoder.TemplatePoolData.CODEC);
    }

    @Override
    protected void gen() {
    }
}
