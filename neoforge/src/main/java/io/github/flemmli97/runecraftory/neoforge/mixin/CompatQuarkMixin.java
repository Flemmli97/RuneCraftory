package io.github.flemmli97.runecraftory.neoforge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;

@Pseudo
@Mixin(targets = "vazkii/quark/content/client/tooltip/AttributeTooltips")
public abstract class CompatQuarkMixin {

//    /**
//     * Disabling the stat tooltip for matching itemstacks cause this mod handles them
//     */
//    @Inject(method = "canStripAttributes", at = @At("TAIL"), remap = false, cancellable = true)
//    private static void runecraftory_disableToolTip(ItemStack stack, EquipmentSlot slot, CallbackInfoReturnable<Boolean> info) {
//        if (ItemStat.SHOW_STATS_CUSTOM && ItemNBT.shouldHaveStats(stack))
//            info.setReturnValue(false);
//    }
}
