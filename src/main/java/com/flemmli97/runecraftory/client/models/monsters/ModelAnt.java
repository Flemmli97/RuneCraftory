package com.flemmli97.runecraftory.client.models.monsters;// Made with Blockbench 3.5.2
// Exported for Minecraft version 1.12
// Paste this class into your mod and generate all required imports


import com.flemmli97.runecraftory.common.entity.monster.EntityAnt;
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
import net.minecraft.util.math.MathHelper;

public class ModelAnt extends ModelBase implements IResetModel {
	public ModelRendererPlus body;
	public ModelRendererPlus bodyConnector;
	public ModelRendererPlus bodyBack;
	public ModelRendererPlus head;
	public ModelRendererPlus headFront;
	public ModelRendererPlus feelerLeft;
	public ModelRendererPlus feelerRight;
	public ModelRendererPlus jawLeft;
	public ModelRendererPlus jawRight;
	public ModelRendererPlus leg1Up1;
	public ModelRendererPlus leg1Down1;
	public ModelRendererPlus leg1Up2;
	public ModelRendererPlus leg1Down2;
	public ModelRendererPlus leg1Up3;
	public ModelRendererPlus leg1Down3;
	public ModelRendererPlus leg1Up4;
	public ModelRendererPlus leg1Down4;
	public ModelRendererPlus leg1Up5;
	public ModelRendererPlus leg1Down5;
	public ModelRendererPlus leg1Up6;
	public ModelRendererPlus leg1Down6;

	public BlockBenchAnimations animations;

