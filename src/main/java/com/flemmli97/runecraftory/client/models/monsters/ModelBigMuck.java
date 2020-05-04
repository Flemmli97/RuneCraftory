package com.flemmli97.runecraftory.client.models.monsters;// Made with Blockbench 3.5.2
// Exported for Minecraft version 1.12
// Paste this class into your mod and generate all required imports


import com.flemmli97.runecraftory.common.entity.monster.EntityBigMuck;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.tenshilib.client.model.BlockBenchAnimations;
import com.flemmli97.tenshilib.client.model.IResetModel;
import com.flemmli97.tenshilib.client.model.ModelRendererPlus;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class ModelBigMuck extends ModelBase implements IResetModel {

	public ModelRendererPlus body;
	public ModelRendererPlus mushroomCap;
	public ModelRendererPlus handLeft;
	public ModelRendererPlus handRight;

	private BlockBenchAnimations animations;

	public ModelBigMuck() {
		textureWidth = 128;
		textureHeight = 128;

		body = new ModelRendererPlus(this);
		body.setDefaultRotPoint(0.0F, 24.0F, 0.0F);
		body.cubeList.add(new ModelBox(body, 0, 0, -6.0F, -14.0F, -5.0F, 11, 14, 11, 0.0F, false));
		body.cubeList.add(new ModelBox(body, 0, 25, -5.0F, -14.0F, -6.0F, 9, 14, 1, 0.0F, false));
		body.cubeList.add(new ModelBox(body, 44, 0, 5.0F, -14.0F, -4.0F, 1, 14, 9, 0.0F, false));
		body.cubeList.add(new ModelBox(body, 44, 0, -7.0F, -14.0F, -4.0F, 1, 14, 9, 0.0F, false));
		body.cubeList.add(new ModelBox(body, 20, 25, -5.0F, -14.0F, 6.0F, 9, 14, 1, 0.0F, false));

		mushroomCap = new ModelRendererPlus(this);
		mushroomCap.setDefaultRotPoint(0.0F, 0.0F, 0.0F);
		body.addChild(mushroomCap);
		mushroomCap.cubeList.add(new ModelBox(mushroomCap, 0, 48, -10.0F, -16.0F, -9.0F, 19, 2, 19, 0.0F, false));
		mushroomCap.cubeList.add(new ModelBox(mushroomCap, 0, 69, -8.0F, -18.0F, -7.0F, 15, 2, 15, 0.0F, false));
		mushroomCap.cubeList.add(new ModelBox(mushroomCap, 0, 86, -7.0F, -21.0F, -6.0F, 13, 3, 13, 0.0F, false));
		mushroomCap.cubeList.add(new ModelBox(mushroomCap, 84, 0, -6.0F, -24.0F, -5.0F, 11, 3, 11, 0.0F, false));
		mushroomCap.cubeList.add(new ModelBox(mushroomCap, 100, 14, -4.0F, -26.0F, -3.0F, 7, 2, 7, 0.0F, false));

		handLeft = new ModelRendererPlus(this);
		handLeft.setDefaultRotPoint(6.0F, -8.5F, 0.5F);
		setRotationAngle(handLeft, 0.0F, 0.0F, 0.2618F);
		handLeft.cubeList.add(new ModelBox(handLeft, 0, 40, 0.0F, -0.5F, -3.5F, 8, 1, 7, 0.0F, true));
		body.addChild(handLeft);

		handRight = new ModelRendererPlus(this);
		handRight.setDefaultRotPoint(-7.0F, -8.5F, 0.5F);
		setRotationAngle(handRight, 0.0F, 0.0F, -0.2618F);
		handRight.cubeList.add(new ModelBox(handRight, 0, 40, -8.0F, -0.5F, -3.5F, 8, 1, 7, 0.0F, false));
		body.addChild(handRight);

		this.animations = new BlockBenchAnimations(this, new ResourceLocation(LibReference.MODID, "models/entity/animation/big_muck.animation"));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		body.render(f5);
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
								  float headPitch, float scaleFactor, Entity entity) {
		if(!(entity instanceof EntityBigMuck))
			return;
		this.resetModel();
		EntityBigMuck mushroom = (EntityBigMuck) entity;
		float partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();
		this.animations.doAnimation("iddle", mushroom.ticksExisted, partialTicks);
		if(mushroom.isMoving())
			this.animations.doAnimation("walk", mushroom.ticksExisted, partialTicks);

		AnimatedAction anim = mushroom.getAnimation();
		if(anim!=null)
			this.animations.doAnimation(anim.getID(), anim.getTick(), partialTicks);
	}

	public void setRotationAngle(ModelRendererPlus modelRenderer, float x, float y, float z) {
		modelRenderer.setDefaultRotAngle(x, y, z);
	}

	@Override
	public void resetModel() {
		body.reset();
		this.resetChild(this.body);
	}

}