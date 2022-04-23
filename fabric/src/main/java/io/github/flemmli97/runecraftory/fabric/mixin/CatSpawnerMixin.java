package io.github.flemmli97.runecraftory.fabric.mixin;

import io.github.flemmli97.runecraftory.common.events.EntityCalls;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.npc.CatSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CatSpawner.class)
public abstract class CatSpawnerMixin {

    @Inject(method = "spawnCat", at = @At("HEAD"), cancellable = true)
    private void checkSpawnCat(BlockPos pos, ServerLevel level, CallbackInfoReturnable<Integer> info) {
        if (EntityCalls.disableNatural(MobSpawnType.NATURAL, EntityType.CAT)) {
            info.setReturnValue(0);
            info.cancel();
        }
    }

}
