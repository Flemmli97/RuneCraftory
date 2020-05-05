package com.flemmli97.runecraftory.client.render.monsters;

import com.flemmli97.runecraftory.client.models.monsters.ModelOrc;
import com.flemmli97.runecraftory.client.render.RenderMobBase;
import com.flemmli97.runecraftory.common.entity.monster.EntityOrc;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderOrc<T extends EntityOrc> extends RenderMobBase<T>{

	private ResourceLocation tex = new ResourceLocation(LibReference.MODID, "textures/entity/monsters/orc.png");
	public RenderOrc(RenderManager renderManager) {
		super(renderManager, new ModelOrc());
	}

	@Override
	protected ResourceLocation getEntityTexture(T entity) {
		return this.tex;
	}

}
