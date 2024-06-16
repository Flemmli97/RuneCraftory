package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.client.model.SittingModel;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityWolf;
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

public class ModelWolf<T extends EntityWolf> extends EntityModel<T> implements ExtendedModel, RideableModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(RuneCraftory.MODID, "wolf"), "main");

    protected final ModelPartHandler model;
    protected final BlockBenchAnimations anim;

    public ModelPartHandler.ModelPartExtended body;
    public ModelPartHandler.ModelPartExtended upper;
    public ModelPartHandler.ModelPartExtended ridingPosition;

    public ModelWolf(ModelPart root) {
        super();
        this.model = new ModelPartHandler(root, "root");
        this.anim = AnimationManager.getInstance().getAnimation(new ResourceLocation(RuneCraftory.MODID, "wolf"));
        this.body = this.model.getPart("body");
        this.upper = this.model.getPart("upperBody");
        this.ridingPosition = this.model.getPart("ridingPos");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -4.75F, -6.0F, 10.0F, 9.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 11.75F, 0.0F));

        PartDefinition upperBody = body.addOrReplaceChild("upperBody", CubeListBuilder.create().texOffs(50, 0).addBox(-5.5F, -5.0F, -2.0F, 11.0F, 10.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.75F, -6.0F));

        PartDefinition head = upperBody.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 24).addBox(-4.5F, -2.0F, -4.0F, 9.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.5F, -2.0F));

        PartDefinition earsRight = head.addOrReplaceChild("earsRight", CubeListBuilder.create().texOffs(20, 63).addBox(-1.5F, -3.0F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, -2.5F, 2.0F));

        PartDefinition earsLeft = head.addOrReplaceChild("earsLeft", CubeListBuilder.create().texOffs(10, 63).addBox(-1.5F, -3.0F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -2.5F, 2.0F));

        PartDefinition snout = head.addOrReplaceChild("snout", CubeListBuilder.create().texOffs(0, 37).addBox(-2.5F, -1.5F, -3.0F, 5.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.5F, -4.0F));

        PartDefinition mouth = snout.addOrReplaceChild("mouth", CubeListBuilder.create().texOffs(36, 47).addBox(-2.5F, 0.0F, -3.0F, 5.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.5F, 0.0F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(24, 47).addBox(-1.5F, 0.0F, -3.0F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.75F, 9.0F, 0.9599F, 0.0F, 0.0F));

        PartDefinition tail2 = tail.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(16, 37).addBox(-1.5F, 0.0F, -3.0F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, -0.4363F, 0.0F, 0.0F));

        PartDefinition leftFrontLegBase = body.addOrReplaceChild("leftFrontLegBase", CubeListBuilder.create().texOffs(12, 56).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 3.25F, -3.5F, 0.4363F, 0.0F, 0.0F));

        PartDefinition leftFrontLeg = leftFrontLegBase.addOrReplaceChild("leftFrontLeg", CubeListBuilder.create().texOffs(12, 47).addBox(-1.5F, 0.0F, -2.0F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.5F, 0.5F, -0.7418F, 0.0F, 0.0F));

        PartDefinition leftFrontPaw = leftFrontLeg.addOrReplaceChild("leftFrontPaw", CubeListBuilder.create().texOffs(0, 63).addBox(-3.0F, 0.0F, -2.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 6.0F, 1.0F, -1.2654F, 0.0F, 0.0F));

        PartDefinition rightFrontLegBase = body.addOrReplaceChild("rightFrontLegBase", CubeListBuilder.create().texOffs(0, 56).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 3.25F, -3.5F, 0.4363F, 0.0F, 0.0F));

        PartDefinition rightFrontLeg = rightFrontLegBase.addOrReplaceChild("rightFrontLeg", CubeListBuilder.create().texOffs(0, 47).addBox(-1.25F, 0.0F, -2.0F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.25F, 3.5F, 0.5F, -0.7418F, 0.0F, 0.0F));

        PartDefinition rightFrontPaw = rightFrontLeg.addOrReplaceChild("rightFrontPaw", CubeListBuilder.create().texOffs(44, 56).addBox(-3.0F, 0.0F, -2.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.75F, 6.0F, 1.0F, -1.2654F, 0.0F, 0.0F));

        PartDefinition leftHindLegBase = body.addOrReplaceChild("leftHindLegBase", CubeListBuilder.create().texOffs(40, 24).addBox(-1.5F, 0.0F, -2.0F, 3.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 2.5F, 5.0F, 0.4363F, 0.0F, 0.0F));

        PartDefinition leftHindLeg = leftHindLegBase.addOrReplaceChild("leftHindLeg", CubeListBuilder.create().texOffs(40, 37).addBox(-1.5F, 0.0F, -2.0F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.5F, 1.0F, -0.7418F, 0.0F, 0.0F));

        PartDefinition leftHindPaw = leftHindLeg.addOrReplaceChild("leftHindPaw", CubeListBuilder.create().texOffs(34, 56).addBox(-3.0F, 0.0F, -2.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 6.0F, 1.0F, -1.2654F, 0.0F, 0.0F));

        PartDefinition rightHindLegBase = body.addOrReplaceChild("rightHindLegBase", CubeListBuilder.create().texOffs(26, 24).addBox(-1.5F, 0.0F, -2.0F, 3.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 2.5F, 5.0F, 0.4363F, 0.0F, 0.0F));

        PartDefinition rightHindLeg = rightHindLegBase.addOrReplaceChild("rightHindLeg", CubeListBuilder.create().texOffs(28, 37).addBox(-1.5F, 0.0F, -2.0F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.5F, 1.0F, -0.7418F, 0.0F, 0.0F));

        PartDefinition rightHindPaw = rightHindLeg.addOrReplaceChild("rightHindPaw", CubeListBuilder.create().texOffs(24, 56).addBox(-3.0F, 0.0F, -2.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 6.0F, 1.0F, -1.2654F, 0.0F, 0.0F));

        PartDefinition ridingPos = body.addOrReplaceChild("ridingPos", CubeListBuilder.create(), PartPose.offset(0.0F, -4.75F, 6.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.model.getMainPart().render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.model.resetPoses();
        this.upper.yRot += netHeadYaw * Mth.DEG_TO_RAD * 0.5f;
        this.upper.xRot += headPitch * Mth.DEG_TO_RAD * 0.5f;
        float partialTicks = Minecraft.getInstance().getFrameTime();
        if (entity.deathTime <= 0 && !entity.playDeath()) {
            this.anim.doAnimation(this, "idle", entity.tickCount, partialTicks);
            if (entity.moveTick() > 0) {
                if (entity.getMoveFlag() == BaseMonster.MoveType.RUN)
                    this.anim.doAnimation(this, "run", entity.tickCount, partialTicks, entity.interpolatedMoveTick(partialTicks));
                else
                    this.anim.doAnimation(this, "walk", entity.tickCount, partialTicks, entity.interpolatedMoveTick(partialTicks));
            }
        }
        this.anim.doAnimation(this, entity.getAnimationHandler(), partialTicks);
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
                this.ridingPosition.translateAndRotate(poseStack);
                ClientHandlers.translateRider(entityRenderer, rider, model, poseStack);
                return true;
            }
        }
        return false;
    }
}