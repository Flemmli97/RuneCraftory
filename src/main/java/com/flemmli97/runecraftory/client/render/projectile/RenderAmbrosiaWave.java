package com.flemmli97.runecraftory.client.render.projectile;

import com.flemmli97.runecraftory.common.entity.monster.projectile.EntityAmbrosiaWave;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.tenshilib.client.render.RenderUtils;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderAmbrosiaWave<T extends EntityAmbrosiaWave> extends Render<T> {

    private static final ResourceLocation tex = new ResourceLocation(LibReference.MODID,"textures/entity/projectile/ambrosia_wave.png");

    public RenderAmbrosiaWave(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
    }

    @Override
    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        for(float f : entity.clientWaveSizes) {
            f = f-EntityAmbrosiaWave.circleInc+EntityAmbrosiaWave.circleInc*partialTicks;
            if(f>0) {
                float alpha = (1-f*0.16f)*entity.clientAlphaMult;
                RenderUtils.renderTexture(this.renderManager, tex, x, y+0.3, z, f*2.36f+partialTicks, f*2.36f+partialTicks, 1,1,1,alpha, 0, 90);
            }
        }
    }
    
    @Override
    protected ResourceLocation getEntityTexture(T entity) {
        return tex;
    }
}
