package io.github.flemmli97.runecraftory.fabric.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.common.config.ClientConfig;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import net.minecraft.client.gui.Gui;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class GuiMixin {

    @Inject(method = "renderHearts", at = @At("HEAD"), cancellable = true)
    private void health(PoseStack poseStack, Player player, int x, int y, int height, int i, float f, int j, int k, int l, boolean bl, CallbackInfo info) {
        if (ClientConfig.renderHealthRPBar == ClientConfig.HealthRPRenderType.BOTH)
            info.cancel();
    }

    @ModifyVariable(method = "renderPlayerHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;getVehicleMaxHearts(Lnet/minecraft/world/entity/LivingEntity;)I", shift = At.Shift.BY, by = 2), ordinal = 13)
    private int food(int old) {
        if (old == 0 && GeneralConfig.disableHunger)
            return -1;
        return old;
    }
}
