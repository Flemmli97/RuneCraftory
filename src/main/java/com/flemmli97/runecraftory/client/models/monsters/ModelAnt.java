package com.flemmli97.runecraftory.client.models.monsters;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

/**
 * ModelAnt 
 * Created using Tabula 7.0.0
 */
public class ModelAnt extends ModelImproved {

	public ModelRenderer bodyFront;
    public ModelRenderer bodyBack;
    public ModelRenderer head;
    public ModelRenderer leg1Up;
    public ModelRenderer leg2Up;
    public ModelRenderer leg3Up;
    public ModelRenderer leg4Up;
    public ModelRenderer leg5Up;
    public ModelRenderer leg6Up;
    public ModelRenderer bodyBackTop;
    public ModelRenderer feeler1;
    public ModelRenderer feeler2;
    public ModelRenderer leg1Down;
    public ModelRenderer leg2Down;
    public ModelRenderer leg3Down;
    public ModelRenderer leg4Down;
    public ModelRenderer leg5Down;
    public ModelRenderer leg6Down;

    public ModelAnt() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.leg6Down = new ModelRenderer(this, 0, 0);
        this.leg6Down.setRotationPoint(-39.0F, 0.0F, 0.0F);
        this.leg6Down.addBox(-30.0F, -4.0F, -4.0F, 30, 8, 8, 0.0F);
        this.setRotateAngle(leg6Down, 0.0F, -0.2792526803190927F, -0.3141592653589793F);
        this.bodyFront = new ModelRenderer(this, 0, 0);
        this.bodyFront.setRotationPoint(0.0F, 205.0F, 0.0F);
        this.bodyFront.addBox(-18.0F, -8.0F, -32.0F, 36, 18, 32, 0.0F);
        this.leg3Up = new ModelRenderer(this, 0, 0);
        this.leg3Up.setRotationPoint(17.0F, 0.0F, -25.0F);
        this.leg3Up.addBox(0.0F, -4.0F, -4.0F, 40, 8, 8, 0.0F);
        this.setRotateAngle(leg3Up, 0.0F, 0.13962634015954636F, 0.3490658503988659F);
        this.leg6Up = new ModelRenderer(this, 0, 0);
        this.leg6Up.setRotationPoint(-17.0F, 0.0F, -25.0F);
        this.leg6Up.addBox(-40.0F, -4.0F, -4.0F, 40, 8, 8, 0.0F);
        this.setRotateAngle(leg6Up, 0.0F, -0.13962634015954636F, -0.3490658503988659F);
        this.leg4Down = new ModelRenderer(this, 0, 0);
        this.leg4Down.setRotationPoint(-39.0F, 0.0F, 0.0F);
        this.leg4Down.addBox(-30.0F, -4.0F, -4.0F, 30, 8, 8, 0.0F);
        this.setRotateAngle(leg4Down, 0.0F, 0.27314402793711257F, -0.31869712141416456F);
        this.leg2Up = new ModelRenderer(this, 0, 0);
        this.leg2Up.setRotationPoint(17.0F, 0.0F, -15.0F);
        this.leg2Up.addBox(0.0F, -4.0F, -4.0F, 40, 8, 8, 0.0F);
        this.setRotateAngle(leg2Up, 0.0F, -0.08726646259971647F, 0.3490658503988659F);
        this.bodyBackTop = new ModelRenderer(this, 0, 0);
        this.bodyBackTop.setRotationPoint(0.0F, -10.0F, 30.0F);
        this.bodyBackTop.addBox(-15.0F, -10.0F, -25.0F, 30, 10, 50, 0.0F);
        this.leg1Up = new ModelRenderer(this, 0, 0);
        this.leg1Up.setRotationPoint(17.0F, 0.0F, -5.0F);
        this.leg1Up.addBox(0.0F, -4.0F, -4.0F, 40, 8, 8, 0.0F);
        this.setRotateAngle(leg1Up, 0.0F, -0.3490658503988659F, 0.3490658503988659F);
        this.leg2Down = new ModelRenderer(this, 0, 0);
        this.leg2Down.setRotationPoint(39.0F, 0.0F, 0.0F);
        this.leg2Down.addBox(0.0F, -4.0F, -4.0F, 30, 8, 8, 0.0F);
        this.setRotateAngle(leg2Down, 0.0F, 0.3141592653589793F, 0.2617993877991494F);
        this.feeler2 = new ModelRenderer(this, 0, 0);
        this.feeler2.setRotationPoint(-11.0F, -15.0F, -31.0F);
        this.feeler2.addBox(-4.0F, -16.0F, -4.0F, 4, 16, 4, 0.0F);
        this.feeler1 = new ModelRenderer(this, 0, 0);
        this.feeler1.setRotationPoint(11.0F, -15.0F, -31.0F);
        this.feeler1.addBox(-2.0F, -16.0F, -2.0F, 4, 16, 4, 0.0F);
        this.head = new ModelRenderer(this, 0, 0);
        this.head.setRotationPoint(0.0F, 0.0F, -32.0F);
        this.head.addBox(-20.0F, -15.0F, -40.0F, 40, 30, 40, 0.0F);
        this.leg3Down = new ModelRenderer(this, 0, 0);
        this.leg3Down.setRotationPoint(39.0F, 0.0F, 0.0F);
        this.leg3Down.addBox(0.0F, -4.0F, -4.0F, 30, 8, 8, 0.0F);
        this.setRotateAngle(leg3Down, 0.0F, 0.2792526803190927F, 0.3141592653589793F);
        this.leg5Down = new ModelRenderer(this, 0, 0);
        this.leg5Down.setRotationPoint(-39.0F, 0.0F, 0.0F);
        this.leg5Down.addBox(-30.0F, -4.0F, -4.0F, 30, 8, 8, 0.0F);
        this.setRotateAngle(leg5Down, 0.0F, -0.3141592653589793F, -0.2792526803190927F);
        this.leg4Up = new ModelRenderer(this, 0, 0);
        this.leg4Up.setRotationPoint(-17.0F, 0.0F, -5.0F);
        this.leg4Up.addBox(-40.0F, -4.0F, -4.0F, 40, 8, 8, 0.0F);
        this.setRotateAngle(leg4Up, 0.0F, 0.3490658503988659F, -0.3490658503988659F);
        this.leg5Up = new ModelRenderer(this, 0, 0);
        this.leg5Up.setRotationPoint(-17.0F, 0.0F, -15.0F);
        this.leg5Up.addBox(-40.0F, -4.0F, -4.0F, 40, 8, 8, 0.0F);
        this.setRotateAngle(leg5Up, 0.0F, 0.08726646259971647F, -0.3490658503988659F);
        this.leg1Down = new ModelRenderer(this, 0, 0);
        this.leg1Down.setRotationPoint(39.0F, 0.0F, 0.0F);
        this.leg1Down.addBox(0.0F, -4.0F, -4.0F, 30, 8, 8, 0.0F);
        this.setRotateAngle(leg1Down, 0.0F, -0.27314402793711257F, 0.31869712141416456F);
        this.bodyBack = new ModelRenderer(this, 0, 0);
        this.bodyBack.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bodyBack.addBox(-25.0F, -10.0F, 0.0F, 50, 20, 60, 0.0F);
        this.leg6Up.addChild(this.leg6Down);
        this.bodyFront.addChild(this.leg3Up);
        this.bodyFront.addChild(this.leg6Up);
        this.leg4Up.addChild(this.leg4Down);
        this.bodyFront.addChild(this.leg2Up);
        this.bodyBack.addChild(this.bodyBackTop);
        this.bodyFront.addChild(this.leg1Up);
        this.leg2Up.addChild(this.leg2Down);
        this.head.addChild(this.feeler2);
        this.head.addChild(this.feeler1);
        this.bodyFront.addChild(this.head);
        this.leg3Up.addChild(this.leg3Down);
        this.leg5Up.addChild(this.leg5Down);
        this.bodyFront.addChild(this.leg4Up);
        this.bodyFront.addChild(this.leg5Up);
        this.leg1Up.addChild(this.leg1Down);
        this.bodyFront.addChild(this.bodyBack);
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) { 
    		this.startingPosition();
    		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        this.bodyFront.render(scale/10.0F);
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

	@Override
	public void startingPosition() {
		this.setRotateAngle(this.feeler1, 0, 0, 0);
		this.setRotateAngle(this.feeler2, 0, 0, 0);
		
        this.setRotateAngle(leg1Up, 0.0F, -0.3490658503988659F, 0.3490658503988659F);
        this.setRotateAngle(leg1Down, 0.0F, -0.27314402793711257F, 0.31869712141416456F);
        this.setRotateAngle(leg2Up, 0.0F, -0.08726646259971647F, 0.3490658503988659F);
        this.setRotateAngle(leg2Down, 0.0F, 0.3141592653589793F, 0.2617993877991494F);
        this.setRotateAngle(leg3Up, 0.0F, 0.13962634015954636F, 0.3490658503988659F);
        this.setRotateAngle(leg3Down, 0.0F, 0.2792526803190927F, 0.3141592653589793F);
        this.setRotateAngle(leg4Up, 0.0F, 0.3490658503988659F, -0.3490658503988659F);
        this.setRotateAngle(leg4Down, 0.0F, 0.27314402793711257F, -0.31869712141416456F);
        this.setRotateAngle(leg5Up, 0.0F, 0.08726646259971647F, -0.3490658503988659F);
        this.setRotateAngle(leg5Down, 0.0F, -0.3141592653589793F, -0.2792526803190927F);
        this.setRotateAngle(leg6Up, 0.0F, -0.13962634015954636F, -0.3490658503988659F);
        this.setRotateAngle(leg6Down, 0.0F, -0.2792526803190927F, -0.3141592653589793F);

	}
	
	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch, float scale, Entity entity) {
	    	this.head.rotateAngleY = netHeadYaw * 0.008726646F;
	    	this.head.rotateAngleX = headPitch * 0.008726646F;
		this.leg1Up.rotateAngleY+=MathHelper.cos(limbSwing * 2.2F) * toRadians(7);
		this.leg2Up.rotateAngleY-=MathHelper.cos(limbSwing * 2.2F) * toRadians(7);
		this.leg3Up.rotateAngleY+=MathHelper.cos(limbSwing * 2.2F) * toRadians(7);
		this.leg4Up.rotateAngleY-=MathHelper.cos(limbSwing * 2.2F) * toRadians(7);
		this.leg5Up.rotateAngleY+=MathHelper.cos(limbSwing * 2.2F) * toRadians(7);
		this.leg6Up.rotateAngleY-=MathHelper.cos(limbSwing * 2.2F) * toRadians(7);

        this.feeler1.rotateAngleZ += MathHelper.cos(ageInTicks * 0.15F) * 0.05F + 0.05F;
        this.feeler2.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.15F) * 0.05F + 0.05F;
        this.feeler1.rotateAngleX += MathHelper.sin(ageInTicks * 0.1F) * 0.05F;
        this.feeler2.rotateAngleX -= MathHelper.sin(ageInTicks * 0.1F) * 0.05F;
    }
}
