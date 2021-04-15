package com.flemmli97.runecraftory.common.registry;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.config.GenerationConfig;
import com.flemmli97.runecraftory.common.config.values.MineralGenConfig;
import com.flemmli97.runecraftory.common.world.features.ChancedBlockCluster;
import com.flemmli97.runecraftory.common.world.features.MineralFeatures;
import com.flemmli97.runecraftory.mixin.FlatGenSettingAccessor;
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

    public static void registerConfiguredFeatures() {
        for (MineralGenConfig conf : GenerationConfig.allMineralConfs()) {
            conf.bake();
            Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, RuneCraftory.MODID + ":mineral_gen_" + conf.blockRes().getPath(), conf.configuredFeature());
            Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, RuneCraftory.MODID + ":mineral_gen_nether_" + conf.blockRes().getPath(), conf.configuredFeatureNether());
        }

        Registry.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(RuneCraftory.MODID, "ambrosia_feature"), AMBROSIA_FEATURE = ModStructures.AMBROSIA_FOREST.get().configure(IFeatureConfig.NO_FEATURE_CONFIG));
        FlatGenSettingAccessor.getSTRUCTURES().put(ModStructures.AMBROSIA_FOREST.get(), AMBROSIA_FEATURE);

        ModStructures.setup();
    }
}
