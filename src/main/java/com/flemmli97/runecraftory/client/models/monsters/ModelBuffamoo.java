package com.flemmli97.runecraftory.client.models.monsters;// Made with Blockbench 3.5.2
// Exported for Minecraft version 1.12
// Paste this class into your mod and generate all required imports


import com.flemmli97.runecraftory.common.entity.monster.EntityBuffamoo;
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

public class ModelBuffamoo extends ModelBase implements IResetModel {
	public ModelRendererPlus body;
	public ModelRendererPlus head;
	public ModelRendererPlus headFront;
	public ModelRendererPlus hornLeft;
	public ModelRendererPlus hornLeftTip;
	public ModelRendererPlus hornRight;
	public ModelRendererPlus hornRight2;
	public ModelRendererPlus udder;
	public ModelRendererPlus frontLegLeftUp;
	public ModelRendererPlus frontLegLeftDown;
	public ModelRendererPlus frontLegRightUp;
	public ModelRendererPlus frontLegRightDown;
	public ModelRendererPlus backLegLeftUp;
	public ModelRendererPlus backLegLeftDown;
	public ModelRendererPlus backLegRightUp;
	public ModelRendererPlus backLegRightDown;
	public ModelRendererPlus frontFur;
	public ModelRendererPlus backFur;
	public ModelRendererPlus tail;
	public ModelRendererPlus tail2;
	public ModelRendererPlus tail3;
	public ModelRendererPlus tailTip;

	public BlockBenchAnimations animations;

