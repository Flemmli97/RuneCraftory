package com.flemmli97.runecraftory.client.render;

import com.flemmli97.runecraftory.client.models.ModelGate;
import com.flemmli97.runecraftory.common.entity.EntityGate;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderGate<T extends EntityGate> extends RenderLiving<T>{

	public RenderGate(RenderManager renderManager) {
		super(renderManager, new ModelGate(), 0);
	}

	@Override
	protected ResourceLocation getEntityTexture(T entity) {
		return new ResourceLocation(LibReference.MODID, "textures/entity/gate_" + entity.elementName() + ".png");
	}

	@Override
	protected float getDeathMaxRotation(T entityLivingBaseIn) {
		return 0;
	}
}
