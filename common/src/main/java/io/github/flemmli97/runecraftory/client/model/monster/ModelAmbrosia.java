package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.EntityAmbrosia;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.client.AnimationManager;
import io.github.flemmli97.tenshilib.client.model.BlockBenchAnimations;
import io.github.flemmli97.tenshilib.client.model.ExtendedModel;
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

public class ModelAmbrosia<T extends EntityAmbrosia> extends EntityModel<T> implements ExtendedModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(RuneCraftory.MODID, "ambrosia"), "main");

    protected final ModelPartHandler model;
    protected final BlockBenchAnimations anim;

    public ModelPartHandler.ModelPartExtended body;
    public ModelPartHandler.ModelPartExtended head;

    public ModelAmbrosia(ModelPart root) {
        super();
        this.model = new ModelPartHandler(root, "body");
        this.anim = AnimationManager.getInstance().getAnimation(new ResourceLocation(RuneCraftory.MODID, "ambrosia"));
        this.body = this.model.getPart("body");
        this.head = this.model.getPart("head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 51).mirror().addBox(-4.0F, 0.0F, -2.0F, 8.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -5.0F, 0.0F, 0.0698F, 0.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition hornFront = head.addOrReplaceChild("hornFront", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-0.5F, -5.0F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -8.0F, -2.5F, 0.6009F, 0.0F, 0.0F));

        PartDefinition hornLeftStick = head.addOrReplaceChild("hornLeftStick", CubeListBuilder.create().texOffs(4, 16).mirror().addBox(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.75F, -8.0F, -0.75F, 0.1745F, 0.0F, -0.0524F));

        PartDefinition hornLeftFlower = hornLeftStick.addOrReplaceChild("hornLeftFlower", CubeListBuilder.create().texOffs(8, 16).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -3.0F, 0.0F));

        PartDefinition hornRightStick = head.addOrReplaceChild("hornRightStick", CubeListBuilder.create().texOffs(4, 16).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.75F, -8.0F, -0.75F, 0.1745F, 0.0F, 0.0524F));

        PartDefinition hornRightFlower = hornRightStick.addOrReplaceChild("hornRightFlower", CubeListBuilder.create().texOffs(8, 16).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, 0.0F));

        PartDefinition leftUpperWing = body.addOrReplaceChild("leftUpperWing", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(0.0F, -15.0F, 0.0F, 17.0F, 15.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 5.0F, 2.0F, -0.2618F, 0.0F, 0.0873F));

        PartDefinition leftLowerWing = body.addOrReplaceChild("leftLowerWing", CubeListBuilder.create().texOffs(0, 37).mirror().addBox(0.0F, 0.0F, 0.0F, 17.0F, 14.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 5.0F, 2.0F, 0.2618F, 0.0F, -0.0873F));

        PartDefinition rightUpperWing = body.addOrReplaceChild("rightUpperWing", CubeListBuilder.create().texOffs(0, 22).addBox(-17.0F, -15.0F, 0.0F, 17.0F, 15.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.0F, 2.0F, -0.2618F, 0.0F, -0.0873F));

        PartDefinition rightLowerWing = body.addOrReplaceChild("rightLowerWing", CubeListBuilder.create().texOffs(0, 37).addBox(-17.0F, 0.0F, 0.0F, 17.0F, 14.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.0F, 2.0F, 0.2618F, 0.0F, 0.0873F));

        PartDefinition leftArm = body.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(33, 1).mirror().addBox(-1.0F, -2.0F, -1.5F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(5.0F, 2.5F, 0.0F, -0.1745F, 0.0F, 0.1F));

        PartDefinition leftArmDown = leftArm.addOrReplaceChild("leftArmDown", CubeListBuilder.create().texOffs(33, 11).mirror().addBox(-1.5F, 0.0F, -3.0F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.5F, 4.0F, 1.5F, -0.1745F, 0.0F, 0.0F));

        PartDefinition rightArm = body.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(33, 1).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, 2.5F, 0.0F, -0.1745F, 0.0F, -0.1F));

        PartDefinition rightArmDown = rightArm.addOrReplaceChild("rightArmDown", CubeListBuilder.create().texOffs(33, 11).addBox(-1.5F, 0.0F, -3.0F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 4.0F, 1.0F, -0.1745F, 0.0F, 0.0F));

        PartDefinition dressUp = body.addOrReplaceChild("dressUp", CubeListBuilder.create().texOffs(34, 50).mirror().addBox(-5.0F, 0.0F, -3.0F, 10.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 10.0F, 0.0F));

        PartDefinition dressDown = dressUp.addOrReplaceChild("dressDown", CubeListBuilder.create().texOffs(24, 57).mirror().addBox(-5.5F, 0.0F, -3.5F, 11.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 1.0F, 0.0F));

        PartDefinition leftLeg = body.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(34, 20).mirror().addBox(-1.9F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.9F, 11.0F, 0.0F, -0.0873F, 0.0F, 0.0F));

        PartDefinition leftLegDown = leftLeg.addOrReplaceChild("leftLegDown", CubeListBuilder.create().texOffs(50, 20).mirror().addBox(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.1F, 7.0F, -2.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition rightLeg = body.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(34, 31).addBox(-2.1F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.9F, 11.0F, 0.0F, -0.0873F, 0.0F, 0.0F));

        PartDefinition rightLegDown = rightLeg.addOrReplaceChild("rightLegDown", CubeListBuilder.create().texOffs(50, 30).mirror().addBox(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.1F, 7.0F, -2.0F, 0.2618F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 66, 65);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.model.getMainPart().render(poseStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.model.resetPoses();
        AnimatedAction anim = entity.getAnimationHandler().getAnimation();
        float partialTicks = Minecraft.getInstance().getFrameTime();
        if (entity.deathTime <= 0) {
            this.head.yRot += netHeadYaw * Mth.DEG_TO_RAD;
            this.head.xRot += headPitch * Mth.DEG_TO_RAD;
            this.anim.doAnimation(this, "iddle", entity.tickCount, partialTicks);
            if (entity.isMoving() && anim == null)
                this.body.xRot += 0.6108652381980154F;
        }
        if (anim != null)
            this.anim.doAnimation(this, anim.getAnimationClient(), anim.getTick(), partialTicks);
    }

    @Override
    public ModelPartHandler getHandler() {
        return this.model;
    }
}