package io.github.flemmli97.runecraftory.fabric.mixin;

import io.github.flemmli97.runecraftory.common.events.EntityCalls;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.levelgen.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PhantomSpawner.class)
public abstract class PhantomSpawnerMixin {

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void tick(ServerLevel level, boolean spawnEnemies, boolean spawnFriendlies, CallbackInfoReturnable<Integer> info) {
        if (EntityCalls.disableNatural(MobSpawnType.NATURAL, EntityType.PHANTOM)) {
            info.setReturnValue(0);
            info.cancel();
        }
    }
}
