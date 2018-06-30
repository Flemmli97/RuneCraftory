package com.flemmli97.runecraftory.client.render;

import com.flemmli97.runecraftory.common.entity.npc.EntityNPCBase;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderNPCBase<T extends EntityNPCBase> extends RenderLiving<T>
{
    private final ResourceLocation tex = new ResourceLocation("textures/entity/steve.png");
    
    public RenderNPCBase(RenderManager renderManager, ModelBase model) {
        super(renderManager, model, 0.0f);
    }
    
    protected ResourceLocation getEntityTexture(T entity) {
        return this.tex;
    }
}
