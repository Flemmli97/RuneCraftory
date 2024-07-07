package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.client.model.SittingModel;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.EntitySarcophagus;
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
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class ModelSarcophagus<T extends EntitySarcophagus> extends EntityModel<T> implements ExtendedModel, RideableModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(RuneCraftory.MODID, "sarcophagus"), "main");

    protected final ModelPartHandler model;
    protected final BlockBenchAnimations anim;

    public ModelPartHandler.ModelPartExtended body;
    public ModelPartHandler.ModelPartExtended ridingPosition;

    public ModelSarcophagus(ModelPart root) {
        super();
        this.model = new ModelPartHandler(root, "root");
        this.anim = AnimationManager.getInstance().getAnimation(new ResourceLocation(RuneCraftory.MODID, "sarcophagus"));
        this.body = this.model.getPart("body");
        this.ridingPosition = this.model.getPart("ridingPos");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(52, 21).addBox(-5.0F, 23.5F, -6.0F, 10.0F, 4.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(0, 33).addBox(-6.0F, 8.5F, -7.0F, 12.0F, 15.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-8.0F, -8.5F, -8.0F, 16.0F, 17.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.5F, 0.0F));

        PartDefinition neck = body.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(48, 0).addBox(-7.0F, -4.0F, -6.0F, 14.0F, 4.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.5F, 0.0F));

        PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create().texOffs(41, 51).addBox(-6.0F, -11.0F, -5.5F, 12.0F, 11.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(55, 73).addBox(4.001F, -2.0F, -6.5F, 3.0F, 6.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(48, 0).addBox(5.0F, -8.0F, -5.0F, 2.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(48, 0).mirror().addBox(-7.0F, -8.0F, -5.0F, 2.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(55, 73).mirror().addBox(-7.001F, -2.0F, -6.5F, 3.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -4.0F, 0.0F));

        PartDefinition snout = head.addOrReplaceChild("snout", CubeListBuilder.create().texOffs(33, 73).addBox(-2.5F, -2.75F, -6.5F, 5.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(88, 6).addBox(-2.0F, 0.25F, -4.5F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.25F, -5.0F));

        PartDefinition earLeft = head.addOrReplaceChild("earLeft", CubeListBuilder.create().texOffs(28, 88).addBox(0.0F, -5.0F, -0.5F, 3.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -11.0F, -2.5F, 0.2182F, 0.0F, 0.2182F));

        PartDefinition earRight = head.addOrReplaceChild("earRight", CubeListBuilder.create().texOffs(28, 88).mirror().addBox(-3.0F, -5.0F, -0.5F, 3.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, -11.0F, -2.5F, 0.2182F, 0.0F, -0.2182F));

        PartDefinition ring = body.addOrReplaceChild("ring", CubeListBuilder.create(), PartPose.offset(0.0F, -1.5F, 9.0F));

        PartDefinition ringLeft = ring.addOrReplaceChild("ringLeft", CubeListBuilder.create().texOffs(84, 70).addBox(-3.0F, -1.0F, 0.0F, 6.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(87, 55).addBox(-2.0F, 2.0F, 0.0F, 4.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(17.0F, -3.0F, 0.0F));

        PartDefinition leftFox = ringLeft.addOrReplaceChild("leftFox", CubeListBuilder.create().texOffs(0, 73).addBox(-2.0F, -5.0F, -2.0F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(66, 73).addBox(-2.0F, -1.0F, 3.0F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(49, 73).addBox(-2.0F, -1.0F, -1.0F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(12, 14).addBox(2.0F, -7.0F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(8, 14).addBox(-3.0F, -7.0F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(74, 37).addBox(-2.0F, -7.0F, -5.0F, 4.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition leftFoxTail = leftFox.addOrReplaceChild("leftFoxTail", CubeListBuilder.create().texOffs(39, 103).mirror().addBox(-7.0F, -7.0F, 0.0F, 14.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -4.0F, 5.0F, 0.0873F, 0.0F, 0.0F));

        PartDefinition leftFoxTail2 = leftFox.addOrReplaceChild("leftFoxTail2", CubeListBuilder.create().texOffs(39, 110).mirror().addBox(-7.0F, 0.0F, 0.001F, 14.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -4.0F, 5.0F, -0.0873F, 0.0F, 0.0F));

        PartDefinition ringLeft2 = ringLeft.addOrReplaceChild("ringLeft2", CubeListBuilder.create().texOffs(85, 85).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 7.0F, 3.0F, new CubeDeformation(-0.001F))
                .texOffs(0, 62).addBox(2.0F, -3.0F, 1.5F, 12.0F, 11.0F, 0.0F, new CubeDeformation(-0.001F)), PartPose.offsetAndRotation(0.0F, 9.0F, 0.0F, 0.0F, 0.0F, 0.3054F));

        PartDefinition ringLeft3 = ringLeft2.addOrReplaceChild("ringLeft3", CubeListBuilder.create().texOffs(67, 88).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, 0.0F, 0.3054F));

        PartDefinition ringLeft4 = ringLeft3.addOrReplaceChild("ringLeft4", CubeListBuilder.create().texOffs(76, 46).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 9.0F, 3.0F, new CubeDeformation(-0.001F))
                .texOffs(52, 37).addBox(2.0F, -4.0F, 1.5F, 11.0F, 13.0F, 0.0F, new CubeDeformation(-0.001F)), PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, 0.0F, 0.0F, 0.3491F));

        PartDefinition ringLeft5 = ringLeft4.addOrReplaceChild("ringLeft5", CubeListBuilder.create().texOffs(0, 84).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 8.0F, 3.0F, new CubeDeformation(-0.001F)), PartPose.offsetAndRotation(0.0F, 7.0F, 0.0F, 0.0F, 0.0F, 0.4363F));

        PartDefinition ringRight = ring.addOrReplaceChild("ringRight", CubeListBuilder.create().texOffs(84, 26).addBox(-3.0F, -1.0F, 0.0F, 6.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(53, 84).addBox(-2.0F, 2.0F, 0.0F, 4.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-17.0F, -3.0F, 0.0F));

        PartDefinition rightFox = ringRight.addOrReplaceChild("rightFox", CubeListBuilder.create().texOffs(17, 66).addBox(-2.0F, -5.0F, -2.0F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(64, 16).addBox(-2.0F, -1.0F, 3.0F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(24, 62).addBox(-2.0F, -1.0F, -1.0F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(4, 14).addBox(-3.0F, -7.0F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 14).addBox(2.0F, -7.0F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(71, 73).addBox(-2.0F, -7.0F, -5.0F, 4.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition rightFoxTail = rightFox.addOrReplaceChild("rightFoxTail", CubeListBuilder.create().texOffs(72, 102).mirror().addBox(-7.0F, -7.0F, 0.0F, 14.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -4.0F, 5.0F, 0.0873F, 0.0F, 0.0F));

        PartDefinition rightFoxTail2 = rightFox.addOrReplaceChild("rightFoxTail2", CubeListBuilder.create().texOffs(72, 109).mirror().addBox(-7.0F, 0.0F, 0.0F, 14.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -4.0F, 5.0F, -0.0873F, 0.0F, 0.0F));

        PartDefinition ringRight2 = ringRight.addOrReplaceChild("ringRight2", CubeListBuilder.create().texOffs(84, 16).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 7.0F, 3.0F, new CubeDeformation(-0.001F))
                .texOffs(0, 62).mirror().addBox(-14.0F, -3.0F, 1.5F, 12.0F, 11.0F, 0.0F, new CubeDeformation(-0.001F)).mirror(false), PartPose.offsetAndRotation(0.0F, 9.0F, 0.0F, 0.0F, 0.0F, -0.3054F));

        PartDefinition ringRight3 = ringRight2.addOrReplaceChild("ringRight3", CubeListBuilder.create().texOffs(14, 88).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, 0.0F, -0.3054F));

        PartDefinition ringRight4 = ringRight3.addOrReplaceChild("ringRight4", CubeListBuilder.create().texOffs(38, 33).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 9.0F, 3.0F, new CubeDeformation(-0.001F))
                .texOffs(52, 37).mirror().addBox(-13.0F, -4.0F, 1.5F, 11.0F, 13.0F, 0.0F, new CubeDeformation(-0.001F)).mirror(false), PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, 0.0F, 0.0F, -0.3491F));

        PartDefinition ringRight5 = ringRight4.addOrReplaceChild("ringRight5", CubeListBuilder.create().texOffs(39, 82).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 8.0F, 3.0F, new CubeDeformation(-0.001F)), PartPose.offsetAndRotation(0.0F, 7.0F, 0.0F, 0.0F, 0.0F, -0.4363F));

        PartDefinition armRight = body.addOrReplaceChild("armRight", CubeListBuilder.create().texOffs(68, 82).addBox(0.0F, -1.5F, -1.5F, 7.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, 0.0F, -8.0F, 0.1745F, 0.3054F, 0.7418F));

        PartDefinition armRight2 = armRight.addOrReplaceChild("armRight2", CubeListBuilder.create().texOffs(88, 0).addBox(0.0F, -1.5F, -1.5F, 6.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, 0.0F, 0.0F, 0.0873F, -0.3054F, -0.7418F));

        PartDefinition armLeft = body.addOrReplaceChild("armLeft", CubeListBuilder.create().texOffs(19, 82).addBox(-7.0F, -1.5F, -1.5F, 7.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, 0.0F, -8.0F, -0.1745F, -0.3054F, 0.7418F));

        PartDefinition armLeft2 = armLeft.addOrReplaceChild("armLeft2", CubeListBuilder.create().texOffs(86, 79).addBox(-6.0F, -1.5F, -1.5F, 6.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, 0.0F, 0.0F, -0.0873F, 0.3054F, -0.7418F));

        PartDefinition ridingPos = body.addOrReplaceChild("ridingPos", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 8.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.model.getMainPart().render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.model.resetPoses();
        this.body.yRot += (netHeadYaw % 360) * Mth.DEG_TO_RAD * 0.5f;
        this.body.xRot += headPitch * Mth.DEG_TO_RAD * 0.5f;
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