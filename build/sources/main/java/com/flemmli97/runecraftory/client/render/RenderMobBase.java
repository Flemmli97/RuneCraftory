package com.flemmli97.runecraftory.client.render;

import com.flemmli97.runecraftory.common.entity.EntityMobBase;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;

public abstract class RenderMobBase<T extends EntityMobBase> extends RenderLiving<T>{

	public RenderMobBase(RenderManager renderManager, ModelBase model) {
		super(renderManager, model, 0);
	}
}
