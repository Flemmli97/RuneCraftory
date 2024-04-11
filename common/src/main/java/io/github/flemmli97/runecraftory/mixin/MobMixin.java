package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.mixinhelper.MobNoAIHandler;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity implements MobNoAIHandler {

    @Unique
    private boolean runecraftory_ignoreNoAI;

    private MobMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "isEffectiveAi", at = @At("HEAD"), cancellable = true)
    private void ignoreNoAITemp(CallbackInfoReturnable<Boolean> info) {
        if (this.runecraftory_ignoreNoAI) {
            info.setReturnValue(super.isEffectiveAi());
            this.runecraftory_ignoreNoAI = false;
        }
    }

    @Override
    public void setIgnoreNoAI() {
        this.runecraftory_ignoreNoAI = true;
    }
}
