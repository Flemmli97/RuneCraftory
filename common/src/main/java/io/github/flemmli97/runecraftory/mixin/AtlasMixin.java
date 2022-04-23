package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Queue;

//Make Texture warning from this mod go away
@Mixin(value = TextureAtlas.class, remap = false)
public class AtlasMixin {

    //IDK why but using unobfuscated name does not remap properly
    @Inject(method = {"m_174717_", "method_18160"}, at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V", ordinal = 1), require = 0, cancellable = true)
    private void supresssWarning(ResourceLocation res, ResourceManager manager, Queue<?> queue, CallbackInfo info) {
        if (res.getNamespace().equals(RuneCraftory.MODID)) {
            info.cancel();
        }
    }
}
