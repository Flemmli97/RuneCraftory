package io.github.flemmli97.runecraftory.neoforge.mixin;

import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.core.entity.ai.workers.guard.AbstractEntityAIGuard;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(AbstractEntityAIGuard.class)
public abstract class CompatMinecolonies {

    /**
     * Disabling targeting of tamed monsters. Needed cause minecolonies uses a custom targeting system
     */
    @Inject(method = "isAttackableTarget", remap = false, at = @At("HEAD"), cancellable = true)
    private static void runecraftory_checkTamedMonster(AbstractEntityCitizen user, LivingEntity entity, CallbackInfoReturnable<Boolean> info) {
        if (entity instanceof BaseMonster monster && monster.isTamed())
            info.setReturnValue(false);
    }
}
