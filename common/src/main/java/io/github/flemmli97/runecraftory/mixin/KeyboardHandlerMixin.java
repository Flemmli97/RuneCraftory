package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.client.ClientHandlers;
import net.minecraft.client.KeyboardHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public abstract class KeyboardHandlerMixin {

    @Inject(method = "keyPress", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/chat/NarratorChatListener;isActive()Z"), cancellable = true)
    private void keyDisable(long windowPointer, int key, int scanCode, int action, int modifiers, CallbackInfo info) {
        if (ClientHandlers.disableKeys(key, scanCode))
            info.cancel();
    }
}
