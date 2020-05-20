package com.flemmli97.runecraftory.client.models.monsters;// Made with Blockbench 3.5.2
// Exported for Minecraft version 1.12
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelBuffamoo extends ModelBase {
	private final ModelRenderer body;
	private final ModelRenderer udder;
	private final ModelRenderer head;
	private final ModelRenderer headFront;
	private final ModelRenderer hornLeft;
	private final ModelRenderer hornRight;
	private final ModelRenderer frontLegLeftUp;
	private final ModelRenderer frontLegLeftDown;
	private final ModelRenderer frontLeftHooves;
	private final ModelRenderer frontLegRightUp;
	private final ModelRenderer frontLegRightDown;
	private final ModelRenderer frontRightHooves;
	private final ModelRenderer backLegLeftUp;
	private final ModelRenderer backLegLeftDown;
	private final ModelRenderer backLeftHooves;
	private final ModelRenderer backLegRightUp;
	private final ModelRenderer backLegRightDown;
	private final ModelRenderer backRightHooves;

	public ModelBuffamoo() {
		this.textureWidth = 64;
		this.textureHeight = 32;

		this.body = new ModelRenderer(this);
		this.body.setRotationPoint(0.0F, 4.0F, -1.0F);
		this.body.cubeList.add(new ModelBox(this.body, 4, 4, -6.0F, -4.0F, -7.0F, 12, 13, 20, 0.0F, false));

		this.udder = new ModelRenderer(this);
		this.udder.setRotationPoint(0.0F, 30.0F, 10.0F);
		this.body.addChild(this.udder);
		this.udder.cubeList.add(new ModelBox(this.udder, 52, 0, -2.0F, -21.0F, -4.0F, 4, 1, 6, 0.0F, false));

		this.head = new ModelRenderer(this);
		this.head.setRotationPoint(0.0F, 0.0F, -7.0F);
		this.body.addChild(this.head);
		this.head.cubeList.add(new ModelBox(this.head, 0, 0, -5.0F, -3.0F, -3.0F, 10, 10, 3, 0.0F, false));

		this.headFront = new ModelRenderer(this);
		this.headFront.setRotationPoint(0.0F, 0.0F, 7.0F);
		this.head.addChild(this.headFront);
		this.headFront.cubeList.add(new ModelBox(this.headFront, 0, 0, -4.0F, -2.0F, -16.0F, 8, 8, 6, 0.0F, false));

		this.hornLeft = new ModelRenderer(this);
		this.hornLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.head.addChild(this.hornLeft);
		this.hornLeft.cubeList.add(new ModelBox(this.hornLeft, 22, 0, 4.0F, -5.0F, -4.0F, 1, 3, 1, 0.0F, false));

		this.hornRight = new ModelRenderer(this);
		this.hornRight.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.head.addChild(this.hornRight);
		this.hornRight.cubeList.add(new ModelBox(this.hornRight, 22, 0, -5.0F, -5.0F, -4.0F, 1, 3, 1, 0.0F, false));

		this.frontLegLeftUp = new ModelRenderer(this);
		this.frontLegLeftUp.setRotationPoint(4.0F, 8.0F, -5.0F);
		this.body.addChild(this.frontLegLeftUp);
		this.frontLegLeftUp.cubeList.add(new ModelBox(this.frontLegLeftUp, 0, 16, -2.0F, 1.0F, -1.0F, 4, 5, 4, 0.0F, true));

		this.frontLegLeftDown = new ModelRenderer(this);
		this.frontLegLeftDown.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.frontLegLeftUp.addChild(this.frontLegLeftDown);
		this.frontLegLeftDown.cubeList.add(new ModelBox(this.frontLegLeftDown, 0, 16, -2.0F, 6.0F, -1.0F, 4, 5, 4, 0.0F, true));

		this.frontLeftHooves = new ModelRenderer(this);
		this.frontLeftHooves.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.frontLegLeftDown.addChild(this.frontLeftHooves);
		this.frontLeftHooves.cubeList.add(new ModelBox(this.frontLeftHooves, 0, 16, -2.5F, 11.0F, -1.5F, 5, 1, 5, 0.0F, true));

		this.frontLegRightUp = new ModelRenderer(this);
		this.frontLegRightUp.setRotationPoint(-4.0F, 8.0F, -5.0F);
		this.body.addChild(this.frontLegRightUp);
		this.frontLegRightUp.cubeList.add(new ModelBox(this.frontLegRightUp, 0, 16, -2.0F, 1.0F, -1.0F, 4, 5, 4, 0.0F, false));

		this.frontLegRightDown = new ModelRenderer(this);
		this.frontLegRightDown.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.frontLegRightUp.addChild(this.frontLegRightDown);
		this.frontLegRightDown.cubeList.add(new ModelBox(this.frontLegRightDown, 0, 16, -2.0F, 6.0F, -1.0F, 4, 5, 4, 0.0F, false));

		this.frontRightHooves = new ModelRenderer(this);
		this.frontRightHooves.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.frontLegRightDown.addChild(this.frontRightHooves);
		this.frontRightHooves.cubeList.add(new ModelBox(this.frontRightHooves, 0, 16, -2.5F, 11.0F, -1.5F, 5, 1, 5, 0.0F, true));

		this.backLegLeftUp = new ModelRenderer(this);
		this.backLegLeftUp.setRotationPoint(4.0F, 8.0F, 8.0F);
		this.body.addChild(this.backLegLeftUp);
		this.backLegLeftUp.cubeList.add(new ModelBox(this.backLegLeftUp, 0, 16, -2.0F, 1.0F, -1.0F, 4, 5, 4, 0.0F, true));

		this.backLegLeftDown = new ModelRenderer(this);
		this.backLegLeftDown.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.backLegLeftUp.addChild(this.backLegLeftDown);
		this.backLegLeftDown.cubeList.add(new ModelBox(this.backLegLeftDown, 0, 16, -2.0F, 6.0F, -1.0F, 4, 5, 4, 0.0F, true));

		this.backLeftHooves = new ModelRenderer(this);
		this.backLeftHooves.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.backLegLeftDown.addChild(this.backLeftHooves);
		this.backLeftHooves.cubeList.add(new ModelBox(this.backLeftHooves, 0, 16, -2.5F, 11.0F, -1.5F, 5, 1, 5, 0.0F, true));

		this.backLegRightUp = new ModelRenderer(this);
		this.backLegRightUp.setRotationPoint(-4.0F, 8.0F, 8.0F);
		this.body.addChild(this.backLegRightUp);
		this.backLegRightUp.cubeList.add(new ModelBox(this.backLegRightUp, 0, 16, -2.0F, 1.0F, -1.0F, 4, 5, 4, 0.0F, false));

		this.backLegRightDown = new ModelRenderer(this);
		this.backLegRightDown.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.backLegRightUp.addChild(this.backLegRightDown);
		this.backLegRightDown.cubeList.add(new ModelBox(this.backLegRightDown, 0, 16, -2.0F, 6.0F, -1.0F, 4, 5, 4, 0.0F, false));

		this.backRightHooves = new ModelRenderer(this);
		this.backRightHooves.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.backLegRightDown.addChild(this.backRightHooves);
		this.backRightHooves.cubeList.add(new ModelBox(this.backRightHooves, 0, 16, -2.5F, 11.0F, -1.5F, 5, 1, 5, 0.0F, true));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		this.body.render(f5);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}