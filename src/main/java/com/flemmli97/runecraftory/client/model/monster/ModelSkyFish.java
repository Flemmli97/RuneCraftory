package com.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.7.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.flemmli97.runecraftory.common.entities.monster.EntitySkyFish;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class ModelSkyFish<T extends EntitySkyFish> extends EntityModel<T> {
    private final ModelRenderer body;

    public ModelSkyFish() {
        this.textureWidth = 16;
        this.textureHeight = 16;

        this.body = new ModelRenderer(this);
        this.body.setRotationPoint(0.5F, 18.0F, 1.0F);
        this.body.setTextureOffset(0, 0).addCuboid(-3.5F, -8.0F, -9.0F, 6.0F, 11.0F, 17.0F, 0.0F, false);
        this.body.setTextureOffset(0, 0).addCuboid(2.5F, -6.0F, -8.0F, 1.0F, 7.0F, 16.0F, 0.0F, false);
        this.body.setTextureOffset(0, 0).addCuboid(-4.5F, -6.0F, -8.0F, 1.0F, 7.0F, 16.0F, 0.0F, false);
        this.body.setTextureOffset(0, 0).addCuboid(-2.5F, -9.0F, -7.0F, 4.0F, 1.0F, 14.0F, 0.0F, false);
        this.body.setTextureOffset(0, 0).addCuboid(-3.5F, -7.0F, -10.0F, 6.0F, 9.0F, 1.0F, 0.0F, false);
        this.body.setTextureOffset(0, 0).addCuboid(-3.5F, -5.5F, -11.0F, 6.0F, 6.0F, 1.0F, 0.0F, false);
        this.body.setTextureOffset(0, 0).addCuboid(-3.5F, -6.5F, 8.0F, 6.0F, 8.0F, 4.0F, 0.0F, false);
        this.body.setTextureOffset(0, 0).addCuboid(2.5F, -5.0F, 8.0F, 1.0F, 5.0F, 3.0F, 0.0F, false);
        this.body.setTextureOffset(0, 0).addCuboid(-4.5F, -5.0F, 8.0F, 1.0F, 5.0F, 3.0F, 0.0F, false);
        this.body.setTextureOffset(0, 0).addCuboid(-2.5F, -4.5F, 12.0F, 4.0F, 5.0F, 4.0F, 0.0F, false);
        this.body.setTextureOffset(0, 0).addCuboid(-0.5F, -12.0F, -7.0F, 0.0F, 3.0F, 5.0F, 0.0F, false);
        this.body.setTextureOffset(0, 0).addCuboid(-0.5F, -12.0F, 3.0F, 0.0F, 3.0F, 4.0F, 0.0F, false);
        this.body.setTextureOffset(0, 0).addCuboid(3.5F, 0.0F, -6.0F, 1.0F, 3.0F, 4.0F, 0.0F, false);
        this.body.setTextureOffset(0, 0).addCuboid(-5.5F, 0.0F, -6.0F, 1.0F, 3.0F, 4.0F, 0.0F, false);
        this.body.setTextureOffset(0, 0).addCuboid(3.5F, 0.0F, 4.0F, 1.0F, 3.0F, 4.0F, 0.0F, false);
        this.body.setTextureOffset(0, 0).addCuboid(-5.5F, 0.0F, 4.0F, 1.0F, 3.0F, 4.0F, 0.0F, false);
        this.body.setTextureOffset(0, 0).addCuboid(-0.5F, -10.5F, 16.0F, 0.0F, 15.0F, 9.0F, 0.0F, false);
    }

    @Override
    public void setAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        //previously the render function, render code was moved to a method below
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.body.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}