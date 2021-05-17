package com.flemmli97.runecraftory.mixin;

import com.flemmli97.runecraftory.common.utils.EntityUtils;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "wakeUp", at = @At(value = "HEAD"), cancellable = true)
    private void keepSleeping(CallbackInfo info) {
        if (EntityUtils.sleeping((LivingEntity) (Object) this))
            info.cancel();
    }
}
