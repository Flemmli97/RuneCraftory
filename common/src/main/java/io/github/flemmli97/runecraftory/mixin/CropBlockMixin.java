package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.common.blocks.Growable;
import io.github.flemmli97.runecraftory.common.registry.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CropBlock.class)
public abstract class CropBlockMixin implements Growable {

    @Inject(method = "mayPlaceOn", at = @At("HEAD"), cancellable = true)
    private void checkPlace(BlockState state, BlockGetter level, BlockPos pos, CallbackInfoReturnable<Boolean> info) {
        if (state.is(ModTags.FARMLAND)) {
            info.setReturnValue(true);
            info.cancel();
        }
    }

    @Override
    public int getGrowableMaxAge() {
        return ((CropBlock) (Object) this).getMaxAge();
    }

    @Override
    public BlockState getGrowableStateForAge(BlockState current, int age) {
        return ((CropBlock) (Object) this).getStateForAge(age);
    }

    @Override
    public boolean isAtMaxAge(BlockState state) {
        return ((CropBlock) (Object) this).isMaxAge(state);
    }
}
