package io.github.flemmli97.runecraftory.client;

import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.mixinhelper.RotationFromMatrix;
import net.minecraft.client.model.geom.PartPose;

public class TransformationHelper {

    public static Vector3f localToGlobalRot(PartPose parent, PartPose child) {
        Matrix4f matrix4f = new Matrix4f(Quaternion.ONE);
        boolean parentRotated = false;
        if (parent.zRot != 0) {
            matrix4f.multiply(Vector3f.ZP.rotation(parent.zRot));
            parentRotated = true;
        }
        if (parent.yRot != 0) {
            matrix4f.multiply(Vector3f.YP.rotation(parent.yRot));
            parentRotated = true;
        }
        if (parent.xRot != 0) {
            matrix4f.multiply(Vector3f.XP.rotation(parent.xRot));
            parentRotated = true;
        }
        if (!parentRotated)
            return new Vector3f(child.xRot, child.yRot, child.zRot);

        if (child.zRot != 0)
            matrix4f.multiply(Vector3f.ZP.rotation(child.zRot));
        if (child.yRot != 0)
            matrix4f.multiply(Vector3f.YP.rotation(child.yRot));
        if (child.xRot != 0)
            matrix4f.multiply(Vector3f.XP.rotation(child.xRot));

        return ((RotationFromMatrix) (Object) matrix4f).getMatrixRotationZYX();
    }
}
