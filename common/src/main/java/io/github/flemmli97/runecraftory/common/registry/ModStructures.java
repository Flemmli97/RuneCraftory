package io.github.flemmli97.runecraftory.common.registry;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.world.structure.NetherJigsawStructure;
import io.github.flemmli97.runecraftory.common.world.structure.SurfaceJigsawStructure;
import io.github.flemmli97.runecraftory.common.world.structure.processors.BossSpawnerProcessor;
import io.github.flemmli97.runecraftory.common.world.structure.processors.NPCDataProcessor;
import io.github.flemmli97.runecraftory.common.world.structure.processors.WaterUnlogProcessor;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import io.github.flemmli97.tenshilib.platform.registry.PlatformRegistry;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import java.util.Map;
import java.util.function.Supplier;

public class ModStructures {

    public static final PlatformRegistry<StructureFeature<?>> STRUCTURES = PlatformUtils.INSTANCE.of(Registry.STRUCTURE_FEATURE_REGISTRY, RuneCraftory.MODID);

    public static final PlatformRegistry<StructureProcessorType<?>> STRUCTURESPROCESSORS = PlatformUtils.INSTANCE.of(Registry.STRUCTURE_PROCESSOR_REGISTRY, RuneCraftory.MODID);

    public static final RegistryEntrySupplier<StructureFeature<JigsawConfiguration>> FOREST_GROVE = register("forest_grove", () -> new SurfaceJigsawStructure(JigsawConfiguration.CODEC));
    public static final RegistryEntrySupplier<StructureFeature<JigsawConfiguration>> WATER_RUINS = register("water_ruins", () -> new SurfaceJigsawStructure(JigsawConfiguration.CODEC));
    public static final RegistryEntrySupplier<StructureFeature<JigsawConfiguration>> THEATER_RUINS = register("theater_ruins", () -> new SurfaceJigsawStructure(JigsawConfiguration.CODEC));
    public static final RegistryEntrySupplier<StructureFeature<JigsawConfiguration>> PLAINS_ARENA = register("plains_arena", () -> new SurfaceJigsawStructure(JigsawConfiguration.CODEC));
    public static final RegistryEntrySupplier<StructureFeature<JigsawConfiguration>> DESERT_ARENA = register("desert_arena", () -> new SurfaceJigsawStructure(JigsawConfiguration.CODEC));
    public static final RegistryEntrySupplier<StructureFeature<JigsawConfiguration>> NETHER_ARENA = register("nether_arena", () -> new NetherJigsawStructure(JigsawConfiguration.CODEC, 40, 80));

    public static final RegistryEntrySupplier<StructureProcessorType<BossSpawnerProcessor>> BOSS_PROCESSOR = STRUCTURESPROCESSORS.register("boss_processor", () -> () -> BossSpawnerProcessor.CODEC);
    public static final RegistryEntrySupplier<StructureProcessorType<NPCDataProcessor>> NPC_PROCESSOR = STRUCTURESPROCESSORS.register("npc_processor", () -> () -> NPCDataProcessor.CODEC);
    public static final RegistryEntrySupplier<StructureProcessorType<WaterUnlogProcessor>> WATERUNLOG_PROCESSOR = STRUCTURESPROCESSORS.register("water_unlog_processor", () -> () -> WaterUnlogProcessor.CODEC);

    public static final Map<ResourceLocation, Holder<StructureProcessorList>> NPC_PROCESSOR_LIST = registerNPCProcessorLists();

    public static <T extends FeatureConfiguration> RegistryEntrySupplier<StructureFeature<T>> register(String name, Supplier<StructureFeature<T>> sup) {
        return STRUCTURES.register(name, sup);
    }

    private static Map<ResourceLocation, Holder<StructureProcessorList>> registerNPCProcessorLists() {
        ImmutableMap.Builder<ResourceLocation, Holder<StructureProcessorList>> map = ImmutableMap.builder();
        for (ResourceLocation shop : ModNPCJobs.DEFAULT_JOB_ID) {
            Holder<StructureProcessorList> holder = BuiltinRegistries.register(BuiltinRegistries.PROCESSOR_LIST, new ResourceLocation(RuneCraftory.MODID, "npc_" + shop.getPath()),
                    new StructureProcessorList(ImmutableList.of(new NPCDataProcessor(shop))));
            map.put(shop, holder);
        }
        return map.build();
    }
}
