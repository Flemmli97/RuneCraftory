package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityNappie;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

public class ModelNappie<T extends EntityNappie> extends ModelPommePomme<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(RuneCraftory.MODID, "nappie"), "main");

    public ModelNappie(ModelPart root) {
        super(root, "nappie");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -6.0F, -7.0F, 14.0F, 14.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(0, 41).addBox(7.0F, -5.0F, -6.0F, 1.0F, 12.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(56, 9).addBox(8.0F, -3.0F, -4.0F, 1.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 41).mirror().addBox(-8.0F, -5.0F, -6.0F, 1.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(56, 9).mirror().addBox(-9.0F, -3.0F, -4.0F, 1.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(46, 42).addBox(-6.0F, -5.0F, -8.0F, 12.0F, 12.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(26, 63).addBox(-4.0F, -3.0F, -9.0F, 8.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(46, 42).mirror().addBox(-6.0F, -5.0F, 7.0F, 12.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(26, 63).mirror().addBox(-4.0F, -3.0F, 8.0F, 8.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(36, 29).addBox(-6.0F, -7.0F, -6.0F, 12.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(0, 28).addBox(-6.0F, 8.0F, -6.0F, 12.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(14, 42).addBox(-4.0F, 9.0F, -4.0F, 8.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 13.0F, 0.0F));

        PartDefinition inner = body.addOrReplaceChild("inner", CubeListBuilder.create().texOffs(26, 51).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(42, 0).addBox(-4.0F, -7.0F, -4.0F, 8.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.0F, 0.0F));

        PartDefinition stem = inner.addOrReplaceChild("stem", CubeListBuilder.create().texOffs(44, 63).addBox(-4.5F, -6.0F, 0.0F, 9.0F, 7.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(44, 63).addBox(-4.5F, -6.0F, 0.0F, 9.0F, 7.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(50, 46).addBox(0.0F, -6.0F, -5.0F, 0.0F, 7.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.0F, 0.0F));

        PartDefinition stem2 = inner.addOrReplaceChild("stem2", CubeListBuilder.create().texOffs(44, 63).addBox(-4.5F, -6.0F, 0.0F, 9.0F, 7.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(50, 46).addBox(0.0F, -6.0F, -5.0F, 0.0F, 7.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition hairRight = inner.addOrReplaceChild("hairRight", CubeListBuilder.create().texOffs(28, 28).mirror().addBox(-6.0F, 0.0F, -4.0F, 6.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.0F, -6.0F, 0.0F, 0.0F, 0.0F, -0.2618F));

        PartDefinition hairLeft = inner.addOrReplaceChild("hairLeft", CubeListBuilder.create().texOffs(28, 28).addBox(0.0F, 0.0F, -4.0F, 6.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -6.0F, 0.0F, 0.0F, 0.0F, 0.3491F));

        PartDefinition handLeft = body.addOrReplaceChild("handLeft", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 1.4696F, -5.3473F, 2.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -9.5F, -4.0F, 0.1745F, -0.1745F, 0.0F));

        PartDefinition handRight = body.addOrReplaceChild("handRight", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 1.4696F, -5.3473F, 2.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -9.5F, -4.0F, 0.1745F, 0.1745F, 0.0F));

        PartDefinition feetLeft = body.addOrReplaceChild("feetLeft", CubeListBuilder.create().texOffs(61, 55).addBox(-1.5F, 0.0F, -7.0F, 3.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, 10.0F, 1.0F));

        PartDefinition feetRight = body.addOrReplaceChild("feetRight", CubeListBuilder.create().texOffs(61, 55).mirror().addBox(-1.5F, 0.0F, -7.0F, 3.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.5F, 10.0F, 1.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }
}