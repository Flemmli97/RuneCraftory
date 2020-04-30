package com.flemmli97.runecraftory.client.render.particles;

import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.tenshilib.client.particles.IParticleCreator;
import com.flemmli97.tenshilib.client.particles.ParticleSimpleTexture;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ParticleAmbrosiaSleep extends ParticleSimpleTexture{

    private static final ResourceLocation tex = new ResourceLocation(LibReference.MODID,"textures/particles/ambrosia_sleep.png");

    private int rows, columns,length,frame, frameOffset;

    public ParticleAmbrosiaSleep(World world, double xCoord, double yCoord, double zCoord, double xSpeed,
            double ySpeed, double zSpeed) {
        super(world, xCoord, yCoord, zCoord);
        this.motionX=xSpeed;
        this.motionY=ySpeed;
        this.motionZ=zSpeed;
        this.particleGravity=0;
        this.particleScale*=0.2-this.rand.nextFloat()*0.1;
        this.particleMaxAge = this.rand.nextInt(12)+40;
        this.frameOffset=this.rand.nextInt(9);
        this.rows=3;
        this.columns=3;
        this.length=rows*columns;
        this.uSize=1D/columns;
        this.vSize=1D/rows;
        //this.setRBGColorF(236/255f, 133/255f, 133/255f);
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();
        if(this.particleAlpha>0.1)
            this.particleAlpha-=0.015f;
        if(this.particleScale>0.01)
            this.particleScale-=this.rand.nextFloat()*0.015;
        this.frame = this.particleAge % this.length+this.frameOffset;
    }
    
    @Override
    public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
        GlStateManager.depthMask(false);
        super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
    }
    
    @Override
    public ResourceLocation texture() {
        return tex;
    }
    
    @Override
    public double uOffset() {
        return (this.frame % this.columns) * this.uSize;
    }
    
    @Override
    public double vOffset() {
        return (this.frame / this.columns) * this.vSize;
    }
    
    public static class Factory implements IParticleCreator
    {
        @Override
        public Particle createParticle(World world, double xCoord, double yCoord, double zCoord,
                double xSpeed, double ySpeed, double zSpeed, Object... modifier) {
            return new ParticleAmbrosiaSleep(world, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed);
        }
    }
}
