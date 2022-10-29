package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.client.model.AnimatedPlayerModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(EntityRenderers.class)
public class EntityRenderersMixin {

    @Inject(method = "createPlayerRenderers", at = @At("RETURN"))
    private static void initModel(EntityRendererProvider.Context context, CallbackInfoReturnable<Map<String, EntityRenderer<? extends Player>>> info) {
        ClientHandlers.animatedPlayerModel = new AnimatedPlayerModel(context.bakeLayer(AnimatedPlayerModel.LAYER_LOCATION));
    }
}
