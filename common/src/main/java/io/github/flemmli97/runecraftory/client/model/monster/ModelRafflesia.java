package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.SittingModel;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.rafflesia.EntityRafflesia;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.rafflesia.EntityRafflesiaPart;
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

public class ModelRafflesia<T extends EntityRafflesia> extends EntityModel<T> implements ExtendedModel, RideableModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(RuneCraftory.MODID, "rafflesia"), "main");

    protected final ModelPartHandler model;
    protected final BlockBenchAnimations anim;

    public ModelPartHandler.ModelPartExtended main;
    public ModelPartHandler.ModelPartExtended mainStem;
    public ModelPartHandler.ModelPartExtended mainStem2;
    public ModelPartHandler.ModelPartExtended mainStem3;
    public ModelPartHandler.ModelPartExtended mainStem4;
    public ModelPartHandler.ModelPartExtended head;
    public ModelPartHandler.ModelPartExtended horseTail;
    public ModelPartHandler.ModelPartExtended pitcher;
    public ModelPartHandler.ModelPartExtended flower;

    public ModelRafflesia(ModelPart root) {
        super();
        this.model = new ModelPartHandler(root, "root");
        this.anim = AnimationManager.getInstance().getAnimation(new ResourceLocation(RuneCraftory.MODID, "rafflesia"));
        this.main = this.model.getPart("main");
        this.mainStem = this.model.getPart("mainStem");
        this.mainStem2 = this.model.getPart("mainStem2");
        this.mainStem3 = this.model.getPart("mainStem3");
        this.mainStem4 = this.model.getPart("mainStem4");
        this.head = this.model.getPart("head");
        this.horseTail = this.model.getPart("leftStem");
        this.pitcher = this.model.getPart("rightStem");
        this.flower = this.model.getPart("frontStem");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 0).addBox(-7.5F, -3.0F, -7.5F, 15.0F, 3.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition mainStem = main.addOrReplaceChild("mainStem", CubeListBuilder.create().texOffs(0, 31).addBox(-1.0F, -12.0F, -2.0F, 3.0F, 10.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, -1.0F, 0.5F));

        PartDefinition mainStem2 = mainStem.addOrReplaceChild("mainStem2", CubeListBuilder.create().texOffs(0, 18).addBox(-1.5F, -10.0F, -1.5F, 3.0F, 10.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, -12.0F, -0.5F));

        PartDefinition mainStem3 = mainStem2.addOrReplaceChild("mainStem3", CubeListBuilder.create().texOffs(87, 66).addBox(-1.5F, -9.0F, -1.5F, 3.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.0F, 0.0F));

        PartDefinition mainStem4 = mainStem3.addOrReplaceChild("mainStem4", CubeListBuilder.create().texOffs(0, 44).addBox(-1.5F, -10.0F, -1.5F, 3.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.0F, 0.0F));

        PartDefinition head = mainStem4.addOrReplaceChild("head", CubeListBuilder.create().texOffs(46, 48).addBox(-5.5F, -5.5F, -11.0F, 11.0F, 11.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.0F, -0.5F));

        PartDefinition mouthUpper = head.addOrReplaceChild("mouthUpper", CubeListBuilder.create().texOffs(0, 62).addBox(-3.0F, -5.0F, -9.0F, 7.0F, 5.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(78, 28).addBox(-3.0F, -0.25F, -9.0F, 7.0F, 1.0F, 9.0F, new CubeDeformation(-0.025F)), PartPose.offsetAndRotation(0.0F, 1.0F, -11.0F, -0.1309F, 0.0F, 0.0F));

        PartDefinition mouthLower = head.addOrReplaceChild("mouthLower", CubeListBuilder.create().texOffs(64, 69).addBox(-3.5F, 0.0F, -9.0F, 7.0F, 3.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(77, 0).addBox(-3.5F, -0.75F, -9.0F, 7.0F, 1.0F, 9.0F, new CubeDeformation(-0.025F)), PartPose.offsetAndRotation(0.5F, 1.0F, -11.0F, 0.0436F, 0.0F, 0.0F));

        PartDefinition flowerBase = head.addOrReplaceChild("flowerBase", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition flower = flowerBase.addOrReplaceChild("flower", CubeListBuilder.create().texOffs(0, 76).addBox(-6.5F, -17.0F, 0.0F, 13.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0873F, 0.0F, 0.0F));

        PartDefinition flower2 = flowerBase.addOrReplaceChild("flower2", CubeListBuilder.create().texOffs(32, 69).addBox(1.0F, -6.5F, 0.0F, 16.0F, 13.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0873F, 0.0F));

        PartDefinition flower4 = flowerBase.addOrReplaceChild("flower4", CubeListBuilder.create().texOffs(0, 76).addBox(-6.5F, -17.0F, 0.0F, 13.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0873F, 0.0F, -3.1416F));

        PartDefinition flower3 = flowerBase.addOrReplaceChild("flower3", CubeListBuilder.create().texOffs(32, 69).addBox(1.0F, -6.5F, 0.0F, 16.0F, 13.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0873F, -3.1416F));

        PartDefinition flowerBase2 = head.addOrReplaceChild("flowerBase2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition flower5 = flowerBase2.addOrReplaceChild("flower5", CubeListBuilder.create().texOffs(0, 76).addBox(-6.5F, -17.0F, 0.0F, 13.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0873F, 0.0F, 0.0F));

        PartDefinition flower6 = flowerBase2.addOrReplaceChild("flower6", CubeListBuilder.create().texOffs(32, 69).addBox(1.0F, -6.5F, 0.0F, 16.0F, 13.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0873F, 0.0F));

        PartDefinition flower8 = flowerBase2.addOrReplaceChild("flower8", CubeListBuilder.create().texOffs(32, 69).addBox(1.0F, -6.5F, 0.0F, 16.0F, 13.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0873F, -3.1416F));

        PartDefinition leafSetBottom = mainStem.addOrReplaceChild("leafSetBottom", CubeListBuilder.create(), PartPose.offset(0.5F, -2.0F, -0.5F));

        PartDefinition leafBottom = leafSetBottom.addOrReplaceChild("leafBottom", CubeListBuilder.create().texOffs(58, 14).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 0.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 0.0F, 1.0F, 0.4363F, -0.7854F, 0.0F));

        PartDefinition leafBottom2 = leafSetBottom.addOrReplaceChild("leafBottom2", CubeListBuilder.create().texOffs(58, 14).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 0.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.0F, 1.0F, 0.4363F, 0.7854F, 0.0F));

        PartDefinition leafBottom3 = leafSetBottom.addOrReplaceChild("leafBottom3", CubeListBuilder.create().texOffs(58, 14).mirror().addBox(-4.0F, 0.0F, 0.0F, 8.0F, 0.0F, 14.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.0F, 0.0F, -1.0F, 0.4363F, 2.3562F, 0.0F));

        PartDefinition leafBottom4 = leafSetBottom.addOrReplaceChild("leafBottom4", CubeListBuilder.create().texOffs(58, 14).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 0.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 0.0F, -1.0F, 0.4363F, -2.3562F, 0.0F));

        PartDefinition leftStem = main.addOrReplaceChild("leftStem", CubeListBuilder.create().texOffs(82, 89).addBox(-2.0F, -10.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.5F, 0.0F, -0.5F, 0.0F, 0.0F, 1.3526F));

        PartDefinition leftStem2 = leftStem.addOrReplaceChild("leftStem2", CubeListBuilder.create().texOffs(0, 56).addBox(-1.0F, -11.0F, -1.0F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -10.0F, 0.0F, 0.0F, 0.0F, -1.3526F));

        PartDefinition leftStem3 = leftStem2.addOrReplaceChild("leftStem3", CubeListBuilder.create().texOffs(32, 90).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -11.0F, 0.0F));

        PartDefinition leftStem4 = leftStem3.addOrReplaceChild("leftStem4", CubeListBuilder.create().texOffs(35, 62).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.0F, 0.0F));

        PartDefinition horseTail = leftStem4.addOrReplaceChild("horseTail", CubeListBuilder.create().texOffs(78, 38).addBox(-2.5F, -9.0F, -3.0F, 6.0F, 9.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(78, 53).addBox(-1.5F, -10.0F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, -2.0F, 0.0F));

        PartDefinition rightStem = main.addOrReplaceChild("rightStem", CubeListBuilder.create().texOffs(88, 10).addBox(0.0F, -10.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.5F, 0.0F, -0.5F, 0.0F, 0.0F, -1.3526F));

        PartDefinition rightStem2 = rightStem.addOrReplaceChild("rightStem2", CubeListBuilder.create().texOffs(24, 90).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -10.0F, 0.0F, 0.0F, 0.0F, 1.3526F));

        PartDefinition rightStem4 = rightStem2.addOrReplaceChild("rightStem4", CubeListBuilder.create().texOffs(46, 48).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -9.0F, 0.0F));

        PartDefinition rightStem3 = rightStem4.addOrReplaceChild("rightStem3", CubeListBuilder.create().texOffs(60, 14).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.0F, 0.0F));

        PartDefinition pitcher = rightStem3.addOrReplaceChild("pitcher", CubeListBuilder.create().texOffs(82, 81).addBox(-2.5F, -1.0F, -6.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(58, 81).addBox(-2.5F, -6.0F, -6.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-1.5F, -9.0F, -2.0F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, -1.25F, 0.0F));

        PartDefinition frontStem = main.addOrReplaceChild("frontStem", CubeListBuilder.create().texOffs(50, 82).addBox(-1.0F, -10.0F, 0.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 0.0F, -7.5F, 1.3526F, 0.0F, 0.0F));

        PartDefinition frontStem2 = frontStem.addOrReplaceChild("frontStem2", CubeListBuilder.create().texOffs(64, 69).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.0F, 1.0F, -1.3526F, 0.0F, 0.0F));

        PartDefinition frontFlower = frontStem2.addOrReplaceChild("frontFlower", CubeListBuilder.create().texOffs(26, 82).addBox(-3.0F, -1.25F, -3.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(23, 62).addBox(-2.0F, -2.25F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.75F, 0.0F));

        PartDefinition frontFlowerPetal = frontFlower.addOrReplaceChild("frontFlowerPetal", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -7.0F, -3.0F, 0.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -1.25F, 0.0F, 0.0F, 0.0F, -0.3054F));

        PartDefinition frontFlowerPetal2 = frontFlower.addOrReplaceChild("frontFlowerPetal2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, -7.0F, -3.0F, 0.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, -1.25F, 0.0F, 0.0F, 0.0F, 0.3054F));

        PartDefinition frontFlowerPetal3 = frontFlower.addOrReplaceChild("frontFlowerPetal3", CubeListBuilder.create().texOffs(88, 58).addBox(-3.0F, -7.0F, 0.0F, 6.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.25F, -3.0F, -0.3054F, 0.0F, 0.0F));

        PartDefinition frontFlowerPetal4 = frontFlower.addOrReplaceChild("frontFlowerPetal4", CubeListBuilder.create().texOffs(88, 58).mirror().addBox(-3.0F, -7.0F, 0.0F, 6.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -1.25F, 3.0F, 0.3054F, 0.0F, 0.0F));

        PartDefinition basePetalSet = main.addOrReplaceChild("basePetalSet", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition basePetal = basePetalSet.addOrReplaceChild("basePetal", CubeListBuilder.create().texOffs(28, 18).addBox(0.0F, 0.0F, -8.0F, 14.0F, 0.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.5236F));

        PartDefinition basePetalTip = basePetal.addOrReplaceChild("basePetalTip", CubeListBuilder.create().texOffs(0, 18).addBox(0.0F, 0.0F, -8.0F, 14.0F, 0.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(14.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0472F));

        PartDefinition basePetal2 = basePetalSet.addOrReplaceChild("basePetal2", CubeListBuilder.create().texOffs(28, 18).mirror().addBox(-14.0F, 0.0F, -8.0F, 14.0F, 0.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5236F));

        PartDefinition basePetalTip2 = basePetal2.addOrReplaceChild("basePetalTip2", CubeListBuilder.create().texOffs(0, 18).mirror().addBox(-14.0F, 0.0F, -8.0F, 14.0F, 0.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-14.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.0472F));

        PartDefinition basePetal3 = basePetalSet.addOrReplaceChild("basePetal3", CubeListBuilder.create().texOffs(0, 48).addBox(-8.0F, 0.0F, -14.0F, 16.0F, 0.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 0.0F, -3.0F, -0.5236F, 0.7854F, 0.0F));

        PartDefinition basePetalTip3 = basePetal3.addOrReplaceChild("basePetalTip3", CubeListBuilder.create().texOffs(32, 34).addBox(-8.0F, 0.0F, -14.0F, 16.0F, 0.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -14.0F, 1.0472F, 0.0F, 0.0F));

        PartDefinition basePetal4 = basePetalSet.addOrReplaceChild("basePetal4", CubeListBuilder.create().texOffs(0, 48).addBox(-8.0F, 0.0F, -14.0F, 16.0F, 0.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -4.0F, -0.5236F, 0.0F, 0.0F));

        PartDefinition basePetalTip4 = basePetal4.addOrReplaceChild("basePetalTip4", CubeListBuilder.create().texOffs(32, 34).addBox(-8.0F, 0.0F, -14.0F, 16.0F, 0.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -14.0F, 1.0472F, 0.0F, 0.0F));

        PartDefinition basePetal5 = basePetalSet.addOrReplaceChild("basePetal5", CubeListBuilder.create().texOffs(0, 48).addBox(-8.0F, 0.0F, -14.0F, 16.0F, 0.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 0.0F, -3.0F, -0.5236F, -0.7854F, 0.0F));

        PartDefinition basePetalTip5 = basePetal5.addOrReplaceChild("basePetalTip5", CubeListBuilder.create().texOffs(32, 34).addBox(-8.0F, 0.0F, -14.0F, 16.0F, 0.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -14.0F, 1.0472F, 0.0F, 0.0F));

        PartDefinition basePetal6 = basePetalSet.addOrReplaceChild("basePetal6", CubeListBuilder.create().texOffs(0, 34).addBox(-8.0F, 0.0F, 0.0F, 16.0F, 0.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 0.0F, 3.0F, 0.5236F, -0.7854F, 0.0F));

        PartDefinition basePetalTip6 = basePetal6.addOrReplaceChild("basePetalTip6", CubeListBuilder.create().texOffs(31, 0).addBox(-8.0F, 0.0F, 0.0F, 16.0F, 0.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 14.0F, -1.0472F, 0.0F, 0.0F));

        PartDefinition basePetal7 = basePetalSet.addOrReplaceChild("basePetal7", CubeListBuilder.create().texOffs(0, 34).addBox(-8.0F, 0.0F, 0.0F, 16.0F, 0.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 4.0F, 0.5236F, 0.0F, 0.0F));

        PartDefinition basePetalTip7 = basePetal7.addOrReplaceChild("basePetalTip7", CubeListBuilder.create().texOffs(31, 0).addBox(-8.0F, 0.0F, 0.0F, 16.0F, 0.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 14.0F, -1.0472F, 0.0F, 0.0F));

        PartDefinition basePetal8 = basePetalSet.addOrReplaceChild("basePetal8", CubeListBuilder.create().texOffs(0, 34).addBox(-8.0F, 0.0F, 0.0F, 16.0F, 0.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 0.0F, 3.0F, 0.5236F, 0.7854F, 0.0F));

        PartDefinition basePetalTip8 = basePetal8.addOrReplaceChild("basePetalTip8", CubeListBuilder.create().texOffs(31, 0).addBox(-8.0F, 0.0F, 0.0F, 16.0F, 0.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 14.0F, -1.0472F, 0.0F, 0.0F));

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
        this.mainStem.yRot = (Mth.lerp(partialTicks, entity.yHeadRotO, entity.yHeadRot) - entity.getSpawnDirection().toYRot()) * Mth.DEG_TO_RAD;
        this.head.yRot += netHeadYaw * Mth.DEG_TO_RAD * 0.3f;
        this.head.xRot += headPitch * Mth.DEG_TO_RAD * 0.3f;
        if (entity.deathTime <= 0 && !entity.playDeath()) {
            this.anim.doAnimation(this, "idle", entity.tickCount, partialTicks);
        }
        this.anim.doAnimation(this, entity.getAnimationHandler(), partialTicks);
        EntityRafflesiaPart horseTail = entity.getHorseTail();
        if (horseTail != null) {
            this.horseTail.visible = true;
            this.anim.doAnimation(this, horseTail.getAnimationHandler(), partialTicks);
        } else {
            this.horseTail.visible = false;
        }
        EntityRafflesiaPart flower = entity.getFlower();
        if (flower != null) {
            this.flower.visible = true;
            this.anim.doAnimation(this, flower.getAnimationHandler(), partialTicks);
        } else {
            this.flower.visible = false;
        }
        EntityRafflesiaPart pitcher = entity.getPitcher();
        if (pitcher != null) {
            this.pitcher.visible = true;
            this.anim.doAnimation(this, pitcher.getAnimationHandler(), partialTicks);
        } else {
            this.pitcher.visible = false;
        }
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
                this.main.translateAndRotate(poseStack);
                this.mainStem.translateAndRotate(poseStack);
                this.mainStem2.translateAndRotate(poseStack);
                this.mainStem3.translateAndRotate(poseStack);
                this.mainStem4.translateAndRotate(poseStack);
                this.head.translateAndRotate(poseStack);
                if (model instanceof SittingModel sittingModel)
                    sittingModel.translateSittingPosition(poseStack);
                else
                    poseStack.translate(0, 6 / 16d, -3 / 16d);
                return true;
            }
        }
        return false;
    }
}