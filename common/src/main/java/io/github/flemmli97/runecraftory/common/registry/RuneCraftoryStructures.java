package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.world.structure.ExtendedJigsawStructure;
import io.github.flemmli97.runecraftory.common.world.structure.NetherJigsawStructure;
import io.github.flemmli97.runecraftory.common.world.structure.processors.BossSpawnerProcessor;
import io.github.flemmli97.runecraftory.common.world.structure.processors.NPCDataProcessor;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.registry.LoaderRegister;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public class RuneCraftoryStructures {

    public static final LoaderRegister<StructureType<?>> STRUCTURES = LoaderRegistryAccess.INSTANCE.of(Registries.STRUCTURE_TYPE, RuneCraftory.MODID);
    public static final LoaderRegister<StructureProcessorType<?>> STRUCTURE_PROCESSORS = LoaderRegistryAccess.INSTANCE.of(Registries.STRUCTURE_PROCESSOR, RuneCraftory.MODID);

    public static final RegistryEntrySupplier<StructureType<?>, StructureType<ExtendedJigsawStructure>> EXTENDED_STRUCTURE = STRUCTURES.register("extended_structure", () -> () -> ExtendedJigsawStructure.CODEC);
    public static final RegistryEntrySupplier<StructureType<?>, StructureType<NetherJigsawStructure>> NETHER_STRUCTURE = STRUCTURES.register("nether_structure", () -> () -> NetherJigsawStructure.CODEC);

    public static final RegistryEntrySupplier<StructureProcessorType<?>, StructureProcessorType<BossSpawnerProcessor>> BOSS_PROCESSOR = STRUCTURE_PROCESSORS.register("boss_processor", () -> () -> BossSpawnerProcessor.CODEC);
    public static final RegistryEntrySupplier<StructureProcessorType<?>, StructureProcessorType<NPCDataProcessor>> NPC_PROCESSOR = STRUCTURE_PROCESSORS.register("npc_processor", () -> () -> NPCDataProcessor.CODEC);

    public static final ResourceKey<StructureTemplatePool> NPC_HOUSES = ResourceKey.create(Registries.TEMPLATE_POOL, RuneCraftory.modRes("npc/houses"));
    public static final ResourceKey<StructureTemplatePool> NPC_BIG_HOUSES = ResourceKey.create(Registries.TEMPLATE_POOL, RuneCraftory.modRes("npc/big_houses"));
}
