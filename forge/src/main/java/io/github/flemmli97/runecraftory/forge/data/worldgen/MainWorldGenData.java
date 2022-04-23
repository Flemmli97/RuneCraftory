package io.github.flemmli97.runecraftory.forge.data.worldgen;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModStructures;
import io.github.flemmli97.runecraftory.common.world.structure.BossSpawnerProcessor;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import java.io.IOException;
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
    public void run(HashCache cache) throws IOException {
        this.addBossStructure(new ResourceLocation(RuneCraftory.MODID, "ambrosia_forest"),
                new RandomSpreadStructurePlacement(20, 8, RandomSpreadType.LINEAR, 1224466880),
                ModStructures.AMBROSIA_FOREST.get(), BiomeTags.IS_FOREST, ModEntities.ambrosia.getID(), true);
        this.addBossStructure(new ResourceLocation(RuneCraftory.MODID, "thunderbolt_ruins"),
                new RandomSpreadStructurePlacement(16, 8, RandomSpreadType.LINEAR, 1224567480),
                ModStructures.THUNDERBOLT_RUINS.get(), BiomeTags.IS_OCEAN, ModEntities.thunderbolt.getID(), true);
        this.templatePoolGen.runExternal(cache);
        this.processorListGen.runExternal(cache);
        this.configuredStructureFeatureGen.runExternal(cache);
        this.structureSetGen.runExternal(cache);
    }

    protected void addBossStructure(ResourceLocation id, StructurePlacement placement, StructureFeature<?> feature, TagKey<Biome> biomeTag, ResourceLocation boss, boolean adapt) {
        ResourceLocation processorID = new ResourceLocation(id.getNamespace(), id.getPath() + "_processor");
        this.processorListGen.addElement(processorID, new StructureProcessorList(List.of(new BossSpawnerProcessor(boss))));
        this.templatePoolGen.addElement(id, new Decoder.TemplatePoolData(id, new ResourceLocation("empty"), List.of(
                Pair.of(new Decoder.StructurePoolElementData(id, processorID,
                        StructureTemplatePool.Projection.RIGID), 1))));
        this.structureSetGen.addElement(id, new Decoder.StructureSetData(Pair.of(id, 1), placement));
        this.configuredStructureFeatureGen.addElement(id, new Decoder.ConfiguredJigsawStructureFeatureData(feature, id,
                1, biomeTag, Map.of(), adapt));
    }

    @Override
    public String getName() {
        return "World Gen";
    }
}
