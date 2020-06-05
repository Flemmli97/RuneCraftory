package com.flemmli97.runecraftory.client.models.monsters;// Made with Blockbench 3.5.2
// Exported for Minecraft version 1.12
// Paste this class into your mod and generate all required imports


import com.flemmli97.runecraftory.common.entity.monster.EntityWooly;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.tenshilib.client.model.BlockBenchAnimations;
import com.flemmli97.tenshilib.client.model.IResetModel;
import com.flemmli97.tenshilib.client.model.ModelRendererPlus;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import com.flemmli97.tenshilib.common.javahelper.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class ModelWooly extends ModelBase implements IResetModel {
	public ModelRendererPlus bodyCenter;
	public ModelRendererPlus body;
	public ModelRendererPlus bodyUp;
	public ModelRendererPlus neck;
	public ModelRendererPlus head;
	public ModelRendererPlus earLeft;
	public ModelRendererPlus earRight;
	public ModelRendererPlus armLeftBase;
	public ModelRendererPlus armLeftUp;
	public ModelRendererPlus armLeftDown;
	public ModelRendererPlus armRightBase;
	public ModelRendererPlus armRightUp;
	public ModelRendererPlus armRightDown;
	public ModelRendererPlus feetLeftBase;
	public ModelRendererPlus feetLeft;
	public ModelRendererPlus feetRightBase;
	public ModelRendererPlus feetRight;
	public ModelRendererPlus tail;

	public BlockBenchAnimations animations;

	public ModelWooly() {
		this.textureWidth = 64;
		this.textureHeight = 62;

		this.bodyCenter = new ModelRendererPlus(this);
		this.bodyCenter.setDefaultRotPoint(0.0F, 17.75F, 0.0F);

		this.body = new ModelRendererPlus(this);
		this.body.setDefaultRotPoint(0.0F, 0.0F, 0.0F);
		this.bodyCenter.addChild(this.body);
		this.body.cubeList.add(new ModelBox(this.body, 0, 31, -3.5F, -7.0F, -4.5F, 7, 13, 9, 0.0F, true));
		this.body.cubeList.add(new ModelBox(this.body, 32, 30, -4.5F, -7.0F, -3.5F, 1, 13, 7, 0.0F, true));
		this.body.cubeList.add(new ModelBox(this.body, 32, 30, 3.5F, -7.0F, -3.5F, 1, 13, 7, 0.0F, false));

		this.bodyUp = new ModelRendererPlus(this);
		this.bodyUp.setDefaultRotPoint(0.0F, -8.0F, 0.0F);
		this.body.addChild(this.bodyUp);
		this.bodyUp.cubeList.add(new ModelBox(this.bodyUp, 32, 14, -2.5F, -1.0F, -3.5F, 5, 2, 7, 0.0F, true));
		this.bodyUp.cubeList.add(new ModelBox(this.bodyUp, 28, 23, 2.5F, -1.0F, -2.5F, 1, 2, 5, 0.0F, false));
		this.bodyUp.cubeList.add(new ModelBox(this.bodyUp, 28, 23, -3.5F, -1.0F, -2.5F, 1, 2, 5, 0.0F, true));

		this.neck = new ModelRendererPlus(this);
		this.neck.setDefaultRotPoint(0.0F, -1.5F, 0.0F);
		this.bodyUp.addChild(this.neck);
		this.neck.cubeList.add(new ModelBox(this.neck, 0, 24, -2.5F, -1.0F, -2.5F, 5, 2, 5, 0.0F, true));

		this.head = new ModelRendererPlus(this);
		this.head.setDefaultRotPoint(0.0F, -1.0F, 0.0F);
		this.neck.addChild(this.head);
		this.head.cubeList.add(new ModelBox(this.head, 0, 0, -3.5F, -7.0F, -4.5F, 7, 7, 9, 0.0F, true));
		this.head.cubeList.add(new ModelBox(this.head, 32, 0, -4.5F, -7.0F, -3.0F, 1, 7, 7, 0.0F, true));
		this.head.cubeList.add(new ModelBox(this.head, 32, 0, 3.5F, -7.0F, -3.5F, 1, 7, 7, 0.0F, false));
		this.head.cubeList.add(new ModelBox(this.head, 0, 16, -3.5F, -8.0F, -3.5F, 7, 1, 7, 0.0F, true));

		this.earLeft = new ModelRendererPlus(this);
		this.earLeft.setDefaultRotPoint(4.0F, -4.0F, 0.0F);
		this.head.addChild(this.earLeft);
		this.setRotationAngle(this.earLeft, 0.0F, 0.0F, -0.3491F);
		this.earLeft.cubeList.add(new ModelBox(this.earLeft, 48, 0, 0.0F, -0.5F, -1.0F, 5, 1, 2, 0.0F, true));

		this.earRight = new ModelRendererPlus(this);
		this.earRight.setDefaultRotPoint(-3.5F, -4.0F, 0.0F);
		this.head.addChild(this.earRight);
		this.setRotationAngle(this.earRight, 0.0F, 0.0F, 0.3491F);
		this.earRight.cubeList.add(new ModelBox(this.earRight, 48, 0, -5.0F, -0.5F, -1.0F, 5, 1, 2, 0.0F, false));

		this.armLeftBase = new ModelRendererPlus(this);
		this.armLeftBase.setDefaultRotPoint(3.75F, -3.0F, 0.0F);
		this.body.addChild(this.armLeftBase);
		this.setRotationAngle(this.armLeftBase, 0.1745F, 0.0F, 0.0F);
		this.armLeftBase.cubeList.add(new ModelBox(this.armLeftBase, 34, 50, 0.25F, -1.0F, -1.0F, 2, 2, 2, 0.0F, false));

		this.armLeftUp = new ModelRendererPlus(this);
		this.armLeftUp.setDefaultRotPoint(1.0F, 0.0F, -0.5F);
		this.armLeftBase.addChild(this.armLeftUp);
		this.setRotationAngle(this.armLeftUp, 0.1745F, 0.1745F, 0.0F);
		this.armLeftUp.cubeList.add(new ModelBox(this.armLeftUp, 54, 23, -0.25F, -0.5F, -4.0F, 1, 1, 4, 0.0F, false));

		this.armLeftDown = new ModelRendererPlus(this);
		this.armLeftDown.setDefaultRotPoint(0.75F, 0.0F, -4.0F);
		this.armLeftUp.addChild(this.armLeftDown);
		this.setRotationAngle(this.armLeftDown, 0.4363F, 0.5236F, 0.0F);
		this.armLeftDown.cubeList.add(new ModelBox(this.armLeftDown, 54, 28, -1.0F, -0.5F, -4.0F, 1, 1, 4, 0.0F, false));

		this.armRightBase = new ModelRendererPlus(this);
		this.armRightBase.setDefaultRotPoint(-3.75F, -3.0F, 0.0F);
		this.body.addChild(this.armRightBase);
		this.setRotationAngle(this.armRightBase, 0.1745F, 0.0F, 0.0F);
		this.armRightBase.cubeList.add(new ModelBox(this.armRightBase, 34, 50, -2.25F, -1.0F, -1.0F, 2, 2, 2, 0.0F, false));

		this.armRightUp = new ModelRendererPlus(this);
		this.armRightUp.setDefaultRotPoint(-1.0F, 0.0F, -0.5F);
		this.armRightBase.addChild(this.armRightUp);
		this.setRotationAngle(this.armRightUp, 0.1745F, -0.1745F, 0.0F);
		this.armRightUp.cubeList.add(new ModelBox(this.armRightUp, 54, 23, -0.75F, -0.5F, -4.0F, 1, 1, 4, 0.0F, false));

		this.armRightDown = new ModelRendererPlus(this);
		this.armRightDown.setDefaultRotPoint(-0.75F, 0.0F, -4.0F);
		this.armRightUp.addChild(this.armRightDown);
		this.setRotationAngle(this.armRightDown, 0.4363F, -0.5236F, 0.0F);
		this.armRightDown.cubeList.add(new ModelBox(this.armRightDown, 54, 28, 0.0F, -0.5F, -4.0F, 1, 1, 4, 0.0F, true));

		this.feetLeftBase = new ModelRendererPlus(this);
		this.feetLeftBase.setDefaultRotPoint(4.0F, 4.75F, 0.0F);
		this.body.addChild(this.feetLeftBase);
		this.feetLeftBase.cubeList.add(new ModelBox(this.feetLeftBase, 42, 50, -1.5F, -5.5F, -2.5F, 3, 7, 5, 0.0F, false));

		this.feetLeft = new ModelRendererPlus(this);
		this.feetLeft.setDefaultRotPoint(0.0F, 0.5F, 0.0F);
		this.feetLeftBase.addChild(this.feetLeft);
		this.feetLeft.cubeList.add(new ModelBox(this.feetLeft, 16, 53, -1.0F, 0.0F, -7.0F, 2, 1, 7, 0.0F, false));

		this.feetRightBase = new ModelRendererPlus(this);
		this.feetRightBase.setDefaultRotPoint(-4.0F, 4.75F, 0.0F);
		this.body.addChild(this.feetRightBase);
		this.feetRightBase.cubeList.add(new ModelBox(this.feetRightBase, 42, 50, -1.5F, -5.5F, -2.5F, 3, 7, 5, 0.0F, true));

		this.feetRight = new ModelRendererPlus(this);
		this.feetRight.setDefaultRotPoint(0.0F, 0.5F, 0.0F);
		this.feetRightBase.addChild(this.feetRight);
		this.feetRight.cubeList.add(new ModelBox(this.feetRight, 16, 53, -1.0F, 0.0F, -7.0F, 2, 1, 7, 0.0F, false));

		this.tail = new ModelRendererPlus(this);
		this.tail.setDefaultRotPoint(0.0F, 3.0F, 4.0F);
		this.body.addChild(this.tail);
		this.tail.cubeList.add(new ModelBox(this.tail, 52, 3, -1.5F, -1.5F, 0.0F, 3, 3, 3, 0.0F, true));

		this.animations = new BlockBenchAnimations(this, new ResourceLocation(LibReference.MODID, "models/entity/animation/wooly.json"));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		this.bodyCenter.render(f5);
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
								  float headPitch, float scaleFactor, Entity entity) {
		if(!(entity instanceof EntityWooly))
			return;
		this.resetModel();
		EntityWooly wooly = (EntityWooly) entity;
		this.head.rotateAngleY = netHeadYaw * 0.008453292F;
		this.head.rotateAngleX = headPitch * 0.012453292F;
		this.earLeft.rotateAngleZ+=Math.abs(MathHelper.sin(ageInTicks*0.04F)*0.1735987755982989F);
		this.earRight.rotateAngleZ-=Math.abs(MathHelper.sin(ageInTicks*0.04F)*0.1735987755982989F);

		//this.armLeftUp.rotateAngleX += MathHelper.cos(limbSwing * 0.6662F) * 0.4F * limbSwingAmount ;
		//this.armRightUp.rotateAngleX += MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 0.4F * limbSwingAmount;

		this.tail.rotateAngleY+= MathHelper.cos(ageInTicks * 0.6662F) * MathUtils.degToRad(10);
		this.armRightUp.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		this.armLeftUp.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		this.armRightUp.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
		this.armLeftUp.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;

		//this.feetRight.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.0F * limbSwingAmount;
		//this.feetLeft.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.0F * limbSwingAmount;

		float partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();
		if(wooly.isMoving())
			this.animations.doAnimation("walk", wooly.ticksExisted, partialTicks);

		AnimatedAction anim = wooly.getAnimation();
		if (anim != null)
			this.animations.doAnimation(anim.getID(), anim.getTick(), partialTicks);
	}

	public void setRotationAngle(ModelRendererPlus modelRenderer, float x, float y, float z) {
		modelRenderer.setDefaultRotAngle(x, y, z);
	}

	@Override
	public void resetModel() {
		this.bodyCenter.reset();
		this.resetChild(this.bodyCenter);
	}
}