package io.github.flemmli97.runecraftory.forge.data.worldgen;

import io.github.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public class ProcessorListGen extends WorldGenData<StructureProcessorList> {

    public ProcessorListGen(DataGenerator generator) {
        super(generator, RuneCraftory.MODID, Registry.PROCESSOR_LIST_REGISTRY, StructureProcessorType.DIRECT_CODEC);
    }

    @Override
    protected void gen() {
    }
}
