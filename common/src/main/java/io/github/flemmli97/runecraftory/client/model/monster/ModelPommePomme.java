package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityPommePomme;
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

public class ModelPommePomme<T extends EntityPommePomme> extends EntityModel<T> implements ExtendedModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(RuneCraftory.MODID, "pomme_pomme"), "main");

    protected final ModelPartHandler model;
    protected final BlockBenchAnimations anim;

    public ModelPommePomme(ModelPart root) {
        super();
        this.model = new ModelPartHandler(root, "body");
        this.anim = AnimationManager.getInstance().getAnimation(new ResourceLocation(RuneCraftory.MODID, "pomme_pomme"));
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -6.0F, -7.0F, 14.0F, 14.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(0, 28).addBox(7.0F, -5.0F, -6.0F, 1.0F, 12.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(26, 28).addBox(8.0F, -3.0F, -4.0F, 1.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 28).mirror().addBox(-8.0F, -5.0F, -6.0F, 1.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(26, 28).mirror().addBox(-9.0F, -3.0F, -4.0F, 1.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(56, 0).addBox(-6.0F, -5.0F, -8.0F, 12.0F, 12.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(56, 13).addBox(-4.0F, -3.0F, -9.0F, 8.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(56, 0).mirror().addBox(-6.0F, -5.0F, 7.0F, 12.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(56, 13).mirror().addBox(-4.0F, -3.0F, 8.0F, 8.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(74, 23).addBox(-6.0F, -7.0F, -6.0F, 12.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(44, 36).addBox(-6.0F, 8.0F, -6.0F, 12.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(92, 36).addBox(-4.0F, 9.0F, -4.0F, 8.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 13.0F, 0.0F));

        PartDefinition inner = body.addOrReplaceChild("inner", CubeListBuilder.create().texOffs(102, 0).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(74, 14).addBox(-4.0F, -7.0F, -4.0F, 8.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.0F, 0.0F));

        PartDefinition stem = inner.addOrReplaceChild("stem", CubeListBuilder.create().texOffs(106, 12).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.0F, 0.0F));

        PartDefinition hairRight = inner.addOrReplaceChild("hairRight", CubeListBuilder.create().texOffs(56, 23).mirror().addBox(-6.0F, 0.0F, -4.0F, 6.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.0F, -6.0F, 0.0F, 0.0F, 0.0F, -0.2618F));

        PartDefinition hairLeft = inner.addOrReplaceChild("hairLeft", CubeListBuilder.create().texOffs(56, 23).addBox(0.0F, 0.0F, -4.0F, 6.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -6.0F, 0.0F, 0.0F, 0.0F, 0.3491F));

        PartDefinition handLeft = body.addOrReplaceChild("handLeft", CubeListBuilder.create().texOffs(82, 8).addBox(-1.0F, 1.4696F, -5.3473F, 2.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -9.5F, -4.0F, 0.1745F, -0.1745F, 0.0F));

        PartDefinition handRight = body.addOrReplaceChild("handRight", CubeListBuilder.create().texOffs(82, 8).addBox(-1.0F, 1.4696F, -5.3473F, 2.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -9.5F, -4.0F, 0.1745F, 0.1745F, 0.0F));

        PartDefinition feetLeft = body.addOrReplaceChild("feetLeft", CubeListBuilder.create().texOffs(82, 0).addBox(-1.5F, 0.0F, -7.0F, 3.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, 10.0F, 1.0F));

        PartDefinition feetRight = body.addOrReplaceChild("feetRight", CubeListBuilder.create().texOffs(82, 0).mirror().addBox(-1.5F, 0.0F, -7.0F, 3.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.5F, 10.0F, 1.0F));

        return LayerDefinition.create(meshdefinition, 126, 52);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.model.getMainPart().render(poseStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.model.resetPoses();
        AnimatedAction anim = entity.getAnimationHandler().getAnimation();
        float partialTicks = Minecraft.getInstance().getFrameTime();
        this.anim.doAnimation(this, "iddle", entity.tickCount, partialTicks);
        if (entity.isMoving())
            this.anim.doAnimation(this, "walk", entity.tickCount, partialTicks);
        if (anim != null)
            this.anim.doAnimation(this, anim.getAnimationClient(), anim.getTick(), partialTicks);
    }

    @Override
    public ModelPartHandler getHandler() {
        return this.model;
    }
}