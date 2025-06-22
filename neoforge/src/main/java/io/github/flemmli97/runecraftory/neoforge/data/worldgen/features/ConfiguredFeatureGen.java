package io.github.flemmli97.runecraftory.neoforge.data.worldgen.features;

import io.github.flemmli97.tenshilib.common.data.provider.CodecBasedProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.concurrent.CompletableFuture;

public class ConfiguredFeatureGen extends CodecBasedProvider<ConfiguredFeature<?, ?>> {

    public ConfiguredFeatureGen(PackOutput output, String modid, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, PackOutput.Target.DATA_PACK, "Configured Features", modid, Registries.CONFIGURED_FEATURE.location().getPath(), ConfiguredFeature.DIRECT_CODEC, provider);
    }

    @Override
    protected void add(HolderLookup.Provider provider) {
    }

    public void add(ResourceLocation id, ConfiguredFeature<?, ?> feature) {
        this.contents.put(id, feature);
    }
}
