package com.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.7.4

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.entities.monster.EntityTortas;
import com.flemmli97.runecraftory.common.entities.monster.boss.EntityAmbrosia;
import com.flemmli97.tenshilib.client.model.BlockBenchAnimations;
import com.flemmli97.tenshilib.client.model.IResetModel;
import com.flemmli97.tenshilib.client.model.ModelRendererPlus;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;

public class ModelTortas<T extends EntityTortas> extends EntityModel<T> implements IResetModel {

    public ModelRendererPlus body;
    public ModelRendererPlus neck;
    public ModelRendererPlus head;
    public ModelRendererPlus jaw;
    public ModelRendererPlus leftLegFrontBase;
    public ModelRendererPlus leftLegFront;
    public ModelRendererPlus leftLegBackBase;
    public ModelRendererPlus leftLegBack;
    public ModelRendererPlus rightLegFrontBase;
    public ModelRendererPlus rightLegFront;
    public ModelRendererPlus rightLegBackBase;
    public ModelRendererPlus rightLegBack;
    public ModelRendererPlus tail;
    public ModelRendererPlus tail2;
    public ModelRendererPlus tail3;

    public BlockBenchAnimations animations;

    public ModelTortas() {
        this.textureWidth = 128;
        this.textureHeight = 108;

        this.body = new ModelRendererPlus(this);
        this.body.setDefaultRotPoint(0.0F, 24.0F, 0.0F);
        this.body.setTextureOffset(0, 0).addCuboid(-9.0F, -6.0F, -11.0F, 18.0F, 4.0F, 24.0F, 0.0F, false);
        this.body.setTextureOffset(84, 4).addCuboid(9.0F, -6.0F, -9.0F, 2.0F, 4.0F, 20.0F, 0.0F, false);
        this.body.setTextureOffset(84, 4).addCuboid(-11.0F, -6.0F, -9.0F, 2.0F, 4.0F, 20.0F, 0.0F, true);
        this.body.setTextureOffset(0, 28).addCuboid(-8.0F, -9.0F, -10.0F, 16.0F, 3.0F, 22.0F, 0.0F, false);
        this.body.setTextureOffset(76, 32).addCuboid(8.0F, -9.0F, -8.0F, 2.0F, 3.0F, 18.0F, 0.0F, false);
        this.body.setTextureOffset(76, 32).addCuboid(-10.0F, -9.0F, -8.0F, 2.0F, 3.0F, 18.0F, 0.0F, true);
        this.body.setTextureOffset(0, 53).addCuboid(-7.0F, -11.0F, -9.0F, 14.0F, 2.0F, 20.0F, 0.0F, false);
        this.body.setTextureOffset(68, 57).addCuboid(7.0F, -11.0F, -7.0F, 2.0F, 2.0F, 16.0F, 0.0F, false);
        this.body.setTextureOffset(68, 57).addCuboid(-9.0F, -11.0F, -7.0F, 2.0F, 2.0F, 16.0F, 0.0F, true);
        this.body.setTextureOffset(0, 75).addCuboid(-8.0F, -2.0F, -10.0F, 16.0F, 2.0F, 21.0F, 0.0F, false);
        this.body.setTextureOffset(105, 61).addCuboid(0.0F, -14.0F, -7.0F, 0.0F, 3.0F, 4.0F, 0.0F, false);
        this.body.setTextureOffset(113, 61).addCuboid(0.0F, -14.0F, -1.0F, 0.0F, 3.0F, 4.0F, 0.0F, false);
        this.body.setTextureOffset(105, 68).addCuboid(0.0F, -14.0F, 5.0F, 0.0F, 3.0F, 4.0F, 0.0F, false);
        this.body.setTextureOffset(113, 68).addCuboid(0.0F, -11.0F, 11.0F, 0.0F, 4.0F, 3.0F, 0.0F, false);

        this.neck = new ModelRendererPlus(this);
        this.neck.setDefaultRotPoint(0.0F, 0.0F, -11.0F);
        this.body.addChild(this.neck);
        this.neck.setTextureOffset(75, 76).addCuboid(-3.0F, -2.0F, -2.0F, 6.0F, 2.0F, 3.0F, 0.0F, false);

        this.head = new ModelRendererPlus(this);
        this.head.setDefaultRotPoint(0.0F, -2.0F, -2.0F);
        this.neck.addChild(this.head);
        this.head.setTextureOffset(75, 81).addCuboid(-3.0F, -3.0F, -3.0F, 6.0F, 4.0F, 6.0F, 0.0F, false);
        this.head.setTextureOffset(75, 91).addCuboid(-3.0F, -4.0F, -2.0F, 6.0F, 1.0F, 3.0F, 0.0F, false);
        this.head.setTextureOffset(93, 76).addCuboid(-2.0F, -2.5F, -3.25F, 4.0F, 4.0F, 1.0F, 0.0F, false);

        this.jaw = new ModelRendererPlus(this);
        this.jaw.setDefaultRotPoint(0.0F, 0.5F, 2.8F);
        this.head.addChild(this.jaw);
        this.setRotationAngle(this.jaw, 0.0436F, 0.0F, 0.0F);
        this.jaw.setTextureOffset(93, 91).addCuboid(-2.0F, -0.5F, -5.1F, 4.0F, 1.0F, 4.0F, 0.0F, false);

        this.leftLegFrontBase = new ModelRendererPlus(this);
        this.leftLegFrontBase.setDefaultRotPoint(8.0F, -2.0F, -7.0F);
        this.body.addChild(this.leftLegFrontBase);
        this.setRotationAngle(this.leftLegFrontBase, 0.0F, -0.6545F, 0.0436F);
        this.leftLegFrontBase.setTextureOffset(0, 99).addCuboid(-5.0F, 0.0F, -7.0F, 5.0F, 2.0F, 7.0F, 0.0F, false);

        this.leftLegFront = new ModelRendererPlus(this);
        this.leftLegFront.setDefaultRotPoint(0.0F, 0.0F, -7.0F);
        this.leftLegFrontBase.addChild(this.leftLegFront);
        this.setRotationAngle(this.leftLegFront, 0.0F, -0.3054F, 0.0F);
        this.leftLegFront.setTextureOffset(24, 102).addCuboid(0.0F, 0.0F, 0.0F, 8.0F, 2.0F, 4.0F, 0.0F, false);

        this.leftLegBackBase = new ModelRendererPlus(this);
        this.leftLegBackBase.setDefaultRotPoint(8.0F, -2.0F, 7.0F);
        this.body.addChild(this.leftLegBackBase);
        this.setRotationAngle(this.leftLegBackBase, 0.0F, -0.829F, 0.0436F);
        this.leftLegBackBase.setTextureOffset(48, 102).addCuboid(0.0F, 0.0F, 0.0F, 4.0F, 2.0F, 4.0F, 0.0F, false);

        this.leftLegBack = new ModelRendererPlus(this);
        this.leftLegBack.setDefaultRotPoint(4.0F, 0.0F, 0.0F);
        this.leftLegBackBase.addChild(this.leftLegBack);
        this.setRotationAngle(this.leftLegBack, 0.0F, -0.5672F, 0.0F);
        this.leftLegBack.setTextureOffset(64, 102).addCuboid(0.0F, 0.0F, 0.0F, 8.0F, 2.0F, 4.0F, 0.0F, false);

        this.rightLegFrontBase = new ModelRendererPlus(this);
        this.rightLegFrontBase.setDefaultRotPoint(-8.0F, -2.0F, -7.0F);
        this.body.addChild(this.rightLegFrontBase);
        this.setRotationAngle(this.rightLegFrontBase, 0.0F, 0.6545F, -0.0436F);
        this.rightLegFrontBase.setTextureOffset(0, 99).addCuboid(0.0F, 0.0F, -7.0F, 5.0F, 2.0F, 7.0F, 0.0F, true);

        this.rightLegFront = new ModelRendererPlus(this);
        this.rightLegFront.setDefaultRotPoint(0.0F, 0.0F, -7.0F);
        this.rightLegFrontBase.addChild(this.rightLegFront);
        this.setRotationAngle(this.rightLegFront, 0.0F, 0.3054F, 0.0F);
        this.rightLegFront.setTextureOffset(24, 102).addCuboid(-8.0F, 0.0F, 0.0F, 8.0F, 2.0F, 4.0F, 0.0F, true);

        this.rightLegBackBase = new ModelRendererPlus(this);
        this.rightLegBackBase.setDefaultRotPoint(-8.0F, -2.0F, 7.0F);
        this.body.addChild(this.rightLegBackBase);
        this.setRotationAngle(this.rightLegBackBase, 0.0F, 0.829F, -0.0436F);
        this.rightLegBackBase.setTextureOffset(48, 102).addCuboid(-4.0F, 0.0F, 0.0F, 4.0F, 2.0F, 4.0F, 0.0F, true);

        this.rightLegBack = new ModelRendererPlus(this);
        this.rightLegBack.setDefaultRotPoint(-4.0F, 0.0F, 0.0F);
        this.rightLegBackBase.addChild(this.rightLegBack);
        this.setRotationAngle(this.rightLegBack, 0.0F, 0.5672F, 0.0F);
        this.rightLegBack.setTextureOffset(64, 102).addCuboid(-8.0F, 0.0F, 0.0F, 8.0F, 2.0F, 4.0F, 0.0F, true);

        this.tail = new ModelRendererPlus(this);
        this.tail.setDefaultRotPoint(0.0F, -1.0F, 11.0F);
        this.body.addChild(this.tail);
        this.tail.setTextureOffset(114, 76).addCuboid(-1.5F, -2.0F, 0.0F, 3.0F, 3.0F, 3.0F, 0.0F, false);

        this.tail2 = new ModelRendererPlus(this);
        this.tail2.setDefaultRotPoint(0.0F, -1.0F, 3.0F);
        this.tail.addChild(this.tail2);
        this.tail2.setTextureOffset(114, 82).addCuboid(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);

        this.tail3 = new ModelRendererPlus(this);
        this.tail3.setDefaultRotPoint(0.0F, 0.0F, 2.0F);
        this.tail2.addChild(this.tail3);
        this.tail3.setTextureOffset(114, 86).addCuboid(-0.5F, 1.0F, 0.0F, 1.0F, 1.0F, 3.0F, 0.0F, false);

        this.animations = new BlockBenchAnimations(this, new ResourceLocation(RuneCraftory.MODID, "models/entity/animation/tortas.json"));
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.body.render(matrixStack, buffer, packedLight, packedOverlay);
    }


    @Override
    public void setAngles(T tortas, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetModel();
        this.head.rotateAngleY = netHeadYaw * 0.017453292F;
        this.head.rotateAngleX = headPitch * 0.017453292F;
        AnimatedAction anim = tortas.getAnimation();
        float partialTicks = Minecraft.getInstance().getRenderPartialTicks();
        if (tortas.isMoving()) {
            if (tortas.isSwimming())
                this.animations.doAnimation("swim", tortas.ticksExisted, partialTicks);
            else
                this.animations.doAnimation("walk", tortas.ticksExisted, partialTicks);
        }
        if (anim != null)
            this.animations.doAnimation(anim.getID().equals(EntityAmbrosia.pollen2.getID()) ? EntityAmbrosia.pollen.getID() : anim.getID(), anim.getTick(), partialTicks);
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