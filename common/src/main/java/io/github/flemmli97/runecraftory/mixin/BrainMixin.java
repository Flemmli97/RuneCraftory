package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.mixinhelper.DisableTicking;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Brain.class)
public abstract class BrainMixin implements DisableTicking {

    @Unique
    private boolean runecraftory$Disabled;

    @Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
    private void checkDisabled(ServerLevel world, LivingEntity entity, CallbackInfo info) {
        if (this.runecraftory$Disabled)
            info.cancel();
    }

    @Override
    public void runecraftory$disable(boolean disable) {
        this.runecraftory$Disabled = disable;
    }
}
