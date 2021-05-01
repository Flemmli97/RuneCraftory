package com.flemmli97.runecraftory.client.model.monster;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.entities.monster.boss.EntityThunderbolt;
import com.flemmli97.tenshilib.client.model.BlockBenchAnimations;
import com.flemmli97.tenshilib.client.model.IResetModel;
import com.flemmli97.tenshilib.client.model.ModelRendererPlus;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;

public class ModelThunderbolt<T extends EntityThunderbolt> extends EntityModel<T> implements IResetModel {

    public ModelRendererPlus body;
    public ModelRendererPlus backFur;
    public ModelRendererPlus neck;
    public ModelRendererPlus head;
    public ModelRendererPlus horn;
    public ModelRendererPlus leftFrontLegBase;
    public ModelRendererPlus leftFrontLeg;
    public ModelRendererPlus leftFrontLegDown;
    public ModelRendererPlus leftFrontLegFur;
    public ModelRendererPlus leftFrontHoove;
    public ModelRendererPlus rightFrontLegBase;
    public ModelRendererPlus rightFrontLeg;
    public ModelRendererPlus rightFrontLegDown;
    public ModelRendererPlus rightFrontLegFur;
    public ModelRendererPlus rightFrontHoove;
    public ModelRendererPlus leftBackLegBase;
    public ModelRendererPlus leftBackLeg;
    public ModelRendererPlus leftBackLegDown;
    public ModelRendererPlus leftBackHoove;
    public ModelRendererPlus rightBackLegBase;
    public ModelRendererPlus rightBackLeg;
    public ModelRendererPlus rightBackLegDown;
    public ModelRendererPlus rightBackHoove;
    public ModelRendererPlus tail;
    public ModelRendererPlus tailTip;

    public BlockBenchAnimations animations;

