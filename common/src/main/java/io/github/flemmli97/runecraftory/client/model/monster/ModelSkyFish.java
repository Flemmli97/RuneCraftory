package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.client.model.SittingModel;
import io.github.flemmli97.runecraftory.common.entities.monster.EntitySkyFish;
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

public class ModelSkyFish<T extends EntitySkyFish> extends EntityModel<T> implements ExtendedModel, RideableModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(RuneCraftory.MODID, "sky_fish"), "main");

    protected final ModelPartHandler model;
    protected final BlockBenchAnimations anim;

    public ModelPartHandler.ModelPartExtended body;
    public ModelPartHandler.ModelPartExtended ridingPosition;

    public ModelSkyFish(ModelPart root) {
        super();
        this.model = new ModelPartHandler(root, "root");
        this.anim = AnimationManager.getInstance().getAnimation(new ResourceLocation(RuneCraftory.MODID, "sky_fish"));
        this.body = this.model.getPart("body");
        this.ridingPosition = this.model.getPart("ridingPos");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -5.0F, -9.0F, 6.0F, 10.0F, 17.0F, new CubeDeformation(0.0F))
                .texOffs(2, 28).addBox(2.5F, -3.0F, -7.0F, 1.0F, 6.0F, 15.0F, new CubeDeformation(0.0F))
                .texOffs(46, 1).addBox(-4.5F, -3.0F, -7.0F, 1.0F, 6.0F, 15.0F, new CubeDeformation(0.0F))
                .texOffs(34, 27).addBox(-2.5F, -6.0F, -7.0F, 4.0F, 1.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 18.0F, 1.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(44, 72).addBox(-2.0F, -0.5F, -3.0F, 4.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 72).addBox(-2.5F, -2.0F, -2.0F, 5.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, -2.0F, -8.5F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(18, 49).addBox(-2.5F, -2.0F, 0.0F, 5.0F, 7.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(34, 72).addBox(2.5F, -0.5F, 0.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(24, 72).addBox(-3.5F, -0.5F, 0.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, -1.5F, 7.0F));

        PartDefinition tailTip = tail.addOrReplaceChild("tailTip", CubeListBuilder.create().texOffs(18, 61).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 4.0F));

        PartDefinition caudalFin = tailTip.addOrReplaceChild("caudalFin", CubeListBuilder.create().texOffs(0, 49).addBox(0.0F, -6.0F, 0.0F, 0.0F, 14.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 5.0F));

        PartDefinition dorsalFin = body.addOrReplaceChild("dorsalFin", CubeListBuilder.create().texOffs(14, 72).addBox(-0.5F, -1.0F, 0.0F, 0.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.0F, -7.0F));

        PartDefinition adiposeFin = body.addOrReplaceChild("adiposeFin", CubeListBuilder.create().texOffs(0, 82).addBox(0.0F, -1.0F, 0.0F, 0.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, -8.0F, 3.0F));

        PartDefinition leftPectoralFin = body.addOrReplaceChild("leftPectoralFin", CubeListBuilder.create().texOffs(16, 82).addBox(-0.25F, 1.0F, 0.5F, 0.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.5F, 0.0F, -5.0F, -0.3054F, 0.4363F, -0.1309F));

        PartDefinition rightPectoralFin = body.addOrReplaceChild("rightPectoralFin", CubeListBuilder.create().texOffs(16, 82).mirror().addBox(0.25F, 1.0F, 0.5F, 0.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.5F, 0.0F, -5.0F, -0.3054F, -0.4363F, 0.1309F));

        PartDefinition leftPelvicFin = body.addOrReplaceChild("leftPelvicFin", CubeListBuilder.create().texOffs(8, 82).addBox(0.0F, 1.0F, 0.0F, 0.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 2.0F, 4.0F, -0.1745F, 0.3927F, 0.0F));

        PartDefinition rightPelvicFin = body.addOrReplaceChild("rightPelvicFin", CubeListBuilder.create().texOffs(8, 82).mirror().addBox(0.0F, 1.0F, 0.0F, 0.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.5F, 2.0F, 4.0F, -0.1745F, -0.3927F, 0.0F));

        PartDefinition riderPos = body.addOrReplaceChild("ridingPos", CubeListBuilder.create(), PartPose.offset(0.0F, -6.0F, 1.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
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
                this.anim.doAnimation(this, "swim", entity.tickCount, partialTicks, entity.interpolatedMoveTick(partialTicks));
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
                this.ridingPosition.translateAndRotate(poseStack);
                ClientHandlers.translateRider(entityRenderer, rider, model, poseStack);
                return true;
            }
        }
        return false;
    }
}