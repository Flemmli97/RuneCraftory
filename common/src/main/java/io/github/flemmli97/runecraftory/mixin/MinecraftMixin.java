package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.mixinhelper.MixinUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Shadow
    public LocalPlayer player;
    @Shadow
    public HitResult hitResult;

    @Inject(method = "startAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isHandsBusy()Z"), cancellable = true)
    private void stopSwing(CallbackInfoReturnable<Boolean> info) {
        if (MixinUtils.stop(this.player, this.player.getMainHandItem(), this.hitResult)) {
            info.setReturnValue(false);
            info.cancel();
        }
    }
}
