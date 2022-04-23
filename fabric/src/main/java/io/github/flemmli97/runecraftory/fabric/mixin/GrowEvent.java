package io.github.flemmli97.runecraftory.fabric.mixin;

import io.github.flemmli97.runecraftory.fabric.event.CropGrowEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin({CropBlock.class})
public abstract class GrowEvent {

    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    private void checkCanGrow(BlockState state, ServerLevel level, BlockPos pos, Random random, CallbackInfo info) {
        if (!CropGrowEvent.EVENT.invoker().canGrow(level, state, pos))
            info.cancel();
    }
}
