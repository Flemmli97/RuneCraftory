package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityBuffamoo;
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

public class ModelBuffamoo<T extends EntityBuffamoo> extends EntityModel<T> implements ExtendedModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(RuneCraftory.MODID, "buffamoo"), "main");

    protected final ModelPartHandler model;
    protected final BlockBenchAnimations anim;

    public ModelPartHandler.ModelPartExtended head;

    public ModelBuffamoo(ModelPart root) {
        super();
        this.model = new ModelPartHandler(root, "body");
        this.anim = AnimationManager.getInstance().getAnimation(new ResourceLocation(RuneCraftory.MODID, "buffamoo"));
        this.head = this.model.getPart("head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -3.0F, -7.0F, 12.0F, 14.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, -1.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 35).addBox(-5.0F, -3.0F, -3.0F, 10.0F, 11.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -6.0F, 0.1745F, 0.0F, 0.0F));

        PartDefinition headFront = head.addOrReplaceChild("headFront", CubeListBuilder.create().texOffs(26, 34).addBox(-4.0F, -2.0F, -9.0F, 8.0F, 9.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(64, 20).addBox(-4.0F, 7.0F, -9.0F, 8.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -3.0F));

        PartDefinition hornLeft = head.addOrReplaceChild("hornLeft", CubeListBuilder.create().texOffs(0, 49).addBox(0.0F, -2.0F, -0.5F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 0.0F, -4.5F, 0.0F, 0.0F, -0.2618F));

        PartDefinition hornLeftTip = hornLeft.addOrReplaceChild("hornLeftTip", CubeListBuilder.create().texOffs(12, 49).addBox(0.0F, -2.0F, -1.0F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 0.0F, 1.0F, 0.0F, 0.0F, -0.9599F));

        PartDefinition hornRight = head.addOrReplaceChild("hornRight", CubeListBuilder.create().texOffs(0, 49).mirror().addBox(-4.0F, -2.0F, -0.5F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.0F, 0.0F, -4.5F, 0.0F, 0.0F, 0.2618F));

        PartDefinition hornRight2 = hornRight.addOrReplaceChild("hornRight2", CubeListBuilder.create().texOffs(12, 49).mirror().addBox(-5.0F, -2.0F, 0.0F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.9599F));

        PartDefinition udder = body.addOrReplaceChild("udder", CubeListBuilder.create().texOffs(65, 0).addBox(-2.0F, -19.0F, -4.0F, 4.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 30.0F, 10.0F));

        PartDefinition frontLegLeftUp = body.addOrReplaceChild("frontLegLeftUp", CubeListBuilder.create().texOffs(89, 0).mirror().addBox(-2.0F, -1.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(3.9F, 11.0F, -4.0F));

        PartDefinition frontLegLeftDown = frontLegLeftUp.addOrReplaceChild("frontLegLeftDown", CubeListBuilder.create().texOffs(89, 10).mirror().addBox(-2.0F, 0.0F, 0.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 4.0F, -2.0F));

        PartDefinition frontLegRightUp = body.addOrReplaceChild("frontLegRightUp", CubeListBuilder.create().texOffs(89, 0).mirror().addBox(-2.0F, -1.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.9F, 11.0F, -4.0F));

        PartDefinition frontLegRightDown = frontLegRightUp.addOrReplaceChild("frontLegRightDown", CubeListBuilder.create().texOffs(89, 10).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, -2.0F));

        PartDefinition backLegLeftUp = body.addOrReplaceChild("backLegLeftUp", CubeListBuilder.create().texOffs(105, 0).mirror().addBox(-2.0F, -1.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(3.9F, 11.0F, 9.0F));

        PartDefinition backLegLeftDown = backLegLeftUp.addOrReplaceChild("backLegLeftDown", CubeListBuilder.create().texOffs(105, 10).mirror().addBox(-2.0F, 0.0F, 0.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 4.0F, -2.0F));

        PartDefinition backLegRightUp = body.addOrReplaceChild("backLegRightUp", CubeListBuilder.create().texOffs(105, 0).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.9F, 11.0F, 9.0F));

        PartDefinition backLegRightDown = backLegRightUp.addOrReplaceChild("backLegRightDown", CubeListBuilder.create().texOffs(105, 10).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, -2.0F));

        PartDefinition topFur = body.addOrReplaceChild("topFur", CubeListBuilder.create().texOffs(60, 36).addBox(0.0F, -7.0F, -4.0F, 0.0F, 9.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, -7.0F));

        PartDefinition topFur2 = body.addOrReplaceChild("topFur2", CubeListBuilder.create().texOffs(74, 37).addBox(0.0F, -6.0F, -3.0F, 0.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, -1.0F));

        PartDefinition topFur3 = body.addOrReplaceChild("topFur3", CubeListBuilder.create().texOffs(86, 37).addBox(0.0F, -6.0F, -3.0F, 0.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, 5.0F));

        PartDefinition topFur4 = body.addOrReplaceChild("topFur4", CubeListBuilder.create().texOffs(98, 38).addBox(0.0F, -6.0F, -2.0F, 0.0F, 9.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, 10.0F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(64, 8).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 12.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition tail2 = tail.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(68, 8).addBox(-0.5F, -0.3F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, 0.5F, -0.1745F, 0.0F, 0.0F));

        PartDefinition tail3 = tail2.addOrReplaceChild("tail3", CubeListBuilder.create().texOffs(72, 8).addBox(-0.5F, -0.3F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.7F, 0.0F, -0.5236F, 0.0F, 0.0F));

        PartDefinition tailTip = tail3.addOrReplaceChild("tailTip", CubeListBuilder.create().texOffs(76, 8).addBox(-1.0F, -0.2F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.3F, 0.0F));

        return LayerDefinition.create(meshdefinition, 121, 55);
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