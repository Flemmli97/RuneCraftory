package io.github.flemmli97.runecraftory.neoforge.data.worldgen.features;

import io.github.flemmli97.tenshilib.common.data.provider.CodecBasedProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.concurrent.CompletableFuture;

public class PlacedFeatureGen extends CodecBasedProvider<PlacedFeature> {

    public PlacedFeatureGen(PackOutput output, String modid, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, PackOutput.Target.DATA_PACK, "Configured Features", modid, Registries.PLACED_FEATURE.location().getPath(), PlacedFeature.DIRECT_CODEC, provider);
    }

    @Override
    protected void add(HolderLookup.Provider provider) {
    }

    public void add(ResourceLocation id, PlacedFeature feature) {
        this.contents.put(id, feature);
    }
}
