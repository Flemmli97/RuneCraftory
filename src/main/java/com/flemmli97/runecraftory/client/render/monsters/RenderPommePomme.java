package com.flemmli97.runecraftory.client.render.monsters;

import com.flemmli97.runecraftory.client.models.monsters.ModelPommePomme;
import com.flemmli97.runecraftory.client.render.RenderMobBase;
import com.flemmli97.runecraftory.common.entity.monster.EntityPommePomme;
import com.flemmli97.runecraftory.common.lib.LibReference;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;


public class RenderPommePomme<T extends EntityPommePomme> extends RenderMobBase<T>{

	private ResourceLocation tex = new ResourceLocation(LibReference.MODID, "textures/entity/monsters/pomme_pomme.png");

	public RenderPommePomme(RenderManager renderManager) {
		super(renderManager, new ModelPommePomme());
	}

	@Override
	protected ResourceLocation getEntityTexture(T entity) {
		return this.tex;
	}
	

}
