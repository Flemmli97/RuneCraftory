package io.github.flemmli97.runecraftory.common.world.structure;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryStructures;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasLookup;

import java.util.Optional;

public class ExtendedJigsawStructure extends Structure {

    public static final MapCodec<ExtendedJigsawStructure> CODEC = RecordCodecBuilder.mapCodec((instance) ->
            instance.group(FilterHolderSet.FILTERED_CODEC.forGetter(d -> d.settings),
                    JigsawStructureData.CODEC.forGetter(d -> d.data)
            ).apply(instance, ExtendedJigsawStructure::new));

    protected final StructureSettings settings;
    protected final JigsawStructureData data;

    public ExtendedJigsawStructure(StructureSettings settings, JigsawStructureData data) {
        super(settings);
        this.settings = settings;
        this.data = data;
    }

    @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        BlockPos blockPos = this.calculatePosition(context);
        if (blockPos == null)
            return Optional.empty();
        return JigsawPlacement.addPieces(context, this.data.startPool(), this.data.startJigsawName(), this.data.maxDepth(), blockPos,
                false, this.data.projectStartToHeightmap(), this.data.maxDistanceFromCenter(),
                PoolAliasLookup.create(this.data.poolAliases(), blockPos, context.seed()),
                this.data.dimensionPadding(), this.data.liquidSettings());
    }

    protected BlockPos calculatePosition(Structure.GenerationContext context) {
        ChunkPos chunkPos = context.chunkPos();
        int i = this.data.startHeight().sample(context.random(), new WorldGenerationContext(context.chunkGenerator(), context.heightAccessor()));
        return new BlockPos(chunkPos.getMinBlockX(), i, chunkPos.getMinBlockZ());
    }

    @Override
    public StructureType<?> type() {
        return RuneCraftoryStructures.EXTENDED_STRUCTURE.get();
    }
}
