package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.client.model.SittingModel;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityBigMuck;
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

public class ModelBigMuck<T extends EntityBigMuck> extends EntityModel<T> implements ExtendedModel, RideableModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(RuneCraftory.MODID, "big_muck"), "main");

    protected final ModelPartHandler model;
    protected final BlockBenchAnimations anim;

    public ModelPartHandler.ModelPartExtended body;
    public ModelPartHandler.ModelPartExtended cap;
    public ModelPartHandler.ModelPartExtended ridingPosition;

    public ModelBigMuck(ModelPart root) {
        super();
        this.model = new ModelPartHandler(root, "root");
        this.anim = AnimationManager.getInstance().getAnimation(new ResourceLocation(RuneCraftory.MODID, "big_muck"));
        this.body = this.model.getPart("body");
        this.cap = this.model.getPart("mushroomCap");
        this.ridingPosition = this.model.getPart("ridingPos");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -14.0F, -5.0F, 11.0F, 14.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(0, 25).addBox(-5.0F, -14.0F, -6.0F, 9.0F, 14.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(44, 0).addBox(5.0F, -14.0F, -4.0F, 1.0F, 14.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(44, 0).addBox(-7.0F, -14.0F, -4.0F, 1.0F, 14.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(20, 25).addBox(-5.0F, -14.0F, 6.0F, 9.0F, 14.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 24.0F, -0.5F));

        PartDefinition mushroomCap = body.addOrReplaceChild("mushroomCap", CubeListBuilder.create().texOffs(0, 48).addBox(-9.5F, -2.0F, -9.5F, 19.0F, 2.0F, 19.0F, new CubeDeformation(0.0F))
                .texOffs(0, 69).addBox(-7.5F, -4.0F, -7.5F, 15.0F, 2.0F, 15.0F, new CubeDeformation(0.0F))
                .texOffs(0, 86).addBox(-6.5F, -7.0F, -6.5F, 13.0F, 3.0F, 13.0F, new CubeDeformation(0.0F))
                .texOffs(0, 102).addBox(-5.5F, -10.0F, -5.5F, 11.0F, 3.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(0, 116).addBox(-3.5F, -12.0F, -3.5F, 7.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, -14.0F, 0.5F));

        PartDefinition ridingPos = mushroomCap.addOrReplaceChild("ridingPos", CubeListBuilder.create(), PartPose.offset(0.0F, -12.0F, 3.0F));

        PartDefinition handLeft = body.addOrReplaceChild("handLeft", CubeListBuilder.create().texOffs(0, 40).mirror().addBox(0.0F, -0.5F, -3.5F, 8.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(6.0F, -8.5F, 0.5F, 0.0F, 0.0F, 0.2618F));

        PartDefinition handRight = body.addOrReplaceChild("handRight", CubeListBuilder.create().texOffs(0, 40).addBox(-8.0F, -0.5F, -3.5F, 8.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, -8.5F, 0.5F, 0.0F, 0.0F, -0.2618F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(44, 23).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -1.0F, 7.0F, 0.6981F, 0.0F, 0.0F));

        PartDefinition tailCap = tail.addOrReplaceChild("tailCap", CubeListBuilder.create().texOffs(50, 23).addBox(-1.5F, -1.5F, 2.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 76, 125);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.model.getMainPart().render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.model.resetPoses();
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
                this.cap.translateAndRotate(poseStack);
                this.ridingPosition.translateAndRotate(poseStack);
                ClientHandlers.translateRider(entityRenderer, model, poseStack);
                return true;
            }
        }
        return false;
    }
}