package com.flemmli97.runecraftory.client.models.monsters;// Made with Blockbench 3.5.2
// Exported for Minecraft version 1.12
// Paste this class into your mod and generate all required imports


import com.flemmli97.runecraftory.common.entity.monster.EntityChipsqueek;
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

public class ModelChipsqueek extends ModelBase implements IResetModel {
	public ModelRendererPlus body;
	public ModelRendererPlus head;
	public ModelRendererPlus earLeft;
	public ModelRendererPlus earRight;
	public ModelRendererPlus tail;
	public ModelRendererPlus armLeft;
	public ModelRendererPlus armLeftDown;
	public ModelRendererPlus armRight;
	public ModelRendererPlus armRightDown;
	public ModelRendererPlus legLeftBase;
	public ModelRendererPlus legLeft;
	public ModelRendererPlus feetLeft;
	public ModelRendererPlus legRightBase;
	public ModelRendererPlus legRight;
	public ModelRendererPlus feetRight;

	public BlockBenchAnimations animations;

	public ModelChipsqueek() {
		this.textureWidth = 64;
		this.textureHeight = 28;

		this.body = new ModelRendererPlus(this);
		this.body.setDefaultRotPoint(0.0F, 16.8F, 1.5F);
		this.setRotationAngle(this.body, -0.5236F, 0.0F, 0.0F);
		this.body.cubeList.add(new ModelBox(this.body, 0, 12, -3.5F, -4.0F, -4.5F, 7, 7, 9, 0.0F, false));

		this.head = new ModelRendererPlus(this);
		this.head.setDefaultRotPoint(0.0F, -2.5F, -4.0F);
		this.body.addChild(this.head);
		this.setRotationAngle(this.head, 0.6109F, 0.0F, 0.0F);
		this.head.cubeList.add(new ModelBox(this.head, 0, 0, -3.0F, -4.0F, -5.5F, 6, 6, 6, 0.0F, false));

		this.earLeft = new ModelRendererPlus(this);
		this.earLeft.setDefaultRotPoint(1.5F, -2.5F, -2.0F);
		this.head.addChild(this.earLeft);
		this.earLeft.cubeList.add(new ModelBox(this.earLeft, 24, 0, -0.5F, -3.5F, -0.5F, 1, 2, 1, 0.0F, false));

		this.earRight = new ModelRendererPlus(this);
		this.earRight.setDefaultRotPoint(-1.5F, -2.5F, -2.0F);
		this.head.addChild(this.earRight);
		this.earRight.cubeList.add(new ModelBox(this.earRight, 24, 0, -0.5F, -3.5F, -0.5F, 1, 2, 1, 0.0F, true));

		this.tail = new ModelRendererPlus(this);
		this.tail.setDefaultRotPoint(0.0F, -1.0F, 4.5F);
		this.body.addChild(this.tail);
		this.setRotationAngle(this.tail, -0.1745F, 0.0F, 0.0F);
		this.tail.cubeList.add(new ModelBox(this.tail, 32, 0, -2.5F, -10.5F, 0.0F, 5, 12, 2, 0.0F, false));

		this.armLeft = new ModelRendererPlus(this);
		this.armLeft.setDefaultRotPoint(3.5F, 0.5F, -2.5F);
		this.body.addChild(this.armLeft);
		this.setRotationAngle(this.armLeft, 0.0F, 0.0F, 0.1745F);
		this.armLeft.cubeList.add(new ModelBox(this.armLeft, 32, 14, -0.5F, 0.0F, -1.0F, 1, 3, 2, 0.0F, false));

		this.armLeftDown = new ModelRendererPlus(this);
		this.armLeftDown.setDefaultRotPoint(0.0F, 2.5F, 1.0F);
		this.armLeft.addChild(this.armLeftDown);
		this.setRotationAngle(this.armLeftDown, 0.0F, 0.0F, 0.5236F);
		this.armLeftDown.cubeList.add(new ModelBox(this.armLeftDown, 32, 20, -0.5F, 0.5F, -2.0F, 1, 3, 2, 0.0F, false));

		this.armRight = new ModelRendererPlus(this);
		this.armRight.setDefaultRotPoint(-3.5F, 0.5F, -2.5F);
		this.body.addChild(this.armRight);
		this.setRotationAngle(this.armRight, 0.0F, 0.0F, -0.1745F);
		this.armRight.cubeList.add(new ModelBox(this.armRight, 32, 14, -0.5F, 0.0F, -1.0F, 1, 3, 2, 0.0F, true));

		this.armRightDown = new ModelRendererPlus(this);
		this.armRightDown.setDefaultRotPoint(0.0F, 2.5F, 1.0F);
		this.armRight.addChild(this.armRightDown);
		this.setRotationAngle(this.armRightDown, 0.0F, 0.0F, -0.5236F);
		this.armRightDown.cubeList.add(new ModelBox(this.armRightDown, 32, 20, -0.5F, 0.5F, -2.0F, 1, 3, 2, 0.0F, true));

		this.legLeftBase = new ModelRendererPlus(this);
		this.legLeftBase.setDefaultRotPoint(2.0F, 1.75F, 2.75F);
		this.body.addChild(this.legLeftBase);
		this.setRotationAngle(this.legLeftBase, 0.5236F, 0.0F, 0.0F);
		this.legLeftBase.cubeList.add(new ModelBox(this.legLeftBase, 46, 0, 0.0F, -1.75F, -1.25F, 2, 4, 3, 0.0F, false));

		this.legLeft = new ModelRendererPlus(this);
		this.legLeft.setDefaultRotPoint(1.0F, 2.75F, 1.75F);
		this.legLeftBase.addChild(this.legLeft);
		this.legLeft.cubeList.add(new ModelBox(this.legLeft, 46, 7, -1.0F, -0.5F, -2.0F, 2, 2, 2, 0.0F, false));

		this.feetLeft = new ModelRendererPlus(this);
		this.feetLeft.setDefaultRotPoint(0.0F, 2.0F, -2.0F);
		this.legLeft.addChild(this.feetLeft);
		this.feetLeft.cubeList.add(new ModelBox(this.feetLeft, 46, 11, -1.0F, -0.5F, -1.0F, 2, 0, 1, 0.0F, false));

		this.legRightBase = new ModelRendererPlus(this);
		this.legRightBase.setDefaultRotPoint(-4.0F, 1.75F, 2.75F);
		this.body.addChild(this.legRightBase);
		this.setRotationAngle(this.legRightBase, 0.5236F, 0.0F, 0.0F);
		this.legRightBase.cubeList.add(new ModelBox(this.legRightBase, 46, 0, 0.0F, -1.75F, -1.25F, 2, 4, 3, 0.0F, true));

		this.legRight = new ModelRendererPlus(this);
		this.legRight.setDefaultRotPoint(1.0F, 2.75F, 1.75F);
		this.legRightBase.addChild(this.legRight);
		this.legRight.cubeList.add(new ModelBox(this.legRight, 46, 7, -1.0F, -0.5F, -2.0F, 2, 2, 2, 0.0F, true));

		this.feetRight = new ModelRendererPlus(this);
		this.feetRight.setDefaultRotPoint(0.0F, 2.0F, -2.0F);
		this.legRight.addChild(this.feetRight);
		this.feetRight.cubeList.add(new ModelBox(this.feetRight, 46, 11, -1.0F, -0.5F, -1.0F, 2, 0, 1, 0.0F, false));

		this.animations = new BlockBenchAnimations(this, new ResourceLocation(LibReference.MODID, "models/entity/animation/chipsqueek.json"));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		this.body.render(f5);
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
								  float headPitch, float scaleFactor, Entity entity) {
		if (!(entity instanceof EntityChipsqueek))
			return;
		this.resetModel();
		this.tail.rotateAngleX += MathHelper.cos(ageInTicks * 0.04F) * 0.25F + 0.05F;
		this.tail.rotateAngleY += MathHelper.sin(ageInTicks * 0.037F) * 0.25F;
		this.earLeft.rotateAngleX += MathHelper.cos(ageInTicks * 0.05F) * 0.1F;
		this.earLeft.rotateAngleY += MathHelper.sin(ageInTicks * 0.05F) * 0.1F;
		this.earRight.rotateAngleX -= MathHelper.cos(ageInTicks * 0.05F) * 0.1F;
		this.earRight.rotateAngleY -= MathHelper.sin(ageInTicks * 0.05F) * 0.1F;
		this.armLeft.rotateAngleX += MathHelper.cos(ageInTicks * 0.04F) * 0.05F;
		this.armLeft.rotateAngleY += MathHelper.sin(ageInTicks * 0.04F) * 0.25F;
		this.armRight.rotateAngleX -= MathHelper.cos(ageInTicks * 0.04F) * 0.05F;
		this.armRight.rotateAngleY -= MathHelper.sin(ageInTicks * 0.04F) * 0.25F;
		this.head.rotateAngleY += netHeadYaw * 0.017453292F;
		this.head.rotateAngleX += headPitch * 0.017453292F;

		EntityChipsqueek chipsqueek = (EntityChipsqueek) entity;
		float partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();
		if (chipsqueek.isMoving())
			this.animations.doAnimation("walk", chipsqueek.ticksExisted, partialTicks);

		AnimatedAction anim = chipsqueek.getAnimation();
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