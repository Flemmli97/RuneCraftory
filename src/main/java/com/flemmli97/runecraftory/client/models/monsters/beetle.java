package com.flemmli97.runecraftory.client.models.monsters;// Made with Blockbench 3.5.2
// Exported for Minecraft version 1.12
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class beetle extends ModelBase {
	private final ModelRenderer body;
	private final ModelRenderer head;
	private final ModelRenderer chin;
	private final ModelRenderer headLeft;
	private final ModelRenderer headRight;
	private final ModelRenderer headUp;
	private final ModelRenderer hornFront;
	private final ModelRenderer hornFrontTip2;
	private final ModelRenderer hornBackBase;
	private final ModelRenderer hornBack;
	private final ModelRenderer hornBackTip;
	private final ModelRenderer elytronLeft;
	private final ModelRenderer elytronRight;
	private final ModelRenderer wingLeft;
	private final ModelRenderer wingLeftSide;
	private final ModelRenderer wingLeftBack;
	private final ModelRenderer wingRight;
	private final ModelRenderer wingRightSide;
	private final ModelRenderer wingRightBack;
	private final ModelRenderer legLeft;
	private final ModelRenderer legLeftMiddle;
	private final ModelRenderer legLeftDown;
	private final ModelRenderer legRight;
	private final ModelRenderer legRightMiddle;
	private final ModelRenderer legRightDown;
	private final ModelRenderer armLeft;
	private final ModelRenderer armLeftMiddle;
	private final ModelRenderer leftClaw;
	private final ModelRenderer armRight;
	private final ModelRenderer armRightMiddle;
	private final ModelRenderer rightClaw;
	private final ModelRenderer armLeft2;
	private final ModelRenderer armLeft2Middle;
	private final ModelRenderer leftClaw2;
	private final ModelRenderer armRight2;
	private final ModelRenderer armRight2Middle;
	private final ModelRenderer rightClaw2;

	public beetle() {
		textureWidth = 128;
		textureHeight = 62;

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0F, 7.0F, 3.0F);
		setRotationAngle(body, -0.6981F, 0.0F, 0.0F);
		body.cubeList.add(new ModelBox(body, 0, 0, -5.0F, -5.0F, -7.0F, 10, 7, 15, 0.0F, false));
		body.cubeList.add(new ModelBox(body, 0, 22, -4.5F, 2.0F, -7.0F, 9, 2, 15, 0.0F, false));

		head = new ModelRenderer(this);
		head.setRotationPoint(0.0F, 1.0F, -7.0F);
		body.addChild(head);
		setRotationAngle(head, 0.5236F, 0.0F, 0.0F);
		head.cubeList.add(new ModelBox(head, 110, 12, -2.0F, -3.1F, -5.0F, 4, 6, 5, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 96, 24, -2.0F, -2.1F, -6.0F, 4, 5, 1, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 106, 24, -1.0F, -1.1F, -7.0F, 2, 4, 1, 0.0F, false));

		chin = new ModelRenderer(this);
		chin.setRotationPoint(0.0F, 2.9F, -6.0F);
		head.addChild(chin);
		setRotationAngle(chin, -0.3491F, 0.0F, 0.0F);
		chin.cubeList.add(new ModelBox(chin, 112, 23, -0.5F, 0.0F, -1.0F, 1, 1, 1, 0.0F, false));

		headLeft = new ModelRenderer(this);
		headLeft.setRotationPoint(4.0F, 0.0F, 0.0F);
		head.addChild(headLeft);
		setRotationAngle(headLeft, 0.0F, 0.4363F, 0.0F);
		headLeft.cubeList.add(new ModelBox(headLeft, 106, 0, -2.0F, -3.1F, -5.0F, 2, 6, 6, 0.0F, false));
		headLeft.cubeList.add(new ModelBox(headLeft, 96, 12, -3.0F, -4.1F, -2.0F, 2, 7, 5, 0.0F, false));

		headRight = new ModelRenderer(this);
		headRight.setRotationPoint(-4.0F, 0.0F, 0.0F);
		head.addChild(headRight);

		setRotationAngle(headRight, 0.0F, -0.4363F, 0.0F);
		headRight.cubeList.add(new ModelBox(headRight, 106, 0, 0.0F, -3.1F, -5.0F, 2, 6, 6, 0.0F, true));
		headRight.cubeList.add(new ModelBox(headRight, 96, 12, 1.0F, -4.1F, -2.0F, 2, 7, 5, 0.0F, true));

		headUp = new ModelRenderer(this);
		headUp.setRotationPoint(0.0F, -3.1F, -4.0F);
		head.addChild(headUp);
		setRotationAngle(headUp, -0.6109F, 0.0F, 0.0F);
		headUp.cubeList.add(new ModelBox(headUp, 84, 30, -1.5F, -2.0F, -1.0F, 3, 4, 2, 0.0F, false));
		headUp.cubeList.add(new ModelBox(headUp, 94, 30, -2.0F, -4.0F, 0.0F, 4, 4, 2, 0.0F, false));
		headUp.cubeList.add(new ModelBox(headUp, 84, 36, -2.5F, -5.0F, 1.0F, 5, 5, 2, 0.0F, false));
		headUp.cubeList.add(new ModelBox(headUp, 98, 36, -2.5F, -6.0F, 3.0F, 5, 6, 3, 0.0F, false));

		hornFront = new ModelRenderer(this);
		hornFront.setRotationPoint(0.0F, 1.0F, -0.75F);
		headUp.addChild(hornFront);
		setRotationAngle(hornFront, -0.0873F, 0.0F, 0.0F);
		hornFront.cubeList.add(new ModelBox(hornFront, 112, 25, -0.5F, -2.0F, -3.25F, 1, 2, 3, 0.0F, false));

		hornFrontTip2 = new ModelRenderer(this);
		hornFrontTip2.setRotationPoint(0.0F, 0.0F, -3.0F);
		hornFront.addChild(hornFrontTip2);
		setRotationAngle(hornFrontTip2, -0.2618F, 0.0F, 0.0F);
		hornFrontTip2.cubeList.add(new ModelBox(hornFrontTip2, 112, 30, -0.5F, -2.0F, -4.25F, 1, 1, 4, 0.0F, false));

		hornBackBase = new ModelRenderer(this);
		hornBackBase.setRotationPoint(0.0F, -4.5F, -5.5F);
		body.addChild(hornBackBase);
		setRotationAngle(hornBackBase, 0.5236F, 0.0F, 0.0F);
		hornBackBase.cubeList.add(new ModelBox(hornBackBase, 82, 18, -1.0F, -4.5F, -1.5F, 2, 4, 2, 0.0F, false));

		hornBack = new ModelRenderer(this);
		hornBack.setRotationPoint(0.0F, -4.25F, 0.5F);
		hornBackBase.addChild(hornBack);
		setRotationAngle(hornBack, 0.5236F, 0.0F, 0.0F);
		hornBack.cubeList.add(new ModelBox(hornBack, 90, 18, -0.5F, -5.25F, -2.0F, 1, 5, 2, 0.0F, false));

		hornBackTip = new ModelRenderer(this);
		hornBackTip.setRotationPoint(0.0F, -5.0F, -1.0F);
		hornBack.addChild(hornBackTip);
		setRotationAngle(hornBackTip, 0.6109F, 0.0F, 0.0F);
		hornBackTip.cubeList.add(new ModelBox(hornBackTip, 84, 24, -0.5F, -5.25F, -1.0F, 1, 5, 1, 0.0F, false));

		elytronLeft = new ModelRenderer(this);
		elytronLeft.setRotationPoint(3.0F, -5.0F, -7.0F);
		body.addChild(elytronLeft);
		elytronLeft.cubeList.add(new ModelBox(elytronLeft, 0, 40, -3.0F, -1.0F, 0.0F, 5, 1, 15, 0.0F, false));
		elytronLeft.cubeList.add(new ModelBox(elytronLeft, 50, 0, 2.0F, 0.0F, 0.0F, 1, 7, 15, 0.0F, false));
		elytronLeft.cubeList.add(new ModelBox(elytronLeft, 48, 22, -3.0F, -0.01F, 15.0F, 5, 7, 1, 0.0F, false));

		elytronRight = new ModelRenderer(this);
		elytronRight.setRotationPoint(-3.0F, -5.0F, -7.0F);
		body.addChild(elytronRight);
		elytronRight.cubeList.add(new ModelBox(elytronRight, 0, 40, -2.0F, -1.0F, 0.0F, 5, 1, 15, 0.0F, true));
		elytronRight.cubeList.add(new ModelBox(elytronRight, 50, 0, -3.0F, 0.0F, 0.0F, 1, 7, 15, 0.0F, true));
		elytronRight.cubeList.add(new ModelBox(elytronRight, 48, 22, -2.0F, -0.01F, 15.0F, 5, 7, 1, 0.0F, true));

		wingLeft = new ModelRenderer(this);
		wingLeft.setRotationPoint(1.0F, -5.0F, -4.0F);
		body.addChild(wingLeft);
		wingLeft.cubeList.add(new ModelBox(wingLeft, 48, 30, -1.0F, 0.0F, -1.0F, 5, 0, 13, 0.0F, false));

		wingLeftSide = new ModelRenderer(this);
		wingLeftSide.setRotationPoint(4.0F, 0.0F, 5.0F);
		wingLeft.addChild(wingLeftSide);
		wingLeftSide.cubeList.add(new ModelBox(wingLeftSide, 40, 43, 0.0F, 0.0F, -6.0F, 0, 7, 13, 0.0F, true));

		wingLeftBack = new ModelRenderer(this);
		wingLeftBack.setRotationPoint(0.0F, 3.0F, 7.0F);
		wingLeftSide.addChild(wingLeftBack);
		wingLeftBack.cubeList.add(new ModelBox(wingLeftBack, 67, 43, -5.0F, -3.0F, 0.0F, 5, 7, 0, 0.0F, false));

		wingRight = new ModelRenderer(this);
		wingRight.setRotationPoint(-1.0F, -5.0F, -4.0F);
		body.addChild(wingRight);
		wingRight.cubeList.add(new ModelBox(wingRight, 48, 30, -4.0F, 0.0F, -1.0F, 5, 0, 13, 0.0F, true));

		wingRightSide = new ModelRenderer(this);
		wingRightSide.setRotationPoint(-4.0F, 0.0F, 5.0F);
		wingRight.addChild(wingRightSide);
		wingRightSide.cubeList.add(new ModelBox(wingRightSide, 40, 43, 0.0F, 0.0F, -6.0F, 0, 7, 13, 0.0F, false));

		wingRightBack = new ModelRenderer(this);
		wingRightBack.setRotationPoint(0.0F, 3.0F, 7.0F);
		wingRightSide.addChild(wingRightBack);
		wingRightBack.cubeList.add(new ModelBox(wingRightBack, 67, 43, 0.0F, -3.0F, 0.0F, 5, 7, 0, 0.0F, true));

		legLeft = new ModelRenderer(this);
		legLeft.setRotationPoint(2.0F, 4.0F, 5.0F);
		body.addChild(legLeft);
		setRotationAngle(legLeft, 0.2618F, 0.0F, 0.0F);
		legLeft.cubeList.add(new ModelBox(legLeft, 82, 0, 0.0F, 0.0F, 0.0F, 2, 5, 2, 0.0F, false));

		legLeftMiddle = new ModelRenderer(this);
		legLeftMiddle.setRotationPoint(1.0F, 5.0F, 0.0F);
		legLeft.addChild(legLeftMiddle);
		setRotationAngle(legLeftMiddle, 1.1345F, 0.0F, 0.0F);
		legLeftMiddle.cubeList.add(new ModelBox(legLeftMiddle, 82, 7, -0.5F, 0.0F, 0.0F, 1, 4, 2, 0.0F, false));

		legLeftDown = new ModelRenderer(this);
		legLeftDown.setRotationPoint(0.0F, 4.0F, 1.5F);
		legLeftMiddle.addChild(legLeftDown);
		setRotationAngle(legLeftDown, -0.7854F, 0.0F, 0.0F);
		legLeftDown.cubeList.add(new ModelBox(legLeftDown, 82, 13, -0.5F, 0.0F, -1.0F, 1, 4, 1, 0.0F, false));

		legRight = new ModelRenderer(this);
		legRight.setRotationPoint(-2.0F, 4.0F, 5.0F);
		body.addChild(legRight);
		setRotationAngle(legRight, 0.2618F, 0.0F, 0.0F);
		legRight.cubeList.add(new ModelBox(legRight, 82, 0, -2.0F, 0.0F, 0.0F, 2, 5, 2, 0.0F, true));

		legRightMiddle = new ModelRenderer(this);
		legRightMiddle.setRotationPoint(-1.0F, 5.0F, 0.0F);
		legRight.addChild(legRightMiddle);
		setRotationAngle(legRightMiddle, 1.1345F, 0.0F, 0.0F);
		legRightMiddle.cubeList.add(new ModelBox(legRightMiddle, 82, 7, -0.5F, 0.0F, 0.0F, 1, 4, 2, 0.0F, true));

		legRightDown = new ModelRenderer(this);
		legRightDown.setRotationPoint(0.0F, 4.0F, 1.5F);
		legRightMiddle.addChild(legRightDown);
		setRotationAngle(legRightDown, -0.7854F, 0.0F, 0.0F);
		legRightDown.cubeList.add(new ModelBox(legRightDown, 82, 13, -0.5F, 0.0F, -1.0F, 1, 4, 1, 0.0F, true));

		armLeft = new ModelRenderer(this);
		armLeft.setRotationPoint(2.0F, 4.0F, -6.0F);
		body.addChild(armLeft);
		setRotationAngle(armLeft, 0.0F, 0.0F, -0.9599F);
		armLeft.cubeList.add(new ModelBox(armLeft, 90, 0, 0.0F, 0.0F, 0.0F, 1, 5, 2, 0.0F, false));

		armLeftMiddle = new ModelRenderer(this);
		armLeftMiddle.setRotationPoint(0.5F, 5.0F, 0.5F);
		armLeft.addChild(armLeftMiddle);
		setRotationAngle(armLeftMiddle, 0.0873F, 0.0F, 0.7854F);
		armLeftMiddle.cubeList.add(new ModelBox(armLeftMiddle, 90, 7, -0.5F, 0.0F, 0.0F, 1, 2, 1, 0.0F, false));
		armLeftMiddle.cubeList.add(new ModelBox(armLeftMiddle, 96, 0, -0.5F, 2.0F, 0.5F, 1, 3, 0, 0.0F, false));
		armLeftMiddle.cubeList.add(new ModelBox(armLeftMiddle, 96, 3, 0.0F, 2.0F, 0.0F, 0, 3, 1, 0.0F, false));

		leftClaw = new ModelRenderer(this);
		leftClaw.setRotationPoint(0.0F, 4.75F, 0.5F);
		armLeftMiddle.addChild(leftClaw);
		setRotationAngle(leftClaw, 0.0F, 0.0F, 0.4363F);
		leftClaw.cubeList.add(new ModelBox(leftClaw, 94, 7, -0.5F, 0.25F, -0.5F, 1, 1, 1, 0.0F, false));

		armRight = new ModelRenderer(this);
		armRight.setRotationPoint(-2.0F, 4.0F, -6.0F);
		body.addChild(armRight);
		setRotationAngle(armRight, 0.0F, 0.0F, 0.9599F);
		armRight.cubeList.add(new ModelBox(armRight, 90, 0, -1.0F, 0.0F, 0.0F, 1, 5, 2, 0.0F, true));

		armRightMiddle = new ModelRenderer(this);
		armRightMiddle.setRotationPoint(-0.5F, 5.0F, 0.5F);
		armRight.addChild(armRightMiddle);
		setRotationAngle(armRightMiddle, 0.0873F, 0.0F, -0.7854F);
		armRightMiddle.cubeList.add(new ModelBox(armRightMiddle, 90, 7, -0.5F, 0.0F, 0.0F, 1, 2, 1, 0.0F, true));
		armRightMiddle.cubeList.add(new ModelBox(armRightMiddle, 96, 0, -0.5F, 2.0F, 0.5F, 1, 3, 0, 0.0F, false));
		armRightMiddle.cubeList.add(new ModelBox(armRightMiddle, 96, 3, 0.0F, 2.0F, 0.0F, 0, 3, 1, 0.0F, false));

		rightClaw = new ModelRenderer(this);
		rightClaw.setRotationPoint(0.0F, 4.75F, 0.5F);
		armRightMiddle.addChild(rightClaw);
		setRotationAngle(rightClaw, 0.0F, 0.0F, -0.4363F);
		rightClaw.cubeList.add(new ModelBox(rightClaw, 94, 7, -0.5F, 0.25F, -0.5F, 1, 1, 1, 0.0F, true));

		armLeft2 = new ModelRenderer(this);
		armLeft2.setRotationPoint(3.0F, 4.0F, -1.0F);
		body.addChild(armLeft2);
		setRotationAngle(armLeft2, 0.1745F, 0.0F, -0.4363F);
		armLeft2.cubeList.add(new ModelBox(armLeft2, 98, 0, 0.0F, 0.0F, 0.0F, 1, 5, 2, 0.0F, false));

		armLeft2Middle = new ModelRenderer(this);
		armLeft2Middle.setRotationPoint(0.5F, 5.0F, 1.5F);
		armLeft2.addChild(armLeft2Middle);
		setRotationAngle(armLeft2Middle, -0.3491F, 0.0F, 0.7854F);
		armLeft2Middle.cubeList.add(new ModelBox(armLeft2Middle, 98, 7, -0.5F, 0.0F, -1.0F, 1, 2, 1, 0.0F, false));
		armLeft2Middle.cubeList.add(new ModelBox(armLeft2Middle, 104, 0, -0.5F, 2.0F, -0.5F, 1, 3, 0, 0.0F, false));
		armLeft2Middle.cubeList.add(new ModelBox(armLeft2Middle, 104, 5, 0.0F, 2.0F, -1.0F, 0, 3, 1, 0.0F, false));

		leftClaw2 = new ModelRenderer(this);
		leftClaw2.setRotationPoint(0.0F, 4.75F, -0.5F);
		armLeft2Middle.addChild(leftClaw2);
		leftClaw2.cubeList.add(new ModelBox(leftClaw2, 102, 7, -0.5F, 0.25F, -0.5F, 1, 1, 1, 0.0F, false));

		armRight2 = new ModelRenderer(this);
		armRight2.setRotationPoint(-3.0F, 4.0F, -1.0F);
		body.addChild(armRight2);
		setRotationAngle(armRight2, 0.1745F, 0.0F, 0.4363F);
		armRight2.cubeList.add(new ModelBox(armRight2, 98, 0, -1.0F, 0.0F, 0.0F, 1, 5, 2, 0.0F, true));

		armRight2Middle = new ModelRenderer(this);
		armRight2Middle.setRotationPoint(-0.5F, 5.0F, 1.5F);
		armRight2.addChild(armRight2Middle);
		setRotationAngle(armRight2Middle, -0.3491F, 0.0F, -0.7854F);
		armRight2Middle.cubeList.add(new ModelBox(armRight2Middle, 98, 7, -0.5F, 0.0F, -1.0F, 1, 2, 1, 0.0F, true));
		armRight2Middle.cubeList.add(new ModelBox(armRight2Middle, 104, 0, -0.5F, 2.0F, -0.5F, 1, 3, 0, 0.0F, false));
		armRight2Middle.cubeList.add(new ModelBox(armRight2Middle, 104, 5, 0.0F, 2.0F, -1.0F, 0, 3, 1, 0.0F, false));

		rightClaw2 = new ModelRenderer(this);
		rightClaw2.setRotationPoint(0.0F, 4.75F, -0.5F);
		armRight2Middle.addChild(rightClaw2);
		rightClaw2.cubeList.add(new ModelBox(rightClaw2, 102, 7, -0.5F, 0.25F, -0.5F, 1, 1, 1, 0.0F, false));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		body.render(f5);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}