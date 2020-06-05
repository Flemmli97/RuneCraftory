package com.flemmli97.runecraftory.client.models.monsters;// Made with Blockbench 3.5.2
// Exported for Minecraft version 1.12
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import com.flemmli97.tenshilib.client.model.ModelRendererPlus;
import net.minecraft.entity.Entity;

public class ModelWoolyWool extends ModelBase {
	public ModelRendererPlus bodyCenter;
	public ModelRendererPlus body;
	public ModelRendererPlus bodyUp;
	public ModelRendererPlus armLeftBase;
	public ModelRendererPlus armRightBase;
	public ModelRendererPlus feetLeftBase;
	public ModelRendererPlus feetRightBase;

	public ModelWoolyWool() {
		this.textureWidth = 64;
		this.textureHeight = 62;

		this.bodyCenter = new ModelRendererPlus(this);
		this.bodyCenter.setDefaultRotPoint(0.0F, 17.75F, 0.0F);

		this.body = new ModelRendererPlus(this);
		this.body.setDefaultRotPoint(0.0F, 0.0F, 0.0F);
		this.bodyCenter.addChild(this.body);
		this.body.cubeList.add(new ModelBox(this.body, 0, 31, -3.5F, -7.0F, -4.5F, 7, 13, 9, 0.25F, true));
		this.body.cubeList.add(new ModelBox(this.body, 32, 30, -4.5F, -7.0F, -3.5F, 1, 13, 7, 0.25F, true));
		this.body.cubeList.add(new ModelBox(this.body, 32, 30, 3.5F, -7.0F, -3.5F, 1, 13, 7, 0.25F, false));

		this.bodyUp = new ModelRendererPlus(this);
		this.bodyUp.setDefaultRotPoint(0.0F, -8.0F, 0.0F);
		this.body.addChild(this.bodyUp);
		this.bodyUp.cubeList.add(new ModelBox(this.bodyUp, 32, 14, -2.5F, -1.0F, -3.5F, 5, 2, 7, 0.25F, true));
		this.bodyUp.cubeList.add(new ModelBox(this.bodyUp, 28, 23, 2.5F, -1.0F, -2.5F, 1, 2, 5, 0.25F, false));
		this.bodyUp.cubeList.add(new ModelBox(this.bodyUp, 28, 23, -3.5F, -1.0F, -2.5F, 1, 2, 5, 0.25F, true));

		this.armLeftBase = new ModelRendererPlus(this);
		this.armLeftBase.setDefaultRotPoint(3.75F, -3.0F, 0.0F);
		this.body.addChild(this.armLeftBase);
		this.setRotationAngle(this.armLeftBase, 0.1745F, 0.0F, 0.0F);
		this.armLeftBase.cubeList.add(new ModelBox(this.armLeftBase, 34, 50, 0.25F, -1.0F, -1.0F, 2, 2, 2, 0.25F, false));

		this.armRightBase = new ModelRendererPlus(this);
		this.armRightBase.setDefaultRotPoint(-3.75F, -3.0F, 0.0F);
		this.body.addChild(this.armRightBase);
		this.setRotationAngle(this.armRightBase, 0.1745F, 0.0F, 0.0F);
		this.armRightBase.cubeList.add(new ModelBox(this.armRightBase, 34, 50, -2.25F, -1.0F, -1.0F, 2, 2, 2, 0.25F, false));

		this.feetLeftBase = new ModelRendererPlus(this);
		this.feetLeftBase.setDefaultRotPoint(4.0F, 4.75F, 0.0F);
		this.body.addChild(this.feetLeftBase);
		this.feetLeftBase.cubeList.add(new ModelBox(this.feetLeftBase, 42, 50, -1.5F, -5.5F, -2.5F, 3, 7, 5, 0.25F, false));

		this.feetRightBase = new ModelRendererPlus(this);
		this.feetRightBase.setDefaultRotPoint(-4.0F, 4.75F, 0.0F);
		this.body.addChild(this.feetRightBase);
		this.feetRightBase.cubeList.add(new ModelBox(this.feetRightBase, 42, 50, -1.5F, -5.5F, -2.5F, 3, 7, 5, 0.25F, true));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		this.bodyCenter.render(f5);
	}

	public void setRotationAngle(ModelRendererPlus modelRenderer, float x, float y, float z) {
		modelRenderer.setDefaultRotAngle(x, y, z);
	}

	private void sync(ModelRendererPlus model, ModelRendererPlus other){
		model.rotateAngleX = other.rotateAngleX;
		model.rotateAngleY = other.rotateAngleY;
		model.rotateAngleZ = other.rotateAngleZ;
		model.rotationPointX = other.rotationPointX;
		model.rotationPointY = other.rotationPointY;
		model.rotationPointZ = other.rotationPointZ;
		model.scaleX = other.scaleX;
		model.scaleY = other.scaleY;
		model.scaleZ = other.scaleZ;
	}

	public void syncModel(ModelWooly model){
		this.sync(bodyCenter, model.bodyCenter);
		this.sync(body, model.body);
		this.sync(bodyUp, model.bodyUp);
		this.sync(armLeftBase, model.armLeftBase);
		this.sync(armRightBase, model.armRightBase);
		this.sync(feetLeftBase, model.feetLeftBase);
		this.sync(feetRightBase, model.feetRightBase);
	}
}