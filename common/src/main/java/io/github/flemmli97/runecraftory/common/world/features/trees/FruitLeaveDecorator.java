package io.github.flemmli97.runecraftory.common.world.features.trees;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.common.registry.ModFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public class FruitLeaveDecorator extends TreeDecorator {

    public static final Codec<FruitLeaveDecorator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockStateProvider.CODEC.fieldOf("fruit").forGetter(d -> d.fruit)).apply(instance, FruitLeaveDecorator::new));

    public final BlockStateProvider fruit;

    public FruitLeaveDecorator(BlockStateProvider fruit) {
        this.fruit = fruit;
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return ModFeatures.FRUIT_DECORATOR.get();
    }

    @Override
    public void place(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter, Random random, List<BlockPos> logPositions, List<BlockPos> leafPositions) {
        List<BlockPos> potentialFruits = new ArrayList<>(leafPositions.stream().filter(p -> Feature.isAir(level, p.below())).toList());
        int fruits = random.nextInt(3) + 4;
        for (int i = 0; i < fruits; i++) {
            if (potentialFruits.isEmpty())
                return;
            BlockPos pos = potentialFruits.remove(random.nextInt(potentialFruits.size()));
            blockSetter.accept(pos, this.fruit.getState(random, pos));
        }
    }
}
