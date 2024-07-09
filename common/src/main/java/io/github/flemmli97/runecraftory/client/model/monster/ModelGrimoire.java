package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.client.model.SittingModel;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.EntityGrimoire;
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

public class ModelGrimoire<T extends EntityGrimoire> extends EntityModel<T> implements ExtendedModel, RideableModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(RuneCraftory.MODID, "grimoire"), "main");

    protected final ModelPartHandler model;
    protected final BlockBenchAnimations anim;

    public ModelPartHandler.ModelPartExtended body;
    public ModelPartHandler.ModelPartExtended body2;
    public ModelPartHandler.ModelPartExtended head;
    public ModelPartHandler.ModelPartExtended ridingPosition;

    public ModelGrimoire(ModelPart root) {
        super();
        this.model = new ModelPartHandler(root, "root");
        this.anim = AnimationManager.getInstance().getAnimation(new ResourceLocation(RuneCraftory.MODID, "grimoire"));
        this.head = this.model.getPart("head");
        this.body = this.model.getPart("body");
        this.body2 = this.model.getPart("body2");
        this.ridingPosition = this.model.getPart("ridingPos");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(37, 41).addBox(-6.5F, -6.0F, -7.5F, 13.0F, 13.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, -1.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition spike = body.addOrReplaceChild("spike", CubeListBuilder.create().texOffs(0, 50).addBox(-1.5F, -5.0F, 0.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.0F, 3.0F, -0.5236F, 0.0F, 0.0F));

        PartDefinition spike2 = body.addOrReplaceChild("spike2", CubeListBuilder.create().texOffs(36, 81).addBox(-1.5F, -5.0F, 0.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.0F, -6.0F, -0.5236F, 0.0F, 0.0F));

        PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create().texOffs(0, 56).addBox(-6.0F, -5.5F, -12.0F, 12.0F, 12.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -7.5F, 0.2182F, 0.0F, 0.0F));

        PartDefinition spike3 = body2.addOrReplaceChild("spike3", CubeListBuilder.create().texOffs(0, 58).addBox(-1.5F, -5.0F, 0.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.5F, -6.5F, -0.5236F, 0.0F, 0.0F));

        PartDefinition body3 = body2.addOrReplaceChild("body3", CubeListBuilder.create().texOffs(50, 69).addBox(-5.0F, -5.25F, -10.0F, 10.0F, 11.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -11.0F, 0.1745F, 0.0F, 0.0F));

        PartDefinition spike4 = body3.addOrReplaceChild("spike4", CubeListBuilder.create().texOffs(104, 31).addBox(-1.0F, -4.0F, 0.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.5F, -3.5F, -0.5236F, 0.0F, 0.0F));

        PartDefinition spike5 = body3.addOrReplaceChild("spike5", CubeListBuilder.create().texOffs(103, 25).addBox(-1.0F, -4.0F, 0.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.5F, -9.5F, -0.5236F, 0.0F, 0.0F));

        PartDefinition head = body3.addOrReplaceChild("head", CubeListBuilder.create().texOffs(59, 10).addBox(-5.5F, -1.5F, -9.0F, 11.0F, 13.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(101, 91).addBox(-4.5F, 1.5F, -11.0F, 9.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 34).addBox(-2.5F, -2.5F, -10.0F, 5.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.0F, -10.0F, 0.4363F, 0.0F, 0.0F));

        PartDefinition spike6 = head.addOrReplaceChild("spike6", CubeListBuilder.create().texOffs(19, 99).addBox(-1.0F, -5.0F, 0.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.5F, -4.5F, -0.2618F, 0.0F, 0.0F));

        PartDefinition spike8 = head.addOrReplaceChild("spike8", CubeListBuilder.create().texOffs(103, 19).addBox(-0.5F, -4.0F, 0.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.5F, -1.5F, -7.0F, -0.5236F, 0.0F, 0.2618F));

        PartDefinition spike7 = head.addOrReplaceChild("spike7", CubeListBuilder.create().texOffs(78, 40).addBox(-1.5F, -4.0F, 0.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5F, -1.5F, -7.0F, -0.5236F, 0.0F, -0.2618F));

        PartDefinition jawLower = head.addOrReplaceChild("jawLower", CubeListBuilder.create().texOffs(98, 53).addBox(-4.0F, 0.0F, -6.0F, 8.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(92, 70).addBox(-4.0F, -0.75F, -6.0F, 8.0F, 1.0F, 7.0F, new CubeDeformation(-0.25F)), PartPose.offset(0.0F, 9.0F, -11.0F));

        PartDefinition jawUpper = head.addOrReplaceChild("jawUpper", CubeListBuilder.create().texOffs(92, 4).addBox(-4.0F, -4.0F, -6.5F, 8.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(92, 78).addBox(-4.0F, -0.25F, -6.0F, 8.0F, 1.0F, 7.0F, new CubeDeformation(-0.25F)), PartPose.offset(0.0F, 9.0F, -11.0F));

        PartDefinition hornLeft = head.addOrReplaceChild("hornLeft", CubeListBuilder.create().texOffs(98, 61).addBox(0.0F, -5.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, -0.5F, 0.0F, 0.0F, 0.0F, 0.6109F));

        PartDefinition hornLeft2 = hornLeft.addOrReplaceChild("hornLeft2", CubeListBuilder.create().texOffs(63, 102).addBox(-2.0F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, -5.0F, 0.0F, 0.0F, 0.0F, -0.4363F));

        PartDefinition hornLeft3 = hornLeft2.addOrReplaceChild("hornLeft3", CubeListBuilder.create().texOffs(104, 37).addBox(-2.0F, -6.0F, -0.5F, 2.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -5.0F, 0.0F, 0.0F, 0.0F, -0.3927F));

        PartDefinition hornRight = head.addOrReplaceChild("hornRight", CubeListBuilder.create().texOffs(0, 42).addBox(-3.0F, -5.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, -0.5F, 0.0F, 0.0F, 0.0F, -0.6109F));

        PartDefinition hornRight2 = hornRight.addOrReplaceChild("hornRight2", CubeListBuilder.create().texOffs(50, 69).addBox(0.0F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, -5.0F, 0.0F, 0.0F, 0.0F, 0.4363F));

        PartDefinition hornRight3 = hornRight2.addOrReplaceChild("hornRight3", CubeListBuilder.create().texOffs(12, 42).addBox(0.0F, -6.0F, -0.5F, 2.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -5.0F, 0.0F, 0.0F, 0.0F, 0.3927F));

        PartDefinition leftWing = body2.addOrReplaceChild("leftWing", CubeListBuilder.create().texOffs(92, 15).addBox(-1.0F, -1.0F, -1.0F, 15.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 38).addBox(-1.0F, 0.0F, -0.5F, 17.0F, 0.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, -2.0F, -9.5F, 0.0F, -0.2618F, -0.4363F));

        PartDefinition leftWing2 = leftWing.addOrReplaceChild("leftWing2", CubeListBuilder.create().texOffs(69, 0).addBox(-1.0F, -1.0F, -0.75F, 17.0F, 2.0F, 2.0F, new CubeDeformation(0.025F))
                .texOffs(0, 0).addBox(-8.5F, -0.025F, -0.25F, 24.0F, 0.0F, 21.0F, new CubeDeformation(0.0F))
                .texOffs(72, 72).addBox(0.0F, -0.5F, 1.25F, 1.0F, 1.0F, 18.0F, new CubeDeformation(0.0F))
                .texOffs(80, 53).addBox(14.5F, -0.5F, 1.25F, 1.0F, 1.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(0, 28).addBox(14.5F, -0.5F, -5.75F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(14.0F, 0.0F, -0.25F, 0.0797F, 0.7383F, 0.729F));

        PartDefinition leftWing3 = leftWing2.addOrReplaceChild("leftWing3", CubeListBuilder.create().texOffs(52, 38).addBox(0.0F, -0.5F, -0.5F, 16.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 21).addBox(-4.0F, 0.0F, -0.5F, 21.0F, 0.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.0F, 0.0F, 0.25F, 0.0F, -0.48F, 0.2618F));

        PartDefinition rightWing = body2.addOrReplaceChild("rightWing", CubeListBuilder.create().texOffs(92, 15).mirror().addBox(-14.0F, -1.0F, -1.0F, 15.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 38).mirror().addBox(-16.0F, 0.0F, -0.5F, 17.0F, 0.0F, 18.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-6.0F, -2.0F, -9.5F, 0.0F, 0.2618F, 0.4363F));

        PartDefinition rightWing2 = rightWing.addOrReplaceChild("rightWing2", CubeListBuilder.create().texOffs(69, 0).mirror().addBox(-16.0F, -1.0F, -0.75F, 17.0F, 2.0F, 2.0F, new CubeDeformation(0.025F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(-15.5F, -0.025F, -0.25F, 24.0F, 0.0F, 21.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(72, 72).mirror().addBox(-1.0F, -0.5F, 1.25F, 1.0F, 1.0F, 18.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(80, 53).mirror().addBox(-15.5F, -0.5F, 1.25F, 1.0F, 1.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 28).mirror().addBox(-15.5F, -0.5F, -5.75F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-14.0F, 0.0F, -0.25F, 0.0797F, -0.7383F, -0.729F));

        PartDefinition rightWing3 = rightWing2.addOrReplaceChild("rightWing3", CubeListBuilder.create().texOffs(52, 38).mirror().addBox(-16.0F, -0.5F, -0.5F, 16.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 21).mirror().addBox(-17.0F, 0.0F, -0.5F, 21.0F, 0.0F, 17.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-15.0F, 0.0F, 0.25F, 0.0F, 0.48F, -0.2618F));

        PartDefinition ridingPos = body2.addOrReplaceChild("ridingPos", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -5.5F, -9.0F, 0.8727F, 0.0F, 0.0F));

        PartDefinition leftLegBase = body.addOrReplaceChild("leftLegBase", CubeListBuilder.create().texOffs(0, 99).addBox(-1.25F, -3.5F, -3.5F, 6.0F, 15.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.75F, 3.0F, 2.5F, 0.7854F, 0.0F, 0.0F));

        PartDefinition leftLeg2 = leftLegBase.addOrReplaceChild("leftLeg2", CubeListBuilder.create().texOffs(0, 10).addBox(-2.0F, -2.0F, -2.75F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 11.5F, 3.25F));

        PartDefinition leftFeet = leftLeg2.addOrReplaceChild("leftFeet", CubeListBuilder.create().texOffs(0, 23).addBox(-2.0F, 0.0F, -3.25F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.0F, 0.5F));

        PartDefinition leftFeetClaw = leftFeet.addOrReplaceChild("leftFeetClaw", CubeListBuilder.create().texOffs(77, 5).addBox(0.0F, -0.5F, -4.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, -3.25F, 0.0F, -0.3491F, 0.0F));

        PartDefinition leftFeetClaw2 = leftFeet.addOrReplaceChild("leftFeetClaw2", CubeListBuilder.create().texOffs(69, 4).addBox(-2.0F, -0.5F, -4.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, -3.25F, 0.0F, 0.3491F, 0.0F));

        PartDefinition leftFeetClaw3 = leftFeet.addOrReplaceChild("leftFeetClaw3", CubeListBuilder.create().texOffs(7, 28).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.5F, 0.25F));

        PartDefinition rightLegBase = body.addOrReplaceChild("rightLegBase", CubeListBuilder.create().texOffs(0, 99).mirror().addBox(-4.75F, -3.5F, -3.5F, 6.0F, 15.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-5.75F, 3.0F, 2.5F, 0.7854F, 0.0F, 0.0F));

        PartDefinition rightLeg2 = rightLegBase.addOrReplaceChild("rightLeg2", CubeListBuilder.create().texOffs(0, 10).mirror().addBox(-2.0F, -2.0F, -2.75F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.0F, 11.5F, 3.25F));

        PartDefinition rightFeet = rightLeg2.addOrReplaceChild("rightFeet", CubeListBuilder.create().texOffs(0, 23).mirror().addBox(-2.0F, 0.0F, -3.25F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 7.0F, 0.5F));

        PartDefinition rightFeetClaw = rightFeet.addOrReplaceChild("rightFeetClaw", CubeListBuilder.create().texOffs(77, 5).mirror().addBox(-2.0F, -0.5F, -4.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.5F, -3.25F, 0.0F, 0.3491F, 0.0F));

        PartDefinition rightFeetClaw2 = rightFeet.addOrReplaceChild("rightFeetClaw2", CubeListBuilder.create().texOffs(69, 4).mirror().addBox(0.0F, -0.5F, -4.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.5F, -3.25F, 0.0F, -0.3491F, 0.0F));

        PartDefinition rightFeetClaw3 = rightFeet.addOrReplaceChild("rightFeetClaw3", CubeListBuilder.create().texOffs(7, 28).mirror().addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.5F, 0.25F));

        PartDefinition tailBase = body.addOrReplaceChild("tailBase", CubeListBuilder.create().texOffs(0, 81).addBox(-5.5F, -5.5F, -2.0F, 11.0F, 11.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, 7.5F, 0.2182F, 0.0F, 0.0F));

        PartDefinition spike9 = tailBase.addOrReplaceChild("spike9", CubeListBuilder.create().texOffs(54, 90).addBox(-1.0F, -5.0F, 0.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.5F, 2.5F, -0.5236F, 0.0F, 0.0F));

        PartDefinition tail = tailBase.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(27, 90).addBox(-4.5F, -4.5F, -1.5F, 9.0F, 9.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 4.5F, 0.3054F, 0.0F, 0.0F));

        PartDefinition spike10 = tail.addOrReplaceChild("spike10", CubeListBuilder.create().texOffs(80, 70).addBox(-1.0F, -5.0F, 0.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.5F, 4.0F, -0.5236F, 0.0F, 0.0F));

        PartDefinition tail2 = tail.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(78, 34).addBox(-3.5F, -3.5F, -1.0F, 7.0F, 7.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 7.5F, 0.2618F, 0.0F, 0.0F));

        PartDefinition spike11 = tail2.addOrReplaceChild("spike11", CubeListBuilder.create().texOffs(71, 102).addBox(-0.75F, -4.0F, 0.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, 2.0F, -0.5236F, 0.0F, 0.0F));

        PartDefinition spike12 = tail2.addOrReplaceChild("spike12", CubeListBuilder.create().texOffs(89, 4).addBox(-0.75F, -4.0F, 0.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, 7.0F, -0.5236F, 0.0F, 0.0F));

        PartDefinition tail3 = tail2.addOrReplaceChild("tail3", CubeListBuilder.create().texOffs(79, 91).addBox(-2.5F, -2.5F, -1.0F, 5.0F, 5.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 11.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition spike13 = tail3.addOrReplaceChild("spike13", CubeListBuilder.create().texOffs(0, 0).addBox(-0.25F, -4.0F, 0.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, 2.0F, -0.5236F, 0.0F, 0.0F));

        PartDefinition spike14 = tail3.addOrReplaceChild("spike14", CubeListBuilder.create().texOffs(0, 10).addBox(-0.25F, -3.0F, 0.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, 6.5F, -0.5236F, 0.0F, 0.0F));

        PartDefinition tailTipBase = tail3.addOrReplaceChild("tailTipBase", CubeListBuilder.create(), PartPose.offset(0.0F, -0.25F, 11.25F));

        PartDefinition tailTipOuter = tailTipBase.addOrReplaceChild("tailTipOuter", CubeListBuilder.create().texOffs(63, 77).addBox(0.0F, -5.5F, -0.25F, 0.0F, 11.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition tailTip = tailTipBase.addOrReplaceChild("tailTip", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0F, -3.0F, 4.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.25F, 2.75F, 0.0F, 0.0F, 0.7854F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.model.getMainPart().render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.model.resetPoses();
        this.head.yRot += (netHeadYaw % 360) * Mth.DEG_TO_RAD * 0.5f;
        this.head.xRot += headPitch * Mth.DEG_TO_RAD * 0.5f;
        float partialTicks = Minecraft.getInstance().getFrameTime();
        if (entity.deathTime <= 0 && !entity.playDeath()) {
            this.anim.doAnimation(this, "idle", entity.tickCount, partialTicks);
            if (!entity.isOnGround())
                this.anim.doAnimation(this, "fly", entity.tickCount, partialTicks);
            else if (entity.moveTick() > 0) {
                if (entity.getMoveFlag() == BaseMonster.MoveType.RUN)
                    this.anim.doAnimation(this, "fly", entity.tickCount, partialTicks, entity.interpolatedMoveTick(partialTicks));
                else
                    this.anim.doAnimation(this, "walk", entity.tickCount, partialTicks, entity.interpolatedMoveTick(partialTicks));
            }
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
                this.body2.translateAndRotate(poseStack);
                this.ridingPosition.translateAndRotate(poseStack);
                ClientHandlers.translateRider(entityRenderer, rider, model, poseStack);
                return true;
            }
        }
        return false;
    }
}