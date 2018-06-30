package com.flemmli97.runecraftory.client.render.projectile;

import com.flemmli97.runecraftory.client.models.ModelButterfly;
import com.flemmli97.runecraftory.common.entity.monster.projectile.EntityButterfly;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderButterfly<T extends EntityButterfly> extends Render<T>
{
    protected ModelBase mainModel;
	private ResourceLocation tex = new ResourceLocation(LibReference.MODID, "textures/entity/projectile/butterfly.png");


    public RenderButterfly(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
        this.mainModel = new ModelButterfly();
    }

    public ModelBase getMainModel()
    {
        return this.mainModel;
    }

    /**
     * Renders the desired {@code T} type Entity.
     */
    @Override
    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
    	this.doRenderModel(entity, x, y, z, entityYaw, partialTicks);
    }
    
	public void doRenderModel(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
		
    	this.bindEntityTexture(entity);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.translate((float)x, (float)y, (float)z);
        GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((-entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks)+180, 0.0F, 0.0F, 0.0F);
        GlStateManager.enableRescaleNormal();

        this.mainModel.render(entity, 0, 0, 0, 0, 0, 0.0325F);
        if (this.renderOutlines)
        {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.disableRescaleNormal();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();    	
    }
	
	@Override
	protected ResourceLocation getEntityTexture(T entity) {
		return tex;
	}
}