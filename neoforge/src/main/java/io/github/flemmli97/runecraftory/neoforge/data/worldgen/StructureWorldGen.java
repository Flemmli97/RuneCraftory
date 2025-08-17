package io.github.flemmli97.runecraftory.neoforge.data.worldgen;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.datapack.manager.StructureBossManager;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.world.structure.ExtendedJigsawStructure;
import io.github.flemmli97.runecraftory.common.world.structure.JigsawStructureData;
import io.github.flemmli97.runecraftory.common.world.structure.processors.BossSpawnerProcessor;
import io.github.flemmli97.runecraftory.neoforge.data.PostProcessVerification;
import io.github.flemmli97.runecraftory.neoforge.data.StructureBossGen;
import io.github.flemmli97.runecraftory.neoforge.data.worldgen.structures.TemplatePoolGen;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;

public class StructureWorldGen {

    private static final Random RANDOM = new Random();

    public static <T> Holder<T> create(BootstrapContext<?> ctx, ResourceKey<T> key) {
        return ctx.lookup(key.registryKey()).getOrThrow(key);
    }

    public static <T> Holder<T> create(BootstrapContext<?> ctx, ResourceKey<Registry<T>> key, ResourceLocation location) {
        return ctx.lookup(key)
                .getOrThrow(ResourceKey.create(key, location));
    }

    public static void createWorldgenStructures(RegistrySetBuilder builder, PostProcessVerification verifier) {
        Map<ResourceLocation, StructureDataHolder> structures = new HashMap<>();
        RANDOM.setSeed(123456789);
        addBossStructure(structures, RuneCraftory.modRes("forest_grove"),
                new RandomSpreadStructurePlacement(23, 8, RandomSpreadType.LINEAR, Math.abs(RANDOM.nextInt())),
                RunecraftoryTags.Biomes.FOREST_GROVE, true,
                StructureBossGen.FOREST_BOSSES, verifier);
        addBossStructure(structures, RuneCraftory.modRes("water_ruins"),
                new RandomSpreadStructurePlacement(32, 16, RandomSpreadType.LINEAR, Math.abs(RANDOM.nextInt())),
                RunecraftoryTags.Biomes.WATER_RUINS, true,
                StructureBossGen.WATER_RUIN_BOSSES, verifier);
        addBossStructure(structures, RuneCraftory.modRes("theater_ruins"),
                new RandomSpreadStructurePlacement(21, 7, RandomSpreadType.LINEAR, Math.abs(RANDOM.nextInt())),
                RunecraftoryTags.Biomes.THEATER_RUINS, true,
                StructureBossGen.THEATER_RUIN_BOSSES, verifier);
        addBossStructure(structures, RuneCraftory.modRes("plains_arena"),
                new RandomSpreadStructurePlacement(24, 8, RandomSpreadType.LINEAR, Math.abs(RANDOM.nextInt())),
                RunecraftoryTags.Biomes.PLAINS_ARENA, true,
                StructureBossGen.PLAINS_BOSSES, verifier);
        addBossStructure(structures, RuneCraftory.modRes("desert_arena"),
                new RandomSpreadStructurePlacement(24, 8, RandomSpreadType.LINEAR, Math.abs(RANDOM.nextInt())),
                RunecraftoryTags.Biomes.DESERT_ARENA, true,
                StructureBossGen.DESERT_BOSSES, verifier);
        addBossStructure(structures, RuneCraftory.modRes("nether_arena"),
                new RandomSpreadStructurePlacement(20, 9, RandomSpreadType.LINEAR, Math.abs(RANDOM.nextInt())),
                RunecraftoryTags.Biomes.NETHER_ARENA, true,
                StructureBossGen.NETHER_BOSSES, verifier);
        addBossStructure(structures, RuneCraftory.modRes("wind_shrine"),
                new RandomSpreadStructurePlacement(20, 7, RandomSpreadType.LINEAR, 1224466886),
                RunecraftoryTags.Biomes.WIND_SHRINE, true,
                StructureBossGen.WIND_SHRINE_BOSSES, verifier);
        addBossStructure(structures, RuneCraftory.modRes("leon_karnak"),
                new RandomSpreadStructurePlacement(21, 8, RandomSpreadType.LINEAR, 1224466887),
                RunecraftoryTags.Biomes.LEON_KARNAK, true,
                StructureBossGen.LEON_KARNAK_BOSSES, verifier);

        builder.add(Registries.PROCESSOR_LIST, ctx ->
                structures.values().forEach(data ->
                        ctx.register(ResourceKey.create(Registries.PROCESSOR_LIST, data.processorList().getFirst()), data.processorList().getSecond().apply(ctx))));
        builder.add(Registries.TEMPLATE_POOL, ctx -> {
            structures.forEach((key, data) -> {
                ctx.register(ResourceKey.create(Registries.TEMPLATE_POOL, key), data.templatePoolBuilder().apply(ctx));
            });
            TemplatePoolGen.bootStrap(ctx);
        });
        builder.add(Registries.STRUCTURE, ctx ->
                structures.forEach((key, data) ->
                        ctx.register(ResourceKey.create(Registries.STRUCTURE, key), data.structureBuilder().apply(ctx))));
        builder.add(Registries.STRUCTURE_SET, ctx ->
                structures.forEach((key, data) ->
                        ctx.register(ResourceKey.create(Registries.STRUCTURE_SET, key), data.structureSetBuilder().apply(ctx))));
    }

