package io.github.flemmli97.runecraftory.fabric.mixin;

import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Camera.class)
public interface CameraAccessor {

    @Accessor("xRot")
    void setXRot(float xRot);

    @Accessor("yRot")
    void setYRot(float yRot);
}
