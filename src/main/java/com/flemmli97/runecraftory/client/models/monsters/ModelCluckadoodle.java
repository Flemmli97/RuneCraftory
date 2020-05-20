package com.flemmli97.runecraftory.client.models.monsters;// Made with Blockbench 3.5.2
// Exported for Minecraft version 1.12
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelCluckadoodle extends ModelBase {
	private final ModelRenderer body;
	private final ModelRenderer head;
	private final ModelRenderer comb;
	private final ModelRenderer beak;
	private final ModelRenderer backFeathers;
	private final ModelRenderer leg0;
	private final ModelRenderer leg1;
	private final ModelRenderer wing0;
	private final ModelRenderer wing1;

	public ModelCluckadoodle() {
		this.textureWidth = 64;
		this.textureHeight = 32;

		this.body = new ModelRenderer(this);
		this.body.setRotationPoint(0.0F, 16.5F, 1.3333F);
		this.body.cubeList.add(new ModelBox(this.body, 0, 9, -3.0F, -3.5F, -5.3333F, 6, 6, 8, 0.0F, false));

		this.head = new ModelRenderer(this);
		this.head.setRotationPoint(0.0F, -1.5F, -5.3333F);
		this.body.addChild(this.head);
		this.head.cubeList.add(new ModelBox(this.head, 0, 0, -2.0F, -6.0F, -2.0F, 4, 6, 3, 0.0F, false));

		this.comb = new ModelRenderer(this);
		this.comb.setRotationPoint(0.0F, 9.0F, 4.0F);
		this.head.addChild(this.comb);
		this.comb.cubeList.add(new ModelBox(this.comb, 0, 0, 0.0F, -19.0F, -7.0F, 0, 5, 5, 0.0F, false));

		this.beak = new ModelRenderer(this);
		this.beak.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.head.addChild(this.beak);
		this.beak.cubeList.add(new ModelBox(this.beak, 14, 0, -2.0F, -4.0F, -4.0F, 4, 3, 2, 0.0F, false));

		this.backFeathers = new ModelRenderer(this);
		this.backFeathers.setRotationPoint(0.0F, -3.5F, 2.6667F);
		this.body.addChild(this.backFeathers);
		this.setRotationAngle(this.backFeathers, -0.4363F, 0.0F, 0.0F);
		this.backFeathers.cubeList.add(new ModelBox(this.backFeathers, 0, 9, -5.0F, -7.0F, 0.0F, 10, 7, 0, 0.0F, false));

		this.leg0 = new ModelRenderer(this);
		this.leg0.setRotationPoint(-2.0F, 19.0F, 1.0F);
		this.leg0.cubeList.add(new ModelBox(this.leg0, 26, 0, -1.0F, 0.0F, -3.0F, 3, 5, 3, 0.0F, false));

		this.leg1 = new ModelRenderer(this);
		this.leg1.setRotationPoint(1.0F, 19.0F, 1.0F);
		this.leg1.cubeList.add(new ModelBox(this.leg1, 26, 0, -1.0F, 0.0F, -3.0F, 3, 5, 3, 0.0F, false));

		this.wing0 = new ModelRenderer(this);
		this.wing0.setRotationPoint(-3.0F, 13.0F, 0.0F);
		this.setRotationAngle(this.wing0, 0.3491F, 0.0F, 0.0F);
		this.wing0.cubeList.add(new ModelBox(this.wing0, 24, 13, -1.0F, -0.9397F, -3.658F, 1, 4, 7, 0.0F, false));

		this.wing1 = new ModelRenderer(this);
		this.wing1.setRotationPoint(3.0F, 12.0F, 0.0F);
		this.setRotationAngle(this.wing1, 0.3491F, 0.0F, 0.0F);
		this.wing1.cubeList.add(new ModelBox(this.wing1, 24, 13, 0.0F, 0.0F, -4.0F, 1, 4, 7, 0.0F, false));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		this.body.render(f5);
		this.leg0.render(f5);
		this.leg1.render(f5);
		this.wing0.render(f5);
		this.wing1.render(f5);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}