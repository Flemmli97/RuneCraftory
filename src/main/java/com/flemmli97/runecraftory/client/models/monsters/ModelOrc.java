package com.flemmli97.runecraftory.client.models.monsters;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

/**
 * ModelOrc
 * Created using Tabula 7.0.0
 */
public class ModelOrc extends ModelImproved {

	public ModelRenderer body;
    public ModelRenderer legUpLeft;
    public ModelRenderer head;
    public ModelRenderer legUpLeft_1;
    public ModelRenderer leftArmUp;
    public ModelRenderer rightArmUp;
    public ModelRenderer leftLegDown;
    public ModelRenderer nose;
    public ModelRenderer earLeft;
    public ModelRenderer earRight;
    public ModelRenderer rightLegDown;
    public ModelRenderer leftArmDown;
    public ModelRenderer rightArmDown;
    public ModelRenderer clubBase;
    public ModelRenderer clubHead;

    public ModelOrc() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.leftArmUp = new ModelRenderer(this, 0, 0);
        this.leftArmUp.setRotationPoint(20.0F, -14.0F, 0.0F);
        this.leftArmUp.addBox(0.0F, -7.0F, -7.0F, 26, 14, 14, 0.0F);
        this.earLeft = new ModelRenderer(this, 0, 0);
        this.earLeft.setRotationPoint(10.0F, -44.0F, -16.0F);
        this.earLeft.addBox(-8.0F, -16.0F, -1.0F, 16, 16, 3, 0.0F);
        this.leftArmDown = new ModelRenderer(this, 0, 0);
        this.leftArmDown.setRotationPoint(28.0F, 0.0F, 0.0F);
        this.leftArmDown.addBox(-2.0F, -7.0F, -35.0F, 14, 14, 42, 0.0F);
        this.clubBase = new ModelRenderer(this, 0, 0);
        this.clubBase.setRotationPoint(-3.0F, -7.0F, -28.0F);
        this.clubBase.addBox(-4.0F, -36.0F, -4.0F, 8, 36, 8, 0.0F);
        this.head = new ModelRenderer(this, 0, 0);
        this.head.setRotationPoint(0.0F, -40.0F, 0.0F);
        this.head.addBox(-22.0F, -44.0F, -22.0F, 44, 44, 44, 0.0F);
        this.nose = new ModelRenderer(this, 0, 0);
        this.nose.setRotationPoint(0.0F, -10.0F, -22.0F);
        this.nose.addBox(-10.0F, -10.0F, -20.0F, 20, 20, 20, 0.0F);
        this.rightArmDown = new ModelRenderer(this, 0, 0);
        this.rightArmDown.setRotationPoint(-28.0F, 0.0F, 0.0F);
        this.rightArmDown.addBox(-10.0F, -7.0F, -35.0F, 14, 14, 42, 0.0F);
        this.rightLegDown = new ModelRenderer(this, 0, 0);
        this.rightLegDown.setRotationPoint(0.0F, 27.0F, 0.0F);
        this.rightLegDown.addBox(-7.0F, 0.0F, -7.0F, 14, 20, 14, 0.0F);
        this.setRotateAngle(rightLegDown, 0.3490658503988659F, 0.0F, 0.0F);
        this.earRight = new ModelRenderer(this, 0, 0);
        this.earRight.setRotationPoint(-10.0F, -44.0F, -16.0F);
        this.earRight.addBox(-8.0F, -16.0F, -1.0F, 16, 16, 3, 0.0F);
        this.legUpLeft = new ModelRenderer(this, 0, 0);
        this.legUpLeft.setRotationPoint(13.0F, 38.0F, 0.0F);
        this.legUpLeft.addBox(-7.0F, 0.0F, -7.0F, 14, 30, 14, 0.0F);
        this.setRotateAngle(legUpLeft, -0.5235987755982988F, 0.0F, 0.0F);
        this.rightArmUp = new ModelRenderer(this, 0, 0);
        this.rightArmUp.setRotationPoint(-20.0F, -14.0F, 0.0F);
        this.rightArmUp.addBox(-24.0F, -7.0F, -7.0F, 26, 14, 14, 0.0F);
        this.clubHead = new ModelRenderer(this, 0, 0);
        this.clubHead.setRotationPoint(0.0F, -36.0F, 0.0F);
        this.clubHead.addBox(-8.0F, -16.0F, -8.0F, 16, 16, 16, 0.0F);
        this.leftLegDown = new ModelRenderer(this, 0, 0);
        this.leftLegDown.setRotationPoint(0.0F, 27.0F, 0.0F);
        this.leftLegDown.addBox(-7.0F, 0.0F, -7.0F, 14, 20, 14, 0.0F);
        this.setRotateAngle(leftLegDown, 0.3490658503988659F, 0.0F, 0.0F);
        this.body = new ModelRenderer(this, 0, 0);
        this.body.setRotationPoint(0.0F, 36.0F, 0.0F);
        this.body.addBox(-20.0F, -40.0F, -20.0F, 40, 80, 40, 0.0F);
        this.setRotateAngle(body, 0.17453292519943295F, 0.0F, 0.0F);
        this.legUpLeft_1 = new ModelRenderer(this, 0, 0);
        this.legUpLeft_1.setRotationPoint(-13.0F, 38.0F, 0.0F);
        this.legUpLeft_1.addBox(-7.0F, 0.0F, -7.0F, 14, 30, 14, 0.0F);
        this.setRotateAngle(legUpLeft_1, -0.5235987755982988F, 0.0F, 0.0F);
        this.body.addChild(this.leftArmUp);
        this.head.addChild(this.earLeft);
        this.leftArmUp.addChild(this.leftArmDown);
        this.rightArmDown.addChild(this.clubBase);
        this.body.addChild(this.head);
        this.head.addChild(this.nose);
        this.rightArmUp.addChild(this.rightArmDown);
        this.legUpLeft_1.addChild(this.rightLegDown);
        this.head.addChild(this.earRight);
        this.body.addChild(this.legUpLeft);
        this.body.addChild(this.rightArmUp);
        this.clubBase.addChild(this.clubHead);
        this.legUpLeft.addChild(this.leftLegDown);
        this.body.addChild(this.legUpLeft_1);
    }
    
    @Override
	public void startingPosition() {
    		this.setRotateAngle(leftArmUp, 0, 0, 0);
    		this.setRotateAngle(rightArmUp, 0, 0, 0);
    		this.setRotateAngle(earLeft, 0, 0, 0);
    		this.setRotateAngle(earRight, 0, 0, 0);
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) { 
    		this.startingPosition();
    		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        this.body.render(scale/5);
    }
    
    @Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch, float scaleFactor, Entity entityIn) {
    		this.head.rotateAngleY = netHeadYaw * 0.008453292F;
    		this.head.rotateAngleX = headPitch * 0.012453292F;
    		this.earLeft.rotateAngleX+=MathHelper.cos(limbSwing * 0.6662F) * toRadians(10);
    		this.earRight.rotateAngleX-=MathHelper.cos(limbSwing * 0.6662F) * toRadians(10);
        this.leftArmUp.rotateAngleX += MathHelper.cos(limbSwing * 0.6662F) * 0.35F * limbSwingAmount ;
        this.rightArmUp.rotateAngleX += MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 0.35F * limbSwingAmount;
	}

	/**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    @Override
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
