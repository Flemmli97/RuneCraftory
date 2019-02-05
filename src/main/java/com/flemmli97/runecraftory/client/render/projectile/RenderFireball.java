package com.flemmli97.runecraftory.client.render.projectile;

import com.flemmli97.runecraftory.common.entity.magic.EntityFireBall;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.tenshilib.client.render.RenderTexture;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderFireball<T extends EntityFireBall> extends RenderTexture<T>{

	private static final ResourceLocation tex = new ResourceLocation(LibReference.MODID, "textures/entity/projectile/fireball.png");
	
	public RenderFireball(RenderManager renderManager) {
		super(renderManager, 1, 1);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityFireBall entity) {
		return tex;
	}
	
	@Override
	public int animationFrames()
	{
		return 6;
	}
}
