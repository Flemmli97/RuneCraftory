package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.client.ClientCalls;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRendererDispatcherMixin {

    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    private <E extends Entity> void onInvisCheck(E entity, Frustum frustum, double camX, double camY, double camZ, CallbackInfoReturnable<Boolean> info) {
        if (entity instanceof LivingEntity living && ClientCalls.invis(living))
            info.setReturnValue(false);
    }
}
