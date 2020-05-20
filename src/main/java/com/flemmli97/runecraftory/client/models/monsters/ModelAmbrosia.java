package com.flemmli97.runecraftory.client.models.monsters;// Made with Blockbench 3.5.2
// Exported for Minecraft version 1.12
// Paste this class into your mod and generate all required imports


import com.flemmli97.runecraftory.common.entity.monster.boss.EntityAmbrosia;
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

public class ModelAmbrosia extends ModelBase implements IResetModel {
	public ModelRendererPlus body;
	public ModelRendererPlus head;
	public ModelRendererPlus hornFront;
	public ModelRendererPlus hornLeftStick;
	public ModelRendererPlus hornLeftFlower;
	public ModelRendererPlus hornRightStick;
	public ModelRendererPlus hornRightFlower;
	public ModelRendererPlus leftUpperWing;
	public ModelRendererPlus leftLowerWing;
	public ModelRendererPlus rightUpperWing;
	public ModelRendererPlus rightLowerWing;
	public ModelRendererPlus leftArm;
	public ModelRendererPlus leftArmDown;
	public ModelRendererPlus rightArm;
	public ModelRendererPlus rightArmDown;
	public ModelRendererPlus dressUp;
	public ModelRendererPlus dressDown;
	public ModelRendererPlus leftLeg;
	public ModelRendererPlus leftLegDown;
	public ModelRendererPlus rightLeg;
	public ModelRendererPlus rightLegDown;

	public BlockBenchAnimations animations;

