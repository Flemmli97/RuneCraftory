package io.github.flemmli97.runecraftory.common.world.structure;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.PostPlacementProcessor;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;

public class NetherJigsawStructure extends StructureFeature<JigsawConfiguration> {

    private static final WorldgenRandom RANDOM = new WorldgenRandom(new LegacyRandomSource(0));

    public NetherJigsawStructure(Codec<JigsawConfiguration> codec, int min, int max) {
        super(codec, ctx -> createPiecesGenerator(ctx, context -> NetherJigsawStructure.tryFindFittingPos(context, min, max)), PostPlacementProcessor.NONE);
    }

    public static Optional<PieceGenerator<JigsawConfiguration>> createPiecesGenerator(PieceGeneratorSupplier.Context<JigsawConfiguration> context,
                                                                                      Function<PieceGeneratorSupplier.Context<JigsawConfiguration>, BlockPos> positionFinder) {
        BlockPos pos = positionFinder.apply(context);
        if (pos == null) {
            return Optional.empty();
        }
        return JigsawPlacement.addPieces(
                context,
                PoolElementStructurePiece::new,
                pos,
                false,
                false
        );
    }

    public static BlockPos tryFindFittingPos(PieceGeneratorSupplier.Context<JigsawConfiguration> context, int min, int max) {
        BlockPos center = context.chunkPos().getMiddleBlockPosition(0);
        NoiseColumn column = context.chunkGenerator().getBaseColumn(center.getX(), center.getZ(), context.heightAccessor());
        List<BlockPos> matching = new ArrayList<>();
        for(int i = min; i < max; i++) {
            BlockState state = column.getBlock(i);
            if(!state.isAir() && column.getBlock(i + 1).isAir())
                matching.add(new BlockPos(center.getX(), i, center.getZ()));
        }
        if(matching.isEmpty())
            return null;
        RANDOM.setLargeFeatureSeed(context.seed(), context.chunkPos().x, context.chunkPos().z);
        return matching.get(RANDOM.nextInt(matching.size()));
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.TOP_LAYER_MODIFICATION;
    }
}
