package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityGoblin;
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

public class ModelGoblin<T extends EntityGoblin> extends EntityModel<T> implements ExtendedModel, IItemArmModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(RuneCraftory.MODID, "goblin"), "main");

    protected final ModelPartHandler model;
    protected final BlockBenchAnimations anim;

    public ModelPartHandler.ModelPartExtended head;
    public ModelPartHandler.ModelPartExtended leftArm;
    public ModelPartHandler.ModelPartExtended leftArmDown;
    public ModelPartHandler.ModelPartExtended rightArm;
    public ModelPartHandler.ModelPartExtended rightArmDown;

    public ModelGoblin(ModelPart root) {
        super();
        this.model = new ModelPartHandler(root, "body");
        this.anim = AnimationManager.getInstance().getAnimation(new ResourceLocation(RuneCraftory.MODID, "goblin"));
        this.head = this.model.getPart("head");
        this.leftArm = this.model.getPart("leftArm");
        this.leftArmDown = this.model.getPart("leftArmDown");
        this.rightArm = this.model.getPart("rightArm");
        this.rightArmDown = this.model.getPart("rightArmDown");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -5.0F, -1.5F, 7.0F, 12.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 11.0F, 0.0F));

        PartDefinition leftArm = body.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(0, 32).addBox(0.0F, -1.5F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, -1.0F, 1.0F));

        PartDefinition leftArmDown = leftArm.addOrReplaceChild("leftArmDown", CubeListBuilder.create().texOffs(8, 25).addBox(-2.0F, 0.0F, -2.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 2.5F, 1.0F));

        PartDefinition rightArm = body.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(24, 25).addBox(-2.0F, -1.5F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.5F, -1.0F, 1.0F));

        PartDefinition rightArmDown = rightArm.addOrReplaceChild("rightArmDown", CubeListBuilder.create().texOffs(0, 25).addBox(0.0F, 0.0F, -2.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 2.5F, 1.0F));

        PartDefinition rightLeg = body.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(32, 17).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, 7.0F, 1.0F));

        PartDefinition rightFoot = rightLeg.addOrReplaceChild("rightFoot", CubeListBuilder.create().texOffs(12, 17).addBox(-1.0F, 0.0F, -4.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, 1.0F));

        PartDefinition leftLeg = body.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(24, 17).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 7.0F, 1.0F));

        PartDefinition leftFoot = leftLeg.addOrReplaceChild("leftFoot", CubeListBuilder.create().texOffs(0, 17).addBox(-1.0F, 0.0F, -4.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, 1.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(24, 0).addBox(-3.0F, -6.0F, -2.5F, 6.0F, 6.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(16, 32).addBox(-1.0F, -2.0F, -4.25F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 38).addBox(-1.0F, -3.0F, -3.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.0F, 0.0F));

        PartDefinition horn = head.addOrReplaceChild("horn", CubeListBuilder.create().texOffs(30, 32).addBox(-0.5F, -2.0F, 0.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.0F, -3.1F, 0.6545F, 0.0F, 0.0F));

        PartDefinition leftEar = head.addOrReplaceChild("leftEar", CubeListBuilder.create().texOffs(16, 25).addBox(-1.0F, -3.0F, -0.5F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(24, 32).addBox(-1.0F, -3.0F, 2.5F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(8, 32).addBox(-1.0F, -3.0F, 4.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -2.0F, 0.0F, 0.2182F, 0.829F, 0.0F));

        PartDefinition rightEar = head.addOrReplaceChild("rightEar", CubeListBuilder.create().texOffs(16, 25).mirror().addBox(0.0F, -3.0F, -0.5F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(24, 32).addBox(0.0F, -3.0F, 2.5F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(8, 32).addBox(0.0F, -3.0F, 4.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -2.0F, 0.0F, 0.2182F, -0.829F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
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
        poseStack.translate(0.0, 11 / 16d, 0.0);
        this.model.getMainPart().translateAndRotate(poseStack);
        if (humanoidArm == HumanoidArm.LEFT) {
            this.leftArmDown.translateAndRotate(poseStack);
            this.leftArm.translateAndRotate(poseStack);
            poseStack.translate(-1 / 16d, 4 / 16d, -2 / 16d);
        } else {
            this.rightArmDown.translateAndRotate(poseStack);
            this.rightArm.translateAndRotate(poseStack);
            poseStack.translate(1 / 16d, 4 / 16d, -2 / 16d);
        }
        poseStack.scale(0.7f, 0.7f, 0.7f);
    }

    @Override
    public void postTransform(boolean leftSide, PoseStack stack) {
    }
}