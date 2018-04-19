package com.flemmli97.runecraftory.client.render.monsters;

import com.flemmli97.runecraftory.client.models.monsters.ModelBeetle;
import com.flemmli97.runecraftory.client.render.RenderMobBase;
import com.flemmli97.runecraftory.common.entity.monster.EntityBeetle;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderBeetle<T extends EntityBeetle> extends RenderMobBase<T>{

	private ResourceLocation tex = new ResourceLocation(LibReference.MODID, "textures/entity/monsters/beetle.png");
	public RenderBeetle(RenderManager renderManager) {
		super(renderManager, new ModelBeetle());
	}

	@Override
	protected ResourceLocation getEntityTexture(T entity) {
		return tex;
	}

}
