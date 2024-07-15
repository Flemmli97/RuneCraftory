package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.client.model.SittingModel;
import io.github.flemmli97.runecraftory.common.entities.monster.EntitySanoUno;
import io.github.flemmli97.tenshilib.client.model.ExtendedModel;
import io.github.flemmli97.tenshilib.client.model.ModelPartHandler;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class ModelSanoUno<T extends EntitySanoUno> extends EntityModel<T> implements ExtendedModel, RideableModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(RuneCraftory.MODID, "sano_uno"), "main");

    protected final ModelPartHandler model;

    public ModelPartHandler.ModelPartExtended body;
    public ModelPartHandler.ModelPartExtended ridingPosition;

    public ModelSanoUno(ModelPart root) {
        super();
        this.model = new ModelPartHandler(root, "root");
        this.body = this.model.getPart("body");
        this.ridingPosition = this.model.getPart("ridingPos");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 27).addBox(-6.5F, -6.0F, 0.0F, 13.0F, 13.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-7.0F, -6.5F, -13.0F, 14.0F, 14.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.0F, 3.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition tori = body.addOrReplaceChild("tori", CubeListBuilder.create().texOffs(64, 38).addBox(-5.5F, -1.1667F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(80, 18).addBox(-6.5F, -6.1667F, -1.0F, 13.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 54).addBox(3.5F, -1.1667F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -11.3333F, -11.0F));

        PartDefinition neckBase = body.addOrReplaceChild("neckBase", CubeListBuilder.create().texOffs(36, 101).addBox(-6.0F, -5.75F, -5.0F, 12.0F, 12.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(129, 66).addBox(-6.0F, -5.75F, -2.0F, 12.0F, 12.0F, 1.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(0.0F, 0.25F, -13.0F, 0.1745F, 0.0F, 0.0F));

        PartDefinition neck = neckBase.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(0, 113).addBox(-4.5F, -5.0F, -5.0F, 9.0F, 10.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.25F, -5.0F, 0.3927F, 0.0F, 0.0F));

        PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create().texOffs(44, 17).addBox(-6.0F, -1.8333F, -10.0F, 13.0F, 11.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(41, 0).addBox(-5.0F, -0.8333F, -13.0F, 11.0F, 10.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(23, 54).addBox(-2.0F, 4.1667F, -18.0F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -3.6667F, -4.0F, 0.6109F, 0.0F, 0.0F));

        PartDefinition earsLeft = head.addOrReplaceChild("earsLeft", CubeListBuilder.create().texOffs(0, 27).addBox(0.0F, -7.0F, 0.0F, 4.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.5F, -1.8333F, -9.0F, 0.2618F, 0.0F, 0.5236F));

        PartDefinition earsRight = head.addOrReplaceChild("earsRight", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -7.0F, 0.0F, 4.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, -1.8333F, -9.0F, 0.2618F, 0.0F, -0.5236F));

        PartDefinition leftLegBack = body.addOrReplaceChild("leftLegBack", CubeListBuilder.create().texOffs(63, 133).addBox(-0.5F, -2.5F, -2.5F, 5.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, 2.5F, 8.5F, -1.3523F, -0.1228F, -0.0984F));

        PartDefinition leftLegBack2 = leftLegBack.addOrReplaceChild("leftLegBack2", CubeListBuilder.create().texOffs(136, 131).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 13.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 8.5F, 0.0F, 2.138F, 0.0F, 0.0F));

        PartDefinition rightLegBack = body.addOrReplaceChild("rightLegBack", CubeListBuilder.create().texOffs(133, 31).addBox(-4.5F, -2.5F, -2.5F, 5.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, 2.5F, 8.5F, -1.3523F, 0.1228F, 0.0984F));

        PartDefinition rightLegBack2 = rightLegBack.addOrReplaceChild("rightLegBack2", CubeListBuilder.create().texOffs(99, 133).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 13.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 8.5F, 0.0F, 2.138F, 0.0F, 0.0F));

        PartDefinition leftLegFront = body.addOrReplaceChild("leftLegFront", CubeListBuilder.create().texOffs(116, 131).addBox(-0.5F, -2.0F, -2.5F, 5.0F, 12.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.5F, 2.0F, -11.5F, 0.7854F, 0.0F, 0.0F));

        PartDefinition leftLegFront2 = leftLegFront.addOrReplaceChild("leftLegFront2", CubeListBuilder.create().texOffs(20, 134).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 13.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 10.0F, 0.0F));

        PartDefinition rightLegFront = body.addOrReplaceChild("rightLegFront", CubeListBuilder.create().texOffs(0, 130).addBox(-4.5F, -2.0F, -2.5F, 5.0F, 12.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.5F, 2.0F, -11.5F, 0.7854F, 0.0F, 0.0F));

        PartDefinition rightLegFront2 = rightLegFront.addOrReplaceChild("rightLegFront2", CubeListBuilder.create().texOffs(83, 133).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 13.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 10.0F, 0.0F));

        PartDefinition tailBase = body.addOrReplaceChild("tailBase", CubeListBuilder.create().texOffs(110, 36).addBox(-3.0F, -3.0F, -2.0F, 6.0F, 6.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.5F, 1.0F, 14.0F, 0.9599F, 0.829F, 0.6109F));

        PartDefinition tail = tailBase.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(100, 18).addBox(-2.5F, -2.5F, -2.0F, 5.0F, 5.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 9.0F, 0.0F, 0.8727F, 0.3927F));

        PartDefinition tailTip = tail.addOrReplaceChild("tailTip", CubeListBuilder.create().texOffs(42, 125).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 11.0F, 0.1745F, 0.829F, 0.1745F));

        PartDefinition tailBase2 = body.addOrReplaceChild("tailBase2", CubeListBuilder.create().texOffs(0, 95).addBox(-3.0F, -3.0F, -2.0F, 6.0F, 6.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.5F, 0.0F, 14.0F, 1.5708F, -0.0873F, 0.6981F));

        PartDefinition tail2 = tailBase2.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(87, 51).addBox(-2.5F, -2.5F, -2.0F, 5.0F, 5.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 10.0F, 0.48F, -0.2182F, 0.0F));

        PartDefinition tailTip2 = tail2.addOrReplaceChild("tailTip2", CubeListBuilder.create().texOffs(21, 119).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 11.0F, 0.5236F, -0.2618F, 0.0436F));

        PartDefinition tailBase3 = body.addOrReplaceChild("tailBase3", CubeListBuilder.create().texOffs(82, 83).addBox(-3.0F, -3.0F, -2.0F, 6.0F, 6.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -1.0F, 14.0F, 1.309F, 0.0F, 0.0873F));

        PartDefinition tail3 = tailBase3.addOrReplaceChild("tail3", CubeListBuilder.create().texOffs(46, 83).addBox(-2.5F, -2.5F, -2.0F, 5.0F, 5.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 10.0F, 0.7854F, 0.0F, 0.2182F));

        PartDefinition tailTip3 = tail3.addOrReplaceChild("tailTip3", CubeListBuilder.create().texOffs(91, 118).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 11.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition tailBase4 = body.addOrReplaceChild("tailBase4", CubeListBuilder.create().texOffs(106, 101).addBox(-3.0F, -3.0F, -2.0F, 6.0F, 6.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 0.0F, 14.0F, 0.829F, 0.2182F, 0.2182F));

        PartDefinition tail4 = tailBase4.addOrReplaceChild("tail4", CubeListBuilder.create().texOffs(23, 77).addBox(-2.5F, -2.5F, -2.0F, 5.0F, 5.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 9.0F, 0.5236F, 0.0F, 0.0F));

        PartDefinition tailTip4 = tail4.addOrReplaceChild("tailTip4", CubeListBuilder.create().texOffs(123, 53).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 11.0F, 0.48F, 0.1309F, 0.0F));

        PartDefinition tailBase5 = body.addOrReplaceChild("tailBase5", CubeListBuilder.create().texOffs(80, 0).addBox(-3.0F, -3.0F, -2.0F, 6.0F, 6.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, 14.0F, 1.4835F, 0.0F, 0.0F));

        PartDefinition tail5 = tailBase5.addOrReplaceChild("tail5", CubeListBuilder.create().texOffs(77, 25).addBox(-2.5F, -2.5F, -2.0F, 5.0F, 5.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 10.0F, 0.829F, 0.0F, 0.0F));

        PartDefinition tailTip5 = tail5.addOrReplaceChild("tailTip5", CubeListBuilder.create().texOffs(118, 86).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 11.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition tailBase6 = body.addOrReplaceChild("tailBase6", CubeListBuilder.create().texOffs(106, 69).addBox(-3.0F, -3.0F, -2.0F, 6.0F, 6.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.5F, 1.0F, 14.0F, 0.9599F, -0.829F, -0.6109F));

        PartDefinition tail6 = tailBase6.addOrReplaceChild("tail6", CubeListBuilder.create().texOffs(0, 72).addBox(-2.5F, -2.5F, -2.0F, 5.0F, 5.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 9.0F, 0.0F, -0.8727F, -0.3927F));

        PartDefinition tailTip6 = tail6.addOrReplaceChild("tailTip6", CubeListBuilder.create().texOffs(123, 15).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 11.0F, 0.1745F, -0.829F, -0.1745F));

        PartDefinition tailBase7 = body.addOrReplaceChild("tailBase7", CubeListBuilder.create().texOffs(60, 65).addBox(-3.0F, -3.0F, -2.0F, 6.0F, 6.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5F, 0.0F, 14.0F, 1.5708F, 0.0873F, -0.6981F));

        PartDefinition tail7 = tailBase7.addOrReplaceChild("tail7", CubeListBuilder.create().texOffs(64, 46).addBox(-2.5F, -2.5F, -2.0F, 5.0F, 5.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 10.0F, 0.48F, 0.2182F, 0.0F));

        PartDefinition tailTip7 = tail7.addOrReplaceChild("tailTip7", CubeListBuilder.create().texOffs(61, 118).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 11.0F, 0.5236F, 0.2618F, -0.0436F));

        PartDefinition tailBase8 = body.addOrReplaceChild("tailBase8", CubeListBuilder.create().texOffs(36, 59).addBox(-3.0F, -3.0F, -2.0F, 6.0F, 6.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -1.0F, 14.0F, 1.309F, 0.0F, -0.0873F));

        PartDefinition tail8 = tailBase8.addOrReplaceChild("tail8", CubeListBuilder.create().texOffs(0, 54).addBox(-2.5F, -2.5F, -2.0F, 5.0F, 5.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 10.0F, 0.7854F, 0.0F, -0.2182F));

        PartDefinition tailTip8 = tail8.addOrReplaceChild("tailTip8", CubeListBuilder.create().texOffs(116, 0).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 11.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition tailBase9 = body.addOrReplaceChild("tailBase9", CubeListBuilder.create().texOffs(72, 101).addBox(-3.0F, -3.0F, -2.0F, 6.0F, 6.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 0.0F, 14.0F, 0.829F, -0.2182F, -0.2182F));

        PartDefinition tail9 = tailBase9.addOrReplaceChild("tail9", CubeListBuilder.create().texOffs(41, 41).addBox(-2.5F, -2.5F, -2.0F, 5.0F, 5.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 9.0F, 0.5236F, 0.0F, 0.0F));

        PartDefinition tailTip9 = tail9.addOrReplaceChild("tailTip9", CubeListBuilder.create().texOffs(121, 118).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 11.0F, 0.48F, -0.1309F, 0.0F));

        PartDefinition ridingPos = body.addOrReplaceChild("ridingPos", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -5.0F, -18.0F, 0.8727F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.model.getMainPart().render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.model.resetPoses();
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