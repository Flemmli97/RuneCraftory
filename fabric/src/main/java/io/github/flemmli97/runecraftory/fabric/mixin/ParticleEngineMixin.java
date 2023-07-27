package io.github.flemmli97.runecraftory.fabric.mixin;

import com.google.common.collect.ImmutableList;
import io.github.flemmli97.runecraftory.client.particles.CustomParticleRenderTypes;
import io.github.flemmli97.tenshilib.client.particles.ParticleRenderTypes;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.texture.TextureManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/**
 * Searching for the cause took ages...
 * mojank hardcoding stuff again
 */
@Mixin(ParticleEngine.class)
public abstract class ParticleEngineMixin {

    @Mutable
    @Final
    @Shadow
    private static List<ParticleRenderType> RENDER_ORDER;

    @Inject(at = @At("RETURN"), method = "<init>")
    private void customTypes(ClientLevel world, TextureManager textureManager, CallbackInfo ci) {
        RENDER_ORDER = ImmutableList.<ParticleRenderType>builder().addAll(RENDER_ORDER)
                .add(CustomParticleRenderTypes.ENTITY_MODEL_TYPE)
                .build();
    }
}
