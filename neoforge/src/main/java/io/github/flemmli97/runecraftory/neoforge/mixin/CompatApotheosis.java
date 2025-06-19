package io.github.flemmli97.runecraftory.neoforge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;

@Pseudo
@Mixin(targets = "shadows/apotheosis/adventure/client/AdventureModuleClient")
public abstract class CompatApotheosis {

//    /**
//     * Disabling the stat tooltip for matching itemstacks cause this mod handles them
//     */
//    @Inject(method = "getHideFlags", remap = false, at = @At("RETURN"), cancellable = true)
//    private static void runecraftory_disableMod(ItemStack stack, CallbackInfoReturnable<Integer> info) {
//        if (ItemStat.SHOW_STATS_CUSTOM && ItemNBT.shouldHaveStats(stack))
//            info.setReturnValue(info.getReturnValue() | ItemStack.TooltipPart.MODIFIERS.getMask());
//    }
}
