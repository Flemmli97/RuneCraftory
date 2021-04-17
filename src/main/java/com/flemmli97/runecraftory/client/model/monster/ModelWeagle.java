package com.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.7.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.flemmli97.runecraftory.common.entities.monster.EntityWeagle;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class ModelWeagle<T extends EntityWeagle> extends EntityModel<T> {
    private final ModelRenderer body;
    private final ModelRenderer head;
    private final ModelRenderer leftWing;
    private final ModelRenderer leftWing2;
    private final ModelRenderer leftWing3;
    private final ModelRenderer rightWing;
    private final ModelRenderer rightWing2;
    private final ModelRenderer rightWing3;

    public ModelWeagle() {
        this.textureWidth = 16;
        this.textureHeight = 16;

        this.body = new ModelRenderer(this);
        this.body.setRotationPoint(0.0F, 14.3333F, 1.0F);
        this.setRotationAngle(this.body, 0.7418F, 0.0F, 0.0F);
        this.body.setTextureOffset(0, 0).addCuboid(-4.0F, -16.3333F, -3.0F, 8.0F, 14.0F, 8.0F, 0.0F, false);
        this.body.setTextureOffset(0, 0).addCuboid(4.0F, -15.3333F, -2.0F, 1.0F, 11.0F, 6.0F, 0.0F, false);
        this.body.setTextureOffset(0, 0).addCuboid(-5.0F, -15.3333F, -2.0F, 1.0F, 11.0F, 6.0F, 0.0F, false);

        this.head = new ModelRenderer(this);
        this.head.setRotationPoint(0.0F, -18.3333F, 1.0F);
        this.body.addChild(this.head);
        this.setRotationAngle(this.head, 0.2182F, 0.0F, 0.0F);
        this.head.setTextureOffset(0, 0).addCuboid(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, 0.0F, false);
        this.head.setTextureOffset(0, 0).addCuboid(0.0F, -3.0F, 3.0F, 0.0F, 5.0F, 3.0F, 0.0F, false);
        this.head.setTextureOffset(0, 0).addCuboid(0.0F, -2.0F, -5.0F, 0.0F, 4.0F, 2.0F, 0.0F, false);
        this.head.setTextureOffset(0, 0).addCuboid(-2.0F, -4.0F, -2.0F, 4.0F, 1.0F, 4.0F, 0.0F, false);
        this.head.setTextureOffset(0, 0).addCuboid(-1.0F, -5.0F, -2.0F, 2.0F, 1.0F, 2.0F, 0.0F, false);

        this.leftWing = new ModelRenderer(this);
        this.leftWing.setRotationPoint(2.0F, -13.3333F, 1.0F);
        this.body.addChild(this.leftWing);
        this.setRotationAngle(this.leftWing, 0.0F, 0.2182F, 0.0F);
        this.leftWing.setTextureOffset(0, 0).addCuboid(3.0F, -1.0F, 0.0F, 4.0F, 1.0F, 1.0F, 0.0F, false);
        this.leftWing.setTextureOffset(0, 0).addCuboid(3.0F, 0.0F, 0.5F, 4.0F, 6.0F, 0.0F, 0.0F, false);

        this.leftWing2 = new ModelRenderer(this);
        this.leftWing2.setRotationPoint(7.0F, -1.0F, 0.0F);
        this.leftWing.addChild(this.leftWing2);
        this.leftWing2.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, 0.0F, 6.0F, 1.0F, 1.0F, 0.0F, false);
        this.leftWing2.setTextureOffset(0, 0).addCuboid(0.0F, 1.0F, 0.5F, 6.0F, 8.0F, 0.0F, 0.0F, false);

        this.leftWing3 = new ModelRenderer(this);
        this.leftWing3.setRotationPoint(6.0F, 0.0F, 1.0F);
        this.leftWing2.addChild(this.leftWing3);
        this.leftWing3.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, -1.0F, 10.0F, 1.0F, 1.0F, 0.0F, false);
        this.leftWing3.setTextureOffset(0, 0).addCuboid(0.0F, 1.0F, -0.5F, 10.0F, 8.0F, 0.0F, 0.0F, false);

        this.rightWing = new ModelRenderer(this);
        this.rightWing.setRotationPoint(-4.6667F, -4.8333F, 0.6667F);
        this.body.addChild(this.rightWing);
        this.setRotationAngle(this.rightWing, 0.0F, -0.2618F, 0.0F);
        this.rightWing.setTextureOffset(0, 0).addCuboid(-4.3333F, -9.5F, 0.3333F, 4.0F, 1.0F, 1.0F, 0.0F, false);
        this.rightWing.setTextureOffset(0, 0).addCuboid(-4.3333F, -8.5F, 0.8333F, 4.0F, 6.0F, 0.0F, 0.0F, false);

        this.rightWing2 = new ModelRenderer(this);
        this.rightWing2.setRotationPoint(-10.3333F, -9.5F, 0.3333F);
        this.rightWing.addChild(this.rightWing2);
        this.rightWing2.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, 0.0F, 6.0F, 1.0F, 1.0F, 0.0F, false);
        this.rightWing2.setTextureOffset(0, 0).addCuboid(0.0F, 1.0F, 0.5F, 6.0F, 8.0F, 0.0F, 0.0F, false);

        this.rightWing3 = new ModelRenderer(this);
        this.rightWing3.setRotationPoint(-10.0F, 0.0F, 1.0F);
        this.rightWing2.addChild(this.rightWing3);
        this.rightWing3.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, -1.0F, 10.0F, 1.0F, 1.0F, 0.0F, false);
        this.rightWing3.setTextureOffset(0, 0).addCuboid(0.0F, 1.0F, -0.5F, 10.0F, 8.0F, 0.0F, 0.0F, false);
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