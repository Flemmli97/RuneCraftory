package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public abstract class MouseHandlerMixin {

    @Shadow
    private Minecraft minecraft;
    @Shadow
    private double accumulatedDX;
    @Shadow
    private double accumulatedDY;

    @Inject(method = "turnPlayer", at = @At("HEAD"), cancellable = true)
    private void disableMouse(CallbackInfo info) {
        if (this.minecraft.player != null && EntityUtils.isDisabled(this.minecraft.player)) {
            this.accumulatedDX = 0;
            this.accumulatedDY = 0;
            MouseHandler handler = (MouseHandler) (Object) this;
            if(handler.isMouseGrabbed())
                handler.releaseMouse();
            info.cancel();
        }
    }

    @Inject(method = "onPress", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getOverlay()Lnet/minecraft/client/gui/screens/Overlay;"), cancellable = true)
    private void disableMousePress(long windowPointer, int button, int action, int modifiers, CallbackInfo info) {
        if (this.minecraft.player != null && EntityUtils.isDisabled(this.minecraft.player)) {
            info.cancel();
        }
    }

    @Inject(method = "onScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getOverlay()Lnet/minecraft/client/gui/screens/Overlay;"), cancellable = true)
    private void disableMouseScroll(CallbackInfo info) {
        if (this.minecraft.player != null && EntityUtils.isDisabled(this.minecraft.player)) {
            info.cancel();
        }
    }
}
