package io.github.flemmli97.runecraftory.client;

import io.github.flemmli97.tenshilib.client.model.PoseExtended;
import net.minecraft.client.model.geom.PartPose;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class TransformationHelper {

    /**
     * Calculates the correct PartPose to apply to a child ModelPart that is not actually a child of the given parent
     */
    public static PartPose withParent(PoseExtended parent, PartPose child) {
        Matrix4f matrix = new Matrix4f();
        matrix.translate(parent.x, parent.y, parent.z);
        boolean parentRotated = parent.xRot != 0.0F || parent.yRot != 0.0F || parent.zRot != 0.0F;
        if (parentRotated) {
            matrix.rotate(new Quaternionf().rotationZYX(parent.zRot, parent.yRot, parent.xRot));
        }
        if (parent.xScale != 1.0F || parent.yScale != 1.0F || parent.zScale != 1.0F) {
            matrix.scale(parent.xScale, parent.yScale, parent.zScale);
        }

        Vector3f pos = new Vector3f();
        matrix.transformPosition(child.x, child.y, child.z, pos);
        Vector3f rot = new Vector3f(child.xRot, child.yRot, child.zRot);
        if (parentRotated) {
            matrix.rotateZYX(rot);
            rot = matrix.getEulerAnglesZYX(new Vector3f());
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
        Matrix4f matrix = new Matrix4f();
        boolean parentRotated = parent.xRot != 0.0F || parent.yRot != 0.0F || parent.zRot != 0.0F;
        if (parentRotated) {
            matrix.rotate(new Quaternionf().rotateXYZ(parent.zRot, parent.yRot, parent.xRot));
        }
        matrix.translate(-parent.x, -parent.y, -parent.z);
        Vector3f pos = new Vector3f();
        matrix.transformPosition(child.x, child.y, child.z, pos);

        Vector3f rot = new Vector3f(child.xRot, child.yRot, child.zRot);
        if (parentRotated) {
            matrix.rotateZYX(rot);
            rot = matrix.getEulerAnglesZYX(new Vector3f());
        }
        return PartPose.offsetAndRotation(pos.x(),
                pos.y(),
                pos.z(),
                rot.x(),
                rot.y(),
                rot.z());
    }
}
