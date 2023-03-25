package io.github.flemmli97.runecraftory.client.model.monster;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.SittingModel;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityHornet;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
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
import net.minecraft.world.entity.Entity;

public class ModelHornet<T extends EntityHornet> extends EntityModel<T> implements ExtendedModel, RideableModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(RuneCraftory.MODID, "hornet"), "main");

    protected final ModelPartHandler model;
    protected final BlockBenchAnimations anim;

    public ModelPartHandler.ModelPartExtended body;

    public ModelHornet(ModelPart root) {
        super();
        this.model = new ModelPartHandler(root, "root");
        this.anim = AnimationManager.getInstance().getAnimation(new ResourceLocation(RuneCraftory.MODID, "hornet"));
        this.body = this.model.getPart("body");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 8).addBox(-3.0F, -3.5F, -5.0F, 6.0F, 6.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 14.5F, -2.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 23).addBox(-2.0F, 0.0F, -4.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, -5.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition jawLeft = head.addOrReplaceChild("jawLeft", CubeListBuilder.create().texOffs(28, 43).addBox(-1.0F, 0.0F, -2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 4.0F, -4.0F, 0.3927F, 0.3491F, 0.0F));

        PartDefinition jawRight = head.addOrReplaceChild("jawRight", CubeListBuilder.create().texOffs(22, 43).addBox(0.0F, 0.0F, -2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 4.0F, -4.0F, 0.3927F, -0.3491F, 0.0F));

        PartDefinition leftWing = body.addOrReplaceChild("leftWing", CubeListBuilder.create().texOffs(40, 0).addBox(0.0F, 0.0F, 0.0F, 12.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -3.5F, -4.0F));

        PartDefinition rightWing = body.addOrReplaceChild("rightWing", CubeListBuilder.create().texOffs(0, 0).addBox(-12.0F, 0.0F, 0.0F, 12.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -3.5F, -4.0F));

        PartDefinition connector = body.addOrReplaceChild("connector", CubeListBuilder.create().texOffs(16, 43).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 4.0F, -0.3491F, 0.0F, 0.0F));

        PartDefinition back = connector.addOrReplaceChild("back", CubeListBuilder.create().texOffs(30, 8).addBox(-3.0F, -2.0F, 0.0F, 6.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, -0.0436F, 0.0F, 0.0F));

        PartDefinition back2 = back.addOrReplaceChild("back2", CubeListBuilder.create().texOffs(16, 23).addBox(-2.5F, 0.0F, -1.0F, 5.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.5F, 5.0F, -0.1745F, 0.0F, 0.0F));

        PartDefinition back3 = back2.addOrReplaceChild("back3", CubeListBuilder.create().texOffs(32, 23).addBox(-2.0F, 0.0F, -1.0F, 4.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, 2.0F, -0.1745F, 0.0F, 0.0F));

        PartDefinition stinger = back3.addOrReplaceChild("stinger", CubeListBuilder.create().texOffs(10, 43).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.5F, 2.0F));

        PartDefinition leg = body.addOrReplaceChild("leg", CubeListBuilder.create().texOffs(0, 36).addBox(0.0F, -2.0F, -2.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 2.5F, -2.5F, 0.0F, 0.0F, -0.2618F));

        PartDefinition legMiddle = leg.addOrReplaceChild("legMiddle", CubeListBuilder.create().texOffs(0, 40).addBox(0.0F, -1.0F, -1.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -1.0F, 0.0F, 0.0F, 0.0F, 1.9199F));

        PartDefinition feet = legMiddle.addOrReplaceChild("feet", CubeListBuilder.create().texOffs(0, 43).addBox(0.0F, -1.0F, -1.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 0.5F, 0.0F, 0.0F, 0.0F, -0.3927F));

        PartDefinition leg2 = body.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(48, 32).addBox(0.0F, -2.0F, -2.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 2.5F, 0.5F, 0.0F, 0.0F, -0.2618F));

        PartDefinition legMiddle2 = leg2.addOrReplaceChild("legMiddle2", CubeListBuilder.create().texOffs(52, 36).addBox(0.0F, -1.0F, -1.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -1.0F, 0.0F, 0.0F, 0.0F, 1.9199F));

        PartDefinition feet2 = legMiddle2.addOrReplaceChild("feet2", CubeListBuilder.create().texOffs(50, 40).addBox(0.0F, -1.0F, -1.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 0.5F, 0.0F, 0.0F, 0.0F, -0.3927F));

        PartDefinition leg3 = body.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(36, 32).addBox(0.0F, -2.0F, -2.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 2.5F, 3.5F, 0.0F, 0.0F, -0.2618F));

        PartDefinition legMiddle3 = leg3.addOrReplaceChild("legMiddle3", CubeListBuilder.create().texOffs(42, 36).addBox(0.0F, -1.0F, -1.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -1.0F, 0.0F, 0.0F, 0.0F, 1.9199F));

        PartDefinition feet3 = legMiddle3.addOrReplaceChild("feet3", CubeListBuilder.create().texOffs(40, 40).addBox(0.0F, -1.0F, -1.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 0.5F, 0.0F, 0.0F, 0.0F, -0.3927F));

        PartDefinition leg4 = body.addOrReplaceChild("leg4", CubeListBuilder.create().texOffs(24, 32).addBox(-4.0F, -2.0F, -2.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 2.5F, -2.5F, 0.0F, 0.0F, 0.2618F));

        PartDefinition legMiddle4 = leg4.addOrReplaceChild("legMiddle4", CubeListBuilder.create().texOffs(32, 36).addBox(-4.0F, -1.0F, -1.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -1.0F, 0.0F, 0.0F, 0.0F, -1.9199F));

        PartDefinition feet4 = legMiddle4.addOrReplaceChild("feet4", CubeListBuilder.create().texOffs(30, 40).addBox(-4.0F, -1.0F, -1.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 0.5F, 0.0F, 0.0F, 0.0F, 0.3927F));

        PartDefinition leg5 = body.addOrReplaceChild("leg5", CubeListBuilder.create().texOffs(12, 32).addBox(-4.0F, -2.0F, -2.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 2.5F, 0.5F, 0.0F, 0.0F, 0.2618F));

        PartDefinition legMiddle5 = leg5.addOrReplaceChild("legMiddle5", CubeListBuilder.create().texOffs(22, 36).addBox(-4.0F, -1.0F, -1.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -1.0F, 0.0F, 0.0F, 0.0F, -1.9199F));

        PartDefinition feet5 = legMiddle5.addOrReplaceChild("feet5", CubeListBuilder.create().texOffs(20, 40).addBox(-4.0F, -1.0F, -1.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 0.5F, 0.0F, 0.0F, 0.0F, 0.3927F));

        PartDefinition leg6 = body.addOrReplaceChild("leg6", CubeListBuilder.create().texOffs(0, 32).addBox(-4.0F, -2.0F, -2.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 2.5F, 3.5F, 0.0F, 0.0F, 0.2618F));

        PartDefinition legMiddle6 = leg6.addOrReplaceChild("legMiddle6", CubeListBuilder.create().texOffs(12, 36).addBox(-4.0F, -1.0F, -1.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -1.0F, 0.0F, 0.0F, 0.0F, -1.9199F));

        PartDefinition feet6 = legMiddle6.addOrReplaceChild("feet6", CubeListBuilder.create().texOffs(10, 40).addBox(-4.0F, -1.0F, -1.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 0.5F, 0.0F, 0.0F, 0.0F, 0.3927F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.model.getMainPart().render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.model.resetPoses();
        AnimatedAction anim = entity.getAnimationHandler().getAnimation();
        float partialTicks = Minecraft.getInstance().getFrameTime();
        if (entity.deathTime <= 0 && !entity.playDeath()) {
            this.anim.doAnimation(this, "iddle", entity.tickCount, partialTicks);
        }
        if (anim != null)
            this.anim.doAnimation(this, anim.getAnimationClient(), anim.getTick(), partialTicks, entity.getAnimationHandler().getInterpolatedAnimationVal(partialTicks));
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
                if (model instanceof SittingModel sittingModel)
                    sittingModel.translateSittingPosition(poseStack);
                else
                    poseStack.translate(0, 6 / 16d, 7 / 16d);
                return true;
            }
        }
        return false;
    }
}
