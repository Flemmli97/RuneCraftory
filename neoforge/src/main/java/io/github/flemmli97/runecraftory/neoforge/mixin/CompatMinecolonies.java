package io.github.flemmli97.runecraftory.neoforge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;

@Pseudo
@Mixin(targets = "com/minecolonies/coremod/entity/ai/citizen/guard/AbstractEntityAIGuard")
public abstract class CompatMinecolonies {
//
//    /**
//     * Disabling targeting of tamed monsters. Needed cause minecolonies uses a custom targeting system
//     */
//    @Inject(method = "isAttackableTarget", remap = false, at = @At("HEAD"), cancellable = true)
//    private static void runecraftory_checkTamedMonster(AbstractEntityCitizen user, LivingEntity entity, CallbackInfoReturnable<Boolean> info) {
//        if (entity instanceof BaseMonster monster && monster.isTamed())
//            info.setReturnValue(false);
//    }
}
