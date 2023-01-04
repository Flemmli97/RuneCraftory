package io.github.flemmli97.runecraftory.fabric.mixin;

import io.github.flemmli97.runecraftory.common.events.EntityCalls;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingHurtEvent {

    @Inject(method = "hurt", at = @At(value = "HEAD"), cancellable = true)
    private void onAttacked(DamageSource damageSrc, float damageAmount, CallbackInfoReturnable<Boolean> info) {
        if (EntityCalls.cancelLivingAttack(damageSrc, (LivingEntity) (Object) this))
            info.setReturnValue(false);
    }

    @ModifyVariable(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getDamageAfterArmorAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F"), argsOnly = true)
    private float hurt(float origin, DamageSource source) {
        return EntityCalls.damageCalculation((LivingEntity) (Object) this, source, origin);
    }

    @Inject(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setHealth(F)V"))
    private void hurtPost(DamageSource damageSrc, float damageAmount, CallbackInfo info) {
        EntityCalls.postDamage((LivingEntity) (Object) this, damageSrc, damageAmount);
    }
}
