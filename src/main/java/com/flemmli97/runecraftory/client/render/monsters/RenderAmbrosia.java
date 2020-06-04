package com.flemmli97.runecraftory.client.render.monsters;

import com.flemmli97.runecraftory.client.models.monsters.ModelAmbrosia;
import com.flemmli97.runecraftory.client.render.RenderMobBase;
import com.flemmli97.runecraftory.common.entity.monster.boss.EntityAmbrosia;
import com.flemmli97.runecraftory.common.lib.LibReference;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderAmbrosia<T extends EntityAmbrosia> extends RenderMobBase<T>{

	private ResourceLocation tex = new ResourceLocation(LibReference.MODID, "textures/entity/monsters/ambrosia.png");

	public RenderAmbrosia(RenderManager renderManager) {
		super(renderManager, new ModelAmbrosia());
	}

	@Override
	protected ResourceLocation getEntityTexture(T entity) {
		return this.tex;
	}

	@Override
	protected float getDeathMaxRotation(T entityLivingBaseIn) {
		return 0;
	}
}
