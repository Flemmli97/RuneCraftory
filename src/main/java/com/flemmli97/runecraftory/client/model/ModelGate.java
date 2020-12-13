package com.flemmli97.runecraftory.client.model;

import com.flemmli97.runecraftory.common.entities.GateEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

/**
 * Created using Tabula 7.0.0
 */
public class ModelGate extends EntityModel<GateEntity> {
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
        this.shape2_2.addCuboid(8.0F, -6.0F, -6.0F, 2, 13, 13, 0.0F);
        this.shape2 = new ModelRenderer(this, 0, 31);
        this.shape2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.shape2.addCuboid(-6.0F, 8.0F, -6.0F, 13, 2, 13, 0.0F);
        this.shape2_4 = new ModelRenderer(this, 53, 31);
        this.shape2_4.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.shape2_4.addCuboid(-6.0F, -6.0F, 8.0F, 13, 13, 2, 0.0F);
        this.core = new ModelRenderer(this, 0, 0);
        this.core.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.core.addCuboid(-7.0F, -7.0F, -7.0F, 15, 15, 15, 0.0F);
        this.shape2_1 = new ModelRenderer(this, 0, 31);
        this.shape2_1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.shape2_1.addCuboid(-6.0F, -9.0F, -6.0F, 13, 2, 13, 0.0F);
        this.shape2_3 = new ModelRenderer(this, 61, 4);
        this.shape2_3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.shape2_3.addCuboid(-9.0F, -6.0F, -6.0F, 2, 13, 13, 0.0F);
        this.shape2_5 = new ModelRenderer(this, 53, 31);
        this.shape2_5.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.shape2_5.addCuboid(-6.0F, -6.0F, -9.0F, 13, 13, 2, 0.0F);
        this.core.addChild(this.shape2_2);
        this.core.addChild(this.shape2);
        this.core.addChild(this.shape2_4);
        this.core.addChild(this.shape2_1);
        this.core.addChild(this.shape2_3);
        this.core.addChild(this.shape2_5);
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.core.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public void setAngles(GateEntity gate, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.core.rotateAngleY = gate.rotate * ageInTicks / 50.0F;
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
