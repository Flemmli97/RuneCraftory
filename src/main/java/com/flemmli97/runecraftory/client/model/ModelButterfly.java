package com.flemmli97.runecraftory.client.model;

import com.flemmli97.runecraftory.common.entities.misc.EntityButterfly;
import com.flemmli97.tenshilib.common.utils.MathUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

/**
 * Butterfly - Undefined
 * Created using Tabula 7.0.0
 */
public class ModelButterfly<T extends EntityButterfly> extends EntityModel<T> {

    public ModelRenderer body;
    public ModelRenderer wingLeft;
    public ModelRenderer wingRight;

    public ModelButterfly() {
        this.textureWidth = 32;
        this.textureHeight = 32;
        this.wingLeft = new ModelRenderer(this, 0, 6);
        this.wingLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.wingLeft.addCuboid(0.0F, 0.0F, -2.5F, 4, 1, 5, 0.0F);
        this.setRotateAngle(this.wingLeft, 0.0F, 0.0F, -0.6108652381980153F);
        this.wingRight = new ModelRenderer(this, 0, 0);
        this.wingRight.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.wingRight.addCuboid(-4.0F, 0.0F, -2.5F, 4, 1, 5, 0.0F);
        this.wingRight.mirror = true;
        this.setRotateAngle(this.wingRight, 0.0F, 0.0F, 0.6108652381980153F);
        this.body = new ModelRenderer(this, 0, 12);
        this.body.setRotationPoint(0.0F, -3F, 0.0F);
        this.body.addCuboid(-0.5F, 0.0F, -3.0F, 1, 1, 6, 0.0F);
        this.body.addChild(this.wingLeft);
        this.body.addChild(this.wingRight);
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.body.render(matrixStack, buffer, packedLight, packedOverlay);
    }


    @Override
    public void setAngles(T butterfly, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.wingLeft.rotateAngleZ = -MathHelper.cos(ageInTicks * 1.2F) * MathUtils.degToRad(20) - MathUtils.degToRad(30);
        this.wingRight.rotateAngleZ = MathHelper.cos(ageInTicks * 1.2F) * MathUtils.degToRad(20) + MathUtils.degToRad(30);
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