package com.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.entities.monster.EntityCluckadoodle;
import com.flemmli97.tenshilib.client.model.BlockBenchAnimations;
import com.flemmli97.tenshilib.client.model.IResetModel;
import com.flemmli97.tenshilib.client.model.ModelRendererPlus;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;

public class ModelCluckadoodle<T extends EntityCluckadoodle> extends EntityModel<T> implements IResetModel {
    public ModelRendererPlus body;
    public ModelRendererPlus neck;
    public ModelRendererPlus head;
    public ModelRendererPlus comb;
    public ModelRendererPlus beak;
    public ModelRendererPlus backFeathersLeft;
    public ModelRendererPlus backFeathersLeftUp;
    public ModelRendererPlus backFeathersRight;
    public ModelRendererPlus backFeathersRightUp;
    public ModelRendererPlus legLeft;
    public ModelRendererPlus legRight;
    public ModelRendererPlus wingLeft;
    public ModelRendererPlus wingRight;

    public BlockBenchAnimations animations;

    public ModelCluckadoodle() {
        this.textureWidth = 66;
        this.textureHeight = 32;

        this.body = new ModelRendererPlus(this);
        this.body.setDefaultRotPoint(0.0F, 15.5F, 1.3333F);
        this.body.setTextureOffset(32, 15).addCuboid(-3.5F, -3.5F, -5.3333F, 7.0F, 7.0F, 10.0F, 0.0F, false);

        this.neck = new ModelRendererPlus(this);
        this.neck.setDefaultRotPoint(0.0F, -2.5F, -5.3333F);
        this.body.addChild(this.neck);
        this.neck.setTextureOffset(0, 16).addCuboid(-2.0F, 0.0F, -1.0F, 4.0F, 3.0F, 2.0F, 0.0F, false);

        this.head = new ModelRendererPlus(this);
        this.head.setDefaultRotPoint(0.0F, 0.0F, 0.0F);
        this.neck.addChild(this.head);
        this.head.setTextureOffset(0, 0).addCuboid(-2.0F, -6.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);

        this.comb = new ModelRendererPlus(this);
        this.comb.setDefaultRotPoint(0.0F, 9.0F, 4.0F);
        this.head.addChild(this.comb);
        this.comb.setTextureOffset(20, 11).addCuboid(0.0F, -21.0F, -7.0F, 0.0F, 7.0F, 6.0F, 0.0F, false);

        this.beak = new ModelRendererPlus(this);
        this.beak.setDefaultRotPoint(0.0F, 0.0F, 0.0F);
        this.head.addChild(this.beak);
        this.beak.setTextureOffset(12, 14).addCuboid(-1.0F, -4.0F, -4.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        this.beak.setTextureOffset(12, 18).addCuboid(-1.0F, -3.5F, -4.5F, 2.0F, 2.0F, 1.0F, 0.0F, false);

        this.backFeathersLeft = new ModelRendererPlus(this);
        this.backFeathersLeft.setDefaultRotPoint(0.0F, -3.0F, 3.6667F);
        this.body.addChild(this.backFeathersLeft);
        this.setRotationAngle(this.backFeathersLeft, -0.4363F, -0.0873F, 1.1345F);
        this.backFeathersLeft.setTextureOffset(0, 21).addCuboid(0.0F, -8.5F, 0.0F, 6.0F, 9.0F, 0.0F, 0.0F, false);

        this.backFeathersLeftUp = new ModelRendererPlus(this);
        this.backFeathersLeftUp.setDefaultRotPoint(0.0F, 0.0F, 0.0F);
        this.backFeathersLeft.addChild(this.backFeathersLeftUp);
        this.setRotationAngle(this.backFeathersLeftUp, -0.1745F, 0.0F, -1.0472F);
        this.backFeathersLeftUp.setTextureOffset(12, 21).addCuboid(0.0F, -10.5F, 0.0F, 6.0F, 11.0F, 0.0F, 0.0F, false);

        this.backFeathersRight = new ModelRendererPlus(this);
        this.backFeathersRight.setDefaultRotPoint(0.0F, -3.0F, 3.6667F);
        this.body.addChild(this.backFeathersRight);
        this.setRotationAngle(this.backFeathersRight, -0.4363F, 0.0873F, -1.1345F);
        this.backFeathersRight.setTextureOffset(0, 21).addCuboid(-6.0F, -9.5F, 0.0F, 6.0F, 10.0F, 0.0F, 0.0F, true);

        this.backFeathersRightUp = new ModelRendererPlus(this);
        this.backFeathersRightUp.setDefaultRotPoint(0.0F, 0.0F, 0.0F);
        this.backFeathersRight.addChild(this.backFeathersRightUp);
        this.setRotationAngle(this.backFeathersRightUp, -0.1745F, 0.0F, 1.0472F);
        this.backFeathersRightUp.setTextureOffset(12, 21).addCuboid(-6.0F, -10.5F, 0.0F, 6.0F, 11.0F, 0.0F, 0.0F, true);

        this.legLeft = new ModelRendererPlus(this);
        this.legLeft.setDefaultRotPoint(1.5F, 3.5F, -0.8333F);
        this.body.addChild(this.legLeft);
        this.legLeft.setTextureOffset(26, 0).addCuboid(-0.5F, -1.0F, -0.5F, 1.0F, 6.0F, 1.0F, 0.0F, false);
        this.legLeft.setTextureOffset(26, 7).addCuboid(-1.5F, 5.0F, -2.5F, 3.0F, 0.0F, 4.0F, 0.0F, false);

        this.legRight = new ModelRendererPlus(this);
        this.legRight.setDefaultRotPoint(-1.5F, 3.5F, -0.8333F);
        this.body.addChild(this.legRight);
        this.legRight.setTextureOffset(26, 0).addCuboid(-0.5F, -1.0F, -0.5F, 1.0F, 6.0F, 1.0F, 0.0F, false);
        this.legRight.setTextureOffset(26, 7).addCuboid(-1.5F, 5.0F, -2.5F, 3.0F, 0.0F, 4.0F, 0.0F, false);

        this.wingLeft = new ModelRendererPlus(this);
        this.wingLeft.setDefaultRotPoint(3.5F, -1.5F, -3.8333F);
        this.body.addChild(this.wingLeft);
        this.setRotationAngle(this.wingLeft, 0.4363F, 0.0F, 0.0F);
        this.wingLeft.setTextureOffset(40, 0).addCuboid(0.0F, -2.0F, -0.5F, 1.0F, 5.0F, 8.0F, 0.0F, false);

        this.wingRight = new ModelRendererPlus(this);
        this.wingRight.setDefaultRotPoint(-3.5F, -1.4397F, -3.4913F);
        this.body.addChild(this.wingRight);
        this.setRotationAngle(this.wingRight, 0.4363F, 0.0F, 0.0F);
        this.wingRight.setTextureOffset(40, 0).addCuboid(-1.0F, -2.0603F, -0.842F, 1.0F, 5.0F, 8.0F, 0.0F, true);

        this.animations = new BlockBenchAnimations(this, new ResourceLocation(RuneCraftory.MODID, "models/entity/animation/cluckadoodle.json"));

    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.body.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public void setAngles(T cluckadoodle, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetModel();
        this.neck.rotateAngleY = netHeadYaw * 0.008726646F * 0.5f;
        this.neck.rotateAngleX = headPitch * 0.008726646F * 0.5f;
        this.head.rotateAngleY = netHeadYaw * 0.008726646F * 0.5f;
        this.head.rotateAngleX = headPitch * 0.008726646F * 0.5f;

        float partialTicks = Minecraft.getInstance().getRenderPartialTicks();

        if (cluckadoodle.isMoving())
            this.animations.doAnimation("walk", cluckadoodle.ticksExisted, partialTicks);

        AnimatedAction anim = cluckadoodle.getAnimation();
        if (anim != null)
            this.animations.doAnimation(anim.getID(), anim.getTick(), partialTicks);
    }

    public void setRotationAngle(ModelRendererPlus modelRenderer, float x, float y, float z) {
        modelRenderer.setDefaultRotAngle(x, y, z);
    }

    @Override
    public void resetModel() {
        this.body.reset();
        this.resetChild(this.body);
    }
}