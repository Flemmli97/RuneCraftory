package io.github.flemmli97.runecraftory.neoforge.data.worldgen;

import io.github.flemmli97.tenshilib.common.data.provider.CodecBasedProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.StructureSet;

import java.util.concurrent.CompletableFuture;

public class StructureSetGen extends CodecBasedProvider<StructureSet> {

    public StructureSetGen(PackOutput output, String modid, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, PackOutput.Target.DATA_PACK, "Structure Set", modid, Registries.STRUCTURE_SET.location().getPath(), StructureSet.DIRECT_CODEC, provider);
    }

    @Override
    protected void add(HolderLookup.Provider provider) {
    }

    public void add(ResourceLocation id, StructureSet set) {
        this.contents.put(id, set);
    }
}
