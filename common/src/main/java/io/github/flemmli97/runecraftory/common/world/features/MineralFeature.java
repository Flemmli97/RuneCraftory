package io.github.flemmli97.runecraftory.common.world.features;

import com.mojang.serialization.Codec;
import io.github.flemmli97.runecraftory.common.registry.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.material.Fluids;

public class MineralFeature extends Feature<ChancedBlockClusterConfig> {

    public MineralFeature(Codec<ChancedBlockClusterConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<ChancedBlockClusterConfig> ctx) {
        if (ctx.config().amount.getMaxValue() == 0)
            return false;
        Holder<Biome> biome = ctx.level().getBiome(ctx.origin());
        if (!this.match(ctx.config(), biome))
            return false;
        BlockState state = ctx.config().stateProvider.getState(ctx.random(), ctx.origin());
        int amount = ctx.config().amount.sample(ctx.random());
        if (amount <= 0)
            return false;
        for (int i = 0; i < ctx.config().tries; i++) {
            BlockPos pos = ctx.origin().offset(ctx.random().nextInt(ctx.config().radius) - ctx.random().nextInt(ctx.config().radius),
                    ctx.random().nextInt(ctx.config().radius) - ctx.random().nextInt(ctx.config().radius), ctx.random().nextInt(ctx.config().radius) - ctx.random().nextInt(ctx.config().radius));
            if (ctx.level().getBlockState(pos).getMaterial().isReplaceable() && ctx.level().getBlockState(pos.below()).is(ModTags.MINERAL_GEN_PLACE) && state.canSurvive(ctx.level(), pos)) {
                if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING))
                    state = state.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.Plane.HORIZONTAL.getRandomDirection(ctx.random()));
                if (state.hasProperty(BlockStateProperties.WATERLOGGED))
                    state = state.setValue(BlockStateProperties.WATERLOGGED, ctx.level().getFluidState(pos).getType() == Fluids.WATER);
                ctx.level().setBlock(pos, state, 3);
                amount--;
                if (amount == 0)
                    break;
            }
        }
        return true;
    }

    private boolean match(ChancedBlockClusterConfig cfg, Holder<Biome> biome) {
        return (cfg.whitelist == null || biome.is(cfg.whitelist))
                && (cfg.blacklist == null || !biome.is(cfg.blacklist));
    }
}