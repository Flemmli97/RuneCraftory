package com.flemmli97.runecraftory.client.render;

import com.flemmli97.runecraftory.client.models.ModelGate;
import com.flemmli97.runecraftory.common.entity.EntityGate;
import com.flemmli97.runecraftory.common.lib.RFNumbers;
import com.flemmli97.runecraftory.common.lib.RFReference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class RenderGate<T extends EntityGate> extends RenderLiving<T>{

	public RenderGate(RenderManager renderManager) {
		super(renderManager, new ModelGate(), 0);
	}

	@Override
	public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		if(Minecraft.getMinecraft().player.getHeldItemMainhand()!=ItemStack.EMPTY)
			this.renderLivingLabel(entity, "Level: " + entity.level() + "; Health: " + (int)(entity.getHealth()/RFNumbers.DAMAGESCALE), x, y, z, 64);
		else
			;
	}

	@Override
	protected ResourceLocation getEntityTexture(T entity) {
		return new ResourceLocation(RFReference.MODID, "textures/entity/gate_" + entity.elementName() + ".png");
	}

	@Override
	protected float getDeathMaxRotation(T entityLivingBaseIn) {
		return 0;
	}
}