	public ModelAmbrosia() {
		this.textureWidth = 66;
		this.textureHeight = 65;

		this.body = new ModelRendererPlus(this);
		this.body.setDefaultRotPoint(0.0F, -5.0F, 0.0F);
		this.setRotationAngle(this.body, 0.0698F, 0.0F, 0.0F);
		this.body.cubeList.add(new ModelBox(this.body, 0, 51, -4.0F, 0.0F, -2.0F, 8, 10, 4, 0.0F, true));

		this.head = new ModelRendererPlus(this);
		this.head.setDefaultRotPoint(0.0F, 0.0F, 0.0F);
		this.body.addChild(this.head);
		this.head.cubeList.add(new ModelBox(this.head, 0, 0, -4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F, true));

		this.hornFront = new ModelRendererPlus(this);
		this.hornFront.setDefaultRotPoint(0.0F, -8.0F, -2.5F);
		this.head.addChild(this.hornFront);
		this.setRotationAngle(this.hornFront, 0.6009F, 0.0F, 0.0F);
		this.hornFront.cubeList.add(new ModelBox(this.hornFront, 0, 16, -0.5F, -5.0F, -0.5F, 1, 5, 1, 0.0F, true));

		this.hornLeftStick = new ModelRendererPlus(this);
		this.hornLeftStick.setDefaultRotPoint(1.75F, -8.0F, -0.75F);
		this.head.addChild(this.hornLeftStick);
		this.setRotationAngle(this.hornLeftStick, 0.1745F, 0.0F, -0.0524F);
		this.hornLeftStick.cubeList.add(new ModelBox(this.hornLeftStick, 4, 16, -0.5F, -3.0F, -0.5F, 1, 3, 1, 0.0F, true));

		this.hornLeftFlower = new ModelRendererPlus(this);
		this.hornLeftFlower.setDefaultRotPoint(0.0F, -3.0F, 0.0F);
		this.hornLeftStick.addChild(this.hornLeftFlower);
		this.hornLeftFlower.cubeList.add(new ModelBox(this.hornLeftFlower, 8, 16, -1.0F, -2.0F, -1.0F, 2, 2, 2, 0.0F, true));

		this.hornRightStick = new ModelRendererPlus(this);
		this.hornRightStick.setDefaultRotPoint(-1.75F, -8.0F, -0.75F);
		this.head.addChild(this.hornRightStick);
		this.setRotationAngle(this.hornRightStick, 0.1745F, 0.0F, 0.0524F);
		this.hornRightStick.cubeList.add(new ModelBox(this.hornRightStick, 4, 16, -0.5F, -3.0F, -0.5F, 1, 3, 1, 0.0F, false));

		this.hornRightFlower = new ModelRendererPlus(this);
		this.hornRightFlower.setDefaultRotPoint(0.0F, -3.0F, 0.0F);
		this.hornRightStick.addChild(this.hornRightFlower);
		this.hornRightFlower.cubeList.add(new ModelBox(this.hornRightFlower, 8, 16, -1.0F, -2.0F, -1.0F, 2, 2, 2, 0.0F, false));

		this.leftUpperWing = new ModelRendererPlus(this);
		this.leftUpperWing.setDefaultRotPoint(0.0F, 5.0F, 2.0F);
		this.body.addChild(this.leftUpperWing);
		this.setRotationAngle(this.leftUpperWing, -0.2618F, 0.0F, 0.0873F);
		this.leftUpperWing.cubeList.add(new ModelBox(this.leftUpperWing, 0, 22, 0.0F, -15.0F, 0.0F, 17, 15, 0, 0.0F, true));

		this.leftLowerWing = new ModelRendererPlus(this);
		this.leftLowerWing.setDefaultRotPoint(0.0F, 5.0F, 2.0F);
		this.body.addChild(this.leftLowerWing);
		this.setRotationAngle(this.leftLowerWing, 0.2618F, 0.0F, -0.0873F);
		this.leftLowerWing.cubeList.add(new ModelBox(this.leftLowerWing, 0, 37, 0.0F, 0.0F, 0.0F, 17, 14, 0, 0.0F, true));

		this.rightUpperWing = new ModelRendererPlus(this);
		this.rightUpperWing.setDefaultRotPoint(0.0F, 5.0F, 2.0F);
		this.body.addChild(this.rightUpperWing);
		this.setRotationAngle(this.rightUpperWing, -0.2618F, 0.0F, -0.0873F);
		this.rightUpperWing.cubeList.add(new ModelBox(this.rightUpperWing, 0, 22, -17.0F, -15.0F, 0.0F, 17, 15, 0, 0.0F, false));

		this.rightLowerWing = new ModelRendererPlus(this);
		this.rightLowerWing.setDefaultRotPoint(0.0F, 5.0F, 2.0F);
		this.body.addChild(this.rightLowerWing);
		this.setRotationAngle(this.rightLowerWing, 0.2618F, 0.0F, 0.0873F);
		this.rightLowerWing.cubeList.add(new ModelBox(this.rightLowerWing, 0, 37, -17.0F, 0.0F, 0.0F, 17, 14, 0, 0.0F, false));

		this.leftArm = new ModelRendererPlus(this);
		this.leftArm.setDefaultRotPoint(5.0F, 2.5F, 0.0F);
		this.body.addChild(this.leftArm);
		this.setRotationAngle(this.leftArm, -0.1745F, 0.0F, 0.1F);
		this.leftArm.cubeList.add(new ModelBox(this.leftArm, 33, 1, -1.0F, -2.0F, -1.5F, 3, 6, 3, 0.0F, true));

		this.leftArmDown = new ModelRendererPlus(this);
		this.leftArmDown.setDefaultRotPoint(0.5F, 4.0F, 1.5F);
		this.leftArm.addChild(this.leftArmDown);
		this.setRotationAngle(this.leftArmDown, -0.1745F, 0.0F, 0.0F);
		this.leftArmDown.cubeList.add(new ModelBox(this.leftArmDown, 33, 11, -1.5F, 0.0F, -3.0F, 3, 6, 3, 0.0F, true));

		this.rightArm = new ModelRendererPlus(this);
		this.rightArm.setDefaultRotPoint(-5.0F, 2.5F, 0.0F);
		this.body.addChild(this.rightArm);
		this.setRotationAngle(this.rightArm, -0.1745F, 0.0F, -0.1F);
		this.rightArm.cubeList.add(new ModelBox(this.rightArm, 33, 1, -2.0F, -2.0F, -2.0F, 3, 6, 3, 0.0F, false));

		this.rightArmDown = new ModelRendererPlus(this);
		this.rightArmDown.setDefaultRotPoint(-0.5F, 4.0F, 1.0F);
		this.rightArm.addChild(this.rightArmDown);
		this.setRotationAngle(this.rightArmDown, -0.1745F, 0.0F, 0.0F);
		this.rightArmDown.cubeList.add(new ModelBox(this.rightArmDown, 33, 11, -1.5F, 0.0F, -3.0F, 3, 6, 3, 0.0F, false));

		this.dressUp = new ModelRendererPlus(this);
		this.dressUp.setDefaultRotPoint(0.0F, 10.0F, 0.0F);
		this.body.addChild(this.dressUp);
		this.dressUp.cubeList.add(new ModelBox(this.dressUp, 34, 50, -5.0F, 0.0F, -3.0F, 10, 1, 6, 0.0F, true));

		this.dressDown = new ModelRendererPlus(this);
		this.dressDown.setDefaultRotPoint(0.0F, 1.0F, 0.0F);
		this.dressUp.addChild(this.dressDown);
		this.dressDown.cubeList.add(new ModelBox(this.dressDown, 24, 57, -5.5F, 0.0F, -3.5F, 11, 1, 7, 0.0F, true));

		this.leftLeg = new ModelRendererPlus(this);
		this.leftLeg.setDefaultRotPoint(1.9F, 12.0F, 0.0F);
		this.body.addChild(this.leftLeg);
		this.setRotationAngle(this.leftLeg, -0.0873F, 0.0F, 0.0F);
		this.leftLeg.cubeList.add(new ModelBox(this.leftLeg, 34, 20, -1.9F, 0.0F, -2.0F, 4, 6, 4, 0.0F, true));

		this.leftLegDown = new ModelRendererPlus(this);
		this.leftLegDown.setDefaultRotPoint(0.1F, 6.0F, -2.0F);
		this.leftLeg.addChild(this.leftLegDown);
		this.setRotationAngle(this.leftLegDown, 0.2618F, 0.0F, 0.0F);
		this.leftLegDown.cubeList.add(new ModelBox(this.leftLegDown, 50, 20, -2.0F, 0.0F, 0.0F, 4, 6, 4, 0.0F, true));

		this.rightLeg = new ModelRendererPlus(this);
		this.rightLeg.setDefaultRotPoint(-1.9F, 12.0F, 0.0F);
		this.body.addChild(this.rightLeg);
		this.setRotationAngle(this.rightLeg, -0.0873F, 0.0F, 0.0F);
		this.rightLeg.cubeList.add(new ModelBox(this.rightLeg, 34, 30, -2.1F, 0.0F, -2.0F, 4, 6, 4, 0.0F, false));

		this.rightLegDown = new ModelRendererPlus(this);
		this.rightLegDown.setDefaultRotPoint(-0.1F, 6.0F, -2.0F);
		this.rightLeg.addChild(this.rightLegDown);
		this.setRotationAngle(this.rightLegDown, 0.2618F, 0.0F, 0.0F);
		this.rightLegDown.cubeList.add(new ModelBox(this.rightLegDown, 50, 30, -2.0F, 0.0F, 0.0F, 4, 6, 4, 0.0F, true));

		this.animations = new BlockBenchAnimations(this, new ResourceLocation(LibReference.MODID, "models/entity/animation/ambrosia.json"));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		this.body.render(f5);
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
								  float headPitch, float scaleFactor, Entity entity) {
		if (!(entity instanceof EntityAmbrosia))
			return;
		this.resetModel();
		this.head.rotateAngleY = netHeadYaw * 0.017453292F;
		this.head.rotateAngleX = headPitch * 0.017453292F;

		this.leftLowerWing.rotateAngleY+= MathHelper.cos(ageInTicks * 1.2F) * MathUtils.degToRad(10)+MathUtils.degToRad(-5);
		this.rightLowerWing.rotateAngleY-=MathHelper.cos(ageInTicks * 1.2F) * MathUtils.degToRad(10)+MathUtils.degToRad(-5);
		this.leftUpperWing.rotateAngleY+=MathHelper.cos(ageInTicks * 1.2F) * MathUtils.degToRad(10)+MathUtils.degToRad(-5);
		this.rightUpperWing.rotateAngleY-=MathHelper.cos(ageInTicks * 1.2F) * MathUtils.degToRad(10)+MathUtils.degToRad(-5);
		this.rightArm.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		this.leftArm.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		this.rightArm.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
		this.leftArm.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;

		this.hornRightStick.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		this.hornLeftStick.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		this.hornRightStick.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
		this.hornLeftStick.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
		this.hornFront.rotateAngleX+= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;

		EntityAmbrosia brosia = (EntityAmbrosia) entity;
		float partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();

		if (brosia.isMoving())
			this.body.rotateAngleX = 0.6108652381980154F;

		AnimatedAction anim = brosia.getAnimation();
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