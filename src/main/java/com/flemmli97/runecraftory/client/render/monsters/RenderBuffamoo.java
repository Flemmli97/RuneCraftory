package com.flemmli97.runecraftory.client.render.monsters;

import com.flemmli97.runecraftory.client.models.monsters.buffamoo;
import com.flemmli97.runecraftory.client.render.RenderMobBase;
import com.flemmli97.runecraftory.common.entity.monster.EntityBuffamoo;
import com.flemmli97.runecraftory.common.lib.LibReference;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;


public class RenderBuffamoo<T extends EntityBuffamoo> extends RenderMobBase<T>{

	private ResourceLocation tex = new ResourceLocation(LibReference.MODID, "textures/entity/monsters/buffamoo.png");

	public RenderBuffamoo(RenderManager renderManager) {
		super(renderManager, new buffamoo());
	}

	@Override
	protected ResourceLocation getEntityTexture(T entity) {
		return this.tex;
	}
	

}
