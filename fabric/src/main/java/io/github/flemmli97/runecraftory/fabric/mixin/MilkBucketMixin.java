package io.github.flemmli97.runecraftory.fabric.mixin;

import io.github.flemmli97.runecraftory.fabric.mixinhelper.PotionCureHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MilkBucketItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MilkBucketItem.class)
public class MilkBucketMixin {

    @Redirect(method = "finishUsingItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;removeAllEffects()Z"))
    public boolean finishUsingItem(LivingEntity livingEntity, ItemStack stack) {
        return PotionCureHelper.cureEffects(livingEntity, stack);
    }
}
