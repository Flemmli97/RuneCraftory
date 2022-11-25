package io.github.flemmli97.runecraftory.forge.mixin;

import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "shadows/apotheosis/adventure/client/AdventureModuleClient")
public abstract class CompatApotheosis {

    @Inject(method = "getHideFlags", remap = false, at = @At("RETURN"), cancellable = true)
    private static void disableMod(ItemStack stack, CallbackInfoReturnable<Integer> info) {
        if (ItemNBT.shouldHaveStats(stack))
            info.setReturnValue(info.getReturnValue() | ItemStack.TooltipPart.MODIFIERS.getMask());
    }
}
