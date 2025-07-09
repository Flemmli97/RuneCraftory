package io.github.flemmli97.runecraftory.fabric.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.flemmli97.runecraftory.fabric.mixinhelper.ItemStackAttributeHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @ModifyExpressionValue(method = "forEachModifier(Lnet/minecraft/world/entity/EquipmentSlotGroup;Ljava/util/function/BiConsumer;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getOrDefault(Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Ljava/lang/Object;"))
    private Object modifyAttributes(Object original) {
        return ItemStackAttributeHelper.modifyData((ItemStack) (Object) this, (ItemAttributeModifiers) original);
    }

    @ModifyExpressionValue(method = "forEachModifier(Lnet/minecraft/world/entity/EquipmentSlot;Ljava/util/function/BiConsumer;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getOrDefault(Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Ljava/lang/Object;"))
    private Object modifyAttributes2(Object original) {
        return ItemStackAttributeHelper.modifyData((ItemStack) (Object) this, (ItemAttributeModifiers) original);
    }
}
