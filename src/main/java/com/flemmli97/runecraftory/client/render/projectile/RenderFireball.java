package com.flemmli97.runecraftory.client.render.projectile;

import com.flemmli97.runecraftory.common.entity.magic.EntityFireBall;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderFireball<T extends EntityFireBall> extends Render<T>{

	private ResourceLocation tex = new ResourceLocation(LibReference.MODID, "textures/entity/projectile/fireball_small.png");
	public RenderFireball(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityFireBall entity) {
		return tex;
	}

}
