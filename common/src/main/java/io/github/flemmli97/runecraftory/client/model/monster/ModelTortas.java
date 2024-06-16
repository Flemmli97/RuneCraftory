package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.client.model.SittingModel;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityTortas;
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

public class ModelTortas<T extends EntityTortas> extends EntityModel<T> implements ExtendedModel, RideableModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(RuneCraftory.MODID, "tortas"), "main");

    protected final ModelPartHandler model;
    protected final BlockBenchAnimations anim;

    public ModelPartHandler.ModelPartExtended body;
    public ModelPartHandler.ModelPartExtended neck;
    public ModelPartHandler.ModelPartExtended ridingPosition;

    public ModelTortas(ModelPart root) {
        super();
        this.model = new ModelPartHandler(root, "root");
        this.anim = AnimationManager.getInstance().getAnimation(new ResourceLocation(RuneCraftory.MODID, "tortas"));
        this.body = this.model.getPart("body");
        this.neck = this.model.getPart("neck");
        this.ridingPosition = this.model.getPart("ridingPos");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, -6.0F, -11.0F, 18.0F, 4.0F, 24.0F, new CubeDeformation(0.0F))
                .texOffs(84, 4).addBox(9.0F, -6.0F, -9.0F, 2.0F, 4.0F, 20.0F, new CubeDeformation(0.0F))
                .texOffs(84, 4).mirror().addBox(-11.0F, -6.0F, -9.0F, 2.0F, 4.0F, 20.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 28).addBox(-8.0F, -9.0F, -10.0F, 16.0F, 3.0F, 22.0F, new CubeDeformation(0.0F))
                .texOffs(76, 32).addBox(8.0F, -9.0F, -8.0F, 2.0F, 3.0F, 18.0F, new CubeDeformation(0.0F))
                .texOffs(76, 32).mirror().addBox(-10.0F, -9.0F, -8.0F, 2.0F, 3.0F, 18.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 53).addBox(-7.0F, -11.0F, -9.0F, 14.0F, 2.0F, 20.0F, new CubeDeformation(0.0F))
                .texOffs(68, 57).addBox(7.0F, -11.0F, -7.0F, 2.0F, 2.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(68, 57).mirror().addBox(-9.0F, -11.0F, -7.0F, 2.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 75).addBox(-8.0F, -2.0F, -10.0F, 16.0F, 2.0F, 21.0F, new CubeDeformation(0.0F))
                .texOffs(105, 61).addBox(0.0F, -14.0F, -7.0F, 0.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(113, 61).addBox(0.0F, -14.0F, -1.0F, 0.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(105, 68).addBox(0.0F, -14.0F, 5.0F, 0.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(113, 68).addBox(0.0F, -11.0F, 11.0F, 0.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition neck = body.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(75, 76).addBox(-3.0F, -2.0F, -2.0F, 6.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -11.0F));

        PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create().texOffs(75, 81).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 4.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(75, 91).addBox(-3.0F, -4.0F, -2.0F, 6.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(93, 76).addBox(-2.0F, -2.5F, -3.25F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, -2.0F));

        PartDefinition jaw = head.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(93, 91).addBox(-2.0F, -0.5F, -4.1F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.45F, 1.8F));

        PartDefinition leftLegFrontBase = body.addOrReplaceChild("leftLegFrontBase", CubeListBuilder.create().texOffs(0, 99).addBox(-5.0F, 0.0F, -7.0F, 5.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, -2.0F, -7.0F, 0.0F, -0.6545F, 0.0436F));

        PartDefinition leftLegFront = leftLegFrontBase.addOrReplaceChild("leftLegFront", CubeListBuilder.create().texOffs(24, 102).addBox(0.0F, 0.0F, 0.0F, 8.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -7.0F, 0.0F, -0.3054F, 0.0F));

        PartDefinition leftLegBackBase = body.addOrReplaceChild("leftLegBackBase", CubeListBuilder.create().texOffs(48, 102).addBox(0.0F, 0.0F, 0.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, -2.0F, 7.0F, 0.0F, -0.829F, 0.0436F));

        PartDefinition leftLegBack = leftLegBackBase.addOrReplaceChild("leftLegBack", CubeListBuilder.create().texOffs(64, 102).addBox(0.0F, 0.0F, 0.0F, 8.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 0.0F, 0.0F, 0.0F, -0.5672F, 0.0F));

        PartDefinition rightLegFrontBase = body.addOrReplaceChild("rightLegFrontBase", CubeListBuilder.create().texOffs(0, 99).mirror().addBox(0.0F, 0.0F, -7.0F, 5.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-8.0F, -2.0F, -7.0F, 0.0F, 0.6545F, -0.0436F));

        PartDefinition rightLegFront = rightLegFrontBase.addOrReplaceChild("rightLegFront", CubeListBuilder.create().texOffs(24, 102).mirror().addBox(-8.0F, 0.0F, 0.0F, 8.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -7.0F, 0.0F, 0.3054F, 0.0F));

        PartDefinition rightLegBackBase = body.addOrReplaceChild("rightLegBackBase", CubeListBuilder.create().texOffs(48, 102).mirror().addBox(-4.0F, 0.0F, 0.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-8.0F, -2.0F, 7.0F, 0.0F, 0.829F, -0.0436F));

        PartDefinition rightLegBack = rightLegBackBase.addOrReplaceChild("rightLegBack", CubeListBuilder.create().texOffs(64, 102).mirror().addBox(-8.0F, 0.0F, 0.0F, 8.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.0F, 0.0F, 0.0F, 0.0F, 0.5672F, 0.0F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(114, 76).addBox(-1.5F, -2.0F, 0.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 11.0F));

        PartDefinition tail2 = tail.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(114, 82).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 3.0F));

        PartDefinition tail3 = tail2.addOrReplaceChild("tail3", CubeListBuilder.create().texOffs(114, 86).addBox(-0.5F, 1.0F, 0.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 2.0F));

        PartDefinition ridingPos = body.addOrReplaceChild("ridingPos", CubeListBuilder.create(), PartPose.offset(0.0F, -11.0F, 4.0F));

        return LayerDefinition.create(meshdefinition, 128, 108);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.model.getMainPart().render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.model.resetPoses();
        this.neck.yRot += netHeadYaw * Mth.DEG_TO_RAD * 0.1f;
        this.neck.xRot += headPitch * Mth.DEG_TO_RAD * 0.1f;
        float partialTicks = Minecraft.getInstance().getFrameTime();
        if (entity.deathTime <= 0 && !entity.playDeath()) {
            this.anim.doAnimation(this, "idle", entity.tickCount, partialTicks);
            if (entity.moveTick() > 0) {
                if (entity.isSwimming())
                    this.anim.doAnimation(this, "swim", entity.tickCount, partialTicks, entity.interpolatedMoveTick(partialTicks));
                else
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