package io.github.flemmli97.runecraftory.common.registry;

public class ModStructures {
//
//    public static final LoaderRegister<StructureProcessorType<?>> STRUCTURES = LoaderRegistryAccess.INSTANCE.of(Registry.STRUCTURE_FEATURE_REGISTRY, RuneCraftory.MODID);
//
//    public static final LoaderRegister<StructureProcessorType<?>> STRUCTURESPROCESSORS = LoaderRegistryAccess.INSTANCE.of(Registry.STRUCTURE_PROCESSOR_REGISTRY, RuneCraftory.MODID);
//
//    public static final RegistryEntrySupplier<StructureFeature<JigsawConfiguration>> FOREST_GROVE = register("forest_grove", () -> new SurfaceJigsawStructure(JigsawConfiguration.CODEC));
//    public static final RegistryEntrySupplier<StructureFeature<JigsawConfiguration>> WATER_RUINS = register("water_ruins", () -> new SurfaceJigsawStructure(JigsawConfiguration.CODEC));
//    public static final RegistryEntrySupplier<StructureFeature<JigsawConfiguration>> THEATER_RUINS = register("theater_ruins", () -> new SurfaceJigsawStructure(JigsawConfiguration.CODEC));
//    public static final RegistryEntrySupplier<StructureFeature<JigsawConfiguration>> PLAINS_ARENA = register("plains_arena", () -> new SurfaceJigsawStructure(JigsawConfiguration.CODEC));
//    public static final RegistryEntrySupplier<StructureFeature<JigsawConfiguration>> DESERT_ARENA = register("desert_arena", () -> new SurfaceJigsawStructure(JigsawConfiguration.CODEC));
//    public static final RegistryEntrySupplier<StructureFeature<JigsawConfiguration>> NETHER_ARENA = register("nether_arena", () -> new NetherJigsawStructure(JigsawConfiguration.CODEC, 40, 80));
//    public static final RegistryEntrySupplier<StructureFeature<JigsawConfiguration>> WIND_SHRINE = register("wind_shrine", () -> new SurfaceJigsawStructure(JigsawConfiguration.CODEC));
//    public static final RegistryEntrySupplier<StructureFeature<JigsawConfiguration>> LEON_KARNAK = register("leon_karnak", () -> new SurfaceJigsawStructure(JigsawConfiguration.CODEC));
//
//    public static final RegistryEntrySupplier<StructureProcessorType<BossSpawnerProcessor>> BOSS_PROCESSOR = STRUCTURESPROCESSORS.register("boss_processor", () -> () -> BossSpawnerProcessor.CODEC);
//    public static final RegistryEntrySupplier<StructureProcessorType<NPCDataProcessor>> NPC_PROCESSOR = STRUCTURESPROCESSORS.register("npc_processor", () -> () -> NPCDataProcessor.CODEC);
//    public static final RegistryEntrySupplier<StructureProcessorType<WaterUnlogProcessor>> WATERUNLOG_PROCESSOR = STRUCTURESPROCESSORS.register("water_unlog_processor", () -> () -> WaterUnlogProcessor.CODEC);
//
//    public static final Map<ResourceLocation, Holder<StructureProcessorList>> NPC_PROCESSOR_LIST = registerNPCProcessorLists();
//
//    public static <T extends FeatureConfiguration> RegistryEntrySupplier<StructureFeature<T>> register(String name, Supplier<StructureFeature<T>> sup) {
//        return STRUCTURES.register(name, sup);
//    }
//
//    private static Map<ResourceLocation, Holder<StructureProcessorList>> registerNPCProcessorLists() {
//        ImmutableMap.Builder<ResourceLocation, Holder<StructureProcessorList>> map = ImmutableMap.builder();
//        for (ResourceLocation shop : ModNPCJobs.DEFAULT_JOB_ID) {
//            Holder<StructureProcessorList> holder = BuiltinRegistries.register(BuiltinRegistries.PROCESSOR_LIST, RuneCraftory.modRes("npc_" + shop.getPath()),
//                    new StructureProcessorList(ImmutableList.of(new NPCDataProcessor(shop))));
//            map.put(shop, holder);
//        }
//        return map.build();
//    }
}
