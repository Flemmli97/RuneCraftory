package io.github.flemmli97.runecraftory.fabric.mixin;

import io.github.flemmli97.runecraftory.client.ClientCalls;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin {

    @Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/Input;tick(Z)V", shift = At.Shift.AFTER))
    private void disableMovement(CallbackInfo info) {
        LocalPlayer player = (LocalPlayer) (Object) this;
        ClientCalls.handleInputUpdate(player, player.input);
    }
}
