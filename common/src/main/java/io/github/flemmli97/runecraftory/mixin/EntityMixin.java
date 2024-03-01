package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.common.entities.GateEntity;
import io.github.flemmli97.runecraftory.mixinhelper.MixinUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    private float nextStep;
    @Unique
    private double runecraftoryMoveTracker;

    @Inject(method = "canCollideWith", at = @At("HEAD"), cancellable = true)
    private void collideCheckEntitySensitive(Entity other, CallbackInfoReturnable<Boolean> info) {
        if (other instanceof GateEntity gate && !gate.canBeCollidedWith((Entity) (Object) this))
            info.setReturnValue(false);
    }

    @ModifyVariable(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getMovementEmission()Lnet/minecraft/world/entity/Entity$MovementEmission;"), ordinal = 1)
    private Vec3 onStepLiving(Vec3 orig, MoverType type, Vec3 pos) {
        if (((Object) this) instanceof LivingEntity living) {
            this.runecraftoryMoveTracker += orig.lengthSqr();
            // Get the next step value independent of current move dist
            float pre = living.moveDist;
            living.moveDist = 0;
            float next = this.nextStep();
            living.moveDist = pre;

            if (this.runecraftoryMoveTracker > 0.5 * 0.5) {
                this.runecraftoryMoveTracker = 0;
                MixinUtils.triggerArmorStepEffect(living);
            }
        }
        return orig;
    }

    @Shadow
    abstract float nextStep();
}
