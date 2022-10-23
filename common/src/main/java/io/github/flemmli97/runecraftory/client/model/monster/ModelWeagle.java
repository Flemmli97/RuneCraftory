package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityWeagle;
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

public class ModelWeagle<T extends EntityWeagle> extends EntityModel<T> implements ExtendedModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(RuneCraftory.MODID, "weagle"), "main");

    protected final ModelPartHandler model;
    protected final BlockBenchAnimations anim;

    public ModelPartHandler.ModelPartExtended head;

    public ModelWeagle(ModelPart root) {
        super();
        this.model = new ModelPartHandler(root, "body");
        this.anim = AnimationManager.getInstance().getAnimation(new ResourceLocation(RuneCraftory.MODID, "weagle"));
        this.head = this.model.getPart("head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -1.0F, -4.0F, 8.0F, 8.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(60, 22).addBox(4.0F, 0.0F, -3.0F, 1.0F, 6.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(36, 22).addBox(-5.0F, 0.0F, -3.0F, 1.0F, 6.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 9.0F, 2.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 39).addBox(-3.0F, -2.0F, -6.0F, 6.0F, 6.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(42, 52).addBox(0.0F, -5.0F, -6.0F, 0.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(12, 61).addBox(0.0F, 4.0F, -5.0F, 0.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(22, 63).addBox(-2.0F, -1.0F, -7.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(46, 61).addBox(-1.0F, 0.25F, -8.5F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, -4.0F));

        PartDefinition leftWing = body.addOrReplaceChild("leftWing", CubeListBuilder.create().texOffs(46, 66).addBox(0.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(20, 52).addBox(0.0F, 0.0F, 0.0F, 4.0F, 0.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 52).addBox(0.0F, 0.2F, 0.0F, 4.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(4.7F, 2.5F, -1.5F));

        PartDefinition leftWing2 = leftWing.addOrReplaceChild("leftWing2", CubeListBuilder.create().texOffs(22, 61).addBox(0.0F, -1.0F, 0.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(54, 39).addBox(0.0F, -0.505F, 0.5F, 6.0F, 0.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(54, 39).addBox(0.0F, -0.305F, 0.5F, 6.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(3.7F, 0.5F, -0.5F));

        PartDefinition leftWing3 = leftWing2.addOrReplaceChild("leftWing3", CubeListBuilder.create().texOffs(20, 58).addBox(0.0F, 0.0F, 0.0F, 10.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 22).addBox(0.0F, 0.49F, 0.5F, 10.0F, 0.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(44, 0).addBox(0.0F, 0.69F, 0.5F, 10.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(5.7F, -1.0F, 0.0F));

        PartDefinition rightWing = body.addOrReplaceChild("rightWing", CubeListBuilder.create().texOffs(46, 66).mirror().addBox(-4.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(20, 52).mirror().addBox(-4.0F, 0.0F, 0.0F, 4.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 52).mirror().addBox(-4.0F, 0.2F, 0.0F, 4.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-4.7F, 2.5F, -1.5F));

        PartDefinition rightWing2 = rightWing.addOrReplaceChild("rightWing2", CubeListBuilder.create().texOffs(22, 61).mirror().addBox(-6.0F, -1.0F, 0.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(54, 39).mirror().addBox(-6.0F, -0.505F, 0.5F, 6.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(26, 39).addBox(-6.0F, -0.305F, 0.5F, 6.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.7F, 0.5F, -0.5F));

        PartDefinition rightWing3 = rightWing2.addOrReplaceChild("rightWing3", CubeListBuilder.create().texOffs(20, 58).mirror().addBox(-10.0F, 0.0F, 0.0F, 10.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 22).mirror().addBox(-10.0F, 0.49F, 0.5F, 10.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(44, 0).mirror().addBox(-10.0F, 0.69F, 0.5F, 10.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-5.7F, -1.0F, 0.0F));

        PartDefinition leftLeg = body.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(0, 61).addBox(-1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(36, 61).addBox(-2.5F, -1.0F, 3.5F, 5.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, 6.0F, 8.5F));

        PartDefinition rightLeg = body.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(54, 52).addBox(-1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(36, 61).mirror().addBox(-2.5F, -1.0F, 3.5F, 5.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.5F, 6.0F, 8.5F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.model.getMainPart().render(poseStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.model.resetPoses();
        this.head.yRot += netHeadYaw * Mth.DEG_TO_RAD * 0.3f;
        this.head.xRot += headPitch * Mth.DEG_TO_RAD * 0.1f;
        AnimatedAction anim = entity.getAnimationHandler().getAnimation();
        float partialTicks = Minecraft.getInstance().getFrameTime();
        if (anim == null) {
            if (entity.deathTime <= 0 && !entity.playDeath()) {
                if (entity.moveTick() > 0) {
                    this.anim.doAnimation(this, "fly", entity.tickCount, partialTicks, entity.interpolatedMoveTick(partialTicks));
                } else
                    this.anim.doAnimation(this, "iddle", entity.tickCount, partialTicks);
            }
        } else
            this.anim.doAnimation(this, anim.getAnimationClient(), anim.getTick(), partialTicks);
    }

    @Override
    public ModelPartHandler getHandler() {
        return this.model;
    }
}