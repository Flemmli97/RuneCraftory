package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.client.model.SittingModel;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityDemon;
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

public class ModelDemon<T extends EntityDemon> extends EntityModel<T> implements ExtendedModel, RideableModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(RuneCraftory.MODID, "demon"), "main");

    protected final ModelPartHandler model;
    protected final BlockBenchAnimations anim;

    public ModelPartHandler.ModelPartExtended body;
    public ModelPartHandler.ModelPartExtended head;
    public ModelPartHandler.ModelPartExtended ridingPosition;

    public ModelDemon(ModelPart root) {
        super();
        this.model = new ModelPartHandler(root, "root");
        this.anim = AnimationManager.getInstance().getAnimation(new ResourceLocation(RuneCraftory.MODID, "demon"));
        this.body = this.model.getPart("body");
        this.head = this.model.getPart("head");
        this.ridingPosition = this.model.getPart("ridingPos");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(21, 0).addBox(-4.0F, -6.5F, -2.5F, 8.0F, 13.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.5F, 0.0F));

        PartDefinition leftWingBase = body.addOrReplaceChild("leftWingBase", CubeListBuilder.create().texOffs(44, 24).addBox(0.0F, -0.5F, -1.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(32, 48).addBox(0.0F, -1.5F, -0.5F, 8.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -2.0F, 2.5F, 0.4363F, -0.5236F, -0.6981F));

        PartDefinition leftWing = leftWingBase.addOrReplaceChild("leftWing", CubeListBuilder.create().texOffs(36, 28).addBox(0.0F, 0.0F, -0.5F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 43).addBox(0.0F, 1.0F, -0.5F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 36).addBox(1.0F, 1.0F, 0.025F, 11.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, -0.5F, -0.5F, 0.0F, 0.0F, 0.3927F));

        PartDefinition rightWingBase = body.addOrReplaceChild("rightWingBase", CubeListBuilder.create().texOffs(44, 24).mirror().addBox(-8.0F, -0.5F, -1.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(32, 48).mirror().addBox(-8.0F, -1.5F, -0.5F, 8.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.5F, -2.0F, 2.5F, 0.4363F, 0.5236F, 0.6981F));

        PartDefinition rightWing = rightWingBase.addOrReplaceChild("rightWing", CubeListBuilder.create().texOffs(36, 28).mirror().addBox(-12.0F, 0.0F, -0.5F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(16, 43).mirror().addBox(-1.0F, 1.0F, -0.5F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 36).mirror().addBox(-12.0F, 1.0F, 0.025F, 11.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-8.0F, -0.5F, -0.5F, 0.0F, 0.0F, -0.3927F));

        PartDefinition tailBase = body.addOrReplaceChild("tailBase", CubeListBuilder.create().texOffs(32, 18).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 4.5F, 2.5F, -0.5672F, 0.0F, 0.0F));

        PartDefinition tail = tailBase.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(24, 28).addBox(-1.0F, -2.0F, 0.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 8.0F, 0.48F, 0.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 20).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.5F, 0.0F));

        PartDefinition leftHorn = head.addOrReplaceChild("leftHorn", CubeListBuilder.create().texOffs(36, 30).addBox(-1.0F, -2.0F, 0.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(50, 37).addBox(-1.0F, -1.5F, 4.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.25F, -6.0F, -3.25F, 0.5672F, 0.829F, 0.0F));

        PartDefinition rightHorn = head.addOrReplaceChild("rightHorn", CubeListBuilder.create().texOffs(24, 20).addBox(0.0F, -2.0F, 0.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(48, 48).addBox(0.0F, -1.5F, 4.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.25F, -6.0F, -3.25F, 0.5672F, -0.829F, 0.0F));

        PartDefinition leftArm = body.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(47, 0).addBox(0.0F, -1.5F, -3.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, -4.0F, 0.5F));

        PartDefinition leftArmDown = leftArm.addOrReplaceChild("leftArmDown", CubeListBuilder.create().texOffs(16, 48).addBox(-2.0F, 0.0F, -4.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 4.5F, 1.0F));

        PartDefinition rightArm = body.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(44, 14).addBox(-4.0F, -1.5F, -3.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, -4.0F, 0.5F));

        PartDefinition rightArmDown = rightArm.addOrReplaceChild("rightArmDown", CubeListBuilder.create().texOffs(0, 10).addBox(-2.0F, 0.0F, -4.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 4.5F, 1.0F));

        PartDefinition trident = rightArmDown.addOrReplaceChild("trident", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -0.5F, -15.0F, 1.0F, 1.0F, 19.0F, new CubeDeformation(0.0F))
                .texOffs(47, 10).addBox(-2.5F, -0.5F, -16.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 20).addBox(1.5F, -0.5F, -19.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 20).addBox(-2.5F, -0.5F, -19.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(44, 30).addBox(-0.5F, -0.5F, -22.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, -2.0F));

        PartDefinition leftLeg = body.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(0, 43).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 6.5F, -0.5F));

        PartDefinition leftLeg2 = leftLeg.addOrReplaceChild("leftLeg2", CubeListBuilder.create().texOffs(38, 38).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, -2.0F));

        PartDefinition rightLeg = body.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(22, 38).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 6.5F, -0.5F));

        PartDefinition rightLeg2 = rightLeg.addOrReplaceChild("rightLeg2", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, -2.0F));

        PartDefinition ridingPos = body.addOrReplaceChild("ridingPos", CubeListBuilder.create(), PartPose.offset(0.0F, 3.0F, 5.0F));

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
                ClientHandlers.translateRider(entityRenderer, rider, model, poseStack);
                return true;
            }
        }
        return false;
    }
}