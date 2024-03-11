package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.mixinhelper.DisableTicking;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GoalSelector.class)
public abstract class GoalSelectorMixin implements DisableTicking {

    @Unique
    private boolean runecraftoryDisabled;

    @Inject(method = "tickRunningGoals", at = @At(value = "HEAD"), cancellable = true)
    private void checkDisabled(boolean tickAllRunning, CallbackInfo info) {
        if (this.runecraftoryDisabled)
            info.cancel();
    }

    @Override
    public void disable(boolean disable) {
        this.runecraftoryDisabled = disable;
    }
}
