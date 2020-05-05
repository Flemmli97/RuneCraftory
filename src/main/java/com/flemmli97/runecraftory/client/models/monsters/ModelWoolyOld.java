package com.flemmli97.runecraftory.client.models.monsters;

import com.flemmli97.runecraftory.common.entity.monster.EntityWooly;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

/**
 * ModelWooly
 * Created using Tabula 7.0.0
 */
public class ModelWoolyOld extends ModelImproved {
    public ModelRenderer bodyBase;
    public ModelRenderer bodyUp;
    public ModelRenderer bodyBack;
    public ModelRenderer bodyLeft;
    public ModelRenderer bodyFront;
    public ModelRenderer legLeft;
    public ModelRenderer bodyRight;
    public ModelRenderer tail;
    public ModelRenderer legRight;
    public ModelRenderer armLeft;
    public ModelRenderer armRight;
    public ModelRenderer neck;
    public ModelRenderer headBase;
    public ModelRenderer headTop;
    public ModelRenderer headRight;
    public ModelRenderer headLeft;
    public ModelRenderer earRight;
    public ModelRenderer earLeft;
    public ModelRenderer footLeft;
    public ModelRenderer footRight;
    public ModelRenderer armJointLeft;
    public ModelRenderer handLeft;
    public ModelRenderer armJointRight;
    public ModelRenderer handRight;

    public ModelWoolyOld() {
        this.textureWidth = 256;
        this.textureHeight = 256;
        this.armLeft = new ModelRenderer(this, 190, 53);
        this.armLeft.setRotationPoint(23.0F, 0.0F, 0.0F);
        this.armLeft.addBox(0.0F, -3.0F, -18.0F, 6, 6, 21, 0.0F);
        this.earRight = new ModelRenderer(this, 0, 49);
        this.earRight.mirror = true;
        this.earRight.setRotationPoint(-17.0F, 0.0F, 0.0F);
        this.earRight.addBox(-16.0F, -2.0F, -5.5F, 16, 4, 11, 0.0F);
        this.bodyBase = new ModelRenderer(this, 0, 101);
        this.bodyBase.setRotationPoint(0.0F, 68.0F, 0.0F);
        this.bodyBase.addBox(-19.0F, -14.0F, -19.0F, 38, 28, 38, 0.0F);
        this.bodyBack = new ModelRenderer(this, 153, 108);
        this.bodyBack.mirror = true;
        this.bodyBack.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bodyBack.addBox(-16.0F, -11.0F, 19.0F, 32, 22, 4, 0.0F);
        this.handRight = new ModelRenderer(this, 190, 81);
        this.handRight.mirror = true;
        this.handRight.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.handRight.addBox(-3.0F, -3.0F, -12.0F, 6, 6, 12, 0.0F);
        this.legRight = new ModelRenderer(this, 0, 168);
        this.legRight.mirror = true;
        this.legRight.setRotationPoint(-8.0F, 14.0F, 0.0F);
        this.legRight.addBox(-3.0F, 0.0F, -3.0F, 6, 9, 6, 0.0F);
        this.headRight = new ModelRenderer(this, 202, 0);
        this.headRight.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.headRight.addBox(-18.0F, -8.0F, -11.0F, 4, 16, 21, 0.0F);
        this.bodyUp = new ModelRenderer(this, 0, 65);
        this.bodyUp.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bodyUp.addBox(-14.5F, -20.0F, -14.5F, 29, 6, 29, 0.0F);
        this.headBase = new ModelRenderer(this, 0, 0);
        this.headBase.setRotationPoint(0.0F, -35.0F, 0.0F);
        this.headBase.addBox(-14.0F, -10.0F, -14.0F, 28, 20, 28, 0.0F);
        this.handLeft = new ModelRenderer(this, 190, 81);
        this.handLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.handLeft.addBox(-3.0F, -3.0F, -12.0F, 6, 6, 12, 0.0F);
        this.earLeft = new ModelRenderer(this, 0, 49);
        this.earLeft.setRotationPoint(17.0F, 0.0F, 0.0F);
        this.earLeft.addBox(0.0F, -2.0F, -5.5F, 16, 4, 11, 0.0F);
        this.armRight = new ModelRenderer(this, 190, 53);
        this.armRight.mirror = true;
        this.armRight.setRotationPoint(-23.0F, 0.0F, 0.0F);
        this.armRight.addBox(-6.0F, -3.0F, -18.0F, 6, 6, 21, 0.0F);
        this.bodyLeft = new ModelRenderer(this, 117, 53);
        this.bodyLeft.mirror = true;
        this.bodyLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bodyLeft.addBox(-23.0F, -11.0F, -16.0F, 4, 22, 32, 0.0F);
        this.bodyFront = new ModelRenderer(this, 153, 108);
        this.bodyFront.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bodyFront.addBox(-16.0F, -11.0F, -23.0F, 32, 22, 4, 0.0F);
        this.bodyRight = new ModelRenderer(this, 117, 53);
        this.bodyRight.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bodyRight.addBox(19.0F, -11.0F, -16.0F, 4, 22, 32, 0.0F);
        this.armJointRight = new ModelRenderer(this, 0, 0);
        this.armJointRight.setRotationPoint(-3.0F, 0.0F, -18.0F);
        this.armJointRight.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.headLeft = new ModelRenderer(this, 202, 0);
        this.headLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.headLeft.addBox(14.0F, -8.0F, -11.0F, 4, 16, 21, 0.0F);
        this.footRight = new ModelRenderer(this, 25, 168);
        this.footRight.mirror = true;
        this.footRight.setRotationPoint(0.0F, 9.0F, 0.0F);
        this.footRight.addBox(-3.0F, 0.0F, -22.0F, 6, 5, 25, 0.0F);
        this.headTop = new ModelRenderer(this, 112, 0);
        this.headTop.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.headTop.addBox(-11.0F, -14.0F, -11.0F, 22, 4, 22, 0.0F);
        this.legLeft = new ModelRenderer(this, 0, 168);
        this.legLeft.setRotationPoint(8.0F, 14.0F, 0.0F);
        this.legLeft.addBox(-3.0F, 0.0F, -3.0F, 6, 9, 6, 0.0F);
        this.tail = new ModelRenderer(this, 89, 168);
        this.tail.setRotationPoint(0.0F, 2.0F, 22.5F);
        this.tail.addBox(-4.0F, -4.0F, 0.0F, 8, 7, 8, 0.0F);
        this.footLeft = new ModelRenderer(this, 25, 168);
        this.footLeft.setRotationPoint(0.0F, 9.0F, 0.0F);
        this.footLeft.addBox(-3.0F, 0.0F, -22.0F, 6, 5, 25, 0.0F);
        this.neck = new ModelRenderer(this, 113, 27);
        this.neck.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.neck.addBox(-10.0F, -25.0F, -10.0F, 20, 5, 20, 0.0F);
        this.armJointLeft = new ModelRenderer(this, 0, 0);
        this.armJointLeft.setRotationPoint(3.0F, 0.0F, -18.0F);
        this.armJointLeft.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.bodyBase.addChild(this.armLeft);
        this.headRight.addChild(this.earRight);
        this.bodyBase.addChild(this.bodyBack);
        this.armJointRight.addChild(this.handRight);
        this.bodyBase.addChild(this.legRight);
        this.headBase.addChild(this.headRight);
        this.bodyBase.addChild(this.bodyUp);
        this.neck.addChild(this.headBase);
        this.armJointLeft.addChild(this.handLeft);
        this.headLeft.addChild(this.earLeft);
        this.bodyBase.addChild(this.armRight);
        this.bodyBase.addChild(this.bodyLeft);
        this.bodyBase.addChild(this.bodyFront);
        this.bodyBase.addChild(this.bodyRight);
        this.armRight.addChild(this.armJointRight);
        this.headBase.addChild(this.headLeft);
        this.legRight.addChild(this.footRight);
        this.headBase.addChild(this.headTop);
        this.bodyBase.addChild(this.legLeft);
        this.bodyBase.addChild(this.tail);
        this.legLeft.addChild(this.footLeft);
        this.bodyUp.addChild(this.neck);
        this.armLeft.addChild(this.armJointLeft);
    }
    
