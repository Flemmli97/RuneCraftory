package io.github.flemmli97.runecraftory.common.world.features;

import com.mojang.serialization.Codec;
import io.github.flemmli97.runecraftory.common.world.features.config.HerbFeatureConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.List;

public class HerbFeature extends Feature<HerbFeatureConfig> {

    public HerbFeature(Codec<HerbFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<HerbFeatureConfig> ctx) {
        BlockPos blockPos = ctx.origin();
        HerbFeatureConfig randomPatchConfiguration = ctx.config();
        Holder<Biome> biome = ctx.level().getBiome(blockPos);
        List<HerbFeatureConfig.HerbEntry> entries = randomPatchConfiguration.entries().stream()
                .filter(p -> p.getWeight().asInt() > 0 && p.biomes().test(biome)).toList();
        if (entries.isEmpty())
            return false;
        RandomSource random = ctx.random();
        HerbFeatureConfig.HerbEntry entry = WeightedRandom.getRandomItem(random, entries).orElse(null);
        if (entry == null)
            return false;
        WorldGenLevel worldGenLevel = ctx.level();
        int i = 0;
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        int j = randomPatchConfiguration.radius() + 1;
        int k = randomPatchConfiguration.ySpread() + 1;
        BlockState state = entry.stateProvider().getState(random, blockPos);
        for (int l = 0; l < randomPatchConfiguration.tries(); ++l) {
            mutableBlockPos.setWithOffset(blockPos, random.nextInt(j) - random.nextInt(j), random.nextInt(k) - random.nextInt(k), random.nextInt(j) - random.nextInt(j));
            if (worldGenLevel.isEmptyBlock(mutableBlockPos) && state.canSurvive(worldGenLevel, mutableBlockPos)) {
                worldGenLevel.setBlock(mutableBlockPos, state, Block.UPDATE_ALL);
                i++;
            }
        }
        return i > 0;
    }
}