	public ModelBuffamoo() {
		this.textureWidth = 121;
		this.textureHeight = 55;

		this.body = new ModelRendererPlus(this);
		this.body.setDefaultRotPoint(0.0F, 4.0F, -1.0F);
		this.body.cubeList.add(new ModelBox(this.body, 0, 0, -6.0F, -3.0F, -7.0F, 12, 14, 20, 0.0F, false));

		this.head = new ModelRendererPlus(this);
		this.head.setDefaultRotPoint(0.0F, 0.0F, -6.0F);
		this.body.addChild(this.head);
		this.setRotationAngle(this.head, 0.1745F, 0.0F, 0.0F);
		this.head.cubeList.add(new ModelBox(this.head, 0, 35, -5.0F, -3.0F, -3.0F, 10, 11, 3, 0.0F, false));

		this.headFront = new ModelRendererPlus(this);
		this.headFront.setDefaultRotPoint(0.0F, 0.0F, -3.0F);
		this.head.addChild(this.headFront);
		this.headFront.cubeList.add(new ModelBox(this.headFront, 26, 34, -4.0F, -2.0F, -9.0F, 8, 9, 9, 0.0F, false));
		this.headFront.cubeList.add(new ModelBox(this.headFront, 64, 20, -4.0F, 7.0F, -9.0F, 8, 2, 6, 0.0F, false));

		this.hornLeft = new ModelRendererPlus(this);
		this.hornLeft.setDefaultRotPoint(4.0F, 0.0F, -4.5F);
		this.head.addChild(this.hornLeft);
		this.setRotationAngle(this.hornLeft, 0.0F, 0.0F, -0.2618F);
		this.hornLeft.cubeList.add(new ModelBox(this.hornLeft, 0, 49, 0.0F, -2.0F, -0.5F, 4, 2, 2, 0.0F, false));

		this.hornLeftTip = new ModelRendererPlus(this);
		this.hornLeftTip.setDefaultRotPoint(4.0F, 0.0F, 1.0F);
		this.hornLeft.addChild(this.hornLeftTip);
		this.setRotationAngle(this.hornLeftTip, 0.0F, 0.0F, -0.9599F);
		this.hornLeftTip.cubeList.add(new ModelBox(this.hornLeftTip, 12, 49, 0.0F, -2.0F, -1.0F, 5, 2, 1, 0.0F, false));

		this.hornRight = new ModelRendererPlus(this);
		this.hornRight.setDefaultRotPoint(-4.0F, 0.0F, -4.5F);
		this.head.addChild(this.hornRight);
		this.setRotationAngle(this.hornRight, 0.0F, 0.0F, 0.2618F);
		this.hornRight.cubeList.add(new ModelBox(this.hornRight, 0, 49, -4.0F, -2.0F, -0.5F, 4, 2, 2, 0.0F, true));

		this.hornRight2 = new ModelRendererPlus(this);
		this.hornRight2.setDefaultRotPoint(-4.0F, 0.0F, 0.0F);
		this.hornRight.addChild(this.hornRight2);
		this.setRotationAngle(this.hornRight2, 0.0F, 0.0F, 0.9599F);
		this.hornRight2.cubeList.add(new ModelBox(this.hornRight2, 12, 49, -5.0F, -2.0F, 0.0F, 5, 2, 1, 0.0F, true));

		this.udder = new ModelRendererPlus(this);
		this.udder.setDefaultRotPoint(0.0F, 30.0F, 10.0F);
		this.body.addChild(this.udder);
		this.udder.cubeList.add(new ModelBox(this.udder, 65, 0, -2.0F, -19.0F, -4.0F, 4, 1, 6, 0.0F, false));

		this.frontLegLeftUp = new ModelRendererPlus(this);
		this.frontLegLeftUp.setDefaultRotPoint(3.9F, 11.0F, -4.0F);
		this.body.addChild(this.frontLegLeftUp);
		this.frontLegLeftUp.cubeList.add(new ModelBox(this.frontLegLeftUp, 89, 0, -2.0F, -1.0F, -2.0F, 4, 5, 4, 0.0F, true));

		this.frontLegLeftDown = new ModelRendererPlus(this);
		this.frontLegLeftDown.setDefaultRotPoint(0.0F, 4.0F, -2.0F);
		this.frontLegLeftUp.addChild(this.frontLegLeftDown);
		this.frontLegLeftDown.cubeList.add(new ModelBox(this.frontLegLeftDown, 89, 10, -2.0F, 0.0F, 0.0F, 4, 5, 4, 0.0F, true));

		this.frontLegRightUp = new ModelRendererPlus(this);
		this.frontLegRightUp.setDefaultRotPoint(-3.9F, 11.0F, -4.0F);
		this.body.addChild(this.frontLegRightUp);
		this.frontLegRightUp.cubeList.add(new ModelBox(this.frontLegRightUp, 89, 0, -2.0F, -1.0F, -2.0F, 4, 5, 4, 0.0F, true));

		this.frontLegRightDown = new ModelRendererPlus(this);
		this.frontLegRightDown.setDefaultRotPoint(0.0F, 4.0F, -2.0F);
		this.frontLegRightUp.addChild(this.frontLegRightDown);
		this.frontLegRightDown.cubeList.add(new ModelBox(this.frontLegRightDown, 89, 10, -2.0F, 0.0F, 0.0F, 4, 5, 4, 0.0F, false));

		this.backLegLeftUp = new ModelRendererPlus(this);
		this.backLegLeftUp.setDefaultRotPoint(3.9F, 11.0F, 9.0F);
		this.body.addChild(this.backLegLeftUp);
		this.backLegLeftUp.cubeList.add(new ModelBox(this.backLegLeftUp, 105, 0, -2.0F, -1.0F, -2.0F, 4, 5, 4, 0.0F, true));

		this.backLegLeftDown = new ModelRendererPlus(this);
		this.backLegLeftDown.setDefaultRotPoint(0.0F, 4.0F, -2.0F);
		this.backLegLeftUp.addChild(this.backLegLeftDown);
		this.backLegLeftDown.cubeList.add(new ModelBox(this.backLegLeftDown, 105, 10, -2.0F, 0.0F, 0.0F, 4, 5, 4, 0.0F, true));

		this.backLegRightUp = new ModelRendererPlus(this);
		this.backLegRightUp.setDefaultRotPoint(-3.9F, 11.0F, 9.0F);
		this.body.addChild(this.backLegRightUp);
		this.backLegRightUp.cubeList.add(new ModelBox(this.backLegRightUp, 105, 0, -2.0F, -1.0F, -2.0F, 4, 5, 4, 0.0F, false));

		this.backLegRightDown = new ModelRendererPlus(this);
		this.backLegRightDown.setDefaultRotPoint(0.0F, 4.0F, -2.0F);
		this.backLegRightUp.addChild(this.backLegRightDown);
		this.backLegRightDown.cubeList.add(new ModelBox(this.backLegRightDown, 105, 10, -2.0F, 0.0F, 0.0F, 4, 5, 4, 0.0F, false));

		this.frontFur = new ModelRendererPlus(this);
		this.frontFur.setDefaultRotPoint(0.0F, 0.0F, 0.0F);
		this.body.addChild(this.frontFur);
		this.frontFur.cubeList.add(new ModelBox(this.frontFur, 64, 28, 0.0F, -9.0F, -11.0F, 0, 9, 13, 0.0F, false));

		this.backFur = new ModelRendererPlus(this);
		this.backFur.setDefaultRotPoint(0.0F, 0.0F, 13.0F);
		this.body.addChild(this.backFur);
		this.backFur.cubeList.add(new ModelBox(this.backFur, 91, 30, 0.0F, -9.0F, -11.0F, 0, 9, 11, 0.0F, false));

		this.tail = new ModelRendererPlus(this);
		this.tail.setDefaultRotPoint(0.0F, -1.0F, 12.0F);
		this.body.addChild(this.tail);
		this.setRotationAngle(this.tail, 0.7854F, 0.0F, 0.0F);
		this.tail.cubeList.add(new ModelBox(this.tail, 64, 8, -0.5F, 0.0F, 0.0F, 1, 4, 1, 0.0F, false));

		this.tail2 = new ModelRendererPlus(this);
		this.tail2.setDefaultRotPoint(0.0F, 4.0F, 0.5F);
		this.tail.addChild(this.tail2);
		this.setRotationAngle(this.tail2, -0.1745F, 0.0F, 0.0F);
		this.tail2.cubeList.add(new ModelBox(this.tail2, 68, 8, -0.5F, -0.3F, -0.5F, 1, 4, 1, 0.0F, false));

		this.tail3 = new ModelRendererPlus(this);
		this.tail3.setDefaultRotPoint(0.0F, 3.7F, 0.0F);
		this.tail2.addChild(this.tail3);
		this.setRotationAngle(this.tail3, -0.5236F, 0.0F, 0.0F);
		this.tail3.cubeList.add(new ModelBox(this.tail3, 72, 8, -0.5F, -0.3F, -0.5F, 1, 5, 1, 0.0F, false));

		this.tailTip = new ModelRendererPlus(this);
		this.tailTip.setDefaultRotPoint(0.0F, 4.3F, 0.0F);
		this.tail3.addChild(this.tailTip);
		this.tailTip.cubeList.add(new ModelBox(this.tailTip, 76, 8, -1.0F, -0.2F, -1.0F, 2, 3, 2, 0.0F, false));

		this.animations = new BlockBenchAnimations(this, new ResourceLocation(LibReference.MODID, "models/entity/animation/buffamoo.json"));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		this.body.render(f5);
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
								  float headPitch, float scaleFactor, Entity entity) {
		if (!(entity instanceof EntityBuffamoo))
			return;
		this.resetModel();
		this.head.rotateAngleY = netHeadYaw * 0.008726646F;
		this.head.rotateAngleX = headPitch * 0.008726646F;

		EntityBuffamoo buffamoo = (EntityBuffamoo) entity;
		float partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();

		if (buffamoo.isMoving())
			this.animations.doAnimation("walk", buffamoo.ticksExisted, partialTicks);

		AnimatedAction anim = buffamoo.getAnimation();
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