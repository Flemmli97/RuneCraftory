package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.common.utils.SeasonUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SnowLayerBlock.class)
public abstract class SnowLayerBlockMixin {

    @Inject(method = "randomTick", at = @At(value = "RETURN"))
    private void meltingTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo info) {
        SeasonUtils.doSnowMelt(state, level, pos, random);
    }
}
