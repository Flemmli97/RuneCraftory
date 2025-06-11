package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.client.model.SittingModel;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.EntityHandonetta;
import io.github.flemmli97.tenshilib.client.AnimationManager;
import io.github.flemmli97.tenshilib.client.model.ExtendedModel;
import io.github.flemmli97.tenshilib.client.model.RideableModel;
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
import net.minecraft.world.entity.Entity;

public class ModelHandonetta<T extends EntityHandonetta> extends EntityModel<T> implements ExtendedModel, RideableModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(RuneCraftory.modRes("handonetta"), "main");

    protected final ModelPartHandler model;
    protected final BlockBenchAnimations anim;

    public ModelPartHandler.ModelPartExtended palm;
    public ModelPartHandler.ModelPartExtended ridingPosition;

    public ModelHandonetta(ModelPart root) {
        super();
        this.model = new ModelPartHandler(root, "root");
        this.anim = AnimationManager.getInstance().getAnimation(RuneCraftory.modRes("handonetta"));
        this.palm = this.model.getPart("palm");
        this.ridingPosition = this.model.getPart("ridingPos");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition palm = partdefinition.addOrReplaceChild("palm", CubeListBuilder.create().texOffs(0, 0).addBox(-15.5F, -5.0F, -4.0F, 31.0F, 14.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, 4.0F, 0.1745F, 0.0F, 0.0F));

        PartDefinition palmLower = palm.addOrReplaceChild("palmLower", CubeListBuilder.create().texOffs(0, 40).addBox(-10.5F, -0.25F, -4.0F, 20.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 9.25F, 0.0F));

        PartDefinition palm2 = palmLower.addOrReplaceChild("palm2", CubeListBuilder.create().texOffs(86, 60).addBox(0.0F, 0.0F, -3.99F, 5.0F, 9.0F, 7.98F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.5F, -0.25F, 0.0F, 0.0F, 0.0F, -0.6109F));

        PartDefinition palm3 = palmLower.addOrReplaceChild("palm3", CubeListBuilder.create().texOffs(86, 77).addBox(-5.0F, 0.0F, -3.99F, 5.0F, 9.0F, 7.98F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.5F, -0.25F, 0.0F, 0.0F, 0.0F, 0.7418F));

        PartDefinition fingerBase = palm.addOrReplaceChild("fingerBase", CubeListBuilder.create().texOffs(0, 22).addBox(-15.5F, -10.0F, -8.0F, 31.0F, 10.0F, 8.0F, new CubeDeformation(-0.001F)), PartPose.offsetAndRotation(0.0F, -5.0F, 4.0F, 0.3927F, 0.0F, 0.0F));

        PartDefinition index = fingerBase.addOrReplaceChild("index", CubeListBuilder.create().texOffs(78, 15).addBox(-3.5F, -8.0F, -3.5F, 7.0F, 8.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(12.0F, -10.0F, -4.0F, 0.2182F, 0.0F, 0.0F));

        PartDefinition index2 = index.addOrReplaceChild("index2", CubeListBuilder.create().texOffs(78, 0).addBox(-3.5F, -8.0F, -3.5F, 7.0F, 8.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition index3 = index2.addOrReplaceChild("index3", CubeListBuilder.create().texOffs(58, 78).addBox(-3.5F, -8.0F, -3.5F, 7.0F, 8.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.48F, 0.0F, 0.0F));

        PartDefinition middle = fingerBase.addOrReplaceChild("middle", CubeListBuilder.create().texOffs(30, 62).addBox(-3.5F, -10.0F, -3.5F, 7.0F, 10.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -10.0F, -4.0F, 0.2182F, 0.0F, 0.0F));

        PartDefinition middle2 = middle.addOrReplaceChild("middle2", CubeListBuilder.create().texOffs(58, 62).addBox(-3.5F, -9.0F, -3.5F, 7.0F, 9.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.0F, 0.0F, 0.3054F, 0.0F, 0.0F));

        PartDefinition middle3 = middle2.addOrReplaceChild("middle3", CubeListBuilder.create().texOffs(28, 79).addBox(-3.5F, -8.0F, -3.5F, 7.0F, 8.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -9.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

        PartDefinition ring = fingerBase.addOrReplaceChild("ring", CubeListBuilder.create().texOffs(0, 78).addBox(-3.5F, -9.0F, -3.5F, 7.0F, 9.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -10.0F, -4.0F, 0.3491F, 0.0F, 0.0F));

        PartDefinition ring2 = ring.addOrReplaceChild("ring2", CubeListBuilder.create().texOffs(86, 30).addBox(-3.5F, -8.0F, -3.5F, 7.0F, 8.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -9.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition ring3 = ring2.addOrReplaceChild("ring3", CubeListBuilder.create().texOffs(86, 45).addBox(-3.5F, -8.0F, -3.5F, 7.0F, 8.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

        PartDefinition pinky = fingerBase.addOrReplaceChild("pinky", CubeListBuilder.create().texOffs(56, 93).addBox(-3.5F, -7.0F, -3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-12.0F, -10.0F, -4.0F, 0.2182F, 0.0F, 0.0F));

        PartDefinition pinky2 = pinky.addOrReplaceChild("pinky2", CubeListBuilder.create().texOffs(0, 94).addBox(-3.5F, -7.0F, -3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, 0.0F, 0.3054F, 0.0F, 0.0F));

        PartDefinition pinky3 = pinky2.addOrReplaceChild("pinky3", CubeListBuilder.create().texOffs(28, 94).addBox(-3.5F, -7.0F, -3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, 0.0F, 0.6545F, 0.0F, 0.0F));

        PartDefinition thumb = palm.addOrReplaceChild("thumb", CubeListBuilder.create().texOffs(56, 40).addBox(-8.0F, -15.0F, -3.5F, 8.0F, 15.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.5F, 7.0F, 0.0F, 0.48F, 0.0F, 0.9599F));

        PartDefinition thumb2 = thumb.addOrReplaceChild("thumb2", CubeListBuilder.create().texOffs(0, 58).addBox(-8.0F, -13.0F, -3.5F, 8.0F, 13.0F, 7.0F, new CubeDeformation(-0.001F)), PartPose.offsetAndRotation(0.0F, -15.0F, 0.0F, 0.3491F, 0.0F, -0.5236F));

        PartDefinition ridingPos = palm.addOrReplaceChild("ridingPos", CubeListBuilder.create(), PartPose.offset(0.0F, -5.0F, 4.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.model.getMainPart().render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.model.resetPoses();
        float partialTicks = ClientHandlers.getPartialTicks();
        this.anim.setVariable("head_x_rotation", entity::getXRot);
        if (entity.deathTime <= 0 && !entity.playDeath()) {
            this.anim.doAnimation(this, "idle", entity.tickCount, partialTicks);
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
                this.palm.translateAndRotate(poseStack);
                this.ridingPosition.translateAndRotate(poseStack);
                ClientHandlers.translateRider(entityRenderer, rider, model, poseStack);
                return true;
            }
        }
        return false;
    }
}