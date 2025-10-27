package io.github.flemmli97.runecraftory.fabric.mixin;

import io.github.flemmli97.runecraftory.common.events.EntityCalls;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin {

    @ModifyVariable(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getDamageAfterArmorAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F"), argsOnly = true)
    private float hurt(float origin, DamageSource source) {
        return EntityCalls.damageCalculation((Player) (Object) this, source, origin);
    }

    @Inject(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;setHealth(F)V"))
    private void hurtPost(DamageSource damageSrc, float damageAmount, CallbackInfo info) {
        EntityCalls.postDamage((Player) (Object) this, damageSrc, damageAmount);
    }

    @Inject(method = "stopSleepInBed", at = @At(value = "HEAD"))
    private void onStopSleeping(boolean wakeImmediatly, boolean updateLevelForSleepingPlayers, CallbackInfo info) {
        if (!wakeImmediatly && !updateLevelForSleepingPlayers)
            EntityCalls.wakeUp((Player) (Object) this);
    }
}
