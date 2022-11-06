package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.common.entities.ExtendedEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TargetingConditions.class)
public abstract class TargetingConditionsMixin {

    @Inject(method = "test", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;canAttack(Lnet/minecraft/world/entity/LivingEntity;)Z"), cancellable = true)
    private void testAdditional(LivingEntity attacker, LivingEntity target, CallbackInfoReturnable<Boolean> info) {
        if (target instanceof ExtendedEntity ex && !ex.canBeAttackedBy(attacker))
            info.setReturnValue(false);
    }
}
