package io.github.flemmli97.runecraftory.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.common.attachment.EntityData;
import io.github.flemmli97.runecraftory.mixinhelper.ClientMixinUtils;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity> {

    @Inject(method = "setupRotations", at = @At(value = "RETURN"))
    public void render(T entity, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks, CallbackInfo info) {
        if (this.getFlipDegrees(entity) != 0)
            ClientMixinUtils.translateSleepingEntity(entity, poseStack);
    }

    @Shadow
    abstract float getFlipDegrees(T livingEntity);

    @ModifyVariable(method = "setupRotations", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/entity/LivingEntity;getBedOrientation()Lnet/minecraft/core/Direction;"))
    private Direction bedDir(Direction original, T entityLiving) {
        if (EntityData.getSleepState(entityLiving) == EntityData.SleepState.VANILLA)
            return null;
        return original;
    }
}
