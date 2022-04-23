package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityBeetle;
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

public class ModelBeetle<T extends EntityBeetle> extends EntityModel<T> implements ExtendedModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(RuneCraftory.MODID, "beetle"), "main");

    protected final ModelPartHandler model;
    protected final BlockBenchAnimations anim;

    public ModelPartHandler.ModelPartExtended head;

    public ModelBeetle(ModelPart root) {
        super();
        this.model = new ModelPartHandler(root, "body");
        this.anim = AnimationManager.getInstance().getAnimation(new ResourceLocation(RuneCraftory.MODID, "beetle"));
        this.head = this.model.getPart("head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -5.0F, -7.0F, 10.0F, 7.0F, 15.0F, new CubeDeformation(0.0F))
                .texOffs(0, 22).addBox(-4.5F, 2.0F, -7.0F, 9.0F, 2.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.0F, 3.0F, -0.6981F, 0.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(110, 12).addBox(-2.0F, -3.1F, -5.0F, 4.0F, 6.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(96, 24).addBox(-2.0F, -2.1F, -6.0F, 4.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(106, 24).addBox(-1.0F, -1.1F, -7.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, -7.0F, 0.5236F, 0.0F, 0.0F));

        PartDefinition chin = head.addOrReplaceChild("chin", CubeListBuilder.create().texOffs(112, 23).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.9F, -6.0F, -0.3491F, 0.0F, 0.0F));

        PartDefinition headLeft = head.addOrReplaceChild("headLeft", CubeListBuilder.create().texOffs(106, 0).addBox(-2.0F, -3.1F, -5.0F, 2.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(96, 12).addBox(-3.0F, -4.1F, -2.0F, 2.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 0.0F, 0.0F, 0.0F, 0.4363F, 0.0F));

        PartDefinition headRight = head.addOrReplaceChild("headRight", CubeListBuilder.create().texOffs(106, 0).mirror().addBox(0.0F, -3.1F, -5.0F, 2.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(96, 12).mirror().addBox(1.0F, -4.1F, -2.0F, 2.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.0F, 0.0F, 0.0F, 0.0F, -0.4363F, 0.0F));

        PartDefinition headUp = head.addOrReplaceChild("headUp", CubeListBuilder.create().texOffs(84, 30).addBox(-1.5F, -2.0F, -1.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(94, 30).addBox(-2.0F, -4.0F, 0.0F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(84, 36).addBox(-2.5F, -5.0F, 1.0F, 5.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(98, 36).addBox(-2.5F, -6.0F, 3.0F, 5.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.1F, -4.0F, -0.6109F, 0.0F, 0.0F));

        PartDefinition hornFront = headUp.addOrReplaceChild("hornFront", CubeListBuilder.create().texOffs(112, 25).addBox(-0.5F, -2.0F, -3.25F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, -0.75F, -0.0873F, 0.0F, 0.0F));

        PartDefinition hornFrontTip2 = hornFront.addOrReplaceChild("hornFrontTip2", CubeListBuilder.create().texOffs(112, 30).addBox(-0.5F, -2.0F, -4.25F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -3.0F, -0.2618F, 0.0F, 0.0F));

        PartDefinition hornBackBase = body.addOrReplaceChild("hornBackBase", CubeListBuilder.create().texOffs(82, 18).addBox(-1.0F, -4.5F, -1.5F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.5F, -5.5F, 0.5236F, 0.0F, 0.0F));

        PartDefinition hornBack = hornBackBase.addOrReplaceChild("hornBack", CubeListBuilder.create().texOffs(90, 18).addBox(-0.5F, -5.25F, -2.0F, 1.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.25F, 0.5F, 0.5236F, 0.0F, 0.0F));

        PartDefinition hornBackTip = hornBack.addOrReplaceChild("hornBackTip", CubeListBuilder.create().texOffs(84, 24).addBox(-0.5F, -5.25F, -1.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.0F, -1.0F, 0.6109F, 0.0F, 0.0F));

        PartDefinition elytronLeft = body.addOrReplaceChild("elytronLeft", CubeListBuilder.create().texOffs(0, 40).addBox(-3.0F, -1.0F, 0.0F, 5.0F, 1.0F, 15.0F, new CubeDeformation(0.0F))
                .texOffs(50, 0).addBox(2.0F, 0.0F, 0.0F, 1.0F, 7.0F, 15.0F, new CubeDeformation(0.0F))
                .texOffs(48, 22).addBox(-3.0F, -0.01F, 15.0F, 5.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -5.0F, -7.0F));

        PartDefinition elytronRight = body.addOrReplaceChild("elytronRight", CubeListBuilder.create().texOffs(0, 40).mirror().addBox(-2.0F, -1.0F, 0.0F, 5.0F, 1.0F, 15.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(50, 0).mirror().addBox(-3.0F, 0.0F, 0.0F, 1.0F, 7.0F, 15.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(48, 22).mirror().addBox(-2.0F, -0.01F, 15.0F, 5.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.0F, -5.0F, -7.0F));

        PartDefinition wingLeft = body.addOrReplaceChild("wingLeft", CubeListBuilder.create().texOffs(48, 30).addBox(-1.0F, 0.0F, -1.0F, 5.0F, 0.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -5.0F, -4.0F));

        PartDefinition wingLeftSide = wingLeft.addOrReplaceChild("wingLeftSide", CubeListBuilder.create().texOffs(40, 43).mirror().addBox(0.0F, 0.0F, -6.0F, 0.0F, 7.0F, 13.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(4.0F, 0.0F, 5.0F));

        PartDefinition wingLeftBack = wingLeftSide.addOrReplaceChild("wingLeftBack", CubeListBuilder.create().texOffs(67, 43).addBox(-5.0F, -3.0F, 0.0F, 5.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, 7.0F));

        PartDefinition wingRight = body.addOrReplaceChild("wingRight", CubeListBuilder.create().texOffs(48, 30).mirror().addBox(-4.0F, 0.0F, -1.0F, 5.0F, 0.0F, 13.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.0F, -5.0F, -4.0F));

        PartDefinition wingRightSide = wingRight.addOrReplaceChild("wingRightSide", CubeListBuilder.create().texOffs(40, 43).addBox(0.0F, 0.0F, -6.0F, 0.0F, 7.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 0.0F, 5.0F));

        PartDefinition wingRightBack = wingRightSide.addOrReplaceChild("wingRightBack", CubeListBuilder.create().texOffs(67, 43).mirror().addBox(0.0F, -3.0F, 0.0F, 5.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 3.0F, 7.0F));

        PartDefinition legLeft = body.addOrReplaceChild("legLeft", CubeListBuilder.create().texOffs(82, 0).addBox(0.0F, 0.0F, 0.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 4.0F, 5.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition legLeftMiddle = legLeft.addOrReplaceChild("legLeftMiddle", CubeListBuilder.create().texOffs(82, 7).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 5.0F, 0.0F, 1.1345F, 0.0F, 0.0F));

        PartDefinition legLeftDown = legLeftMiddle.addOrReplaceChild("legLeftDown", CubeListBuilder.create().texOffs(82, 13).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, 1.5F, -0.7854F, 0.0F, 0.0F));

        PartDefinition legRight = body.addOrReplaceChild("legRight", CubeListBuilder.create().texOffs(82, 0).mirror().addBox(-2.0F, 0.0F, 0.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.0F, 4.0F, 5.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition legRightMiddle = legRight.addOrReplaceChild("legRightMiddle", CubeListBuilder.create().texOffs(82, 7).mirror().addBox(-0.5F, 0.0F, 0.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.0F, 5.0F, 0.0F, 1.1345F, 0.0F, 0.0F));

        PartDefinition legRightDown = legRightMiddle.addOrReplaceChild("legRightDown", CubeListBuilder.create().texOffs(82, 13).mirror().addBox(-0.5F, 0.0F, -1.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 4.0F, 1.5F, -0.7854F, 0.0F, 0.0F));

        PartDefinition armLeft = body.addOrReplaceChild("armLeft", CubeListBuilder.create().texOffs(90, 0).addBox(0.0F, 0.0F, 0.0F, 1.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 4.0F, -6.0F, 0.0F, 0.0F, -0.9599F));

        PartDefinition armLeftMiddle = armLeft.addOrReplaceChild("armLeftMiddle", CubeListBuilder.create().texOffs(90, 7).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(96, 0).addBox(-0.5F, 2.0F, 0.5F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(96, 3).addBox(0.0F, 2.0F, 0.0F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 5.0F, 0.5F, 0.0873F, 0.0F, 0.7854F));

        PartDefinition leftClaw = armLeftMiddle.addOrReplaceChild("leftClaw", CubeListBuilder.create().texOffs(94, 7).addBox(-0.5F, 0.25F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.75F, 0.5F, 0.0F, 0.0F, 0.4363F));

        PartDefinition armRight = body.addOrReplaceChild("armRight", CubeListBuilder.create().texOffs(90, 0).mirror().addBox(-1.0F, 0.0F, 0.0F, 1.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.0F, 4.0F, -6.0F, 0.0F, 0.0F, 0.9599F));

        PartDefinition armRightMiddle = armRight.addOrReplaceChild("armRightMiddle", CubeListBuilder.create().texOffs(90, 7).mirror().addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(96, 0).addBox(-0.5F, 2.0F, 0.5F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(96, 3).addBox(0.0F, 2.0F, 0.0F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 5.0F, 0.5F, 0.0873F, 0.0F, -0.7854F));

        PartDefinition rightClaw = armRightMiddle.addOrReplaceChild("rightClaw", CubeListBuilder.create().texOffs(94, 7).mirror().addBox(-0.5F, 0.25F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 4.75F, 0.5F, 0.0F, 0.0F, -0.4363F));

        PartDefinition armLeft2 = body.addOrReplaceChild("armLeft2", CubeListBuilder.create().texOffs(98, 0).addBox(0.0F, 0.0F, 0.0F, 1.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 4.0F, -1.0F, 0.1745F, 0.0F, -0.4363F));

        PartDefinition armLeft2Middle = armLeft2.addOrReplaceChild("armLeft2Middle", CubeListBuilder.create().texOffs(98, 7).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(104, 0).addBox(-0.5F, 2.0F, -0.5F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(104, 5).addBox(0.0F, 2.0F, -1.0F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 5.0F, 1.5F, -0.3491F, 0.0F, 0.7854F));

        PartDefinition leftClaw2 = armLeft2Middle.addOrReplaceChild("leftClaw2", CubeListBuilder.create().texOffs(102, 7).addBox(-0.5F, 0.25F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.75F, -0.5F));

        PartDefinition armRight2 = body.addOrReplaceChild("armRight2", CubeListBuilder.create().texOffs(98, 0).mirror().addBox(-1.0F, 0.0F, 0.0F, 1.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, 4.0F, -1.0F, 0.1745F, 0.0F, 0.4363F));

        PartDefinition armRight2Middle = armRight2.addOrReplaceChild("armRight2Middle", CubeListBuilder.create().texOffs(98, 7).mirror().addBox(-0.5F, 0.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(104, 0).addBox(-0.5F, 2.0F, -0.5F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(104, 5).addBox(0.0F, 2.0F, -1.0F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 5.0F, 1.5F, -0.3491F, 0.0F, -0.7854F));

        PartDefinition rightClaw2 = armRight2Middle.addOrReplaceChild("rightClaw2", CubeListBuilder.create().texOffs(102, 7).addBox(-0.5F, 0.25F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.75F, -0.5F));

        return LayerDefinition.create(meshdefinition, 128, 62);
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
        this.anim.doAnimation(this, "iddle", entity.tickCount, partialTicks);
        if (!entity.isOnGround())
            this.anim.doAnimation(this, "fly", entity.tickCount, partialTicks);
        else if (entity.isMoving())
            this.anim.doAnimation(this, "walk", entity.tickCount, partialTicks);
        if (anim != null)
            this.anim.doAnimation(this, anim.getAnimationClient(), anim.getTick(), partialTicks);
    }

    @Override
    public ModelPartHandler getHandler() {
        return this.model;
    }
}