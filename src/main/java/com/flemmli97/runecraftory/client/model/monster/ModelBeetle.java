package com.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.entities.monster.EntityBeetle;
import com.flemmli97.tenshilib.client.model.BlockBenchAnimations;
import com.flemmli97.tenshilib.client.model.IResetModel;
import com.flemmli97.tenshilib.client.model.ModelRendererPlus;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class ModelBeetle<T extends EntityBeetle> extends EntityModel<T> implements IResetModel {
    public ModelRendererPlus body;
    public ModelRendererPlus head;
    public ModelRendererPlus chin;
    public ModelRendererPlus headLeft;
    public ModelRendererPlus headRight;
    public ModelRendererPlus headUp;
    public ModelRendererPlus hornFront;
    public ModelRendererPlus hornFrontTip2;
    public ModelRendererPlus hornBackBase;
    public ModelRendererPlus hornBack;
    public ModelRendererPlus hornBackTip;
    public ModelRendererPlus elytronLeft;
    public ModelRendererPlus elytronRight;
    public ModelRendererPlus wingLeft;
    public ModelRendererPlus wingLeftSide;
    public ModelRendererPlus wingLeftBack;
    public ModelRendererPlus wingRight;
    public ModelRendererPlus wingRightSide;
    public ModelRendererPlus wingRightBack;
    public ModelRendererPlus legLeft;
    public ModelRendererPlus legLeftMiddle;
    public ModelRendererPlus legLeftDown;
    public ModelRendererPlus legRight;
    public ModelRendererPlus legRightMiddle;
    public ModelRendererPlus legRightDown;
    public ModelRendererPlus armLeft;
    public ModelRendererPlus armLeftMiddle;
    public ModelRendererPlus leftClaw;
    public ModelRendererPlus armRight;
    public ModelRendererPlus armRightMiddle;
    public ModelRendererPlus rightClaw;
    public ModelRendererPlus armLeft2;
    public ModelRendererPlus armLeft2Middle;
    public ModelRendererPlus leftClaw2;
    public ModelRendererPlus armRight2;
    public ModelRendererPlus armRight2Middle;
    public ModelRendererPlus rightClaw2;

    public BlockBenchAnimations animations;

    public ModelBeetle() {
        this.textureWidth = 128;
        this.textureHeight = 62;

        this.body = new ModelRendererPlus(this);
        this.body.setDefaultRotPoint(0.0F, 7.0F, 3.0F);
        this.setRotationAngle(this.body, -0.6981F, 0.0F, 0.0F);
        this.body.setTextureOffset(0, 0).addCuboid(-5.0F, -5.0F, -7.0F, 10.0F, 7.0F, 15.0F, 0.0F, false);
        this.body.setTextureOffset(0, 22).addCuboid(-4.5F, 2.0F, -7.0F, 9.0F, 2.0F, 15.0F, 0.0F, false);

        this.head = new ModelRendererPlus(this);
        this.head.setDefaultRotPoint(0.0F, 1.0F, -7.0F);
        this.body.addChild(this.head);
        this.setRotationAngle(this.head, 0.5236F, 0.0F, 0.0F);
        this.head.setTextureOffset(110, 12).addCuboid(-2.0F, -3.1F, -5.0F, 4.0F, 6.0F, 5.0F, 0.0F, false);
        this.head.setTextureOffset(96, 24).addCuboid(-2.0F, -2.1F, -6.0F, 4.0F, 5.0F, 1.0F, 0.0F, false);
        this.head.setTextureOffset(106, 24).addCuboid(-1.0F, -1.1F, -7.0F, 2.0F, 4.0F, 1.0F, 0.0F, false);

        this.chin = new ModelRendererPlus(this);
        this.chin.setDefaultRotPoint(0.0F, 2.9F, -6.0F);
        this.head.addChild(this.chin);
        this.setRotationAngle(this.chin, -0.3491F, 0.0F, 0.0F);
        this.chin.setTextureOffset(112, 23).addCuboid(-0.5F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        this.headLeft = new ModelRendererPlus(this);
        this.headLeft.setDefaultRotPoint(4.0F, 0.0F, 0.0F);
        this.head.addChild(this.headLeft);
        this.setRotationAngle(this.headLeft, 0.0F, 0.4363F, 0.0F);
        this.headLeft.setTextureOffset(106, 0).addCuboid(-2.0F, -3.1F, -5.0F, 2.0F, 6.0F, 6.0F, 0.0F, false);
        this.headLeft.setTextureOffset(96, 12).addCuboid(-3.0F, -4.1F, -2.0F, 2.0F, 7.0F, 5.0F, 0.0F, false);

        this.headRight = new ModelRendererPlus(this);
        this.headRight.setDefaultRotPoint(-4.0F, 0.0F, 0.0F);
        this.head.addChild(this.headRight);
        this.setRotationAngle(this.headRight, 0.0F, -0.4363F, 0.0F);
        this.headRight.setTextureOffset(106, 0).addCuboid(0.0F, -3.1F, -5.0F, 2.0F, 6.0F, 6.0F, 0.0F, true);
        this.headRight.setTextureOffset(96, 12).addCuboid(1.0F, -4.1F, -2.0F, 2.0F, 7.0F, 5.0F, 0.0F, true);

        this.headUp = new ModelRendererPlus(this);
        this.headUp.setDefaultRotPoint(0.0F, -3.1F, -4.0F);
        this.head.addChild(this.headUp);
        this.setRotationAngle(this.headUp, -0.6109F, 0.0F, 0.0F);
        this.headUp.setTextureOffset(84, 30).addCuboid(-1.5F, -2.0F, -1.0F, 3.0F, 4.0F, 2.0F, 0.0F, false);
        this.headUp.setTextureOffset(94, 30).addCuboid(-2.0F, -4.0F, 0.0F, 4.0F, 4.0F, 2.0F, 0.0F, false);
        this.headUp.setTextureOffset(84, 36).addCuboid(-2.5F, -5.0F, 1.0F, 5.0F, 5.0F, 2.0F, 0.0F, false);
        this.headUp.setTextureOffset(98, 36).addCuboid(-2.5F, -6.0F, 3.0F, 5.0F, 6.0F, 3.0F, 0.0F, false);

        this.hornFront = new ModelRendererPlus(this);
        this.hornFront.setDefaultRotPoint(0.0F, 1.0F, -0.75F);
        this.headUp.addChild(this.hornFront);
        this.setRotationAngle(this.hornFront, -0.0873F, 0.0F, 0.0F);
        this.hornFront.setTextureOffset(112, 25).addCuboid(-0.5F, -2.0F, -3.25F, 1.0F, 2.0F, 3.0F, 0.0F, false);

        this.hornFrontTip2 = new ModelRendererPlus(this);
        this.hornFrontTip2.setDefaultRotPoint(0.0F, 0.0F, -3.0F);
        this.hornFront.addChild(this.hornFrontTip2);
        this.setRotationAngle(this.hornFrontTip2, -0.2618F, 0.0F, 0.0F);
        this.hornFrontTip2.setTextureOffset(112, 30).addCuboid(-0.5F, -2.0F, -4.25F, 1.0F, 1.0F, 4.0F, 0.0F, false);

        this.hornBackBase = new ModelRendererPlus(this);
        this.hornBackBase.setDefaultRotPoint(0.0F, -4.5F, -5.5F);
        this.body.addChild(this.hornBackBase);
        this.setRotationAngle(this.hornBackBase, 0.5236F, 0.0F, 0.0F);
        this.hornBackBase.setTextureOffset(82, 18).addCuboid(-1.0F, -4.5F, -1.5F, 2.0F, 4.0F, 2.0F, 0.0F, false);

        this.hornBack = new ModelRendererPlus(this);
        this.hornBack.setDefaultRotPoint(0.0F, -4.25F, 0.5F);
        this.hornBackBase.addChild(this.hornBack);
        this.setRotationAngle(this.hornBack, 0.5236F, 0.0F, 0.0F);
        this.hornBack.setTextureOffset(90, 18).addCuboid(-0.5F, -5.25F, -2.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        this.hornBackTip = new ModelRendererPlus(this);
        this.hornBackTip.setDefaultRotPoint(0.0F, -5.0F, -1.0F);
        this.hornBack.addChild(this.hornBackTip);
        this.setRotationAngle(this.hornBackTip, 0.6109F, 0.0F, 0.0F);
        this.hornBackTip.setTextureOffset(84, 24).addCuboid(-0.5F, -5.25F, -1.0F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        this.elytronLeft = new ModelRendererPlus(this);
        this.elytronLeft.setDefaultRotPoint(3.0F, -5.0F, -7.0F);
        this.body.addChild(this.elytronLeft);
        this.elytronLeft.setTextureOffset(0, 40).addCuboid(-3.0F, -1.0F, 0.0F, 5.0F, 1.0F, 15.0F, 0.0F, false);
        this.elytronLeft.setTextureOffset(50, 0).addCuboid(2.0F, 0.0F, 0.0F, 1.0F, 7.0F, 15.0F, 0.0F, false);
        this.elytronLeft.setTextureOffset(48, 22).addCuboid(-3.0F, -0.01F, 15.0F, 5.0F, 7.0F, 1.0F, 0.0F, false);

        this.elytronRight = new ModelRendererPlus(this);
        this.elytronRight.setDefaultRotPoint(-3.0F, -5.0F, -7.0F);
        this.body.addChild(this.elytronRight);
        this.elytronRight.setTextureOffset(0, 40).addCuboid(-2.0F, -1.0F, 0.0F, 5.0F, 1.0F, 15.0F, 0.0F, true);
        this.elytronRight.setTextureOffset(50, 0).addCuboid(-3.0F, 0.0F, 0.0F, 1.0F, 7.0F, 15.0F, 0.0F, true);
        this.elytronRight.setTextureOffset(48, 22).addCuboid(-2.0F, -0.01F, 15.0F, 5.0F, 7.0F, 1.0F, 0.0F, true);

        this.wingLeft = new ModelRendererPlus(this);
        this.wingLeft.setDefaultRotPoint(1.0F, -5.0F, -4.0F);
        this.body.addChild(this.wingLeft);
        this.wingLeft.setTextureOffset(48, 30).addCuboid(-1.0F, 0.0F, -1.0F, 5.0F, 0.0F, 13.0F, 0.0F, false);

        this.wingLeftSide = new ModelRendererPlus(this);
        this.wingLeftSide.setDefaultRotPoint(4.0F, 0.0F, 5.0F);
        this.wingLeft.addChild(this.wingLeftSide);
        this.wingLeftSide.setTextureOffset(40, 43).addCuboid(0.0F, 0.0F, -6.0F, 0.0F, 7.0F, 13.0F, 0.0F, true);

        this.wingLeftBack = new ModelRendererPlus(this);
        this.wingLeftBack.setDefaultRotPoint(0.0F, 3.0F, 7.0F);
        this.wingLeftSide.addChild(this.wingLeftBack);
        this.wingLeftBack.setTextureOffset(67, 43).addCuboid(-5.0F, -3.0F, 0.0F, 5.0F, 7.0F, 0.0F, 0.0F, false);

        this.wingRight = new ModelRendererPlus(this);
        this.wingRight.setDefaultRotPoint(-1.0F, -5.0F, -4.0F);
        this.body.addChild(this.wingRight);
        this.wingRight.setTextureOffset(48, 30).addCuboid(-4.0F, 0.0F, -1.0F, 5.0F, 0.0F, 13.0F, 0.0F, true);

        this.wingRightSide = new ModelRendererPlus(this);
        this.wingRightSide.setDefaultRotPoint(-4.0F, 0.0F, 5.0F);
        this.wingRight.addChild(this.wingRightSide);
        this.wingRightSide.setTextureOffset(40, 43).addCuboid(0.0F, 0.0F, -6.0F, 0.0F, 7.0F, 13.0F, 0.0F, false);

        this.wingRightBack = new ModelRendererPlus(this);
        this.wingRightBack.setDefaultRotPoint(0.0F, 3.0F, 7.0F);
        this.wingRightSide.addChild(this.wingRightBack);
        this.wingRightBack.setTextureOffset(67, 43).addCuboid(0.0F, -3.0F, 0.0F, 5.0F, 7.0F, 0.0F, 0.0F, true);

        this.legLeft = new ModelRendererPlus(this);
        this.legLeft.setDefaultRotPoint(2.0F, 4.0F, 5.0F);
        this.body.addChild(this.legLeft);
        this.setRotationAngle(this.legLeft, 0.2618F, 0.0F, 0.0F);
        this.legLeft.setTextureOffset(82, 0).addCuboid(0.0F, 0.0F, 0.0F, 2.0F, 5.0F, 2.0F, 0.0F, false);

        this.legLeftMiddle = new ModelRendererPlus(this);
        this.legLeftMiddle.setDefaultRotPoint(1.0F, 5.0F, 0.0F);
        this.legLeft.addChild(this.legLeftMiddle);
        this.setRotationAngle(this.legLeftMiddle, 1.1345F, 0.0F, 0.0F);
        this.legLeftMiddle.setTextureOffset(82, 7).addCuboid(-0.5F, 0.0F, 0.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);

        this.legLeftDown = new ModelRendererPlus(this);
        this.legLeftDown.setDefaultRotPoint(0.0F, 4.0F, 1.5F);
        this.legLeftMiddle.addChild(this.legLeftDown);
        this.setRotationAngle(this.legLeftDown, -0.7854F, 0.0F, 0.0F);
        this.legLeftDown.setTextureOffset(82, 13).addCuboid(-0.5F, 0.0F, -1.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        this.legRight = new ModelRendererPlus(this);
        this.legRight.setDefaultRotPoint(-2.0F, 4.0F, 5.0F);
        this.body.addChild(this.legRight);
        this.setRotationAngle(this.legRight, 0.2618F, 0.0F, 0.0F);
        this.legRight.setTextureOffset(82, 0).addCuboid(-2.0F, 0.0F, 0.0F, 2.0F, 5.0F, 2.0F, 0.0F, true);

        this.legRightMiddle = new ModelRendererPlus(this);
        this.legRightMiddle.setDefaultRotPoint(-1.0F, 5.0F, 0.0F);
        this.legRight.addChild(this.legRightMiddle);
        this.setRotationAngle(this.legRightMiddle, 1.1345F, 0.0F, 0.0F);
        this.legRightMiddle.setTextureOffset(82, 7).addCuboid(-0.5F, 0.0F, 0.0F, 1.0F, 4.0F, 2.0F, 0.0F, true);

        this.legRightDown = new ModelRendererPlus(this);
        this.legRightDown.setDefaultRotPoint(0.0F, 4.0F, 1.5F);
        this.legRightMiddle.addChild(this.legRightDown);
        this.setRotationAngle(this.legRightDown, -0.7854F, 0.0F, 0.0F);
        this.legRightDown.setTextureOffset(82, 13).addCuboid(-0.5F, 0.0F, -1.0F, 1.0F, 4.0F, 1.0F, 0.0F, true);

        this.armLeft = new ModelRendererPlus(this);
        this.armLeft.setDefaultRotPoint(2.0F, 4.0F, -6.0F);
        this.body.addChild(this.armLeft);
        this.setRotationAngle(this.armLeft, 0.0F, 0.0F, -0.9599F);
        this.armLeft.setTextureOffset(90, 0).addCuboid(0.0F, 0.0F, 0.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        this.armLeftMiddle = new ModelRendererPlus(this);
        this.armLeftMiddle.setDefaultRotPoint(0.5F, 5.0F, 0.5F);
        this.armLeft.addChild(this.armLeftMiddle);
        this.setRotationAngle(this.armLeftMiddle, 0.0873F, 0.0F, 0.7854F);
        this.armLeftMiddle.setTextureOffset(90, 7).addCuboid(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.armLeftMiddle.setTextureOffset(96, 0).addCuboid(-0.5F, 2.0F, 0.5F, 1.0F, 3.0F, 0.0F, 0.0F, false);
        this.armLeftMiddle.setTextureOffset(96, 3).addCuboid(0.0F, 2.0F, 0.0F, 0.0F, 3.0F, 1.0F, 0.0F, false);

        this.leftClaw = new ModelRendererPlus(this);
        this.leftClaw.setDefaultRotPoint(0.0F, 4.75F, 0.5F);
        this.armLeftMiddle.addChild(this.leftClaw);
        this.setRotationAngle(this.leftClaw, 0.0F, 0.0F, 0.4363F);
        this.leftClaw.setTextureOffset(94, 7).addCuboid(-0.5F, 0.25F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        this.armRight = new ModelRendererPlus(this);
        this.armRight.setDefaultRotPoint(-2.0F, 4.0F, -6.0F);
        this.body.addChild(this.armRight);
        this.setRotationAngle(this.armRight, 0.0F, 0.0F, 0.9599F);
        this.armRight.setTextureOffset(90, 0).addCuboid(-1.0F, 0.0F, 0.0F, 1.0F, 5.0F, 2.0F, 0.0F, true);

        this.armRightMiddle = new ModelRendererPlus(this);
        this.armRightMiddle.setDefaultRotPoint(-0.5F, 5.0F, 0.5F);
        this.armRight.addChild(this.armRightMiddle);
        this.setRotationAngle(this.armRightMiddle, 0.0873F, 0.0F, -0.7854F);
        this.armRightMiddle.setTextureOffset(90, 7).addCuboid(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.0F, true);
        this.armRightMiddle.setTextureOffset(96, 0).addCuboid(-0.5F, 2.0F, 0.5F, 1.0F, 3.0F, 0.0F, 0.0F, false);
        this.armRightMiddle.setTextureOffset(96, 3).addCuboid(0.0F, 2.0F, 0.0F, 0.0F, 3.0F, 1.0F, 0.0F, false);

        this.rightClaw = new ModelRendererPlus(this);
        this.rightClaw.setDefaultRotPoint(0.0F, 4.75F, 0.5F);
        this.armRightMiddle.addChild(this.rightClaw);
        this.setRotationAngle(this.rightClaw, 0.0F, 0.0F, -0.4363F);
        this.rightClaw.setTextureOffset(94, 7).addCuboid(-0.5F, 0.25F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);

        this.armLeft2 = new ModelRendererPlus(this);
        this.armLeft2.setDefaultRotPoint(3.0F, 4.0F, -1.0F);
        this.body.addChild(this.armLeft2);
        this.setRotationAngle(this.armLeft2, 0.1745F, 0.0F, -0.4363F);
        this.armLeft2.setTextureOffset(98, 0).addCuboid(0.0F, 0.0F, 0.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        this.armLeft2Middle = new ModelRendererPlus(this);
        this.armLeft2Middle.setDefaultRotPoint(0.5F, 5.0F, 1.5F);
        this.armLeft2.addChild(this.armLeft2Middle);
        this.setRotationAngle(this.armLeft2Middle, -0.3491F, 0.0F, 0.7854F);
        this.armLeft2Middle.setTextureOffset(98, 7).addCuboid(-0.5F, 0.0F, -1.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.armLeft2Middle.setTextureOffset(104, 0).addCuboid(-0.5F, 2.0F, -0.5F, 1.0F, 3.0F, 0.0F, 0.0F, false);
        this.armLeft2Middle.setTextureOffset(104, 5).addCuboid(0.0F, 2.0F, -1.0F, 0.0F, 3.0F, 1.0F, 0.0F, false);

        this.leftClaw2 = new ModelRendererPlus(this);
        this.leftClaw2.setDefaultRotPoint(0.0F, 4.75F, -0.5F);
        this.armLeft2Middle.addChild(this.leftClaw2);
        this.leftClaw2.setTextureOffset(102, 7).addCuboid(-0.5F, 0.25F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        this.armRight2 = new ModelRendererPlus(this);
        this.armRight2.setDefaultRotPoint(-3.0F, 4.0F, -1.0F);
        this.body.addChild(this.armRight2);
        this.setRotationAngle(this.armRight2, 0.1745F, 0.0F, 0.4363F);
        this.armRight2.setTextureOffset(98, 0).addCuboid(-1.0F, 0.0F, 0.0F, 1.0F, 5.0F, 2.0F, 0.0F, true);

        this.armRight2Middle = new ModelRendererPlus(this);
        this.armRight2Middle.setDefaultRotPoint(-0.5F, 5.0F, 1.5F);
        this.armRight2.addChild(this.armRight2Middle);
        this.setRotationAngle(this.armRight2Middle, -0.3491F, 0.0F, -0.7854F);
        this.armRight2Middle.setTextureOffset(98, 7).addCuboid(-0.5F, 0.0F, -1.0F, 1.0F, 2.0F, 1.0F, 0.0F, true);
        this.armRight2Middle.setTextureOffset(104, 0).addCuboid(-0.5F, 2.0F, -0.5F, 1.0F, 3.0F, 0.0F, 0.0F, false);
        this.armRight2Middle.setTextureOffset(104, 5).addCuboid(0.0F, 2.0F, -1.0F, 0.0F, 3.0F, 1.0F, 0.0F, false);

        this.rightClaw2 = new ModelRendererPlus(this);
        this.rightClaw2.setDefaultRotPoint(0.0F, 4.75F, -0.5F);
        this.armRight2Middle.addChild(this.rightClaw2);
        this.rightClaw2.setTextureOffset(102, 7).addCuboid(-0.5F, 0.25F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        this.animations = new BlockBenchAnimations(this, new ResourceLocation(RuneCraftory.MODID, "models/entity/animation/beetle.json"));
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.body.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public void setAngles(T beetle, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetModel();
        this.head.rotateAngleY += netHeadYaw * 0.008726646F;
        this.head.rotateAngleX += headPitch * 0.008726646F;
        this.armRight.rotateAngleZ += MathHelper.cos(ageInTicks * 0.04F) * 0.07F + 0.05F;
        this.armLeft.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.04F) * 0.07F + 0.05F;
        this.armRight.rotateAngleX += MathHelper.sin(ageInTicks * 0.037F) * 0.07F;
        this.armLeft.rotateAngleX -= MathHelper.sin(ageInTicks * 0.037F) * 0.07F;
        this.armRight2.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.armLeft2.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.armRight2.rotateAngleX += MathHelper.sin(ageInTicks * 0.037F) * 0.05F;
        this.armLeft2.rotateAngleX -= MathHelper.sin(ageInTicks * 0.037F) * 0.05F;

        float partialTicks = Minecraft.getInstance().getRenderPartialTicks();

        if (!beetle.isOnGround())
            this.animations.doAnimation("fly", beetle.ticksExisted, partialTicks);
        else if (beetle.isMoving())
            this.animations.doAnimation("walk", beetle.ticksExisted, partialTicks);

        AnimatedAction anim = beetle.getAnimation();
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