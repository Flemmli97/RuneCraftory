package io.github.flemmli97.runecraftory.common.world.features.trees;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

import java.util.ArrayList;
import java.util.List;

public class FruitLeaveDecorator extends TreeDecorator {

    public static final MapCodec<FruitLeaveDecorator> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(BlockStateProvider.CODEC.fieldOf("fruit").forGetter(d -> d.fruit),
                    IntProvider.NON_NEGATIVE_CODEC.fieldOf("amount").forGetter(d -> d.amount)
            ).apply(instance, FruitLeaveDecorator::new));

    public final BlockStateProvider fruit;
    public final IntProvider amount;

    public FruitLeaveDecorator(BlockStateProvider fruit, IntProvider amount) {
        this.fruit = fruit;
        this.amount = amount;
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return RuneCraftoryFeatures.FRUIT_DECORATOR.get();
    }

    @Override
    public void place(Context context) {
        List<BlockPos> potentialFruits = new ArrayList<>(context.leaves().stream().filter(p -> context.isAir(p.below())).toList());
        int fruits = this.amount.sample(context.random());
        for (int i = 0; i < fruits; i++) {
            if (potentialFruits.isEmpty())
                return;
            BlockPos pos = potentialFruits.remove(context.random().nextInt(potentialFruits.size()));
            context.setBlock(pos, this.fruit.getState(context.random(), pos));
        }
    }
}
