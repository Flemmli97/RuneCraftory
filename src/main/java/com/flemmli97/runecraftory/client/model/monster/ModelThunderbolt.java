package com.flemmli97.runecraftory.client.model.monster;

import com.flemmli97.runecraftory.common.entities.monster.boss.EntityThunderbolt;
import com.flemmli97.tenshilib.client.model.IResetModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class ModelThunderbolt<T extends EntityThunderbolt> extends EntityModel<T> implements IResetModel {

    private final ModelRenderer body;
    private final ModelRenderer tail;
    private final ModelRenderer neck;
    private final ModelRenderer bone;
    private final ModelRenderer leftFrontLeg;
    private final ModelRenderer rightFrontLeg;
    private final ModelRenderer leftBackLeg;
    private final ModelRenderer rightBackLeg;

    public ModelThunderbolt() {
        textureWidth = 16;
        textureHeight = 16;

        body = new ModelRenderer(this);
        body.setRotationPoint(4.5F, 20.0F, -13.0F);
        body.setTextureOffset(0, 0).addCuboid(-11.0F, -23.0F, 0.0F, 13.0F, 13.0F, 26.0F, 0.0F, false);
        body.setTextureOffset(0, 0).addCuboid(-5.0F, -26.0F, 4.0F, 1.0F, 4.0F, 24.0F, 0.0F, false);

        tail = new ModelRenderer(this);
        tail.setRotationPoint(-4.0F, -22.0F, 26.0F);
        body.addChild(tail);
        setRotationAngle(tail, 0.7854F, 0.0F, 0.0F);
        tail.setTextureOffset(0, 0).addCuboid(-3.0F, 0.0F, -1.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
        tail.setTextureOffset(0, 0).addCuboid(-1.0F, -1.0F, 0.0F, 1.0F, 13.0F, 4.0F, 0.0F, false);

        neck = new ModelRenderer(this);
        neck.setRotationPoint(-4.0F, -18.0F, 0.0F);
        body.addChild(neck);
        setRotationAngle(neck, 0.6109F, 0.0F, 0.0F);
        neck.setTextureOffset(0, 0).addCuboid(-4.0F, -11.0F, 0.0F, 8.0F, 12.0F, 9.0F, 0.0F, false);

        bone = new ModelRenderer(this);
        bone.setRotationPoint(0.0F, -13.0F, 8.0F);
        neck.addChild(bone);
        bone.setTextureOffset(0, 0).addCuboid(-6.0F, -10.0F, -12.0F, 12.0F, 12.0F, 15.0F, 0.0F, false);
        bone.setTextureOffset(0, 0).addCuboid(-1.0F, -13.0F, -5.0F, 1.0F, 15.0F, 11.0F, 0.0F, false);
        bone.setTextureOffset(0, 0).addCuboid(-1.0F, 2.0F, -5.0F, 1.0F, 15.0F, 11.0F, 0.0F, false);
        bone.setTextureOffset(0, 0).addCuboid(-2.0F, -21.0F, -10.0F, 3.0F, 11.0F, 3.0F, 0.0F, false);
        bone.setTextureOffset(0, 0).addCuboid(-4.0F, -10.0F, -15.0F, 8.0F, 12.0F, 3.0F, 0.0F, false);
        bone.setTextureOffset(0, 0).addCuboid(-3.0F, -9.0F, -22.0F, 6.0F, 10.0F, 7.0F, 0.0F, false);

        leftFrontLeg = new ModelRenderer(this);
        leftFrontLeg.setRotationPoint(0.0F, -11.0F, 0.0F);
        body.addChild(leftFrontLeg);
        leftFrontLeg.setTextureOffset(0, 0).addCuboid(-1.0F, 1.0F, 0.0F, 3.0F, 6.0F, 3.0F, 0.0F, false);
        leftFrontLeg.setTextureOffset(0, 0).addCuboid(-1.0F, 7.0F, 0.0F, 3.0F, 6.0F, 3.0F, 0.0F, false);
        leftFrontLeg.setTextureOffset(0, 0).addCuboid(-1.5F, 13.0F, -0.5F, 4.0F, 2.0F, 4.0F, 0.0F, false);

        rightFrontLeg = new ModelRenderer(this);
        rightFrontLeg.setRotationPoint(-10.0F, -11.0F, 0.0F);
        body.addChild(rightFrontLeg);
        rightFrontLeg.setTextureOffset(0, 0).addCuboid(-1.0F, 1.0F, 0.0F, 3.0F, 6.0F, 3.0F, 0.0F, false);
        rightFrontLeg.setTextureOffset(0, 0).addCuboid(-1.0F, 7.0F, 0.0F, 3.0F, 6.0F, 3.0F, 0.0F, false);
        rightFrontLeg.setTextureOffset(0, 0).addCuboid(-1.5F, 13.0F, -0.5F, 4.0F, 2.0F, 4.0F, 0.0F, false);

        leftBackLeg = new ModelRenderer(this);
        leftBackLeg.setRotationPoint(0.0F, -11.0F, 23.0F);
        body.addChild(leftBackLeg);
        leftBackLeg.setTextureOffset(0, 0).addCuboid(-1.0F, 1.0F, 0.0F, 3.0F, 6.0F, 3.0F, 0.0F, false);
        leftBackLeg.setTextureOffset(0, 0).addCuboid(-1.0F, 7.0F, 0.0F, 3.0F, 6.0F, 3.0F, 0.0F, false);
        leftBackLeg.setTextureOffset(0, 0).addCuboid(-1.5F, 13.0F, -0.5F, 4.0F, 2.0F, 4.0F, 0.0F, false);

        rightBackLeg = new ModelRenderer(this);
        rightBackLeg.setRotationPoint(-9.0F, -11.0F, 23.0F);
        body.addChild(rightBackLeg);
        rightBackLeg.setTextureOffset(0, 0).addCuboid(-1.0F, 1.0F, 0.0F, 3.0F, 6.0F, 3.0F, 0.0F, false);
        rightBackLeg.setTextureOffset(0, 0).addCuboid(-1.0F, 7.0F, 0.0F, 3.0F, 6.0F, 3.0F, 0.0F, false);
        rightBackLeg.setTextureOffset(0, 0).addCuboid(-1.5F, 13.0F, -0.5F, 4.0F, 2.0F, 4.0F, 0.0F, false);
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.body.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public void setAngles(T thunderbolt, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetModel();

        /*AnimatedAction anim = thunderbolt.getAnimation();
        float partialTicks = Minecraft.getInstance().getRenderPartialTicks();
        if (anim == null) {
            if (thunderbolt.isMoving()) {
                this.animations.doAnimation("fly", thunderbolt.ticksExisted, partialTicks);
            } else
                this.animations.doAnimation("flyIddle", thunderbolt.ticksExisted, partialTicks);
        } else
            this.animations.doAnimation(anim.getID(), anim.getTick(), partialTicks);*/
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
        //modelRenderer.setDefaultRotAngle(x, y, z);
    }

    @Override
    public void resetModel() {
        //this.body.reset();
        //this.resetChild(this.body);
    }
}
