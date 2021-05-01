package com.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.entities.monster.EntityBuffamoo;
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

public class ModelBuffamoo<T extends EntityBuffamoo> extends EntityModel<T> implements IResetModel {
    public ModelRendererPlus body;
    public ModelRendererPlus head;
    public ModelRendererPlus headFront;
    public ModelRendererPlus hornLeft;
    public ModelRendererPlus hornLeftTip;
    public ModelRendererPlus hornRight;
    public ModelRendererPlus hornRight2;
    public ModelRendererPlus udder;
    public ModelRendererPlus frontLegLeftUp;
    public ModelRendererPlus frontLegLeftDown;
    public ModelRendererPlus frontLegRightUp;
    public ModelRendererPlus frontLegRightDown;
    public ModelRendererPlus backLegLeftUp;
    public ModelRendererPlus backLegLeftDown;
    public ModelRendererPlus backLegRightUp;
    public ModelRendererPlus backLegRightDown;
    public ModelRendererPlus topFur1;
    public ModelRendererPlus topFur2;
    public ModelRendererPlus topFur3;
    public ModelRendererPlus topFur4;
    public ModelRendererPlus tail;
    public ModelRendererPlus tail2;
    public ModelRendererPlus tail3;
    public ModelRendererPlus tailTip;

    public BlockBenchAnimations animations;

