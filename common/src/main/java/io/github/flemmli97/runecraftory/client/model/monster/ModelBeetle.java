package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.client.model.SittingModel;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityBeetle;
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

public class ModelBeetle<T extends EntityBeetle> extends EntityModel<T> implements ExtendedModel, RideableModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(RuneCraftory.MODID, "beetle"), "main");

    protected final ModelPartHandler model;
    protected final BlockBenchAnimations anim;

    public ModelPartHandler.ModelPartExtended body;
    public ModelPartHandler.ModelPartExtended head;
    public ModelPartHandler.ModelPartExtended ridingPosition;

    public ModelBeetle(ModelPart root) {
        super();
        this.model = new ModelPartHandler(root, "root");
        this.anim = AnimationManager.getInstance().getAnimation(new ResourceLocation(RuneCraftory.MODID, "beetle"));
        this.head = this.model.getPart("head");
        this.body = this.model.getPart("body");
        this.ridingPosition = this.model.getPart("ridingPos");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -5.0F, -7.0F, 10.0F, 7.0F, 15.0F, new CubeDeformation(0.0F))
                .texOffs(50, 0).addBox(-4.5F, 2.0F, -7.0F, 9.0F, 2.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.0F, 3.0F, -0.6981F, 0.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(26, 44).addBox(-2.0F, -3.1F, -5.0F, 4.0F, 6.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(10, 73).addBox(-2.0F, -2.1F, -6.0F, 4.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(32, 80).addBox(-1.0F, -1.1F, -7.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, -7.0F, 0.5236F, 0.0F, 0.0F));

        PartDefinition chin = head.addOrReplaceChild("chin", CubeListBuilder.create().texOffs(0, 80).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.9F, -7.0F, 0.3927F, 0.0F, 0.0F));

        PartDefinition headLeft = head.addOrReplaceChild("headLeft", CubeListBuilder.create().texOffs(44, 44).addBox(-2.0F, -3.1F, -5.0F, 2.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(60, 44).addBox(-3.0F, -4.1F, -2.0F, 2.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 0.0F, 0.0F, 0.0F, 0.4363F, 0.0F));

        PartDefinition headRight = head.addOrReplaceChild("headRight", CubeListBuilder.create().texOffs(44, 44).mirror().addBox(0.0F, -3.1F, -5.0F, 2.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(60, 44).mirror().addBox(1.0F, -4.1F, -2.0F, 2.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.0F, 0.0F, 0.0F, 0.0F, -0.4363F, 0.0F));

        PartDefinition headUp = head.addOrReplaceChild("headUp", CubeListBuilder.create().texOffs(0, 73).addBox(-1.5F, -2.0F, -1.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(54, 64).addBox(-2.0F, -4.0F, 0.0F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(16, 64).addBox(-2.5F, -5.0F, 1.0F, 5.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 64).addBox(-2.5F, -6.0F, 3.0F, 5.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.1F, -4.0F, -0.6109F, 0.0F, 0.0F));

        PartDefinition hornFront = headUp.addOrReplaceChild("hornFront", CubeListBuilder.create().texOffs(16, 80).addBox(-0.5F, -2.0F, -3.25F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, -0.75F, -0.0873F, 0.0F, 0.0F));

        PartDefinition hornFrontTip2 = hornFront.addOrReplaceChild("hornFrontTip2", CubeListBuilder.create().texOffs(36, 73).addBox(-0.5F, -2.0F, -4.25F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -3.0F, -0.2618F, 0.0F, 0.0F));

        PartDefinition hornBackBase = body.addOrReplaceChild("hornBackBase", CubeListBuilder.create().texOffs(62, 73).addBox(-1.0F, -4.5F, -1.5F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.5F, -5.5F, 0.5236F, 0.0F, 0.0F));

        PartDefinition hornBack = hornBackBase.addOrReplaceChild("hornBack", CubeListBuilder.create().texOffs(10, 80).addBox(-0.5F, -5.25F, -2.0F, 1.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.25F, 0.5F, 0.5236F, 0.0F, 0.0F));

        PartDefinition hornBackTip = hornBack.addOrReplaceChild("hornBackTip", CubeListBuilder.create().texOffs(38, 80).addBox(-0.5F, -5.25F, -1.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.0F, -1.0F, 0.6109F, 0.0F, 0.0F));

        PartDefinition elytronLeft = body.addOrReplaceChild("elytronLeft", CubeListBuilder.create().texOffs(0, 22).addBox(-3.0F, -1.0F, 0.0F, 5.0F, 1.0F, 15.0F, new CubeDeformation(0.0F))
                .texOffs(40, 22).addBox(2.0F, 0.0F, 0.0F, 1.0F, 7.0F, 15.0F, new CubeDeformation(0.0F))
                .texOffs(42, 64).addBox(-3.0F, -0.01F, 15.0F, 5.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -5.0F, -7.0F));

        PartDefinition elytronRight = body.addOrReplaceChild("elytronRight", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-2.0F, -1.0F, 0.0F, 5.0F, 1.0F, 15.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(40, 22).mirror().addBox(-3.0F, 0.0F, 0.0F, 1.0F, 7.0F, 15.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(42, 64).mirror().addBox(-2.0F, -0.01F, 15.0F, 5.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.0F, -5.0F, -7.0F));

        PartDefinition wingLeft = body.addOrReplaceChild("wingLeft", CubeListBuilder.create().texOffs(72, 22).addBox(-1.0F, 0.0F, -1.0F, 5.0F, 0.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -5.0F, -4.0F));

        PartDefinition wingLeftSide = wingLeft.addOrReplaceChild("wingLeftSide", CubeListBuilder.create().texOffs(0, 44).mirror().addBox(0.0F, 0.0F, -6.0F, 0.0F, 7.0F, 13.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(4.0F, 0.0F, 5.0F));

        PartDefinition wingLeftBack = wingLeftSide.addOrReplaceChild("wingLeftBack", CubeListBuilder.create().texOffs(66, 64).addBox(-5.0F, -3.0F, 0.0F, 5.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, 7.0F));

        PartDefinition wingRight = body.addOrReplaceChild("wingRight", CubeListBuilder.create().texOffs(72, 22).mirror().addBox(-4.0F, 0.0F, -1.0F, 5.0F, 0.0F, 13.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.0F, -5.0F, -4.0F));

        PartDefinition wingRightSide = wingRight.addOrReplaceChild("wingRightSide", CubeListBuilder.create().texOffs(0, 44).addBox(0.0F, 0.0F, -6.0F, 0.0F, 7.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 0.0F, 5.0F));

        PartDefinition wingRightBack = wingRightSide.addOrReplaceChild("wingRightBack", CubeListBuilder.create().texOffs(66, 64).mirror().addBox(0.0F, -3.0F, 0.0F, 5.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 3.0F, 7.0F));

        PartDefinition legLeft = body.addOrReplaceChild("legLeft", CubeListBuilder.create().texOffs(30, 64).addBox(0.0F, 0.0F, -1.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 4.0F, 5.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition legLeftMiddle = legLeft.addOrReplaceChild("legLeftMiddle", CubeListBuilder.create().texOffs(54, 73).addBox(-0.5F, 0.0F, 0.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 5.0F, 0.0F, 1.1345F, 0.0F, 0.0F));

        PartDefinition legLeftDown = legLeftMiddle.addOrReplaceChild("legLeftDown", CubeListBuilder.create().texOffs(46, 73).addBox(-0.5F, 0.0F, -1.5F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, 1.5F, -0.7854F, 0.0F, 0.0F));

        PartDefinition legLeftDown2 = legLeftDown.addOrReplaceChild("legLeftDown2", CubeListBuilder.create().texOffs(24, 80).addBox(-0.5F, -1.0F, -3.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.0F, 0.4363F, 0.0F));

        PartDefinition legLeftDown3 = legLeftDown.addOrReplaceChild("legLeftDown3", CubeListBuilder.create().texOffs(24, 80).addBox(-0.5F, -1.0F, -3.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 4.0F, 0.0F, 0.0F, -0.4363F, 0.0F));

        PartDefinition legRight = body.addOrReplaceChild("legRight", CubeListBuilder.create().texOffs(30, 64).mirror().addBox(-3.0F, 0.0F, -1.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.0F, 4.0F, 5.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition legRightMiddle = legRight.addOrReplaceChild("legRightMiddle", CubeListBuilder.create().texOffs(54, 73).mirror().addBox(-1.5F, 0.0F, 0.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.0F, 5.0F, 0.0F, 1.1345F, 0.0F, 0.0F));

        PartDefinition legRightDown = legRightMiddle.addOrReplaceChild("legRightDown", CubeListBuilder.create().texOffs(46, 73).mirror().addBox(-1.5F, 0.0F, -1.5F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 4.0F, 1.5F, -0.7854F, 0.0F, 0.0F));

        PartDefinition legRightDown2 = legRightDown.addOrReplaceChild("legRightDown2", CubeListBuilder.create().texOffs(24, 80).mirror().addBox(-0.5F, -1.0F, -3.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.0F, 4.0F, 0.0F, 0.0F, 0.4363F, 0.0F));

        PartDefinition legRightDown3 = legRightDown.addOrReplaceChild("legRightDown3", CubeListBuilder.create().texOffs(24, 80).mirror().addBox(-0.5F, -1.0F, -3.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.0F, -0.4363F, 0.0F));

        PartDefinition armLeft = body.addOrReplaceChild("armLeft", CubeListBuilder.create().texOffs(28, 73).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 4.0F, -6.0F, 0.0F, 0.0F, -0.9599F));

        PartDefinition armLeftMiddle = armLeft.addOrReplaceChild("armLeftMiddle", CubeListBuilder.create().texOffs(42, 83).addBox(-1.5F, 0.0F, 0.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 87).addBox(-1.0F, 2.0F, 0.5F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(52, 82).addBox(-0.5F, 2.0F, 0.0F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 5.0F, 0.5F, 0.0873F, 0.0F, 0.7854F));

        PartDefinition leftClaw = armLeftMiddle.addOrReplaceChild("leftClaw", CubeListBuilder.create().texOffs(52, 80).addBox(-0.5F, 0.25F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 4.75F, 0.5F, 0.0F, 0.0F, 0.4363F));

        PartDefinition armRight = body.addOrReplaceChild("armRight", CubeListBuilder.create().texOffs(28, 73).mirror().addBox(-1.0F, 0.0F, 0.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.5F, 4.0F, -6.0F, 0.0F, 0.0F, 0.9599F));

        PartDefinition armRightMiddle = armRight.addOrReplaceChild("armRightMiddle", CubeListBuilder.create().texOffs(42, 83).mirror().addBox(-0.5F, 0.0F, 0.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 87).addBox(0.0F, 2.0F, 0.5F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(52, 82).addBox(0.5F, 2.0F, 0.0F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 5.0F, 0.5F, 0.0873F, 0.0F, -0.7854F));

        PartDefinition rightClaw = armRightMiddle.addOrReplaceChild("rightClaw", CubeListBuilder.create().texOffs(52, 80).mirror().addBox(-0.5F, 0.25F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.5F, 4.75F, 0.5F, 0.0F, 0.0F, -0.4363F));

        PartDefinition armLeft2 = body.addOrReplaceChild("armLeft2", CubeListBuilder.create().texOffs(20, 73).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.5F, 4.0F, -1.0F, 0.1745F, 0.0F, -0.4363F));

        PartDefinition armLeft2Middle = armLeft2.addOrReplaceChild("armLeft2Middle", CubeListBuilder.create().texOffs(42, 80).addBox(-1.5F, 0.0F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(56, 80).addBox(-1.0F, 2.0F, -0.5F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(48, 82).addBox(-0.5F, 2.0F, -1.0F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 5.0F, 1.5F, -0.3491F, 0.0F, 0.7854F));

        PartDefinition leftClaw2 = armLeft2Middle.addOrReplaceChild("leftClaw2", CubeListBuilder.create().texOffs(48, 80).addBox(-0.5F, 0.25F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 4.75F, -0.5F));

        PartDefinition armRight2 = body.addOrReplaceChild("armRight2", CubeListBuilder.create().texOffs(20, 73).mirror().addBox(-1.0F, 0.0F, 0.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.5F, 4.0F, -1.0F, 0.1745F, 0.0F, 0.4363F));

        PartDefinition armRight2Middle = armRight2.addOrReplaceChild("armRight2Middle", CubeListBuilder.create().texOffs(42, 80).mirror().addBox(-0.5F, 0.0F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(56, 80).addBox(0.0F, 2.0F, -0.5F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(48, 82).addBox(0.5F, 2.0F, -1.0F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 5.0F, 1.5F, -0.3491F, 0.0F, -0.7854F));

        PartDefinition rightClaw2 = armRight2Middle.addOrReplaceChild("rightClaw2", CubeListBuilder.create().texOffs(48, 80).addBox(-0.5F, 0.25F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 4.75F, -0.5F));

        PartDefinition ridingPos = body.addOrReplaceChild("ridingPos", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -6.0F, 6.0F, 1.0472F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.model.getMainPart().render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.model.resetPoses();
        this.head.yRot += (netHeadYaw % 360) * Mth.DEG_TO_RAD * 0.5f;
        this.head.xRot += headPitch * Mth.DEG_TO_RAD * 0.5f;
        float partialTicks = Minecraft.getInstance().getFrameTime();
        if (entity.deathTime <= 0 && !entity.playDeath()) {
            this.anim.doAnimation(this, "idle", entity.tickCount, partialTicks);
            if (!entity.isOnGround())
                this.anim.doAnimation(this, "fly", entity.tickCount, partialTicks);
            else if (entity.moveTick() > 0)
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
                ClientHandlers.translateRider(entityRenderer, rider, model, poseStack);
                return true;
            }
        }
        return false;
    }
}