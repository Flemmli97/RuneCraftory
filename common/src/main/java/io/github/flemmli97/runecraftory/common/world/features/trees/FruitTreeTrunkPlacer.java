package io.github.flemmli97.runecraftory.common.world.features.trees;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.common.blocks.BlockTreeBase;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.common.registry.ModFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.BiConsumer;

public class FruitTreeTrunkPlacer extends TrunkPlacer {

    public static final Codec<FruitTreeTrunkPlacer> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(Codec.INT.fieldOf("baseHeight").forGetter(d -> d.baseHeight),
                    Codec.INT.fieldOf("heightRandA").forGetter(d -> d.heightRandA),
                    Codec.INT.fieldOf("minBranches").forGetter(d -> d.minBranches),
                    Codec.INT.fieldOf("maxBranches").forGetter(d -> d.maxBranches)
            ).apply(instance, FruitTreeTrunkPlacer::new));

    private final int minBranches, maxBranches;

    public FruitTreeTrunkPlacer(int baseHeight, int heightRandA, int minBranches, int maxBranches) {
        super(baseHeight, heightRandA, 0);
        this.minBranches = minBranches;
        this.maxBranches = maxBranches;
    }

    protected static boolean placeIfFree(LevelSimulatedReader level, Map<BlockPos, BlockState> blockSetter, BlockPos pos, BlockState state) {
        if (level.isStateAtPosition(pos, BlockTreeBase::isAirOrReplaceable)) {
            blockSetter.put(pos, state);
            return true;
        }
        return false;
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return ModFeatures.FRUIT_TRUNK_PLACER.get();
    }

    @Override
    public int getTreeHeight(Random random) {
        return random.nextInt(1 + this.heightRandA) + this.baseHeight;
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter, Random random, int freeTreeHeight, BlockPos pos, TreeConfiguration config) {
        BlockPos soil = pos.below();
        Map<BlockPos, BlockState> placements = new HashMap<>();
        if (level.isStateAtPosition(soil, s -> !s.is(ModBlocks.treeSoil.get()))) {
            placements.put(soil, ModBlocks.treeSoil.get().defaultBlockState());
        }
        List<FoliagePlacer.FoliageAttachment> foliagePos = new ArrayList<>();
        for (int i = 1; i <= freeTreeHeight; i++) {
            BlockPos log = pos.above(i);
            if (!placeIfFree(level, placements, log, config.trunkProvider.getState(random, log)))
                return List.of();
            if (i == freeTreeHeight && freeTreeHeight <= 3) {
                foliagePos.add(new FoliagePlacer.FoliageAttachment(log.above(), -1, false));
            }
        }
        List<Direction> dirs = new ArrayList<>(Direction.Plane.HORIZONTAL.stream().toList());
        float chance = 1;
        for (int i = 0; i < dirs.size(); i++) {
            Direction dir = dirs.remove(random.nextInt(dirs.size()));
            if (random.nextFloat() < chance) {
                int offset = freeTreeHeight <= 3 || random.nextInt(3) == 0 ? 1 : 0;
                if (!this.growBranch(pos.above(freeTreeHeight + offset), dir, config, level, placements, random, random.nextInt(this.minBranches, this.maxBranches + 1), foliagePos))
                    return List.of();
                chance -= 0.3;
            }
        }
        placements.forEach(blockSetter);
        return foliagePos;
    }

    private boolean growBranch(BlockPos pos, Direction dir, TreeConfiguration config, LevelSimulatedReader level, Map<BlockPos, BlockState> blockSetter, Random rand, int amount, List<FoliagePlacer.FoliageAttachment> logs) {
        List<BlockPos> list = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0)
                    continue;
                if ((dir.getStepX() != 0 && i == -dir.getStepX()) || (dir.getStepZ() != 0 && j == -dir.getStepZ()))
                    continue;
                list.add(new BlockPos(i, 0, j));
            }
        }
        BlockPos.MutableBlockPos mut = pos.mutable();
        for (int i = 0; i < amount; i++) {
            BlockPos d = list.get(rand.nextInt(list.size()));
            dir = d.getX() == 0 || d.getZ() == 0 ? Direction.fromNormal(d) : rand.nextBoolean() ? dir.getClockWise() : dir;
            if (i != 0 && (mut.getY() == pos.getY() ? rand.nextInt(2) == 0 : rand.nextInt(3) == 0))
                d = d.above();
            mut.move(d);
            BlockPos newPos = mut.immutable();
            if (!placeIfFree(level, blockSetter, newPos, config.trunkProvider.getState(rand, newPos).setValue(RotatedPillarBlock.AXIS, dir.getAxis())))
                return false;
            logs.add(new FoliagePlacer.FoliageAttachment(newPos.above(), 0, false));
        }
        return true;
    }
}