    public ModelBuffamoo() {
        this.textureWidth = 121;
        this.textureHeight = 55;

        this.body = new ModelRendererPlus(this);
        this.body.setDefaultRotPoint(0.0F, 4.0F, -1.0F);
        this.body.setTextureOffset(0, 0).addCuboid(-6.0F, -3.0F, -7.0F, 12.0F, 14.0F, 20.0F, 0.0F, false);

        this.head = new ModelRendererPlus(this);
        this.head.setDefaultRotPoint(0.0F, 0.0F, -6.0F);
        this.body.addChild(this.head);
        this.setRotationAngle(this.head, 0.1745F, 0.0F, 0.0F);
        this.head.setTextureOffset(0, 35).addCuboid(-5.0F, -3.0F, -3.0F, 10.0F, 11.0F, 3.0F, 0.0F, false);

        this.headFront = new ModelRendererPlus(this);
        this.headFront.setDefaultRotPoint(0.0F, 0.0F, -3.0F);
        this.head.addChild(this.headFront);
        this.headFront.setTextureOffset(26, 34).addCuboid(-4.0F, -2.0F, -9.0F, 8.0F, 9.0F, 9.0F, 0.0F, false);
        this.headFront.setTextureOffset(64, 20).addCuboid(-4.0F, 7.0F, -9.0F, 8.0F, 2.0F, 6.0F, 0.0F, false);

        this.hornLeft = new ModelRendererPlus(this);
        this.hornLeft.setDefaultRotPoint(4.0F, 0.0F, -4.5F);
        this.head.addChild(this.hornLeft);
        this.setRotationAngle(this.hornLeft, 0.0F, 0.0F, -0.2618F);
        this.hornLeft.setTextureOffset(0, 49).addCuboid(0.0F, -2.0F, -0.5F, 4.0F, 2.0F, 2.0F, 0.0F, false);

        this.hornLeftTip = new ModelRendererPlus(this);
        this.hornLeftTip.setDefaultRotPoint(4.0F, 0.0F, 1.0F);
        this.hornLeft.addChild(this.hornLeftTip);
        this.setRotationAngle(this.hornLeftTip, 0.0F, 0.0F, -0.9599F);
        this.hornLeftTip.setTextureOffset(12, 49).addCuboid(0.0F, -2.0F, -1.0F, 5.0F, 2.0F, 1.0F, 0.0F, false);

        this.hornRight = new ModelRendererPlus(this);
        this.hornRight.setDefaultRotPoint(-4.0F, 0.0F, -4.5F);
        this.head.addChild(this.hornRight);
        this.setRotationAngle(this.hornRight, 0.0F, 0.0F, 0.2618F);
        this.hornRight.setTextureOffset(0, 49).addCuboid(-4.0F, -2.0F, -0.5F, 4.0F, 2.0F, 2.0F, 0.0F, true);

        this.hornRight2 = new ModelRendererPlus(this);
        this.hornRight2.setDefaultRotPoint(-4.0F, 0.0F, 0.0F);
        this.hornRight.addChild(this.hornRight2);
        this.setRotationAngle(this.hornRight2, 0.0F, 0.0F, 0.9599F);
        this.hornRight2.setTextureOffset(12, 49).addCuboid(-5.0F, -2.0F, 0.0F, 5.0F, 2.0F, 1.0F, 0.0F, true);

        this.udder = new ModelRendererPlus(this);
        this.udder.setDefaultRotPoint(0.0F, 30.0F, 10.0F);
        this.body.addChild(this.udder);
        this.udder.setTextureOffset(65, 0).addCuboid(-2.0F, -19.0F, -4.0F, 4.0F, 1.0F, 6.0F, 0.0F, false);

        this.frontLegLeftUp = new ModelRendererPlus(this);
        this.frontLegLeftUp.setDefaultRotPoint(3.9F, 11.0F, -4.0F);
        this.body.addChild(this.frontLegLeftUp);
        this.frontLegLeftUp.setTextureOffset(89, 0).addCuboid(-2.0F, -1.0F, -2.0F, 4.0F, 5.0F, 4.0F, 0.0F, true);

        this.frontLegLeftDown = new ModelRendererPlus(this);
        this.frontLegLeftDown.setDefaultRotPoint(0.0F, 4.0F, -2.0F);
        this.frontLegLeftUp.addChild(this.frontLegLeftDown);
        this.frontLegLeftDown.setTextureOffset(89, 10).addCuboid(-2.0F, 0.0F, 0.0F, 4.0F, 5.0F, 4.0F, 0.0F, true);

        this.frontLegRightUp = new ModelRendererPlus(this);
        this.frontLegRightUp.setDefaultRotPoint(-3.9F, 11.0F, -4.0F);
        this.body.addChild(this.frontLegRightUp);
        this.frontLegRightUp.setTextureOffset(89, 0).addCuboid(-2.0F, -1.0F, -2.0F, 4.0F, 5.0F, 4.0F, 0.0F, true);

        this.frontLegRightDown = new ModelRendererPlus(this);
        this.frontLegRightDown.setDefaultRotPoint(0.0F, 4.0F, -2.0F);
        this.frontLegRightUp.addChild(this.frontLegRightDown);
        this.frontLegRightDown.setTextureOffset(89, 10).addCuboid(-2.0F, 0.0F, 0.0F, 4.0F, 5.0F, 4.0F, 0.0F, false);

        this.backLegLeftUp = new ModelRendererPlus(this);
        this.backLegLeftUp.setDefaultRotPoint(3.9F, 11.0F, 9.0F);
        this.body.addChild(this.backLegLeftUp);
        this.backLegLeftUp.setTextureOffset(105, 0).addCuboid(-2.0F, -1.0F, -2.0F, 4.0F, 5.0F, 4.0F, 0.0F, true);

        this.backLegLeftDown = new ModelRendererPlus(this);
        this.backLegLeftDown.setDefaultRotPoint(0.0F, 4.0F, -2.0F);
        this.backLegLeftUp.addChild(this.backLegLeftDown);
        this.backLegLeftDown.setTextureOffset(105, 10).addCuboid(-2.0F, 0.0F, 0.0F, 4.0F, 5.0F, 4.0F, 0.0F, true);

        this.backLegRightUp = new ModelRendererPlus(this);
        this.backLegRightUp.setDefaultRotPoint(-3.9F, 11.0F, 9.0F);
        this.body.addChild(this.backLegRightUp);
        this.backLegRightUp.setTextureOffset(105, 0).addCuboid(-2.0F, -1.0F, -2.0F, 4.0F, 5.0F, 4.0F, 0.0F, false);

        this.backLegRightDown = new ModelRendererPlus(this);
        this.backLegRightDown.setDefaultRotPoint(0.0F, 4.0F, -2.0F);
        this.backLegRightUp.addChild(this.backLegRightDown);
        this.backLegRightDown.setTextureOffset(105, 10).addCuboid(-2.0F, 0.0F, 0.0F, 4.0F, 5.0F, 4.0F, 0.0F, false);

        this.topFur1 = new ModelRendererPlus(this);
        this.topFur1.setDefaultRotPoint(0.0F, -1.0F, -4.0F);
        this.body.addChild(this.topFur1);
        this.topFur1.setTextureOffset(60, 36).addCuboid(0.0F, -8.0F, -7.0F, 0.0F, 9.0F, 7.0F, 0.0F, false);

        this.topFur2 = new ModelRendererPlus(this);
        this.topFur2.setDefaultRotPoint(0.0F, -1.0F, 2.0F);
        this.body.addChild(this.topFur2);
        this.topFur2.setTextureOffset(74, 37).addCuboid(0.0F, -8.0F, -6.0F, 0.0F, 9.0F, 6.0F, 0.0F, false);

        this.topFur3 = new ModelRendererPlus(this);
        this.topFur3.setDefaultRotPoint(0.0F, -1.0F, 8.0F);
        this.body.addChild(this.topFur3);
        this.topFur3.setTextureOffset(86, 37).addCuboid(0.0F, -8.0F, -6.0F, 0.0F, 9.0F, 6.0F, 0.0F, false);

        this.topFur4 = new ModelRendererPlus(this);
        this.topFur4.setDefaultRotPoint(0.0F, -1.0F, 13.0F);
        this.body.addChild(this.topFur4);
        this.topFur4.setTextureOffset(98, 38).addCuboid(0.0F, -8.0F, -5.0F, 0.0F, 9.0F, 5.0F, 0.0F, false);

        this.tail = new ModelRendererPlus(this);
        this.tail.setDefaultRotPoint(0.0F, -1.0F, 12.0F);
        this.body.addChild(this.tail);
        this.setRotationAngle(this.tail, 0.7854F, 0.0F, 0.0F);
        this.tail.setTextureOffset(64, 8).addCuboid(-0.5F, 0.0F, 0.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        this.tail2 = new ModelRendererPlus(this);
        this.tail2.setDefaultRotPoint(0.0F, 4.0F, 0.5F);
        this.tail.addChild(this.tail2);
        this.setRotationAngle(this.tail2, -0.1745F, 0.0F, 0.0F);
        this.tail2.setTextureOffset(68, 8).addCuboid(-0.5F, -0.3F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        this.tail3 = new ModelRendererPlus(this);
        this.tail3.setDefaultRotPoint(0.0F, 3.7F, 0.0F);
        this.tail2.addChild(this.tail3);
        this.setRotationAngle(this.tail3, -0.5236F, 0.0F, 0.0F);
        this.tail3.setTextureOffset(72, 8).addCuboid(-0.5F, -0.3F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        this.tailTip = new ModelRendererPlus(this);
        this.tailTip.setDefaultRotPoint(0.0F, 4.3F, 0.0F);
        this.tail3.addChild(this.tailTip);
        this.tailTip.setTextureOffset(76, 8).addCuboid(-1.0F, -0.2F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);

        this.animations = new BlockBenchAnimations(this, new ResourceLocation(RuneCraftory.MODID, "models/entity/animation/buffamoo.json"));
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.body.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public void setAngles(T buffamoo, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetModel();
        this.head.rotateAngleY = netHeadYaw * 0.008726646F;
        this.head.rotateAngleX = headPitch * 0.008726646F;

        float partialTicks = Minecraft.getInstance().getRenderPartialTicks();
        this.topFur1.rotateAngleZ = (float) (MathHelper.sin(buffamoo.ticksExisted * 0.4f) * 0.1);
        this.topFur2.rotateAngleZ = (float) (MathHelper.sin(buffamoo.ticksExisted * 0.4f + 4) * 0.1);
        this.topFur3.rotateAngleZ = (float) (MathHelper.sin(buffamoo.ticksExisted * 0.4f + 8) * 0.1);
        this.topFur4.rotateAngleZ = (float) (MathHelper.sin(buffamoo.ticksExisted * 0.4f + 12) * 0.1);

        if (buffamoo.isMoving())
            this.animations.doAnimation("walk", buffamoo.ticksExisted, partialTicks);

        AnimatedAction anim = buffamoo.getAnimation();
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