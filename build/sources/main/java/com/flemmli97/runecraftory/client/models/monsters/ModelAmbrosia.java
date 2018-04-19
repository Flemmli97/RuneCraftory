package com.flemmli97.runecraftory.client.models.monsters;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;

/**
 * ModelAmbrosia
 * Created using Tabula 7.0.0
 */
public class ModelAmbrosia extends ModelImproved {
    public ModelRenderer body;
    public ModelRenderer rightArm;
    public ModelRenderer leftArm;
    public ModelRenderer head;
    public ModelRenderer rightLeg;
    public ModelRenderer leftLeg;
    public ModelRenderer leftUpperWing;
    public ModelRenderer leftLowerWing;
    public ModelRenderer rightUpperWing;
    public ModelRenderer rightLowerWing;
    public ModelRenderer dressUp;
    public ModelRenderer rightArmJoint;
    public ModelRenderer rightArmDown;
    public ModelRenderer leftArmJoint;
    public ModelRenderer leftArmDown;
    public ModelRenderer hornFront;
    public ModelRenderer hornLeftStick;
    public ModelRenderer hornRightStick;
    public ModelRenderer hornLeftFlower;
    public ModelRenderer hornRightFlower;
    public ModelRenderer rightLegDown;
    public ModelRenderer leftLegDown;
    public ModelRenderer dressDown;

