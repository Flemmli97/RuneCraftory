package io.github.flemmli97.runecraftory.common.utils;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class MathsHelper {

    public static float[] YXRotFrom(Vec3 direction) {
        return new float[]{YRotFrom(direction.x(), direction.z()), XRotFrom(direction.x(), direction.y(), direction.z())};
    }

    public static float[] YXRotFrom(double x, double y, double z) {
        return new float[]{YRotFrom(x, z), XRotFrom(x, y, z)};
    }

    public static float XRotFrom(Vec3 direction) {
        return XRotFrom(direction.x(), direction.y(), direction.z());
    }

    public static float XRotFrom(double x, double y, double z) {
        double horDist = Math.sqrt(x * x + z * z);
        return (float) (-(Mth.atan2(y, horDist) * Mth.RAD_TO_DEG));
    }

    public static float YRotFrom(Vec3 direction) {
        return YRotFrom(direction.x(), direction.z());
    }

    public static float YRotFrom(double x, double z) {
        return (float) (Mth.atan2(z, x) * Mth.RAD_TO_DEG) - 90.0f;
    }
}
