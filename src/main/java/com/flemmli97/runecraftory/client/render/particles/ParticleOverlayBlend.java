package com.flemmli97.runecraftory.client.render.particles;

import org.lwjgl.opengl.GL14;

import com.flemmli97.tenshilib.client.particles.ParticleSimpleTexture;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

/**
 * Here to figure out how to do the overlay blending.
 */
public abstract class ParticleOverlayBlend extends ParticleSimpleTexture{

    public ParticleOverlayBlend(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn);
    }
    
    public ParticleOverlayBlend(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
    }

    @Override
    public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
        GlStateManager.pushMatrix();
        GlStateManager.depthMask(false);
        
        //GlStateManager.color(246.3f/255, 148.7f/255, 148.7f/255);

        /*GlStateManager.glTexEnv(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_COLOR, white);
        GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL13.GL_COMBINE);
        GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, GL13.GL_COMBINE_RGB, GL11.GL_REPLACE);
        GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, GL13.GL_SOURCE0_RGB, GL13.GL_CONSTANT);
        GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, GL13.GL_OPERAND0_RGB, GL11.GL_SRC_COLOR);
        GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, GL13.GL_COMBINE_ALPHA, GL11.GL_REPLACE);
        GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, GL13.GL_SOURCE0_ALPHA, GL11.GL_TEXTURE);
        GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, GL13.GL_OPERAND0_ALPHA, GL11.GL_SRC_ALPHA);*/
        
        //this.setRBGColorF(246.3f/255, 148.7f/255, 148.7f/255);
        //GlStateManager.glBlendEquation(GL14.GL_FUNC_SUBTRACT);
        //GlStateManager.glBlendEquation(GL14.GL_FUNC_ADD);
        //GlStateManager.enableColorLogic();
        //GlStateManager.colorLogicOp(GL11.GL_COPY_INVERTED);
        //GL14.glBlendColor(0, 0, 0, 0);
        //GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ZERO, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
        //GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_CONSTANT_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR,GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        //GlStateManager.glBlendEquation(0x929B);
        //GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_BLEND);//glTexEnvi(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_BLEND);
        // ask for the texture to be blended/mixed/lerped with incoming color
        // specify the color2 as per the TexEnv documentation
        //GlStateManager.glTexEnv(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_COLOR, white);
        //GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL14.GL_DEPTH_TEXTURE_MODE, GL11.GL_INTENSITY);
        //GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR,
            GlStateManager.SourceFactor.DST_ALPHA, GlStateManager.DestFactor.ZERO);
        //GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR,GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        //GlStateManager.alphaFunc(GL11.GL_GREATER, 10/255f);
        //GL14.glBlendColor(0, 0, 0, 0);
        //GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR,GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);  
        
        super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
        //GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        //GlStateManager.disableColorLogic();
        //GlStateManager.disableBlend();
        //GlStateManager.glBlendEquation(GL14.GL_FUNC_ADD);
        //GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
       /* GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
        GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, GL13.GL_COMBINE_RGB, GL11.GL_MODULATE);
        GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, GL13.GL_COMBINE_ALPHA, GL11.GL_MODULATE);
        GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, GL13.GL_SOURCE0_RGB, GL11.GL_TEXTURE);
        GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, GL13.GL_SOURCE0_ALPHA, GL11.GL_TEXTURE);
        GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, GL13.GL_OPERAND0_RGB, GL11.GL_SRC_COLOR);
        GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, GL13.GL_OPERAND0_ALPHA, GL11.GL_SRC_ALPHA);*/

        //256*I/(M+1); I = lower, M = divide layer
        
        
        /**
         this.setRBGColorF(246.3f/255, 148.7f/255, 148.7f/255);
         GL14.glBlendColor(0, 0, 0, 0);
         GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_CONSTANT_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR,GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);  
         */
        GlStateManager.popMatrix();
    }
    
    private static final GLManip one = ()->{
        GL14.glBlendColor(0, 0, 0, 0);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_CONSTANT_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR,GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);  
        
    };
    
    private static final GLManip two = ()-> GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR,
        GlStateManager.SourceFactor.DST_ALPHA, GlStateManager.DestFactor.ZERO);
    
    private static interface GLManip{
        
        public void apply();
    }
}
