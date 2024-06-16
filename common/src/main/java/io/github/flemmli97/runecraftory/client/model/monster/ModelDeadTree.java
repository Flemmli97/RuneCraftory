package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.client.model.SittingModel;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.EntityDeadTree;
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

public class ModelDeadTree<T extends EntityDeadTree> extends EntityModel<T> implements ExtendedModel, RideableModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(RuneCraftory.MODID, "dead_tree"), "main");

    protected final ModelPartHandler model;
    protected final BlockBenchAnimations anim;

    public ModelPartHandler.ModelPartExtended body;
    public ModelPartHandler.ModelPartExtended leafs;
    public ModelPartHandler.ModelPartExtended ridingPosition;

    public ModelDeadTree(ModelPart root) {
        super();
        this.model = new ModelPartHandler(root, "root");
        this.anim = AnimationManager.getInstance().getAnimation(new ResourceLocation(RuneCraftory.MODID, "dead_tree"));
        this.body = this.model.getPart("body");
        this.leafs = this.model.getPart("leafs");
        this.ridingPosition = this.model.getPart("ridingPos");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(64, 56).addBox(-6.0F, -19.0F, -6.0F, 12.0F, 28.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(0, 88).addBox(-7.0F, 8.0F, -7.0F, 14.0F, 2.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 14.0F, 0.0F));

        PartDefinition root1 = body.addOrReplaceChild("root1", CubeListBuilder.create().texOffs(97, 102).addBox(-6.0F, -1.75F, -1.25F, 6.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(6, 6).addBox(-8.0F, -0.75F, -0.75F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, 8.75F, 5.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition root2 = body.addOrReplaceChild("root2", CubeListBuilder.create().texOffs(100, 96).addBox(0.0F, -1.75F, -1.5F, 6.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 20).addBox(6.0F, -0.75F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 8.75F, -5.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition root3 = body.addOrReplaceChild("root3", CubeListBuilder.create().texOffs(100, 62).addBox(0.0F, -1.75F, -1.5F, 6.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 10).addBox(6.0F, -0.75F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(8, 10).addBox(2.0F, -5.75F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 3).addBox(3.5F, -5.75F, -1.5F, 0.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 8.75F, 5.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition root4 = body.addOrReplaceChild("root4", CubeListBuilder.create().texOffs(100, 56).addBox(-6.0F, -1.75F, -1.5F, 6.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(6, 22).addBox(-8.0F, -0.75F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, 8.75F, -5.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition leafs = body.addOrReplaceChild("leafs", CubeListBuilder.create().texOffs(0, 0).addBox(-20.0F, -16.0F, -20.0F, 40.0F, 16.0F, 40.0F, new CubeDeformation(0.0F))
                .texOffs(0, 56).addBox(-8.0F, -31.9F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(14.0F, 0.0F, -19.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(13.0F, 0.0F, -2.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(10.0F, 0.0F, 10.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(7.0F, 0.0F, 15.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-13.0F, 0.0F, 13.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-17.0F, 0.0F, 3.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-2.0F, 0.0F, 16.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-5.0F, 0.0F, 10.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-17.0F, 0.0F, -19.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-21.0F, 0.0F, -10.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-22.0F, -9.0F, -10.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-22.0F, -13.0F, 12.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(11.0F, -9.0F, -22.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(19.0F, -12.0F, -11.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(19.0F, -7.0F, 7.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-2.0F, -11.0F, 19.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -15.0F, 0.0F));

        PartDefinition ridingPos = leafs.addOrReplaceChild("ridingPos", CubeListBuilder.create(), PartPose.offset(0.0F, -32.0F, 3.0F));

        PartDefinition armLeft = body.addOrReplaceChild("armLeft", CubeListBuilder.create().texOffs(0, 20).addBox(0.0F, -3.0F, -10.0F, 6.0F, 6.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, -11.0F, 2.0F, 0.0698F, -0.8727F, 0.1745F));

        PartDefinition armLeftMiddle = armLeft.addOrReplaceChild("armLeftMiddle", CubeListBuilder.create().texOffs(62, 96).addBox(-1.5F, -1.5F, -14.0F, 3.0F, 3.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 0.0F, -10.0F, 0.3491F, 0.5236F, 0.0F));

        PartDefinition handLeft = armLeftMiddle.addOrReplaceChild("handLeft", CubeListBuilder.create().texOffs(82, 96).addBox(-2.5F, -2.5F, -4.0F, 5.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -13.9F));

        PartDefinition armRight = body.addOrReplaceChild("armRight", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -3.0F, -10.0F, 6.0F, 6.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, -11.0F, 2.0F, 0.0698F, 0.8727F, -0.1745F));

        PartDefinition armRightMiddle = armRight.addOrReplaceChild("armRightMiddle", CubeListBuilder.create().texOffs(42, 90).addBox(-1.5F, -1.5F, -14.0F, 3.0F, 3.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 0.0F, -10.0F, 0.3491F, -0.5236F, 0.0F));

        PartDefinition handRight = armRightMiddle.addOrReplaceChild("handRight", CubeListBuilder.create().texOffs(48, 56).addBox(-2.5F, -2.5F, -4.0F, 5.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -13.9F));

        return LayerDefinition.create(meshdefinition, 256, 256);
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
            this.anim.doAnimation(this, "idle", entity.tickCount, partialTicks);
            if (entity.moveTick() > 0)
                this.anim.doAnimation(this, "walk", entity.tickCount, partialTicks, entity.interpolatedMoveTick(partialTicks));
        }
        if (anim != null) {
            String s = anim.getAnimationClient();
            if (s.equals("summon"))
                s += "_" + (entity.summonAnimationType() + 1);
            this.anim.doAnimation(this, s, anim.getTick(), partialTicks, entity.getAnimationHandler().getInterpolatedAnimationVal(partialTicks));
        }
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
                this.leafs.translateAndRotate(poseStack);
                this.ridingPosition.translateAndRotate(poseStack);
                ClientHandlers.translateRider(entityRenderer, model, poseStack);
                return true;
            }
        }
        return false;
    }
}