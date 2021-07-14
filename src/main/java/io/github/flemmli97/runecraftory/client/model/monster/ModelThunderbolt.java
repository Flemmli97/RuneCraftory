package io.github.flemmli97.runecraftory.client.model.monster;

import com.flemmli97.tenshilib.api.entity.AnimatedAction;
import com.flemmli97.tenshilib.client.model.BlockBenchAnimations;
import com.flemmli97.tenshilib.client.model.IResetModel;
import com.flemmli97.tenshilib.client.model.ModelRendererPlus;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.EntityThunderbolt;
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

        body = new ModelRendererPlus(this);
        body.setDefaultRotPoint(0.0F, 4.0F, 0.0F);
        body.setTextureOffset(0, 0).addBox(-6.5F, -7.0F, -13.0F, 13.0F, 13.0F, 26.0F, 0.0F, false);

        backFur = new ModelRendererPlus(this);
        backFur.setDefaultRotPoint(0.0F, -7.0F, 3.0F);
        body.addChild(backFur);
        backFur.setTextureOffset(0, 0).addBox(-0.5F, -2.0F, -12.0F, 1.0F, 3.0F, 24.0F, 0.0F, false);

        neck = new ModelRendererPlus(this);
        neck.setDefaultRotPoint(-1.0F, 0.0F, -13.0F);
        body.addChild(neck);
        setRotationAngle(neck, 0.5236F, 0.0F, 0.0F);
        neck.setTextureOffset(0, 0).addBox(-2.5F, -12.0F, 0.0F, 7.0F, 12.0F, 9.0F, 0.0F, false);
        neck.setTextureOffset(0, 0).addBox(0.5F, -12.0F, 3.0F, 1.0F, 12.0F, 10.0F, 0.0F, false);

        head = new ModelRendererPlus(this);
        head.setDefaultRotPoint(1.0F, -13.0F, 0.0F);
        neck.addChild(head);
        head.setTextureOffset(0, 0).addBox(-5.0F, -9.0F, -2.5F, 10.0F, 10.0F, 13.0F, 0.0F, false);
        head.setTextureOffset(0, 0).addBox(-0.5F, -12.0F, 3.5F, 1.0F, 13.0F, 10.0F, 0.0F, false);
        head.setTextureOffset(0, 0).addBox(-3.5F, -9.0F, -6.5F, 7.0F, 10.0F, 4.0F, 0.0F, false);
        head.setTextureOffset(0, 0).addBox(-2.5F, -8.5F, -12.5F, 5.0F, 9.0F, 6.0F, 0.0F, false);

        horn = new ModelRendererPlus(this);
        horn.setDefaultRotPoint(0.0F, -12.0F, -2.0F);
        head.addChild(horn);
        horn.setTextureOffset(0, 0).addBox(-1.5F, -6.0F, 0.5F, 3.0F, 9.0F, 3.0F, 0.0F, false);

        leftFrontLegBase = new ModelRendererPlus(this);
        leftFrontLegBase.setDefaultRotPoint(4.5F, 4.0F, -13.0F);
        body.addChild(leftFrontLegBase);
        leftFrontLegBase.setTextureOffset(0, 0).addBox(-1.5F, -1.0F, -0.5F, 4.0F, 4.0F, 4.0F, 0.0F, false);

        leftFrontLeg = new ModelRendererPlus(this);
        leftFrontLeg.setDefaultRotPoint(0.0F, 3.0F, 3.0F);
        leftFrontLegBase.addChild(leftFrontLeg);
        leftFrontLeg.setTextureOffset(0, 0).addBox(-1.0F, 0.0F, -3.0F, 3.0F, 5.0F, 3.0F, 0.0F, false);

        leftFrontLegDown = new ModelRendererPlus(this);
        leftFrontLegDown.setDefaultRotPoint(0.5F, 5.0F, -3.0F);
        leftFrontLeg.addChild(leftFrontLegDown);
        leftFrontLegDown.setTextureOffset(0, 0).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 7.0F, 3.0F, 0.0F, false);

        leftFrontLegFur = new ModelRendererPlus(this);
        leftFrontLegFur.setDefaultRotPoint(0.0F, 0.0F, 3.0F);
        leftFrontLegDown.addChild(leftFrontLegFur);
        leftFrontLegFur.setTextureOffset(0, 0).addBox(0.0F, -1.0F, 0.0F, 0.0F, 4.0F, 5.0F, 0.0F, false);

        leftFrontHoove = new ModelRendererPlus(this);
        leftFrontHoove.setDefaultRotPoint(0.0F, 6.0F, 1.5F);
        leftFrontLegDown.addChild(leftFrontHoove);
        leftFrontHoove.setTextureOffset(0, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 2.0F, 4.0F, 0.0F, false);

        rightFrontLegBase = new ModelRendererPlus(this);
        rightFrontLegBase.setDefaultRotPoint(-5.5F, 4.0F, -13.0F);
        body.addChild(rightFrontLegBase);
        rightFrontLegBase.setTextureOffset(0, 0).addBox(-1.5F, -1.0F, -0.5F, 4.0F, 4.0F, 4.0F, 0.0F, false);

        rightFrontLeg = new ModelRendererPlus(this);
        rightFrontLeg.setDefaultRotPoint(0.0F, 3.0F, 3.0F);
        rightFrontLegBase.addChild(rightFrontLeg);
        rightFrontLeg.setTextureOffset(0, 0).addBox(-1.0F, 0.0F, -3.0F, 3.0F, 5.0F, 3.0F, 0.0F, false);

        rightFrontLegDown = new ModelRendererPlus(this);
        rightFrontLegDown.setDefaultRotPoint(0.5F, 5.0F, -3.0F);
        rightFrontLeg.addChild(rightFrontLegDown);
        rightFrontLegDown.setTextureOffset(0, 0).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 7.0F, 3.0F, 0.0F, false);

        rightFrontLegFur = new ModelRendererPlus(this);
        rightFrontLegFur.setDefaultRotPoint(0.0F, 0.0F, 3.0F);
        rightFrontLegDown.addChild(rightFrontLegFur);
        rightFrontLegFur.setTextureOffset(0, 0).addBox(0.0F, -1.0F, 0.0F, 0.0F, 4.0F, 5.0F, 0.0F, false);

        rightFrontHoove = new ModelRendererPlus(this);
        rightFrontHoove.setDefaultRotPoint(0.0F, 6.0F, 1.5F);
        rightFrontLegDown.addChild(rightFrontHoove);
        rightFrontHoove.setTextureOffset(0, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 2.0F, 4.0F, 0.0F, false);

        leftBackLegBase = new ModelRendererPlus(this);
        leftBackLegBase.setDefaultRotPoint(4.5F, 4.0F, 9.6F);
        body.addChild(leftBackLegBase);
        leftBackLegBase.setTextureOffset(0, 0).addBox(-2.5F, -4.0F, -3.5F, 5.0F, 7.0F, 7.0F, 0.0F, false);

        leftBackLeg = new ModelRendererPlus(this);
        leftBackLeg.setDefaultRotPoint(0.0F, 3.0F, -1.5F);
        leftBackLegBase.addChild(leftBackLeg);
        setRotationAngle(leftBackLeg, 0.3927F, 0.0F, 0.0F);
        leftBackLeg.setTextureOffset(0, 0).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 6.0F, 4.0F, 0.0F, false);

        leftBackLegDown = new ModelRendererPlus(this);
        leftBackLegDown.setDefaultRotPoint(0.0F, 6.0F, 4.0F);
        leftBackLeg.addChild(leftBackLegDown);
        setRotationAngle(leftBackLegDown, -0.3927F, 0.0F, 0.0F);
        leftBackLegDown.setTextureOffset(0, 0).addBox(-1.5F, 0.0F, -3.0F, 3.0F, 8.0F, 3.0F, 0.0F, false);
        leftBackLegDown.setTextureOffset(0, 0).addBox(0.0F, 0.0F, -1.0F, 0.0F, 3.0F, 6.0F, 0.0F, false);

        leftBackHoove = new ModelRendererPlus(this);
        leftBackHoove.setDefaultRotPoint(-0.5F, 7.3007F, -1.5463F);
        leftBackLegDown.addChild(leftBackHoove);
        leftBackHoove.setTextureOffset(0, 0).addBox(-1.5F, -0.3007F, -1.9537F, 4.0F, 2.0F, 4.0F, 0.0F, false);

        rightBackLegBase = new ModelRendererPlus(this);
        rightBackLegBase.setDefaultRotPoint(-4.5F, 4.0F, 9.6F);
        body.addChild(rightBackLegBase);
        rightBackLegBase.setTextureOffset(0, 0).addBox(-2.5F, -4.0F, -3.5F, 5.0F, 7.0F, 7.0F, 0.0F, false);

        rightBackLeg = new ModelRendererPlus(this);
        rightBackLeg.setDefaultRotPoint(0.0F, 3.0F, -1.5F);
        rightBackLegBase.addChild(rightBackLeg);
        setRotationAngle(rightBackLeg, 0.3927F, 0.0F, 0.0F);
        rightBackLeg.setTextureOffset(0, 0).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 6.0F, 4.0F, 0.0F, false);

        rightBackLegDown = new ModelRendererPlus(this);
        rightBackLegDown.setDefaultRotPoint(0.0F, 6.0F, 4.0F);
        rightBackLeg.addChild(rightBackLegDown);
        setRotationAngle(rightBackLegDown, -0.3927F, 0.0F, 0.0F);
        rightBackLegDown.setTextureOffset(0, 0).addBox(-1.5F, 0.0F, -3.0F, 3.0F, 8.0F, 3.0F, 0.0F, false);
        rightBackLegDown.setTextureOffset(0, 0).addBox(0.0F, 0.0F, -1.0F, 0.0F, 3.0F, 6.0F, 0.0F, false);

        rightBackHoove = new ModelRendererPlus(this);
        rightBackHoove.setDefaultRotPoint(0.0F, 7.0F, -1.5F);
        rightBackLegDown.addChild(rightBackHoove);
        rightBackHoove.setTextureOffset(0, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 2.0F, 4.0F, 0.0F, false);

        tail = new ModelRendererPlus(this);
        tail.setDefaultRotPoint(0.5F, -5.5F, 13.0F);
        body.addChild(tail);
        setRotationAngle(tail, 0.6545F, 0.0F, 0.0F);
        tail.setTextureOffset(0, 0).addBox(-2.5F, 0.0F, -1.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);
        tail.setTextureOffset(0, 0).addBox(-1.0F, -1.0F, 0.0F, 1.0F, 7.0F, 4.0F, 0.0F, false);

        tailTip = new ModelRendererPlus(this);
        tailTip.setDefaultRotPoint(0.0F, 6.0F, 3.0F);
        tail.addChild(tailTip);
        setRotationAngle(tailTip, -0.5236F, 0.0F, 0.0F);
        tailTip.setTextureOffset(0, 0).addBox(-1.5F, 0.0F, -3.5F, 2.0F, 13.0F, 3.0F, 0.0F, false);
        tailTip.setTextureOffset(0, 0).addBox(-1.0F, -1.0F, -3.5F, 1.0F, 14.0F, 4.0F, 0.0F, false);
        this.animations = new BlockBenchAnimations(this, new ResourceLocation(RuneCraftory.MODID, "models/entity/animation/thunderbolt.json"));
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.body.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public void setRotationAngles(T thunderbolt, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetModel();
        this.head.rotateAngleY += netHeadYaw * 0.002f;
        this.head.rotateAngleX += headPitch * 0.002f;
        this.neck.rotateAngleY += headPitch * 0.005f;
        this.neck.rotateAngleX += headPitch * 0.003f;

        AnimatedAction anim = thunderbolt.getAnimationHandler().getAnimation().orElse(null);
        float partialTicks = Minecraft.getInstance().getRenderPartialTicks();
        if (anim == null) {
            if (thunderbolt.isRunning())
                this.animations.doAnimation("run", thunderbolt.ticksExisted, partialTicks);
            else if (thunderbolt.isMoving())
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
