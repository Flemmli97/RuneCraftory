package com.flemmli97.runecraftory.mixin;

import com.flemmli97.runecraftory.common.utils.IDisableBrain;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Brain.class)
public abstract class BrainMixin implements IDisableBrain {

    @Unique
    private boolean rfdisabled;

    @Inject(method = "tick(Lnet/minecraft/world/server/ServerWorld;Lnet/minecraft/entity/LivingEntity;)V", at = @At(value = "HEAD"), cancellable = true)
    private void checkDisabled(ServerWorld world, LivingEntity entity, CallbackInfo info) {
        if (this.rfdisabled)
            info.cancel();
    }

    @Override
    public void disableBrain(boolean disable) {
        this.rfdisabled = disable;
    }
}
