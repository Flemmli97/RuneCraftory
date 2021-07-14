package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.7.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.flemmli97.tenshilib.client.model.BlockBenchAnimations;
import com.flemmli97.tenshilib.client.model.IItemArmModel;
import com.flemmli97.tenshilib.client.model.IResetModel;
import com.flemmli97.tenshilib.client.model.ModelRendererPlus;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityGoblin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class ModelGoblin<T extends EntityGoblin> extends EntityModel<T> implements IResetModel, IItemArmModel {

    public ModelRendererPlus body;
    public ModelRendererPlus leftArm;
    public ModelRendererPlus leftArmDown;
    public ModelRendererPlus rightArm;
    public ModelRendererPlus rightArmDown;
    public ModelRendererPlus rightLeg;
    public ModelRendererPlus rightLegDown;
    public ModelRendererPlus leftLeg;
    public ModelRendererPlus leftLegDown;
    public ModelRendererPlus head;
    public ModelRendererPlus horn;
    public ModelRendererPlus leftEar;
    public ModelRendererPlus rightEar;

    public BlockBenchAnimations animations;

    public ModelGoblin() {
        this.textureWidth = 16;
        this.textureHeight = 16;

        this.body = new ModelRendererPlus(this);
        this.body.setDefaultRotPoint(0.0F, 23.5F, 0.0F);
        this.body.setTextureOffset(0, 0).addBox(-3.5F, -17.0F, -1.5F, 7.0F, 12.0F, 5.0F, 0.0F, false);

        this.leftArm = new ModelRendererPlus(this);
        this.leftArm.setDefaultRotPoint(3.5F, -13.0F, 1.0F);
        this.body.addChild(this.leftArm);
        this.leftArm.setTextureOffset(0, 0).addBox(0.0F, -1.5F, -1.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);

        this.leftArmDown = new ModelRendererPlus(this);
        this.leftArmDown.setDefaultRotPoint(1.0F, 2.5F, 1.0F);
        this.leftArm.addChild(this.leftArmDown);
        this.leftArmDown.setTextureOffset(0, 0).addBox(-1.0F, 0.0F, -2.0F, 2.0F, 5.0F, 2.0F, 0.0F, false);

        this.rightArm = new ModelRendererPlus(this);
        this.rightArm.setDefaultRotPoint(-3.5F, -13.0F, 1.0F);
        this.body.addChild(this.rightArm);
        this.rightArm.setTextureOffset(0, 0).addBox(-2.0F, -1.5F, -1.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);

        this.rightArmDown = new ModelRendererPlus(this);
        this.rightArmDown.setDefaultRotPoint(-1.0F, 2.5F, 1.0F);
        this.rightArm.addChild(this.rightArmDown);
        this.rightArmDown.setTextureOffset(0, 0).addBox(-1.0F, 0.0F, -2.0F, 2.0F, 5.0F, 2.0F, 0.0F, false);

        this.rightLeg = new ModelRendererPlus(this);
        this.rightLeg.setDefaultRotPoint(-1.5F, -5.0F, 1.0F);
        this.body.addChild(this.rightLeg);
        this.rightLeg.setTextureOffset(0, 0).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 5.0F, 2.0F, 0.0F, false);

        this.rightLegDown = new ModelRendererPlus(this);
        this.rightLegDown.setDefaultRotPoint(0.0F, 3.5F, -1.0F);
        this.rightLeg.addChild(this.rightLegDown);
        this.rightLegDown.setTextureOffset(0, 0).addBox(-1.0F, 1.0F, -2.0F, 2.0F, 1.0F, 4.0F, 0.0F, false);

        this.leftLeg = new ModelRendererPlus(this);
        this.leftLeg.setDefaultRotPoint(1.5F, -5.0F, 1.0F);
        this.body.addChild(this.leftLeg);
        this.leftLeg.setTextureOffset(0, 0).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 5.0F, 2.0F, 0.0F, false);

        this.leftLegDown = new ModelRendererPlus(this);
        this.leftLegDown.setDefaultRotPoint(0.0F, 3.5F, -1.0F);
        this.leftLeg.addChild(this.leftLegDown);
        this.leftLegDown.setTextureOffset(0, 0).addBox(-1.0F, 1.0F, -2.0F, 2.0F, 1.0F, 4.0F, 0.0F, false);

        this.head = new ModelRendererPlus(this);
        this.head.setDefaultRotPoint(0.0F, -17.0F, 0.0F);
        this.body.addChild(this.head);
        this.head.setTextureOffset(0, 0).addBox(-3.0F, -6.0F, -2.5F, 6.0F, 6.0F, 7.0F, 0.0F, false);
        this.head.setTextureOffset(0, 0).addBox(-0.5F, -3.0F, -3.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        this.horn = new ModelRendererPlus(this);
        this.horn.setDefaultRotPoint(0.0F, -6.0F, -3.1F);
        this.head.addChild(this.horn);
        this.setRotationAngle(this.horn, 0.6545F, 0.0F, 0.0F);
        this.horn.setTextureOffset(0, 0).addBox(-0.5F, -2.0F, 0.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        this.leftEar = new ModelRendererPlus(this);
        this.leftEar.setDefaultRotPoint(3.0F, -2.0F, 0.0F);
        this.head.addChild(this.leftEar);
        this.setRotationAngle(this.leftEar, 0.2182F, 0.829F, 0.0F);
        this.leftEar.setTextureOffset(0, 0).addBox(-1.0F, -3.0F, -0.5F, 1.0F, 3.0F, 3.0F, 0.0F, false);
        this.leftEar.setTextureOffset(0, 0).addBox(-1.0F, -3.0F, 2.5F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        this.leftEar.setTextureOffset(0, 0).addBox(-1.0F, -3.0F, 4.5F, 1.0F, 1.0F, 3.0F, 0.0F, false);

        this.rightEar = new ModelRendererPlus(this);
        this.rightEar.setDefaultRotPoint(-3.0F, -2.0F, -0.5F);
        this.head.addChild(this.rightEar);
        this.setRotationAngle(this.rightEar, 0.2182F, -0.829F, 0.0F);
        this.rightEar.setTextureOffset(0, 0).addBox(0.0F, -3.0F, 0.0F, 1.0F, 3.0F, 2.0F, 0.0F, false);
        this.rightEar.setTextureOffset(0, 0).addBox(0.0F, -3.0F, 2.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        this.rightEar.setTextureOffset(0, 0).addBox(0.0F, -3.0F, 4.0F, 1.0F, 1.0F, 3.0F, 0.0F, false);

        this.animations = new BlockBenchAnimations(this, new ResourceLocation(RuneCraftory.MODID, "models/entity/animation/goblin.json"));
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.body.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public void setRotationAngles(T goblin, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetModel();
        this.head.rotateAngleY += netHeadYaw * 0.017453292F;
        this.head.rotateAngleX += headPitch * 0.017453292F;

        this.rightEar.rotateAngleZ += MathHelper.cos(ageInTicks * 0.1F) * 0.04F + 0.05F;
        this.leftEar.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.1F) * 0.04F + 0.05F;
        this.rightEar.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.04F;
        this.leftEar.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.04F;

        this.rightArm.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.leftArm.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.rightArm.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
        this.leftArm.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
        float partialTicks = Minecraft.getInstance().getRenderPartialTicks();

        if (goblin.isMoving())
            this.animations.doAnimation("walk", goblin.ticksExisted, partialTicks);

        goblin.getAnimationHandler().getAnimation().ifPresent(anim -> this.animations.doAnimation(anim.getAnimationClient(), anim.getTick(), partialTicks));
    }

    public void setRotationAngle(ModelRendererPlus modelRenderer, float x, float y, float z) {
        modelRenderer.setDefaultRotAngle(x, y, z);
    }

    @Override
    public void resetModel() {
        this.body.reset();
        this.resetChild(this.body);
    }

    @Override
    public void transform(HandSide side, MatrixStack stack) {
        this.body.translateRotate(stack);
        if (side == HandSide.LEFT) {
            this.leftArm.translateRotate(stack);
            this.leftArmDown.translateRotate(stack);
        } else {
            this.rightArm.translateRotate(stack);
            this.rightArmDown.translateRotate(stack);
        }
    }

    @Override
    public void postTransform(boolean leftSide, MatrixStack stack) {
        stack.translate((leftSide ? 1 : -1) / 16d, 2 / 16d, -4 / 16d);
    }
}