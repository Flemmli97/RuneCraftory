package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.common.attachment.ToggleStateHandler;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEffects;
import io.github.flemmli97.runecraftory.mixinhelper.MobToggleHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity implements MobToggleHandler {

    @Unique
    private final ToggleStateHandler runecraftory$mobAiStateHandler = new ToggleStateHandler((Mob) (Object) this, ((Mob) (Object) this).isNoAi(),
            state -> ((Mob) (Object) this).setNoAi(state));

    @Unique
    private boolean runecraftory$ignoreNoAI;

    private MobMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "isEffectiveAi", at = @At("HEAD"), cancellable = true)
    private void ignoreNoAITemp(CallbackInfoReturnable<Boolean> info) {
        if (this.runecraftory$ignoreNoAI) {
            info.setReturnValue(super.isEffectiveAi());
            this.runecraftory$ignoreNoAI = false;
        }
    }

    @Inject(method = "setNoAi", at = @At("RETURN"))
    private void onSetNoAi(boolean noAi, CallbackInfo info) {
        this.runecraftory$mobAiStateHandler.updateDefaultedValue(noAi);
    }

    @Inject(method = "playAmbientSound", at = @At("HEAD"), cancellable = true)
    private void onAmbient(CallbackInfo info) {
        if (this.hasEffect(RuneCraftoryEffects.SLEEP.asHolder()))
            info.cancel();
    }

    @Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
    private void onSave(CompoundTag compound, CallbackInfo ci) {
        compound.put("RunecraftoryAiState", this.runecraftory$NoAIState().save());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
    private void onRead(CompoundTag compound, CallbackInfo ci) {
        this.runecraftory$mobAiStateHandler.read(compound.getCompound("RunecraftoryAiState"));
    }

    @Override
    public void runecraftory$setIgnoreNoAI() {
        this.runecraftory$ignoreNoAI = true;
    }

    @Override
    public ToggleStateHandler runecraftory$NoAIState() {
        return this.runecraftory$mobAiStateHandler;
    }
}
