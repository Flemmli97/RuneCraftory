package io.github.flemmli97.runecraftory.neoforge.data.worldgen;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.provider.FileVerifier;
import io.github.flemmli97.runecraftory.common.datapack.manager.StructureBossManager;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.world.structure.ExtendedJigsawStructure;
import io.github.flemmli97.runecraftory.common.world.structure.JigsawStructureData;
import io.github.flemmli97.runecraftory.common.world.structure.processors.BossSpawnerProcessor;
import io.github.flemmli97.runecraftory.neoforge.data.StructureBossGen;
import io.github.flemmli97.runecraftory.neoforge.data.worldgen.structures.ProcessorListGen;
import io.github.flemmli97.runecraftory.neoforge.data.worldgen.structures.StructureGen;
import io.github.flemmli97.runecraftory.neoforge.data.worldgen.structures.StructureSetGen;
import io.github.flemmli97.runecraftory.neoforge.data.worldgen.structures.TemplatePoolGen;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.pools.DimensionPadding;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class StructureWorldGen implements DataProvider {

    private final Random random = new Random();
    protected final CompletableFuture<HolderLookup.Provider> provider;

    private final List<DataProvider> subProviders = new ArrayList<>();
    private final ProcessorListGen processorListGen;
    private final StructureGen structureGen;
    private final StructureSetGen structureSetGen;
    private final TemplatePoolGen templatePoolGen;

    private final FileVerifier verifier;

    public StructureWorldGen(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider, FileVerifier verifier) {
        this.provider = provider;
        this.subProviders.add(this.processorListGen = new ProcessorListGen(packOutput, RuneCraftory.MODID, provider));
        this.subProviders.add(this.structureSetGen = new StructureSetGen(packOutput, RuneCraftory.MODID, provider));
        this.subProviders.add(this.structureGen = new StructureGen(packOutput, RuneCraftory.MODID, provider));
        this.subProviders.add(this.templatePoolGen = new TemplatePoolGen(packOutput, RuneCraftory.MODID, provider));
        this.verifier = verifier;
    }

    protected static <T> Holder<T> create(HolderLookup.Provider provider, ResourceKey<T> key) {
        return Holder.Reference.createStandAlone(provider.lookupOrThrow(key.registryKey()),
                key);
    }

    protected static <T> Holder<T> create(HolderLookup.Provider provider, ResourceKey<Registry<T>> key, ResourceLocation location) {
        return Holder.Reference.createStandAlone(provider.lookupOrThrow(key),
                ResourceKey.create(key, location));
    }

    protected void add(HolderLookup.Provider provider) {
        this.random.setSeed(123456789);
        this.addBossStructure(provider, RuneCraftory.modRes("forest_grove"),
                new RandomSpreadStructurePlacement(23, 8, RandomSpreadType.LINEAR, Math.abs(this.random.nextInt())),
                RunecraftoryTags.Biomes.FOREST_GROVE, true,
                this.simple(StructureBossGen.FOREST_BOSSES));
        this.addBossStructure(provider, RuneCraftory.modRes("water_ruins"),
                new RandomSpreadStructurePlacement(32, 16, RandomSpreadType.LINEAR, Math.abs(this.random.nextInt())),
                RunecraftoryTags.Biomes.WATER_RUINS, true,
                this.simple(StructureBossGen.WATER_RUIN_BOSSES));
        this.addBossStructure(provider, RuneCraftory.modRes("theater_ruins"),
                new RandomSpreadStructurePlacement(21, 7, RandomSpreadType.LINEAR, Math.abs(this.random.nextInt())),
                RunecraftoryTags.Biomes.THEATER_RUINS, true,
                this.simple(StructureBossGen.THEATER_RUIN_BOSSES));
        this.addBossStructure(provider, RuneCraftory.modRes("plains_arena"),
                new RandomSpreadStructurePlacement(24, 8, RandomSpreadType.LINEAR, Math.abs(this.random.nextInt())),
                RunecraftoryTags.Biomes.PLAINS_ARENA, true,
                this.simple(StructureBossGen.PLAINS_BOSSES));
        this.addBossStructure(provider, RuneCraftory.modRes("desert_arena"),
                new RandomSpreadStructurePlacement(24, 8, RandomSpreadType.LINEAR, Math.abs(this.random.nextInt())),
                RunecraftoryTags.Biomes.DESERT_ARENA, true,
                this.simple(StructureBossGen.DESERT_BOSSES));
        this.addBossStructure(provider, RuneCraftory.modRes("nether_arena"),
                new RandomSpreadStructurePlacement(20, 9, RandomSpreadType.LINEAR, Math.abs(this.random.nextInt())),
                RunecraftoryTags.Biomes.NETHER_ARENA, true,
                this.simple(StructureBossGen.NETHER_BOSSES));
        this.addBossStructure(provider, RuneCraftory.modRes("wind_shrine"),
                new RandomSpreadStructurePlacement(20, 7, RandomSpreadType.LINEAR, 1224466886),
                RunecraftoryTags.Biomes.WIND_SHRINE, true,
                this.simple(StructureBossGen.WIND_SHRINE_BOSSES));
        this.addBossStructure(provider, RuneCraftory.modRes("leon_karnak"),
                new RandomSpreadStructurePlacement(21, 8, RandomSpreadType.LINEAR, 1224466887),
                RunecraftoryTags.Biomes.LEON_KARNAK, true,
                this.simple(StructureBossGen.LEON_KARNAK_BOSSES));
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        return this.provider.thenAccept(this::add)
                .thenCompose(res -> CompletableFuture.allOf(this.subProviders.stream().map(p -> p.run(cache))
                        .toArray(CompletableFuture[]::new)));
    }

    @Override
    public String getName() {
        return "Structure World Gen Data";
    }

    @SuppressWarnings("deprecation")
    protected void addBossStructure(HolderLookup.Provider provider, ResourceLocation id,
                                    StructurePlacement placement, TagKey<Biome> biomeTag, boolean adapt,
                                    StructureProcessorList list) {
        ResourceLocation processorID = ResourceLocation.fromNamespaceAndPath(id.getNamespace(), id.getPath() + "_processor");
        this.processorListGen.add(processorID, list);
        this.templatePoolGen.add(id, new StructureTemplatePool(create(provider, Pools.EMPTY),
                List.of(Pair.of(StructurePoolElement.single(id.toString(), create(provider, Registries.PROCESSOR_LIST, processorID)), 1)),
                adapt ? StructureTemplatePool.Projection.RIGID : StructureTemplatePool.Projection.TERRAIN_MATCHING));
        this.structureGen.add(id, new ExtendedJigsawStructure(
                new Structure.StructureSettings(HolderSet.emptyNamed(provider.lookupOrThrow(Registries.BIOME), biomeTag),
                        Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.BEARD_BOX),
                new JigsawStructureData(create(provider, Registries.TEMPLATE_POOL, id),
                        Optional.empty(), 2, ConstantHeight.of(VerticalAnchor.absolute(0)),
                        Optional.of(Heightmap.Types.WORLD_SURFACE_WG), 90, List.of(),
                        DimensionPadding.ZERO, LiquidSettings.IGNORE_WATERLOGGING)
        ));
        this.structureSetGen.add(id, new StructureSet(create(provider, Registries.STRUCTURE, id), placement));
    }

    protected StructureProcessorList simple(ResourceLocation boss) {
        if (!this.verifier.exists(boss, PackType.SERVER_DATA, StructureBossManager.DIRECTORY))
            throw new IllegalStateException("StructureBoss data does not exist");
        return new StructureProcessorList(List.of(new BossSpawnerProcessor(boss)));
    }
}
