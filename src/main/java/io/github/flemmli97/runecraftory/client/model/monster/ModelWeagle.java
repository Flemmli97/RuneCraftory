package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.7.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityWeagle;
import com.flemmli97.tenshilib.client.model.BlockBenchAnimations;
import com.flemmli97.tenshilib.client.model.IResetModel;
import com.flemmli97.tenshilib.client.model.ModelRendererPlus;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;

public class ModelWeagle<T extends EntityWeagle> extends EntityModel<T> implements IResetModel {
    public ModelRendererPlus body;
    public ModelRendererPlus head;
    public ModelRendererPlus leftWing;
    public ModelRendererPlus leftWing2;
    public ModelRendererPlus leftWing3;
    public ModelRendererPlus rightWing;
    public ModelRendererPlus rightWing2;
    public ModelRendererPlus rightWing3;
    public ModelRendererPlus leftLeg;
    public ModelRendererPlus rightLeg;

    public BlockBenchAnimations animations;

    public ModelWeagle() {
        this.textureWidth = 16;
        this.textureHeight = 16;

        this.body = new ModelRendererPlus(this);
        this.body.setDefaultRotPoint(0.0F, 9.0F, 2.0F);
        this.body.setTextureOffset(0, 0).addBox(-4.0F, -1.0F, -4.0F, 8.0F, 8.0F, 14.0F, 0.0F, false);
        this.body.setTextureOffset(0, 0).addBox(4.0F, 0.0F, -3.0F, 1.0F, 6.0F, 11.0F, 0.0F, false);
        this.body.setTextureOffset(0, 0).addBox(-5.0F, 0.0F, -3.0F, 1.0F, 6.0F, 11.0F, 0.0F, false);

        this.head = new ModelRendererPlus(this);
        this.head.setDefaultRotPoint(0.0F, 2.0F, -4.0F);
        this.body.addChild(this.head);
        this.head.setTextureOffset(0, 0).addBox(-3.0F, -2.0F, -6.0F, 6.0F, 6.0F, 7.0F, 0.0F, false);
        this.head.setTextureOffset(0, 0).addBox(0.0F, -5.0F, -6.0F, 0.0F, 3.0F, 6.0F, 0.0F, false);
        this.head.setTextureOffset(0, 0).addBox(0.0F, 4.0F, -5.0F, 0.0F, 2.0F, 5.0F, 0.0F, false);
        this.head.setTextureOffset(0, 0).addBox(-2.0F, -1.0F, -7.0F, 4.0F, 4.0F, 1.0F, 0.0F, false);
        this.head.setTextureOffset(0, 0).addBox(-1.0F, 0.0F, -8.5F, 2.0F, 3.0F, 2.0F, 0.0F, false);

        this.leftWing = new ModelRendererPlus(this);
        this.leftWing.setDefaultRotPoint(4.7F, 2.5F, -1.5F);
        this.body.addChild(this.leftWing);
        this.leftWing.setTextureOffset(0, 0).addBox(0.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, 0.0F, false);
        this.leftWing.setTextureOffset(0, 0).addBox(0.0F, 0.0F, 0.0F, 4.0F, 0.0F, 6.0F, 0.0F, false);

        this.leftWing2 = new ModelRendererPlus(this);
        this.leftWing2.setDefaultRotPoint(3.7F, 0.5F, -0.5F);
        this.leftWing.addChild(this.leftWing2);
        this.leftWing2.setTextureOffset(0, 0).addBox(0.0F, -1.0F, 0.0F, 6.0F, 1.0F, 1.0F, 0.0F, false);
        this.leftWing2.setTextureOffset(0, 0).addBox(0.0F, -0.5F, 0.5F, 6.0F, 0.0F, 8.0F, 0.0F, false);

        this.leftWing3 = new ModelRendererPlus(this);
        this.leftWing3.setDefaultRotPoint(5.7F, -1.0F, 0.0F);
        this.leftWing2.addChild(this.leftWing3);
        this.leftWing3.setTextureOffset(0, 0).addBox(0.0F, 0.0F, 0.0F, 10.0F, 1.0F, 1.0F, 0.0F, false);
        this.leftWing3.setTextureOffset(0, 0).addBox(0.0F, 0.5F, 0.5F, 10.0F, 0.0F, 8.0F, 0.0F, false);

        this.rightWing = new ModelRendererPlus(this);
        this.rightWing.setDefaultRotPoint(-4.7F, 2.5F, -1.5F);
        this.body.addChild(this.rightWing);
        this.rightWing.setTextureOffset(0, 0).addBox(-4.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, 0.0F, false);
        this.rightWing.setTextureOffset(0, 0).addBox(-4.0F, 0.0F, 0.0F, 4.0F, 0.0F, 6.0F, 0.0F, false);

        this.rightWing2 = new ModelRendererPlus(this);
        this.rightWing2.setDefaultRotPoint(-3.7F, 0.5F, -0.5F);
        this.rightWing.addChild(this.rightWing2);
        this.rightWing2.setTextureOffset(0, 0).addBox(-6.0F, -1.0F, 0.0F, 6.0F, 1.0F, 1.0F, 0.0F, false);
        this.rightWing2.setTextureOffset(0, 0).addBox(-6.0F, -0.5F, 0.5F, 6.0F, 0.0F, 8.0F, 0.0F, false);

        this.rightWing3 = new ModelRendererPlus(this);
        this.rightWing3.setDefaultRotPoint(-5.7F, -1.0F, 0.0F);
        this.rightWing2.addChild(this.rightWing3);
        this.rightWing3.setTextureOffset(0, 0).addBox(-10.0F, 0.0F, 0.0F, 10.0F, 1.0F, 1.0F, 0.0F, false);
        this.rightWing3.setTextureOffset(0, 0).addBox(-10.0F, 0.5F, 0.5F, 10.0F, 0.0F, 8.0F, 0.0F, false);

        this.leftLeg = new ModelRendererPlus(this);
        this.leftLeg.setDefaultRotPoint(3.5F, 6.0F, 8.5F);
        this.body.addChild(this.leftLeg);
        this.leftLeg.setTextureOffset(0, 0).addBox(-1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 4.0F, 0.0F, false);
        this.leftLeg.setTextureOffset(0, 0).addBox(-2.5F, -1.0F, 3.5F, 5.0F, 4.0F, 0.0F, 0.0F, false);

        this.rightLeg = new ModelRendererPlus(this);
        this.rightLeg.setDefaultRotPoint(-3.5F, 6.0F, 8.5F);
        this.body.addChild(this.rightLeg);
        this.rightLeg.setTextureOffset(0, 0).addBox(-1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 4.0F, 0.0F, false);
        this.rightLeg.setTextureOffset(0, 0).addBox(-2.5F, -1.0F, 3.5F, 5.0F, 4.0F, 0.0F, 0.0F, false);

        this.animations = new BlockBenchAnimations(this, new ResourceLocation(RuneCraftory.MODID, "models/entity/animation/weagle.json"));
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.body.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public void setRotationAngles(T weagle, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetModel();
        this.head.rotateAngleY += netHeadYaw * 0.003F;
        this.head.rotateAngleX += headPitch * 0.01F;
        AnimatedAction anim = weagle.getAnimation();
        float partialTicks = Minecraft.getInstance().getRenderPartialTicks();
        if (anim == null) {
            if (weagle.isMoving()) {
                this.animations.doAnimation("fly", weagle.ticksExisted, partialTicks);
            } else
                this.animations.doAnimation("flyIddle", weagle.ticksExisted, partialTicks);
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