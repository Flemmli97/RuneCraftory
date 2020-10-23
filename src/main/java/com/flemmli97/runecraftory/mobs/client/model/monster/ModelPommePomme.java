package com.flemmli97.runecraftory.mobs.client.model.monster;// Made with Blockbench 3.5.2

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.mobs.entity.monster.EntityPommePomme;
import com.flemmli97.tenshilib.client.model.BlockBenchAnimations;
import com.flemmli97.tenshilib.client.model.IResetModel;
import com.flemmli97.tenshilib.client.model.ModelRendererPlus;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;

public class ModelPommePomme<T extends EntityPommePomme> extends EntityModel<T> implements IResetModel {
    public ModelRendererPlus body;
    public ModelRendererPlus inner;
    public ModelRendererPlus stem;
    public ModelRendererPlus hairRight;
    public ModelRendererPlus hairLeft;
    public ModelRendererPlus handLeft;
    public ModelRendererPlus handRight;
    public ModelRendererPlus feetLeft;
    public ModelRendererPlus feetRight;

    public BlockBenchAnimations animations;

    public ModelPommePomme() {
        this.textureWidth = 126;
        this.textureHeight = 52;

        this.body = new ModelRendererPlus(this);
        this.body.setDefaultRotPoint(0.0F, 13.0F, 0.0F);
        this.body.setTextureOffset(0, 0).addCuboid(-7.0F, -6.0F, -7.0F, 14.0F, 14.0F, 14.0F, 0.0F, false);
        this.body.setTextureOffset(0, 28).addCuboid(7.0F, -5.0F, -6.0F, 1.0F, 12.0F, 12.0F, 0.0F, false);
        this.body.setTextureOffset(26, 28).addCuboid(8.0F, -3.0F, -4.0F, 1.0F, 8.0F, 8.0F, 0.0F, false);
        this.body.setTextureOffset(0, 28).addCuboid(-8.0F, -5.0F, -6.0F, 1.0F, 12.0F, 12.0F, 0.0F, true);
        this.body.setTextureOffset(26, 28).addCuboid(-9.0F, -3.0F, -4.0F, 1.0F, 8.0F, 8.0F, 0.0F, true);
        this.body.setTextureOffset(56, 0).addCuboid(-6.0F, -5.0F, -8.0F, 12.0F, 12.0F, 1.0F, 0.0F, false);
        this.body.setTextureOffset(56, 13).addCuboid(-4.0F, -3.0F, -9.0F, 8.0F, 8.0F, 1.0F, 0.0F, false);
        this.body.setTextureOffset(56, 0).addCuboid(-6.0F, -5.0F, 7.0F, 12.0F, 12.0F, 1.0F, 0.0F, true);
        this.body.setTextureOffset(56, 13).addCuboid(-4.0F, -3.0F, 8.0F, 8.0F, 8.0F, 1.0F, 0.0F, true);
        this.body.setTextureOffset(74, 23).addCuboid(-6.0F, -7.0F, -6.0F, 12.0F, 1.0F, 12.0F, 0.0F, false);
        this.body.setTextureOffset(44, 36).addCuboid(-6.0F, 8.0F, -6.0F, 12.0F, 1.0F, 12.0F, 0.0F, false);
        this.body.setTextureOffset(92, 36).addCuboid(-4.0F, 9.0F, -4.0F, 8.0F, 1.0F, 8.0F, 0.0F, false);

        this.inner = new ModelRendererPlus(this);
        this.inner.setDefaultRotPoint(0.0F, -7.0F, 0.0F);
        this.body.addChild(this.inner);
        this.inner.setTextureOffset(102, 0).addCuboid(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F, 0.0F, false);
        this.inner.setTextureOffset(74, 14).addCuboid(-4.0F, -7.0F, -4.0F, 8.0F, 1.0F, 8.0F, 0.0F, false);

        this.stem = new ModelRendererPlus(this);
        this.stem.setDefaultRotPoint(0.0F, -8.0F, 0.0F);
        this.inner.addChild(this.stem);
        this.stem.setTextureOffset(106, 12).addCuboid(-0.5F, -4.0F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        this.hairRight = new ModelRendererPlus(this);
        this.hairRight.setDefaultRotPoint(-4.0F, -6.0F, 0.0F);
        this.inner.addChild(this.hairRight);
        this.setRotationAngle(this.hairRight, 0.0F, 0.0F, -0.2618F);
        this.hairRight.setTextureOffset(56, 23).addCuboid(-6.0F, 0.0F, -4.0F, 6.0F, 0.0F, 8.0F, 0.0F, true);

        this.hairLeft = new ModelRendererPlus(this);
        this.hairLeft.setDefaultRotPoint(4.0F, -6.0F, 0.0F);
        this.inner.addChild(this.hairLeft);
        this.setRotationAngle(this.hairLeft, 0.0F, 0.0F, 0.3491F);
        this.hairLeft.setTextureOffset(56, 23).addCuboid(0.0F, 0.0F, -4.0F, 6.0F, 0.0F, 8.0F, 0.0F, false);

        this.handLeft = new ModelRendererPlus(this);
        this.handLeft.setDefaultRotPoint(3.0F, -9.5F, -4.0F);
        this.body.addChild(this.handLeft);
        this.setRotationAngle(this.handLeft, 0.1745F, -0.1745F, 0.0F);
        this.handLeft.setTextureOffset(82, 8).addCuboid(-1.0F, 1.4696F, -5.3473F, 2.0F, 1.0F, 5.0F, 0.0F, false);

        this.handRight = new ModelRendererPlus(this);
        this.handRight.setDefaultRotPoint(-3.0F, -9.5F, -4.0F);
        this.body.addChild(this.handRight);
        this.setRotationAngle(this.handRight, 0.1745F, 0.1745F, 0.0F);
        this.handRight.setTextureOffset(82, 8).addCuboid(-1.0F, 1.4696F, -5.3473F, 2.0F, 1.0F, 5.0F, 0.0F, false);

        this.feetLeft = new ModelRendererPlus(this);
        this.feetLeft.setDefaultRotPoint(3.5F, 10.0F, 1.0F);
        this.body.addChild(this.feetLeft);
        this.feetLeft.setTextureOffset(82, 0).addCuboid(-1.5F, 0.0F, -7.0F, 3.0F, 1.0F, 7.0F, 0.0F, false);

        this.feetRight = new ModelRendererPlus(this);
        this.feetRight.setDefaultRotPoint(-3.5F, 10.0F, 1.0F);
        this.body.addChild(this.feetRight);
        this.feetRight.setTextureOffset(82, 0).addCuboid(-1.5F, 0.0F, -7.0F, 3.0F, 1.0F, 7.0F, 0.0F, true);

        this.animations = new BlockBenchAnimations(this, new ResourceLocation(RuneCraftory.MODID, "models/entity/animation/pomme_pomme.json"));
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.body.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public void setAngles(T pommePomme, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetModel();

        float partialTicks = Minecraft.getInstance().getRenderPartialTicks();
        this.animations.doAnimation("iddle", pommePomme.ticksExisted, partialTicks);
        if (pommePomme.isMoving())
            this.animations.doAnimation("walk", pommePomme.ticksExisted, partialTicks);
        AnimatedAction anim = pommePomme.getAnimation();
        if (anim != null) {
            if (anim.getID().equals("roll")) {
                if (anim.getTick() > anim.getAttackTime())
                    this.animations.doAnimation(anim.getID(), anim.getTick() - anim.getAttackTime(), partialTicks);
            } else
                this.animations.doAnimation(anim.getID(), anim.getTick(), partialTicks);
        }
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