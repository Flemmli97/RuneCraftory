package com.flemmli97.runecraftory.client.render.monsters;

import com.flemmli97.runecraftory.client.models.monsters.ModelChipsqueek;
import com.flemmli97.runecraftory.client.render.RenderMobBase;
import com.flemmli97.runecraftory.common.entity.monster.EntityChipsqueek;
import com.flemmli97.runecraftory.common.lib.LibReference;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;


public class RenderChipsqueek<T extends EntityChipsqueek> extends RenderMobBase<T>{

	private ResourceLocation tex = new ResourceLocation(LibReference.MODID, "textures/entity/monsters/chipsqueek.png");

	public RenderChipsqueek(RenderManager renderManager) {
		super(renderManager, new ModelChipsqueek());
	}

	@Override
	protected ResourceLocation getEntityTexture(T entity) {
		return this.tex;
	}
	

}
