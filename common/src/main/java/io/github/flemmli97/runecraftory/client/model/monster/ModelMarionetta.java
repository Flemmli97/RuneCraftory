package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.SittingModel;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.EntityMarionetta;
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
import net.minecraft.world.entity.Entity;

public class ModelMarionetta<T extends EntityMarionetta> extends EntityModel<T> implements ExtendedModel, RideableModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(RuneCraftory.MODID, "marionetta"), "main");

    protected final ModelPartHandler model;
    protected final BlockBenchAnimations anim;

    public ModelPartHandler.ModelPartExtended body;

    public ModelMarionetta(ModelPart root) {
        super();
        this.model = new ModelPartHandler(root, "root");
        this.anim = AnimationManager.getInstance().getAnimation(new ResourceLocation(RuneCraftory.MODID, "marionetta"));
        this.body = this.model.getPart("body");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(29, 31).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.75F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 15).addBox(-4.0F, -7.0F, -4.0F, 8.0F, 7.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, 0.0F));

        PartDefinition hairLeft = head.addOrReplaceChild("hairLeft", CubeListBuilder.create().texOffs(19, 67).addBox(0.0F, 0.0F, -1.0F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(30, 80).addBox(6.75F, 0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -7.0F, 1.0F, 0.0F, 0.0F, 0.6109F));

        PartDefinition hairRight = head.addOrReplaceChild("hairRight", CubeListBuilder.create().texOffs(0, 72).addBox(-7.0F, 0.0F, -1.0F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(30, 77).addBox(-8.75F, 0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -7.0F, 1.0F, 0.0F, 0.0F, -0.6109F));

        PartDefinition hat = head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -2.0F, -6.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.5F, 0.0F));

        PartDefinition hatMiddle = hat.addOrReplaceChild("hatMiddle", CubeListBuilder.create().texOffs(49, 0).addBox(-5.0F, -4.0F, -4.0F, 9.0F, 4.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, -2.0F, -0.5F));

        PartDefinition hatTop = hatMiddle.addOrReplaceChild("hatTop", CubeListBuilder.create().texOffs(0, 31).addBox(-4.0F, -4.0F, -3.0F, 7.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 0.0F));

        PartDefinition hatTopLeft = hatTop.addOrReplaceChild("hatTopLeft", CubeListBuilder.create().texOffs(0, 67).addBox(0.0F, -2.0F, 0.0F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -2.0F, 0.0F, 0.0F, -0.2618F, -0.6109F));

        PartDefinition hatTopLeftTip = hatTopLeft.addOrReplaceChild("hatTopLeftTip", CubeListBuilder.create().texOffs(13, 82).addBox(0.0F, 0.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, -2.0F, 1.0F, 0.0F, 0.0F, 1.1345F));

        PartDefinition hatTopRight = hatTop.addOrReplaceChild("hatTopRight", CubeListBuilder.create().texOffs(60, 56).addBox(-7.0F, -2.0F, 0.0F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -2.0F, 0.0F, 0.0F, 0.2618F, 0.6109F));

        PartDefinition hatTopRightTip = hatTopRight.addOrReplaceChild("hatTopRightTip", CubeListBuilder.create().texOffs(13, 77).addBox(-6.0F, 0.0F, -2.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, -2.0F, 2.0F, 0.0F, 0.0F, -1.1345F));

        PartDefinition leftArm = body.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(17, 56).addBox(-1.5F, -2.25F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(47, 56).addBox(-1.0F, -2.0F, -1.5F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, -3.25F, 0.0F));

        PartDefinition leftArmDown = leftArm.addOrReplaceChild("leftArmDown", CubeListBuilder.create().texOffs(0, 77).addBox(-1.5F, 0.0F, -3.0F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 5.0F, 1.5F));

        PartDefinition rightArm = body.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(0, 56).addBox(-2.5F, -2.25F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(34, 56).addBox(-2.0F, -2.0F, -1.5F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, -3.25F, 0.0F));

        PartDefinition rightArmDown = rightArm.addOrReplaceChild("rightArmDown", CubeListBuilder.create().texOffs(38, 67).addBox(-1.5F, 0.0F, -3.0F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 5.0F, 1.5F));

        PartDefinition dressUp = body.addOrReplaceChild("dressUp", CubeListBuilder.create().texOffs(70, 15).addBox(-5.0F, -2.0F, -3.0F, 10.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, 0.0F));

        PartDefinition dressDown = dressUp.addOrReplaceChild("dressDown", CubeListBuilder.create().texOffs(33, 15).addBox(-5.5F, 0.0F, -3.5F, 11.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 0.0F));

        PartDefinition clothBack = dressDown.addOrReplaceChild("clothBack", CubeListBuilder.create().texOffs(19, 72).addBox(-4.5F, 0.0F, 0.0F, 9.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, 3.0F, 0.2182F, 0.0F, 0.0F));

        PartDefinition clothBack2 = clothBack.addOrReplaceChild("clothBack2", CubeListBuilder.create().texOffs(51, 45).addBox(-5.5F, 0.0F, 0.0F, 9.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 3.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

        PartDefinition leftLeg = dressDown.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(34, 45).addBox(-1.9F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(1.9F, 2.25F, 0.0F));

        PartDefinition leftLegDown = leftLeg.addOrReplaceChild("leftLegDown", CubeListBuilder.create().texOffs(17, 45).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.1F, 6.0F, -2.0F));

        PartDefinition rightLeg = dressDown.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(0, 45).addBox(-2.1F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.9F, 2.25F, 0.0F));

        PartDefinition rightLegDown = rightLeg.addOrReplaceChild("rightLegDown", CubeListBuilder.create().texOffs(54, 31).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.1F, 6.0F, -2.0F));

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
        float partialTicks = Minecraft.getInstance().getFrameTime();
        if (entity.deathTime <= 0 && !entity.playDeath()) {
            this.anim.doAnimation(this, "iddle", entity.tickCount, partialTicks);
            if (entity.moveTick() > 0)
                this.anim.doAnimation(this, "walk", entity.tickCount, partialTicks, entity.interpolatedMoveTick(partialTicks));
        }
        if (anim != null) {
            if (entity.caughtTarget())
                this.anim.doAnimation(this, "chest_attack_hit", anim.getTick(), partialTicks);
            else
                this.anim.doAnimation(this, anim.getAnimationClient(), anim.getTick(), partialTicks);
        }
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
                    poseStack.translate(0, 6 / 16d, 7 / 16d);
                return true;
            }
        }
        return false;
    }
}