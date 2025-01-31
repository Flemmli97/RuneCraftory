package io.github.flemmli97.runecraftory.client;

import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import io.github.flemmli97.runecraftory.mixinhelper.RotationFromMatrix;
import net.minecraft.client.model.geom.PartPose;

public class TransformationHelper {

    /**
     * Calculates the correct PartPose to apply to a child ModelPart that is not actually a child of the given parent
     */
    public static PartPose withParent(PartPose parent, PartPose child) {
        Matrix4f matrix4f = new Matrix4f(Quaternion.ONE);
        matrix4f.multiplyWithTranslation(parent.x, parent.y, parent.z);
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
        Vector4f pos = new Vector4f(child.x, child.y, child.z, 1);
        pos.transform(matrix4f);
        Vector3f rot;
        if (!parentRotated) {
            rot = new Vector3f(child.xRot, child.yRot, child.zRot);
        } else {
            if (child.zRot != 0)
                matrix4f.multiply(Vector3f.ZP.rotation(child.zRot));
            if (child.yRot != 0)
                matrix4f.multiply(Vector3f.YP.rotation(child.yRot));
            if (child.xRot != 0)
                matrix4f.multiply(Vector3f.XP.rotation(child.xRot));
            rot = ((RotationFromMatrix) (Object) matrix4f).runecraftory$getMatrixRotationZYX();
        }
        return PartPose.offsetAndRotation(pos.x(),
                pos.y(),
                pos.z(),
                rot.x(),
                rot.y(),
                rot.z());
    }

    /**
     * Gets relative value with the input poses that are absolute.
     * Vanilla non child poses -> Models with child element
     */
    public static PartPose withoutParent(PartPose parent, PartPose child) {
        Matrix4f matrix4f = new Matrix4f(Quaternion.ONE);
        boolean parentRotated = false;
        if (parent.xRot != 0) {
            matrix4f.multiply(Vector3f.XN.rotation(parent.xRot));
            parentRotated = true;
        }
        if (parent.yRot != 0) {
            matrix4f.multiply(Vector3f.YN.rotation(parent.yRot));
            parentRotated = true;
        }
        if (parent.zRot != 0) {
            matrix4f.multiply(Vector3f.ZN.rotation(parent.zRot));
            parentRotated = true;
        }
        matrix4f.multiplyWithTranslation(-parent.x, -parent.y, -parent.z);
        Vector4f pos = new Vector4f(child.x, child.y, child.z, 1);
        pos.transform(matrix4f);
        Vector3f rot;
        if (!parentRotated) {
            rot = new Vector3f(child.xRot, child.yRot, child.zRot);
        } else {
            if (child.zRot != 0)
                matrix4f.multiply(Vector3f.ZP.rotation(child.zRot));
            if (child.yRot != 0)
                matrix4f.multiply(Vector3f.YP.rotation(child.yRot));
            if (child.xRot != 0)
                matrix4f.multiply(Vector3f.XP.rotation(child.xRot));
            rot = ((RotationFromMatrix) (Object) matrix4f).runecraftory$getMatrixRotationZYX();
        }
        return PartPose.offsetAndRotation(pos.x(),
                pos.y(),
                pos.z(),
                rot.x(),
                rot.y(),
                rot.z());
    }
}
