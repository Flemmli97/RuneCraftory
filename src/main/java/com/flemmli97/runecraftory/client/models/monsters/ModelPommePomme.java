package com.flemmli97.runecraftory.client.models.monsters;// Made with Blockbench 3.5.2
// Exported for Minecraft version 1.12
// Paste this class into your mod and generate all required imports


import com.flemmli97.runecraftory.common.entity.monster.EntityPommePomme;
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

public class ModelPommePomme extends ModelBase implements IResetModel {
	public ModelRendererPlus body;
	public ModelRendererPlus inner;
	public ModelRendererPlus stem;
	public ModelRendererPlus hairRight;
	public ModelRendererPlus hairLeft;
	public ModelRendererPlus handLeft;
	public ModelRendererPlus handRight;
	public ModelRendererPlus feetLeft;
	public ModelRendererPlus feetRight;

    public  BlockBenchAnimations animations;

    public ModelPommePomme() {
        this.textureWidth = 126;
        this.textureHeight = 52;

        this.body = new ModelRendererPlus(this);
        this.body.setDefaultRotPoint(0.0F, 13.0F, 0.0F);
        this.body.cubeList.add(new ModelBox(this.body, 0, 0, -7.0F, -6.0F, -7.0F, 14, 14, 14, 0.0F, false));
        this.body.cubeList.add(new ModelBox(this.body, 0, 28, 7.0F, -5.0F, -6.0F, 1, 12, 12, 0.0F, false));
        this.body.cubeList.add(new ModelBox(this.body, 26, 28, 8.0F, -3.0F, -4.0F, 1, 8, 8, 0.0F, false));
        this.body.cubeList.add(new ModelBox(this.body, 0, 28, -8.0F, -5.0F, -6.0F, 1, 12, 12, 0.0F, true));
        this.body.cubeList.add(new ModelBox(this.body, 26, 28, -9.0F, -3.0F, -4.0F, 1, 8, 8, 0.0F, true));
        this.body.cubeList.add(new ModelBox(this.body, 56, 0, -6.0F, -5.0F, -8.0F, 12, 12, 1, 0.0F, false));
        this.body.cubeList.add(new ModelBox(this.body, 56, 13, -4.0F, -3.0F, -9.0F, 8, 8, 1, 0.0F, false));
        this.body.cubeList.add(new ModelBox(this.body, 56, 0, -6.0F, -5.0F, 7.0F, 12, 12, 1, 0.0F, true));
        this.body.cubeList.add(new ModelBox(this.body, 56, 13, -4.0F, -3.0F, 8.0F, 8, 8, 1, 0.0F, true));
        this.body.cubeList.add(new ModelBox(this.body, 74, 23, -6.0F, -7.0F, -6.0F, 12, 1, 12, 0.0F, false));
        this.body.cubeList.add(new ModelBox(this.body, 44, 36, -6.0F, 8.0F, -6.0F, 12, 1, 12, 0.0F, false));
        this.body.cubeList.add(new ModelBox(this.body, 92, 36, -4.0F, 9.0F, -4.0F, 8, 1, 8, 0.0F, false));

        this.inner = new ModelRendererPlus(this);
        this.inner.setDefaultRotPoint(0.0F, -7.0F, 0.0F);
        this.body.addChild(this.inner);
        this.inner.cubeList.add(new ModelBox(this.inner, 102, 0, -3.0F, -6.0F, -3.0F, 6, 6, 6, 0.0F, false));
        this.inner.cubeList.add(new ModelBox(this.inner, 74, 14, -4.0F, -7.0F, -4.0F, 8, 1, 8, 0.0F, false));

        this.stem = new ModelRendererPlus(this);
        this.stem.setDefaultRotPoint(0.0F, -8.0F, 0.0F);
        this.inner.addChild(this.stem);
        this.stem.cubeList.add(new ModelBox(this.stem, 106, 12, -0.5F, -4.0F, -0.5F, 1, 5, 1, 0.0F, false));

        this.hairRight = new ModelRendererPlus(this);
        this.hairRight.setDefaultRotPoint(-4.0F, -6.0F, 0.0F);
        this.inner.addChild(this.hairRight);
        this.setRotationAngle(this.hairRight, 0.0F, 0.0F, -0.2618F);
        this.hairRight.cubeList.add(new ModelBox(this.hairRight, 56, 23, -6.0F, 0.0F, -4.0F, 6, 0, 8, 0.0F, true));

        this.hairLeft = new ModelRendererPlus(this);
        this.hairLeft.setDefaultRotPoint(4.0F, -6.0F, 0.0F);
        this.inner.addChild(this.hairLeft);
        this.setRotationAngle(this.hairLeft, 0.0F, 0.0F, 0.3491F);
        this.hairLeft.cubeList.add(new ModelBox(this.hairLeft, 56, 23, 0.0F, 0.0F, -4.0F, 6, 0, 8, 0.0F, false));

        this.handLeft = new ModelRendererPlus(this);
        this.handLeft.setDefaultRotPoint(3.0F, -9.5F, -4.0F);
        this.body.addChild(this.handLeft);
        this.setRotationAngle(this.handLeft, 0.1745F, -0.1745F, 0.0F);
        this.handLeft.cubeList.add(new ModelBox(this.handLeft, 82, 8, -1.0F, 1.4696F, -5.3473F, 2, 1, 5, 0.0F, false));

        this.handRight = new ModelRendererPlus(this);
        this.handRight.setDefaultRotPoint(-3.0F, -9.5F, -4.0F);
        this.body.addChild(this.handRight);
        this.setRotationAngle(this.handRight, 0.1745F, 0.1745F, 0.0F);
        this.handRight.cubeList.add(new ModelBox(this.handRight, 82, 8, -1.0F, 1.4696F, -5.3473F, 2, 1, 5, 0.0F, false));

        this.feetLeft = new ModelRendererPlus(this);
        this.feetLeft.setDefaultRotPoint(3.5F, 10.0F, 1.0F);
        this.body.addChild(this.feetLeft);
        this.feetLeft.cubeList.add(new ModelBox(this.feetLeft, 82, 0, -1.5F, 0.0F, -7.0F, 3, 1, 7, 0.0F, false));

        this.feetRight = new ModelRendererPlus(this);
        this.feetRight.setDefaultRotPoint(-3.5F, 10.0F, 1.0F);
        this.body.addChild(this.feetRight);
        this.feetRight.cubeList.add(new ModelBox(this.feetRight, 82, 0, -1.5F, 0.0F, -7.0F, 3, 1, 7, 0.0F, true));

        this.animations = new BlockBenchAnimations(this, new ResourceLocation(LibReference.MODID, "models/entity/animation/pomme_pomme.json"));
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.body.render(f5);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
                                  float headPitch, float scaleFactor, Entity entity) {
        if (!(entity instanceof EntityPommePomme))
            return;
        this.resetModel();
        EntityPommePomme pommePomme = (EntityPommePomme) entity;

        float partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();
        this.animations.doAnimation("iddle", pommePomme.ticksExisted, partialTicks);
        if (pommePomme.isMoving())
            this.animations.doAnimation("walk", pommePomme.ticksExisted, partialTicks);
        AnimatedAction anim = pommePomme.getAnimation();
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