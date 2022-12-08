package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(SnowLayerBlock.class)
public class SnowLayerBlockMixin {

    @Inject(method = "getStateForPlacement", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void replace(BlockPlaceContext context, CallbackInfoReturnable<BlockState> info, BlockState state) {
        if (state.is(ModBlocks.snow.get())) {
            int i = state.getValue(SnowLayerBlock.LAYERS);
            info.setReturnValue(Blocks.SNOW.defaultBlockState().setValue(SnowLayerBlock.LAYERS, Math.min(8, i + 1)));
        }
    }
}
