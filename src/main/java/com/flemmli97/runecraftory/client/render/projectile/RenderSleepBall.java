package com.flemmli97.runecraftory.client.render.projectile;

import com.flemmli97.runecraftory.common.entity.monster.projectile.EntityAmbrosiaSleep;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderSleepBall<T extends EntityAmbrosiaSleep> extends Render<T> {

    public RenderSleepBall(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
    }

    @Override
    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        
    }
    
    @Override
    protected ResourceLocation getEntityTexture(T entity) {
        return null;
    }
}
