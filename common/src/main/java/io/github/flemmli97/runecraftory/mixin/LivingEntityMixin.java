package io.github.flemmli97.runecraftory.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.flemmli97.runecraftory.common.events.EntityCalls;
import io.github.flemmli97.runecraftory.platform.ExtendedEffect;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = "setSprinting", at = @At("HEAD"), cancellable = true)
    private void onSprint(boolean sprinting, CallbackInfo info) {
        if (sprinting && Platform.INSTANCE.getEntityData((LivingEntity) (Object) this).isParalysed()) {
            info.cancel();
        }
    }

    @Inject(method = "dropAllDeathLoot", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;dropEquipment()V", shift = At.Shift.AFTER))
    private void doDeathLootDrop(ServerLevel level, DamageSource damageSource, CallbackInfo info) {
        EntityCalls.dropInventoryDeath((LivingEntity) (Object) this);
    }

    @Inject(method = "collectEquipmentChanges", at = @At("RETURN"))
    private void onChange(CallbackInfoReturnable<Map<EquipmentSlot, ItemStack>> info, @Local Map<EquipmentSlot, ItemStack> map) {
        if (map != null)
            // Can't be a method reference!
            EntityCalls.updateEquipment((LivingEntity) (Object) this, map, this.getLastHandItem(EquipmentSlot.MAINHAND), this::getLastArmorItem);
    }

    @Inject(method = "onEffectAdded", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffect;addAttributeModifiers(Lnet/minecraft/world/entity/ai/attributes/AttributeMap;I)V"))
    private void onAddedEffect(MobEffectInstance effectInstance, Entity entity, CallbackInfo ci) {
        if (effectInstance.getEffect().value() instanceof ExtendedEffect eff) {
            eff.onEffectAdded((LivingEntity) (Object) this, effectInstance);
        }
    }

    @Inject(method = "onEffectRemoved", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffect;removeAttributeModifiers(Lnet/minecraft/world/entity/ai/attributes/AttributeMap;)V"))
    private void onAddedEffect(MobEffectInstance effectInstance, CallbackInfo ci) {
        if (effectInstance.getEffect().value() instanceof ExtendedEffect eff) {
            eff.onEffectRemoved((LivingEntity) (Object) this, effectInstance);
        }
    }

    @Shadow
    protected abstract ItemStack getLastHandItem(EquipmentSlot slot);

    @Shadow
    protected abstract ItemStack getLastArmorItem(EquipmentSlot slot);
}
