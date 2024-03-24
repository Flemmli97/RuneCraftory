package io.github.flemmli97.runecraftory.fabric.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.client.BossBarTracker;
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
    private int runecraftory_boss_inc;

    @ModifyVariable(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Font;drawShadow(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/network/chat/Component;FFI)I"),
            ordinal = 1)
    private int incrementAdd(int origin) {
        if (this.runecraftory_boss_inc != 0)
            return this.runecraftory_boss_inc - (10 + 9);
        return origin;
    }

    @Inject(method = "drawBar", at = @At("HEAD"), cancellable = true)
    private void onDrawingBar(PoseStack poseStack, int x, int y, BossEvent bossEvent, CallbackInfo info) {
        this.runecraftory_boss_inc = BossBarTracker.tryRenderCustomBossbar(poseStack, x, y, bossEvent, false);
        if (this.runecraftory_boss_inc != 0)
            info.cancel();
    }
}