    @Override
	public void startingPosition() {
        this.setRotateAngle(this.armLeft, 0.17453292519943295F, -0.03490658503988659F, 0.0F);
        this.setRotateAngle(this.earRight, 0.0F, 0.0F, -0.1890191579909859F);
        this.setRotateAngle(this.earLeft, 0.0F, 0.0F, 0.1890191579909859F);

        this.setRotateAngle(this.handRight, 0.22759093446006054F, -0.5462880558742251F, 0.0F);
        this.setRotateAngle(this.handLeft, 0.22759093446006054F, 0.5462880558742251F, 0.0F);
        this.setRotateAngle(this.armRight, 0.17453292519943295F, 0.03490658503988659F, 0.0F);
        this.setRotateAngle(this.armJointRight, -0.17453292519943295F, 0.03490658503988659F, 0.0F);
        this.setRotateAngle(this.armJointLeft, -0.17453292519943295F, 0.03490658503988659F, 0.0F);

        this.setRotateAngle(this.footLeft, 0, 0, 0);
        this.setRotateAngle(this.footRight, 0, 0, 0);
        this.setRotateAngle(this.legLeft, 0, 0, 0);
        this.setRotateAngle(this.legRight, 0, 0, 0);
        this.setRotateAngle(this.tail, 0, 0, 0);
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) { 
		this.startingPosition();
		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        this.bodyBase.render(scale/4);
    }
    
    @Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch, float scaleFactor, Entity entity) {
    		EntityWooly wooly = (EntityWooly) entity;
	    	this.headBase.rotateAngleY = netHeadYaw * 0.008453292F;
	    	this.headBase.rotateAngleX = headPitch * 0.012453292F;
    		this.earLeft.rotateAngleZ+=Math.abs(MathHelper.sin(ageInTicks*0.04F)*0.1735987755982989F);
    		this.earRight.rotateAngleZ-=Math.abs(MathHelper.sin(ageInTicks*0.04F)*0.1735987755982989F);
    		
        this.armLeft.rotateAngleX += MathHelper.cos(limbSwing * 0.6662F) * 0.4F * limbSwingAmount ;
        this.armRight.rotateAngleX += MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 0.4F * limbSwingAmount;
            
        this.tail.rotateAngleY+= MathHelper.cos(ageInTicks * 0.6662F) * toRadians(10);
        this.armRight.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.armLeft.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.armRight.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
        this.armLeft.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
        
        this.legRight.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.0F * limbSwingAmount;
        this.legLeft.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.0F * limbSwingAmount;
		
        float partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();
		AnimatedAction anim = ((EntityWooly)entity).getAnimation();
    	if(anim!=null)
    	{
    		
    	}
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
