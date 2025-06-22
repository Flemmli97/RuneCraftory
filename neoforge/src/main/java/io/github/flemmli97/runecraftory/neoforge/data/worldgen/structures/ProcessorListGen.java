package io.github.flemmli97.runecraftory.neoforge.data.worldgen.structures;

import io.github.flemmli97.tenshilib.common.data.provider.CodecBasedProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import java.util.concurrent.CompletableFuture;

public class ProcessorListGen extends CodecBasedProvider<StructureProcessorList> {

    public ProcessorListGen(PackOutput output, String modid, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, PackOutput.Target.DATA_PACK, "Processor List", modid, Registries.PROCESSOR_LIST.location().getPath(), StructureProcessorType.DIRECT_CODEC, provider);
    }

    @Override
    protected void add(HolderLookup.Provider provider) {
    }

    public void add(ResourceLocation id, StructureProcessorList list) {
        this.contents.put(id, list);
    }
}
