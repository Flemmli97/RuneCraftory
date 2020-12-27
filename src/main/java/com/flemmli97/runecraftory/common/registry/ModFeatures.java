package com.flemmli97.runecraftory.common.registry;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.world.features.MineralFeatures;
import com.flemmli97.runecraftory.common.world.features.WeightedClusterProvider;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, RuneCraftory.MODID);

    public static RegistryObject<MineralFeatures> MINERALFEATURE = FEATURES.register("mineral_feature", ()->new MineralFeatures(WeightedClusterProvider.CODEC));
}
