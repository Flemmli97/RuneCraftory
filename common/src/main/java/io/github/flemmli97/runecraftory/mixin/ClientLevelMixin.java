package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.common.events.EntityCalls;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin {

    @Inject(method = "tickNonPassenger", at = @At("HEAD"), cancellable = true)
    private void onTickEntity(Entity entity, CallbackInfo info) {
        if (entity instanceof LivingEntity living && EntityCalls.rootTick(living)) {
            info.cancel();
            for (Entity passengers : entity.getPassengers()) {
                if (entity instanceof LivingEntity livingPass && EntityCalls.rootTick(livingPass))
                    continue;
                this.tickPassenger(entity, passengers);
            }
        }
    }

    @Shadow
    abstract void tickPassenger(Entity ridingEntity, Entity passengerEntity);
}
