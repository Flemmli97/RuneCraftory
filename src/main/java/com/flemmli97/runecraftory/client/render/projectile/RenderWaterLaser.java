package com.flemmli97.runecraftory.client.render.projectile;

import org.apache.commons.lang3.tuple.Pair;

import com.flemmli97.runecraftory.common.entity.magic.EntityWaterLaser;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.tenshilib.client.render.RenderBeam;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderWaterLaser extends RenderBeam<EntityWaterLaser>{

	private static final ResourceLocation tex = new ResourceLocation(LibReference.MODID, "textures/entity/projectile/water_laser.png");

	public RenderWaterLaser(RenderManager renderManager) {
		super(renderManager, 0.25f);
	}

	@Override
	public Pair<ResourceLocation, Float> startTexture(EntityWaterLaser entity) {
		return null;
	}

	@Override
	public Pair<ResourceLocation, Float> endTexture(EntityWaterLaser entity) {
		return null;
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityWaterLaser entity) {
		return tex;
	}

	@Override
	public int animationFrames(BeamPart part)
	{
		return 8;
	}
}
