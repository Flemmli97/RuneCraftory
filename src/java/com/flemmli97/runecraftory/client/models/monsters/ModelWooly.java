package com.flemmli97.runecraftory.client.models.monsters;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * ModelWooly
 * Created using Tabula 7.0.0
 */
public class ModelWooly extends ModelBase {
    public ModelRenderer bodyBase;
    public ModelRenderer bodyUp;
    public ModelRenderer bodyBack;
    public ModelRenderer bodyLeft;
    public ModelRenderer bodyFront;
    public ModelRenderer legLeft;
    public ModelRenderer bodyLeft_1;
    public ModelRenderer tail;
    public ModelRenderer legLeft_1;
    public ModelRenderer armLeft;
    public ModelRenderer armLeft_1;
    public ModelRenderer neck;
    public ModelRenderer headBase;
    public ModelRenderer headTop;
    public ModelRenderer headRight;
    public ModelRenderer headLeft;
    public ModelRenderer earRight;
    public ModelRenderer earRight_1;
    public ModelRenderer footLeft;
    public ModelRenderer footLeft_1;
    public ModelRenderer armJoint;
    public ModelRenderer handLeft;
    public ModelRenderer armJoint_1;
    public ModelRenderer handLeft_1;

    public ModelWooly() {
        this.textureWidth = 256;
        this.textureHeight = 256;
        this.armLeft = new ModelRenderer(this, 190, 53);
        this.armLeft.setRotationPoint(23.0F, 0.0F, 0.0F);
        this.armLeft.addBox(0.0F, -3.0F, -18.0F, 6, 6, 21, 0.0F);
        this.setRotateAngle(armLeft, 0.17453292519943295F, -0.03490658503988659F, 0.0F);
        this.earRight = new ModelRenderer(this, 0, 49);
        this.earRight.mirror = true;
        this.earRight.setRotationPoint(-17.0F, 0.0F, 0.0F);
        this.earRight.addBox(-16.0F, -2.0F, -5.5F, 16, 4, 11, 0.0F);
        this.setRotateAngle(earRight, 0.0F, 0.0F, -0.1890191579909859F);
        this.bodyBase = new ModelRenderer(this, 0, 101);
        this.bodyBase.setRotationPoint(0.0F, 68.0F, 0.0F);
        this.bodyBase.addBox(-19.0F, -14.0F, -19.0F, 38, 28, 38, 0.0F);
        this.bodyBack = new ModelRenderer(this, 153, 108);
        this.bodyBack.mirror = true;
        this.bodyBack.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bodyBack.addBox(-16.0F, -11.0F, 19.0F, 32, 22, 4, 0.0F);
        this.handLeft_1 = new ModelRenderer(this, 190, 81);
        this.handLeft_1.mirror = true;
        this.handLeft_1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.handLeft_1.addBox(-3.0F, -3.0F, -12.0F, 6, 6, 12, 0.0F);
        this.setRotateAngle(handLeft_1, 0.22759093446006054F, -0.5462880558742251F, 0.0F);
        this.legLeft_1 = new ModelRenderer(this, 0, 168);
        this.legLeft_1.mirror = true;
        this.legLeft_1.setRotationPoint(-8.0F, 14.0F, 0.0F);
        this.legLeft_1.addBox(-3.0F, 0.0F, -3.0F, 6, 9, 6, 0.0F);
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
        this.setRotateAngle(handLeft, 0.22759093446006054F, 0.5462880558742251F, 0.0F);
        this.earRight_1 = new ModelRenderer(this, 0, 49);
        this.earRight_1.setRotationPoint(17.0F, 0.0F, 0.0F);
        this.earRight_1.addBox(0.0F, -2.0F, -5.5F, 16, 4, 11, 0.0F);
        this.setRotateAngle(earRight_1, 0.0F, 0.0F, 0.17453292519943295F);
        this.armLeft_1 = new ModelRenderer(this, 190, 53);
        this.armLeft_1.mirror = true;
        this.armLeft_1.setRotationPoint(-23.0F, 0.0F, 0.0F);
        this.armLeft_1.addBox(-6.0F, -3.0F, -18.0F, 6, 6, 21, 0.0F);
        this.setRotateAngle(armLeft_1, 0.17453292519943295F, 0.03490658503988659F, 0.0F);
        this.bodyLeft = new ModelRenderer(this, 117, 53);
        this.bodyLeft.mirror = true;
        this.bodyLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bodyLeft.addBox(-23.0F, -11.0F, -16.0F, 4, 22, 32, 0.0F);
        this.bodyFront = new ModelRenderer(this, 153, 108);
        this.bodyFront.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bodyFront.addBox(-16.0F, -11.0F, -23.0F, 32, 22, 4, 0.0F);
        this.bodyLeft_1 = new ModelRenderer(this, 117, 53);
        this.bodyLeft_1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bodyLeft_1.addBox(19.0F, -11.0F, -16.0F, 4, 22, 32, 0.0F);
        this.armJoint_1 = new ModelRenderer(this, 0, 0);
        this.armJoint_1.setRotationPoint(-3.0F, 0.0F, -18.0F);
        this.armJoint_1.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.setRotateAngle(armJoint_1, -0.17453292519943295F, 0.03490658503988659F, 0.0F);
        this.headLeft = new ModelRenderer(this, 202, 0);
        this.headLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.headLeft.addBox(14.0F, -8.0F, -11.0F, 4, 16, 21, 0.0F);
        this.footLeft_1 = new ModelRenderer(this, 25, 168);
        this.footLeft_1.mirror = true;
        this.footLeft_1.setRotationPoint(0.0F, 9.0F, 0.0F);
        this.footLeft_1.addBox(-3.0F, 0.0F, -22.0F, 6, 5, 25, 0.0F);
        this.headTop = new ModelRenderer(this, 112, 0);
        this.headTop.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.headTop.addBox(-11.0F, -14.0F, -11.0F, 22, 4, 22, 0.0F);
        this.legLeft = new ModelRenderer(this, 0, 168);
        this.legLeft.setRotationPoint(8.0F, 14.0F, 0.0F);
        this.legLeft.addBox(-3.0F, 0.0F, -3.0F, 6, 9, 6, 0.0F);
        this.tail = new ModelRenderer(this, 89, 168);
        this.tail.setRotationPoint(0.0F, 2.0F, 23.0F);
        this.tail.addBox(-4.0F, -4.0F, 0.0F, 8, 7, 8, 0.0F);
        this.footLeft = new ModelRenderer(this, 25, 168);
        this.footLeft.setRotationPoint(0.0F, 9.0F, 0.0F);
        this.footLeft.addBox(-3.0F, 0.0F, -22.0F, 6, 5, 25, 0.0F);
        this.neck = new ModelRenderer(this, 113, 27);
        this.neck.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.neck.addBox(-10.0F, -25.0F, -10.0F, 20, 5, 20, 0.0F);
        this.armJoint = new ModelRenderer(this, 0, 0);
        this.armJoint.setRotationPoint(3.0F, 0.0F, -18.0F);
        this.armJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.setRotateAngle(armJoint, -0.17453292519943295F, 0.03490658503988659F, 0.0F);
        this.bodyBase.addChild(this.armLeft);
        this.headRight.addChild(this.earRight);
        this.bodyBase.addChild(this.bodyBack);
        this.armJoint_1.addChild(this.handLeft_1);
        this.bodyBase.addChild(this.legLeft_1);
        this.headBase.addChild(this.headRight);
        this.bodyBase.addChild(this.bodyUp);
        this.neck.addChild(this.headBase);
        this.armJoint.addChild(this.handLeft);
        this.headLeft.addChild(this.earRight_1);
        this.bodyBase.addChild(this.armLeft_1);
        this.bodyBase.addChild(this.bodyLeft);
        this.bodyBase.addChild(this.bodyFront);
        this.bodyBase.addChild(this.bodyLeft_1);
        this.armLeft_1.addChild(this.armJoint_1);
        this.headBase.addChild(this.headLeft);
        this.legLeft_1.addChild(this.footLeft_1);
        this.headBase.addChild(this.headTop);
        this.bodyBase.addChild(this.legLeft);
        this.bodyBase.addChild(this.tail);
        this.legLeft.addChild(this.footLeft);
        this.bodyUp.addChild(this.neck);
        this.armLeft.addChild(this.armJoint);
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
