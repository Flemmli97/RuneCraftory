package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.common.entities.GateEntity;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Inject(method = "canCollideWith", at = @At("HEAD"), cancellable = true)
    private void collideCheckEntitySensitive(Entity other, CallbackInfoReturnable<Boolean> info) {
        if (other instanceof GateEntity gate && !gate.canBeCollidedWith((Entity) (Object) this))
            info.setReturnValue(false);
    }
}
