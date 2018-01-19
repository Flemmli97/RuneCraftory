package com.flemmli97.runecraftory.client.render.monsters;

import com.flemmli97.runecraftory.client.models.monsters.ModelWooly;
import com.flemmli97.runecraftory.client.render.RenderMobBase;
import com.flemmli97.runecraftory.common.entity.starter.EntityWooly;
import com.flemmli97.runecraftory.common.lib.RFReference;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderWooly<T extends EntityWooly> extends RenderMobBase<T>{

	private ResourceLocation tex = new ResourceLocation(RFReference.MODID, "textures/entity/monsters/wooly.png");
	public RenderWooly(RenderManager renderManager) {
		super(renderManager, new ModelWooly());
	}

	@Override
	protected ResourceLocation getEntityTexture(T entity) {
		return tex;
	}

}
