package com.flemmli97.runecraftory.client.models.monsters;// Made with Blockbench 3.5.2
// Exported for Minecraft version 1.12
// Paste this class into your mod and generate all required imports


import com.flemmli97.runecraftory.common.entity.monster.EntityCluckadoodle;
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

public class ModelCluckadoodle extends ModelBase implements IResetModel {
	public ModelRendererPlus body;
	public ModelRendererPlus neck;
	public ModelRendererPlus head;
	public ModelRendererPlus comb;
	public ModelRendererPlus beak;
	public ModelRendererPlus backFeathersLeft;
	public ModelRendererPlus backFeathersLeftUp;
	public ModelRendererPlus backFeathersRight;
	public ModelRendererPlus backFeathersRightUp;
	public ModelRendererPlus legLeft;
	public ModelRendererPlus legRight;
	public ModelRendererPlus wingLeft;
	public ModelRendererPlus wingRight;

	public BlockBenchAnimations animations;

	public ModelCluckadoodle() {
		this.textureWidth = 66;
		this.textureHeight = 32;

		this.body = new ModelRendererPlus(this);
		this.body.setDefaultRotPoint(0.0F, 15.5F, 1.3333F);
		this.body.cubeList.add(new ModelBox(this.body, 32, 15, -3.5F, -3.5F, -5.3333F, 7, 7, 10, 0.0F, false));

		this.neck = new ModelRendererPlus(this);
		this.neck.setDefaultRotPoint(0.0F, -2.5F, -5.3333F);
		this.body.addChild(this.neck);
		this.neck.cubeList.add(new ModelBox(this.neck, 0, 16, -2.0F, 0.0F, -1.0F, 4, 3, 2, 0.0F, false));

		this.head = new ModelRendererPlus(this);
		this.head.setDefaultRotPoint(0.0F, 0.0F, 0.0F);
		this.neck.addChild(this.head);
		this.head.cubeList.add(new ModelBox(this.head, 0, 0, -2.0F, -6.0F, -2.0F, 4, 6, 4, 0.0F, false));

		this.comb = new ModelRendererPlus(this);
		this.comb.setDefaultRotPoint(0.0F, 9.0F, 4.0F);
		this.head.addChild(this.comb);
		this.comb.cubeList.add(new ModelBox(this.comb, 20, 11, 0.0F, -21.0F, -7.0F, 0, 7, 6, 0.0F, false));

		this.beak = new ModelRendererPlus(this);
		this.beak.setDefaultRotPoint(0.0F, 0.0F, 0.0F);
		this.head.addChild(this.beak);
		this.beak.cubeList.add(new ModelBox(this.beak, 12, 14, -1.0F, -4.0F, -4.0F, 2, 2, 2, 0.0F, false));
		this.beak.cubeList.add(new ModelBox(this.beak, 12, 18, -1.0F, -3.5F, -4.5F, 2, 2, 1, 0.0F, false));

		this.backFeathersLeft = new ModelRendererPlus(this);
		this.backFeathersLeft.setDefaultRotPoint(0.0F, -3.0F, 3.6667F);
		this.body.addChild(this.backFeathersLeft);
		this.setRotationAngle(this.backFeathersLeft, -0.4363F, -0.0873F, 1.1345F);
		this.backFeathersLeft.cubeList.add(new ModelBox(this.backFeathersLeft, 0, 21, 0.0F, -8.5F, 0.0F, 6, 9, 0, 0.0F, false));

		this.backFeathersLeftUp = new ModelRendererPlus(this);
		this.backFeathersLeftUp.setDefaultRotPoint(0.0F, 0.0F, 0.0F);
		this.backFeathersLeft.addChild(this.backFeathersLeftUp);
		this.setRotationAngle(this.backFeathersLeftUp, -0.1745F, 0.0F, -1.0472F);
		this.backFeathersLeftUp.cubeList.add(new ModelBox(this.backFeathersLeftUp, 12, 21, 0.0F, -10.5F, 0.0F, 6, 11, 0, 0.0F, false));

		this.backFeathersRight = new ModelRendererPlus(this);
		this.backFeathersRight.setDefaultRotPoint(0.0F, -3.0F, 3.6667F);
		this.body.addChild(this.backFeathersRight);
		this.setRotationAngle(this.backFeathersRight, -0.4363F, 0.0873F, -1.1345F);
		this.backFeathersRight.cubeList.add(new ModelBox(this.backFeathersRight, 0, 21, -6.0F, -9.5F, 0.0F, 6, 10, 0, 0.0F, true));

		this.backFeathersRightUp = new ModelRendererPlus(this);
		this.backFeathersRightUp.setDefaultRotPoint(0.0F, 0.0F, 0.0F);
		this.backFeathersRight.addChild(this.backFeathersRightUp);
		this.setRotationAngle(this.backFeathersRightUp, -0.1745F, 0.0F, 1.0472F);
		this.backFeathersRightUp.cubeList.add(new ModelBox(this.backFeathersRightUp, 12, 21, -6.0F, -10.5F, 0.0F, 6, 11, 0, 0.0F, true));

		this.legLeft = new ModelRendererPlus(this);
		this.legLeft.setDefaultRotPoint(1.5F, 3.5F, -0.8333F);
		this.body.addChild(this.legLeft);
		this.legLeft.cubeList.add(new ModelBox(this.legLeft, 26, 0, -0.5F, -1.0F, -0.5F, 1, 6, 1, 0.0F, false));
		this.legLeft.cubeList.add(new ModelBox(this.legLeft, 26, 7, -1.5F, 5.0F, -2.5F, 3, 0, 4, 0.0F, false));

		this.legRight = new ModelRendererPlus(this);
		this.legRight.setDefaultRotPoint(-1.5F, 3.5F, -0.8333F);
		this.body.addChild(this.legRight);
		this.legRight.cubeList.add(new ModelBox(this.legRight, 26, 0, -0.5F, -1.0F, -0.5F, 1, 6, 1, 0.0F, false));
		this.legRight.cubeList.add(new ModelBox(this.legRight, 26, 7, -1.5F, 5.0F, -2.5F, 3, 0, 4, 0.0F, false));

		this.wingLeft = new ModelRendererPlus(this);
		this.wingLeft.setDefaultRotPoint(3.5F, -1.5F, -3.8333F);
		this.body.addChild(this.wingLeft);
		this.setRotationAngle(this.wingLeft, 0.4363F, 0.0F, 0.0F);
		this.wingLeft.cubeList.add(new ModelBox(this.wingLeft, 40, 0, 0.0F, -2.0F, -0.5F, 1, 5, 8, 0.0F, false));

		this.wingRight = new ModelRendererPlus(this);
		this.wingRight.setDefaultRotPoint(-3.5F, -1.4397F, -3.4913F);
		this.body.addChild(this.wingRight);
		this.setRotationAngle(this.wingRight, 0.4363F, 0.0F, 0.0F);
		this.wingRight.cubeList.add(new ModelBox(this.wingRight, 40, 0, -1.0F, -2.0603F, -0.842F, 1, 5, 8, 0.0F, true));

		this.animations = new BlockBenchAnimations(this, new ResourceLocation(LibReference.MODID, "models/entity/animation/cluckadoodle.json"));

	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		this.body.render(f5);
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
								  float headPitch, float scaleFactor, Entity entity) {
		if (!(entity instanceof EntityCluckadoodle))
			return;
		this.resetModel();
		this.neck.rotateAngleY = netHeadYaw * 0.008726646F*0.5f;
		this.neck.rotateAngleX = headPitch * 0.008726646F*0.5f;
		this.head.rotateAngleY = netHeadYaw * 0.008726646F*0.5f;
		this.head.rotateAngleX = headPitch * 0.008726646F*0.5f;

		EntityCluckadoodle cluckadoodle = (EntityCluckadoodle) entity;
		float partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();

		if (cluckadoodle.isMoving())
			this.animations.doAnimation("walk", cluckadoodle.ticksExisted, partialTicks);

		AnimatedAction anim = cluckadoodle.getAnimation();
		if (anim != null)
			this.animations.doAnimation(anim.getID(), anim.getTick(), partialTicks);
	}

	public void setRotationAngle(ModelRendererPlus modelRenderer, float x, float y, float z) {
		modelRenderer.setDefaultRotAngle(x, y, z);
	}

	@Override
	public void resetModel() {
		this.body.reset();
		this.resetChild(this.body);
	}
}