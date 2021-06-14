package io.github.flemmli97.runecraftory.client.model.monster;

// Made with Blockbench 3.5.2

import com.flemmli97.tenshilib.client.model.BlockBenchAnimations;
import com.flemmli97.tenshilib.client.model.IResetModel;
import com.flemmli97.tenshilib.client.model.ModelRendererPlus;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import com.flemmli97.tenshilib.common.utils.MathUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityWooly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class ModelWooly<T extends EntityWooly> extends EntityModel<T> implements IResetModel {
    public ModelRendererPlus bodyCenter;
    public ModelRendererPlus body;
    public ModelRendererPlus bodyUp;
    public ModelRendererPlus neck;
    public ModelRendererPlus head;
    public ModelRendererPlus earLeft;
    public ModelRendererPlus earRight;
    public ModelRendererPlus armLeftBase;
    public ModelRendererPlus armLeftUp;
    public ModelRendererPlus armLeftDown;
    public ModelRendererPlus armRightBase;
    public ModelRendererPlus armRightUp;
    public ModelRendererPlus armRightDown;
    public ModelRendererPlus feetLeftBase;
    public ModelRendererPlus feetLeft;
    public ModelRendererPlus feetRightBase;
    public ModelRendererPlus feetRight;
    public ModelRendererPlus tail;

    public BlockBenchAnimations animations;

    public ModelWooly() {
        this.textureWidth = 64;
        this.textureHeight = 62;

        this.bodyCenter = new ModelRendererPlus(this);
        this.bodyCenter.setDefaultRotPoint(0.0F, 17.75F, 0.0F);

        this.body = new ModelRendererPlus(this);
        this.body.setDefaultRotPoint(0.0F, 0.0F, 0.0F);
        this.bodyCenter.addChild(this.body);
        this.body.setTextureOffset(0, 31).addBox(-3.5F, -7.0F, -4.5F, 7.0F, 13.0F, 9.0F, 0.0F, true);
        this.body.setTextureOffset(32, 30).addBox(-4.5F, -7.0F, -3.5F, 1.0F, 13.0F, 7.0F, 0.0F, true);
        this.body.setTextureOffset(32, 30).addBox(3.5F, -7.0F, -3.5F, 1.0F, 13.0F, 7.0F, 0.0F, false);

        this.bodyUp = new ModelRendererPlus(this);
        this.bodyUp.setDefaultRotPoint(0.0F, -8.0F, 0.0F);
        this.body.addChild(this.bodyUp);
        this.bodyUp.setTextureOffset(32, 14).addBox(-2.5F, -1.0F, -3.5F, 5, 2, 7, 0.0F, true);
        this.bodyUp.setTextureOffset(28, 23).addBox(2.5F, -1.0F, -2.5F, 1, 2, 5, 0.0F, false);
        this.bodyUp.setTextureOffset(28, 23).addBox(-3.5F, -1.0F, -2.5F, 1, 2, 5, 0.0F, true);

        this.neck = new ModelRendererPlus(this);
        this.neck.setDefaultRotPoint(0.0F, -1.5F, 0.0F);
        this.bodyUp.addChild(this.neck);
        this.neck.setTextureOffset(0, 24).addBox(-2.5F, -1.0F, -2.5F, 5, 2, 5, 0.0F, true);

        this.head = new ModelRendererPlus(this);
        this.head.setDefaultRotPoint(0.0F, -1.0F, 0.0F);
        this.neck.addChild(this.head);
        this.head.setTextureOffset(0, 0).addBox(-3.5F, -7.0F, -4.5F, 7, 7, 9, 0.0F, true);
        this.head.setTextureOffset(32, 0).addBox(-4.5F, -7.0F, -3.0F, 1, 7, 7, 0.0F, true);
        this.head.setTextureOffset(32, 0).addBox(3.5F, -7.0F, -3.5F, 1, 7, 7, 0.0F, false);
        this.head.setTextureOffset(0, 16).addBox(-3.5F, -8.0F, -3.5F, 7, 1, 7, 0.0F, true);

        this.earLeft = new ModelRendererPlus(this);
        this.earLeft.setDefaultRotPoint(4.0F, -4.0F, 0.0F);
        this.head.addChild(this.earLeft);
        this.setRotationAngle(this.earLeft, 0.0F, 0.0F, -0.3491F);
        this.earLeft.setTextureOffset(48, 0).addBox(0.0F, -0.5F, -1.0F, 5, 1, 2, 0.0F, true);

        this.earRight = new ModelRendererPlus(this);
        this.earRight.setDefaultRotPoint(-3.5F, -4.0F, 0.0F);
        this.head.addChild(this.earRight);
        this.setRotationAngle(this.earRight, 0.0F, 0.0F, 0.3491F);
        this.earRight.setTextureOffset(48, 0).addBox(-5.0F, -0.5F, -1.0F, 5, 1, 2, 0.0F, false);

        this.armLeftBase = new ModelRendererPlus(this);
        this.armLeftBase.setDefaultRotPoint(3.75F, -3.0F, 0.0F);
        this.body.addChild(this.armLeftBase);
        this.setRotationAngle(this.armLeftBase, 0.1745F, 0.0F, 0.0F);
        this.armLeftBase.setTextureOffset(34, 50).addBox(0.25F, -1.0F, -1.0F, 2, 2, 2, 0.0F, false);

        this.armLeftUp = new ModelRendererPlus(this);
        this.armLeftUp.setDefaultRotPoint(1.0F, 0.0F, -0.5F);
        this.armLeftBase.addChild(this.armLeftUp);
        this.setRotationAngle(this.armLeftUp, 0.1745F, 0.1745F, 0.0F);
        this.armLeftUp.setTextureOffset(54, 23).addBox(-0.25F, -0.5F, -4.0F, 1, 1, 4, 0.0F, false);

        this.armLeftDown = new ModelRendererPlus(this);
        this.armLeftDown.setDefaultRotPoint(0.75F, 0.0F, -4.0F);
        this.armLeftUp.addChild(this.armLeftDown);
        this.setRotationAngle(this.armLeftDown, 0.4363F, 0.5236F, 0.0F);
        this.armLeftDown.setTextureOffset(54, 28).addBox(-1.0F, -0.5F, -4.0F, 1, 1, 4, 0.0F, false);

        this.armRightBase = new ModelRendererPlus(this);
        this.armRightBase.setDefaultRotPoint(-3.75F, -3.0F, 0.0F);
        this.body.addChild(this.armRightBase);
        this.setRotationAngle(this.armRightBase, 0.1745F, 0.0F, 0.0F);
        this.armRightBase.setTextureOffset(34, 50).addBox(-2.25F, -1.0F, -1.0F, 2, 2, 2, 0.0F, false);

        this.armRightUp = new ModelRendererPlus(this);
        this.armRightUp.setDefaultRotPoint(-1.0F, 0.0F, -0.5F);
        this.armRightBase.addChild(this.armRightUp);
        this.setRotationAngle(this.armRightUp, 0.1745F, -0.1745F, 0.0F);
        this.armRightUp.setTextureOffset(54, 23).addBox(-0.75F, -0.5F, -4.0F, 1, 1, 4, 0.0F, false);

        this.armRightDown = new ModelRendererPlus(this);
        this.armRightDown.setDefaultRotPoint(-0.75F, 0.0F, -4.0F);
        this.armRightUp.addChild(this.armRightDown);
        this.setRotationAngle(this.armRightDown, 0.4363F, -0.5236F, 0.0F);
        this.armRightDown.setTextureOffset(54, 28).addBox(0.0F, -0.5F, -4.0F, 1, 1, 4, 0.0F, true);

        this.feetLeftBase = new ModelRendererPlus(this);
        this.feetLeftBase.setDefaultRotPoint(4.0F, 4.75F, 0.0F);
        this.body.addChild(this.feetLeftBase);
        this.feetLeftBase.setTextureOffset(42, 50).addBox(-1.5F, -5.5F, -2.5F, 3, 7, 5, 0.0F, false);

        this.feetLeft = new ModelRendererPlus(this);
        this.feetLeft.setDefaultRotPoint(0.0F, 0.5F, 0.0F);
        this.feetLeftBase.addChild(this.feetLeft);
        this.feetLeft.setTextureOffset(16, 53).addBox(-1.0F, 0.0F, -7.0F, 2, 1, 7, 0.0F, false);

        this.feetRightBase = new ModelRendererPlus(this);
        this.feetRightBase.setDefaultRotPoint(-4.0F, 4.75F, 0.0F);
        this.body.addChild(this.feetRightBase);
        this.feetRightBase.setTextureOffset(42, 50).addBox(-1.5F, -5.5F, -2.5F, 3, 7, 5, 0.0F, true);

        this.feetRight = new ModelRendererPlus(this);
        this.feetRight.setDefaultRotPoint(0.0F, 0.5F, 0.0F);
        this.feetRightBase.addChild(this.feetRight);
        this.feetRight.setTextureOffset(16, 53).addBox(-1.0F, 0.0F, -7.0F, 2, 1, 7, 0.0F, false);

        this.tail = new ModelRendererPlus(this);
        this.tail.setDefaultRotPoint(0.0F, 3.0F, 4.0F);
        this.body.addChild(this.tail);
        this.tail.setTextureOffset(52, 3).addBox(-1.5F, -1.5F, 0.0F, 3, 3, 3, 0.0F, true);

        this.animations = new BlockBenchAnimations(this, new ResourceLocation(RuneCraftory.MODID, "models/entity/animation/wooly.json"));
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.bodyCenter.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public void setRotationAngles(T wooly, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetModel();
        this.head.rotateAngleY += netHeadYaw * 0.008453292F;
        this.head.rotateAngleX += headPitch * 0.012453292F;
        this.earLeft.rotateAngleZ += Math.abs(MathHelper.sin(ageInTicks * 0.04F) * 0.1735987755982989F);
        this.earRight.rotateAngleZ -= Math.abs(MathHelper.sin(ageInTicks * 0.04F) * 0.1735987755982989F);

        //this.armLeftUp.rotateAngleX += MathHelper.cos(limbSwing * 0.6662F) * 0.4F * limbSwingAmount ;
        //this.armRightUp.rotateAngleX += MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 0.4F * limbSwingAmount;

        this.tail.rotateAngleY += MathHelper.cos(ageInTicks * 0.6662F) * MathUtils.degToRad(10);
        this.armRightUp.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.armLeftUp.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.armRightUp.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
        this.armLeftUp.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;

        //this.feetRight.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.0F * limbSwingAmount;
        //this.feetLeft.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.0F * limbSwingAmount;

        float partialTicks = Minecraft.getInstance().getRenderPartialTicks();
        if (wooly.isMoving())
            this.animations.doAnimation("walk", wooly.ticksExisted, partialTicks);

        AnimatedAction anim = wooly.getAnimation();
        if (anim != null)
            this.animations.doAnimation(anim.getAnimationClient(), anim.getTick(), partialTicks);
    }

    public void setRotationAngle(ModelRendererPlus modelRenderer, float x, float y, float z) {
        modelRenderer.setDefaultRotAngle(x, y, z);
    }

    @Override
    public void resetModel() {
        this.bodyCenter.reset();
        this.resetChild(this.bodyCenter);
    }
}
