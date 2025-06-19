package io.github.flemmli97.runecraftory.fabric.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.flemmli97.runecraftory.client.ClientCalls;
import net.minecraft.client.Camera;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Camera.class)
public abstract class CameraMixin {

    @Shadow
    private float partialTickTime;

    @WrapOperation(method = "setRotation", at = @At(value = "INVOKE", target = "Lorg/joml/Quaternionf;rotationYXZ(FFF)Lorg/joml/Quaternionf;", remap = false))
    private Quaternionf cameraInject(Quaternionf instance, float angleY, float angleX, float angleZ, Operation<Quaternionf> original) {
        float[] rotations = new float[]{angleY, angleX, angleZ};
        boolean[] changed = {false};
        ClientCalls.renderShaking((Camera) (Object) this, angleY, angleX, 0, this.partialTickTime,
                f -> {
                    rotations[0] = f;
                    changed[0] = true;
                }, f -> {
                    rotations[1] = f;
                    changed[0] = true;
                }, f -> {
                    rotations[2] = f;
                    changed[0] = true;
                });
        return original.call(instance, rotations[0], rotations[1], rotations[2]);
    }
}
