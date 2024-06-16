package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.client.model.SittingModel;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityMinotaur;
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

public class ModelMinotaur<T extends EntityMinotaur> extends EntityModel<T> implements ExtendedModel, RideableModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(RuneCraftory.MODID, "minotaur"), "main");

    protected final ModelPartHandler model;
    protected final BlockBenchAnimations anim;

    public ModelPartHandler.ModelPartExtended body;
    public ModelPartHandler.ModelPartExtended head;
    public ModelPartHandler.ModelPartExtended ridingPosition;

    public ModelMinotaur(ModelPart root) {
        super();
        this.model = new ModelPartHandler(root, "root");
        this.anim = AnimationManager.getInstance().getAnimation(new ResourceLocation(RuneCraftory.MODID, "minotaur"));
        this.body = this.model.getPart("body");
        this.head = this.model.getPart("head");
        this.ridingPosition = this.model.getPart("ridingPos");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 19).addBox(-6.0F, -3.75F, -4.5F, 12.0F, 10.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.75F, 1.0F, 0.1745F, 0.0F, 0.0F));

        PartDefinition bodyUpper = body.addOrReplaceChild("bodyUpper", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -10.0F, -4.5F, 14.0F, 10.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.75F, 0.0F));

        PartDefinition head = bodyUpper.addOrReplaceChild("head", CubeListBuilder.create().texOffs(33, 29).addBox(-4.5F, -9.0F, -5.5F, 9.0F, 9.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.0F, 0.0F));

        PartDefinition earLeft = head.addOrReplaceChild("earLeft", CubeListBuilder.create().texOffs(0, 4).addBox(-2.5F, -1.0F, -1.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -8.0F, -3.5F, 0.0873F, 0.0F, 0.0F));

        PartDefinition earRight = head.addOrReplaceChild("earRight", CubeListBuilder.create().texOffs(0, 0).addBox(0.5F, -1.0F, -1.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -8.0F, -3.5F, 0.0873F, 0.0F, 0.0F));

        PartDefinition headFront = head.addOrReplaceChild("headFront", CubeListBuilder.create().texOffs(80, 61).addBox(-2.75F, -2.0F, -4.0F, 5.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.25F, -2.0F, -5.5F));

        PartDefinition leftHorn = head.addOrReplaceChild("leftHorn", CubeListBuilder.create().texOffs(37, 0).addBox(-1.0F, -3.0F, -1.0F, 6.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -7.0F, -1.0F, 0.0F, 0.0F, -0.6109F));

        PartDefinition leftHorn2 = leftHorn.addOrReplaceChild("leftHorn2", CubeListBuilder.create().texOffs(65, 61).addBox(-1.0F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, -2.0F, 0.5F, 0.0F, 0.3054F, -0.1309F));

        PartDefinition rightHorn = head.addOrReplaceChild("rightHorn", CubeListBuilder.create().texOffs(33, 21).addBox(-5.0F, -3.0F, -1.0F, 6.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -7.0F, -1.0F, 0.0F, 0.0F, 0.6109F));

        PartDefinition rightHorn2 = rightHorn.addOrReplaceChild("rightHorn2", CubeListBuilder.create().texOffs(43, 47).addBox(-5.0F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, -2.0F, 0.5F, 0.0F, -0.3054F, 0.1309F));

        PartDefinition handLeftUp = bodyUpper.addOrReplaceChild("handLeftUp", CubeListBuilder.create().texOffs(60, 16).addBox(0.0F, -2.0F, -4.5F, 5.0F, 10.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, -3.0F, 2.0F, -0.2618F, 0.0F, -0.48F));

        PartDefinition handLeftDown = handLeftUp.addOrReplaceChild("handLeftDown", CubeListBuilder.create().texOffs(70, 31).addBox(-2.0F, 0.0F, -4.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 8.0F, 0.0F, -0.4363F, 0.2618F, 0.2618F));

        PartDefinition handRightUp = bodyUpper.addOrReplaceChild("handRightUp", CubeListBuilder.create().texOffs(8, 60).addBox(-5.0F, -2.0F, -4.5F, 5.0F, 10.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, -3.0F, 2.0F, -0.2618F, 0.0F, 0.48F));

        PartDefinition handRightDown = handRightUp.addOrReplaceChild("handRightDown", CubeListBuilder.create().texOffs(68, 67).addBox(-2.0F, 0.0F, -4.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 8.0F, 0.0F, -0.4363F, -0.2618F, -0.2618F));

        PartDefinition axe = handRightDown.addOrReplaceChild("axe", CubeListBuilder.create().texOffs(0, 38).addBox(-1.0F, -10.0F, -1.0F, 2.0F, 38.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(20, 38).addBox(-1.0F, 20.0F, -6.0F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.025F))
                .texOffs(58, 78).addBox(-1.0F, 18.0F, -2.0F, 2.0F, 8.0F, 5.0F, new CubeDeformation(0.025F))
                .texOffs(8, 38).addBox(-1.0F, 15.0F, 3.0F, 2.0F, 14.0F, 8.0F, new CubeDeformation(0.025F))
                .texOffs(22, 81).addBox(-1.0F, 16.0F, 11.0F, 2.0F, 12.0F, 1.0F, new CubeDeformation(0.025F))
                .texOffs(69, 51).addBox(-0.5F, 14.0F, 2.0F, 1.0F, 1.0F, 9.0F, new CubeDeformation(0.025F))
                .texOffs(65, 4).addBox(-0.5F, 29.0F, 2.0F, 1.0F, 1.0F, 9.0F, new CubeDeformation(0.025F))
                .texOffs(5, 7).addBox(-0.5F, 15.0F, 11.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.025F))
                .texOffs(5, 3).addBox(-0.5F, 29.0F, 11.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.025F))
                .texOffs(28, 81).addBox(-0.5F, 16.0F, 12.0F, 1.0F, 12.0F, 1.0F, new CubeDeformation(0.025F)), PartPose.offsetAndRotation(0.0F, 10.0F, -2.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition ridingPos = bodyUpper.addOrReplaceChild("ridingPos", CubeListBuilder.create(), PartPose.offset(0.0F, -10.0F, 6.0F));

        PartDefinition tailBase = body.addOrReplaceChild("tailBase", CubeListBuilder.create().texOffs(28, 47).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.25F, 4.5F, -0.6981F, 0.0F, 0.0F));

        PartDefinition tailMiddle = tailBase.addOrReplaceChild("tailMiddle", CubeListBuilder.create().texOffs(35, 8).addBox(-1.0F, -2.0F, 0.0F, 2.0F, 2.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 11.0F, 0.5672F, 0.0F, 0.0F));

        PartDefinition tailTip = tailMiddle.addOrReplaceChild("tailTip", CubeListBuilder.create().texOffs(43, 60).addBox(-1.0F, -2.0F, 0.0F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 10.5F, 0.1745F, 0.0F, 0.0F));

        PartDefinition legLeftUp = body.addOrReplaceChild("legLeftUp", CubeListBuilder.create().texOffs(54, 47).addBox(-2.0F, -2.0F, -3.0F, 6.0F, 7.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(43, 71).addBox(-1.5F, 5.0F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.5F, 4.25F, 0.0F, -0.5236F, 0.0F, 0.0F));

        PartDefinition legLeftDown = legLeftUp.addOrReplaceChild("legLeftDown", CubeListBuilder.create().texOffs(50, 60).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 10.0F, -2.0F));

        PartDefinition legLeftDown2 = legLeftDown.addOrReplaceChild("legLeftDown2", CubeListBuilder.create().texOffs(80, 47).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 7.0F));

        PartDefinition legLeftDown3 = legLeftDown2.addOrReplaceChild("legLeftDown3", CubeListBuilder.create().texOffs(76, 0).addBox(-1.5F, 0.0F, -6.0F, 4.0F, 2.0F, 6.0F, new CubeDeformation(0.025F)), PartPose.offsetAndRotation(-0.5F, 4.0F, 4.0F, 0.3491F, 0.0F, 0.0F));

        PartDefinition legRightUp = body.addOrReplaceChild("legRightUp", CubeListBuilder.create().texOffs(50, 0).addBox(-4.0F, -2.0F, -3.0F, 6.0F, 7.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(23, 71).addBox(-3.5F, 5.0F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5F, 4.25F, 0.0F, -0.5236F, 0.0F, 0.0F));

        PartDefinition legRightDown = legRightUp.addOrReplaceChild("legRightDown", CubeListBuilder.create().texOffs(28, 60).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 10.0F, -2.0F));

        PartDefinition legRightDown2 = legRightDown.addOrReplaceChild("legRightDown2", CubeListBuilder.create().texOffs(80, 14).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 7.0F));

        PartDefinition legRightDown3 = legRightDown2.addOrReplaceChild("legRightDown3", CubeListBuilder.create().texOffs(2, 75).addBox(-2.5F, 0.0F, -6.0F, 4.0F, 2.0F, 6.0F, new CubeDeformation(0.025F)), PartPose.offsetAndRotation(0.5F, 4.0F, 4.0F, 0.3491F, 0.0F, 0.0F));

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