package io.github.flemmli97.runecraftory.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.mixinhelper.ClientMixinUtils;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity> {

    @Inject(method = "setupRotations", at = @At(value = "RETURN"))
    public void render(T entity, PoseStack poseStack, float bob, float yBodyRot, float partialTick, float scale, CallbackInfo info) {
        ClientMixinUtils.translateSleepingEntity(entity, poseStack, this.getFlipDegrees(entity));
    }

    @Shadow
    protected abstract float getFlipDegrees(T livingEntity);
}
