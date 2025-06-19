package io.github.flemmli97.runecraftory.fabric.mixin;

import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

//    @ModifyVariable(method = "getAttributeModifiers", at = @At(value = "RETURN", shift = At.Shift.BY, by = -2))
//    private Multimap<Attribute, AttributeModifier> getStats(Multimap<Attribute, AttributeModifier> old, EquipmentSlot slot) {
//        return ItemNBT.getStatsAttributeMap((ItemStack) (Object) this, old, slot);
//    }
}