    protected static void addBossStructure(Map<ResourceLocation, StructureDataHolder> data, ResourceLocation id,
                                           StructurePlacement placement, TagKey<Biome> biomeTag, boolean adapt,
                                           ResourceLocation bossId, PostProcessVerification verifier) {
        ResourceLocation processorID = ResourceLocation.fromNamespaceAndPath(id.getNamespace(), id.getPath() + "_processor");
        data.put(id, new StructureDataHolder(Pair.of(processorID, ctx -> simple(bossId, verifier)),
                ctx -> new StructureTemplatePool(create(ctx, Pools.EMPTY),
                        List.of(Pair.of(StructurePoolElement.single(id.toString(), create(ctx, Registries.PROCESSOR_LIST, processorID)), 1)),
                        adapt ? StructureTemplatePool.Projection.RIGID : StructureTemplatePool.Projection.TERRAIN_MATCHING),
                ctx -> new ExtendedJigsawStructure(
                        new Structure.StructureSettings(ctx.lookup(Registries.BIOME).getOrThrow(biomeTag),
                                Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.BEARD_BOX),
                        new JigsawStructureData(create(ctx, Registries.TEMPLATE_POOL, id),
                                Optional.empty(), 2, ConstantHeight.of(VerticalAnchor.absolute(0)),
                                Optional.of(Heightmap.Types.WORLD_SURFACE_WG), 90, List.of(),
                                DimensionPadding.ZERO, LiquidSettings.IGNORE_WATERLOGGING)),
                ctx -> new StructureSet(create(ctx, Registries.STRUCTURE, id), placement)));
    }

    protected static StructureProcessorList simple(ResourceLocation boss, PostProcessVerification verifier) {
        verifier.add(ver -> {
            if (!ver.exists(boss, PackType.SERVER_DATA, StructureBossManager.DIRECTORY))
                throw new IllegalStateException("StructureBoss data does not exist: " + boss);
        });
        return new StructureProcessorList(List.of(new BossSpawnerProcessor(boss)));
    }

    protected record StructureDataHolder(
            Pair<ResourceLocation, Function<BootstrapContext<?>, StructureProcessorList>> processorList,
            Function<BootstrapContext<?>, StructureTemplatePool> templatePoolBuilder,
            Function<BootstrapContext<?>, Structure> structureBuilder,
            Function<BootstrapContext<?>, StructureSet> structureSetBuilder) {
    }
}
