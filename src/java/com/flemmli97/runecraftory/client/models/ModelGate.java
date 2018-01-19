package com.flemmli97.runecraftory.client.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * Created using Tabula 7.0.0
 */
public class ModelGate extends ModelBase {
    public ModelRenderer core;
    public ModelRenderer shape2;
    public ModelRenderer shape2_1;
    public ModelRenderer shape2_2;
    public ModelRenderer shape2_3;
    public ModelRenderer shape2_4;
    public ModelRenderer shape2_5;

    public ModelGate() {
        this.textureWidth = 95;
        this.textureHeight = 48;
        this.shape2_2 = new ModelRenderer(this, 61, 4);
        this.shape2_2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.shape2_2.addBox(8.0F, -6.0F, -6.0F, 2, 13, 13, 0.0F);
        this.shape2 = new ModelRenderer(this, 0, 31);
        this.shape2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.shape2.addBox(-6.0F, 8.0F, -6.0F, 13, 2, 13, 0.0F);
        this.shape2_4 = new ModelRenderer(this, 53, 31);
        this.shape2_4.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.shape2_4.addBox(-6.0F, -6.0F, 8.0F, 13, 13, 2, 0.0F);
        this.core = new ModelRenderer(this, 0, 0);
        this.core.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.core.addBox(-7.0F, -7.0F, -7.0F, 15, 15, 15, 0.0F);
        this.shape2_1 = new ModelRenderer(this, 0, 31);
        this.shape2_1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.shape2_1.addBox(-6.0F, -9.0F, -6.0F, 13, 2, 13, 0.0F);
        this.shape2_3 = new ModelRenderer(this, 61, 4);
        this.shape2_3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.shape2_3.addBox(-9.0F, -6.0F, -6.0F, 2, 13, 13, 0.0F);
        this.shape2_5 = new ModelRenderer(this, 53, 31);
        this.shape2_5.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.shape2_5.addBox(-6.0F, -6.0F, -9.0F, 13, 13, 2, 0.0F);
        this.core.addChild(this.shape2_2);
        this.core.addChild(this.shape2);
        this.core.addChild(this.shape2_4);
        this.core.addChild(this.shape2_1);
        this.core.addChild(this.shape2_3);
        this.core.addChild(this.shape2_5);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.core.render(f5);
    }
    
    

    @Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch, float scaleFactor, Entity entityIn) {
		this.core.rotateAngleY=ageInTicks/50.0F;
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
