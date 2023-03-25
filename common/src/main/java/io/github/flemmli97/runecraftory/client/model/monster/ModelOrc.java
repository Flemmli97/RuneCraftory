package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.SittingModel;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityOrc;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.client.AnimationManager;
import io.github.flemmli97.tenshilib.client.model.BlockBenchAnimations;
import io.github.flemmli97.tenshilib.client.model.ExtendedModel;
import io.github.flemmli97.tenshilib.client.model.IItemArmModel;
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
import net.minecraft.world.entity.HumanoidArm;

public class ModelOrc<T extends EntityOrc> extends EntityModel<T> implements ExtendedModel, RideableModel<T>, IItemArmModel, SittingModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(RuneCraftory.MODID, "orc"), "main");

    protected final ModelPartHandler model;
    protected final BlockBenchAnimations anim;

    public ModelPartHandler.ModelPartExtended body;
    public ModelPartHandler.ModelPartExtended head;
    public ModelPartHandler.ModelPartExtended handLeftUp;
    public ModelPartHandler.ModelPartExtended handLeftDown;
    public ModelPartHandler.ModelPartExtended handRightUp;
    public ModelPartHandler.ModelPartExtended handRightDown;

    public ModelOrc(ModelPart root) {
        super();
        this.model = new ModelPartHandler(root, "root");
        this.anim = AnimationManager.getInstance().getAnimation(new ResourceLocation(RuneCraftory.MODID, "orc"));
        this.body = this.model.getPart("body");
        this.head = this.model.getPart("head");
        this.handLeftUp = this.model.getPart("handLeftUp");
        this.handLeftDown = this.model.getPart("handLeftDown");
        this.handRightUp = this.model.getPart("handRightUp");
        this.handRightDown = this.model.getPart("handRightDown");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(34, 0).addBox(-5.0F, -11.0F, -4.0F, 10.0F, 18.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-5.0F, -11.0F, -4.0F, 10.0F, 18.0F, 7.0F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(0.5F, 6.5F, 1.0F, 0.1745F, 0.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 25).addBox(-4.5F, -9.0F, -4.5F, 9.0F, 9.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.0F, -0.5F));

        PartDefinition earLeft = head.addOrReplaceChild("earLeft", CubeListBuilder.create().texOffs(8, 64).mirror().addBox(-1.5F, -3.0F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, -9.0F, -2.5F, 0.0873F, 0.0F, 0.0F));

        PartDefinition earRight = head.addOrReplaceChild("earRight", CubeListBuilder.create().texOffs(8, 64).addBox(-1.5F, -3.0F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -9.0F, -2.5F, 0.0873F, 0.0F, 0.0F));

        PartDefinition headFront = head.addOrReplaceChild("headFront", CubeListBuilder.create().texOffs(32, 54).addBox(-2.75F, -2.25F, -4.0F, 5.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.25F, -1.75F, -2.5F));

        PartDefinition handLeftUp = body.addOrReplaceChild("handLeftUp", CubeListBuilder.create().texOffs(16, 43).addBox(0.0F, -2.0F, -3.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, -4.0F, 0.5F, -1.0472F, -0.5236F, -0.0873F));

        PartDefinition handLeftDown = handLeftUp.addOrReplaceChild("handLeftDown", CubeListBuilder.create().texOffs(52, 25).addBox(-4.0F, 0.0F, -4.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 5.0F, 1.0F, -0.4363F, 0.2618F, 0.2618F));

        PartDefinition leftItem = handLeftDown.addOrReplaceChild("leftItem", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.5F, 9.0F, -3.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition handRightUp = body.addOrReplaceChild("handRightUp", CubeListBuilder.create().texOffs(0, 43).addBox(-4.0F, -2.0F, -3.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, -4.0F, 0.5F, -1.0472F, 0.5236F, 0.0873F));

        PartDefinition handRightDown = handRightUp.addOrReplaceChild("handRightDown", CubeListBuilder.create().texOffs(36, 25).addBox(0.0F, 0.0F, -4.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 5.0F, 1.0F, -0.4363F, -0.2618F, -0.2618F));

        PartDefinition rightItem = handRightDown.addOrReplaceChild("rightItem", CubeListBuilder.create(), PartPose.offsetAndRotation(2.5F, 9.0F, -3.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition legLeftUp = body.addOrReplaceChild("legLeftUp", CubeListBuilder.create().texOffs(16, 54).addBox(-2.0F, 0.0F, -3.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 64).addBox(-2.0F, 2.0F, -3.25F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 7.0F, 0.5F, -0.5236F, 0.0F, 0.0F));

        PartDefinition legLeftDown = legLeftUp.addOrReplaceChild("legLeftDown", CubeListBuilder.create().texOffs(0, 54).addBox(-2.6F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.6F, 6.0F, -3.0F, 0.3491F, 0.0F, 0.0F));

        PartDefinition legRightUp = body.addOrReplaceChild("legRightUp", CubeListBuilder.create().texOffs(48, 43).addBox(-1.8F, 0.0F, -3.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(32, 60).addBox(-1.8F, 2.0F, -3.25F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.7F, 7.0F, 0.5F, -0.5236F, 0.0F, 0.0F));

        PartDefinition legRightDown = legRightUp.addOrReplaceChild("legRightDown", CubeListBuilder.create().texOffs(32, 43).addBox(-1.4F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4F, 6.0F, -3.0F, 0.3491F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.model.getMainPart().render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
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
                this.anim.doAnimation(this, "walk", entity.tickCount, partialTicks, entity.interpolatedMoveTick(partialTicks));
        }
        if (this.riding)
            this.anim.doAnimation(this, "sit", entity.tickCount, partialTicks);
        if (anim != null)
            this.anim.doAnimation(this, anim.getAnimationClient(), anim.getTick(), partialTicks, entity.getAnimationHandler().getInterpolatedAnimationVal(partialTicks));
    }

    @Override
    public ModelPartHandler getHandler() {
        return this.model;
    }

    @Override
    public void transform(HumanoidArm humanoidArm, PoseStack poseStack) {
        this.body.translateAndRotate(poseStack);
        if (humanoidArm == HumanoidArm.LEFT) {
            this.handLeftUp.translateAndRotate(poseStack);
            this.handLeftDown.translateAndRotate(poseStack);
            poseStack.translate(-2 / 16d, 8 / 16d, -4 / 16d);
        } else {
            this.handRightUp.translateAndRotate(poseStack);
            this.handRightDown.translateAndRotate(poseStack);
            poseStack.translate(2 / 16d, 8 / 16d, -4 / 16d);
        }
    }

    @Override
    public void postTransform(boolean leftSide, PoseStack stack) {
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
                    poseStack.translate(0, -1 / 16d, 6 / 16d);
                return true;
            }
        }
        return false;
    }

    @Override
    public void translateSittingPosition(PoseStack stack) {
        stack.translate(0, 2 / 16d, 6 / 16d);
    }
}