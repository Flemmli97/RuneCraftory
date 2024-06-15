package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.SittingModel;
import io.github.flemmli97.runecraftory.client.render.monster.RenderWooly;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityWooly;
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

public class ModelWooly<T extends EntityWooly> extends EntityModel<T> implements ExtendedModel, RideableModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(RuneCraftory.MODID, "wooly"), "main");

    protected final ModelPartHandler model;
    protected final BlockBenchAnimations anim;

    public ModelPartHandler.ModelPartExtended bodyCenter;
    public ModelPartHandler.ModelPartExtended head;
    public ModelPartHandler.ModelPartExtended body;
    public ModelPartHandler.ModelPartExtended bodyUp;
    public ModelPartHandler.ModelPartExtended armLeftBase;
    public ModelPartHandler.ModelPartExtended armRightBase;
    public ModelPartHandler.ModelPartExtended feetLeftBase;
    public ModelPartHandler.ModelPartExtended feetRightBase;
    public ModelPartHandler.ModelPartExtended ridingPosition;

    public ModelWooly(ModelPart root) {
        super();
        this.model = new ModelPartHandler(root.getChild("bodyCenter"), "bodyCenter");
        this.anim = AnimationManager.getInstance().getAnimation(new ResourceLocation(RuneCraftory.MODID, "wooly"));
        this.bodyCenter = this.model.getMainPart();
        this.head = this.model.getPart("head");
        this.body = this.model.getPart("body");
        this.bodyUp = this.model.getPart("bodyUp");
        this.armLeftBase = this.model.getPart("armLeftBase");
        this.armRightBase = this.model.getPart("armRightBase");
        this.feetLeftBase = this.model.getPart("feetLeftBase");
        this.feetRightBase = this.model.getPart("feetRightBase");
        this.ridingPosition = this.model.getPart("ridingPos");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bodyCenter = partdefinition.addOrReplaceChild("bodyCenter", CubeListBuilder.create(), PartPose.offset(0.0F, 17.75F, 0.0F));

        PartDefinition body = bodyCenter.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 31).mirror().addBox(-3.5F, -7.0F, -4.5F, 7.0F, 13.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(32, 30).mirror().addBox(-4.5F, -7.0F, -3.5F, 1.0F, 13.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(32, 30).addBox(3.5F, -7.0F, -3.5F, 1.0F, 13.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bodyUp = body.addOrReplaceChild("bodyUp", CubeListBuilder.create().texOffs(32, 14).mirror().addBox(-2.5F, -1.0F, -3.5F, 5.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(28, 23).addBox(2.5F, -1.0F, -2.5F, 1.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(28, 23).mirror().addBox(-3.5F, -1.0F, -2.5F, 1.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -8.0F, 0.0F));

        PartDefinition neck = bodyUp.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(0, 24).mirror().addBox(-2.5F, -1.0F, -2.5F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -1.5F, 0.0F));

        PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-3.5F, -7.0F, -4.5F, 7.0F, 7.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(32, 0).mirror().addBox(-4.5F, -7.0F, -3.0F, 1.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(32, 0).addBox(3.5F, -7.0F, -3.5F, 1.0F, 7.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 16).mirror().addBox(-3.5F, -8.0F, -3.5F, 7.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -1.0F, 0.0F));

        PartDefinition earLeft = head.addOrReplaceChild("earLeft", CubeListBuilder.create().texOffs(48, 0).mirror().addBox(0.0F, -0.5F, -1.0F, 5.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.0F, -4.0F, 0.0F, 0.0F, 0.0F, -0.3491F));

        PartDefinition earRight = head.addOrReplaceChild("earRight", CubeListBuilder.create().texOffs(48, 0).addBox(-5.0F, -0.5F, -1.0F, 5.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5F, -4.0F, 0.0F, 0.0F, 0.0F, 0.3491F));

        PartDefinition armLeftBase = body.addOrReplaceChild("armLeftBase", CubeListBuilder.create().texOffs(34, 50).addBox(0.25F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.75F, -3.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

        PartDefinition armLeftUp = armLeftBase.addOrReplaceChild("armLeftUp", CubeListBuilder.create().texOffs(54, 23).addBox(-0.25F, -0.5F, -4.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.0F, -0.5F, 0.1745F, 0.1745F, 0.0F));

        PartDefinition armLeftDown = armLeftUp.addOrReplaceChild("armLeftDown", CubeListBuilder.create().texOffs(54, 28).addBox(-1.0F, -0.5F, -4.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.75F, 0.0F, -4.0F, 0.4363F, 0.5236F, 0.0F));

        PartDefinition armRightBase = body.addOrReplaceChild("armRightBase", CubeListBuilder.create().texOffs(34, 50).addBox(-2.25F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.75F, -3.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

        PartDefinition armRightUp = armRightBase.addOrReplaceChild("armRightUp", CubeListBuilder.create().texOffs(54, 23).addBox(-0.75F, -0.5F, -4.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 0.0F, -0.5F, 0.1745F, -0.1745F, 0.0F));

        PartDefinition armRightDown = armRightUp.addOrReplaceChild("armRightDown", CubeListBuilder.create().texOffs(54, 28).mirror().addBox(0.0F, -0.5F, -4.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.75F, 0.0F, -4.0F, 0.4363F, -0.5236F, 0.0F));

        PartDefinition feetLeftBase = body.addOrReplaceChild("feetLeftBase", CubeListBuilder.create().texOffs(42, 50).addBox(-1.5F, -5.5F, -2.5F, 3.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 4.75F, 0.0F));

        PartDefinition feetLeft = feetLeftBase.addOrReplaceChild("feetLeft", CubeListBuilder.create().texOffs(16, 53).addBox(-1.0F, 0.0F, -7.0F, 2.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.5F, 0.0F));

        PartDefinition feetRightBase = body.addOrReplaceChild("feetRightBase", CubeListBuilder.create().texOffs(42, 50).mirror().addBox(-1.5F, -5.5F, -2.5F, 3.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-4.0F, 4.75F, 0.0F));

        PartDefinition feetRight = feetRightBase.addOrReplaceChild("feetRight", CubeListBuilder.create().texOffs(16, 53).addBox(-1.0F, 0.0F, -7.0F, 2.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.5F, 0.0F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(52, 3).mirror().addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 3.0F, 4.0F));

        PartDefinition ridingPos = body.addOrReplaceChild("ridingPos", CubeListBuilder.create(), PartPose.offset(0.0F, -7.0F, 6.0F));

        return LayerDefinition.create(meshdefinition, 64, 62);
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
        if (ridingEntityRenderer instanceof LivingEntityRenderer<?, ?> lR && entityRenderer instanceof RenderWooly<T> scaledRender) {
            EntityModel<?> model = lR.getModel();
            if (model instanceof HumanoidModel<?> || model instanceof IllagerModel<?> || model instanceof SittingModel) {
                this.bodyCenter.translateAndRotate(poseStack);
                this.body.translateAndRotate(poseStack);
                this.ridingPosition.translateAndRotate(poseStack);
                poseStack.scale(1 / scaledRender.scale, 1 / scaledRender.scale, 1 / scaledRender.scale);
                if (model instanceof SittingModel sittingModel)
                    sittingModel.translateSittingPosition(poseStack);
                else
                    poseStack.translate(0, 11 / 16d, 0);
                poseStack.scale(scaledRender.scale, scaledRender.scale, scaledRender.scale);
                return true;
            }
        }
        return false;
    }
}