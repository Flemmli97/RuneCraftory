package com.flemmli97.runecraftory.client.models.monsters;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * ModelBeetle
 * Created using Tabula 7.0.0
 */
public class ModelBeetle extends ModelBase {
    public ModelRenderer bodyBase;
    public ModelRenderer bodyBackMiddle;
    public ModelRenderer leftLegUp;
    public ModelRenderer rightLegUp;
    public ModelRenderer arm1Up;
    public ModelRenderer arm2Up;
    public ModelRenderer arm1Up_1;
    public ModelRenderer arm1Up_2;
    public ModelRenderer headBase;
    public ModelRenderer headBack;
    public ModelRenderer bodyBackTop;
    public ModelRenderer leftLegDown;
    public ModelRenderer rightLefDown;
    public ModelRenderer arm1Down;
    public ModelRenderer arm1Down_1;
    public ModelRenderer arm1Down_2;
    public ModelRenderer arm1Down_3;
    public ModelRenderer headMiddle;
    public ModelRenderer headTop;
    public ModelRenderer headBackMiddle;
    public ModelRenderer headBackTop;
    public ModelRenderer hornBack;
    public ModelRenderer hornFront;
    public ModelRenderer hornFrontTop;
    public ModelRenderer hornBackTop;

    public ModelBeetle() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.rightLegUp = new ModelRenderer(this, 0, 0);
        this.rightLegUp.setRotationPoint(-18.0F, 26.0F, -5.0F);
        this.rightLegUp.addBox(-5.0F, 0.0F, -5.0F, 10, 30, 10, 0.0F);
        this.setRotateAngle(rightLegUp, -1.2217304763960306F, 0.0F, 0.0F);
        this.leftLegUp = new ModelRenderer(this, 0, 0);
        this.leftLegUp.setRotationPoint(18.0F, 26.0F, -5.0F);
        this.leftLegUp.addBox(-5.0F, 0.0F, -5.0F, 10, 30, 10, 0.0F);
        this.setRotateAngle(leftLegUp, -1.2217304763960306F, 0.0F, 0.0F);
        this.arm1Down_2 = new ModelRenderer(this, 0, 0);
        this.arm1Down_2.setRotationPoint(0.0F, 0.0F, -20.0F);
        this.arm1Down_2.addBox(-2.0F, -2.0F, -24.0F, 4, 4, 24, 0.0F);
        this.setRotateAngle(arm1Down_2, -0.593411945678072F, 0.7330382858376184F, 0.0F);
        this.arm1Down = new ModelRenderer(this, 0, 0);
        this.arm1Down.setRotationPoint(0.0F, 0.0F, -20.0F);
        this.arm1Down.addBox(-2.0F, -2.0F, -24.0F, 4, 4, 24, 0.0F);
        this.setRotateAngle(arm1Down, -0.593411945678072F, 0.7330382858376184F, 0.0F);
        this.arm1Up_2 = new ModelRenderer(this, 0, 0);
        this.arm1Up_2.setRotationPoint(-22.0F, -10.0F, -8.0F);
        this.arm1Up_2.addBox(-2.0F, -2.0F, -20.0F, 4, 4, 20, 0.0F);
        this.setRotateAngle(arm1Up_2, 0.0F, 0.6283185307179586F, 0.0F);
        this.arm2Up = new ModelRenderer(this, 0, 0);
        this.arm2Up.setRotationPoint(-22.0F, 10.0F, -8.0F);
        this.arm2Up.addBox(-2.0F, -2.0F, -20.0F, 4, 4, 20, 0.0F);
        this.setRotateAngle(arm2Up, 0.0F, 0.6283185307179586F, 0.0F);
        this.arm1Up = new ModelRenderer(this, 0, 0);
        this.arm1Up.setRotationPoint(22.0F, 10.0F, -8.0F);
        this.arm1Up.addBox(-2.0F, -2.0F, -20.0F, 4, 4, 20, 0.0F);
        this.setRotateAngle(arm1Up, 0.0F, -0.6283185307179586F, 0.0F);
        this.rightLefDown = new ModelRenderer(this, 0, 0);
        this.rightLefDown.setRotationPoint(0.0F, 28.0F, 0.0F);
        this.rightLefDown.addBox(-5.0F, 0.0F, -5.0F, 10, 30, 10, 0.0F);
        this.setRotateAngle(rightLefDown, 0.3141592653589793F, 0.0F, 0.0F);
        this.headBackMiddle = new ModelRenderer(this, 0, 0);
        this.headBackMiddle.setRotationPoint(0.0F, -16.0F, 0.0F);
        this.headBackMiddle.addBox(-8.0F, -10.0F, -8.0F, 16, 10, 10, 0.0F);
        this.headTop = new ModelRenderer(this, 0, 0);
        this.headTop.setRotationPoint(0.0F, -10.0F, 0.0F);
        this.headTop.addBox(-5.0F, -12.0F, -8.0F, 10, 12, 16, 0.0F);
        this.headBack = new ModelRenderer(this, 0, 0);
        this.headBack.setRotationPoint(0.0F, -35.0F, 16.0F);
        this.headBack.addBox(-14.0F, -16.0F, -8.0F, 28, 16, 14, 0.0F);
        this.arm1Up_1 = new ModelRenderer(this, 0, 0);
        this.arm1Up_1.setRotationPoint(22.0F, -10.0F, -8.0F);
        this.arm1Up_1.addBox(-2.0F, -2.0F, -20.0F, 4, 4, 20, 0.0F);
        this.setRotateAngle(arm1Up_1, 0.0F, -0.6283185307179586F, 0.0F);
        this.bodyBackTop = new ModelRenderer(this, 0, 0);
        this.bodyBackTop.setRotationPoint(0.0F, 0.0F, 10.0F);
        this.bodyBackTop.addBox(-18.0F, -31.0F, 0.0F, 36, 56, 10, 0.0F);
        this.bodyBackMiddle = new ModelRenderer(this, 0, 0);
        this.bodyBackMiddle.setRotationPoint(0.0F, 0.0F, 8.0F);
        this.bodyBackMiddle.addBox(-21.0F, -35.0F, 0.0F, 42, 66, 10, 0.0F);
        this.hornFront = new ModelRenderer(this, 0, 0);
        this.hornFront.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.hornFront.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 12, 0.0F);
        this.setRotateAngle(hornFront, 0.8377580409572781F, 0.0F, 0.0F);
        this.hornFrontTop = new ModelRenderer(this, 0, 0);
        this.hornFrontTop.setRotationPoint(0.0F, 0.0F, 12.0F);
        this.hornFrontTop.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 12, 0.0F);
        this.setRotateAngle(hornFrontTop, -0.3490658503988659F, 0.0F, 0.0F);
        this.headMiddle = new ModelRenderer(this, 0, 0);
        this.headMiddle.setRotationPoint(0.0F, -16.0F, 0.0F);
        this.headMiddle.addBox(-10.0F, -10.0F, -8.0F, 20, 10, 16, 0.0F);
        this.headBase = new ModelRenderer(this, 0, 0);
        this.headBase.setRotationPoint(0.0F, -35.0F, 0.0F);
        this.headBase.addBox(-17.0F, -16.0F, -8.0F, 34, 16, 16, 0.0F);
        this.headBackTop = new ModelRenderer(this, 0, 0);
        this.headBackTop.setRotationPoint(0.0F, -10.0F, 0.0F);
        this.headBackTop.addBox(-4.0F, -12.0F, -8.0F, 6, 12, 8, 0.0F);
        this.hornBack = new ModelRenderer(this, 0, 0);
        this.hornBack.setRotationPoint(0.0F, 6.0F, 5.0F);
        this.hornBack.addBox(-1.5F, -1.5F, 0.0F, 3, 3, 20, 0.0F);
        this.setRotateAngle(hornBack, 0.22759093446006054F, 0.0F, 0.0F);
        this.arm1Down_3 = new ModelRenderer(this, 0, 0);
        this.arm1Down_3.setRotationPoint(0.0F, 0.0F, -20.0F);
        this.arm1Down_3.addBox(-2.0F, -2.0F, -24.0F, 4, 4, 24, 0.0F);
        this.setRotateAngle(arm1Down_3, -0.593411945678072F, -0.7330382858376184F, 0.0F);
        this.hornBackTop = new ModelRenderer(this, 0, 0);
        this.hornBackTop.setRotationPoint(0.0F, 0.0F, 20.0F);
        this.hornBackTop.addBox(-1.5F, -1.5F, 0.0F, 3, 3, 16, 0.0F);
        this.setRotateAngle(hornBackTop, 0.5462880558742251F, 0.0F, 0.0F);
        this.leftLegDown = new ModelRenderer(this, 0, 0);
        this.leftLegDown.setRotationPoint(0.0F, 28.0F, 0.0F);
        this.leftLegDown.addBox(-5.0F, 0.0F, -5.0F, 10, 30, 10, 0.0F);
        this.setRotateAngle(leftLegDown, 0.3141592653589793F, 0.0F, 0.0F);
        this.bodyBase = new ModelRenderer(this, 0, 0);
        this.bodyBase.setRotationPoint(0.0F, 19.0F, 0.0F);
        this.bodyBase.addBox(-25.0F, -35.0F, -8.0F, 50, 70, 16, 0.0F);
        this.setRotateAngle(bodyBase, 0.9075712110370513F, 0.0F, 0.0F);
        this.arm1Down_1 = new ModelRenderer(this, 0, 0);
        this.arm1Down_1.setRotationPoint(0.0F, 0.0F, -20.0F);
        this.arm1Down_1.addBox(-2.0F, -2.0F, -24.0F, 4, 4, 24, 0.0F);
        this.setRotateAngle(arm1Down_1, -0.593411945678072F, -0.7330382858376184F, 0.0F);
        this.bodyBase.addChild(this.rightLegUp);
        this.bodyBase.addChild(this.leftLegUp);
        this.arm1Up_1.addChild(this.arm1Down_2);
        this.arm1Up.addChild(this.arm1Down);
        this.bodyBase.addChild(this.arm1Up_2);
        this.bodyBase.addChild(this.arm2Up);
        this.bodyBase.addChild(this.arm1Up);
        this.rightLegUp.addChild(this.rightLefDown);
        this.headBack.addChild(this.headBackMiddle);
        this.headMiddle.addChild(this.headTop);
        this.bodyBase.addChild(this.headBack);
        this.bodyBase.addChild(this.arm1Up_1);
        this.bodyBackMiddle.addChild(this.bodyBackTop);
        this.bodyBase.addChild(this.bodyBackMiddle);
        this.headBackTop.addChild(this.hornFront);
        this.hornFront.addChild(this.hornFrontTop);
        this.headBase.addChild(this.headMiddle);
        this.bodyBase.addChild(this.headBase);
        this.headBackMiddle.addChild(this.headBackTop);
        this.headBackMiddle.addChild(this.hornBack);
        this.arm1Up_2.addChild(this.arm1Down_3);
        this.hornBack.addChild(this.hornBackTop);
        this.leftLegUp.addChild(this.leftLegDown);
        this.arm2Up.addChild(this.arm1Down_1);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.bodyBase.render(f5/4);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