    public ModelAmbrosia() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.leftArm = new ModelRenderer(this, 32, 48);
        this.leftArm.setRotationPoint(20.0F, 10.0F, 0.0F);
        this.leftArm.addBox(-4.0F, -8.0F, -8.0F, 12, 24, 16, 0.0F);
        this.setRotateAngle(leftArm, -0.17453292519943295F, 0.0F, -0.08726646259971647F);
        this.head = new ModelRenderer(this, 0, 0);
        this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.head.addBox(-16.0F, -32.0F, -16.0F, 32, 32, 32, 0.0F);
        this.leftArmJoint = new ModelRenderer(this, 0, 0);
        this.leftArmJoint.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.leftArmJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.hornLeftFlower = new ModelRenderer(this, 0, 0);
        this.hornLeftFlower.setRotationPoint(0.0F, -8.0F, 0.0F);
        this.hornLeftFlower.addBox(-3.0F, -10.0F, -3.0F, 6, 10, 6, 0.0F);
        this.body = new ModelRenderer(this, 16, 16);
        this.body.setRotationPoint(0.0F, -20.0F, 0.0F);
        this.body.addBox(-16.0F, 0.0F, -8.0F, 32, 40, 16, 0.0F);
        this.setRotateAngle(body, 0.4363323129985824F, 0.0F, 0.0F);
        this.rightArm = new ModelRenderer(this, 40, 16);
        this.rightArm.setRotationPoint(-20.0F, 10.0F, 0.0F);
        this.rightArm.addBox(-8.0F, -8.0F, -8.0F, 12, 24, 16, 0.0F);
        this.setRotateAngle(rightArm, -0.17453292519943295F, 0.0F, 0.08726646259971647F);
        this.leftArmDown = new ModelRenderer(this, 0, 0);
        this.leftArmDown.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.leftArmDown.addBox(-4.0F, -2.0F, -8.0F, 12, 26, 16, 0.0F);
        this.setRotateAngle(leftArmDown, -0.17453292519943295F, 0.0F, 0.0F);
        this.hornRightFlower = new ModelRenderer(this, 0, 0);
        this.hornRightFlower.setRotationPoint(0.0F, -8.0F, 0.0F);
        this.hornRightFlower.addBox(-3.0F, -10.0F, -3.0F, 6, 10, 6, 0.0F);
        this.dressDown = new ModelRenderer(this, 0, 0);
        this.dressDown.setRotationPoint(0.0F, 4.0F, 0.0F);
        this.dressDown.addBox(-20.0F, 0.0F, -12.0F, 40, 4, 24, 0.0F);
        this.rightLegDown = new ModelRenderer(this, 16, 48);
        this.rightLegDown.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.rightLegDown.addBox(-8.0F, -2.0F, -8.0F, 16, 26, 16, 0.0F);
        this.setRotateAngle(rightLegDown, 0.2617993877991494F, 0.0F, 0.0F);
        this.rightArmJoint = new ModelRenderer(this, 0, 0);
        this.rightArmJoint.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.rightArmJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.leftLegDown = new ModelRenderer(this, 16, 48);
        this.leftLegDown.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.leftLegDown.addBox(-8.0F, -2.0F, -8.0F, 16, 26, 16, 0.0F);
        this.setRotateAngle(leftLegDown, 0.2617993877991494F, 0.0F, 0.0F);
        this.rightLeg = new ModelRenderer(this, 0, 16);
        this.rightLeg.setRotationPoint(-7.6F, 48.0F, 0.0F);
        this.rightLeg.addBox(-8.0F, 0.0F, -8.0F, 16, 24, 16, 0.0F);
        this.setRotateAngle(rightLeg, -0.08726646259971647F, 0.0F, 0.0F);
        this.rightLowerWing = new ModelRenderer(this, 0, 0);
        this.rightLowerWing.setRotationPoint(0.0F, 20.0F, 8.0F);
        this.rightLowerWing.addBox(-60.0F, 0.0F, 0.0F, 60, 50, 4, 0.0F);
        this.setRotateAngle(rightLowerWing, 0.08726646259971647F, 0.0F, -0.08726646259971647F);
        this.rightUpperWing = new ModelRenderer(this, 0, 0);
        this.rightUpperWing.setRotationPoint(0.0F, 20.0F, 8.0F);
        this.rightUpperWing.addBox(-70.0F, -60.0F, 0.0F, 70, 60, 4, 0.0F);
        this.setRotateAngle(rightUpperWing, -0.2617993877991494F, 0.0F, 0.08726646259971647F);
        this.leftUpperWing = new ModelRenderer(this, 0, 0);
        this.leftUpperWing.setRotationPoint(0.0F, 20.0F, 8.0F);
        this.leftUpperWing.addBox(0.0F, -60.0F, 0.0F, 70, 60, 4, 0.0F);
        this.setRotateAngle(leftUpperWing, -0.2617993877991494F, 0.0F, -0.08726646259971647F);
        this.leftLowerWing = new ModelRenderer(this, 0, 0);
        this.leftLowerWing.setRotationPoint(0.0F, 20.0F, 8.0F);
        this.leftLowerWing.addBox(0.0F, 0.0F, 0.0F, 60, 50, 4, 0.0F);
        //this.setRotateAngle(leftLowerWing, 0.08726646259971647F, 0.0F, 0.08726646259971647F);
        this.hornLeftStick = new ModelRenderer(this, 0, 0);
        this.hornLeftStick.setRotationPoint(8.0F, -32.0F, -4.0F);
        this.hornLeftStick.addBox(-1.0F, -8.0F, -1.0F, 2, 8, 2, 0.0F);
        this.setRotateAngle(hornLeftStick, 0.17453292519943295F, 0.0F, 0.17453292519943295F);
        this.hornFront = new ModelRenderer(this, 0, 0);
        this.hornFront.setRotationPoint(0.0F, -32.0F, -10.0F);
        this.hornFront.addBox(-2.0F, -20.0F, -2.0F, 4, 20, 4, 0.0F);
        this.setRotateAngle(hornFront, 0.5009094953223726F, 0.0F, 0.0F);
        this.dressUp = new ModelRenderer(this, 0, 0);
        this.dressUp.setRotationPoint(0.0F, 40.0F, 0.0F);
        this.dressUp.addBox(-18.0F, 0.0F, -10.0F, 36, 4, 20, 0.0F);
        this.leftLeg = new ModelRenderer(this, 16, 48);
        this.leftLeg.setRotationPoint(7.6F, 48.0F, 0.0F);
        this.leftLeg.addBox(-8.0F, 0.0F, -8.0F, 16, 24, 16, 0.0F);
        this.setRotateAngle(leftLeg, -0.08726646259971647F, 0.0F, 0.0F);
        this.rightArmDown = new ModelRenderer(this, 0, 0);
        this.rightArmDown.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.rightArmDown.addBox(-8.0F, -2.0F, -8.0F, 12, 26, 16, 0.0F);
        this.setRotateAngle(rightArmDown, -0.17453292519943295F, 0.0F, 0.0F);
        this.hornRightStick = new ModelRenderer(this, 0, 0);
        this.hornRightStick.setRotationPoint(-8.0F, -32.0F, -4.0F);
        this.hornRightStick.addBox(-1.0F, -8.0F, -1.0F, 2, 8, 2, 0.0F);
        this.setRotateAngle(hornRightStick, 0.17453292519943295F, 0.0F, -0.17453292519943295F);
        this.body.addChild(this.leftArm);
        this.body.addChild(this.head);
        this.leftArm.addChild(this.leftArmJoint);
        this.hornLeftStick.addChild(this.hornLeftFlower);
        this.body.addChild(this.rightArm);
        this.leftArmJoint.addChild(this.leftArmDown);
        this.hornRightStick.addChild(this.hornRightFlower);
        this.dressUp.addChild(this.dressDown);
        this.rightLeg.addChild(this.rightLegDown);
        this.rightArm.addChild(this.rightArmJoint);
        this.leftLeg.addChild(this.leftLegDown);
        this.body.addChild(this.rightLeg);
        this.body.addChild(this.rightLowerWing);
        this.body.addChild(this.rightUpperWing);
        this.body.addChild(this.leftUpperWing);
        this.body.addChild(this.leftLowerWing);
        this.head.addChild(this.hornLeftStick);
        this.head.addChild(this.hornFront);
        this.body.addChild(this.dressUp);
        this.body.addChild(this.leftLeg);
        this.rightArmJoint.addChild(this.rightArmDown);
        this.head.addChild(this.hornRightStick);
    }

    @Override
	public void startingPosition() {
        this.setRotateAngle(leftLowerWing, 0.08726646259971647F, 0.0F, 0.08726646259971647F);
        this.setRotateAngle(rightLowerWing, 0.08726646259971647F, 0.0F, -0.08726646259971647F);
        this.setRotateAngle(leftUpperWing, -0.2617993877991494F, 0.0F, -0.08726646259971647F);
        this.setRotateAngle(rightUpperWing, -0.2617993877991494F, 0.0F, 0.08726646259971647F);
        
        this.setRotateAngle(rightArm, -0.17453292519943295F, 0.0F, 0.08726646259971647F);
        this.setRotateAngle(leftArm, -0.17453292519943295F, 0.0F, -0.08726646259971647F);
        
        this.setRotateAngle(body, 0.2617993877991494F, 0.0F, 0.0F);
        
        this.setRotateAngle(hornRightStick, 0.17453292519943295F, 0.0F, -0.17453292519943295F);
        this.setRotateAngle(hornLeftStick, 0.17453292519943295F, 0.0F, 0.17453292519943295F);
        this.setRotateAngle(hornFront, 0.5009094953223726F, 0.0F, 0.0F);

	}

	@Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) { 
		this.startingPosition();
		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
    		this.body.render(scale/4);
    }

    @Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch, float scale, Entity entity) {
	    	if(((EntityLivingBase)entity).limbSwingAmount != ((EntityLivingBase)entity).prevLimbSwingAmount)
	    	{
	    		this.setRotateAngle(body, 0.6108652381980154F, 0.0F, 0.0F);
	    	}
	    	this.head.rotateAngleY = netHeadYaw * 0.017453292F;
	    	this.head.rotateAngleX = headPitch * 0.017453292F;

    		this.leftLowerWing.rotateAngleY+=MathHelper.cos(ageInTicks * 1.2F) * toRadians(10)+toRadians(-5);
    		this.rightLowerWing.rotateAngleY-=MathHelper.cos(ageInTicks * 1.2F) * toRadians(10)+toRadians(-5);
    		this.leftUpperWing.rotateAngleY+=MathHelper.cos(ageInTicks * 1.2F) * toRadians(10)+toRadians(-5);
    		this.rightUpperWing.rotateAngleY-=MathHelper.cos(ageInTicks * 1.2F) * toRadians(10)+toRadians(-5);
        this.rightArm.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.leftArm.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.rightArm.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
        this.leftArm.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
        
        this.hornRightStick.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.hornLeftStick.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.hornRightStick.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
        this.hornLeftStick.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
        this.hornFront.rotateAngleX+= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
    }
}
