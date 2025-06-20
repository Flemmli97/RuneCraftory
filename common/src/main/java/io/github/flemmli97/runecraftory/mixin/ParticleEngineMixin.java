package io.github.flemmli97.runecraftory.mixin;

import net.minecraft.client.particle.ParticleEngine;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ParticleEngine.class)
public abstract class ParticleEngineMixin {

//    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;depthMask(Z)V", remap = false))
//    private void customTypes(LightTexture lightTexture, Camera camera, float partialTick, CallbackInfo info) {
//        CustomParticleRenderTypes.endBatch();
//    }
}
