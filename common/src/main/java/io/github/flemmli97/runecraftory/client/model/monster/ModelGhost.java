package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.SittingModel;
import io.github.flemmli97.runecraftory.client.render.monster.RenderGhost;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityGhost;
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
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class ModelGhost<T extends EntityGhost> extends EntityModel<T> implements ExtendedModel, RideableModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(RuneCraftory.MODID, "ghost"), "main");

    protected final ModelPartHandler model;
    protected final BlockBenchAnimations anim;

    public ModelPartHandler.ModelPartExtended body;

    public ModelGhost(ModelPart root) {
        super(RenderType::entityTranslucentCull);
        this.model = new ModelPartHandler(root, "root");
        this.anim = AnimationManager.getInstance().getAnimation(new ResourceLocation(RuneCraftory.MODID, "ghost"));
        this.body = this.model.getPart("body");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -14.5F, -7.0F, 14.0F, 29.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 8.5F, 0.0F));

        PartDefinition hat = body.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(43, 30).addBox(-7.0F, -7.0F, 0.0F, 14.0F, 7.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(0, 96).addBox(-7.0F, -6.0F, 0.0F, 14.0F, 0.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -14.5F, -7.0F, -0.0873F, 0.0F, 0.0F));

        PartDefinition hat2 = hat.addOrReplaceChild("hat2", CubeListBuilder.create().texOffs(0, 44).addBox(-6.0F, -4.0F, 0.0F, 12.0F, 4.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, 1.0F, -0.1745F, 0.0F, 0.0F));

        PartDefinition hat3 = hat2.addOrReplaceChild("hat3", CubeListBuilder.create().texOffs(39, 52).addBox(-5.0F, -4.0F, 0.0F, 10.0F, 4.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.0F, 1.0F, -0.2182F, 0.0F, 0.0F));

        PartDefinition hat4 = hat3.addOrReplaceChild("hat4", CubeListBuilder.create().texOffs(43, 0).addBox(-4.0F, -4.0F, 0.0F, 8.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.0F, 1.0F, -0.3054F, 0.0F, 0.0F));

        PartDefinition hat5 = hat4.addOrReplaceChild("hat5", CubeListBuilder.create().texOffs(60, 67).addBox(-2.0F, -6.0F, 0.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.0F, 2.0F, -0.3927F, 0.0F, 0.0F));

        PartDefinition hat6 = hat5.addOrReplaceChild("hat6", CubeListBuilder.create().texOffs(0, 44).addBox(-1.0F, -7.0F, 0.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.0F, 1.0F, -0.5236F, 0.0F, 0.0F));

        PartDefinition armLeftUp = body.addOrReplaceChild("armLeftUp", CubeListBuilder.create().texOffs(43, 67).addBox(0.0F, -2.5F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(7.0F, -6.0F, 0.0F));

        PartDefinition armLeftDown = armLeftUp.addOrReplaceChild("armLeftDown", CubeListBuilder.create().texOffs(26, 63).addBox(-2.0F, 0.0F, -4.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-1.5F, 8.0F, -3.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 5.5F, 2.0F));

        PartDefinition armRightUp = body.addOrReplaceChild("armRightUp", CubeListBuilder.create().texOffs(43, 67).mirror().addBox(-4.0F, -2.5F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-7.0F, -6.0F, 0.0F));

        PartDefinition armRightDown = armRightUp.addOrReplaceChild("armRightDown", CubeListBuilder.create().texOffs(26, 63).addBox(-2.0F, 0.0F, -4.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).mirror().addBox(-1.5F, 8.0F, -3.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.0F, 5.5F, 2.0F));

        PartDefinition scythe = armRightDown.addOrReplaceChild("scythe", CubeListBuilder.create().texOffs(0, 61).addBox(-1.0F, -30.0F, -1.0F, 2.0F, 33.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(57, 14).addBox(-0.5F, -29.0F, -9.0F, 1.0F, 7.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.0F, -3.0F, 0.6109F, 0.0F, 0.0F));

        PartDefinition blade2 = scythe.addOrReplaceChild("blade2", CubeListBuilder.create().texOffs(9, 61).addBox(-0.5F, 3.0F, -7.0F, 1.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -31.0F, -9.0F, 0.1309F, 0.0F, 0.0F));

        PartDefinition blade3 = blade2.addOrReplaceChild("blade3", CubeListBuilder.create().texOffs(70, 8).addBox(-0.5F, 3.0F, -6.0F, 1.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, -7.0F, 0.1309F, 0.0F, 0.0F));

        PartDefinition blade4 = blade3.addOrReplaceChild("blade4", CubeListBuilder.create().texOffs(71, 72).addBox(-0.5F, 3.0F, -6.0F, 1.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, -6.0F, 0.1745F, 0.0F, 0.0F));

        PartDefinition blade5 = blade4.addOrReplaceChild("blade5", CubeListBuilder.create().texOffs(70, 52).addBox(-0.5F, 3.0F, -6.0F, 1.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, -6.0F, 0.3491F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.scale(0.85f, 0.85f, 0.85f);
        poseStack.translate(0, 0.2, 0);
        this.model.getMainPart().render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, 0.85f);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.model.resetPoses();
        this.model.getMainPart().visible = true;
        float partialTicks = Minecraft.getInstance().getFrameTime();
        if (entity.deathTime <= 0 && !entity.playDeath()) {
            this.anim.doAnimation(this, "idle", entity.tickCount, partialTicks);
        }
        this.anim.doAnimation(this, entity.getAnimationHandler(), partialTicks);
    }

    @Override
    public ModelPartHandler getHandler() {
        return this.model;
    }

    @Override
    public boolean transform(T entity, EntityRenderer<T> entityRenderer, Entity rider, EntityRenderer<?> ridingEntityRenderer, PoseStack poseStack, int riderNum) {
        if (ridingEntityRenderer instanceof LivingEntityRenderer<?, ?> lR && entityRenderer instanceof RenderGhost<T> scaledRender) {
            EntityModel<?> model = lR.getModel();
            if (model instanceof HumanoidModel<?> || model instanceof IllagerModel<?> || model instanceof SittingModel) {
                this.body.translateAndRotate(poseStack);
                if (model instanceof SittingModel sittingModel)
                    sittingModel.translateSittingPosition(poseStack);
                else
                    poseStack.translate(0, -7 / 16d, 10 / 16d);
                poseStack.scale(1 / scaledRender.scale, 1 / scaledRender.scale, 1 / scaledRender.scale);
                poseStack.translate(0, 11 / 16d, 0);
                poseStack.scale(scaledRender.scale, scaledRender.scale, scaledRender.scale);
                return true;
            }
        }
        return false;
    }
}