package com.flemmli97.runecraftory.client.render;

import com.flemmli97.runecraftory.api.entities.ItemStats;
import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.lib.CalculationConstants;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.init.Items;

public abstract class RenderMobBase<T extends EntityMobBase> extends RenderLiving<T>{

	public RenderMobBase(RenderManager renderManager, ModelBase model) {
		super(renderManager, model, 0);
	}

	@Override
	public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
		// TODO Auto-generated method stub
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		if(Minecraft.getMinecraft().player.getHeldItemMainhand().getItem()!=Items.AIR)
		{
			this.renderLivingLabel(entity, "Level: " + entity.level() + "; Health: " + (int)(entity.getHealth()/CalculationConstants.DAMAGESCALE+1), x, y+1, z, 64);
			this.renderLivingLabel(entity, "Att: " + entity.getAttributeMap().getAttributeInstance(ItemStats.RFATTACK).getAttributeValue(), x, y, z, 64);
		}
		else
			;
	}

	@Override
	protected void renderLivingLabel(T entityIn, String str, double x, double y, double z, int maxDistance) {
		// TODO Auto-generated method stub
		super.renderLivingLabel(entityIn, str, x, y, z, maxDistance);
	}

}
