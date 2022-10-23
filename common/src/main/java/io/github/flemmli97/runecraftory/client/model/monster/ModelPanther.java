package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityPanther;
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

public class ModelPanther<T extends EntityPanther> extends EntityModel<T> implements ExtendedModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(RuneCraftory.MODID, "panther"), "main");

    protected final ModelPartHandler model;
    protected final BlockBenchAnimations anim;

    public ModelPanther(ModelPart root) {
        super();
        this.model = new ModelPartHandler(root, "body");
        this.anim = AnimationManager.getInstance().getAnimation(new ResourceLocation(RuneCraftory.MODID, "panther"));
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -7.0F, -11.0F, 12.0F, 13.0F, 25.0F, new CubeDeformation(0.0F))
                .texOffs(24, 106).addBox(3.0F, -10.0F, -9.0F, 0.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(12, 106).addBox(3.0F, -10.0F, -1.0F, 0.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 106).addBox(3.0F, -10.0F, 8.0F, 0.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(48, 53).addBox(-2.0F, -6.0F, 14.0F, 10.0F, 11.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 3.5F, -1.0F, -0.5236F, 0.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(74, 0).addBox(-2.5F, -6.0F, -12.0F, 11.0F, 12.0F, 13.0F, new CubeDeformation(0.0F))
                .texOffs(66, 69).addBox(-1.0F, 3.75F, -15.5F, 8.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(74, 53).addBox(-1.0F, -1.0F, -16.0F, 8.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(86, 94).addBox(3.0F, -9.0F, -6.0F, 0.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.0F, -10.0F, 0.6109F, 0.0F, 0.0F));

        PartDefinition earRight = head.addOrReplaceChild("earRight", CubeListBuilder.create().texOffs(46, 106).addBox(-1.5F, -2.75F, -2.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -6.0F, -3.0F));

        PartDefinition earLeft = head.addOrReplaceChild("earLeft", CubeListBuilder.create().texOffs(36, 106).addBox(-1.5F, -2.75F, -2.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(7.0F, -6.0F, -3.0F));

        PartDefinition leftFrontLeg = body.addOrReplaceChild("leftFrontLeg", CubeListBuilder.create().texOffs(54, 38).addBox(-1.5F, -4.0F, -4.0F, 6.0F, 8.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(46, 111).addBox(4.5F, -4.0F, -0.5F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.5F, 4.0F, -6.5F, 0.1396F, -0.0873F, -0.3491F));

        PartDefinition leftFrontUpper = leftFrontLeg.addOrReplaceChild("leftFrontUpper", CubeListBuilder.create().texOffs(22, 69).addBox(-2.0F, 0.0F, -6.0F, 5.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 4.0F, 2.5F, -0.9163F, 0.0F, 0.0873F));

        PartDefinition leftFrontDown = leftFrontUpper.addOrReplaceChild("leftFrontDown", CubeListBuilder.create().texOffs(0, 94).addBox(-1.5F, -1.0F, -5.0F, 4.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.0F, -0.5F, -0.2618F, 0.0F, 0.1745F));

        PartDefinition leftFrontPaw = leftFrontDown.addOrReplaceChild("leftFrontPaw", CubeListBuilder.create().texOffs(78, 82).addBox(-1.5F, -1.25F, -6.0F, 4.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, -1.0F, 0.2182F, 0.0F, 0.0F));

        PartDefinition rightFrontLeg = body.addOrReplaceChild("rightFrontLeg", CubeListBuilder.create().texOffs(28, 38).addBox(-4.5F, -4.0F, -4.0F, 6.0F, 8.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(46, 111).mirror().addBox(-7.5F, -4.0F, -0.5F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.5F, 4.0F, -6.5F, 0.1396F, 0.0873F, 0.3491F));

        PartDefinition rightFrontUpper = rightFrontLeg.addOrReplaceChild("rightFrontUpper", CubeListBuilder.create().texOffs(0, 69).addBox(-3.0F, 0.0F, -6.0F, 5.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 4.0F, 2.5F, -0.9163F, 0.0F, -0.0873F));

        PartDefinition rightFrontDown = rightFrontUpper.addOrReplaceChild("rightFrontDown", CubeListBuilder.create().texOffs(60, 82).addBox(-2.5F, -1.0F, -5.0F, 4.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.0F, -0.5F, -0.2618F, 0.0F, -0.1745F));

        PartDefinition rightFrontPaw = rightFrontDown.addOrReplaceChild("rightFrontPaw", CubeListBuilder.create().texOffs(40, 82).addBox(-2.5F, -1.25F, -6.0F, 4.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, -1.0F, 0.2182F, 0.0F, 0.0F));

        PartDefinition leftHindLeg = body.addOrReplaceChild("leftHindLeg", CubeListBuilder.create().texOffs(24, 53).addBox(-1.5F, -3.0F, -3.5F, 5.0F, 9.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.5F, 2.0F, 9.5F, -0.3054F, 0.0F, 0.0F));

        PartDefinition leftHindUpper = leftHindLeg.addOrReplaceChild("leftHindUpper", CubeListBuilder.create().texOffs(20, 82).addBox(-2.5F, 0.0F, 0.0F, 5.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 6.0F, -2.0F, 1.1345F, 0.0F, 0.0F));

        PartDefinition leftHindDown = leftHindUpper.addOrReplaceChild("leftHindDown", CubeListBuilder.create().texOffs(34, 94).addBox(-2.0F, 0.0F, -4.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.0F, 4.5F, -0.6981F, 0.0F, 0.0F));

        PartDefinition leftHindPaw = leftHindDown.addOrReplaceChild("leftHindPaw", CubeListBuilder.create().texOffs(68, 94).addBox(-2.0F, -1.0F, -5.0F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.25F, -1.5F, 0.3927F, 0.0F, 0.0F));

        PartDefinition rightHindLeg = body.addOrReplaceChild("rightHindLeg", CubeListBuilder.create().texOffs(0, 53).addBox(-3.5F, -3.0F, -3.5F, 5.0F, 9.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 2.0F, 9.5F, -0.3054F, 0.0F, 0.0F));

        PartDefinition rightHindUpper = rightHindLeg.addOrReplaceChild("rightHindUpper", CubeListBuilder.create().texOffs(0, 82).addBox(-2.5F, 0.0F, 0.0F, 5.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 6.0F, -2.0F, 1.1345F, 0.0F, 0.0F));

        PartDefinition rightHindDown = rightHindUpper.addOrReplaceChild("rightHindDown", CubeListBuilder.create().texOffs(18, 94).addBox(-2.0F, 0.0F, -4.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.0F, 4.5F, -0.6981F, 0.0F, 0.0F));

        PartDefinition rightHindPaw = rightHindDown.addOrReplaceChild("rightHindPaw", CubeListBuilder.create().texOffs(50, 94).addBox(-2.0F, -1.0F, -5.0F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.25F, -1.5F, 0.3927F, 0.0F, 0.0F));

        PartDefinition tailBase = body.addOrReplaceChild("tailBase", CubeListBuilder.create().texOffs(44, 69).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -4.5F, 17.0F));

        PartDefinition tailMiddle = tailBase.addOrReplaceChild("tailMiddle", CubeListBuilder.create().texOffs(80, 38).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 3.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, 7.0F, -0.0873F, 0.0F, 0.0F));

        PartDefinition tailTip = tailMiddle.addOrReplaceChild("tailTip", CubeListBuilder.create().texOffs(0, 38).addBox(-1.0F, -2.0F, 0.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 11.0F, 0.48F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
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
        if (entity.deathTime <= 0 && !entity.playDeath()) {
            this.anim.doAnimation(this, "iddle", entity.tickCount, partialTicks);
            if (entity.moveTick() > 0)
                this.anim.doAnimation(this, "walk", entity.tickCount, partialTicks, entity.interpolatedMoveTick(partialTicks));
        }
        if (anim != null)
            this.anim.doAnimation(this, anim.getAnimationClient(), anim.getTick(), partialTicks);
    }

    @Override
    public ModelPartHandler getHandler() {
        return this.model;
    }
}