package com.flemmli97.runecraftory.client.particles;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

public class BoltParticle extends SpriteTexturedParticle {

    public final IAnimatedSprite spriteProvider;

    protected BoltParticle(ClientWorld p_i232447_1_, double x, double y, double z, IAnimatedSprite sprite, double motionX, double motionY, double motionZ, float pitch, float yaw) {
        super(p_i232447_1_, x, y, z);
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;
        this.spriteProvider = sprite;
        this.selectSpriteWithAge(this.spriteProvider);
    }

    @Override
    public IParticleRenderType getRenderType() {
        return ParticleRenderTypes.TRANSLUCENTADD;
    }

    @Override
    public void buildGeometry(IVertexBuilder p_225606_1_, ActiveRenderInfo p_225606_2_, float p_225606_3_) {
        Vector3d vector3d = p_225606_2_.getProjectedView();
        float f = (float) (MathHelper.lerp(p_225606_3_, this.prevPosX, this.posX) - vector3d.getX());
        float f1 = (float) (MathHelper.lerp(p_225606_3_, this.prevPosY, this.posY) - vector3d.getY());
        float f2 = (float) (MathHelper.lerp(p_225606_3_, this.prevPosZ, this.posZ) - vector3d.getZ());
        Quaternion quaternion;
        if (this.particleAngle == 0.0F) {
            quaternion = p_225606_2_.getRotation();
        } else {
            quaternion = new Quaternion(p_225606_2_.getRotation());
            float f3 = MathHelper.lerp(p_225606_3_, this.prevParticleAngle, this.particleAngle);
            quaternion.multiply(Vector3f.POSITIVE_Z.getRadialQuaternion(f3));
        }

        Vector3f vector3f1 = new Vector3f(-1.0F, -1.0F, 0.0F);
        vector3f1.func_214905_a(quaternion);
        Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
        float f4 = this.getScale(p_225606_3_);

        for (int i = 0; i < 4; ++i) {
            Vector3f vector3f = avector3f[i];
            vector3f.func_214905_a(quaternion);
            vector3f.mul(f4);
            vector3f.add(f, f1, f2);
        }

        float f7 = this.getMinU();
        float f8 = this.getMaxU();
        float f5 = this.getMinV();
        float f6 = this.getMaxV();
        int j = this.getBrightnessForRender(p_225606_3_);
        p_225606_1_.vertex((double) avector3f[0].getX(), (double) avector3f[0].getY(), (double) avector3f[0].getZ()).texture(f8, f6).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).light(j).endVertex();
        p_225606_1_.vertex((double) avector3f[1].getX(), (double) avector3f[1].getY(), (double) avector3f[1].getZ()).texture(f8, f5).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).light(j).endVertex();
        p_225606_1_.vertex((double) avector3f[2].getX(), (double) avector3f[2].getY(), (double) avector3f[2].getZ()).texture(f7, f5).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).light(j).endVertex();
        p_225606_1_.vertex((double) avector3f[3].getX(), (double) avector3f[3].getY(), (double) avector3f[3].getZ()).texture(f7, f6).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).light(j).endVertex();
    }

}
