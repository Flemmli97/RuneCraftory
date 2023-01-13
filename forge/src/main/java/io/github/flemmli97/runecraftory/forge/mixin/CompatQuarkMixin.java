package io.github.flemmli97.runecraftory.forge.mixin;

import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "vazkii/quark/content/client/tooltip/AttributeTooltips")
public abstract class CompatQuarkMixin {

    /**
     * Disabling the stat tooltip for matching itemstacks cause this mod handles them
     */
    @Inject(method = "canStripAttributes", at = @At("TAIL"), remap = false, cancellable = true)
    private static void disableToolTip(ItemStack stack, EquipmentSlot slot, CallbackInfoReturnable<Boolean> info) {
        if (ItemNBT.shouldHaveStats(stack))
            info.setReturnValue(false);
    }
}
