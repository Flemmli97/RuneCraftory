package io.github.flemmli97.runecraftory.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.common.attachment.EntityData;
import io.github.flemmli97.runecraftory.mixinhelper.ClientMixinUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity> {

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;getBob(Lnet/minecraft/world/entity/LivingEntity;F)F"))
    public void render(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo info) {
        ClientMixinUtils.translateSleepingEntity(entity, poseStack, partialTicks);
    }

    @ModifyVariable(method = "setupRotations", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/entity/LivingEntity;getPose()Lnet/minecraft/world/entity/Pose;"))
    private Pose pose(Pose original, T entityLiving) {
        if (EntityData.getSleepState(entityLiving) == EntityData.SleepState.VANILLA)
            return Pose.SLEEPING;
        return original;
    }

    @ModifyVariable(method = "setupRotations", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/entity/LivingEntity;getBedOrientation()Lnet/minecraft/core/Direction;"))
    private Direction bedDir(Direction original, T entityLiving) {
        if (EntityData.getSleepState(entityLiving) == EntityData.SleepState.VANILLA)
            return null;
        return original;
    }
}
