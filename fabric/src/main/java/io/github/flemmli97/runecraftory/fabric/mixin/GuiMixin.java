package io.github.flemmli97.runecraftory.fabric.mixin;

import io.github.flemmli97.runecraftory.client.ClientCalls;
import io.github.flemmli97.runecraftory.common.config.ClientConfig;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class GuiMixin {

    @Shadow
    @Final
    private LayeredDraw layers;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void renderOverlay(CallbackInfo info) {
        this.layers.add(ClientCalls::renderScreenOverlays);
    }

    @Inject(method = "renderHearts", at = @At("HEAD"), cancellable = true)
    private void health(GuiGraphics guiGraphics, Player player, int x, int y, int height, int offsetHeartIndex, float maxHealth, int currentHealth, int displayHealth, int absorptionAmount, boolean renderHighlight, CallbackInfo info) {
        if (ClientConfig.renderHealthRpBar == ClientConfig.HealthRPRenderType.BOTH)
            info.cancel();
    }

    @Inject(method = "renderFood", at = @At("HEAD"), cancellable = true)
    private void food(GuiGraphics guiGraphics, Player player, int y, int x, CallbackInfo info) {
        if (GeneralConfig.disableHunger)
            info.cancel();
    }
}
