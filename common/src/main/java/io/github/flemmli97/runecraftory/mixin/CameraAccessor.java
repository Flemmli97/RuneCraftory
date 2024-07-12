package io.github.flemmli97.runecraftory.mixin;

import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Camera.class)
public interface CameraAccessor {

    @Invoker("move")
    void moveCamera(double distanceOffset, double verticalOffset, double horizontalOffset);
}
