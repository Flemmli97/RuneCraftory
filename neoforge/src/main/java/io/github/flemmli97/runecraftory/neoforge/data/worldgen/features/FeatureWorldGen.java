package io.github.flemmli97.runecraftory.neoforge.data.worldgen.features;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.events.WorldRegistrationCalls;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.holdersets.AnyHolderSet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;

public class FeatureWorldGen implements DataProvider {

    protected final CompletableFuture<HolderLookup.Provider> provider;

    private final List<DataProvider> subProviders = new ArrayList<>();
    private final ConfiguredFeatureGen configuredFeatureGen;
    private final PlacedFeatureGen placedFeatureGen;
    private final BiomeModifiersGen biomeModifiersGen;

    public FeatureWorldGen(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider) {
        this.provider = provider;
        this.subProviders.add(this.configuredFeatureGen = new ConfiguredFeatureGen(packOutput, RuneCraftory.MODID, provider));
        this.subProviders.add(this.placedFeatureGen = new PlacedFeatureGen(packOutput, RuneCraftory.MODID, provider));
        this.subProviders.add(this.biomeModifiersGen = new BiomeModifiersGen(packOutput, RuneCraftory.MODID, provider));
    }

    protected static <T> Holder<T> create(HolderLookup.Provider provider, ResourceKey<T> key) {
        return Holder.Reference.createStandAlone(provider.lookupOrThrow(key.registryKey()),
                key);
    }

    protected static <T> Holder<T> create(HolderLookup.Provider provider, ResourceKey<Registry<T>> key, ResourceLocation location) {
        return Holder.Reference.createStandAlone(provider.lookupOrThrow(key),
                ResourceKey.create(key, location));
    }

    @SuppressWarnings("deprecation")
    protected void add(HolderLookup.Provider provider) {
        WorldRegistrationCalls.createFeatures(new WorldRegistrationCalls.FeatureRegister() {
            @Override
            public void registerConfigured(ResourceLocation id, Function<HolderLookup.Provider, ConfiguredFeature<?, ?>> feature) {
                FeatureWorldGen.this.configuredFeatureGen.add(id, feature.apply(provider));
            }

            @Override
            public void registerPlaced(ResourceLocation id, ResourceLocation configuredId, BiFunction<HolderLookup.Provider, Holder<ConfiguredFeature<?, ?>>, PlacedFeature> placed) {
                FeatureWorldGen.this.placedFeatureGen.add(id, placed.apply(provider, create(provider, Registries.CONFIGURED_FEATURE, configuredId)));
            }
        }, feat -> this.biomeModifiersGen.add(feat.placedFeature(), new BiomeModifiers.AddFeaturesBiomeModifier(
                feat.tag().<HolderSet<Biome>>map(t -> HolderSet.emptyNamed(provider.lookupOrThrow(Registries.BIOME),
                        t)).orElse(new AnyHolderSet<>(provider.lookupOrThrow(Registries.BIOME))),
                HolderSet.direct(create(provider, Registries.PLACED_FEATURE, feat.placedFeature())), feat.decoration())));
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        return this.provider.thenAccept(this::add)
                .thenCompose(res -> CompletableFuture.allOf(this.subProviders.stream().map(p -> p.run(cache))
                        .toArray(CompletableFuture[]::new)));
    }

    @Override
    public String getName() {
        return "Feature World Gen Data";
    }
}
