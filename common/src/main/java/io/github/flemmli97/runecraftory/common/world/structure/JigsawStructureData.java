package io.github.flemmli97.runecraftory.common.world.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.pools.DimensionPadding;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasBinding;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;

import java.util.List;
import java.util.Optional;

public record JigsawStructureData(Holder<StructureTemplatePool> startPool,
                                  Optional<ResourceLocation> startJigsawName,
                                  int maxDepth,
                                  HeightProvider startHeight,
                                  Optional<Heightmap.Types> projectStartToHeightmap,
                                  int maxDistanceFromCenter,
                                  List<PoolAliasBinding> poolAliases,
                                  DimensionPadding dimensionPadding,
                                  LiquidSettings liquidSettings) {

    public static MapCodec<JigsawStructureData> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(JigsawStructureData::startPool),
                    ResourceLocation.CODEC.optionalFieldOf("start_jigsaw_name").forGetter(JigsawStructureData::startJigsawName),
                    Codec.intRange(0, 20).fieldOf("size").forGetter(JigsawStructureData::maxDepth),
                    HeightProvider.CODEC.fieldOf("start_height").forGetter(JigsawStructureData::startHeight),
                    Heightmap.Types.CODEC.optionalFieldOf("project_start_to_heightmap").forGetter(JigsawStructureData::projectStartToHeightmap),
                    Codec.intRange(1, 128).fieldOf("max_distance_from_center").forGetter(JigsawStructureData::maxDistanceFromCenter),
                    Codec.list(PoolAliasBinding.CODEC).optionalFieldOf("pool_aliases", List.of()).forGetter(JigsawStructureData::poolAliases),
                    DimensionPadding.CODEC.optionalFieldOf("dimension_padding", JigsawStructure.DEFAULT_DIMENSION_PADDING).forGetter(JigsawStructureData::dimensionPadding),
                    LiquidSettings.CODEC.optionalFieldOf("liquid_settings", JigsawStructure.DEFAULT_LIQUID_SETTINGS).forGetter(JigsawStructureData::liquidSettings)
            ).apply(instance, JigsawStructureData::new));
}
