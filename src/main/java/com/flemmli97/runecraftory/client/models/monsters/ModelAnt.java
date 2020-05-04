package com.flemmli97.runecraftory.client.models.monsters;// Made with Blockbench 3.5.2
// Exported for Minecraft version 1.12
// Paste this class into your mod and generate all required imports


import com.flemmli97.runecraftory.common.entity.monster.EntityAnt;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.tenshilib.client.model.BlockBenchAnimations;
import com.flemmli97.tenshilib.client.model.IResetModel;
import com.flemmli97.tenshilib.client.model.ModelRendererPlus;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class ModelAnt extends ModelBase implements IResetModel {
    public ModelRendererPlus body;
    public ModelRendererPlus bodyBack;
    public ModelRendererPlus bodyBackTop2;
    public ModelRendererPlus head;
    public ModelRendererPlus headFront;
    public ModelRendererPlus feelerLeft;
    public ModelRendererPlus feelerRight;
    public ModelRendererPlus jawLeft;
    public ModelRendererPlus jawRight;
    public ModelRendererPlus leg1Up1;
    public ModelRendererPlus leg1Down1;
    public ModelRendererPlus leg1Up2;
    public ModelRendererPlus leg1Down2;
    public ModelRendererPlus leg1Up3;
    public ModelRendererPlus leg1Down3;
    public ModelRendererPlus leg1Up4;
    public ModelRendererPlus leg1Down4;
    public ModelRendererPlus leg1Up5;
    public ModelRendererPlus leg1Down5;
    public ModelRendererPlus leg1Up6;
    public ModelRendererPlus leg1Down6;
    public BlockBenchAnimations animations;

    public ModelAnt() {
        textureWidth = 64;
        textureHeight = 64;

        body = new ModelRendererPlus(this);
        body.setDefaultRotPoint(0.0F, 20.0F, -4.0F);
        body.cubeList.add(new ModelBox(body, 0, 23, -4.0F, -2.0F, -4.0F, 8, 4, 8, 0.0F, true));

        bodyBack = new ModelRendererPlus(this);
        bodyBack.setDefaultRotPoint(0.0F, 0.0F, 4.0F);
        body.addChild(bodyBack);
        bodyBack.cubeList.add(new ModelBox(bodyBack, 0, 35, -5.0F, -2.5F, 0.0F, 10, 5, 13, 0.0F, true));

        bodyBackTop2 = new ModelRendererPlus(this);
        bodyBackTop2.setDefaultRotPoint(0.0F, -2.5F, 7.0F);
        bodyBack.addChild(bodyBackTop2);
        bodyBackTop2.cubeList.add(new ModelBox(bodyBackTop2, 28, 5, -3.5F, -1.0F, -6.0F, 7, 1, 11, 0.0F, true));

        head = new ModelRendererPlus(this);
        head.setDefaultRotPoint(0.0F, 0.0F, -4.0F);
        body.addChild(head);
        head.cubeList.add(new ModelBox(head, 0, 0, -5.0F, -3.0F, -3.0F, 10, 6, 3, 0.0F, true));

        headFront = new ModelRendererPlus(this);
        headFront.setDefaultRotPoint(0.0F, 0.0F, 0.0F);
        head.addChild(headFront);
        headFront.cubeList.add(new ModelBox(headFront, 0, 9, -4.5F, -2.5F, -8.0F, 9, 5, 5, 0.0F, true));

        feelerLeft = new ModelRendererPlus(this);
        feelerLeft.setDefaultRotPoint(3.5F, -2.5F, -7.5F);
        headFront.addChild(feelerLeft);
        setRotationAngle(feelerLeft, 0.6109F, 0.0F, 0.0F);
        feelerLeft.cubeList.add(new ModelBox(feelerLeft, 26, 0, -0.5F, -3.5F, -0.5F, 1, 4, 1, 0.0F, true));

        feelerRight = new ModelRendererPlus(this);
        feelerRight.setDefaultRotPoint(-3.5F, -2.5F, -7.5F);
        headFront.addChild(feelerRight);
        setRotationAngle(feelerRight, 0.6109F, 0.0F, 0.0F);
        feelerRight.cubeList.add(new ModelBox(feelerRight, 26, 0, -0.5F, -3.5F, -0.5F, 1, 4, 1, 0.0F, false));

        jawLeft = new ModelRendererPlus(this);
        jawLeft.setDefaultRotPoint(-3.0F, 1.5F, -7.5F);
        headFront.addChild(jawLeft);
        setRotationAngle(jawLeft, 0.0F, -0.3491F, 0.0F);
        jawLeft.cubeList.add(new ModelBox(jawLeft, 0, 19, 0.0F, -0.5F, -3.5F, 2, 1, 3, 0.0F, true));

        jawRight = new ModelRendererPlus(this);
        jawRight.setDefaultRotPoint(3.0F, 1.5F, -7.5F);
        headFront.addChild(jawRight);
        setRotationAngle(jawRight, 0.0F, 0.3491F, 0.0F);
        jawRight.cubeList.add(new ModelBox(jawRight, 0, 19, -2.0F, -0.5F, -3.5F, 2, 1, 3, 0.0F, false));

        leg1Up1 = new ModelRendererPlus(this);
        leg1Up1.setDefaultRotPoint(-4.0F, 1.0F, 2.5F);
        body.addChild(leg1Up1);
        setRotationAngle(leg1Up1, 0.0F, 0.6109F, 0.2618F);
        leg1Up1.cubeList.add(new ModelBox(leg1Up1, 0, 53, -7.0F, -1.0F, -1.0F, 7, 2, 2, 0.0F, true));

        leg1Down1 = new ModelRendererPlus(this);
        leg1Down1.setDefaultRotPoint(-6.5F, 0.0F, 0.0F);
        leg1Up1.addChild(leg1Down1);
        setRotationAngle(leg1Down1, 0.0F, 0.4363F, -0.6981F);
        leg1Down1.cubeList.add(new ModelBox(leg1Down1, 0, 57, -9.0F, -1.0F, -1.0F, 9, 2, 2, 0.0F, true));

        leg1Up2 = new ModelRendererPlus(this);
        leg1Up2.setDefaultRotPoint(-4.0F, 1.0F, 0.0F);
        body.addChild(leg1Up2);
        setRotationAngle(leg1Up2, 0.0F, 0.0F, 0.2618F);
        leg1Up2.cubeList.add(new ModelBox(leg1Up2, 0, 53, -7.0F, -1.0F, -1.0F, 7, 2, 2, 0.0F, true));

        leg1Down2 = new ModelRendererPlus(this);
        leg1Down2.setDefaultRotPoint(-6.5F, 0.0F, 0.0F);
        leg1Up2.addChild(leg1Down2);
        setRotationAngle(leg1Down2, 0.0F, 0.1745F, -0.6981F);
        leg1Down2.cubeList.add(new ModelBox(leg1Down2, 0, 57, -9.0F, -1.0F, -1.0F, 9, 2, 2, 0.0F, true));

        leg1Up3 = new ModelRendererPlus(this);
        leg1Up3.setDefaultRotPoint(-4.0F, 1.0F, -2.5F);
        body.addChild(leg1Up3);
        setRotationAngle(leg1Up3, 0.0F, -0.5236F, 0.3491F);
        leg1Up3.cubeList.add(new ModelBox(leg1Up3, 0, 53, -7.0F, -1.0F, -1.0F, 7, 2, 2, 0.0F, true));

        leg1Down3 = new ModelRendererPlus(this);
        leg1Down3.setDefaultRotPoint(-6.5F, 0.0F, 0.0F);
        leg1Up3.addChild(leg1Down3);
        setRotationAngle(leg1Down3, 0.0F, -0.0873F, -0.7854F);
        leg1Down3.cubeList.add(new ModelBox(leg1Down3, 0, 57, -9.0F, -1.0F, -1.0F, 9, 2, 2, 0.0F, true));

        leg1Up4 = new ModelRendererPlus(this);
        leg1Up4.setDefaultRotPoint(4.0F, 1.0F, -2.5F);
        body.addChild(leg1Up4);
        setRotationAngle(leg1Up4, 0.0F, 0.5236F, -0.3491F);
        leg1Up4.cubeList.add(new ModelBox(leg1Up4, 0, 53, 0.0F, -1.0F, -1.0F, 7, 2, 2, 0.0F, false));

        leg1Down4 = new ModelRendererPlus(this);
        leg1Down4.setDefaultRotPoint(6.5F, 0.0F, 0.0F);
        leg1Up4.addChild(leg1Down4);
        setRotationAngle(leg1Down4, 0.0F, -0.0873F, 0.7854F);
        leg1Down4.cubeList.add(new ModelBox(leg1Down4, 0, 57, 0.0F, -1.0F, -1.0F, 9, 2, 2, 0.0F, false));

        leg1Up5 = new ModelRendererPlus(this);
        leg1Up5.setDefaultRotPoint(4.0F, 1.0F, 0.0F);
        body.addChild(leg1Up5);
        setRotationAngle(leg1Up5, 0.0F, 0.0F, -0.2618F);
        leg1Up5.cubeList.add(new ModelBox(leg1Up5, 0, 53, 0.0F, -1.0F, -1.0F, 7, 2, 2, 0.0F, false));

        leg1Down5 = new ModelRendererPlus(this);
        leg1Down5.setDefaultRotPoint(6.5F, 0.0F, 0.0F);
        leg1Up5.addChild(leg1Down5);
        setRotationAngle(leg1Down5, 0.0F, -0.1745F, 0.6981F);
        leg1Down5.cubeList.add(new ModelBox(leg1Down5, 0, 57, 0.0F, -1.0F, -1.0F, 9, 2, 2, 0.0F, false));

        leg1Up6 = new ModelRendererPlus(this);
        leg1Up6.setDefaultRotPoint(4.0F, 1.0F, 2.5F);
        body.addChild(leg1Up6);
        setRotationAngle(leg1Up6, 0.0F, -0.6109F, -0.2618F);
        leg1Up6.cubeList.add(new ModelBox(leg1Up6, 0, 53, 0.0F, -1.0F, -1.0F, 7, 2, 2, 0.0F, false));

        leg1Down6 = new ModelRendererPlus(this);
        leg1Down6.setDefaultRotPoint(6.5F, 0.0F, 0.0F);
        leg1Up6.addChild(leg1Down6);
        setRotationAngle(leg1Down6, 0.0F, -0.4363F, 0.6981F);
        leg1Down6.cubeList.add(new ModelBox(leg1Down6, 0, 57, 0.0F, -1.0F, -1.0F, 9, 2, 2, 0.0F, false));

        this.animations = new BlockBenchAnimations(this, new ResourceLocation(LibReference.MODID, "models/entity/animation/ant.json"));
        System.out.println(this.animations);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        body.render(f5);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
                                  float headPitch, float scaleFactor, Entity entity) {
        if (!(entity instanceof EntityAnt))
            return;
        this.resetModel();
        this.head.rotateAngleY = netHeadYaw * 0.008726646F;
        this.head.rotateAngleX = headPitch * 0.008726646F;
        this.feelerLeft.rotateAngleZ += MathHelper.cos(ageInTicks * 0.15F) * 0.05F + 0.05F;
        this.feelerRight.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.15F) * 0.05F + 0.05F;
        this.feelerLeft.rotateAngleX += MathHelper.sin(ageInTicks * 0.1F) * 0.05F;
        this.feelerRight.rotateAngleX -= MathHelper.sin(ageInTicks * 0.1F) * 0.05F;
        EntityAnt ant = (EntityAnt) entity;
        float partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();

        if (ant.isMoving())
            this.animations.doAnimation("walk", ant.ticksExisted, partialTicks);

        AnimatedAction anim = ant.getAnimation();
        if (anim != null)
            this.animations.doAnimation(anim.getID(), anim.getTick(), partialTicks);
    }

    public void setRotationAngle(ModelRendererPlus modelRenderer, float x, float y, float z) {
        modelRenderer.setDefaultRotAngle(x, y, z);
    }

    @Override
    public void resetModel() {
        body.reset();
        this.resetChild(this.body);
    }
}