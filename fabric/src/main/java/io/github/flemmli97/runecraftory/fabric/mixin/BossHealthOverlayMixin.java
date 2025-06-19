package io.github.flemmli97.runecraftory.fabric.mixin;

import io.github.flemmli97.runecraftory.client.BossBarTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.world.BossEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BossHealthOverlay.class)
public class BossHealthOverlayMixin {

    @Unique
    private int runecraftory$Boss_inc;

    @ModifyVariable(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;III)I"),
            ordinal = 1)
    private int incrementAdd(int origin) {
        if (this.runecraftory$Boss_inc != 0)
            return this.runecraftory$Boss_inc - (10 + 9);
        return origin;
    }

    @Inject(method = "drawBar(Lnet/minecraft/client/gui/GuiGraphics;IILnet/minecraft/world/BossEvent;)V", at = @At("HEAD"), cancellable = true)
    private void onDrawingBar(GuiGraphics guiGraphics, int x, int y, BossEvent bossEvent, CallbackInfo info) {
        this.runecraftory$Boss_inc = BossBarTracker.tryRenderCustomBossbar(guiGraphics, x, y, bossEvent, false);
        if (this.runecraftory$Boss_inc != 0)
            info.cancel();
    }
}
