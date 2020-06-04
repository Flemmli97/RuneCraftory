package com.flemmli97.runecraftory.client.models.monsters;// Made with Blockbench 3.5.2
// Exported for Minecraft version 1.12
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class buffamoo extends ModelBase {
	private final ModelRenderer body;
	private final ModelRenderer head;
	private final ModelRenderer headFront;
	private final ModelRenderer hornLeft;
	private final ModelRenderer hornLeftTip;
	private final ModelRenderer hornRight;
	private final ModelRenderer hornRight2;
	private final ModelRenderer udder;
	private final ModelRenderer frontLegLeftUp;
	private final ModelRenderer frontLegLeftDown;
	private final ModelRenderer frontLegRightUp;
	private final ModelRenderer frontLegRightDown;
	private final ModelRenderer backLegLeftUp;
	private final ModelRenderer backLegLeftDown;
	private final ModelRenderer backLegRightUp;
	private final ModelRenderer backLegRightDown;
	private final ModelRenderer backFur;
	private final ModelRenderer tail;
	private final ModelRenderer tail2;
	private final ModelRenderer tail3;
	private final ModelRenderer tailTip;

	public buffamoo() {
		textureWidth = 121;
		textureHeight = 55;

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0F, 4.0F, -1.0F);
		body.cubeList.add(new ModelBox(body, 0, 0, -6.0F, -3.0F, -7.0F, 12, 12, 20, 0.0F, false));

		head = new ModelRenderer(this);
		head.setRotationPoint(0.0F, 0.0F, -6.0F);
		body.addChild(head);
		setRotationAngle(head, 0.1745F, 0.0F, 0.0F);
		head.cubeList.add(new ModelBox(head, 0, 32, -5.0F, -3.0F, -3.0F, 10, 10, 3, 0.0F, false));

		headFront = new ModelRenderer(this);
		headFront.setRotationPoint(0.0F, 0.0F, -3.0F);
		head.addChild(headFront);
		headFront.cubeList.add(new ModelBox(headFront, 26, 32, -4.0F, -2.0F, -8.0F, 8, 8, 8, 0.0F, false));

		hornLeft = new ModelRenderer(this);
		hornLeft.setRotationPoint(4.0F, 0.0F, -4.5F);
		head.addChild(hornLeft);
		setRotationAngle(hornLeft, 0.0F, 0.0F, -0.2618F);
		hornLeft.cubeList.add(new ModelBox(hornLeft, 0, 45, 0.0F, -2.0F, -0.5F, 4, 2, 2, 0.0F, false));

		hornLeftTip = new ModelRenderer(this);
		hornLeftTip.setRotationPoint(4.0F, 0.0F, 1.0F);
		hornLeft.addChild(hornLeftTip);
		setRotationAngle(hornLeftTip, 0.0F, 0.0F, -0.9599F);
		hornLeftTip.cubeList.add(new ModelBox(hornLeftTip, 12, 45, 0.0F, -2.0F, -1.0F, 5, 2, 1, 0.0F, false));

		hornRight = new ModelRenderer(this);
		hornRight.setRotationPoint(-4.0F, 0.0F, -4.5F);
		head.addChild(hornRight);
		setRotationAngle(hornRight, 0.0F, 0.0F, 0.2618F);
		hornRight.cubeList.add(new ModelBox(hornRight, 0, 45, -4.0F, -2.0F, -0.5F, 4, 2, 2, 0.0F, true));

		hornRight2 = new ModelRenderer(this);
		hornRight2.setRotationPoint(-4.0F, 0.0F, 0.0F);
		hornRight.addChild(hornRight2);
		setRotationAngle(hornRight2, 0.0F, 0.0F, 0.9599F);
		hornRight2.cubeList.add(new ModelBox(hornRight2, 12, 45, -5.0F, -2.0F, 0.0F, 5, 2, 1, 0.0F, true));

		udder = new ModelRenderer(this);
		udder.setRotationPoint(0.0F, 30.0F, 10.0F);
		body.addChild(udder);
		udder.cubeList.add(new ModelBox(udder, 52, 0, -2.0F, -21.0F, -4.0F, 4, 1, 6, 0.0F, false));

		frontLegLeftUp = new ModelRenderer(this);
		frontLegLeftUp.setRotationPoint(4.0F, 9.0F, -4.0F);
		body.addChild(frontLegLeftUp);
		frontLegLeftUp.cubeList.add(new ModelBox(frontLegLeftUp, 89, 0, -2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F, true));

		frontLegLeftDown = new ModelRenderer(this);
		frontLegLeftDown.setRotationPoint(0.0F, 6.0F, 0.0F);
		frontLegLeftUp.addChild(frontLegLeftDown);
		frontLegLeftDown.cubeList.add(new ModelBox(frontLegLeftDown, 89, 10, -2.0F, 0.0F, -2.0F, 4, 5, 4, 0.0F, true));

		frontLegRightUp = new ModelRenderer(this);
		frontLegRightUp.setRotationPoint(-4.0F, 9.0F, -4.0F);
		body.addChild(frontLegRightUp);
		frontLegRightUp.cubeList.add(new ModelBox(frontLegRightUp, 89, 0, -2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F, true));

		frontLegRightDown = new ModelRenderer(this);
		frontLegRightDown.setRotationPoint(0.0F, 6.0F, 0.0F);
		frontLegRightUp.addChild(frontLegRightDown);
		frontLegRightDown.cubeList.add(new ModelBox(frontLegRightDown, 89, 10, -2.0F, 0.0F, -2.0F, 4, 5, 4, 0.0F, false));

		backLegLeftUp = new ModelRenderer(this);
		backLegLeftUp.setRotationPoint(4.0F, 9.0F, 9.0F);
		body.addChild(backLegLeftUp);
		backLegLeftUp.cubeList.add(new ModelBox(backLegLeftUp, 105, 0, -2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F, true));

		backLegLeftDown = new ModelRenderer(this);
		backLegLeftDown.setRotationPoint(0.0F, 6.0F, 0.0F);
		backLegLeftUp.addChild(backLegLeftDown);
		backLegLeftDown.cubeList.add(new ModelBox(backLegLeftDown, 105, 10, -2.0F, 0.0F, -2.0F, 4, 5, 4, 0.0F, true));

		backLegRightUp = new ModelRenderer(this);
		backLegRightUp.setRotationPoint(-4.0F, 8.0F, 8.0F);
		body.addChild(backLegRightUp);
		backLegRightUp.cubeList.add(new ModelBox(backLegRightUp, 105, 0, -2.0F, 1.0F, -1.0F, 4, 6, 4, 0.0F, false));

		backLegRightDown = new ModelRenderer(this);
		backLegRightDown.setRotationPoint(0.0F, 7.0F, 1.0F);
		backLegRightUp.addChild(backLegRightDown);
		backLegRightDown.cubeList.add(new ModelBox(backLegRightDown, 105, 10, -2.0F, 0.0F, -2.0F, 4, 5, 4, 0.0F, false));

		backFur = new ModelRenderer(this);
		backFur.setRotationPoint(0.0F, 0.0F, 0.0F);
		body.addChild(backFur);
		backFur.cubeList.add(new ModelBox(backFur, 64, 19, 0.0F, -9.0F, -11.0F, 0, 9, 24, 0.0F, false));

		tail = new ModelRenderer(this);
		tail.setRotationPoint(-0.5F, -1.0F, 12.0F);
		body.addChild(tail);
		setRotationAngle(tail, 0.7854F, 0.0F, 0.0F);
		tail.cubeList.add(new ModelBox(tail, 0, 49, -0.5F, 0.0F, 0.0F, 1, 4, 1, 0.0F, false));

		tail2 = new ModelRenderer(this);
		tail2.setRotationPoint(0.0F, 4.0F, 0.5F);
		tail.addChild(tail2);
		setRotationAngle(tail2, -0.1745F, 0.0F, 0.0F);
		tail2.cubeList.add(new ModelBox(tail2, 4, 49, -0.5F, -0.3F, -0.5F, 1, 4, 1, 0.0F, false));

		tail3 = new ModelRenderer(this);
		tail3.setRotationPoint(0.0F, 3.7F, 0.0F);
		tail2.addChild(tail3);
		setRotationAngle(tail3, -0.5236F, 0.0F, 0.0F);
		tail3.cubeList.add(new ModelBox(tail3, 8, 49, -0.5F, -0.3F, -0.5F, 1, 5, 1, 0.0F, false));

		tailTip = new ModelRenderer(this);
		tailTip.setRotationPoint(0.0F, 4.3F, 0.0F);
		tail3.addChild(tailTip);
		tailTip.cubeList.add(new ModelBox(tailTip, 12, 49, -1.0F, -0.2F, -1.0F, 2, 3, 2, 0.0F, false));
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