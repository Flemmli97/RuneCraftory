package io.github.flemmli97.runecraftory.mixin;

import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.mixinhelper.RotationFromMatrix;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Matrix4f.class)
public abstract class Matrix4fMixin implements RotationFromMatrix {

    @Shadow
    private float m00;
    @Shadow
    private float m01;
    @Shadow
    private float m10;
    @Shadow
    private float m11;
    @Shadow
    private float m20;
    @Shadow
    private float m21;
    @Shadow
    private float m22;

    @Override
    public Vector3f getMatrixRotationZYX() {
        if (this.m00 == 0 && this.m10 == 0) {
            float y = Mth.HALF_PI;
            float z = 0;
            float x = (float) Mth.atan2(this.m01, this.m11);
            return new Vector3f(x, y, z);
        }
        float y = (float) Mth.atan2(-this.m20, Mth.sqrt(this.m00 * this.m00 + this.m10 * this.m10));
        float z = (float) Mth.atan2(this.m10, this.m00);
        float x = (float) Mth.atan2(this.m21, this.m22);
        return new Vector3f(x, y, z);
    }
}
