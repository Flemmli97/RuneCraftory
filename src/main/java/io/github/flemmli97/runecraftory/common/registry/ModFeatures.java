package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.config.GenerationConfig;
import io.github.flemmli97.runecraftory.common.config.values.MineralGenConfig;
import io.github.flemmli97.runecraftory.common.world.features.ChancedBlockCluster;
import io.github.flemmli97.runecraftory.common.world.features.MineralFeatures;
import io.github.flemmli97.runecraftory.mixin.FlatGenSettingAccessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, RuneCraftory.MODID);

    public static final RegistryObject<MineralFeatures> MINERALFEATURE = FEATURES.register("mineral_feature", () -> new MineralFeatures(ChancedBlockCluster.CODEC));

    public static ConfiguredFeature<?, ?> CONFIGURED_HERBFEATURE;
    public static ConfiguredFeature<?, ?> CONFIGURED_NETHER_HERBFEATURE;
    public static ConfiguredFeature<?, ?> CONFIGURED_END_HERBFEATURE;

    public static StructureFeature<?, ?> AMBROSIA_FEATURE;
    public static StructureFeature<?, ?> THUNDERBOLT_FEATURE;

    public static void registerConfiguredFeatures() {
        for (MineralGenConfig conf : GenerationConfig.allMineralConfs()) {
            conf.bake();
            Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, RuneCraftory.MODID + ":mineral_gen_" + conf.blockRes().getPath(), conf.configuredFeature());
            Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, RuneCraftory.MODID + ":mineral_gen_nether_" + conf.blockRes().getPath(), conf.configuredFeatureNether());
        }

        Registry.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(RuneCraftory.MODID, "ambrosia_feature"), AMBROSIA_FEATURE = ModStructures.AMBROSIA_FOREST.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        FlatGenSettingAccessor.getSTRUCTURES().put(ModStructures.AMBROSIA_FOREST.get(), AMBROSIA_FEATURE);
        Registry.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(RuneCraftory.MODID, "thunderbolt_feature"), THUNDERBOLT_FEATURE = ModStructures.THUNDERBOLT_RUINS.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        FlatGenSettingAccessor.getSTRUCTURES().put(ModStructures.THUNDERBOLT_RUINS.get(), THUNDERBOLT_FEATURE);

        ModStructures.setup();
    }
}
