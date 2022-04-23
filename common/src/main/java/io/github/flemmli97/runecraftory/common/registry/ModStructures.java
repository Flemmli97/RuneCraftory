package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.world.structure.AmbrosiaForestStructure;
import io.github.flemmli97.runecraftory.common.world.structure.BossSpawnerProcessor;
import io.github.flemmli97.runecraftory.common.world.structure.ThunderboltRuinsStructure;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import io.github.flemmli97.tenshilib.platform.registry.PlatformRegistry;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import java.util.function.Supplier;

public class ModStructures {

    public static final PlatformRegistry<StructureFeature<?>> STRUCTURES = PlatformUtils.INSTANCE.of(Registry.STRUCTURE_FEATURE_REGISTRY, RuneCraftory.MODID);

    public static final PlatformRegistry<StructureProcessorType<?>> STRUCTURESPROCESSORS = PlatformUtils.INSTANCE.of(Registry.STRUCTURE_PROCESSOR_REGISTRY, RuneCraftory.MODID);

    public static final RegistryEntrySupplier<StructureFeature<JigsawConfiguration>> AMBROSIA_FOREST = register("ambrosia_forest", () -> new AmbrosiaForestStructure(JigsawConfiguration.CODEC));
    public static final RegistryEntrySupplier<StructureFeature<JigsawConfiguration>> THUNDERBOLT_RUINS = register("thunderbolt_ruins", () -> new ThunderboltRuinsStructure(JigsawConfiguration.CODEC));

    public static final RegistryEntrySupplier<StructureProcessorType<BossSpawnerProcessor>> BOSS_PROCESSOR = STRUCTURESPROCESSORS.register("boss_processor", () -> () -> BossSpawnerProcessor.CODEC);

    public static <T extends FeatureConfiguration> RegistryEntrySupplier<StructureFeature<T>> register(String name, Supplier<StructureFeature<T>> sup) {
        return STRUCTURES.register(name, sup);
    }
}
