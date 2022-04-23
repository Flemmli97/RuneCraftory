package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityWooly;
import io.github.flemmli97.tenshilib.client.AnimationManager;
import io.github.flemmli97.tenshilib.client.model.BlockBenchAnimations;
import io.github.flemmli97.tenshilib.client.model.ExtendedModel;
import io.github.flemmli97.tenshilib.client.model.ModelPartHandler;
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

public class ModelWoolyWool<T extends EntityWooly> extends EntityModel<T> implements ExtendedModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(RuneCraftory.MODID, "wooly_wool"), "main");

    protected final ModelPartHandler model;
    protected final BlockBenchAnimations anim;

    public ModelPartHandler.ModelPartExtended bodyCenter;
    public ModelPartHandler.ModelPartExtended body;
    public ModelPartHandler.ModelPartExtended bodyUp;
    public ModelPartHandler.ModelPartExtended armLeftBase;
    public ModelPartHandler.ModelPartExtended armRightBase;
    public ModelPartHandler.ModelPartExtended feetLeftBase;
    public ModelPartHandler.ModelPartExtended feetRightBase;

    public ModelWoolyWool(ModelPart root) {
        super();
        this.model = new ModelPartHandler(root.getChild("bodyCenter"), "bodyCenter");
        this.anim = AnimationManager.getInstance().getAnimation(new ResourceLocation(RuneCraftory.MODID, "wooly_wool"));
        this.bodyCenter = this.model.getMainPart();
        this.body = this.model.getPart("body");
        this.bodyUp = this.model.getPart("bodyUp");
        this.armLeftBase = this.model.getPart("armLeftBase");
        this.armRightBase = this.model.getPart("armRightBase");
        this.feetLeftBase = this.model.getPart("feetLeftBase");
        this.feetRightBase = this.model.getPart("feetRightBase");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bodyCenter = partdefinition.addOrReplaceChild("bodyCenter", CubeListBuilder.create(), PartPose.offset(0.0F, 17.75F, 0.0F));

        PartDefinition body = bodyCenter.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 31).mirror().addBox(-3.5F, -7.0F, -4.5F, 7.0F, 13.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(32, 30).mirror().addBox(-4.5F, -7.0F, -3.5F, 1.0F, 13.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(32, 30).addBox(3.5F, -7.0F, -3.5F, 1.0F, 13.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bodyUp = body.addOrReplaceChild("bodyUp", CubeListBuilder.create().texOffs(32, 14).mirror().addBox(-2.5F, -1.0F, -3.5F, 5.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(28, 23).addBox(2.5F, -1.0F, -2.5F, 1.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(28, 23).mirror().addBox(-3.5F, -1.0F, -2.5F, 1.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -8.0F, 0.0F));

        PartDefinition armLeftBase = body.addOrReplaceChild("armLeftBase", CubeListBuilder.create().texOffs(34, 50).addBox(0.25F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.75F, -3.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

        PartDefinition armRightBase = body.addOrReplaceChild("armRightBase", CubeListBuilder.create().texOffs(34, 50).addBox(-2.25F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.75F, -3.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

        PartDefinition feetLeftBase = body.addOrReplaceChild("feetLeftBase", CubeListBuilder.create().texOffs(42, 50).addBox(-1.5F, -5.5F, -2.5F, 3.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 4.75F, 0.0F));

        PartDefinition feetRightBase = body.addOrReplaceChild("feetRightBase", CubeListBuilder.create().texOffs(42, 50).mirror().addBox(-1.5F, -5.5F, -2.5F, 3.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-4.0F, 4.75F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 62);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.model.getMainPart().render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public ModelPartHandler getHandler() {
        return this.model;
    }

    private void sync(ModelPartHandler.ModelPartExtended model, ModelPartHandler.ModelPartExtended other) {
        model.xRot = other.xRot;
        model.yRot = other.yRot;
        model.zRot = other.zRot;
        model.x = other.x;
        model.y = other.y;
        model.z = other.z;
        model.xScale = other.xScale;
        model.yScale = other.yScale;
        model.zScale = other.zScale;
    }

    public void syncModel(ModelWooly<T> model) {
        this.sync(this.bodyCenter, model.bodyCenter);
        this.sync(this.body, model.body);
        this.sync(this.bodyUp, model.bodyUp);
        this.sync(this.armLeftBase, model.armLeftBase);
        this.sync(this.armRightBase, model.armRightBase);
        this.sync(this.feetLeftBase, model.feetLeftBase);
        this.sync(this.feetRightBase, model.feetRightBase);
    }
}