package com.flemmli97.runecraftory.client.render.monsters;

import com.flemmli97.runecraftory.client.models.monsters.ModelBigMuck;
import com.flemmli97.runecraftory.client.render.RenderMobBase;
import com.flemmli97.runecraftory.common.entity.monster.EntityBigMuck;
import com.flemmli97.runecraftory.common.lib.LibReference;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderBigMuck<T extends EntityBigMuck> extends RenderMobBase<T>{

	private ResourceLocation tex = new ResourceLocation(LibReference.MODID, "textures/entity/monsters/big_muck.png");
	public RenderBigMuck(RenderManager renderManager) {
		super(renderManager, new ModelBigMuck());
	}

	@Override
	protected ResourceLocation getEntityTexture(T entity) {
		return this.tex;
	}

}
