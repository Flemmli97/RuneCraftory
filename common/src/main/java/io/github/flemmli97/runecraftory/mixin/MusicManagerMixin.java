package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.client.BossBarTracker;
import net.minecraft.client.sounds.MusicManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MusicManager.class)
public abstract class MusicManagerMixin {

    @Shadow
    private int nextSongDelay;

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void musicCheck(CallbackInfo info) {
        if (BossBarTracker.hasActiveMusic())
            this.nextSongDelay = Integer.MAX_VALUE;
    }
}
