package io.github.flemmli97.runecraftory.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WalkNodeEvaluator.class)
public class WalkNodeEvaluatorMixin {

    /**
     * Fix wrong start with non vanilla water
     */
    @ModifyExpressionValue(method = "getStart", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"))
    private boolean waterTagCheck(boolean res, @Local BlockState state) {
        if (state.getFluidState().is(FluidTags.WATER) && state.getFluidState().isSource())
            return true;
        return res;
    }
}
