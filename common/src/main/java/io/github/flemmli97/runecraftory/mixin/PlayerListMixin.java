package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.common.events.EntityCalls;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public class PlayerListMixin {

    @Inject(method = "reloadResources", at = @At("RETURN"))
    private void onRoadingRes(CallbackInfo info) {
        EntityCalls.onResourceReloadEnd(((PlayerList) (Object) this).getServer());
    }
}
