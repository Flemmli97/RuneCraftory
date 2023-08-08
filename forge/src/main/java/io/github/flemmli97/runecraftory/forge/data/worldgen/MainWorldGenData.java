package io.github.flemmli97.runecraftory.forge.data.worldgen;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.common.registry.ModStructures;
import io.github.flemmli97.runecraftory.common.registry.ModTags;
import io.github.flemmli97.runecraftory.common.world.structure.processors.BossSpawnerProcessor;
import io.github.flemmli97.runecraftory.common.world.structure.processors.WaterUnlogProcessor;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import java.util.List;
import java.util.Map;

public class MainWorldGenData implements DataProvider {

    private final ConfiguredStructureFeatureGen configuredStructureFeatureGen;
    private final ProcessorListGen processorListGen;
    private final StructureSetGen structureSetGen;
    private final TemplatePoolGen templatePoolGen;

    public MainWorldGenData(DataGenerator dataGenerator) {
        this.configuredStructureFeatureGen = new ConfiguredStructureFeatureGen(dataGenerator);
        this.processorListGen = new ProcessorListGen(dataGenerator);
        this.structureSetGen = new StructureSetGen(dataGenerator);
        this.templatePoolGen = new TemplatePoolGen(dataGenerator);
    }

    @Override
    public void run(HashCache cache) {
        this.addBossStructure(ModStructures.FOREST_GROVE.getID(),
                new RandomSpreadStructurePlacement(22, 8, RandomSpreadType.LINEAR, 1224466880),
                ModStructures.FOREST_GROVE.get(), BiomeTags.IS_FOREST, true,
                this.simpleTag(ModTags.FOREST_BOSSES));
        this.addBossStructure(ModStructures.WATER_RUINS.getID(),
                new RandomSpreadStructurePlacement(32, 16, RandomSpreadType.LINEAR, 1224567480),
                ModStructures.WATER_RUINS.get(), BiomeTags.IS_OCEAN, true,
                this.simpleTag(ModTags.WATER_RUIN_BOSSES));
        this.addBossStructure(ModStructures.THEATER_RUINS.getID(),
                new RandomSpreadStructurePlacement(21, 7, RandomSpreadType.LINEAR, 1226867120),
                ModStructures.THEATER_RUINS.get(), ModTags.IS_SPOOKY, true,
                new StructureProcessorList(List.of(new BossSpawnerProcessor(ModTags.THEATER_RUIN_BOSSES),
                        WaterUnlogProcessor.INST)));
        this.addBossStructure(ModStructures.PLAINS_ARENA.getID(),
                new RandomSpreadStructurePlacement(23, 7, RandomSpreadType.LINEAR, 1226867124),
                ModStructures.PLAINS_ARENA.get(), ModTags.IS_PLAINS, true,
                this.simpleTag(ModTags.PLAINS_ARENA_BOSSES));
        this.addBossStructure(ModStructures.DESERT_ARENA.getID(),
                new RandomSpreadStructurePlacement(23, 8, RandomSpreadType.LINEAR, 1274867120),
                ModStructures.DESERT_ARENA.get(), ModTags.IS_SANDY, true,
                this.simpleTag(ModTags.DESERT_ARENA_BOSSES));
        this.addBossStructure(ModStructures.NETHER_ARENA.getID(),
                new RandomSpreadStructurePlacement(20, 9, RandomSpreadType.LINEAR, 1226869984),
                ModStructures.NETHER_ARENA.get(), BiomeTags.IS_NETHER, true,
                this.simpleTag(ModTags.NETHER_ARENA_BOSSES));
        this.templatePoolGen.runExternal(cache);
        this.processorListGen.runExternal(cache);
        this.configuredStructureFeatureGen.runExternal(cache);
        this.structureSetGen.runExternal(cache);
    }

    @Override
    public String getName() {
        return "World Gen";
    }

    protected void addBossStructure(ResourceLocation id, StructurePlacement placement, StructureFeature<?> feature, TagKey<Biome> biomeTag, boolean adapt,
                                    StructureProcessorList list) {
        ResourceLocation processorID = new ResourceLocation(id.getNamespace(), id.getPath() + "_processor");
        this.processorListGen.addElement(processorID, list);
        this.templatePoolGen.addElement(id, new Decoder.TemplatePoolData(id, new ResourceLocation("empty"), List.of(
                Pair.of(new Decoder.StructurePoolElementData(id, processorID,
                        StructureTemplatePool.Projection.RIGID), 1))));
        this.structureSetGen.addElement(id, new Decoder.StructureSetData(Pair.of(id, 1), placement));
        this.configuredStructureFeatureGen.addElement(id, new Decoder.ConfiguredJigsawStructureFeatureData(feature, id,
                1, biomeTag, Map.of(), adapt));
    }

    protected StructureProcessorList simple(ResourceLocation boss) {
        return new StructureProcessorList(List.of(new BossSpawnerProcessor(boss)));
    }

    protected StructureProcessorList simpleTag(TagKey<EntityType<?>> boss) {
        return new StructureProcessorList(List.of(new BossSpawnerProcessor(boss)));
    }
}
