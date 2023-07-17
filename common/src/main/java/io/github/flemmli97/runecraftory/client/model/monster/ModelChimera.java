package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.SittingModel;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.EntityChimera;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.client.AnimationManager;
import io.github.flemmli97.tenshilib.client.model.BlockBenchAnimations;
import io.github.flemmli97.tenshilib.client.model.ExtendedModel;
import io.github.flemmli97.tenshilib.client.model.ModelPartHandler;
import io.github.flemmli97.tenshilib.client.model.RideableModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.IllagerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class ModelChimera<T extends EntityChimera> extends EntityModel<T> implements ExtendedModel, RideableModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(RuneCraftory.MODID, "chimera"), "main");

    protected final ModelPartHandler model;
    protected final BlockBenchAnimations anim;

    public ModelPartHandler.ModelPartExtended body;
    public ModelPartHandler.ModelPartExtended upperBody;

    public ModelChimera(ModelPart root) {
        super();
        this.model = new ModelPartHandler(root, "root");
        this.anim = AnimationManager.getInstance().getAnimation(new ResourceLocation(RuneCraftory.MODID, "chimera"));
        this.body = this.model.getPart("body");
        this.upperBody = this.model.getPart("upperBody");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-6.5F, -7.0F, -7.0F, 13.0F, 12.0F, 18.0F, new CubeDeformation(0.0F))
                .texOffs(56, 52).addBox(-1.0F, -8.0F, -7.0F, 2.0F, 1.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 8.25F, 0.0F));

        PartDefinition wingLeftBase = body.addOrReplaceChild("wingLeftBase", CubeListBuilder.create().texOffs(8, 0).addBox(0.0F, 0.0F, -3.5F, 2.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(6.5F, -6.0F, 0.5F));

        PartDefinition wingLeft = wingLeftBase.addOrReplaceChild("wingLeft", CubeListBuilder.create().texOffs(0, 30).addBox(0.0F, 0.0F, -3.0F, 18.0F, 0.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 0.0F, -3.5F));

        PartDefinition wingRightBase = body.addOrReplaceChild("wingRightBase", CubeListBuilder.create().texOffs(8, 0).mirror().addBox(-2.0F, 0.0F, -3.5F, 2.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-6.5F, -6.0F, 0.5F));

        PartDefinition wingRight = wingRightBase.addOrReplaceChild("wingRight", CubeListBuilder.create().texOffs(29, 0).addBox(-18.0F, 0.0F, -3.0F, 18.0F, 0.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 0.0F, -3.5F));

        PartDefinition upperBody = body.addOrReplaceChild("upperBody", CubeListBuilder.create().texOffs(0, 45).addBox(-5.5F, -5.0F, -5.0F, 12.0F, 12.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, -4.0F, -7.0F));

        PartDefinition hornMiddleLeft = upperBody.addOrReplaceChild("hornMiddleLeft", CubeListBuilder.create().texOffs(51, 63).addBox(-1.0F, -4.0F, -2.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.5F, -4.5F, -2.75F, 0.0F, 0.0F, 0.5236F));

        PartDefinition hornBackLeft = upperBody.addOrReplaceChild("hornBackLeft", CubeListBuilder.create().texOffs(80, 0).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.5F, -5.0F, 0.0F, -0.2618F, 0.0F, 0.0F));

        PartDefinition hornMiddleRight = upperBody.addOrReplaceChild("hornMiddleRight", CubeListBuilder.create().texOffs(51, 39).addBox(-1.0F, -4.0F, -2.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5F, -4.5F, -2.75F, 0.0F, 0.0F, -0.5236F));

        PartDefinition hornBackRight = upperBody.addOrReplaceChild("hornBackRight", CubeListBuilder.create(), PartPose.offset(-3.5F, -5.0F, 0.0F));

        PartDefinition hornBackRight_r1 = hornBackRight.addOrReplaceChild("hornBackRight_r1", CubeListBuilder.create().texOffs(78, 74).addBox(-1.0F, -5.0F, 0.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, -1.0F, -0.2618F, 0.0F, 0.0F));

        PartDefinition head = upperBody.addOrReplaceChild("head", CubeListBuilder.create().texOffs(34, 45).addBox(-4.5F, -4.0F, -6.0F, 10.0F, 11.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(51, 30).addBox(-5.5F, -4.75F, -4.5F, 12.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.75F, -4.5F));

        PartDefinition hornFront = head.addOrReplaceChild("hornFront", CubeListBuilder.create().texOffs(62, 22).addBox(-1.0F, -6.0F, -2.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -5.0F, -1.0F, 0.6981F, 0.0F, 0.0F));

        PartDefinition snout = head.addOrReplaceChild("snout", CubeListBuilder.create().texOffs(70, 53).addBox(-2.5F, -2.5F, -3.0F, 6.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.5F, -6.0F));

        PartDefinition mouth = snout.addOrReplaceChild("mouth", CubeListBuilder.create().texOffs(71, 63).addBox(-2.5F, 0.0F, -3.0F, 6.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.5F, 0.0F, 0.5236F, 0.0F, 0.0F));

        PartDefinition tongue = mouth.addOrReplaceChild("tongue", CubeListBuilder.create().texOffs(0, 12).addBox(-2.0F, 0.0F, -5.5F, 4.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 0.0F, 0.5F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(58, 63).addBox(-1.5F, -2.5F, 0.0F, 3.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, -2.5F, 10.5F));

        PartDefinition tail2 = tail.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(38, 62).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 7.0F));

        PartDefinition tailTip = tail2.addOrReplaceChild("tailTip", CubeListBuilder.create().texOffs(18, 62).addBox(-2.5F, -2.5F, -0.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 7.0F));

        PartDefinition tailWingLeft = tailTip.addOrReplaceChild("tailWingLeft", CubeListBuilder.create().texOffs(55, 15).addBox(0.0F, 0.0F, -3.5F, 7.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(2.25F, -0.5F, 2.0F));

        PartDefinition tailWingRight = tailTip.addOrReplaceChild("tailWingRight", CubeListBuilder.create().texOffs(53, 39).addBox(-7.0F, 0.0F, -3.5F, 7.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.25F, -0.5F, 2.0F));

        PartDefinition leftFrontLegBase = body.addOrReplaceChild("leftFrontLegBase", CubeListBuilder.create().texOffs(14, 72).addBox(-1.5F, 0.0F, -2.5F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 4.0F, -4.5F, 0.4363F, 0.0F, 0.0F));

        PartDefinition leftFrontLeg = leftFrontLegBase.addOrReplaceChild("leftFrontLeg", CubeListBuilder.create().texOffs(62, 73).addBox(-1.5F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(-0.001F)), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, -0.7418F, 0.0F, 0.0F));

        PartDefinition leftFrontPaw = leftFrontLeg.addOrReplaceChild("leftFrontPaw", CubeListBuilder.create().texOffs(78, 67).addBox(-3.0F, 0.0F, -2.0F, 4.0F, 5.0F, 2.0F, new CubeDeformation(-0.001F)), PartPose.offsetAndRotation(1.5F, 6.0F, 2.0F, -1.2654F, 0.0F, 0.0F));

        PartDefinition rightFrontLegBase = body.addOrReplaceChild("rightFrontLegBase", CubeListBuilder.create().texOffs(70, 42).addBox(-1.5F, 0.0F, -2.5F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5F, 4.0F, -4.5F, 0.4363F, 0.0F, 0.0F));

        PartDefinition rightFrontLeg = rightFrontLegBase.addOrReplaceChild("rightFrontLeg", CubeListBuilder.create().texOffs(46, 72).addBox(-1.25F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(-0.001F)), PartPose.offsetAndRotation(-0.25F, 6.0F, 0.0F, -0.7418F, 0.0F, 0.0F));

        PartDefinition rightFrontPaw = rightFrontLeg.addOrReplaceChild("rightFrontPaw", CubeListBuilder.create().texOffs(0, 74).addBox(-3.0F, 0.0F, -2.0F, 4.0F, 5.0F, 2.0F, new CubeDeformation(-0.001F)), PartPose.offsetAndRotation(1.75F, 6.0F, 2.0F, -1.2654F, 0.0F, 0.0F));

        PartDefinition leftHindLegBase = body.addOrReplaceChild("leftHindLegBase", CubeListBuilder.create().texOffs(0, 62).addBox(-1.5F, 0.0F, -3.0F, 4.0F, 7.0F, 5.0F, new CubeDeformation(0.025F)), PartPose.offsetAndRotation(2.5F, 3.25F, 6.0F, 0.4363F, 0.0F, 0.0F));

        PartDefinition leftHindLeg = leftHindLegBase.addOrReplaceChild("leftHindLeg", CubeListBuilder.create().texOffs(30, 72).addBox(-1.5F, 0.0F, -3.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.5F, 1.0F, -0.7418F, 0.0F, 0.0F));

        PartDefinition leftHindPaw = leftHindLeg.addOrReplaceChild("leftHindPaw", CubeListBuilder.create().texOffs(0, 37).addBox(-3.0F, 0.0F, -3.0F, 4.0F, 4.0F, 3.0F, new CubeDeformation(-0.025F)), PartPose.offsetAndRotation(1.5F, 7.0F, 1.0F, -1.2654F, 0.0F, 0.0F));

        PartDefinition rightHindLegBase = body.addOrReplaceChild("rightHindLegBase", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, -3.0F, 4.0F, 7.0F, 5.0F, new CubeDeformation(0.025F)), PartPose.offsetAndRotation(-3.5F, 3.25F, 6.0F, 0.4363F, 0.0F, 0.0F));

        PartDefinition rightHindLeg = rightHindLegBase.addOrReplaceChild("rightHindLeg", CubeListBuilder.create().texOffs(72, 18).addBox(-1.5F, 0.0F, -3.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.5F, 1.0F, -0.7418F, 0.0F, 0.0F));

        PartDefinition rightHindPaw = rightHindLeg.addOrReplaceChild("rightHindPaw", CubeListBuilder.create().texOffs(0, 30).addBox(-3.0F, 0.0F, -3.0F, 4.0F, 4.0F, 3.0F, new CubeDeformation(-0.025F)), PartPose.offsetAndRotation(1.5F, 7.0F, 1.0F, -1.2654F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.model.getMainPart().render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.model.resetPoses();
        AnimatedAction anim = entity.getAnimationHandler().getAnimation();
        float partialTicks = Minecraft.getInstance().getFrameTime();
        if (entity.deathTime <= 0 && !entity.playDeath()) {
            this.upperBody.yRot += netHeadYaw * 0.8 * Mth.DEG_TO_RAD;
            this.upperBody.xRot += headPitch * Mth.DEG_TO_RAD;
            this.anim.doAnimation(this, "idle", entity.tickCount, partialTicks);
            if (entity.moveTick() > 0) {
                if (entity.getMoveFlag() == BaseMonster.MoveType.RUN)
                    this.anim.doAnimation(this, "run", entity.tickCount, partialTicks, entity.interpolatedMoveTick(partialTicks));
                else
                    this.anim.doAnimation(this, "walk", entity.tickCount, partialTicks, entity.interpolatedMoveTick(partialTicks));
            }
        }
        if (anim != null)
            this.anim.doAnimation(this, anim.getAnimationClient(), anim.getTick(), partialTicks);
    }

    @Override
    public ModelPartHandler getHandler() {
        return this.model;
    }

    @Override
    public boolean transform(T entity, EntityRenderer<T> entityRenderer, Entity rider, EntityRenderer<?> ridingEntityRenderer, PoseStack poseStack, int riderNum) {
        if (ridingEntityRenderer instanceof LivingEntityRenderer<?, ?> lR) {
            EntityModel<?> model = lR.getModel();
            if (model instanceof HumanoidModel<?> || model instanceof IllagerModel<?> || model instanceof SittingModel) {
                this.body.translateAndRotate(poseStack);
                if (model instanceof SittingModel sittingModel)
                    sittingModel.translateSittingPosition(poseStack);
                else
                    poseStack.translate(0, 10 / 16d, 5 / 16d);
                return true;
            }
        }
        return false;
    }
}