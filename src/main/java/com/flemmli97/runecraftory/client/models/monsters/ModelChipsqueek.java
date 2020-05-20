package com.flemmli97.runecraftory.client.models.monsters;// Made with Blockbench 3.5.2
// Exported for Minecraft version 1.12
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelChipsqueek extends ModelBase {
	private final ModelRenderer body;
	private final ModelRenderer legFrontLeft;
	private final ModelRenderer legFrontRight;
	private final ModelRenderer legBackLeftBase;
	private final ModelRenderer legBackLeft;
	private final ModelRenderer legBackLeft3;
	private final ModelRenderer legBackLeftBase2;
	private final ModelRenderer legBackLeft2;
	private final ModelRenderer legBackLeft4;

	public ModelChipsqueek() {
        this.textureWidth = 16;
        this.textureHeight = 16;

        this.body = new ModelRenderer(this);
        this.body.setRotationPoint(0.0F, 18.0F, 2.0F);
        this.setRotationAngle(this.body, -0.5236F, 0.0F, 0.0F);
        this.body.cubeList.add(new ModelBox(this.body, 0, 0, -4.0F, -3.0F, -5.0F, 6, 6, 9, 0.0F, false));
        this.body.cubeList.add(new ModelBox(this.body, 0, 0, -2.0F, -2.0F, 4.0F, 2, 2, 3, 0.0F, false));
        this.body.cubeList.add(new ModelBox(this.body, 0, 0, -2.0F, -6.0F, 5.0F, 2, 4, 2, 0.0F, false));
        this.body.cubeList.add(new ModelBox(this.body, 0, 0, -3.0F, -4.0F, -9.0F, 4, 4, 4, 0.0F, false));

        this.legFrontLeft = new ModelRenderer(this);
        this.legFrontLeft.setRotationPoint(-1.0F, -2.0F, 0.0F);
        this.body.addChild(this.legFrontLeft);
        this.legFrontLeft.cubeList.add(new ModelBox(this.legFrontLeft, 0, 0, -4.0F, 4.0F, -4.0F, 1, 5, 2, 0.0F, false));

        this.legFrontRight = new ModelRenderer(this);
        this.legFrontRight.setRotationPoint(2.0F, -2.0F, 0.0F);
        this.body.addChild(this.legFrontRight);
        this.legFrontRight.cubeList.add(new ModelBox(this.legFrontRight, 0, 0, 0.0F, 4.0F, -4.0F, 1, 5, 2, 0.0F, false));

        this.legBackLeftBase = new ModelRenderer(this);
        this.legBackLeftBase.setRotationPoint(2.0F, -6.0F, 0.0F);
        this.body.addChild(this.legBackLeftBase);
        this.legBackLeftBase.cubeList.add(new ModelBox(this.legBackLeftBase, 0, 0, 0.0F, 6.0F, 1.0F, 1, 3, 3, 0.0F, false));

        this.legBackLeft = new ModelRenderer(this);
        this.legBackLeft.setRotationPoint(0.0F, 11.0F, 4.0F);
        this.legBackLeftBase.addChild(this.legBackLeft);
        this.legBackLeft.cubeList.add(new ModelBox(this.legBackLeft, 0, 0, 0.0F, -2.0F, -1.0F, 1, 1, 1, 0.0F, false));

        this.legBackLeft3 = new ModelRenderer(this);
        this.legBackLeft3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.legBackLeft.addChild(this.legBackLeft3);
        this.legBackLeft3.cubeList.add(new ModelBox(this.legBackLeft3, 0, 0, 0.0F, -1.0F, -3.0F, 1, 1, 3, 0.0F, false));

        this.legBackLeftBase2 = new ModelRenderer(this);
        this.legBackLeftBase2.setRotationPoint(-5.0F, -6.0F, 0.0F);
        this.body.addChild(this.legBackLeftBase2);
        this.legBackLeftBase2.cubeList.add(new ModelBox(this.legBackLeftBase2, 0, 0, 0.0F, 6.0F, 1.0F, 1, 3, 3, 0.0F, false));

        this.legBackLeft2 = new ModelRenderer(this);
        this.legBackLeft2.setRotationPoint(0.0F, 11.0F, 4.0F);
        this.legBackLeftBase2.addChild(this.legBackLeft2);
        this.legBackLeft2.cubeList.add(new ModelBox(this.legBackLeft2, 0, 0, 0.0F, -2.0F, -1.0F, 1, 1, 1, 0.0F, false));

        this.legBackLeft4 = new ModelRenderer(this);
        this.legBackLeft4.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.legBackLeft2.addChild(this.legBackLeft4);
        this.legBackLeft4.cubeList.add(new ModelBox(this.legBackLeft4, 0, 0, 0.0F, -1.0F, -3.0F, 1, 1, 3, 0.0F, false));
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