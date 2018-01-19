package com.flemmli97.runecraftory.client.models.monsters;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * ModelOrc
 * Created using Tabula 7.0.0
 */
public class ModelOrc extends ModelBase {

	public ModelRenderer body;
    public ModelRenderer legUpLeft;
    public ModelRenderer shape3;
    public ModelRenderer legUpLeft_1;
    public ModelRenderer shape11;
    public ModelRenderer shape11_1;
    public ModelRenderer shape9;
    public ModelRenderer shape4;
    public ModelRenderer shape5;
    public ModelRenderer shape5_1;
    public ModelRenderer shape9_1;
    public ModelRenderer shape12;
    public ModelRenderer shape12_1;
    public ModelRenderer shape15;
    public ModelRenderer shape16;

    public ModelOrc() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.shape11 = new ModelRenderer(this, 0, 0);
        this.shape11.setRotationPoint(20.0F, -14.0F, 0.0F);
        this.shape11.addBox(0.0F, -7.0F, -7.0F, 26, 14, 14, 0.0F);
        this.shape5 = new ModelRenderer(this, 0, 0);
        this.shape5.setRotationPoint(10.0F, -44.0F, -16.0F);
        this.shape5.addBox(-8.0F, -16.0F, -1.0F, 16, 16, 3, 0.0F);
        this.shape12 = new ModelRenderer(this, 0, 0);
        this.shape12.setRotationPoint(28.0F, 0.0F, 0.0F);
        this.shape12.addBox(-2.0F, -7.0F, -35.0F, 14, 14, 42, 0.0F);
        this.shape15 = new ModelRenderer(this, 0, 0);
        this.shape15.setRotationPoint(-3.0F, -7.0F, -28.0F);
        this.shape15.addBox(-4.0F, -36.0F, -4.0F, 8, 36, 8, 0.0F);
        this.shape3 = new ModelRenderer(this, 0, 0);
        this.shape3.setRotationPoint(0.0F, -40.0F, 0.0F);
        this.shape3.addBox(-22.0F, -44.0F, -22.0F, 44, 44, 44, 0.0F);
        this.shape4 = new ModelRenderer(this, 0, 0);
        this.shape4.setRotationPoint(0.0F, -10.0F, -22.0F);
        this.shape4.addBox(-10.0F, -10.0F, -20.0F, 20, 20, 20, 0.0F);
        this.shape12_1 = new ModelRenderer(this, 0, 0);
        this.shape12_1.setRotationPoint(-28.0F, 0.0F, 0.0F);
        this.shape12_1.addBox(-10.0F, -7.0F, -35.0F, 14, 14, 42, 0.0F);
        this.shape9_1 = new ModelRenderer(this, 0, 0);
        this.shape9_1.setRotationPoint(0.0F, 27.0F, 0.0F);
        this.shape9_1.addBox(-7.0F, 0.0F, -7.0F, 14, 20, 14, 0.0F);
        this.setRotateAngle(shape9_1, 0.3490658503988659F, 0.0F, 0.0F);
        this.shape5_1 = new ModelRenderer(this, 0, 0);
        this.shape5_1.setRotationPoint(-10.0F, -44.0F, -16.0F);
        this.shape5_1.addBox(-8.0F, -16.0F, -1.0F, 16, 16, 3, 0.0F);
        this.legUpLeft = new ModelRenderer(this, 0, 0);
        this.legUpLeft.setRotationPoint(13.0F, 38.0F, 0.0F);
        this.legUpLeft.addBox(-7.0F, 0.0F, -7.0F, 14, 30, 14, 0.0F);
        this.setRotateAngle(legUpLeft, -0.5235987755982988F, 0.0F, 0.0F);
        this.shape11_1 = new ModelRenderer(this, 0, 0);
        this.shape11_1.setRotationPoint(-20.0F, -14.0F, 0.0F);
        this.shape11_1.addBox(-24.0F, -7.0F, -7.0F, 26, 14, 14, 0.0F);
        this.shape16 = new ModelRenderer(this, 0, 0);
        this.shape16.setRotationPoint(0.0F, -36.0F, 0.0F);
        this.shape16.addBox(-8.0F, -16.0F, -8.0F, 16, 16, 16, 0.0F);
        this.shape9 = new ModelRenderer(this, 0, 0);
        this.shape9.setRotationPoint(0.0F, 27.0F, 0.0F);
        this.shape9.addBox(-7.0F, 0.0F, -7.0F, 14, 20, 14, 0.0F);
        this.setRotateAngle(shape9, 0.3490658503988659F, 0.0F, 0.0F);
        this.body = new ModelRenderer(this, 0, 0);
        this.body.setRotationPoint(0.0F, 50.0F, 0.0F);
        this.body.addBox(-20.0F, -40.0F, -20.0F, 40, 80, 40, 0.0F);
        this.setRotateAngle(body, 0.17453292519943295F, 0.0F, 0.0F);
        this.legUpLeft_1 = new ModelRenderer(this, 0, 0);
        this.legUpLeft_1.setRotationPoint(-13.0F, 38.0F, 0.0F);
        this.legUpLeft_1.addBox(-7.0F, 0.0F, -7.0F, 14, 30, 14, 0.0F);
        this.setRotateAngle(legUpLeft_1, -0.5235987755982988F, 0.0F, 0.0F);
        this.body.addChild(this.shape11);
        this.shape3.addChild(this.shape5);
        this.shape11.addChild(this.shape12);
        this.shape12_1.addChild(this.shape15);
        this.body.addChild(this.shape3);
        this.shape3.addChild(this.shape4);
        this.shape11_1.addChild(this.shape12_1);
        this.legUpLeft_1.addChild(this.shape9_1);
        this.shape3.addChild(this.shape5_1);
        this.body.addChild(this.legUpLeft);
        this.body.addChild(this.shape11_1);
        this.shape15.addChild(this.shape16);
        this.legUpLeft.addChild(this.shape9);
        this.body.addChild(this.legUpLeft_1);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.body.render(f5/5);
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
