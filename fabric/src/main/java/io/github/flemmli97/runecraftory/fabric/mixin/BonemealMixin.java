package io.github.flemmli97.runecraftory.fabric.mixin;

import io.github.flemmli97.runecraftory.common.events.EntityCalls;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BoneMealItem.class)
public abstract class BonemealMixin {

    @Inject(method = "growCrop", at = @At(value = "HEAD"), cancellable = true)
    private static void bonemealCheck(ItemStack stack, Level level, BlockPos pos, CallbackInfoReturnable<Boolean> info) {
        if (EntityCalls.onTryBonemeal(level, stack, level.getBlockState(pos), pos, null))
            info.setReturnValue(false);
    }
}
