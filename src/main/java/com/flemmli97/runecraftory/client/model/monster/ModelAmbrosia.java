package com.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.entities.monster.boss.EntityAmbrosia;
import com.flemmli97.tenshilib.client.model.BlockBenchAnimations;
import com.flemmli97.tenshilib.client.model.IResetModel;
import com.flemmli97.tenshilib.client.model.ModelRendererPlus;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import com.flemmli97.tenshilib.common.utils.MathUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class ModelAmbrosia<T extends EntityAmbrosia> extends EntityModel<T> implements IResetModel {
    public ModelRendererPlus body;
    public ModelRendererPlus head;
    public ModelRendererPlus hornFront;
    public ModelRendererPlus hornLeftStick;
    public ModelRendererPlus hornLeftFlower;
    public ModelRendererPlus hornRightStick;
    public ModelRendererPlus hornRightFlower;
    public ModelRendererPlus leftUpperWing;
    public ModelRendererPlus leftLowerWing;
    public ModelRendererPlus rightUpperWing;
    public ModelRendererPlus rightLowerWing;
    public ModelRendererPlus leftArm;
    public ModelRendererPlus leftArmDown;
    public ModelRendererPlus rightArm;
    public ModelRendererPlus rightArmDown;
    public ModelRendererPlus dressUp;
    public ModelRendererPlus dressDown;
    public ModelRendererPlus leftLeg;
    public ModelRendererPlus leftLegDown;
    public ModelRendererPlus rightLeg;
    public ModelRendererPlus rightLegDown;

    public BlockBenchAnimations animations;

    public ModelAmbrosia() {
        this.textureWidth = 66;
        this.textureHeight = 65;

        this.body = new ModelRendererPlus(this);
        this.body.setDefaultRotPoint(0.0F, -5.0F, 0.0F);
        this.setRotationAngle(this.body, 0.0698F, 0.0F, 0.0F);
        this.body.setTextureOffset(0, 51).addCuboid(-4.0F, 0.0F, -2.0F, 8.0F, 10.0F, 4.0F, 0.0F, true);

        this.head = new ModelRendererPlus(this);
        this.head.setDefaultRotPoint(0.0F, 0.0F, 0.0F);
        this.body.addChild(this.head);
        this.head.setTextureOffset(0, 0).addCuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, true);

        this.hornFront = new ModelRendererPlus(this);
        this.hornFront.setDefaultRotPoint(0.0F, -8.0F, -2.5F);
        this.head.addChild(this.hornFront);
        this.setRotationAngle(this.hornFront, 0.6009F, 0.0F, 0.0F);
        this.hornFront.setTextureOffset(0, 16).addCuboid(-0.5F, -5.0F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, true);

        this.hornLeftStick = new ModelRendererPlus(this);
        this.hornLeftStick.setDefaultRotPoint(1.75F, -8.0F, -0.75F);
        this.head.addChild(this.hornLeftStick);
        this.setRotationAngle(this.hornLeftStick, 0.1745F, 0.0F, -0.0524F);
        this.hornLeftStick.setTextureOffset(4, 16).addCuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);

        this.hornLeftFlower = new ModelRendererPlus(this);
        this.hornLeftFlower.setDefaultRotPoint(0.0F, -3.0F, 0.0F);
        this.hornLeftStick.addChild(this.hornLeftFlower);
        this.hornLeftFlower.setTextureOffset(8, 16).addCuboid(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, true);

        this.hornRightStick = new ModelRendererPlus(this);
        this.hornRightStick.setDefaultRotPoint(-1.75F, -8.0F, -0.75F);
        this.head.addChild(this.hornRightStick);
        this.setRotationAngle(this.hornRightStick, 0.1745F, 0.0F, 0.0524F);
        this.hornRightStick.setTextureOffset(4, 16).addCuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        this.hornRightFlower = new ModelRendererPlus(this);
        this.hornRightFlower.setDefaultRotPoint(0.0F, -3.0F, 0.0F);
        this.hornRightStick.addChild(this.hornRightFlower);
        this.hornRightFlower.setTextureOffset(8, 16).addCuboid(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);

        this.leftUpperWing = new ModelRendererPlus(this);
        this.leftUpperWing.setDefaultRotPoint(0.0F, 5.0F, 2.0F);
        this.body.addChild(this.leftUpperWing);
        this.setRotationAngle(this.leftUpperWing, -0.2618F, 0.0F, 0.0873F);
        this.leftUpperWing.setTextureOffset(0, 22).addCuboid(0.0F, -15.0F, 0.0F, 17.0F, 15.0F, 0.0F, 0.0F, true);

        this.leftLowerWing = new ModelRendererPlus(this);
        this.leftLowerWing.setDefaultRotPoint(0.0F, 5.0F, 2.0F);
        this.body.addChild(this.leftLowerWing);
        this.setRotationAngle(this.leftLowerWing, 0.2618F, 0.0F, -0.0873F);
        this.leftLowerWing.setTextureOffset(0, 37).addCuboid(0.0F, 0.0F, 0.0F, 17.0F, 14.0F, 0.0F, 0.0F, true);

        this.rightUpperWing = new ModelRendererPlus(this);
        this.rightUpperWing.setDefaultRotPoint(0.0F, 5.0F, 2.0F);
        this.body.addChild(this.rightUpperWing);
        this.setRotationAngle(this.rightUpperWing, -0.2618F, 0.0F, -0.0873F);
        this.rightUpperWing.setTextureOffset(0, 22).addCuboid(-17.0F, -15.0F, 0.0F, 17.0F, 15.0F, 0.0F, 0.0F, false);

        this.rightLowerWing = new ModelRendererPlus(this);
        this.rightLowerWing.setDefaultRotPoint(0.0F, 5.0F, 2.0F);
        this.body.addChild(this.rightLowerWing);
        this.setRotationAngle(this.rightLowerWing, 0.2618F, 0.0F, 0.0873F);
        this.rightLowerWing.setTextureOffset(0, 37).addCuboid(-17.0F, 0.0F, 0.0F, 17.0F, 14.0F, 0.0F, 0.0F, false);

        this.leftArm = new ModelRendererPlus(this);
        this.leftArm.setDefaultRotPoint(5.0F, 2.5F, 0.0F);
        this.body.addChild(this.leftArm);
        this.setRotationAngle(this.leftArm, -0.1745F, 0.0F, 0.1F);
        this.leftArm.setTextureOffset(33, 1).addCuboid(-1.0F, -2.0F, -1.5F, 3.0F, 6.0F, 3.0F, 0.0F, true);

        this.leftArmDown = new ModelRendererPlus(this);
        this.leftArmDown.setDefaultRotPoint(0.5F, 4.0F, 1.5F);
        this.leftArm.addChild(this.leftArmDown);
        this.setRotationAngle(this.leftArmDown, -0.1745F, 0.0F, 0.0F);
        this.leftArmDown.setTextureOffset(33, 11).addCuboid(-1.5F, 0.0F, -3.0F, 3.0F, 6.0F, 3.0F, 0.0F, true);

        this.rightArm = new ModelRendererPlus(this);
        this.rightArm.setDefaultRotPoint(-5.0F, 2.5F, 0.0F);
        this.body.addChild(this.rightArm);
        this.setRotationAngle(this.rightArm, -0.1745F, 0.0F, -0.1F);
        this.rightArm.setTextureOffset(33, 1).addCuboid(-2.0F, -2.0F, -2.0F, 3.0F, 6.0F, 3.0F, 0.0F, false);

        this.rightArmDown = new ModelRendererPlus(this);
        this.rightArmDown.setDefaultRotPoint(-0.5F, 4.0F, 1.0F);
        this.rightArm.addChild(this.rightArmDown);
        this.setRotationAngle(this.rightArmDown, -0.1745F, 0.0F, 0.0F);
        this.rightArmDown.setTextureOffset(33, 11).addCuboid(-1.5F, 0.0F, -3.0F, 3.0F, 6.0F, 3.0F, 0.0F, false);

        this.dressUp = new ModelRendererPlus(this);
        this.dressUp.setDefaultRotPoint(0.0F, 10.0F, 0.0F);
        this.body.addChild(this.dressUp);
        this.dressUp.setTextureOffset(34, 50).addCuboid(-5.0F, 0.0F, -3.0F, 10.0F, 1.0F, 6.0F, 0.0F, true);

        this.dressDown = new ModelRendererPlus(this);
        this.dressDown.setDefaultRotPoint(0.0F, 1.0F, 0.0F);
        this.dressUp.addChild(this.dressDown);
        this.dressDown.setTextureOffset(24, 57).addCuboid(-5.5F, 0.0F, -3.5F, 11.0F, 1.0F, 7.0F, 0.0F, true);

        this.leftLeg = new ModelRendererPlus(this);
        this.leftLeg.setDefaultRotPoint(1.9F, 11.0F, 0.0F);
        this.body.addChild(this.leftLeg);
        this.setRotationAngle(this.leftLeg, -0.0873F, 0.0F, 0.0F);
        this.leftLeg.setTextureOffset(34, 20).addCuboid(-1.9F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, 0.0F, true);

        this.leftLegDown = new ModelRendererPlus(this);
        this.leftLegDown.setDefaultRotPoint(0.1F, 7.0F, -2.0F);
        this.leftLeg.addChild(this.leftLegDown);
        this.setRotationAngle(this.leftLegDown, 0.2618F, 0.0F, 0.0F);
        this.leftLegDown.setTextureOffset(50, 20).addCuboid(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, 0.0F, true);

        this.rightLeg = new ModelRendererPlus(this);
        this.rightLeg.setDefaultRotPoint(-1.9F, 11.0F, 0.0F);
        this.body.addChild(this.rightLeg);
        this.setRotationAngle(this.rightLeg, -0.0873F, 0.0F, 0.0F);
        this.rightLeg.setTextureOffset(34, 31).addCuboid(-2.1F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, 0.0F, false);

        this.rightLegDown = new ModelRendererPlus(this);
        this.rightLegDown.setDefaultRotPoint(-0.1F, 7.0F, -2.0F);
        this.rightLeg.addChild(this.rightLegDown);
        this.setRotationAngle(this.rightLegDown, 0.2618F, 0.0F, 0.0F);
        this.rightLegDown.setTextureOffset(50, 30).addCuboid(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, 0.0F, true);

        this.animations = new BlockBenchAnimations(this, new ResourceLocation(RuneCraftory.MODID, "models/entity/animation/ambrosia.json"));
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.body.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public void setAngles(T brosia, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetModel();
        AnimatedAction anim = brosia.getAnimation();
        if (brosia.deathTime <= 0) {
            this.head.rotateAngleY = netHeadYaw * 0.017453292F;
            this.head.rotateAngleX = headPitch * 0.017453292F;

            this.leftLowerWing.rotateAngleY += MathHelper.cos(ageInTicks * 1.2F) * MathUtils.degToRad(10) + MathUtils.degToRad(-5);
            this.rightLowerWing.rotateAngleY -= MathHelper.cos(ageInTicks * 1.2F) * MathUtils.degToRad(10) + MathUtils.degToRad(-5);
            this.leftUpperWing.rotateAngleY += MathHelper.cos(ageInTicks * 1.2F) * MathUtils.degToRad(10) + MathUtils.degToRad(-5);
            this.rightUpperWing.rotateAngleY -= MathHelper.cos(ageInTicks * 1.2F) * MathUtils.degToRad(10) + MathUtils.degToRad(-5);
            this.rightArm.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
            this.leftArm.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
            this.rightArm.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
            this.leftArm.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;

            this.hornRightStick.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
            this.hornLeftStick.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
            this.hornRightStick.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
            this.hornLeftStick.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
            this.hornFront.rotateAngleX += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;

            if (brosia.isMoving() && anim == null)
                this.body.rotateAngleX = 0.6108652381980154F;
        }
        float partialTicks = Minecraft.getInstance().getRenderPartialTicks();
        if (anim != null)
            this.animations.doAnimation(anim.getID().equals(EntityAmbrosia.pollen2.getID()) ? EntityAmbrosia.pollen.getID() : anim.getID(), anim.getTick(), partialTicks);
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