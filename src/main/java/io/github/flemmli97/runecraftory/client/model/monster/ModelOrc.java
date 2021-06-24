package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import com.flemmli97.tenshilib.client.model.BlockBenchAnimations;
import com.flemmli97.tenshilib.client.model.IItemArmModel;
import com.flemmli97.tenshilib.client.model.IResetModel;
import com.flemmli97.tenshilib.client.model.ModelRendererPlus;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityOrc;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class ModelOrc<T extends EntityOrc> extends EntityModel<T> implements IResetModel, IItemArmModel {
    public ModelRendererPlus body;
    public ModelRendererPlus head;
    public ModelRendererPlus headFront;
    public ModelRendererPlus snout;
    public ModelRendererPlus earLeft;
    public ModelRendererPlus earRight;
    public ModelRendererPlus handLeftUp;
    public ModelRendererPlus handLeftDown;
    public ModelRendererPlus handRightUp;
    public ModelRendererPlus handRightDown;
    public ModelRendererPlus mazeStick;
    public ModelRendererPlus mazeHead;
    public ModelRendererPlus legLeftUp;
    public ModelRendererPlus legLeftDown;
    public ModelRendererPlus legRightUp;
    public ModelRendererPlus legRightDown;
    public ModelRendererPlus floof;

    public BlockBenchAnimations animations;

    public ModelOrc() {
        this.textureWidth = 72;
        this.textureHeight = 96;

        this.body = new ModelRendererPlus(this);
        this.body.setDefaultRotPoint(0.0F, 6.0F, 0.0F);
        this.setRotationAngle(this.body, 0.1745F, 0.0F, 0.0F);
        this.body.setTextureOffset(0, 30).addBox(-5.0F, -9.0F, -4.0F, 9.0F, 18.0F, 9.0F, 0.0F, false);
        this.body.setTextureOffset(36, 30).addBox(-5.0F, -9.0F, -4.0F, 9.0F, 18.0F, 9.0F, 0.2F, false);

        this.head = new ModelRendererPlus(this);
        this.head.setDefaultRotPoint(-0.5F, -9.5F, 0.5F);
        this.body.addChild(this.head);
        this.head.setTextureOffset(0, 0).addBox(-5.5F, -10.5F, -5.5F, 11.0F, 11.0F, 11.0F, 0.0F, false);

        this.headFront = new ModelRendererPlus(this);
        this.headFront.setDefaultRotPoint(0.25F, -1.25F, -3.5F);
        this.head.addChild(this.headFront);
        this.headFront.setTextureOffset(0, 22).addBox(-3.75F, -3.25F, -4.0F, 7.0F, 5.0F, 2.0F, 0.0F, false);

        this.snout = new ModelRendererPlus(this);
        this.snout.setDefaultRotPoint(-0.25F, -0.75F, -2.5F);
        this.headFront.addChild(this.snout);
        this.snout.setTextureOffset(18, 22).addBox(-1.5F, -2.5F, -2.5F, 3.0F, 3.0F, 1.0F, 0.0F, false);

        this.earLeft = new ModelRendererPlus(this);
        this.earLeft.setDefaultRotPoint(-3.25F, -9.25F, 0.0F);
        this.headFront.addChild(this.earLeft);
        this.setRotationAngle(this.earLeft, 0.0873F, 0.0F, 0.0F);
        this.earLeft.setTextureOffset(26, 22).addBox(-1.5F, -3.0F, -1.0F, 3.0F, 3.0F, 1.0F, 0.0F, true);

        this.earRight = new ModelRendererPlus(this);
        this.earRight.setDefaultRotPoint(2.75F, -9.25F, 0.0F);
        this.headFront.addChild(this.earRight);
        this.setRotationAngle(this.earRight, 0.0873F, 0.0F, 0.0F);
        this.earRight.setTextureOffset(26, 22).addBox(-1.5F, -3.0F, -1.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);

        this.handLeftUp = new ModelRendererPlus(this);
        this.handLeftUp.setDefaultRotPoint(4.0F, -3.0F, 0.0F);
        this.body.addChild(this.handLeftUp);
        this.setRotationAngle(this.handLeftUp, -1.0472F, -0.5236F, -0.0873F);
        this.handLeftUp.setTextureOffset(0, 57).addBox(0.0F, -2.0F, -2.0F, 3.0F, 5.0F, 3.0F, 0.0F, false);

        this.handLeftDown = new ModelRendererPlus(this);
        this.handLeftDown.setDefaultRotPoint(3.0F, 3.0F, -0.5F);
        this.handLeftUp.addChild(this.handLeftDown);
        this.setRotationAngle(this.handLeftDown, -0.4363F, 0.2618F, 0.2618F);
        this.handLeftDown.setTextureOffset(12, 57).addBox(-3.0F, 0.0F, -1.5F, 3.0F, 9.0F, 3.0F, 0.0F, false);

        this.handRightUp = new ModelRendererPlus(this);
        this.handRightUp.setDefaultRotPoint(-5.0F, -3.0F, 0.0F);
        this.body.addChild(this.handRightUp);
        this.setRotationAngle(this.handRightUp, -1.0472F, 0.5236F, 0.0873F);
        this.handRightUp.setTextureOffset(0, 57).addBox(-3.0F, -2.0F, -2.0F, 3.0F, 5.0F, 3.0F, 0.0F, true);

        this.handRightDown = new ModelRendererPlus(this);
        this.handRightDown.setDefaultRotPoint(-3.0F, 3.0F, -0.5F);
        this.handRightUp.addChild(this.handRightDown);
        this.setRotationAngle(this.handRightDown, -0.4363F, -0.2618F, -0.2618F);
        this.handRightDown.setTextureOffset(12, 57).addBox(0.0F, 0.0F, -1.5F, 3.0F, 9.0F, 3.0F, 0.0F, true);

        this.mazeStick = new ModelRendererPlus(this);
        this.mazeStick.setDefaultRotPoint(0.0F, 0.0F, 0.0F);
        this.handRightDown.addChild(this.mazeStick);
        this.mazeStick.setTextureOffset(0, 69).addBox(1.0F, 7.5F, -13.5F, 1.0F, 1.0F, 12.0F, 0.0F, false);

        this.mazeHead = new ModelRendererPlus(this);
        this.mazeHead.setDefaultRotPoint(0.0F, 0.0F, 0.0F);
        this.mazeStick.addChild(this.mazeHead);
        this.mazeHead.setTextureOffset(26, 76).addBox(0.0F, 6.5F, -16.0F, 3.0F, 3.0F, 3.0F, 0.0F, false);

        this.legLeftUp = new ModelRendererPlus(this);
        this.legLeftUp.setDefaultRotPoint(2.4F, 9.0F, 2.0F);
        this.body.addChild(this.legLeftUp);
        this.setRotationAngle(this.legLeftUp, -0.5236F, 0.0F, 0.0F);
        this.legLeftUp.setTextureOffset(44, 0).addBox(-1.5F, 0.0F, -3.0F, 3.0F, 6.0F, 3.0F, 0.0F, false);

        this.legLeftDown = new ModelRendererPlus(this);
        this.legLeftDown.setDefaultRotPoint(0.1F, 6.0F, -3.0F);
        this.legLeftUp.addChild(this.legLeftDown);
        this.setRotationAngle(this.legLeftDown, 0.3491F, 0.0F, 0.0F);
        this.legLeftDown.setTextureOffset(44, 11).addBox(-1.6F, 0.0F, 0.0F, 3.0F, 5.0F, 3.0F, 0.0F, false);

        this.legRightUp = new ModelRendererPlus(this);
        this.legRightUp.setDefaultRotPoint(-3.6F, 9.0F, 2.0F);
        this.body.addChild(this.legRightUp);
        this.setRotationAngle(this.legRightUp, -0.5236F, 0.0F, 0.0F);
        this.legRightUp.setTextureOffset(44, 0).addBox(-1.3F, 0.0F, -3.0F, 3.0F, 6.0F, 3.0F, 0.0F, false);

        this.legRightDown = new ModelRendererPlus(this);
        this.legRightDown.setDefaultRotPoint(0.1F, 6.0F, -3.0F);
        this.legRightUp.addChild(this.legRightDown);
        this.setRotationAngle(this.legRightDown, 0.3491F, 0.0F, 0.0F);
        this.legRightDown.setTextureOffset(44, 11).addBox(-1.4F, 0.0F, 0.0F, 3.0F, 5.0F, 3.0F, 0.0F, false);

        this.floof = new ModelRendererPlus(this);
        this.floof.setDefaultRotPoint(0.0F, 0.0F, 0.0F);
        this.body.addChild(this.floof);
        this.floof.setTextureOffset(0, 83).addBox(-7.0F, -8.8F, -6.0F, 13.0F, 0.0F, 13.0F, 0.0F, false);

        this.animations = new BlockBenchAnimations(this, new ResourceLocation(RuneCraftory.MODID, "models/entity/animation/orc.json"));
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.body.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public void setRotationAngles(T orc, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetModel();
        this.head.rotateAngleY += netHeadYaw * 0.017453292F;
        this.head.rotateAngleX += headPitch * 0.017453292F;
        this.handRightUp.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.handLeftUp.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.handRightUp.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
        this.handLeftUp.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
        float partialTicks = Minecraft.getInstance().getRenderPartialTicks();
        if (orc.isMoving())
            this.animations.doAnimation("walk", orc.ticksExisted, partialTicks);
        AnimatedAction anim = orc.getAnimation();
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

    @Override
    public void transform(HandSide side, MatrixStack stack) {
        this.body.translateRotate(stack);
        if (side == HandSide.LEFT) {
            this.handLeftUp.translateRotate(stack);
            this.handLeftDown.translateRotate(stack);
        } else {
            this.handRightUp.translateRotate(stack);
            this.handRightDown.translateRotate(stack);
        }
    }

    @Override
    public void postTransform(boolean leftSide, MatrixStack stack) {
        stack.translate((leftSide ? 2 : -2) / 16d, 3 / 16d, -9 / 16d);
    }
}