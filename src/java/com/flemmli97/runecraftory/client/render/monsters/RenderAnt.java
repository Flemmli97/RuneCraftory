package com.flemmli97.runecraftory.client.render.monsters;

import com.flemmli97.runecraftory.client.models.monsters.ModelAnt;
import com.flemmli97.runecraftory.client.render.RenderMobBase;
import com.flemmli97.runecraftory.common.entity.starter.EntityAnt;
import com.flemmli97.runecraftory.common.lib.RFReference;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderAnt<T extends EntityAnt> extends RenderMobBase<T>{

	private ResourceLocation tex = new ResourceLocation(RFReference.MODID, "textures/entity/monsters/ant.png");

	public RenderAnt(RenderManager renderManager) {
		super(renderManager, new ModelAnt());
	}

	@Override
	protected ResourceLocation getEntityTexture(T entity) {
		return tex;
	}
	

}
