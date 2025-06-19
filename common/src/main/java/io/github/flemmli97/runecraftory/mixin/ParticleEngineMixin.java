package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.client.particles.CustomParticleRenderTypes;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleEngine.class)
public abstract class ParticleEngineMixin {

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;depthMask(Z)V"))
    private void customTypes(LightTexture lightTexture, Camera camera, float partialTick, CallbackInfo info) {
        CustomParticleRenderTypes.endBatch();
    }
}
