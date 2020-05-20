package com.flemmli97.runecraftory.client.render.monsters;

import com.flemmli97.runecraftory.client.models.monsters.ModelCluckadoodle;
import com.flemmli97.runecraftory.client.render.RenderMobBase;
import com.flemmli97.runecraftory.common.entity.monster.EntityCluckadoodle;
import com.flemmli97.runecraftory.common.lib.LibReference;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderCluckadoodle<T extends EntityCluckadoodle> extends RenderMobBase<T>{

	private ResourceLocation tex = new ResourceLocation(LibReference.MODID, "textures/entity/monsters/cluckadoodle.png");

	public RenderCluckadoodle(RenderManager renderManager) {
		super(renderManager, new ModelCluckadoodle());
	}

	@Override
	protected ResourceLocation getEntityTexture(T entity) {
		return this.tex;
	}
	

}
