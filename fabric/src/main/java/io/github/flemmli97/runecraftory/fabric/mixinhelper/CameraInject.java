package io.github.flemmli97.runecraftory.fabric.mixinhelper;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.client.ClientCalls;
import io.github.flemmli97.runecraftory.fabric.mixin.CameraAccessor;
import net.minecraft.client.Camera;

public class CameraInject {

    public static void modifyCam(Camera camera, PoseStack stack, float partialTicks) {
        CameraAccessor acc = (CameraAccessor) camera;
        ClientCalls.renderShaking(camera.getYRot(), camera.getXRot(), 0, partialTicks,
                acc::setYRot, acc::setXRot, f -> stack.mulPose(Vector3f.ZP.rotationDegrees(f)));
    }
}