    public ModelThunderbolt() {
        this.textureWidth = 16;
        this.textureHeight = 16;

        this.body = new ModelRendererPlus(this);
        this.body.setDefaultRotPoint(4.5F, 20.0F, -13.0F);
        this.body.setTextureOffset(0, 0).addCuboid(-11.0F, -23.0F, 0.0F, 13.0F, 13.0F, 26.0F, 0.0F, false);

        this.backFur = new ModelRendererPlus(this);
        this.backFur.setDefaultRotPoint(-4.5F, -23.0F, 16.0F);
        this.body.addChild(this.backFur);
        this.backFur.setTextureOffset(0, 0).addCuboid(-0.5F, -2.0F, -12.0F, 1.0F, 3.0F, 24.0F, 0.0F, false);

        this.neck = new ModelRendererPlus(this);
        this.neck.setDefaultRotPoint(-5.5F, -16.0F, 0.0F);
        this.body.addChild(this.neck);
        this.setRotationAngle(this.neck, 0.5236F, 0.0F, 0.0F);
        this.neck.setTextureOffset(0, 0).addCuboid(-2.5F, -12.0F, 0.0F, 7.0F, 12.0F, 9.0F, 0.0F, false);
        this.neck.setTextureOffset(0, 0).addCuboid(0.5F, -12.0F, 3.0F, 1.0F, 12.0F, 10.0F, 0.0F, false);

        this.head = new ModelRendererPlus(this);
        this.head.setDefaultRotPoint(1.0F, -13.0F, 0.0F);
        this.neck.addChild(this.head);
        this.head.setTextureOffset(0, 0).addCuboid(-5.0F, -9.0F, -2.5F, 10.0F, 10.0F, 13.0F, 0.0F, false);
        this.head.setTextureOffset(0, 0).addCuboid(-0.5F, -12.0F, 3.5F, 1.0F, 13.0F, 10.0F, 0.0F, false);
        this.head.setTextureOffset(0, 0).addCuboid(-3.5F, -9.0F, -6.5F, 7.0F, 10.0F, 4.0F, 0.0F, false);
        this.head.setTextureOffset(0, 0).addCuboid(-2.5F, -7.5F, -12.5F, 5.0F, 7.0F, 6.0F, 0.0F, false);

        this.horn = new ModelRendererPlus(this);
        this.horn.setDefaultRotPoint(0.0F, -12.0F, -2.0F);
        this.head.addChild(this.horn);
        this.horn.setTextureOffset(0, 0).addCuboid(-1.0F, -6.0F, 0.5F, 3.0F, 9.0F, 3.0F, 0.0F, false);

        this.leftFrontLegBase = new ModelRendererPlus(this);
        this.leftFrontLegBase.setDefaultRotPoint(0.0F, -12.0F, 0.0F);
        this.body.addChild(this.leftFrontLegBase);
        this.leftFrontLegBase.setTextureOffset(0, 0).addCuboid(-1.5F, -1.0F, -0.5F, 4.0F, 4.0F, 4.0F, 0.0F, false);

        this.leftFrontLeg = new ModelRendererPlus(this);
        this.leftFrontLeg.setDefaultRotPoint(0.0F, 3.0F, 3.0F);
        this.leftFrontLegBase.addChild(this.leftFrontLeg);
        this.leftFrontLeg.setTextureOffset(0, 0).addCuboid(-1.0F, 0.0F, -3.0F, 3.0F, 5.0F, 3.0F, 0.0F, false);

        this.leftFrontLegDown = new ModelRendererPlus(this);
        this.leftFrontLegDown.setDefaultRotPoint(0.5F, 5.0F, -3.0F);
        this.leftFrontLeg.addChild(this.leftFrontLegDown);
        this.leftFrontLegDown.setTextureOffset(0, 0).addCuboid(-1.5F, 0.0F, 0.0F, 3.0F, 7.0F, 3.0F, 0.0F, false);

        this.leftFrontLegFur = new ModelRendererPlus(this);
        this.leftFrontLegFur.setDefaultRotPoint(0.0F, 0.0F, 3.0F);
        this.leftFrontLegDown.addChild(this.leftFrontLegFur);
        this.leftFrontLegFur.setTextureOffset(0, 0).addCuboid(0.0F, -1.0F, 0.0F, 0.0F, 4.0F, 5.0F, 0.0F, false);

        this.leftFrontHoove = new ModelRendererPlus(this);
        this.leftFrontHoove.setDefaultRotPoint(0.0F, 6.0F, 1.5F);
        this.leftFrontLegDown.addChild(this.leftFrontHoove);
        this.leftFrontHoove.setTextureOffset(0, 0).addCuboid(-2.0F, 0.0F, -2.0F, 4.0F, 2.0F, 4.0F, 0.0F, false);

        this.rightFrontLegBase = new ModelRendererPlus(this);
        this.rightFrontLegBase.setDefaultRotPoint(-10.0F, -12.0F, 0.0F);
        this.body.addChild(this.rightFrontLegBase);
        this.rightFrontLegBase.setTextureOffset(0, 0).addCuboid(-1.5F, -1.0F, -0.5F, 4.0F, 4.0F, 4.0F, 0.0F, false);

        this.rightFrontLeg = new ModelRendererPlus(this);
        this.rightFrontLeg.setDefaultRotPoint(0.0F, 3.0F, 3.0F);
        this.rightFrontLegBase.addChild(this.rightFrontLeg);
        this.rightFrontLeg.setTextureOffset(0, 0).addCuboid(-1.0F, 0.0F, -3.0F, 3.0F, 5.0F, 3.0F, 0.0F, false);

        this.rightFrontLegDown = new ModelRendererPlus(this);
        this.rightFrontLegDown.setDefaultRotPoint(0.5F, 5.0F, -3.0F);
        this.rightFrontLeg.addChild(this.rightFrontLegDown);
        this.rightFrontLegDown.setTextureOffset(0, 0).addCuboid(-1.5F, 0.0F, 0.0F, 3.0F, 7.0F, 3.0F, 0.0F, false);

        this.rightFrontLegFur = new ModelRendererPlus(this);
        this.rightFrontLegFur.setDefaultRotPoint(0.0F, 0.0F, 3.0F);
        this.rightFrontLegDown.addChild(this.rightFrontLegFur);
        this.rightFrontLegFur.setTextureOffset(0, 0).addCuboid(0.0F, -1.0F, 0.0F, 0.0F, 4.0F, 5.0F, 0.0F, false);

        this.rightFrontHoove = new ModelRendererPlus(this);
        this.rightFrontHoove.setDefaultRotPoint(0.0F, 6.0F, 1.5F);
        this.rightFrontLegDown.addChild(this.rightFrontHoove);
        this.rightFrontHoove.setTextureOffset(0, 0).addCuboid(-2.0F, 0.0F, -2.0F, 4.0F, 2.0F, 4.0F, 0.0F, false);

        this.leftBackLegBase = new ModelRendererPlus(this);
        this.leftBackLegBase.setDefaultRotPoint(0.0F, -12.0F, 22.6F);
        this.body.addChild(this.leftBackLegBase);
        this.leftBackLegBase.setTextureOffset(0, 0).addCuboid(-2.5F, -4.0F, -3.5F, 5.0F, 7.0F, 7.0F, 0.0F, false);

        this.leftBackLeg = new ModelRendererPlus(this);
        this.leftBackLeg.setDefaultRotPoint(0.0F, 3.0F, -1.5F);
        this.leftBackLegBase.addChild(this.leftBackLeg);
        this.setRotationAngle(this.leftBackLeg, 0.3927F, 0.0F, 0.0F);
        this.leftBackLeg.setTextureOffset(0, 0).addCuboid(-1.5F, 0.0F, 0.0F, 3.0F, 6.0F, 4.0F, 0.0F, false);

        this.leftBackLegDown = new ModelRendererPlus(this);
        this.leftBackLegDown.setDefaultRotPoint(0.0F, 6.0F, 4.0F);
        this.leftBackLeg.addChild(this.leftBackLegDown);
        this.setRotationAngle(this.leftBackLegDown, -0.3927F, 0.0F, 0.0F);
        this.leftBackLegDown.setTextureOffset(0, 0).addCuboid(-1.5F, 0.0F, -3.0F, 3.0F, 8.0F, 3.0F, 0.0F, false);
        this.leftBackLegDown.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, -1.0F, 0.0F, 3.0F, 6.0F, 0.0F, false);

