package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.client.model.SittingModel;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.EntityRaccoon;
import io.github.flemmli97.tenshilib.client.AnimationManager;
import io.github.flemmli97.tenshilib.client.model.BlockBenchAnimations;
import io.github.flemmli97.tenshilib.client.model.ModelPartHandler;
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

public class ModelRaccoonBerserk<T extends EntityRaccoon> extends ModelRaccoonBase<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(RuneCraftory.MODID, "raccoon_berserk"), "main");

    protected final ModelPartHandler model;
    protected final BlockBenchAnimations anim;

    public ModelPartHandler.ModelPartExtended body;
    public ModelPartHandler.ModelPartExtended head;
    public ModelPartHandler.ModelPartExtended ridingPosition;

    public ModelRaccoonBerserk(ModelPart root) {
        super();
        this.model = new ModelPartHandler(root, "root");
        this.anim = AnimationManager.getInstance().getAnimation(new ResourceLocation(RuneCraftory.MODID, "raccoon_berserk"));
        this.body = this.model.getPart("body");
        this.head = this.model.getPart("head");
        this.ridingPosition = this.model.getPart("ridingPos");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, 0.0F, -5.5F, 12.0F, 7.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.75F, 3.5F, 0.0436F, 0.0F, 0.0F));

        PartDefinition bodyMiddle = body.addOrReplaceChild("bodyMiddle", CubeListBuilder.create().texOffs(31, 28).addBox(-5.0F, -8.0F, -4.5F, 10.0F, 8.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, 0.0F, 0.1309F, 0.0F, 0.0F));

        PartDefinition bodyUpper = bodyMiddle.addOrReplaceChild("bodyUpper", CubeListBuilder.create().texOffs(38, 10).addBox(-6.0F, -6.0F, -4.0F, 12.0F, 6.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, -0.0873F, 0.0F, 0.0F));

        PartDefinition head = bodyUpper.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 18).addBox(-5.0F, -8.0F, -5.0F, 10.0F, 9.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

        PartDefinition leaf = head.addOrReplaceChild("leaf", CubeListBuilder.create().texOffs(27, 0).addBox(-1.5F, -0.05F, -6.75F, 5.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -8.0F, 0.5F));

        PartDefinition snout = head.addOrReplaceChild("snout", CubeListBuilder.create().texOffs(70, 10).addBox(-2.5F, -1.255F, -2.375F, 5.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-1.0F, -1.245F, -4.125F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.245F, -5.125F));

        PartDefinition earLeft = head.addOrReplaceChild("earLeft", CubeListBuilder.create().texOffs(5, 6).addBox(-1.0F, -2.0F, 0.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -8.0F, -2.0F));

        PartDefinition earRight = head.addOrReplaceChild("earRight", CubeListBuilder.create().texOffs(0, 4).addBox(-1.0F, -2.0F, 0.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, -8.0F, -2.0F));

        PartDefinition leftArm = bodyUpper.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(0, 63).addBox(0.0F, -2.5F, -2.0F, 5.0F, 7.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(15, 70).addBox(2.0F, -2.5F, -2.0F, 3.0F, 5.0F, 5.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(6.0F, -2.5F, -0.5F, -0.4363F, -0.3491F, 0.0F));

        PartDefinition leftArmDown = leftArm.addOrReplaceChild("leftArmDown", CubeListBuilder.create().texOffs(44, 57).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 4.5F, 0.5F, -0.6545F, 0.3491F, 0.0F));

        PartDefinition rightArm = bodyUpper.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(60, 24).addBox(-5.0F, -2.5F, -2.0F, 5.0F, 7.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(59, 68).addBox(-5.0F, -2.5F, -2.0F, 3.0F, 5.0F, 5.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(-6.0F, -2.5F, -0.5F, -0.4363F, 0.3491F, 0.0F));

        PartDefinition rightArmDown = rightArm.addOrReplaceChild("rightArmDown", CubeListBuilder.create().texOffs(24, 57).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 4.5F, 0.5F, -0.6545F, -0.3491F, 0.0F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 37).addBox(-2.0F, -3.0F, -1.0F, 5.0F, 5.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 3.5F, 5.25F));

        PartDefinition tail2 = tail.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(0, 51).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 8.0F, new CubeDeformation(-0.01F)), PartPose.offset(0.5F, -0.5F, 7.75F));

        PartDefinition leftLegBase = body.addOrReplaceChild("leftLegBase", CubeListBuilder.create().texOffs(46, 45).addBox(-0.5F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, 4.5F, 0.0F, -0.6109F, 0.0F, 0.0F));

        PartDefinition leftLeg = leftLegBase.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(65, 0).addBox(-2.5F, -1.0F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 3.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

        PartDefinition leftLegDown = leftLeg.addOrReplaceChild("leftLegDown", CubeListBuilder.create().texOffs(64, 57).addBox(-2.5F, 0.0F, -1.5F, 5.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, -1.0F, 0.3927F, 0.0F, 0.0F));

        PartDefinition rightLegBase = body.addOrReplaceChild("rightLegBase", CubeListBuilder.create().texOffs(22, 45).addBox(-5.5F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, 4.5F, 0.0F, -0.6109F, 0.0F, 0.0F));

        PartDefinition rightLeg = rightLegBase.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(45, 0).addBox(-2.5F, -1.0F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 3.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

        PartDefinition rightLegDown = rightLeg.addOrReplaceChild("rightLegDown", CubeListBuilder.create().texOffs(64, 40).addBox(-2.5F, 0.0F, -1.5F, 5.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, -1.0F, 0.3927F, 0.0F, 0.0F));

        PartDefinition ridingPos = body.addOrReplaceChild("ridingPos", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 7.0F));

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
            this.head.yRot += netHeadYaw * 0.8 * Mth.DEG_TO_RAD;
            this.head.xRot += headPitch * Mth.DEG_TO_RAD;
            this.anim.doAnimation(this, "idle", entity.tickCount, partialTicks);
            if (entity.moveTick() > 0) {
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
                ClientHandlers.translateRider(entityRenderer, model, poseStack);
                return true;
            }
        }
        return false;
    }
}