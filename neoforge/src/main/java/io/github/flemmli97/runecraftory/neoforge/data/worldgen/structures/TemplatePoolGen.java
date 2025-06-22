package io.github.flemmli97.runecraftory.neoforge.data.worldgen.structures;

import io.github.flemmli97.tenshilib.common.data.provider.CodecBasedProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import java.util.concurrent.CompletableFuture;

public class TemplatePoolGen extends CodecBasedProvider<StructureTemplatePool> {

    public TemplatePoolGen(PackOutput output, String modid, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, PackOutput.Target.DATA_PACK, "Template Pools", modid, Registries.TEMPLATE_POOL.location().getPath(), StructureTemplatePool.DIRECT_CODEC, provider);
    }

    @Override
    protected void add(HolderLookup.Provider provider) {
    }

    public void add(ResourceLocation id, StructureTemplatePool pool) {
        this.contents.put(id, pool);
    }
}
