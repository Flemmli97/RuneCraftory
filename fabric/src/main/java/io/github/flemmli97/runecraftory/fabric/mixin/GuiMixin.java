package io.github.flemmli97.runecraftory.fabric.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.common.config.ClientConfig;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class GuiMixin {

    @Inject(method = "renderPlayerHealth", at = @At("HEAD"), cancellable = true)
    private void health(PoseStack stack, CallbackInfo info) {
        if (ClientConfig.renderOverlay)
            info.cancel();
    }
}
