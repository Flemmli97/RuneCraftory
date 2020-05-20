package com.flemmli97.runecraftory.client.render.monsters;

import com.flemmli97.runecraftory.client.models.monsters.ModelAnt;
import com.flemmli97.runecraftory.client.render.RenderMobBase;
import com.flemmli97.runecraftory.common.entity.monster.EntityAnt;
import com.flemmli97.runecraftory.common.lib.LibReference;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderAnt<T extends EntityAnt> extends RenderMobBase<T>{

	private ResourceLocation tex = new ResourceLocation(LibReference.MODID, "textures/entity/monsters/ant.png");

	public RenderAnt(RenderManager renderManager) {
		super(renderManager, new ModelAnt());
	}

	@Override
	protected ResourceLocation getEntityTexture(T entity) {
		return this.tex;
	}

	@Override
	protected void preRenderCallback(T entitylivingbaseIn, float partialTickTime) {
		GlStateManager.scale(0.7f, 0.7f, 0.7f);
	}
}
