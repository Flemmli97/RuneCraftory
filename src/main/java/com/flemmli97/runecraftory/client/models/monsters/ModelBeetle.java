package com.flemmli97.runecraftory.client.models.monsters;// Made with Blockbench 3.5.2
// Exported for Minecraft version 1.12
// Paste this class into your mod and generate all required imports


import com.flemmli97.runecraftory.common.entity.monster.EntityBeetle;
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

public class ModelBeetle extends ModelBase implements IResetModel {
	public ModelRendererPlus body;
	public ModelRendererPlus head;
	public ModelRendererPlus chin;
	public ModelRendererPlus headLeft;
	public ModelRendererPlus headRight;
	public ModelRendererPlus headUp;
	public ModelRendererPlus hornFront;
	public ModelRendererPlus hornFrontTip2;
	public ModelRendererPlus hornBackBase;
	public ModelRendererPlus hornBack;
	public ModelRendererPlus hornBackTip;
	public ModelRendererPlus elytronLeft;
	public ModelRendererPlus elytronRight;
	public ModelRendererPlus wingLeft;
	public ModelRendererPlus wingLeftSide;
	public ModelRendererPlus wingLeftBack;
	public ModelRendererPlus wingRight;
	public ModelRendererPlus wingRightSide;
	public ModelRendererPlus wingRightBack;
	public ModelRendererPlus legLeft;
	public ModelRendererPlus legLeftMiddle;
	public ModelRendererPlus legLeftDown;
	public ModelRendererPlus legRight;
	public ModelRendererPlus legRightMiddle;
	public ModelRendererPlus legRightDown;
	public ModelRendererPlus armLeft;
	public ModelRendererPlus armLeftMiddle;
	public ModelRendererPlus leftClaw;
	public ModelRendererPlus armRight;
	public ModelRendererPlus armRightMiddle;
	public ModelRendererPlus rightClaw;
	public ModelRendererPlus armLeft2;
	public ModelRendererPlus armLeft2Middle;
	public ModelRendererPlus leftClaw2;
	public ModelRendererPlus armRight2;
	public ModelRendererPlus armRight2Middle;
	public ModelRendererPlus rightClaw2;

	public BlockBenchAnimations animations;

