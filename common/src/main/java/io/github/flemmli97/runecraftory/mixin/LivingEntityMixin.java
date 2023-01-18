package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.common.attachment.EntityData;
import io.github.flemmli97.runecraftory.common.events.EntityCalls;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Unique
    private boolean runecraftoryStopKnockback;

    @Inject(method = "setSprinting", at = @At("HEAD"), cancellable = true)
    private void onSprint(boolean sprinting, CallbackInfo info) {
        if (sprinting && Platform.INSTANCE.getEntityData((LivingEntity) (Object) this).map(EntityData::isParalysed).orElse(false)) {
            info.cancel();
        }
    }

    @Inject(method = "dropAllDeathLoot", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;dropEquipment()V", shift = At.Shift.AFTER))
    private void doDeathLootDrop(DamageSource source, CallbackInfo info) {
        EntityCalls.dropInventoryDeath((LivingEntity) (Object) this);
    }

    @Inject(method = "collectEquipmentChanges", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void onChange(CallbackInfoReturnable<Map<EquipmentSlot, ItemStack>> info, Map<EquipmentSlot, ItemStack> map) {
        if (map != null)
            EntityCalls.updateEquipmentNew((LivingEntity) (Object) this, map, this.getLastHandItem(EquipmentSlot.MAINHAND));
    }

    /**
     * The actual knockback method has no dmg context or anything
     */
    @Inject(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;knockback(DDD)V"))
    private void onHurtKnockback(DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
        if(source instanceof CustomDamage customDamage && customDamage.getKnockBackType() == CustomDamage.KnockBackType.NONE)
            this.runecraftoryStopKnockback = true;
    }

    @Inject(method = "knockback", at = @At("HEAD"), cancellable = true)
    private void knockbackCheck(double strength, double x, double z, CallbackInfo info) {
        if(this.runecraftoryStopKnockback)
            info.cancel();
        this.runecraftoryStopKnockback = false;
    }

    @Shadow
    abstract ItemStack getLastHandItem(EquipmentSlot slot);
}
