package io.github.flemmli97.runecraftory.client.model.armor;

import io.github.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.LivingEntity;

public class RingsArmorModel extends HumanoidModel<LivingEntity> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(RuneCraftory.modRes("rings"), "main");

    public RingsArmorModel(ModelPart modelPart) {
        super(modelPart);
    }

    @Override
    public void setupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }
}
