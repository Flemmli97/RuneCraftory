package io.github.flemmli97.runecraftory.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.GateEntity;
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

public class ModelGate extends EntityModel<GateEntity> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(RuneCraftory.MODID, "gate_model"), "main");
    private final ModelPart core;

    public ModelGate(ModelPart root) {
        this.core = root.getChild("core");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition core = partdefinition.addOrReplaceChild("core", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-8.0F, -7.0F, -7.0F, 15.0F, 15.0F, 15.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition shape = core.addOrReplaceChild("shape", CubeListBuilder.create().texOffs(0, 31).mirror().addBox(-7.0F, 8.0F, -6.0F, 13.0F, 2.0F, 13.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition shape2 = core.addOrReplaceChild("shape2", CubeListBuilder.create().texOffs(0, 31).mirror().addBox(-7.0F, -9.0F, -6.0F, 13.0F, 2.0F, 13.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition shape3 = core.addOrReplaceChild("shape3", CubeListBuilder.create().texOffs(61, 4).mirror().addBox(-10.0F, -6.0F, -6.0F, 2.0F, 13.0F, 13.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition shape4 = core.addOrReplaceChild("shape4", CubeListBuilder.create().texOffs(61, 4).mirror().addBox(7.0F, -6.0F, -6.0F, 2.0F, 13.0F, 13.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition shape5 = core.addOrReplaceChild("shape5", CubeListBuilder.create().texOffs(53, 31).mirror().addBox(-7.0F, -6.0F, 8.0F, 13.0F, 13.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition shape6 = core.addOrReplaceChild("shape6", CubeListBuilder.create().texOffs(53, 31).mirror().addBox(-7.0F, -6.0F, -9.0F, 13.0F, 13.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 95, 48);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.core.render(poseStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public void setupAnim(GateEntity gate, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.core.yRot = gate.rotate * ageInTicks / 50.0F;
    }
}
