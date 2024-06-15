package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.SittingModel;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityScorpion;
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
import net.minecraft.world.entity.Entity;

public class ModelScorpion<T extends EntityScorpion> extends EntityModel<T> implements ExtendedModel, RideableModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(RuneCraftory.MODID, "scorpion"), "main");

    protected final ModelPartHandler model;
    protected final BlockBenchAnimations anim;

    public ModelPartHandler.ModelPartExtended body;
    public ModelPartHandler.ModelPartExtended ridingPosition;

    public ModelScorpion(ModelPart root) {
        super();
        this.model = new ModelPartHandler(root, "root");
        this.anim = AnimationManager.getInstance().getAnimation(new ResourceLocation(RuneCraftory.MODID, "scorpion"));
        this.body = this.model.getPart("body");
        this.ridingPosition = this.model.getPart("ridingPos");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(34, 0).addBox(-4.0F, -3.0F, -5.0F, 9.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 21.0F, 3.0F));

        PartDefinition bodyBack = body.addOrReplaceChild("bodyBack", CubeListBuilder.create().texOffs(66, 0).addBox(-3.0F, -1.75F, 0.0F, 7.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 2.0F));

        PartDefinition tail = bodyBack.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 13).addBox(-2.0F, -4.0F, 0.0F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 6.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition bone = tail.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(38, 13).addBox(-1.5F, -3.0F, 0.0F, 4.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 5.0F, 0.6545F, 0.0F, 0.0F));

        PartDefinition bone2 = bone.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(20, 13).addBox(-1.5F, -3.0F, 0.0F, 4.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 5.0F, 0.5236F, 0.0F, 0.0F));

        PartDefinition bone3 = bone2.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(34, 22).addBox(-1.0F, -3.0F, 0.0F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 5.0F, 0.6545F, 0.0F, 0.0F));

        PartDefinition bone4 = bone3.addOrReplaceChild("bone4", CubeListBuilder.create().texOffs(18, 22).addBox(-1.0F, -3.0F, 0.0F, 3.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 4.0F, 0.6545F, 0.0F, 0.0F));

        PartDefinition bone5 = bone4.addOrReplaceChild("bone5", CubeListBuilder.create().texOffs(40, 42).addBox(-1.0F, -3.0F, 0.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(20, 48).addBox(0.0F, -2.0F, 3.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 5.0F, 0.2182F, 0.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -0.5F, -5.0F));

        PartDefinition headFront = head.addOrReplaceChild("headFront", CubeListBuilder.create().texOffs(0, 0).addBox(-4.5F, -3.5F, -7.0F, 10.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.5F, 0.0F));

        PartDefinition rightLegUp1 = body.addOrReplaceChild("rightLegUp1", CubeListBuilder.create().texOffs(0, 34).addBox(-6.0F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 1.0F, -3.5F, 0.0F, -0.5236F, 0.3491F));

        PartDefinition rightLegDown1 = rightLegUp1.addOrReplaceChild("rightLegDown1", CubeListBuilder.create().texOffs(0, 42).addBox(-7.0F, -1.0F, -0.5F, 7.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.5F, 0.0F, 0.0F, 0.0F, -0.0873F, -0.7854F));

        PartDefinition rightLegUp2 = body.addOrReplaceChild("rightLegUp2", CubeListBuilder.create().texOffs(64, 30).addBox(-6.0F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 1.0F, -1.5F, 0.0F, -0.2618F, 0.3491F));

        PartDefinition rightLegDown2 = rightLegUp2.addOrReplaceChild("rightLegDown2", CubeListBuilder.create().texOffs(64, 39).addBox(-7.0F, -1.0F, -0.5F, 7.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.5F, 0.0F, 0.0F, 0.0F, -0.0873F, -0.7854F));

        PartDefinition rightLegUp3 = body.addOrReplaceChild("rightLegUp3", CubeListBuilder.create().texOffs(48, 30).addBox(-6.0F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

        PartDefinition rightLegDown3 = rightLegUp3.addOrReplaceChild("rightLegDown3", CubeListBuilder.create().texOffs(48, 39).addBox(-7.0F, -1.0F, -0.5F, 7.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.5F, 0.0F, 0.0F, 0.0F, 0.1745F, -0.6981F));

        PartDefinition rightLegUp4 = body.addOrReplaceChild("rightLegUp4", CubeListBuilder.create().texOffs(32, 30).addBox(-6.0F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 1.0F, 1.5F, 0.0F, 0.6109F, 0.2618F));

        PartDefinition rightLegDown4 = rightLegUp4.addOrReplaceChild("rightLegDown4", CubeListBuilder.create().texOffs(32, 39).addBox(-7.0F, -1.0F, -0.5F, 7.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.5F, 0.0F, 0.0F, 0.0F, 0.4363F, -0.6981F));

        PartDefinition leftLegUp1 = body.addOrReplaceChild("leftLegUp1", CubeListBuilder.create().texOffs(16, 30).addBox(0.0F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 1.0F, -3.5F, 0.0F, 0.5236F, -0.3491F));

        PartDefinition leftLegDown1 = leftLegUp1.addOrReplaceChild("leftLegDown1", CubeListBuilder.create().texOffs(16, 39).addBox(0.0F, -1.0F, -0.5F, 7.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.5F, 0.0F, 0.0F, 0.0F, -0.0873F, 0.7854F));

        PartDefinition leftLegUp2 = body.addOrReplaceChild("leftLegUp2", CubeListBuilder.create().texOffs(0, 30).addBox(0.0F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 1.0F, -1.5F, 0.0F, 0.2618F, -0.3491F));

        PartDefinition leftLegDown2 = leftLegUp2.addOrReplaceChild("leftLegDown2", CubeListBuilder.create().texOffs(0, 39).addBox(0.0F, -1.0F, -0.5F, 7.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.5F, 0.0F, 0.0F, 0.0F, -0.0873F, 0.7854F));

        PartDefinition leftLegUp3 = body.addOrReplaceChild("leftLegUp3", CubeListBuilder.create().texOffs(48, 26).addBox(0.0F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 1.0F, 1.0F, 0.0F, 0.0F, -0.2618F));

        PartDefinition leftLegDown3 = leftLegUp3.addOrReplaceChild("leftLegDown3", CubeListBuilder.create().texOffs(60, 34).addBox(0.0F, -1.0F, -0.5F, 7.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.5F, 0.0F, 0.0F, 0.0F, -0.1745F, 0.6981F));

        PartDefinition leftLegUp4 = body.addOrReplaceChild("leftLegUp4", CubeListBuilder.create().texOffs(48, 22).addBox(0.0F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 1.0F, 1.5F, 0.0F, -0.6109F, -0.2618F));

        PartDefinition leftLegDown4 = leftLegUp4.addOrReplaceChild("leftLegDown4", CubeListBuilder.create().texOffs(44, 34).addBox(0.0F, -1.0F, -0.5F, 7.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.5F, 0.0F, 0.0F, 0.0F, -0.4363F, 0.6981F));

        PartDefinition shearsLeftBase = body.addOrReplaceChild("shearsLeftBase", CubeListBuilder.create().texOffs(28, 42).addBox(0.0F, -1.5F, -2.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 0.5F, -8.5F));

        PartDefinition leg1Down9 = shearsLeftBase.addOrReplaceChild("leg1Down9", CubeListBuilder.create().texOffs(64, 42).addBox(0.0F, -1.0F, -2.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 0.0F, 0.5F, 0.0F, 1.0472F, 0.0F));

        PartDefinition leg1Down10 = leg1Down9.addOrReplaceChild("leg1Down10", CubeListBuilder.create().texOffs(0, 22).addBox(0.0F, -2.0F, -2.5F, 5.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 0.0F, -0.5F, 0.0F, 0.829F, 0.0F));

        PartDefinition leg1Down11 = leg1Down10.addOrReplaceChild("leg1Down11", CubeListBuilder.create().texOffs(30, 34).addBox(-0.5F, -1.5F, -1.25F, 5.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, -0.5F, 0.0F, 0.0F, 0.4363F, 0.0F));

        PartDefinition leg1Down12 = leg1Down10.addOrReplaceChild("leg1Down12", CubeListBuilder.create().texOffs(10, 48).addBox(0.0F, -1.0F, -1.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -0.5F, -1.5F, 0.0F, 0.4363F, 0.0F));

        PartDefinition shearsRightBase = body.addOrReplaceChild("shearsRightBase", CubeListBuilder.create().texOffs(16, 42).addBox(-3.0F, -1.5F, -2.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 0.5F, -8.5F));

        PartDefinition leg1Down13 = shearsRightBase.addOrReplaceChild("leg1Down13", CubeListBuilder.create().texOffs(52, 42).addBox(-4.0F, -1.0F, -2.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 0.0F, 0.5F, 0.0F, -1.0472F, 0.0F));

        PartDefinition leg1Down14 = leg1Down13.addOrReplaceChild("leg1Down14", CubeListBuilder.create().texOffs(56, 13).addBox(-5.0F, -2.0F, -2.5F, 5.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 0.0F, -0.5F, 0.0F, -0.829F, 0.0F));

        PartDefinition leg1Down15 = leg1Down14.addOrReplaceChild("leg1Down15", CubeListBuilder.create().texOffs(16, 34).addBox(-5.0F, -1.5F, -1.5F, 5.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.75F, -0.5F, 0.25F, 0.0F, -0.4363F, 0.0F));

        PartDefinition leg1Down16 = leg1Down14.addOrReplaceChild("leg1Down16", CubeListBuilder.create().texOffs(0, 48).addBox(-4.0F, -1.0F, -1.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -0.5F, -1.5F, 0.0F, -0.4363F, 0.0F));

        PartDefinition riderPos = body.addOrReplaceChild("ridingPos", CubeListBuilder.create(), PartPose.offset(0.0F, -3.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.model.getMainPart().render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.model.resetPoses();
        float partialTicks = Minecraft.getInstance().getFrameTime();
        if (entity.deathTime <= 0 && !entity.playDeath()) {
            this.anim.doAnimation(this, "idle", entity.tickCount, partialTicks);
            if (entity.moveTick() > 0)
                this.anim.doAnimation(this, "walk", entity.tickCount, partialTicks, entity.interpolatedMoveTick(partialTicks));
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
                if (model instanceof SittingModel sittingModel)
                    sittingModel.translateSittingPosition(poseStack);
                else
                    poseStack.translate(0, 11 / 16d, 0);
                return true;
            }
        }
        return false;
    }
}