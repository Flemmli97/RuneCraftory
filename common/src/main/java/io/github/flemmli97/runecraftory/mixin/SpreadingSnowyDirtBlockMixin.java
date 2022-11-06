package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.SpreadingSnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(SpreadingSnowyDirtBlock.class)
public class SpreadingSnowyDirtBlockMixin {

    @Inject(method = "canBeGrass", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private static void grassCheck(BlockState state, LevelReader levelReader, BlockPos pos, CallbackInfoReturnable<Boolean> info, BlockPos above, BlockState aboveState) {
        if (aboveState.is(ModBlocks.snow.get()) && aboveState.getValue(SnowLayerBlock.LAYERS) == 1) {
            info.setReturnValue(true);
        }
    }

    @Redirect(method = "randomTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z", ordinal = 1))
    private boolean spreading(ServerLevel level, BlockPos pos, BlockState state) {
        return level.setBlockAndUpdate(pos, state.setValue(SpreadingSnowyDirtBlock.SNOWY, level.getBlockState(pos.above()).is(BlockTags.SNOW)));
    }
}
