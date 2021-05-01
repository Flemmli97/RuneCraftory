package com.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.entities.monster.EntityBigMuck;
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

public class ModelBigMuck<T extends EntityBigMuck> extends EntityModel<T> implements IResetModel {
    public ModelRendererPlus body;
    public ModelRendererPlus mushroomCap;
    public ModelRendererPlus handLeft;
    public ModelRendererPlus handRight;
    public ModelRendererPlus tail;
    public ModelRendererPlus tailCap;

    public BlockBenchAnimations animations;

    public ModelBigMuck() {
        this.textureWidth = 76;
        this.textureHeight = 125;

        this.body = new ModelRendererPlus(this);
        this.body.setDefaultRotPoint(0.0F, 24.0F, 0.0F);
        this.body.setTextureOffset(0, 0).addCuboid(-6.0F, -14.0F, -5.0F, 11.0F, 14.0F, 11.0F, 0.0F, false);
        this.body.setTextureOffset(0, 25).addCuboid(-5.0F, -14.0F, -6.0F, 9.0F, 14.0F, 1.0F, 0.0F, false);
        this.body.setTextureOffset(44, 0).addCuboid(5.0F, -14.0F, -4.0F, 1.0F, 14.0F, 9.0F, 0.0F, false);
        this.body.setTextureOffset(44, 0).addCuboid(-7.0F, -14.0F, -4.0F, 1.0F, 14.0F, 9.0F, 0.0F, false);
        this.body.setTextureOffset(20, 25).addCuboid(-5.0F, -14.0F, 6.0F, 9.0F, 14.0F, 1.0F, 0.0F, false);

        this.mushroomCap = new ModelRendererPlus(this);
        this.mushroomCap.setDefaultRotPoint(0.0F, 0.0F, 0.0F);
        this.body.addChild(this.mushroomCap);
        this.mushroomCap.setTextureOffset(0, 48).addCuboid(-10.0F, -16.0F, -9.0F, 19.0F, 2.0F, 19.0F, 0.0F, false);
        this.mushroomCap.setTextureOffset(0, 69).addCuboid(-8.0F, -18.0F, -7.0F, 15.0F, 2.0F, 15.0F, 0.0F, false);
        this.mushroomCap.setTextureOffset(0, 86).addCuboid(-7.0F, -21.0F, -6.0F, 13.0F, 3.0F, 13.0F, 0.0F, false);
        this.mushroomCap.setTextureOffset(0, 102).addCuboid(-6.0F, -24.0F, -5.0F, 11.0F, 3.0F, 11.0F, 0.0F, false);
        this.mushroomCap.setTextureOffset(0, 116).addCuboid(-4.0F, -26.0F, -3.0F, 7.0F, 2.0F, 7.0F, 0.0F, false);

        this.handLeft = new ModelRendererPlus(this);
        this.handLeft.setDefaultRotPoint(6.0F, -8.5F, 0.5F);
        this.body.addChild(this.handLeft);
        this.setRotationAngle(this.handLeft, 0.0F, 0.0F, 0.2618F);
        this.handLeft.setTextureOffset(0, 40).addCuboid(0.0F, -0.5F, -3.5F, 8.0F, 1.0F, 7.0F, 0.0F, true);

        this.handRight = new ModelRendererPlus(this);
        this.handRight.setDefaultRotPoint(-7.0F, -8.5F, 0.5F);
        this.body.addChild(this.handRight);
        this.setRotationAngle(this.handRight, 0.0F, 0.0F, -0.2618F);
        this.handRight.setTextureOffset(0, 40).addCuboid(-8.0F, -0.5F, -3.5F, 8.0F, 1.0F, 7.0F, 0.0F, false);

        this.tail = new ModelRendererPlus(this);
        this.tail.setDefaultRotPoint(-0.5F, -1.0F, 7.0F);
        this.body.addChild(this.tail);
        this.setRotationAngle(this.tail, 0.6981F, 0.0F, 0.0F);
        this.tail.setTextureOffset(44, 23).addCuboid(-0.5F, -1.0F, 0.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

        this.tailCap = new ModelRendererPlus(this);
        this.tailCap.setDefaultRotPoint(0.5F, 0.0F, 0.0F);
        this.tail.addChild(this.tailCap);
        this.tailCap.setTextureOffset(50, 23).addCuboid(-1.5F, -1.5F, 2.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);

        this.animations = new BlockBenchAnimations(this, new ResourceLocation(RuneCraftory.MODID, "models/entity/animation/big_muck.json"));
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.body.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public void setAngles(T mushroom, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetModel();
        float partialTicks = Minecraft.getInstance().getRenderPartialTicks();
        this.animations.doAnimation("iddle", mushroom.ticksExisted, partialTicks);
        if (mushroom.isMoving())
            this.animations.doAnimation("walk", mushroom.ticksExisted, partialTicks);

        this.tail.rotateAngleX += MathHelper.cos(ageInTicks * 0.09F) * 0.15F + 0.05F;
        this.tail.rotateAngleY += MathHelper.sin(ageInTicks * 0.067F) * 0.15F;

        AnimatedAction anim = mushroom.getAnimation();
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