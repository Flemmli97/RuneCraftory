package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityPommePomme;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

public class ModelMino<T extends EntityPommePomme> extends ModelPommePomme<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(RuneCraftory.MODID, "mino"), "main");

    public ModelMino(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -6.0F, -7.0F, 14.0F, 14.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(42, 53).addBox(7.0F, -2.0F, -6.0F, 1.0F, 9.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(28, 44).addBox(-8.0F, -2.0F, -6.0F, 1.0F, 9.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(56, 53).addBox(-6.0F, -2.0F, -8.0F, 12.0F, 9.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(56, 13).addBox(-6.0F, -2.0F, 7.0F, 12.0F, 9.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(42, 28).addBox(-6.0F, -7.0F, -6.0F, 12.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(42, 0).addBox(-6.0F, 8.0F, -6.0F, 12.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(42, 44).addBox(-4.0F, 9.0F, -4.0F, 8.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 13.0F, 0.0F));

        PartDefinition inner = body.addOrReplaceChild("inner", CubeListBuilder.create().texOffs(0, 56).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 28).addBox(-7.0F, -8.0F, -7.0F, 14.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(18, 65).addBox(-3.0F, -14.0F, -3.0F, 6.0F, 4.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 44).addBox(-5.0F, -10.0F, -5.0F, 10.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.0F, 0.0F));

        PartDefinition stem = inner.addOrReplaceChild("stem", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -10.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.0F, 0.0F));

        PartDefinition handLeft = body.addOrReplaceChild("handLeft", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 1.4696F, -5.3473F, 2.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -9.5F, -4.0F, 0.1745F, -0.1745F, 0.0F));

        PartDefinition handRight = body.addOrReplaceChild("handRight", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 1.4696F, -5.3473F, 2.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -9.5F, -4.0F, 0.1745F, 0.1745F, 0.0F));

        PartDefinition feetLeft = body.addOrReplaceChild("feetLeft", CubeListBuilder.create().texOffs(66, 41).addBox(-1.5F, 0.0F, -7.0F, 3.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, 10.0F, 1.0F));

        PartDefinition feetRight = body.addOrReplaceChild("feetRight", CubeListBuilder.create().texOffs(66, 41).mirror().addBox(-1.5F, 0.0F, -7.0F, 3.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.5F, 10.0F, 1.0F));

        PartDefinition ridingPos = body.addOrReplaceChild("ridingPos", CubeListBuilder.create(), PartPose.offset(0.0F, -5.0F, 5.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }
}