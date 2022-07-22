package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityDuck;
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

public class ModelDuck<T extends EntityDuck> extends EntityModel<T> implements ExtendedModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(RuneCraftory.MODID, "duck"), "main");

    protected final ModelPartHandler model;
    protected final BlockBenchAnimations anim;

    public ModelDuck(ModelPart root) {
        super();
        this.model = new ModelPartHandler(root, "body");
        this.anim = AnimationManager.getInstance().getAnimation(new ResourceLocation(RuneCraftory.MODID, "duck"));
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-6.5F, -12.0F, -6.5F, 13.0F, 24.0F, 13.0F, new CubeDeformation(0.0F))
                .texOffs(40, 25).addBox(-7.01F, -6.01F, -6.5F, 1.0F, 3.0F, 13.0F, new CubeDeformation(0.0F))
                .texOffs(24, 38).addBox(6.01F, -6.01F, -6.5F, 1.0F, 3.0F, 13.0F, new CubeDeformation(0.0F))
                .texOffs(53, 12).addBox(-7.0F, -6.0F, 6.0F, 14.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 38).addBox(-4.5F, -14.0F, -4.5F, 9.0F, 2.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(56, 26).addBox(-3.5F, -5.0F, -10.5F, 7.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 11.5F, 0.0F));

        PartDefinition tailBase = body.addOrReplaceChild("tailBase", CubeListBuilder.create().texOffs(40, 0).addBox(-5.5F, -8.0F, 0.0F, 11.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.0F, 6.5F, 0.0436F, 0.0F, 0.0F));

        PartDefinition tailMiddle = tailBase.addOrReplaceChild("tailMiddle", CubeListBuilder.create().texOffs(56, 17).addBox(-4.5F, -6.0F, 0.0F, 9.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, 3.0F, 0.0436F, 0.0F, 0.0F));

        PartDefinition tailTip = tailMiddle.addOrReplaceChild("tailTip", CubeListBuilder.create().texOffs(0, 60).addBox(-3.5F, -7.0F, 0.0F, 7.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.0F, 2.0F, -0.6981F, 0.0F, 0.0F));

        PartDefinition hair = body.addOrReplaceChild("hair", CubeListBuilder.create().texOffs(56, 42).addBox(-4.5F, -7.0F, 0.0F, 9.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -14.0F, -4.5F, -0.6545F, 0.0F, 0.0F));

        PartDefinition leftWing = body.addOrReplaceChild("leftWing", CubeListBuilder.create().texOffs(44, 46).addBox(0.0F, 0.0F, -4.5F, 1.0F, 13.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(6.5F, -2.0F, 0.0F));

        PartDefinition rightWing = body.addOrReplaceChild("rightWing", CubeListBuilder.create().texOffs(44, 46).mirror().addBox(-1.0F, 0.0F, -4.5F, 1.0F, 13.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-6.5F, -2.0F, 0.0F));

        PartDefinition leftFeet = body.addOrReplaceChild("leftFeet", CubeListBuilder.create().texOffs(17, 55).addBox(-2.0F, -0.5F, -8.0F, 4.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(3.25F, 12.0F, -2.5F));

        PartDefinition rightFeet = body.addOrReplaceChild("rightFeet", CubeListBuilder.create().texOffs(0, 50).addBox(-2.0F, -0.5F, -8.0F, 4.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.25F, 12.0F, -2.5F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.scale(0.85f, 0.85f, 0.85f);
        poseStack.translate(0, 0.26, 0);
        this.model.getMainPart().render(poseStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.model.resetPoses();
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