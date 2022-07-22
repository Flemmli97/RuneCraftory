package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityChipsqueek;
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

public class ModelChipsqueek<T extends EntityChipsqueek> extends EntityModel<T> implements ExtendedModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(RuneCraftory.MODID, "chipsqueek"), "main");

    protected final ModelPartHandler model;
    protected final BlockBenchAnimations anim;

    public ModelPartHandler.ModelPartExtended head;

    public ModelChipsqueek(ModelPart root) {
        super();
        this.model = new ModelPartHandler(root, "body");
        this.anim = AnimationManager.getInstance().getAnimation(new ResourceLocation(RuneCraftory.MODID, "chipsqueek"));
        this.head = this.model.getPart("head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 12).addBox(-3.5F, -4.0F, -4.5F, 7.0F, 7.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 16.8F, 1.5F, -0.5236F, 0.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -4.0F, -5.5F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.5F, -4.0F, 0.6109F, 0.0F, 0.0F));

        PartDefinition earLeft = head.addOrReplaceChild("earLeft", CubeListBuilder.create().texOffs(24, 0).addBox(-0.5F, -3.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, -2.5F, -2.0F));

        PartDefinition earRight = head.addOrReplaceChild("earRight", CubeListBuilder.create().texOffs(24, 0).mirror().addBox(-0.5F, -3.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.5F, -2.5F, -2.0F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(32, 0).addBox(-2.5F, -10.5F, 0.0F, 5.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 4.5F, -0.1745F, 0.0F, 0.0F));

        PartDefinition armLeft = body.addOrReplaceChild("armLeft", CubeListBuilder.create().texOffs(32, 14).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.5F, 0.5F, -2.5F, 0.0F, 0.0F, 0.1745F));

        PartDefinition armLeftDown = armLeft.addOrReplaceChild("armLeftDown", CubeListBuilder.create().texOffs(32, 20).addBox(-0.5F, 0.5F, -2.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.5F, 1.0F, 0.0F, 0.0F, 0.5236F));

        PartDefinition armRight = body.addOrReplaceChild("armRight", CubeListBuilder.create().texOffs(32, 14).mirror().addBox(-0.5F, 0.0F, -1.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.5F, 0.5F, -2.5F, 0.0F, 0.0F, -0.1745F));

        PartDefinition armRightDown = armRight.addOrReplaceChild("armRightDown", CubeListBuilder.create().texOffs(32, 20).mirror().addBox(-0.5F, 0.5F, -2.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 2.5F, 1.0F, 0.0F, 0.0F, -0.5236F));

        PartDefinition legLeftBase = body.addOrReplaceChild("legLeftBase", CubeListBuilder.create().texOffs(46, 0).addBox(0.0F, -1.75F, -1.25F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 1.75F, 2.75F, 0.5236F, 0.0F, 0.0F));

        PartDefinition legLeft = legLeftBase.addOrReplaceChild("legLeft", CubeListBuilder.create().texOffs(46, 7).addBox(-1.0F, -0.5F, -2.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 2.75F, 1.75F));

        PartDefinition feetLeft = legLeft.addOrReplaceChild("feetLeft", CubeListBuilder.create().texOffs(46, 11).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, -2.0F));

        PartDefinition legRightBase = body.addOrReplaceChild("legRightBase", CubeListBuilder.create().texOffs(46, 0).mirror().addBox(0.0F, -1.75F, -1.25F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.0F, 1.75F, 2.75F, 0.5236F, 0.0F, 0.0F));

        PartDefinition legRight = legRightBase.addOrReplaceChild("legRight", CubeListBuilder.create().texOffs(46, 7).mirror().addBox(-1.0F, -0.5F, -2.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.0F, 2.75F, 1.75F));

        PartDefinition feetRight = legRight.addOrReplaceChild("feetRight", CubeListBuilder.create().texOffs(46, 11).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, -2.0F));

        return LayerDefinition.create(meshdefinition, 64, 28);
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
        if (entity.deathTime <= 0) {
            this.anim.doAnimation(this, "iddle", entity.tickCount, partialTicks);
            if (entity.isMoving())
                this.anim.doAnimation(this, "walk", entity.tickCount, partialTicks);
        }
        if (anim != null)
            this.anim.doAnimation(this, anim.getAnimationClient(), anim.getTick(), partialTicks);
    }

    @Override
    public ModelPartHandler getHandler() {
        return this.model;
    }
}