package com.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.entities.monster.EntityChipsqueek;
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

public class ModelChipsqueek<T extends EntityChipsqueek> extends EntityModel<T> implements IResetModel {
    public ModelRendererPlus body;
    public ModelRendererPlus head;
    public ModelRendererPlus earLeft;
    public ModelRendererPlus earRight;
    public ModelRendererPlus tail;
    public ModelRendererPlus armLeft;
    public ModelRendererPlus armLeftDown;
    public ModelRendererPlus armRight;
    public ModelRendererPlus armRightDown;
    public ModelRendererPlus legLeftBase;
    public ModelRendererPlus legLeft;
    public ModelRendererPlus feetLeft;
    public ModelRendererPlus legRightBase;
    public ModelRendererPlus legRight;
    public ModelRendererPlus feetRight;

    public BlockBenchAnimations animations;

    public ModelChipsqueek() {
        this.textureWidth = 64;
        this.textureHeight = 28;

        this.body = new ModelRendererPlus(this);
        this.body.setDefaultRotPoint(0.0F, 16.8F, 1.5F);
        this.setRotationAngle(this.body, -0.5236F, 0.0F, 0.0F);
        this.body.setTextureOffset(0, 12).addCuboid(-3.5F, -4.0F, -4.5F, 7.0F, 7.0F, 9.0F, 0.0F, false);

        this.head = new ModelRendererPlus(this);
        this.head.setDefaultRotPoint(0.0F, -2.5F, -4.0F);
        this.body.addChild(this.head);
        this.setRotationAngle(this.head, 0.6109F, 0.0F, 0.0F);
        this.head.setTextureOffset(0, 0).addCuboid(-3.0F, -4.0F, -5.5F, 6.0F, 6.0F, 6.0F, 0.0F, false);

        this.earLeft = new ModelRendererPlus(this);
        this.earLeft.setDefaultRotPoint(1.5F, -2.5F, -2.0F);
        this.head.addChild(this.earLeft);
        this.earLeft.setTextureOffset(24, 0).addCuboid(-0.5F, -3.5F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        this.earRight = new ModelRendererPlus(this);
        this.earRight.setDefaultRotPoint(-1.5F, -2.5F, -2.0F);
        this.head.addChild(this.earRight);
        this.earRight.setTextureOffset(24, 0).addCuboid(-0.5F, -3.5F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, true);

        this.tail = new ModelRendererPlus(this);
        this.tail.setDefaultRotPoint(0.0F, -1.0F, 4.5F);
        this.body.addChild(this.tail);
        this.setRotationAngle(this.tail, -0.1745F, 0.0F, 0.0F);
        this.tail.setTextureOffset(32, 0).addCuboid(-2.5F, -10.5F, 0.0F, 5.0F, 12.0F, 2.0F, 0.0F, false);

        this.armLeft = new ModelRendererPlus(this);
        this.armLeft.setDefaultRotPoint(3.5F, 0.5F, -2.5F);
        this.body.addChild(this.armLeft);
        this.setRotationAngle(this.armLeft, 0.0F, 0.0F, 0.1745F);
        this.armLeft.setTextureOffset(32, 14).addCuboid(-0.5F, 0.0F, -1.0F, 1.0F, 3.0F, 2.0F, 0.0F, false);

        this.armLeftDown = new ModelRendererPlus(this);
        this.armLeftDown.setDefaultRotPoint(0.0F, 2.5F, 1.0F);
        this.armLeft.addChild(this.armLeftDown);
        this.setRotationAngle(this.armLeftDown, 0.0F, 0.0F, 0.5236F);
        this.armLeftDown.setTextureOffset(32, 20).addCuboid(-0.5F, 0.5F, -2.0F, 1.0F, 3.0F, 2.0F, 0.0F, false);

        this.armRight = new ModelRendererPlus(this);
        this.armRight.setDefaultRotPoint(-3.5F, 0.5F, -2.5F);
        this.body.addChild(this.armRight);
        this.setRotationAngle(this.armRight, 0.0F, 0.0F, -0.1745F);
        this.armRight.setTextureOffset(32, 14).addCuboid(-0.5F, 0.0F, -1.0F, 1.0F, 3.0F, 2.0F, 0.0F, true);

        this.armRightDown = new ModelRendererPlus(this);
        this.armRightDown.setDefaultRotPoint(0.0F, 2.5F, 1.0F);
        this.armRight.addChild(this.armRightDown);
        this.setRotationAngle(this.armRightDown, 0.0F, 0.0F, -0.5236F);
        this.armRightDown.setTextureOffset(32, 20).addCuboid(-0.5F, 0.5F, -2.0F, 1.0F, 3.0F, 2.0F, 0.0F, true);

        this.legLeftBase = new ModelRendererPlus(this);
        this.legLeftBase.setDefaultRotPoint(2.0F, 1.75F, 2.75F);
        this.body.addChild(this.legLeftBase);
        this.setRotationAngle(this.legLeftBase, 0.5236F, 0.0F, 0.0F);
        this.legLeftBase.setTextureOffset(46, 0).addCuboid(0.0F, -1.75F, -1.25F, 2.0F, 4.0F, 3.0F, 0.0F, false);

        this.legLeft = new ModelRendererPlus(this);
        this.legLeft.setDefaultRotPoint(1.0F, 2.75F, 1.75F);
        this.legLeftBase.addChild(this.legLeft);
        this.legLeft.setTextureOffset(46, 7).addCuboid(-1.0F, -0.5F, -2.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);

        this.feetLeft = new ModelRendererPlus(this);
        this.feetLeft.setDefaultRotPoint(0.0F, 2.0F, -2.0F);
        this.legLeft.addChild(this.feetLeft);
        this.feetLeft.setTextureOffset(46, 11).addCuboid(-1.0F, -0.5F, -1.0F, 2.0F, 0.0F, 1.0F, 0.0F, false);

        this.legRightBase = new ModelRendererPlus(this);
        this.legRightBase.setDefaultRotPoint(-4.0F, 1.75F, 2.75F);
        this.body.addChild(this.legRightBase);
        this.setRotationAngle(this.legRightBase, 0.5236F, 0.0F, 0.0F);
        this.legRightBase.setTextureOffset(46, 0).addCuboid(0.0F, -1.75F, -1.25F, 2.0F, 4.0F, 3.0F, 0.0F, true);

        this.legRight = new ModelRendererPlus(this);
        this.legRight.setDefaultRotPoint(1.0F, 2.75F, 1.75F);
        this.legRightBase.addChild(this.legRight);
        this.legRight.setTextureOffset(46, 7).addCuboid(-1.0F, -0.5F, -2.0F, 2.0F, 2.0F, 2.0F, 0.0F, true);

        this.feetRight = new ModelRendererPlus(this);
        this.feetRight.setDefaultRotPoint(0.0F, 2.0F, -2.0F);
        this.legRight.addChild(this.feetRight);
        this.feetRight.setTextureOffset(46, 11).addCuboid(-1.0F, -0.5F, -1.0F, 2.0F, 0.0F, 1.0F, 0.0F, false);

        this.animations = new BlockBenchAnimations(this, new ResourceLocation(RuneCraftory.MODID, "models/entity/animation/chipsqueek.json"));
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.body.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public void setAngles(T chipsqueek, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetModel();
        this.tail.rotateAngleX += MathHelper.cos(ageInTicks * 0.04F) * 0.25F + 0.05F;
        this.tail.rotateAngleY += MathHelper.sin(ageInTicks * 0.037F) * 0.25F;
        this.earLeft.rotateAngleX += MathHelper.cos(ageInTicks * 0.05F) * 0.1F;
        this.earLeft.rotateAngleY += MathHelper.sin(ageInTicks * 0.05F) * 0.1F;
        this.earRight.rotateAngleX -= MathHelper.cos(ageInTicks * 0.05F) * 0.1F;
        this.earRight.rotateAngleY -= MathHelper.sin(ageInTicks * 0.05F) * 0.1F;
        this.armLeft.rotateAngleX += MathHelper.cos(ageInTicks * 0.04F) * 0.05F;
        this.armLeft.rotateAngleY += MathHelper.sin(ageInTicks * 0.04F) * 0.25F;
        this.armRight.rotateAngleX -= MathHelper.cos(ageInTicks * 0.04F) * 0.05F;
        this.armRight.rotateAngleY -= MathHelper.sin(ageInTicks * 0.04F) * 0.25F;
        this.head.rotateAngleY += netHeadYaw * 0.017453292F;
        this.head.rotateAngleX += headPitch * 0.017453292F;

        float partialTicks = Minecraft.getInstance().getRenderPartialTicks();
        if (chipsqueek.isMoving())
            this.animations.doAnimation("walk", chipsqueek.ticksExisted, partialTicks);

        AnimatedAction anim = chipsqueek.getAnimation();
        if (anim != null)
            this.animations.doAnimation(anim.getAnimationClient(), anim.getTick(), partialTicks);
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