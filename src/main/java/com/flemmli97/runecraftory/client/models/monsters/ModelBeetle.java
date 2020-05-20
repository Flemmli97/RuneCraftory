package com.flemmli97.runecraftory.client.models.monsters;// Made with Blockbench 3.5.2
// Exported for Minecraft version 1.12
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelBeetle extends ModelBase {
	private final ModelRenderer body;
	private final ModelRenderer hornBackBase;
	private final ModelRenderer hornBack;
	private final ModelRenderer hornBackTip;
	private final ModelRenderer head;
	private final ModelRenderer elytronLeft;
	private final ModelRenderer elytronRight;
	private final ModelRenderer wingLeft;
	private final ModelRenderer wingRight;
	private final ModelRenderer legBaseLeft;
	private final ModelRenderer legMiddleLeft;
	private final ModelRenderer legEndLeft;
	private final ModelRenderer legBaseRight;
	private final ModelRenderer legMiddleRight;
	private final ModelRenderer legEndRight;
	private final ModelRenderer legBaseSmall;
	private final ModelRenderer legMiddleSmall;
	private final ModelRenderer legEndSmall;

	public ModelBeetle() {
        this.textureWidth = 16;
        this.textureHeight = 16;

        this.body = new ModelRenderer(this);
        this.body.setRotationPoint(0.0F, 3.5F, 1.25F);
        this.setRotationAngle(this.body, 0.5236F, 0.0F, 0.0F);
        this.body.cubeList.add(new ModelBox(this.body, 0, 0, -5.0F, -8.5F, -3.25F, 10, 17, 7, 0.0F, false));
        this.body.cubeList.add(new ModelBox(this.body, 0, 0, -6.0F, -7.5F, -3.25F, 1, 15, 6, 0.0F, false));
        this.body.cubeList.add(new ModelBox(this.body, 0, 0, 5.0F, -7.5F, -3.25F, 1, 15, 6, 0.0F, false));
        this.body.cubeList.add(new ModelBox(this.body, 0, 0, -4.0F, -9.5F, -3.25F, 8, 1, 6, 0.0F, false));
        this.body.cubeList.add(new ModelBox(this.body, 0, 0, -4.0F, 8.5F, -3.25F, 8, 1, 6, 0.0F, false));
        this.body.cubeList.add(new ModelBox(this.body, 0, 0, -4.5F, -8.5F, -5.25F, 9, 17, 2, 0.0F, false));

        this.hornBackBase = new ModelRenderer(this);
        this.hornBackBase.setRotationPoint(0.0F, -7.5F, 2.75F);
        this.body.addChild(this.hornBackBase);
        this.setRotationAngle(this.hornBackBase, -0.7854F, 0.0F, 0.0F);
        this.hornBackBase.cubeList.add(new ModelBox(this.hornBackBase, 0, 0, -1.0F, -4.5F, -1.0F, 2, 4, 2, 0.0F, false));

        this.hornBack = new ModelRenderer(this);
        this.hornBack.setRotationPoint(0.0F, -4.0F, 1.0F);
        this.hornBackBase.addChild(this.hornBack);
        this.setRotationAngle(this.hornBack, 0.4363F, 0.0F, 0.0F);
        this.hornBack.cubeList.add(new ModelBox(this.hornBack, 0, 0, -0.5F, -3.5F, -2.0F, 1, 3, 2, 0.0F, false));

        this.hornBackTip = new ModelRenderer(this);
        this.hornBackTip.setRotationPoint(0.0F, -3.0F, -1.0F);
        this.hornBack.addChild(this.hornBackTip);
        this.setRotationAngle(this.hornBackTip, 0.6109F, 0.0F, 0.0F);
        this.hornBackTip.cubeList.add(new ModelBox(this.hornBackTip, 0, 0, -0.5F, -6.5F, -1.0F, 1, 6, 1, 0.0F, false));

        this.head = new ModelRenderer(this);
        this.head.setRotationPoint(0.0F, -10.5F, -1.75F);
        this.body.addChild(this.head);
        this.head.cubeList.add(new ModelBox(this.head, 0, 0, -2.0F, -3.0F, -1.5F, 4, 4, 3, 0.0F, false));

        this.elytronLeft = new ModelRenderer(this);
        this.elytronLeft.setRotationPoint(0.0F, -7.5F, 3.75F);
        this.body.addChild(this.elytronLeft);
        this.elytronLeft.cubeList.add(new ModelBox(this.elytronLeft, 0, 0, -4.0F, 0.0F, 0.0F, 4, 15, 1, 0.0F, false));

        this.elytronRight = new ModelRenderer(this);
        this.elytronRight.setRotationPoint(0.0F, -7.5F, 3.75F);
        this.body.addChild(this.elytronRight);
        this.elytronRight.cubeList.add(new ModelBox(this.elytronRight, 0, 0, 0.0F, 0.0F, 0.0F, 4, 15, 1, 0.0F, false));

        this.wingLeft = new ModelRenderer(this);
        this.wingLeft.setRotationPoint(0.0F, -7.5F, 3.75F);
        this.body.addChild(this.wingLeft);
        this.wingLeft.cubeList.add(new ModelBox(this.wingLeft, 0, 0, -4.0F, 0.0F, 0.0F, 4, 15, 0, 0.0F, false));

        this.wingRight = new ModelRenderer(this);
        this.wingRight.setRotationPoint(0.0F, -7.5F, 3.75F);
        this.body.addChild(this.wingRight);
        this.wingRight.cubeList.add(new ModelBox(this.wingRight, 0, 0, 0.0F, 0.0F, 0.0F, 4, 15, 0, 0.0F, false));

        this.legBaseLeft = new ModelRenderer(this);
        this.legBaseLeft.setRotationPoint(-3.0F, 6.5F, -5.25F);
        this.body.addChild(this.legBaseLeft);
        this.setRotationAngle(this.legBaseLeft, -0.9599F, 0.0F, 0.0F);
        this.legBaseLeft.cubeList.add(new ModelBox(this.legBaseLeft, 0, 0, -1.0F, 0.0F, 0.0F, 2, 6, 2, 0.0F, false));

        this.legMiddleLeft = new ModelRenderer(this);
        this.legMiddleLeft.setRotationPoint(0.0F, 6.0F, 0.0F);
        this.legBaseLeft.addChild(this.legMiddleLeft);
        this.setRotationAngle(this.legMiddleLeft, 0.6981F, 0.0F, 0.0F);
        this.legMiddleLeft.cubeList.add(new ModelBox(this.legMiddleLeft, 0, 0, -0.5F, 0.0F, 0.0F, 1, 4, 2, 0.0F, false));

        this.legEndLeft = new ModelRenderer(this);
        this.legEndLeft.setRotationPoint(0.0F, 4.0F, 1.5F);
        this.legMiddleLeft.addChild(this.legEndLeft);
        this.setRotationAngle(this.legEndLeft, -0.6109F, 0.0F, 0.0F);
        this.legEndLeft.cubeList.add(new ModelBox(this.legEndLeft, 0, 0, -0.5F, 0.0F, -1.0F, 1, 4, 1, 0.0F, false));

        this.legBaseRight = new ModelRenderer(this);
        this.legBaseRight.setRotationPoint(3.0F, 6.5F, -5.25F);
        this.body.addChild(this.legBaseRight);
        this.setRotationAngle(this.legBaseRight, -0.9599F, 0.0F, 0.0F);
        this.legBaseRight.cubeList.add(new ModelBox(this.legBaseRight, 0, 0, -1.0F, 0.0F, 0.0F, 2, 6, 2, 0.0F, false));

        this.legMiddleRight = new ModelRenderer(this);
        this.legMiddleRight.setRotationPoint(0.0F, 6.0F, 0.0F);
        this.legBaseRight.addChild(this.legMiddleRight);
        this.setRotationAngle(this.legMiddleRight, 0.6981F, 0.0F, 0.0F);
        this.legMiddleRight.cubeList.add(new ModelBox(this.legMiddleRight, 0, 0, -0.5F, 0.0F, 0.0F, 1, 4, 2, 0.0F, false));

        this.legEndRight = new ModelRenderer(this);
        this.legEndRight.setRotationPoint(0.0F, 4.0F, 1.5F);
        this.legMiddleRight.addChild(this.legEndRight);
        this.setRotationAngle(this.legEndRight, -0.6109F, 0.0F, 0.0F);
        this.legEndRight.cubeList.add(new ModelBox(this.legEndRight, 0, 0, -0.5F, 0.0F, -1.0F, 1, 4, 1, 0.0F, false));

        this.legBaseSmall = new ModelRenderer(this);
        this.legBaseSmall.setRotationPoint(2.25F, 5.75F, -5.0F);
        this.setRotationAngle(this.legBaseSmall, -0.6981F, 0.0F, 0.0F);
        this.legBaseSmall.cubeList.add(new ModelBox(this.legBaseSmall, 0, 0, 1.75F, -0.75F, -3.0F, 1, 5, 2, 0.0F, false));

        this.legMiddleSmall = new ModelRenderer(this);
        this.legMiddleSmall.setRotationPoint(-7.25F, 10.25F, -2.0F);
        this.legBaseSmall.addChild(this.legMiddleSmall);
        this.legMiddleSmall.cubeList.add(new ModelBox(this.legMiddleSmall, 0, 0, 9.0F, -6.0F, -0.5F, 1, 4, 1, 0.0F, false));

        this.legEndSmall = new ModelRenderer(this);
        this.legEndSmall.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.legMiddleSmall.addChild(this.legEndSmall);
        this.legEndSmall.cubeList.add(new ModelBox(this.legEndSmall, 0, 0, 9.0F, -2.0F, -0.5F, 1, 4, 1, 0.0F, false));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.body.render(f5);
        this.legBaseSmall.render(f5);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}