	public ModelBeetle() {
		this.textureWidth = 128;
		this.textureHeight = 62;

		this.body = new ModelRendererPlus(this);
		this.body.setDefaultRotPoint(0.0F, 7.0F, 3.0F);
		this.setRotationAngle(this.body, -0.6981F, 0.0F, 0.0F);
		this.body.cubeList.add(new ModelBox(this.body, 0, 0, -5.0F, -5.0F, -7.0F, 10, 7, 15, 0.0F, false));
		this.body.cubeList.add(new ModelBox(this.body, 0, 22, -4.5F, 2.0F, -7.0F, 9, 2, 15, 0.0F, false));

		this.head = new ModelRendererPlus(this);
		this.head.setDefaultRotPoint(0.0F, 1.0F, -7.0F);
		this.body.addChild(this.head);
		this.setRotationAngle(this.head, 0.5236F, 0.0F, 0.0F);
		this.head.cubeList.add(new ModelBox(this.head, 110, 12, -2.0F, -3.1F, -5.0F, 4, 6, 5, 0.0F, false));
		this.head.cubeList.add(new ModelBox(this.head, 96, 24, -2.0F, -2.1F, -6.0F, 4, 5, 1, 0.0F, false));
		this.head.cubeList.add(new ModelBox(this.head, 106, 24, -1.0F, -1.1F, -7.0F, 2, 4, 1, 0.0F, false));

		this.chin = new ModelRendererPlus(this);
		this.chin.setDefaultRotPoint(0.0F, 2.9F, -6.0F);
		this.head.addChild(this.chin);
		this.setRotationAngle(this.chin, -0.3491F, 0.0F, 0.0F);
		this.chin.cubeList.add(new ModelBox(this.chin, 112, 23, -0.5F, 0.0F, -1.0F, 1, 1, 1, 0.0F, false));

		this.headLeft = new ModelRendererPlus(this);
		this.headLeft.setDefaultRotPoint(4.0F, 0.0F, 0.0F);
		this.head.addChild(this.headLeft);
		this.setRotationAngle(this.headLeft, 0.0F, 0.4363F, 0.0F);
		this.headLeft.cubeList.add(new ModelBox(this.headLeft, 106, 0, -2.0F, -3.1F, -5.0F, 2, 6, 6, 0.0F, false));
		this.headLeft.cubeList.add(new ModelBox(this.headLeft, 96, 12, -3.0F, -4.1F, -2.0F, 2, 7, 5, 0.0F, false));

		this.headRight = new ModelRendererPlus(this);
		this.headRight.setDefaultRotPoint(-4.0F, 0.0F, 0.0F);
		this.head.addChild(this.headRight);
		this.setRotationAngle(this.headRight, 0.0F, -0.4363F, 0.0F);
		this.headRight.cubeList.add(new ModelBox(this.headRight, 106, 0, 0.0F, -3.1F, -5.0F, 2, 6, 6, 0.0F, true));
		this.headRight.cubeList.add(new ModelBox(this.headRight, 96, 12, 1.0F, -4.1F, -2.0F, 2, 7, 5, 0.0F, true));

		this.headUp = new ModelRendererPlus(this);
		this.headUp.setDefaultRotPoint(0.0F, -3.1F, -4.0F);
		this.head.addChild(this.headUp);
		this.setRotationAngle(this.headUp, -0.6109F, 0.0F, 0.0F);
		this.headUp.cubeList.add(new ModelBox(this.headUp, 84, 30, -1.5F, -2.0F, -1.0F, 3, 4, 2, 0.0F, false));
		this.headUp.cubeList.add(new ModelBox(this.headUp, 94, 30, -2.0F, -4.0F, 0.0F, 4, 4, 2, 0.0F, false));
		this.headUp.cubeList.add(new ModelBox(this.headUp, 84, 36, -2.5F, -5.0F, 1.0F, 5, 5, 2, 0.0F, false));
		this.headUp.cubeList.add(new ModelBox(this.headUp, 98, 36, -2.5F, -6.0F, 3.0F, 5, 6, 3, 0.0F, false));

		this.hornFront = new ModelRendererPlus(this);
		this.hornFront.setDefaultRotPoint(0.0F, 1.0F, -0.75F);
		this.headUp.addChild(this.hornFront);
		this.setRotationAngle(this.hornFront, -0.0873F, 0.0F, 0.0F);
		this.hornFront.cubeList.add(new ModelBox(this.hornFront, 112, 25, -0.5F, -2.0F, -3.25F, 1, 2, 3, 0.0F, false));

		this.hornFrontTip2 = new ModelRendererPlus(this);
		this.hornFrontTip2.setDefaultRotPoint(0.0F, 0.0F, -3.0F);
		this.hornFront.addChild(this.hornFrontTip2);
		this.setRotationAngle(this.hornFrontTip2, -0.2618F, 0.0F, 0.0F);
		this.hornFrontTip2.cubeList.add(new ModelBox(this.hornFrontTip2, 112, 30, -0.5F, -2.0F, -4.25F, 1, 1, 4, 0.0F, false));

		this.hornBackBase = new ModelRendererPlus(this);
		this.hornBackBase.setDefaultRotPoint(0.0F, -4.5F, -5.5F);
		this.body.addChild(this.hornBackBase);
		this.setRotationAngle(this.hornBackBase, 0.5236F, 0.0F, 0.0F);
		this.hornBackBase.cubeList.add(new ModelBox(this.hornBackBase, 82, 18, -1.0F, -4.5F, -1.5F, 2, 4, 2, 0.0F, false));

		this.hornBack = new ModelRendererPlus(this);
		this.hornBack.setDefaultRotPoint(0.0F, -4.25F, 0.5F);
		this.hornBackBase.addChild(this.hornBack);
		this.setRotationAngle(this.hornBack, 0.5236F, 0.0F, 0.0F);
		this.hornBack.cubeList.add(new ModelBox(this.hornBack, 90, 18, -0.5F, -5.25F, -2.0F, 1, 5, 2, 0.0F, false));

		this.hornBackTip = new ModelRendererPlus(this);
		this.hornBackTip.setDefaultRotPoint(0.0F, -5.0F, -1.0F);
		this.hornBack.addChild(this.hornBackTip);
		this.setRotationAngle(this.hornBackTip, 0.6109F, 0.0F, 0.0F);
		this.hornBackTip.cubeList.add(new ModelBox(this.hornBackTip, 84, 24, -0.5F, -5.25F, -1.0F, 1, 5, 1, 0.0F, false));

		this.elytronLeft = new ModelRendererPlus(this);
		this.elytronLeft.setDefaultRotPoint(3.0F, -5.0F, -7.0F);
		this.body.addChild(this.elytronLeft);
		this.elytronLeft.cubeList.add(new ModelBox(this.elytronLeft, 0, 40, -3.0F, -1.0F, 0.0F, 5, 1, 15, 0.0F, false));
		this.elytronLeft.cubeList.add(new ModelBox(this.elytronLeft, 50, 0, 2.0F, 0.0F, 0.0F, 1, 7, 15, 0.0F, false));
		this.elytronLeft.cubeList.add(new ModelBox(this.elytronLeft, 48, 22, -3.0F, -0.01F, 15.0F, 5, 7, 1, 0.0F, false));

		this.elytronRight = new ModelRendererPlus(this);
		this.elytronRight.setDefaultRotPoint(-3.0F, -5.0F, -7.0F);
		this.body.addChild(this.elytronRight);
		this.elytronRight.cubeList.add(new ModelBox(this.elytronRight, 0, 40, -2.0F, -1.0F, 0.0F, 5, 1, 15, 0.0F, true));
		this.elytronRight.cubeList.add(new ModelBox(this.elytronRight, 50, 0, -3.0F, 0.0F, 0.0F, 1, 7, 15, 0.0F, true));
		this.elytronRight.cubeList.add(new ModelBox(this.elytronRight, 48, 22, -2.0F, -0.01F, 15.0F, 5, 7, 1, 0.0F, true));

		this.wingLeft = new ModelRendererPlus(this);
		this.wingLeft.setDefaultRotPoint(1.0F, -5.0F, -4.0F);
		this.body.addChild(this.wingLeft);
		this.wingLeft.cubeList.add(new ModelBox(this.wingLeft, 48, 30, -1.0F, 0.0F, -1.0F, 5, 0, 13, 0.0F, false));

		this.wingLeftSide = new ModelRendererPlus(this);
		this.wingLeftSide.setDefaultRotPoint(4.0F, 0.0F, 5.0F);
		this.wingLeft.addChild(this.wingLeftSide);
		this.wingLeftSide.cubeList.add(new ModelBox(this.wingLeftSide, 40, 43, 0.0F, 0.0F, -6.0F, 0, 7, 13, 0.0F, true));

		this.wingLeftBack = new ModelRendererPlus(this);
		this.wingLeftBack.setDefaultRotPoint(0.0F, 3.0F, 7.0F);
		this.wingLeftSide.addChild(this.wingLeftBack);
		this.wingLeftBack.cubeList.add(new ModelBox(this.wingLeftBack, 67, 43, -5.0F, -3.0F, 0.0F, 5, 7, 0, 0.0F, false));

		this.wingRight = new ModelRendererPlus(this);
		this.wingRight.setDefaultRotPoint(-1.0F, -5.0F, -4.0F);
		this.body.addChild(this.wingRight);
		this.wingRight.cubeList.add(new ModelBox(this.wingRight, 48, 30, -4.0F, 0.0F, -1.0F, 5, 0, 13, 0.0F, true));

		this.wingRightSide = new ModelRendererPlus(this);
		this.wingRightSide.setDefaultRotPoint(-4.0F, 0.0F, 5.0F);
		this.wingRight.addChild(this.wingRightSide);
		this.wingRightSide.cubeList.add(new ModelBox(this.wingRightSide, 40, 43, 0.0F, 0.0F, -6.0F, 0, 7, 13, 0.0F, false));

		this.wingRightBack = new ModelRendererPlus(this);
		this.wingRightBack.setDefaultRotPoint(0.0F, 3.0F, 7.0F);
		this.wingRightSide.addChild(this.wingRightBack);
		this.wingRightBack.cubeList.add(new ModelBox(this.wingRightBack, 67, 43, 0.0F, -3.0F, 0.0F, 5, 7, 0, 0.0F, true));

		this.legLeft = new ModelRendererPlus(this);
		this.legLeft.setDefaultRotPoint(2.0F, 4.0F, 5.0F);
		this.body.addChild(this.legLeft);
		this.setRotationAngle(this.legLeft, 0.2618F, 0.0F, 0.0F);
		this.legLeft.cubeList.add(new ModelBox(this.legLeft, 82, 0, 0.0F, 0.0F, 0.0F, 2, 5, 2, 0.0F, false));

		this.legLeftMiddle = new ModelRendererPlus(this);
		this.legLeftMiddle.setDefaultRotPoint(1.0F, 5.0F, 0.0F);
		this.legLeft.addChild(this.legLeftMiddle);
		this.setRotationAngle(this.legLeftMiddle, 1.1345F, 0.0F, 0.0F);
		this.legLeftMiddle.cubeList.add(new ModelBox(this.legLeftMiddle, 82, 7, -0.5F, 0.0F, 0.0F, 1, 4, 2, 0.0F, false));

		this.legLeftDown = new ModelRendererPlus(this);
		this.legLeftDown.setDefaultRotPoint(0.0F, 4.0F, 1.5F);
		this.legLeftMiddle.addChild(this.legLeftDown);
		this.setRotationAngle(this.legLeftDown, -0.7854F, 0.0F, 0.0F);
		this.legLeftDown.cubeList.add(new ModelBox(this.legLeftDown, 82, 13, -0.5F, 0.0F, -1.0F, 1, 4, 1, 0.0F, false));

		this.legRight = new ModelRendererPlus(this);
		this.legRight.setDefaultRotPoint(-2.0F, 4.0F, 5.0F);
		this.body.addChild(this.legRight);
		this.setRotationAngle(this.legRight, 0.2618F, 0.0F, 0.0F);
		this.legRight.cubeList.add(new ModelBox(this.legRight, 82, 0, -2.0F, 0.0F, 0.0F, 2, 5, 2, 0.0F, true));

		this.legRightMiddle = new ModelRendererPlus(this);
		this.legRightMiddle.setDefaultRotPoint(-1.0F, 5.0F, 0.0F);
		this.legRight.addChild(this.legRightMiddle);
		this.setRotationAngle(this.legRightMiddle, 1.1345F, 0.0F, 0.0F);
		this.legRightMiddle.cubeList.add(new ModelBox(this.legRightMiddle, 82, 7, -0.5F, 0.0F, 0.0F, 1, 4, 2, 0.0F, true));

		this.legRightDown = new ModelRendererPlus(this);
		this.legRightDown.setDefaultRotPoint(0.0F, 4.0F, 1.5F);
		this.legRightMiddle.addChild(this.legRightDown);
		this.setRotationAngle(this.legRightDown, -0.7854F, 0.0F, 0.0F);
		this.legRightDown.cubeList.add(new ModelBox(this.legRightDown, 82, 13, -0.5F, 0.0F, -1.0F, 1, 4, 1, 0.0F, true));

		this.armLeft = new ModelRendererPlus(this);
		this.armLeft.setDefaultRotPoint(2.0F, 4.0F, -6.0F);
		this.body.addChild(this.armLeft);
		this.setRotationAngle(this.armLeft, 0.0F, 0.0F, -0.9599F);
		this.armLeft.cubeList.add(new ModelBox(this.armLeft, 90, 0, 0.0F, 0.0F, 0.0F, 1, 5, 2, 0.0F, false));

		this.armLeftMiddle = new ModelRendererPlus(this);
		this.armLeftMiddle.setDefaultRotPoint(0.5F, 5.0F, 0.5F);
		this.armLeft.addChild(this.armLeftMiddle);
		this.setRotationAngle(this.armLeftMiddle, 0.0873F, 0.0F, 0.7854F);
		this.armLeftMiddle.cubeList.add(new ModelBox(this.armLeftMiddle, 90, 7, -0.5F, 0.0F, 0.0F, 1, 2, 1, 0.0F, false));
		this.armLeftMiddle.cubeList.add(new ModelBox(this.armLeftMiddle, 96, 0, -0.5F, 2.0F, 0.5F, 1, 3, 0, 0.0F, false));
		this.armLeftMiddle.cubeList.add(new ModelBox(this.armLeftMiddle, 96, 3, 0.0F, 2.0F, 0.0F, 0, 3, 1, 0.0F, false));

		this.leftClaw = new ModelRendererPlus(this);
		this.leftClaw.setDefaultRotPoint(0.0F, 4.75F, 0.5F);
		this.armLeftMiddle.addChild(this.leftClaw);
		this.setRotationAngle(this.leftClaw, 0.0F, 0.0F, 0.4363F);
		this.leftClaw.cubeList.add(new ModelBox(this.leftClaw, 94, 7, -0.5F, 0.25F, -0.5F, 1, 1, 1, 0.0F, false));

		this.armRight = new ModelRendererPlus(this);
		this.armRight.setDefaultRotPoint(-2.0F, 4.0F, -6.0F);
		this.body.addChild(this.armRight);
		this.setRotationAngle(this.armRight, 0.0F, 0.0F, 0.9599F);
		this.armRight.cubeList.add(new ModelBox(this.armRight, 90, 0, -1.0F, 0.0F, 0.0F, 1, 5, 2, 0.0F, true));

		this.armRightMiddle = new ModelRendererPlus(this);
		this.armRightMiddle.setDefaultRotPoint(-0.5F, 5.0F, 0.5F);
		this.armRight.addChild(this.armRightMiddle);
		this.setRotationAngle(this.armRightMiddle, 0.0873F, 0.0F, -0.7854F);
		this.armRightMiddle.cubeList.add(new ModelBox(this.armRightMiddle, 90, 7, -0.5F, 0.0F, 0.0F, 1, 2, 1, 0.0F, true));
		this.armRightMiddle.cubeList.add(new ModelBox(this.armRightMiddle, 96, 0, -0.5F, 2.0F, 0.5F, 1, 3, 0, 0.0F, false));
		this.armRightMiddle.cubeList.add(new ModelBox(this.armRightMiddle, 96, 3, 0.0F, 2.0F, 0.0F, 0, 3, 1, 0.0F, false));

		this.rightClaw = new ModelRendererPlus(this);
		this.rightClaw.setDefaultRotPoint(0.0F, 4.75F, 0.5F);
		this.armRightMiddle.addChild(this.rightClaw);
		this.setRotationAngle(this.rightClaw, 0.0F, 0.0F, -0.4363F);
		this.rightClaw.cubeList.add(new ModelBox(this.rightClaw, 94, 7, -0.5F, 0.25F, -0.5F, 1, 1, 1, 0.0F, true));

		this.armLeft2 = new ModelRendererPlus(this);
		this.armLeft2.setDefaultRotPoint(3.0F, 4.0F, -1.0F);
		this.body.addChild(this.armLeft2);
		this.setRotationAngle(this.armLeft2, 0.1745F, 0.0F, -0.4363F);
		this.armLeft2.cubeList.add(new ModelBox(this.armLeft2, 98, 0, 0.0F, 0.0F, 0.0F, 1, 5, 2, 0.0F, false));

		this.armLeft2Middle = new ModelRendererPlus(this);
		this.armLeft2Middle.setDefaultRotPoint(0.5F, 5.0F, 1.5F);
		this.armLeft2.addChild(this.armLeft2Middle);
		this.setRotationAngle(this.armLeft2Middle, -0.3491F, 0.0F, 0.7854F);
		this.armLeft2Middle.cubeList.add(new ModelBox(this.armLeft2Middle, 98, 7, -0.5F, 0.0F, -1.0F, 1, 2, 1, 0.0F, false));
		this.armLeft2Middle.cubeList.add(new ModelBox(this.armLeft2Middle, 104, 0, -0.5F, 2.0F, -0.5F, 1, 3, 0, 0.0F, false));
		this.armLeft2Middle.cubeList.add(new ModelBox(this.armLeft2Middle, 104, 5, 0.0F, 2.0F, -1.0F, 0, 3, 1, 0.0F, false));

		this.leftClaw2 = new ModelRendererPlus(this);
		this.leftClaw2.setDefaultRotPoint(0.0F, 4.75F, -0.5F);
		this.armLeft2Middle.addChild(this.leftClaw2);
		this.leftClaw2.cubeList.add(new ModelBox(this.leftClaw2, 102, 7, -0.5F, 0.25F, -0.5F, 1, 1, 1, 0.0F, false));

		this.armRight2 = new ModelRendererPlus(this);
		this.armRight2.setDefaultRotPoint(-3.0F, 4.0F, -1.0F);
		this.body.addChild(this.armRight2);
		this.setRotationAngle(this.armRight2, 0.1745F, 0.0F, 0.4363F);
		this.armRight2.cubeList.add(new ModelBox(this.armRight2, 98, 0, -1.0F, 0.0F, 0.0F, 1, 5, 2, 0.0F, true));

		this.armRight2Middle = new ModelRendererPlus(this);
		this.armRight2Middle.setDefaultRotPoint(-0.5F, 5.0F, 1.5F);
		this.armRight2.addChild(this.armRight2Middle);
		this.setRotationAngle(this.armRight2Middle, -0.3491F, 0.0F, -0.7854F);
		this.armRight2Middle.cubeList.add(new ModelBox(this.armRight2Middle, 98, 7, -0.5F, 0.0F, -1.0F, 1, 2, 1, 0.0F, true));
		this.armRight2Middle.cubeList.add(new ModelBox(this.armRight2Middle, 104, 0, -0.5F, 2.0F, -0.5F, 1, 3, 0, 0.0F, false));
		this.armRight2Middle.cubeList.add(new ModelBox(this.armRight2Middle, 104, 5, 0.0F, 2.0F, -1.0F, 0, 3, 1, 0.0F, false));

		this.rightClaw2 = new ModelRendererPlus(this);
		this.rightClaw2.setDefaultRotPoint(0.0F, 4.75F, -0.5F);
		this.armRight2Middle.addChild(this.rightClaw2);
		this.rightClaw2.cubeList.add(new ModelBox(this.rightClaw2, 102, 7, -0.5F, 0.25F, -0.5F, 1, 1, 1, 0.0F, false));

		this.animations = new BlockBenchAnimations(this, new ResourceLocation(LibReference.MODID, "models/entity/animation/beetle.json"));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		this.body.render(f5);
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
								  float headPitch, float scaleFactor, Entity entity) {
		if (!(entity instanceof EntityBeetle))
			return;
		this.resetModel();
		this.head.rotateAngleY += netHeadYaw * 0.008726646F;
		this.head.rotateAngleX += headPitch * 0.008726646F;
		this.armRight.rotateAngleZ += MathHelper.cos(ageInTicks * 0.04F) * 0.07F + 0.05F;
		this.armLeft.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.04F) * 0.07F + 0.05F;
		this.armRight.rotateAngleX += MathHelper.sin(ageInTicks * 0.037F) * 0.07F;
		this.armLeft.rotateAngleX -= MathHelper.sin(ageInTicks * 0.037F) * 0.07F;
		this.armRight2.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		this.armLeft2.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		this.armRight2.rotateAngleX += MathHelper.sin(ageInTicks * 0.037F) * 0.05F;
		this.armLeft2.rotateAngleX -= MathHelper.sin(ageInTicks * 0.037F) * 0.05F;

		EntityBeetle beetle = (EntityBeetle) entity;
		float partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();

		if(!beetle.onGround)
			this.animations.doAnimation("fly", beetle.ticksExisted, partialTicks);
		else if (beetle.isMoving())
			this.animations.doAnimation("walk", beetle.ticksExisted, partialTicks);

		AnimatedAction anim = beetle.getAnimation();
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