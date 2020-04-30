package com.flemmli97.runecraftory.client.models;

import com.flemmli97.tenshilib.common.javahelper.MathUtils;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

/**
 * Butterfly - Undefined
 * Created using Tabula 7.0.0
 */
public class ModelButterfly extends ModelBase {
    public ModelRenderer body;
    public ModelRenderer wingLeft;
    public ModelRenderer wingRight;

    public ModelButterfly() {
        this.textureWidth = 32;
        this.textureHeight = 32;
        this.wingLeft = new ModelRenderer(this, 0, 6);
        this.wingLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.wingLeft.addBox(0.0F, 0.0F, -2.5F, 4, 1, 5, 0.0F);
        this.setRotateAngle(wingLeft, 0.0F, 0.0F, -0.6108652381980153F);
        this.wingRight = new ModelRenderer(this, 0, 0);
        this.wingRight.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.wingRight.addBox(-4.0F, 0.0F, -2.5F, 4, 1, 5, 0.0F);
        this.wingRight.mirror=true;
        this.setRotateAngle(wingRight, 0.0F, 0.0F, 0.6108652381980153F);
        this.body = new ModelRenderer(this, 0, 12);
        this.body.setRotationPoint(0.0F, -3F, 0.0F);
        this.body.addBox(-0.5F, 0.0F, -3.0F, 1, 1, 6, 0.0F);
        this.body.addChild(this.wingLeft);
        this.body.addChild(this.wingRight);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.body.render(f5);
    }

    
    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        this.wingLeft.rotateAngleZ=-MathHelper.cos(ageInTicks * 1.2F) * MathUtils.degToRad(20)-MathUtils.degToRad(30);
        this.wingRight.rotateAngleZ=MathHelper.cos(ageInTicks * 1.2F) * MathUtils.degToRad(20)+MathUtils.degToRad(30);
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
