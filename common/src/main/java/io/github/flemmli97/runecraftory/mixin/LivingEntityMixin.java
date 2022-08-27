package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.common.attachment.EntityData;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "setSprinting", at = @At("HEAD"), cancellable = true)
    private void onSprint(boolean sprinting, CallbackInfo info) {
        if (sprinting && Platform.INSTANCE.getEntityData((LivingEntity) (Object) this).map(EntityData::isParalysed).orElse(false)) {
            info.cancel();
        }
    }
}
