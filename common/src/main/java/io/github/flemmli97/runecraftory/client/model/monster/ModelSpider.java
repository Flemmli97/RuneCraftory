package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.client.model.SittingModel;
import io.github.flemmli97.runecraftory.common.entities.monster.EntitySpider;
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

public class ModelSpider<T extends EntitySpider> extends EntityModel<T> implements ExtendedModel, RideableModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(RuneCraftory.MODID, "spider"), "main");

    protected final ModelPartHandler model;
    protected final BlockBenchAnimations anim;

    public ModelPartHandler.ModelPartExtended body;
    public ModelPartHandler.ModelPartExtended head;
    public ModelPartHandler.ModelPartExtended ridingPosition;

    public ModelSpider(ModelPart root) {
        super();
        this.model = new ModelPartHandler(root, "root");
        this.anim = AnimationManager.getInstance().getAnimation(new ResourceLocation(RuneCraftory.MODID, "spider"));
        this.body = this.model.getPart("body");
        this.head = this.model.getPart("head");
        this.ridingPosition = this.model.getPart("ridingPos");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 28).addBox(-3.5F, -2.5F, -3.5F, 7.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 17.0F, -3.5F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(27, 21).addBox(-4.0F, -2.5F, -7.0F, 8.0F, 7.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 17).addBox(-2.25F, 0.0F, -7.25F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 14).addBox(0.25F, 0.0F, -7.25F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 3).addBox(1.25F, 3.0F, -7.75F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-2.75F, 3.0F, -7.75F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, -2.5F));

        PartDefinition abdomen = body.addOrReplaceChild("abdomen", CubeListBuilder.create().texOffs(0, 14).addBox(-5.5F, -4.0F, 0.0F, 11.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 2.5F));

        PartDefinition abdomen2 = abdomen.addOrReplaceChild("abdomen2", CubeListBuilder.create().texOffs(0, 0).addBox(-5.5F, -8.0F, 0.0F, 11.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 6.0F));

        PartDefinition abdomen3 = abdomen2.addOrReplaceChild("abdomen3", CubeListBuilder.create().texOffs(32, 12).addBox(-4.5F, -6.0F, 0.0F, 9.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 6.0F));

        PartDefinition leg = body.addOrReplaceChild("leg", CubeListBuilder.create().texOffs(27, 39).addBox(0.0F, -1.0F, -0.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 2.0F, -2.25F, 0.0F, 0.3054F, -0.4363F));

        PartDefinition legMiddle = leg.addOrReplaceChild("legMiddle", CubeListBuilder.create().texOffs(28, 37).addBox(0.0F, 0.0F, -0.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, -1.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition legTip = legMiddle.addOrReplaceChild("legTip", CubeListBuilder.create().texOffs(28, 4).addBox(0.0F, 0.0F, -0.5F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6981F));

        PartDefinition leg2 = body.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(28, 35).addBox(0.0F, -1.0F, -0.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 2.0F, -0.75F, 0.0F, 0.0873F, -0.4363F));

        PartDefinition legMiddle2 = leg2.addOrReplaceChild("legMiddle2", CubeListBuilder.create().texOffs(34, 10).addBox(0.0F, 0.0F, -0.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, -1.0F, 0.0F, 0.0F, 0.0436F, 0.7854F));

        PartDefinition legTip2 = legMiddle2.addOrReplaceChild("legTip2", CubeListBuilder.create().texOffs(28, 2).addBox(0.0F, 0.0F, -0.5F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition leg3 = body.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(27, 39).addBox(0.0F, -1.0F, -0.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 2.0F, 1.0F, 0.0F, -0.0436F, -0.4363F));

        PartDefinition legMiddle3 = leg3.addOrReplaceChild("legMiddle3", CubeListBuilder.create().texOffs(28, 37).addBox(0.0F, 0.0F, -0.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, -1.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition legTip3 = legMiddle3.addOrReplaceChild("legTip3", CubeListBuilder.create().texOffs(28, 4).addBox(0.0F, 0.0F, -0.5F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6981F));

        PartDefinition leg4 = body.addOrReplaceChild("leg4", CubeListBuilder.create().texOffs(34, 8).addBox(0.0F, -1.0F, -0.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 2.0F, 2.0F, 0.0F, -0.3054F, -0.48F));

        PartDefinition legMiddle4 = leg4.addOrReplaceChild("legMiddle4", CubeListBuilder.create().texOffs(34, 6).addBox(0.0F, 0.0F, -0.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, -1.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition legTip4 = legMiddle4.addOrReplaceChild("legTip4", CubeListBuilder.create().texOffs(28, 0).addBox(0.0F, 0.0F, -0.5F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.829F));

        PartDefinition leg5 = body.addOrReplaceChild("leg5", CubeListBuilder.create().texOffs(27, 39).mirror().addBox(-6.0F, -1.0F, -0.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, 2.0F, -2.25F, 0.0F, -0.3054F, 0.4363F));

        PartDefinition legMiddle5 = leg5.addOrReplaceChild("legMiddle5", CubeListBuilder.create().texOffs(28, 37).mirror().addBox(-6.0F, 0.0F, -0.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-6.0F, -1.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition legTip5 = legMiddle5.addOrReplaceChild("legTip5", CubeListBuilder.create().texOffs(28, 4).mirror().addBox(-7.0F, 0.0F, -0.5F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-6.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.6981F));

        PartDefinition leg6 = body.addOrReplaceChild("leg6", CubeListBuilder.create().texOffs(28, 35).mirror().addBox(-6.0F, -1.0F, -0.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, 2.0F, -0.75F, 0.0F, -0.0873F, 0.4363F));

        PartDefinition legMiddle6 = leg6.addOrReplaceChild("legMiddle6", CubeListBuilder.create().texOffs(34, 10).mirror().addBox(-6.0F, 0.0F, -0.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-6.0F, -1.0F, 0.0F, 0.0F, -0.0873F, -0.7854F));

        PartDefinition legTip6 = legMiddle6.addOrReplaceChild("legTip6", CubeListBuilder.create().texOffs(28, 2).mirror().addBox(-7.0F, 0.0F, -0.5F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-6.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition leg7 = body.addOrReplaceChild("leg7", CubeListBuilder.create().texOffs(27, 39).mirror().addBox(-6.0F, -1.0F, -0.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, 2.0F, 1.0F, 0.0F, 0.0436F, 0.4363F));

        PartDefinition legMiddle7 = leg7.addOrReplaceChild("legMiddle7", CubeListBuilder.create().texOffs(28, 37).mirror().addBox(-6.0F, 0.0F, -0.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-6.0F, -1.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition legTip7 = legMiddle7.addOrReplaceChild("legTip7", CubeListBuilder.create().texOffs(28, 4).mirror().addBox(-7.0F, 0.0F, -0.5F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-6.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.6981F));

        PartDefinition leg8 = body.addOrReplaceChild("leg8", CubeListBuilder.create().texOffs(34, 8).mirror().addBox(-6.0F, -1.0F, -0.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, 2.0F, 2.0F, 0.0F, 0.3054F, 0.48F));

        PartDefinition legMiddle8 = leg8.addOrReplaceChild("legMiddle8", CubeListBuilder.create().texOffs(34, 6).mirror().addBox(-6.0F, 0.0F, -0.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-6.0F, -1.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition legTip8 = legMiddle8.addOrReplaceChild("legTip8", CubeListBuilder.create().texOffs(28, 0).mirror().addBox(-7.0F, 0.0F, -0.5F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-6.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.829F));

        PartDefinition riderPos = body.addOrReplaceChild("ridingPos", CubeListBuilder.create(), PartPose.offset(0.0F, -3.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
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
                ClientHandlers.translateRider(entityRenderer, rider, model, poseStack);
                return true;
            }
        }
        return false;
    }
}