package io.github.flemmli97.runecraftory.fabric.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.client.ClientCalls;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {

    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(T entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        ClientCalls.renderEntityShake(entity, matrixStack, partialTicks);
    }
}
