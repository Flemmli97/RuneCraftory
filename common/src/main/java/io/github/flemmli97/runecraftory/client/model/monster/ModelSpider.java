package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.monster.EntitySpider;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.client.AnimationManager;
import io.github.flemmli97.tenshilib.client.model.BlockBenchAnimations;
import io.github.flemmli97.tenshilib.client.model.ExtendedModel;
import io.github.flemmli97.tenshilib.client.model.ModelPartHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

public class ModelSpider<T extends EntitySpider> extends EntityModel<T> implements ExtendedModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(RuneCraftory.MODID, "spider"), "main");

    protected final ModelPartHandler model;
    protected final BlockBenchAnimations anim;

    public ModelSpider(ModelPart root) {
        super();
        this.model = new ModelPartHandler(root, "body");
        this.anim = AnimationManager.getInstance().getAnimation(new ResourceLocation(RuneCraftory.MODID, "spider"));
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 28).addBox(-3.5F, -2.5F, -3.5F, 7.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 17.0F, -2.5F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(27, 21).addBox(-4.0F, -2.5F, -7.0F, 8.0F, 7.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 17).addBox(-2.25F, 0.0F, -7.25F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 14).addBox(0.25F, 0.0F, -7.25F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 3).addBox(1.25F, 3.0F, -7.75F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-2.75F, 3.0F, -7.75F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, -2.5F));

        PartDefinition abdomen = body.addOrReplaceChild("abdomen", CubeListBuilder.create().texOffs(0, 14).addBox(-5.5F, -4.0F, 0.0F, 11.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 2.5F));

        PartDefinition abdomen2 = abdomen.addOrReplaceChild("abdomen2", CubeListBuilder.create().texOffs(0, 0).addBox(-5.5F, -8.0F, 0.0F, 11.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 6.0F));

        PartDefinition abdomen3 = abdomen2.addOrReplaceChild("abdomen3", CubeListBuilder.create().texOffs(32, 12).addBox(-4.5F, -6.0F, 0.0F, 9.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 6.0F));

        PartDefinition leg = body.addOrReplaceChild("leg", CubeListBuilder.create().texOffs(27, 39).addBox(0.0F, -1.0F, -0.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 2.0F, -2.0F, 0.0F, 0.3054F, -0.4363F));

        PartDefinition legMiddle = leg.addOrReplaceChild("legMiddle", CubeListBuilder.create().texOffs(28, 37).addBox(0.0F, 0.0F, -0.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, -1.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition legTip = legMiddle.addOrReplaceChild("legTip", CubeListBuilder.create().texOffs(28, 4).addBox(0.0F, 0.0F, -0.5F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6981F));

        PartDefinition leg2 = body.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(28, 35).addBox(0.0F, -1.0F, -0.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 2.0F, 0.0F, 0.0F, 0.0F, -0.4363F));

        PartDefinition legMiddle2 = leg2.addOrReplaceChild("legMiddle2", CubeListBuilder.create().texOffs(34, 10).addBox(0.0F, 0.0F, -0.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, -1.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition legTip2 = legMiddle2.addOrReplaceChild("legTip2", CubeListBuilder.create().texOffs(28, 2).addBox(0.0F, 0.0F, -0.5F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition leg3 = body.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(34, 8).addBox(0.0F, -1.0F, -0.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 2.0F, 2.0F, 0.0F, -0.3054F, -0.48F));

        PartDefinition legMiddle3 = leg3.addOrReplaceChild("legMiddle3", CubeListBuilder.create().texOffs(34, 6).addBox(0.0F, 0.0F, -0.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, -1.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition legTip3 = legMiddle3.addOrReplaceChild("legTip3", CubeListBuilder.create().texOffs(28, 0).addBox(0.0F, 0.0F, -0.5F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.829F));

        PartDefinition leg4 = body.addOrReplaceChild("leg4", CubeListBuilder.create().texOffs(27, 39).mirror().addBox(-6.0F, -1.0F, -0.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, 2.0F, -2.0F, 0.0F, -0.3054F, 0.4363F));

        PartDefinition legMiddle4 = leg4.addOrReplaceChild("legMiddle4", CubeListBuilder.create().texOffs(28, 37).mirror().addBox(-6.0F, 0.0F, -0.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-6.0F, -1.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition legTip4 = legMiddle4.addOrReplaceChild("legTip4", CubeListBuilder.create().texOffs(28, 4).mirror().addBox(-7.0F, 0.0F, -0.5F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-6.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.6981F));

        PartDefinition leg5 = body.addOrReplaceChild("leg5", CubeListBuilder.create().texOffs(28, 35).mirror().addBox(-6.0F, -1.0F, -0.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.4363F));

        PartDefinition legMiddle5 = leg5.addOrReplaceChild("legMiddle5", CubeListBuilder.create().texOffs(34, 10).mirror().addBox(-6.0F, 0.0F, -0.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-6.0F, -1.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition legTip5 = legMiddle5.addOrReplaceChild("legTip5", CubeListBuilder.create().texOffs(28, 2).mirror().addBox(-7.0F, 0.0F, -0.5F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-6.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition leg6 = body.addOrReplaceChild("leg6", CubeListBuilder.create().texOffs(34, 8).mirror().addBox(-6.0F, -1.0F, -0.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, 2.0F, 2.0F, 0.0F, 0.3054F, 0.48F));

        PartDefinition legMiddle6 = leg6.addOrReplaceChild("legMiddle6", CubeListBuilder.create().texOffs(34, 6).mirror().addBox(-6.0F, 0.0F, -0.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-6.0F, -1.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition legTip6 = legMiddle6.addOrReplaceChild("legTip6", CubeListBuilder.create().texOffs(28, 0).mirror().addBox(-7.0F, 0.0F, -0.5F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-6.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.829F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.model.getMainPart().render(poseStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.model.resetPoses();
        AnimatedAction anim = entity.getAnimationHandler().getAnimation();
        float partialTicks = Minecraft.getInstance().getFrameTime();
        if (entity.deathTime <= 0 && !entity.playDeath()) {
            if (entity.moveTick() > 0)
                this.anim.doAnimation(this, "walk", entity.tickCount, partialTicks, entity.interpolatedMoveTick());
        }
        if (anim != null)
            this.anim.doAnimation(this, anim.getAnimationClient(), anim.getTick(), partialTicks);
    }

    @Override
    public ModelPartHandler getHandler() {
        return this.model;
    }
}