        this.leftBackHoove = new ModelRendererPlus(this);
        this.leftBackHoove.setDefaultRotPoint(-0.5F, 7.3007F, -1.5463F);
        this.leftBackLegDown.addChild(this.leftBackHoove);
        this.leftBackHoove.setTextureOffset(0, 0).addCuboid(-1.5F, -0.3007F, -1.9537F, 4.0F, 2.0F, 4.0F, 0.0F, false);

        this.rightBackLegBase = new ModelRendererPlus(this);
        this.rightBackLegBase.setDefaultRotPoint(-9.0F, -12.0F, 22.6F);
        this.body.addChild(this.rightBackLegBase);
        this.rightBackLegBase.setTextureOffset(0, 0).addCuboid(-2.5F, -4.0F, -3.5F, 5.0F, 7.0F, 7.0F, 0.0F, false);

        this.rightBackLeg = new ModelRendererPlus(this);
        this.rightBackLeg.setDefaultRotPoint(0.0F, 3.0F, -1.5F);
        this.rightBackLegBase.addChild(this.rightBackLeg);
        this.setRotationAngle(this.rightBackLeg, 0.3927F, 0.0F, 0.0F);
        this.rightBackLeg.setTextureOffset(0, 0).addCuboid(-1.5F, 0.0F, 0.0F, 3.0F, 6.0F, 4.0F, 0.0F, false);

        this.rightBackLegDown = new ModelRendererPlus(this);
        this.rightBackLegDown.setDefaultRotPoint(0.0F, 6.0F, 4.0F);
        this.rightBackLeg.addChild(this.rightBackLegDown);
        this.setRotationAngle(this.rightBackLegDown, -0.3927F, 0.0F, 0.0F);
        this.rightBackLegDown.setTextureOffset(0, 0).addCuboid(-1.5F, 0.0F, -3.0F, 3.0F, 8.0F, 3.0F, 0.0F, false);
        this.rightBackLegDown.setTextureOffset(0, 0).addCuboid(-1.0F, 0.0F, -1.0F, 1.0F, 3.0F, 6.0F, 0.0F, false);

        this.rightBackHoove = new ModelRendererPlus(this);
        this.rightBackHoove.setDefaultRotPoint(0.0F, 7.0F, -1.5F);
        this.rightBackLegDown.addChild(this.rightBackHoove);
        this.rightBackHoove.setTextureOffset(0, 0).addCuboid(-2.0F, 0.0F, -2.0F, 4.0F, 2.0F, 4.0F, 0.0F, false);

        this.tail = new ModelRendererPlus(this);
        this.tail.setDefaultRotPoint(-4.0F, -21.5F, 26.0F);
        this.body.addChild(this.tail);
        this.setRotationAngle(this.tail, 0.6545F, 0.0F, 0.0F);
        this.tail.setTextureOffset(0, 0).addCuboid(-2.5F, 0.0F, -1.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);
        this.tail.setTextureOffset(0, 0).addCuboid(-1.0F, -1.0F, 0.0F, 1.0F, 7.0F, 4.0F, 0.0F, false);

        this.tailTip = new ModelRendererPlus(this);
        this.tailTip.setDefaultRotPoint(0.0F, 6.0F, 3.0F);
        this.tail.addChild(this.tailTip);
        this.setRotationAngle(this.tailTip, -0.5236F, 0.0F, 0.0F);
        this.tailTip.setTextureOffset(0, 0).addCuboid(-1.5F, 0.0F, -3.5F, 2.0F, 13.0F, 3.0F, 0.0F, false);
        this.tailTip.setTextureOffset(0, 0).addCuboid(-1.0F, -1.0F, -3.5F, 1.0F, 14.0F, 4.0F, 0.0F, false);

        this.animations = new BlockBenchAnimations(this, new ResourceLocation(RuneCraftory.MODID, "models/entity/animation/thunderbolt.json"));
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.body.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public void setAngles(T thunderbolt, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetModel();

        AnimatedAction anim = thunderbolt.getAnimation();
        float partialTicks = Minecraft.getInstance().getRenderPartialTicks();
        if (anim == null) {
            if (thunderbolt.isMoving())
                this.animations.doAnimation("walk", thunderbolt.ticksExisted, partialTicks);
        } else
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
