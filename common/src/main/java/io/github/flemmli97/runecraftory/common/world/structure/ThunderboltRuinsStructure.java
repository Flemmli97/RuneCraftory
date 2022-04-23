package io.github.flemmli97.runecraftory.common.world.structure;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.PostPlacementProcessor;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;

import java.util.Optional;
import java.util.function.Function;

public class ThunderboltRuinsStructure extends StructureFeature<JigsawConfiguration> {

    public ThunderboltRuinsStructure(Codec<JigsawConfiguration> codec) {
        super(codec, ctx -> AmbrosiaForestStructure.createPiecesGenerator(ctx, ThunderboltRuinsStructure::isFeatureChunk), PostPlacementProcessor.NONE);
    }

    public static Optional<PieceGenerator<JigsawConfiguration>> createPiecesGenerator(PieceGeneratorSupplier.Context<JigsawConfiguration> context,
                                                                                      Function<PieceGeneratorSupplier.Context<JigsawConfiguration>, Boolean> isRightChunk) {
        if (!isRightChunk.apply(context)) {
            return Optional.empty();
        }

        BlockPos blockpos = context.chunkPos().getMiddleBlockPosition(0);
        int topLandY = context.chunkGenerator().getFirstFreeHeight(blockpos.getX(), blockpos.getZ(), Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor());
        blockpos = blockpos.above(topLandY - 2);
        return JigsawPlacement.addPieces(
                context,
                PoolElementStructurePiece::new,
                blockpos,
                false,
                false
        );
    }

    private static boolean isFeatureChunk(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
        return true;
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }
}
