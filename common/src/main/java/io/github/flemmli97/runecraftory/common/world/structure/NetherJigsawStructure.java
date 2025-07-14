package io.github.flemmli97.runecraftory.common.world.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryStructures;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.StructureType;

import java.util.ArrayList;
import java.util.List;

public class NetherJigsawStructure extends ExtendedJigsawStructure {

    public static final MapCodec<NetherJigsawStructure> CODEC = RecordCodecBuilder.mapCodec((instance) ->
            instance.group(FilterHolderSet.FILTERED_CODEC.forGetter(d -> d.settings),
                    JigsawStructureData.CODEC.forGetter(d -> d.data),
                    Codec.INT.fieldOf("min_y").forGetter(d -> d.min),
                    Codec.INT.fieldOf("max_y").forGetter(d -> d.max)
            ).apply(instance, NetherJigsawStructure::new));

    private final int min, max;

    public NetherJigsawStructure(StructureSettings settings, JigsawStructureData data, int min, int max) {
        super(settings, data);
        this.min = min;
        this.max = max;
    }

    public static BlockPos tryFindFittingPos(GenerationContext context, int min, int max) {
        BlockPos center = context.chunkPos().getMiddleBlockPosition(0);
        NoiseColumn column = context.chunkGenerator().getBaseColumn(center.getX(), center.getZ(), context.heightAccessor(), context.randomState());
        List<BlockPos> matching = new ArrayList<>();
        for (int i = min; i < max; i++) {
            BlockState state = column.getBlock(i);
            if (!state.isAir() && column.getBlock(i + 1).isAir())
                matching.add(new BlockPos(center.getX(), i, center.getZ()));
        }
        if (matching.isEmpty())
            return null;
        context.random().setLargeFeatureSeed(context.seed(), context.chunkPos().x, context.chunkPos().z);
        return matching.get(context.random().nextInt(matching.size()));
    }

    @Override
    protected BlockPos calculatePosition(GenerationContext context) {
        return tryFindFittingPos(context, this.min, this.max);
    }

    @Override
    public StructureType<?> type() {
        return RuneCraftoryStructures.NETHER_STRUCTURE.get();
    }
}
