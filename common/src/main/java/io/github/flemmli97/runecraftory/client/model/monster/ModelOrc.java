package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityOrc;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.client.AnimationManager;
import io.github.flemmli97.tenshilib.client.model.BlockBenchAnimations;
import io.github.flemmli97.tenshilib.client.model.ExtendedModel;
import io.github.flemmli97.tenshilib.client.model.IItemArmModel;
import io.github.flemmli97.tenshilib.client.model.ModelPartHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;

public class ModelOrc<T extends EntityOrc> extends EntityModel<T> implements ExtendedModel, IItemArmModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(RuneCraftory.MODID, "orc"), "main");

    protected final ModelPartHandler model;
    protected final BlockBenchAnimations anim;

    public ModelPartHandler.ModelPartExtended head;
    public ModelPartHandler.ModelPartExtended maze;
    public ModelPartHandler.ModelPartExtended handLeftUp;
    public ModelPartHandler.ModelPartExtended handLeftDown;
    public ModelPartHandler.ModelPartExtended handRightUp;
    public ModelPartHandler.ModelPartExtended handRightDown;

    public ModelOrc(ModelPart root) {
        super();
        this.model = new ModelPartHandler(root, "body");
        this.anim = AnimationManager.getInstance().getAnimation(new ResourceLocation(RuneCraftory.MODID, "orc"));
        this.head = this.model.getPart("head");
        this.maze = this.model.getPart("mazeStick");
        this.handLeftUp = this.model.getPart("handLeftUp");
        this.handLeftDown = this.model.getPart("handLeftDown");
        this.handRightUp = this.model.getPart("handRightUp");
        this.handRightDown = this.model.getPart("handRightDown");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 30).addBox(-5.0F, -9.0F, -4.0F, 9.0F, 18.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(36, 30).addBox(-5.0F, -9.0F, -4.0F, 9.0F, 18.0F, 9.0F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-5.5F, -10.5F, -5.5F, 11.0F, 11.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, -9.5F, 0.5F));

        PartDefinition headFront = head.addOrReplaceChild("headFront", CubeListBuilder.create().texOffs(0, 22).addBox(-3.75F, -3.25F, -4.0F, 7.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.25F, -1.25F, -3.5F));

        PartDefinition snout = headFront.addOrReplaceChild("snout", CubeListBuilder.create().texOffs(18, 22).addBox(-1.5F, -2.5F, -2.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.25F, -0.75F, -2.5F));

        PartDefinition earLeft = headFront.addOrReplaceChild("earLeft", CubeListBuilder.create().texOffs(26, 22).mirror().addBox(-1.5F, -3.0F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.25F, -9.25F, 0.0F, 0.0873F, 0.0F, 0.0F));

        PartDefinition earRight = headFront.addOrReplaceChild("earRight", CubeListBuilder.create().texOffs(26, 22).addBox(-1.5F, -3.0F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.75F, -9.25F, 0.0F, 0.0873F, 0.0F, 0.0F));

        PartDefinition handLeftUp = body.addOrReplaceChild("handLeftUp", CubeListBuilder.create().texOffs(0, 57).addBox(0.0F, -2.0F, -2.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -3.0F, 0.0F, -1.0472F, -0.5236F, -0.0873F));

        PartDefinition handLeftDown = handLeftUp.addOrReplaceChild("handLeftDown", CubeListBuilder.create().texOffs(12, 57).addBox(-3.0F, 0.0F, -1.5F, 3.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 3.0F, -0.5F, -0.4363F, 0.2618F, 0.2618F));

        PartDefinition handRightUp = body.addOrReplaceChild("handRightUp", CubeListBuilder.create().texOffs(0, 57).mirror().addBox(-3.0F, -2.0F, -2.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-5.0F, -3.0F, 0.0F, -1.0472F, 0.5236F, 0.0873F));

        PartDefinition handRightDown = handRightUp.addOrReplaceChild("handRightDown", CubeListBuilder.create().texOffs(12, 57).mirror().addBox(0.0F, 0.0F, -1.5F, 3.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, 3.0F, -0.5F, -0.4363F, -0.2618F, -0.5236F));

        PartDefinition mazeStick = handRightDown.addOrReplaceChild("mazeStick", CubeListBuilder.create().texOffs(0, 69).addBox(1.0F, 7.5F, -13.5F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition mazeHead = mazeStick.addOrReplaceChild("mazeHead", CubeListBuilder.create().texOffs(26, 76).addBox(0.0F, 6.5F, -16.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition legLeftUp = body.addOrReplaceChild("legLeftUp", CubeListBuilder.create().texOffs(44, 0).addBox(-1.5F, 0.0F, -3.0F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.4F, 9.0F, 2.0F, -0.5236F, 0.0F, 0.0F));

        PartDefinition legLeftDown = legLeftUp.addOrReplaceChild("legLeftDown", CubeListBuilder.create().texOffs(44, 11).addBox(-1.6F, 0.0F, 0.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1F, 6.0F, -3.0F, 0.3491F, 0.0F, 0.0F));

        PartDefinition legRightUp = body.addOrReplaceChild("legRightUp", CubeListBuilder.create().texOffs(44, 0).addBox(-1.3F, 0.0F, -3.0F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.6F, 9.0F, 2.0F, -0.5236F, 0.0F, 0.0F));

        PartDefinition legRightDown = legRightUp.addOrReplaceChild("legRightDown", CubeListBuilder.create().texOffs(44, 11).addBox(-1.4F, 0.0F, 0.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1F, 6.0F, -3.0F, 0.3491F, 0.0F, 0.0F));

        PartDefinition floof = body.addOrReplaceChild("floof", CubeListBuilder.create().texOffs(0, 83).addBox(-7.0F, -8.8F, -6.0F, 13.0F, 0.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 72, 96);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.model.getMainPart().render(poseStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.model.resetPoses();
        this.head.yRot += netHeadYaw * Mth.DEG_TO_RAD * 0.5f;
        this.head.xRot += headPitch * Mth.DEG_TO_RAD * 0.5f;
        AnimatedAction anim = entity.getAnimationHandler().getAnimation();
        float partialTicks = Minecraft.getInstance().getFrameTime();
        if (entity.deathTime <= 0 && !entity.playDeath()) {
            this.anim.doAnimation(this, "iddle", entity.tickCount, partialTicks);
            if (entity.moveTick() > 0)
                this.anim.doAnimation(this, "walk", entity.tickCount, partialTicks, entity.interpolatedMoveTick());
        }
        if (anim != null)
            this.anim.doAnimation(this, anim.getAnimationClient(), anim.getTick(), partialTicks);
    }

    @Override
    public ModelPartHandler getHandler() {
        return this.model;
    }

    @Override
    public void transform(HumanoidArm humanoidArm, PoseStack poseStack) {
        poseStack.translate(0.0, 6 / 16d, 0.0);
        this.model.getMainPart().translateAndRotate(poseStack);
        if (humanoidArm == HumanoidArm.LEFT) {
            this.handLeftUp.translateAndRotate(poseStack);
            this.handLeftDown.translateAndRotate(poseStack);
            poseStack.translate(-2 / 16d, 9 / 16d, 0 / 16d);
        } else {
            this.handRightUp.translateAndRotate(poseStack);
            this.handRightDown.translateAndRotate(poseStack);
            poseStack.translate(2 / 16d, 9 / 16d, 0 / 16d);
        }
    }

    @Override
    public void postTransform(boolean leftSide, PoseStack stack) {
    }
}