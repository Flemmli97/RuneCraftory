package io.github.flemmli97.runecraftory.neoforge.data.worldgen;

import io.github.flemmli97.runecraftory.common.events.WorldRegistrationCalls;
import io.github.flemmli97.runecraftory.neoforge.data.worldgen.features.BiomeModifiersGen;
import io.github.flemmli97.runecraftory.neoforge.data.worldgen.features.ConfiguredFeatureGen;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class FeatureWorldGen {

    public static void createWorldgenFeatures(RegistrySetBuilder builder) {
        Map<ResourceKey<ConfiguredFeature<?, ?>>, Function<WorldRegistrationCalls.HolderGetterLookup, ConfiguredFeature<?, ?>>> configured = new HashMap<>();
        Map<ResourceKey<PlacedFeature>, Function<WorldRegistrationCalls.HolderGetterLookup, PlacedFeature>> placed = new HashMap<>();
        List<WorldRegistrationCalls.FeatureBiomeModifier> features = new ArrayList<>();

        WorldRegistrationCalls.createFeatures(new WorldRegistrationCalls.FeatureRegister() {
            @Override
            public void registerConfigured(ResourceKey<ConfiguredFeature<?, ?>> id, Function<WorldRegistrationCalls.HolderGetterLookup, ConfiguredFeature<?, ?>> register) {
                configured.put(id, register);
            }

            @Override
            public void registerPlaced(ResourceKey<PlacedFeature> id, Function<WorldRegistrationCalls.HolderGetterLookup, PlacedFeature> register) {
                placed.put(id, register);
            }
        }, features::add);
        builder.add(Registries.CONFIGURED_FEATURE, ctx -> {
            configured.forEach((key, func) -> {
                ctx.register(key, func.apply(ctx::lookup));
            });
            ConfiguredFeatureGen.bootStrap(ctx);
        }).add(Registries.PLACED_FEATURE, ctx -> {
            placed.forEach((key, func) -> {
                ctx.register(key, func.apply(ctx::lookup));
            });
        }).add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ctx -> {
            features.forEach(feat -> ctx.register(ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, feat.placedFeature().location()), new BiomeModifiers.AddFeaturesBiomeModifier(
                    ctx.lookup(Registries.BIOME).getOrThrow(feat.tag()),
                    HolderSet.direct(ctx.lookup(Registries.PLACED_FEATURE).getOrThrow(feat.placedFeature())),
                    feat.decoration())));
            BiomeModifiersGen.bootStrap(ctx);
        });
    }
}
