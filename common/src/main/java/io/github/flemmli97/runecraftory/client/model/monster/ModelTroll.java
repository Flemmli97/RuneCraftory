package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.SittingModel;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityTroll;
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

public class ModelTroll<T extends EntityTroll> extends EntityModel<T> implements ExtendedModel, RideableModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(RuneCraftory.MODID, "troll"), "main");

    protected final ModelPartHandler model;
    protected final BlockBenchAnimations anim;

    public ModelPartHandler.ModelPartExtended body;
    public ModelPartHandler.ModelPartExtended head;

    public ModelTroll(ModelPart root) {
        super();
        this.model = new ModelPartHandler(root, "root");
        this.anim = AnimationManager.getInstance().getAnimation(new ResourceLocation(RuneCraftory.MODID, "troll"));
        this.body = this.model.getPart("body");
        this.head = this.model.getPart("head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(54, 0).addBox(-7.5F, -3.5F, -4.0F, 15.0F, 7.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.5F, 0.0F));

        PartDefinition clothFront = body.addOrReplaceChild("clothFront", CubeListBuilder.create().texOffs(0, 90).addBox(-6.5F, 0.0F, 0.0F, 13.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.5F, -4.0F));

        PartDefinition clothBack = body.addOrReplaceChild("clothBack", CubeListBuilder.create().texOffs(84, 76).addBox(-6.5F, 0.0F, 0.0F, 13.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.5F, 5.0F));

        PartDefinition upperBody = body.addOrReplaceChild("upperBody", CubeListBuilder.create().texOffs(0, 0).addBox(-8.5F, -12.0F, -4.5F, 17.0F, 12.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.5F, 0.0F));

        PartDefinition head = upperBody.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 22).addBox(-5.0F, -8.75F, -5.0F, 10.0F, 10.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(0, 76).addBox(-2.0F, -9.75F, -6.0F, 4.0F, 4.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -12.25F, -1.0F));

        PartDefinition earLeft = head.addOrReplaceChild("earLeft", CubeListBuilder.create().texOffs(40, 90).addBox(0.0F, -2.5F, -0.5F, 1.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, -5.25F, -2.5F, 0.2182F, 0.3054F, 0.0F));

        PartDefinition earRight = head.addOrReplaceChild("earRight", CubeListBuilder.create().texOffs(26, 90).addBox(-1.0F, -2.5F, -0.5F, 1.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, -5.25F, -2.5F, 0.2182F, -0.3054F, 0.0F));

        PartDefinition hornLeft = head.addOrReplaceChild("hornLeft", CubeListBuilder.create().texOffs(64, 90).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, -8.75F, -2.0F));

        PartDefinition hornRight = head.addOrReplaceChild("hornRight", CubeListBuilder.create().texOffs(54, 90).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, -8.75F, -2.0F));

        PartDefinition leftArmUp = upperBody.addOrReplaceChild("leftArmUp", CubeListBuilder.create().texOffs(28, 42).addBox(0.0F, -3.0F, -3.0F, 7.0F, 11.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(56, 76).addBox(0.0F, -3.0F, -3.0F, 7.0F, 5.0F, 7.0F, new CubeDeformation(0.25F)), PartPose.offset(8.5F, -7.0F, 0.0F));

        PartDefinition leftArmDown = leftArmUp.addOrReplaceChild("leftArmDown", CubeListBuilder.create().texOffs(0, 42).addBox(-3.0F, 0.0F, -3.0F, 7.0F, 11.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 8.0F, 0.0F));

        PartDefinition rightArmUp = upperBody.addOrReplaceChild("rightArmUp", CubeListBuilder.create().texOffs(68, 22).addBox(-7.0F, -3.0F, -3.0F, 7.0F, 11.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(28, 76).addBox(-7.0F, -3.0F, -3.0F, 7.0F, 5.0F, 7.0F, new CubeDeformation(0.25F)), PartPose.offset(-8.5F, -7.0F, 0.0F));

        PartDefinition rightArmDown = rightArmUp.addOrReplaceChild("rightArmDown", CubeListBuilder.create().texOffs(40, 22).addBox(-4.0F, 0.0F, -3.0F, 7.0F, 11.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 8.0F, 0.0F));

        PartDefinition leftLegUp = body.addOrReplaceChild("leftLegUp", CubeListBuilder.create().texOffs(56, 60).addBox(-3.0F, 0.0F, -3.0F, 7.0F, 9.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 3.5F, 0.0F));

        PartDefinition leftLegDown = leftLegUp.addOrReplaceChild("leftLegDown", CubeListBuilder.create().texOffs(28, 60).addBox(-3.0F, 0.0F, 0.0F, 7.0F, 9.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 9.0F, -3.0F));

        PartDefinition rightLegUp = body.addOrReplaceChild("rightLegUp", CubeListBuilder.create().texOffs(0, 60).addBox(-4.0F, 0.0F, -3.0F, 7.0F, 9.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 3.5F, 0.0F));

        PartDefinition rightLegDown = rightLegUp.addOrReplaceChild("rightLegDown", CubeListBuilder.create().texOffs(56, 42).addBox(-4.0F, 0.0F, 0.0F, 7.0F, 9.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 9.0F, -3.0F));

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
            if (!EntityTroll.SLEEP.is(anim))
                this.anim.doAnimation(this, "idle", entity.tickCount, partialTicks);
            if (entity.moveTick() > 0)
                this.anim.doAnimation(this, "walk", entity.tickCount, partialTicks, entity.interpolatedMoveTick(partialTicks));
        }
        if (anim != null)
            this.anim.doAnimation(this, anim.getAnimationClient(), anim.getTick(), partialTicks, entity.getAnimationHandler().getInterpolatedAnimationVal(partialTicks));
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
                    poseStack.translate(0, -6 / 16d, 7 / 16d);
                return true;
            }
        }
        return false;
    }
}