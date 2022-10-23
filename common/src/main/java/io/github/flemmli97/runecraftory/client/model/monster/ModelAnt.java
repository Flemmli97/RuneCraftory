package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityAnt;
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
import net.minecraft.util.Mth;

public class ModelAnt<T extends EntityAnt> extends EntityModel<T> implements ExtendedModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(RuneCraftory.MODID, "ant"), "main");

    protected final ModelPartHandler model;
    protected final BlockBenchAnimations anim;

    public ModelPartHandler.ModelPartExtended head;

    public ModelAnt(ModelPart root) {
        super();
        this.model = new ModelPartHandler(root, "body");
        this.anim = AnimationManager.getInstance().getAnimation(new ResourceLocation(RuneCraftory.MODID, "ant"));
        this.head = this.model.getPart("head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(38, 14).mirror().addBox(-3.0F, -2.0F, -4.0F, 6.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 20.0F, -3.0F));

        PartDefinition bodyConnector = body.addOrReplaceChild("bodyConnector", CubeListBuilder.create().texOffs(54, 10).mirror().addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 3.5F));

        PartDefinition bodyBack = body.addOrReplaceChild("bodyBack", CubeListBuilder.create().texOffs(0, 26).mirror().addBox(-5.0F, -2.5F, 0.0F, 10.0F, 5.0F, 11.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(32, 0).mirror().addBox(-3.5F, -3.5F, 1.0F, 7.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 4.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-5.0F, -3.0F, -3.0F, 10.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, -4.0F));

        PartDefinition headFront = head.addOrReplaceChild("headFront", CubeListBuilder.create().texOffs(0, 9).mirror().addBox(-4.5F, -2.5F, -8.0F, 9.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition feelerLeft = headFront.addOrReplaceChild("feelerLeft", CubeListBuilder.create().texOffs(26, 0).mirror().addBox(-0.5F, -5.5F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.5F, -2.5F, -7.5F, 0.9599F, 0.0F, 0.0F));

        PartDefinition feelerRight = headFront.addOrReplaceChild("feelerRight", CubeListBuilder.create().texOffs(26, 0).addBox(-0.5F, -5.5F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5F, -2.5F, -7.5F, 0.9599F, 0.0F, 0.0F));

        PartDefinition jawLeft = headFront.addOrReplaceChild("jawLeft", CubeListBuilder.create().texOffs(38, 10).mirror().addBox(0.0F, -0.5F, -3.5F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, 1.5F, -7.5F, 0.0F, -0.3491F, 0.0F));

        PartDefinition jawRight = headFront.addOrReplaceChild("jawRight", CubeListBuilder.create().texOffs(38, 10).addBox(-2.0F, -0.5F, -3.5F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 1.5F, -7.5F, 0.0F, 0.3491F, 0.0F));

        PartDefinition leg1Up1 = body.addOrReplaceChild("leg1Up1", CubeListBuilder.create().texOffs(0, 19).mirror().addBox(-7.0F, -1.0F, -1.0F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, 1.0F, 1.5F, 0.0F, 0.6109F, 0.2618F));

        PartDefinition leg1Down1 = leg1Up1.addOrReplaceChild("leg1Down1", CubeListBuilder.create().texOffs(0, 23).mirror().addBox(-9.0F, -1.0F, -0.5F, 9.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-6.5F, 0.0F, 0.0F, 0.0F, 0.4363F, -0.6981F));

        PartDefinition leg1Up2 = body.addOrReplaceChild("leg1Up2", CubeListBuilder.create().texOffs(0, 19).mirror().addBox(-7.0F, -1.0F, -1.0F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

        PartDefinition leg1Down2 = leg1Up2.addOrReplaceChild("leg1Down2", CubeListBuilder.create().texOffs(0, 23).mirror().addBox(-9.0F, -1.0F, -0.5F, 9.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-6.5F, 0.0F, 0.0F, 0.0F, 0.1745F, -0.6981F));

        PartDefinition leg1Up3 = body.addOrReplaceChild("leg1Up3", CubeListBuilder.create().texOffs(0, 19).mirror().addBox(-7.0F, -1.0F, -1.0F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, 1.0F, -1.5F, 0.0F, -0.5236F, 0.3491F));

        PartDefinition leg1Down3 = leg1Up3.addOrReplaceChild("leg1Down3", CubeListBuilder.create().texOffs(0, 23).mirror().addBox(-9.0F, -1.0F, -0.5F, 9.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-6.5F, 0.0F, 0.0F, 0.0F, -0.0873F, -0.7854F));

        PartDefinition leg1Up4 = body.addOrReplaceChild("leg1Up4", CubeListBuilder.create().texOffs(0, 19).addBox(0.0F, -1.0F, -1.0F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 1.0F, -1.5F, 0.0F, 0.5236F, -0.3491F));

        PartDefinition leg1Down4 = leg1Up4.addOrReplaceChild("leg1Down4", CubeListBuilder.create().texOffs(0, 23).addBox(0.0F, -1.0F, -0.5F, 9.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.5F, 0.0F, 0.0F, 0.0F, -0.0873F, 0.7854F));

        PartDefinition leg1Up5 = body.addOrReplaceChild("leg1Up5", CubeListBuilder.create().texOffs(0, 19).addBox(0.0F, -1.0F, -1.0F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 1.0F, 0.0F, 0.0F, 0.0F, -0.2618F));

        PartDefinition leg1Down5 = leg1Up5.addOrReplaceChild("leg1Down5", CubeListBuilder.create().texOffs(0, 23).addBox(0.0F, -1.0F, -0.5F, 9.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.5F, 0.0F, 0.0F, 0.0F, -0.1745F, 0.6981F));

        PartDefinition leg1Up6 = body.addOrReplaceChild("leg1Up6", CubeListBuilder.create().texOffs(0, 19).addBox(0.0F, -1.0F, -1.0F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 1.0F, 1.5F, 0.0F, -0.6109F, -0.2618F));

        PartDefinition leg1Down6 = leg1Up6.addOrReplaceChild("leg1Down6", CubeListBuilder.create().texOffs(0, 23).addBox(0.0F, -1.0F, -0.5F, 9.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.5F, 0.0F, 0.0F, 0.0F, -0.4363F, 0.6981F));

        return LayerDefinition.create(meshdefinition, 64, 42);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.model.getMainPart().render(poseStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.model.resetPoses();
        this.head.yRot += netHeadYaw * Mth.DEG_TO_RAD * 0.5f;
        this.head.xRot += headPitch * Mth.DEG_TO_RAD * 0.5f;
        AnimatedAction anim = entity.getAnimationHandler().getAnimation();
        float partialTicks = Minecraft.getInstance().getFrameTime();
        if (entity.deathTime <= 0 && !entity.playDeath()) {
            this.anim.doAnimation(this, "iddle", entity.tickCount, partialTicks);
            if (entity.moveTick() > 0)
                this.anim.doAnimation(this, "walk", entity.tickCount, partialTicks, entity.interpolatedMoveTick(partialTicks));
        }
        if (anim != null)
            this.anim.doAnimation(this, anim.getAnimationClient(), anim.getTick(), partialTicks);
    }

    @Override
    public ModelPartHandler getHandler() {
        return this.model;
    }
}