	public ModelAnt() {
		this.textureWidth = 64;
		this.textureHeight = 42;

		this.body = new ModelRendererPlus(this);
		this.body.setDefaultRotPoint(0.0F, 20.0F, -3.0F);
		this.body.cubeList.add(new ModelBox(this.body, 38, 14, -3.0F, -2.0F, -4.0F, 6, 4, 7, 0.0F, true));

		this.bodyConnector = new ModelRendererPlus(this);
		this.bodyConnector.setDefaultRotPoint(0.0F, 0.0F, 3.5F);
		this.body.addChild(this.bodyConnector);
		this.bodyConnector.cubeList.add(new ModelBox(this.bodyConnector, 54, 10, -2.0F, -1.0F, -0.5F, 4, 2, 1, 0.0F, true));

		this.bodyBack = new ModelRendererPlus(this);
		this.bodyBack.setDefaultRotPoint(0.0F, 0.0F, 4.0F);
		this.body.addChild(this.bodyBack);
		this.bodyBack.cubeList.add(new ModelBox(this.bodyBack, 0, 26, -5.0F, -2.5F, 0.0F, 10, 5, 11, 0.0F, true));
		this.bodyBack.cubeList.add(new ModelBox(this.bodyBack, 32, 0, -3.5F, -3.5F, 1.0F, 7, 1, 9, 0.0F, true));

		this.head = new ModelRendererPlus(this);
		this.head.setDefaultRotPoint(0.0F, 0.0F, -4.0F);
		this.body.addChild(this.head);
		this.head.cubeList.add(new ModelBox(this.head, 0, 0, -5.0F, -3.0F, -3.0F, 10, 6, 3, 0.0F, true));

		this.headFront = new ModelRendererPlus(this);
		this.headFront.setDefaultRotPoint(0.0F, 0.0F, 0.0F);
		this.head.addChild(this.headFront);
		this.headFront.cubeList.add(new ModelBox(this.headFront, 0, 9, -4.5F, -2.5F, -8.0F, 9, 5, 5, 0.0F, true));

		this.feelerLeft = new ModelRendererPlus(this);
		this.feelerLeft.setDefaultRotPoint(3.5F, -2.5F, -7.5F);
		this.headFront.addChild(this.feelerLeft);
		this.setRotationAngle(this.feelerLeft, 0.6109F, 0.0F, 0.0F);
		this.feelerLeft.cubeList.add(new ModelBox(this.feelerLeft, 26, 0, -0.5F, -5.5F, -0.5F, 1, 6, 1, 0.0F, true));

		this.feelerRight = new ModelRendererPlus(this);
		this.feelerRight.setDefaultRotPoint(-3.5F, -2.5F, -7.5F);
		this.headFront.addChild(this.feelerRight);
		this.setRotationAngle(this.feelerRight, 0.6109F, 0.0F, 0.0F);
		this.feelerRight.cubeList.add(new ModelBox(this.feelerRight, 26, 0, -0.5F, -5.5F, -0.5F, 1, 6, 1, 0.0F, false));

		this.jawLeft = new ModelRendererPlus(this);
		this.jawLeft.setDefaultRotPoint(-3.0F, 1.5F, -7.5F);
		this.headFront.addChild(this.jawLeft);
		this.setRotationAngle(this.jawLeft, 0.0F, -0.3491F, 0.0F);
		this.jawLeft.cubeList.add(new ModelBox(this.jawLeft, 38, 10, 0.0F, -0.5F, -3.5F, 2, 1, 3, 0.0F, true));

		this.jawRight = new ModelRendererPlus(this);
		this.jawRight.setDefaultRotPoint(3.0F, 1.5F, -7.5F);
		this.headFront.addChild(this.jawRight);
		this.setRotationAngle(this.jawRight, 0.0F, 0.3491F, 0.0F);
		this.jawRight.cubeList.add(new ModelBox(this.jawRight, 38, 10, -2.0F, -0.5F, -3.5F, 2, 1, 3, 0.0F, false));

		this.leg1Up1 = new ModelRendererPlus(this);
		this.leg1Up1.setDefaultRotPoint(-3.0F, 1.0F, 1.5F);
		this.body.addChild(this.leg1Up1);
		this.setRotationAngle(this.leg1Up1, 0.0F, 0.6109F, 0.2618F);
		this.leg1Up1.cubeList.add(new ModelBox(this.leg1Up1, 0, 19, -7.0F, -1.0F, -1.0F, 7, 2, 2, 0.0F, true));

		this.leg1Down1 = new ModelRendererPlus(this);
		this.leg1Down1.setDefaultRotPoint(-6.5F, 0.0F, 0.0F);
		this.leg1Up1.addChild(this.leg1Down1);
		this.setRotationAngle(this.leg1Down1, 0.0F, 0.4363F, -0.6981F);
		this.leg1Down1.cubeList.add(new ModelBox(this.leg1Down1, 0, 23, -9.0F, -1.0F, -0.5F, 9, 2, 1, 0.0F, true));

		this.leg1Up2 = new ModelRendererPlus(this);
		this.leg1Up2.setDefaultRotPoint(-3.0F, 1.0F, 0.0F);
		this.body.addChild(this.leg1Up2);
		this.setRotationAngle(this.leg1Up2, 0.0F, 0.0F, 0.2618F);
		this.leg1Up2.cubeList.add(new ModelBox(this.leg1Up2, 0, 19, -7.0F, -1.0F, -1.0F, 7, 2, 2, 0.0F, true));

		this.leg1Down2 = new ModelRendererPlus(this);
		this.leg1Down2.setDefaultRotPoint(-6.5F, 0.0F, 0.0F);
		this.leg1Up2.addChild(this.leg1Down2);
		this.setRotationAngle(this.leg1Down2, 0.0F, 0.1745F, -0.6981F);
		this.leg1Down2.cubeList.add(new ModelBox(this.leg1Down2, 0, 23, -9.0F, -1.0F, -0.5F, 9, 2, 1, 0.0F, true));

		this.leg1Up3 = new ModelRendererPlus(this);
		this.leg1Up3.setDefaultRotPoint(-3.0F, 1.0F, -1.5F);
		this.body.addChild(this.leg1Up3);
		this.setRotationAngle(this.leg1Up3, 0.0F, -0.5236F, 0.3491F);
		this.leg1Up3.cubeList.add(new ModelBox(this.leg1Up3, 0, 19, -7.0F, -1.0F, -1.0F, 7, 2, 2, 0.0F, true));

		this.leg1Down3 = new ModelRendererPlus(this);
		this.leg1Down3.setDefaultRotPoint(-6.5F, 0.0F, 0.0F);
		this.leg1Up3.addChild(this.leg1Down3);
		this.setRotationAngle(this.leg1Down3, 0.0F, -0.0873F, -0.7854F);
		this.leg1Down3.cubeList.add(new ModelBox(this.leg1Down3, 0, 23, -9.0F, -1.0F, -0.5F, 9, 2, 1, 0.0F, true));

		this.leg1Up4 = new ModelRendererPlus(this);
		this.leg1Up4.setDefaultRotPoint(3.0F, 1.0F, -1.5F);
		this.body.addChild(this.leg1Up4);
		this.setRotationAngle(this.leg1Up4, 0.0F, 0.5236F, -0.3491F);
		this.leg1Up4.cubeList.add(new ModelBox(this.leg1Up4, 0, 19, 0.0F, -1.0F, -1.0F, 7, 2, 2, 0.0F, false));

		this.leg1Down4 = new ModelRendererPlus(this);
		this.leg1Down4.setDefaultRotPoint(6.5F, 0.0F, 0.0F);
		this.leg1Up4.addChild(this.leg1Down4);
		this.setRotationAngle(this.leg1Down4, 0.0F, -0.0873F, 0.7854F);
		this.leg1Down4.cubeList.add(new ModelBox(this.leg1Down4, 0, 23, 0.0F, -1.0F, -0.5F, 9, 2, 1, 0.0F, false));

		this.leg1Up5 = new ModelRendererPlus(this);
		this.leg1Up5.setDefaultRotPoint(3.0F, 1.0F, 0.0F);
		this.body.addChild(this.leg1Up5);
		this.setRotationAngle(this.leg1Up5, 0.0F, 0.0F, -0.2618F);
		this.leg1Up5.cubeList.add(new ModelBox(this.leg1Up5, 0, 19, 0.0F, -1.0F, -1.0F, 7, 2, 2, 0.0F, false));

		this.leg1Down5 = new ModelRendererPlus(this);
		this.leg1Down5.setDefaultRotPoint(6.5F, 0.0F, 0.0F);
		this.leg1Up5.addChild(this.leg1Down5);
		this.setRotationAngle(this.leg1Down5, 0.0F, -0.1745F, 0.6981F);
		this.leg1Down5.cubeList.add(new ModelBox(this.leg1Down5, 0, 23, 0.0F, -1.0F, -0.5F, 9, 2, 1, 0.0F, false));

		this.leg1Up6 = new ModelRendererPlus(this);
		this.leg1Up6.setDefaultRotPoint(3.0F, 1.0F, 1.5F);
		this.body.addChild(this.leg1Up6);
		this.setRotationAngle(this.leg1Up6, 0.0F, -0.6109F, -0.2618F);
		this.leg1Up6.cubeList.add(new ModelBox(this.leg1Up6, 0, 19, 0.0F, -1.0F, -1.0F, 7, 2, 2, 0.0F, false));

		this.leg1Down6 = new ModelRendererPlus(this);
		this.leg1Down6.setDefaultRotPoint(6.5F, 0.0F, 0.0F);
		this.leg1Up6.addChild(this.leg1Down6);
		this.setRotationAngle(this.leg1Down6, 0.0F, -0.4363F, 0.6981F);
		this.leg1Down6.cubeList.add(new ModelBox(this.leg1Down6, 0, 23, 0.0F, -1.0F, -0.5F, 9, 2, 1, 0.0F, false));

		this.animations = new BlockBenchAnimations(this, new ResourceLocation(LibReference.MODID, "models/entity/animation/ant.json"));

	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		this.body.render(f5);
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
								  float headPitch, float scaleFactor, Entity entity) {
		if (!(entity instanceof EntityAnt))
			return;
		this.resetModel();
		this.head.rotateAngleY = netHeadYaw * 0.008726646F;
		this.head.rotateAngleX = headPitch * 0.008726646F;
		this.feelerLeft.rotateAngleZ += MathHelper.cos(ageInTicks * 0.15F) * 0.05F + 0.05F;
		this.feelerRight.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.15F) * 0.05F + 0.05F;
		this.feelerLeft.rotateAngleX += MathHelper.sin(ageInTicks * 0.1F) * 0.05F;
		this.feelerRight.rotateAngleX -= MathHelper.sin(ageInTicks * 0.1F) * 0.05F;
		EntityAnt ant = (EntityAnt) entity;
		float partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();

		if (ant.isMoving())
			this.animations.doAnimation("walk", ant.ticksExisted, partialTicks);

		AnimatedAction anim = ant.getAnimation();
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