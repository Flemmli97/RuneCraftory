package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.SittingModel;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.EntityThunderbolt;
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

public class ModelThunderbolt<T extends EntityThunderbolt> extends EntityModel<T> implements ExtendedModel, RideableModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(RuneCraftory.MODID, "thunderbolt"), "main");

    protected final ModelPartHandler model;
    protected final BlockBenchAnimations anim;

    public ModelPartHandler.ModelPartExtended body;
    public ModelPartHandler.ModelPartExtended head;
    public ModelPartHandler.ModelPartExtended neck;

    public ModelThunderbolt(ModelPart root) {
        super();
        this.model = new ModelPartHandler(root, "root");
        this.anim = AnimationManager.getInstance().getAnimation(new ResourceLocation(RuneCraftory.MODID, "thunderbolt"));
        this.body = this.model.getPart("body");
        this.head = this.model.getPart("head");
        this.neck = this.model.getPart("neck");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-6.5F, -7.0F, -13.0F, 13.0F, 13.0F, 26.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 0.0F));

        PartDefinition backFur = body.addOrReplaceChild("backFur", CubeListBuilder.create().texOffs(0, 39).addBox(-0.5F, -2.0F, -12.0F, 1.0F, 3.0F, 24.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.0F, 3.0F));

        PartDefinition neck = body.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(52, 0).addBox(-2.5F, -12.0F, 0.0F, 7.0F, 12.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(20, 66).addBox(0.5F, -12.0F, 8.0F, 1.0F, 12.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 0.0F, -13.0F, 0.5236F, 0.0F, 0.0F));

        PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create().texOffs(26, 39).addBox(-5.0F, -9.0F, -2.5F, 10.0F, 10.0F, 13.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-0.5F, -12.0F, 3.5F, 1.0F, 13.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(59, 39).addBox(-3.5F, -8.0F, -6.5F, 7.0F, 9.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 66).addBox(-2.5F, -7.5F, -11.5F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -13.0F, 0.0F));

        PartDefinition horn = head.addOrReplaceChild("horn", CubeListBuilder.create().texOffs(82, 49).addBox(-1.5F, 0.0F, 0.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(52, 0).addBox(-1.0F, -5.0F, 1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -12.0F, -2.0F));

        PartDefinition leftFrontLegBase = body.addOrReplaceChild("leftFrontLegBase", CubeListBuilder.create().texOffs(75, 0).addBox(-1.5F, -1.0F, -0.5F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(4.5F, 4.0F, -13.0F));

        PartDefinition leftFrontLeg = leftFrontLegBase.addOrReplaceChild("leftFrontLeg", CubeListBuilder.create().texOffs(54, 82).addBox(-1.0F, 0.0F, -3.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, 3.0F));

        PartDefinition leftFrontLegDown = leftFrontLeg.addOrReplaceChild("leftFrontLegDown", CubeListBuilder.create().texOffs(0, 79).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 5.0F, -3.0F));

        PartDefinition leftFrontLegFur = leftFrontLegDown.addOrReplaceChild("leftFrontLegFur", CubeListBuilder.create().texOffs(12, 48).addBox(0.0F, -1.0F, 0.0F, 0.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 3.0F));

        PartDefinition leftFrontHoove = leftFrontLegDown.addOrReplaceChild("leftFrontHoove", CubeListBuilder.create().texOffs(77, 68).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 1.5F));

        PartDefinition rightFrontLegBase = body.addOrReplaceChild("rightFrontLegBase", CubeListBuilder.create().texOffs(73, 74).addBox(-1.5F, -1.0F, -0.5F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.5F, 4.0F, -13.0F));

        PartDefinition rightFrontLeg = rightFrontLegBase.addOrReplaceChild("rightFrontLeg", CubeListBuilder.create().texOffs(81, 41).addBox(-1.0F, 0.0F, -3.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, 3.0F));

        PartDefinition rightFrontLegDown = rightFrontLeg.addOrReplaceChild("rightFrontLegDown", CubeListBuilder.create().texOffs(78, 21).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 5.0F, -3.0F));

        PartDefinition rightFrontLegFur = rightFrontLegDown.addOrReplaceChild("rightFrontLegFur", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -1.0F, 0.0F, 0.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 3.0F));

        PartDefinition rightFrontHoove = rightFrontLegDown.addOrReplaceChild("rightFrontHoove", CubeListBuilder.create().texOffs(77, 62).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 1.5F));

        PartDefinition leftBackLegBase = body.addOrReplaceChild("leftBackLegBase", CubeListBuilder.create().texOffs(43, 62).addBox(-2.5F, -4.0F, -3.5F, 5.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(4.5F, 4.0F, 9.6F));

        PartDefinition leftBackLeg = leftBackLegBase.addOrReplaceChild("leftBackLeg", CubeListBuilder.create().texOffs(72, 52).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, -1.5F, 0.3927F, 0.0F, 0.0F));

        PartDefinition leftBackLegDown = leftBackLeg.addOrReplaceChild("leftBackLegDown", CubeListBuilder.create().texOffs(42, 76).addBox(-1.5F, 0.0F, -3.0F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(12, 17).addBox(0.0F, 0.0F, -1.0F, 0.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 4.0F, -0.3927F, 0.0F, 0.0F));

        PartDefinition leftBackHoove = leftBackLegDown.addOrReplaceChild("leftBackHoove", CubeListBuilder.create().texOffs(77, 35).addBox(-1.5F, -0.3007F, -1.9537F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 7.3007F, -1.5463F));

        PartDefinition rightBackLegBase = body.addOrReplaceChild("rightBackLegBase", CubeListBuilder.create().texOffs(0, 39).addBox(-2.5F, -4.0F, -3.5F, 5.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.5F, 4.0F, 9.6F));

        PartDefinition rightBackLeg = rightBackLegBase.addOrReplaceChild("rightBackLeg", CubeListBuilder.create().texOffs(12, 0).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, -1.5F, 0.3927F, 0.0F, 0.0F));

        PartDefinition rightBackLegDown = rightBackLeg.addOrReplaceChild("rightBackLegDown", CubeListBuilder.create().texOffs(26, 39).addBox(-1.5F, 0.0F, -3.0F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 17).addBox(0.0F, 0.0F, -1.0F, 0.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 4.0F, -0.3927F, 0.0F, 0.0F));

        PartDefinition rightBackHoove = rightBackLegDown.addOrReplaceChild("rightBackHoove", CubeListBuilder.create().texOffs(54, 76).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.0F, -1.5F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 53).addBox(-2.5F, 0.0F, -1.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(66, 78).addBox(-1.0F, -1.0F, 0.0F, 1.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -5.5F, 13.0F, 0.6545F, 0.0F, 0.0F));

        PartDefinition tailTip = tail.addOrReplaceChild("tailTip", CubeListBuilder.create().texOffs(67, 62).addBox(-1.5F, 0.0F, -3.5F, 2.0F, 13.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(32, 66).addBox(-1.0F, -1.0F, -3.5F, 1.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 3.0F, -0.5236F, 0.0F, 0.0F));

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
        this.head.yRot += netHeadYaw * Mth.DEG_TO_RAD * 0.15f;
        this.head.xRot += headPitch * Mth.DEG_TO_RAD * 0.15f;
        this.neck.yRot += netHeadYaw * Mth.DEG_TO_RAD * 0.15f;
        this.neck.xRot += headPitch * Mth.DEG_TO_RAD * 0.15f;

        float partialTicks = Minecraft.getInstance().getFrameTime();
        if (anim == null) {
            if (entity.deathTime <= 0 && !entity.playDeath()) {
                if (entity.getMoveFlag() == BaseMonster.MoveType.RUN)
                    this.anim.doAnimation(this, "run", entity.tickCount, partialTicks, entity.interpolatedMoveTick(partialTicks));
                else if (entity.moveTick() > 0)
                    this.anim.doAnimation(this, "walk", entity.tickCount, partialTicks, entity.interpolatedMoveTick(partialTicks));
            }
        } else
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
                    poseStack.translate(0, 3 / 16d, 1 / 16d);
                return true;
            }
        }
        return false;
    }
}