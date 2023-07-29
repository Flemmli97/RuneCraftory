package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.SittingModel;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.EntitySkelefang;
import io.github.flemmli97.runecraftory.common.particles.SkelefangParticleData;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
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
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

import java.util.function.Function;

public class ModelSkelefang<T extends EntitySkelefang> extends EntityModel<T> implements ExtendedModel, RideableModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(RuneCraftory.MODID, "skelefang"), "main");

    protected final ModelPartHandler model;
    protected final BlockBenchAnimations anim;

    public ModelPartHandler.ModelPartExtended head;
    public ModelPartHandler.ModelPartExtended neck;
    public ModelPartHandler.ModelPartExtended body;
    public ModelPartHandler.ModelPartExtended spineFront;
    public ModelPartHandler.ModelPartExtended ribsBody;
    public ModelPartHandler.ModelPartExtended spineBack;
    public ModelPartHandler.ModelPartExtended ribsSpine;
    public ModelPartHandler.ModelPartExtended leftLegBase;
    public ModelPartHandler.ModelPartExtended rightLegBase;
    public ModelPartHandler.ModelPartExtended tailBase;
    public ModelPartHandler.ModelPartExtended tail;

    public ModelPartHandler.ModelPartExtended bone1;
    public ModelPartHandler.ModelPartExtended bone2;

    public ModelPartHandler.ModelPartExtended heart;

    private int beamTick = -1, entityTick;
    private boolean translucentTail, translucentTailBase, translucentSpineBack, translucentSpineFront, translucentBackRibs, translucentFrontRibs;

    public ModelSkelefang(ModelPart root, Function<ResourceLocation, RenderType> function) {
        super(function);
        this.model = new ModelPartHandler(root, "root");
        this.anim = AnimationManager.getInstance().getAnimation(new ResourceLocation(RuneCraftory.MODID, "skelefang"));
        this.head = this.model.getPart("head");
        this.neck = this.model.getPart("neckSpine");
        this.body = this.model.getPart("body");
        this.spineFront = this.model.getPart("spineFront");
        this.ribsBody = this.model.getPart("ribsBody");
        this.spineBack = this.model.getPart("spineBack");
        this.ribsSpine = this.model.getPart("ribsSpine");
        this.leftLegBase = this.model.getPart("legLeftConnectorBase");
        this.rightLegBase = this.model.getPart("legRightConnectorBase");
        this.tailBase = this.model.getPart("tailBase");
        this.tail = this.model.getPart("tail");
        this.bone1 = this.model.getPart("randomBone");
        this.bone2 = this.model.getPart("randomBone2");
        this.heart = this.model.getPart("heartYAxis");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -15.5F, -13.0F));

        PartDefinition spineFront = body.addOrReplaceChild("spineFront", CubeListBuilder.create().texOffs(40, 39).addBox(-4.0F, -4.5F, -10.0F, 8.0F, 8.0F, 24.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-1.5F, -12.5F, 0.0F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(84, 85).addBox(-1.0F, -9.5F, -7.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 63).addBox(-2.0F, -14.5F, 8.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition ribsBody = spineFront.addOrReplaceChild("ribsBody", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition ribBaseLeft = ribsBody.addOrReplaceChild("ribBaseLeft", CubeListBuilder.create().texOffs(80, 187).addBox(-0.5F, 0.0F, -2.0F, 16.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -3.5F, -7.0F, 0.0F, 0.0F, 0.7418F));

        PartDefinition ribOtherLeft = ribBaseLeft.addOrReplaceChild("ribOtherLeft", CubeListBuilder.create().texOffs(183, 65).addBox(0.0F, 0.0F, -2.0F, 17.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.5F, 0.0F, 0.0F, 0.0F, 0.0F, 1.3963F));

        PartDefinition ribBaseLeft2 = ribsBody.addOrReplaceChild("ribBaseLeft2", CubeListBuilder.create().texOffs(96, 138).addBox(-0.5F, 0.0F, -3.0F, 23.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -3.5F, 0.5F, 0.0F, 0.0F, 0.5672F));

        PartDefinition legLeftConnectorBase = ribBaseLeft2.addOrReplaceChild("legLeftConnectorBase", CubeListBuilder.create().texOffs(179, 26).addBox(0.0F, 0.0F, -3.0F, 16.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.5F, 0.0F, 0.0F, -0.0873F, 0.0F, -0.7418F));

        PartDefinition legLeftConnector = legLeftConnectorBase.addOrReplaceChild("legLeftConnector", CubeListBuilder.create().texOffs(42, 173).addBox(0.0F, 0.0F, -3.0F, 17.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(16.0F, 1.0F, 0.0F, 0.0F, -0.0873F, 1.0908F));

        PartDefinition legLeftBase = legLeftConnector.addOrReplaceChild("legLeftBase", CubeListBuilder.create().texOffs(78, 0).addBox(-1.0F, -3.5F, 0.0F, 27.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, 2.5F, 3.0F, 0.1222F, -0.5061F, 0.5585F));

        PartDefinition leftLeg = legLeftBase.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(0, 63).addBox(0.0F, -3.0F, -11.5F, 6.0F, 6.0F, 22.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(26.0F, 0.0F, -0.5F, 0.1745F, -0.0873F, -0.0873F));

        PartDefinition leftClaws = leftLeg.addOrReplaceChild("leftClaws", CubeListBuilder.create(), PartPose.offsetAndRotation(3.0F, 0.0F, -11.5F, 0.0F, -1.0036F, 0.0F));

        PartDefinition leftClaw = leftClaws.addOrReplaceChild("leftClaw", CubeListBuilder.create().texOffs(82, 172).addBox(-14.0F, -2.0F, -1.5F, 14.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, -0.5F, -1.5F, 0.0F, 0.0F, -0.8727F));

        PartDefinition leftClaw2 = leftClaws.addOrReplaceChild("leftClaw2", CubeListBuilder.create().texOffs(82, 172).addBox(-14.0F, -2.0F, -1.5F, 14.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, -0.5F, -1.5F));

        PartDefinition leftClaw3 = leftClaws.addOrReplaceChild("leftClaw3", CubeListBuilder.create().texOffs(82, 172).addBox(-14.0F, -2.0F, -1.5F, 14.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, -0.5F, -1.5F, 0.0F, 0.0F, 0.8727F));

        PartDefinition ribOtherLeft2 = ribBaseLeft2.addOrReplaceChild("ribOtherLeft2", CubeListBuilder.create().texOffs(142, 109).addBox(0.0F, 0.0F, -3.0F, 22.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(22.5F, 0.0F, 0.0F, 0.0F, 0.0F, 1.1345F));

        PartDefinition ribBaseLeft3 = ribsBody.addOrReplaceChild("ribBaseLeft3", CubeListBuilder.create().texOffs(129, 71).addBox(-0.5F, 0.0F, -3.0F, 24.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -3.5F, 9.5F, 0.0F, 0.0F, 0.48F));

        PartDefinition ribOtherLeft3 = ribBaseLeft3.addOrReplaceChild("ribOtherLeft3", CubeListBuilder.create().texOffs(123, 31).addBox(0.0F, 0.0F, -3.0F, 25.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(23.5F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0472F));

        PartDefinition ribBaseRight = ribsBody.addOrReplaceChild("ribBaseRight", CubeListBuilder.create().texOffs(187, 18).addBox(-15.5F, 0.0F, -2.0F, 16.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -3.5F, -7.0F, 0.0F, 0.0F, -0.7418F));

        PartDefinition ribOtherRight = ribBaseRight.addOrReplaceChild("ribOtherRight", CubeListBuilder.create().texOffs(181, 10).addBox(-17.0F, 0.0F, -2.0F, 17.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.5F, 0.0F, 0.0F, 0.0F, 0.0F, -1.3963F));

        PartDefinition ribBaseRight2 = ribsBody.addOrReplaceChild("ribBaseRight2", CubeListBuilder.create().texOffs(138, 43).addBox(-22.5F, 0.0F, -3.0F, 23.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -3.5F, 0.5F, 0.0F, 0.0F, -0.5672F));

        PartDefinition legRightConnectorBase = ribBaseRight2.addOrReplaceChild("legRightConnectorBase", CubeListBuilder.create().texOffs(178, 131).addBox(-16.0F, 0.0F, -3.0F, 16.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.5F, 0.0F, 0.0F, -0.0873F, 0.0F, 0.7418F));

        PartDefinition legRightConnector = legRightConnectorBase.addOrReplaceChild("legRightConnector", CubeListBuilder.create().texOffs(134, 171).addBox(-17.0F, 0.0F, -3.0F, 17.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-16.0F, 1.0F, 0.0F, 0.0F, 0.0873F, -1.0908F));

        PartDefinition legRightBase = legRightConnector.addOrReplaceChild("legRightBase", CubeListBuilder.create().texOffs(34, 71).addBox(-26.0F, -3.5F, 0.0F, 27.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, 2.5F, 3.0F, 0.1222F, 0.5061F, -0.5585F));

        PartDefinition rightLeg = legRightBase.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(44, 9).addBox(-6.0F, -3.0F, -11.5F, 6.0F, 6.0F, 22.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-26.0F, 0.0F, -0.5F, 0.1745F, 0.0873F, 0.0873F));

        PartDefinition rightClaws = rightLeg.addOrReplaceChild("rightClaws", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.0F, 0.0F, -11.5F, 0.0F, 1.0036F, 0.0F));

        PartDefinition rightClaw = rightClaws.addOrReplaceChild("rightClaw", CubeListBuilder.create().texOffs(51, 0).addBox(0.0F, -2.0F, -1.5F, 14.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, -0.5F, -1.5F, 0.0F, 0.0F, 0.8727F));

        PartDefinition rightClaw2 = rightClaws.addOrReplaceChild("rightClaw2", CubeListBuilder.create().texOffs(51, 0).addBox(0.0F, -2.0F, -1.5F, 14.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, -0.5F, -1.5F));

        PartDefinition rightClaw3 = rightClaws.addOrReplaceChild("rightClaw3", CubeListBuilder.create().texOffs(51, 0).addBox(0.0F, -2.0F, -1.5F, 14.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, -0.5F, -1.5F, 0.0F, 0.0F, -0.8727F));

        PartDefinition ribOtherRight2 = ribBaseRight2.addOrReplaceChild("ribOtherRight2", CubeListBuilder.create().texOffs(142, 83).addBox(-22.0F, 0.0F, -3.0F, 22.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-22.5F, 0.0F, 0.0F, 0.0F, 0.0F, -1.1345F));

        PartDefinition ribBaseRight3 = ribsBody.addOrReplaceChild("ribBaseRight3", CubeListBuilder.create().texOffs(0, 129).addBox(-23.5F, 0.0F, -3.0F, 24.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -3.5F, 9.5F, 0.0F, 0.0F, -0.48F));

        PartDefinition ribOtherRight3 = ribBaseRight3.addOrReplaceChild("ribOtherRight3", CubeListBuilder.create().texOffs(62, 122).addBox(-25.0F, 0.0F, -3.0F, 25.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-23.5F, 0.0F, 0.0F, 0.0F, 0.0F, -1.0472F));

        PartDefinition neckSpine = spineFront.addOrReplaceChild("neckSpine", CubeListBuilder.create().texOffs(92, 97).addBox(-3.0F, -3.0F, -19.0F, 6.0F, 6.0F, 19.0F, new CubeDeformation(0.0F))
                .texOffs(80, 37).addBox(-1.5F, -9.0F, -5.5F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(123, 43).addBox(-1.0F, -10.0F, -10.5F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(92, 109).addBox(-7.0F, -1.0F, -11.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(92, 103).addBox(-9.0F, -1.5F, -6.0F, 6.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(27, 102).addBox(3.0F, -1.5F, -6.0F, 6.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(98, 59).addBox(3.0F, -1.0F, -11.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.5F, -9.5F, 0.0873F, 0.0F, 0.0F));

        PartDefinition head = neckSpine.addOrReplaceChild("head", CubeListBuilder.create().texOffs(48, 85).addBox(-7.5F, -18.0F, -8.0F, 14.0F, 24.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-9.5F, -17.0F, -23.0F, 18.0F, 16.0F, 15.0F, new CubeDeformation(0.0F))
                .texOffs(92, 72).addBox(-6.5F, -13.0F, -36.0F, 12.0F, 12.0F, 13.0F, new CubeDeformation(0.0F))
                .texOffs(56, 117).addBox(-6.5F, -15.0F, -36.0F, 12.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(165, 157).addBox(-6.5F, -1.0F, -36.0F, 12.0F, 1.0F, 13.0F, new CubeDeformation(-0.025F))
                .texOffs(78, 14).addBox(-9.0F, -1.0F, -23.0F, 17.0F, 2.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, -15.0F, -0.1745F, 0.0F, 0.0F));

        PartDefinition headBack = head.addOrReplaceChild("headBack", CubeListBuilder.create().texOffs(170, 172).addBox(-5.5F, -7.0F, 0.0F, 10.0F, 7.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -11.0F, 0.0F, 0.4363F, 0.0F, 0.0F));

        PartDefinition horn = head.addOrReplaceChild("horn", CubeListBuilder.create().texOffs(0, 31).addBox(-3.5F, -17.0F, -6.0F, 6.0F, 17.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -17.0F, -11.0F, 0.7418F, 0.0F, 0.0F));

        PartDefinition jaw = head.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(80, 37).addBox(-7.5F, 0.0F, -15.0F, 14.0F, 7.0F, 15.0F, new CubeDeformation(0.0F))
                .texOffs(109, 122).addBox(-7.5F, -1.0F, -15.0F, 14.0F, 1.0F, 15.0F, new CubeDeformation(-0.025F))
                .texOffs(141, 138).addBox(-6.5F, 0.0F, -28.0F, 12.0F, 6.0F, 13.0F, new CubeDeformation(0.0F))
                .texOffs(31, 159).addBox(-6.5F, -1.0F, -28.0F, 12.0F, 1.0F, 13.0F, new CubeDeformation(-0.025F)), PartPose.offsetAndRotation(0.0F, -1.0F, -8.0F, 0.1309F, 0.0F, 0.0F));

        PartDefinition heartYAxis = body.addOrReplaceChild("heartYAxis", CubeListBuilder.create(), PartPose.offset(0.0F, 9.5F, 13.0F));

        PartDefinition heartZAxis = heartYAxis.addOrReplaceChild("heartZAxis", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 7.0F, 0.0F, -0.6109F, 0.0F, 0.0F));

        PartDefinition heartXAxis = heartZAxis.addOrReplaceChild("heartXAxis", CubeListBuilder.create().texOffs(48, 134).addBox(-6.0F, -6.0F, -6.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition spineBack = body.addOrReplaceChild("spineBack", CubeListBuilder.create().texOffs(0, 31).addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 24.0F, new CubeDeformation(0.0F))
                .texOffs(0, 91).addBox(-2.0F, -13.0F, 2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(27, 91).addBox(-2.0F, -11.0F, 10.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(78, 14).addBox(-1.5F, -10.0F, 18.0F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.5F, 14.0F, -0.0873F, 0.0F, 0.0F));

        PartDefinition ribsSpine = spineBack.addOrReplaceChild("ribsSpine", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition ribBaseLeft4 = ribsSpine.addOrReplaceChild("ribBaseLeft4", CubeListBuilder.create().texOffs(127, 14).addBox(-0.5F, 0.0F, -3.0F, 24.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -3.0F, 5.0F, 0.0F, 0.0F, 0.5236F));

        PartDefinition ribOtherLeft4 = ribBaseLeft4.addOrReplaceChild("ribOtherLeft4", CubeListBuilder.create().texOffs(0, 117).addBox(0.0F, 0.0F, -3.0F, 25.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(23.5F, 0.0F, 0.0F, 0.0F, 0.0F, 1.2217F));

        PartDefinition ribBaseLeft5 = ribsSpine.addOrReplaceChild("ribBaseLeft5", CubeListBuilder.create().texOffs(160, 55).addBox(-0.5F, 0.0F, -2.5F, 22.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -3.0F, 13.75F, 0.0F, 0.0F, 0.5672F));

        PartDefinition ribOtherLeft5 = ribBaseLeft5.addOrReplaceChild("ribOtherLeft5", CubeListBuilder.create().texOffs(124, 157).addBox(0.0F, 0.0F, -2.5F, 22.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(21.5F, 0.0F, 0.0F, 0.0F, 0.0F, 1.3526F));

        PartDefinition ribBaseLeft6 = ribsSpine.addOrReplaceChild("ribBaseLeft6", CubeListBuilder.create().texOffs(178, 142).addBox(-0.5F, 0.0F, -2.0F, 17.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -3.0F, 21.5F, 0.0F, 0.0F, 0.6545F));

        PartDefinition ribOtherLeft6 = ribBaseLeft6.addOrReplaceChild("ribOtherLeft6", CubeListBuilder.create().texOffs(40, 184).addBox(0.0F, 0.0F, -2.0F, 16.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(16.5F, 0.0F, 0.0F, 0.0F, 0.0F, 1.3526F));

        PartDefinition ribBaseRight4 = ribsSpine.addOrReplaceChild("ribBaseRight4", CubeListBuilder.create().texOffs(123, 97).addBox(-23.5F, 0.0F, -3.0F, 24.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -3.0F, 5.0F, 0.0F, 0.0F, -0.5236F));

        PartDefinition ribOtherRight4 = ribBaseRight4.addOrReplaceChild("ribOtherRight4", CubeListBuilder.create().texOffs(104, 59).addBox(-25.0F, 0.0F, -3.0F, 25.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-23.5F, 0.0F, 0.0F, 0.0F, 0.0F, -1.2217F));

        PartDefinition ribBaseRight5 = ribsSpine.addOrReplaceChild("ribBaseRight5", CubeListBuilder.create().texOffs(152, 121).addBox(-21.5F, 0.0F, -2.5F, 22.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -3.0F, 13.75F, 0.0F, 0.0F, -0.5672F));

        PartDefinition ribOtherRight5 = ribBaseRight5.addOrReplaceChild("ribOtherRight5", CubeListBuilder.create().texOffs(146, 0).addBox(-22.0F, 0.0F, -2.5F, 22.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-21.5F, 0.0F, 0.0F, 0.0F, 0.0F, -1.3526F));

        PartDefinition ribBaseRight6 = ribsSpine.addOrReplaceChild("ribBaseRight6", CubeListBuilder.create().texOffs(177, 95).addBox(-16.5F, 0.0F, -2.0F, 17.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -3.0F, 21.5F, 0.0F, 0.0F, -0.6545F));

        PartDefinition ribOtherRight6 = ribBaseRight6.addOrReplaceChild("ribOtherRight6", CubeListBuilder.create().texOffs(0, 184).addBox(-16.0F, 0.0F, -2.0F, 16.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-16.5F, 0.0F, 0.0F, 0.0F, 0.0F, -1.3526F));

        PartDefinition tailBase = spineBack.addOrReplaceChild("tailBase", CubeListBuilder.create().texOffs(80, 150).addBox(-3.0F, -3.5F, 0.0F, 6.0F, 6.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, 23.5F, -0.1745F, 0.0F, 0.0F));

        PartDefinition tail = tailBase.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(109, 167).addBox(-2.5F, -2.5F, 0.0F, 5.0F, 5.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.5F, 15.5F, -0.2618F, 0.0F, 0.0F));

        PartDefinition tail2 = tail.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(0, 141).addBox(-2.5F, -2.5F, 0.0F, 5.0F, 5.0F, 17.0F, new CubeDeformation(0.0F))
                .texOffs(78, 23).addBox(-6.5F, -1.5F, 4.0F, 4.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(100, 31).addBox(-8.5F, -1.5F, 12.0F, 6.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(51, 7).addBox(2.5F, -1.5F, 4.0F, 4.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(92, 97).addBox(2.5F, -1.5F, 12.0F, 6.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 15.0F, 0.3491F, 0.0F, 0.0F));

        PartDefinition tail3 = tail2.addOrReplaceChild("tail3", CubeListBuilder.create().texOffs(0, 163).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 17.0F, new CubeDeformation(0.0F))
                .texOffs(40, 43).addBox(-10.5F, -1.5F, 4.0F, 9.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(40, 37).addBox(1.5F, -1.5F, 4.0F, 9.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 77).addBox(-8.5F, -1.5F, 11.0F, 7.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(40, 49).addBox(1.5F, -1.5F, 11.0F, 7.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 17.0F, 0.1745F, 0.0F, 0.0F));

        PartDefinition tail4 = tail3.addOrReplaceChild("tail4", CubeListBuilder.create().texOffs(0, 91).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 21.0F, new CubeDeformation(0.0F))
                .texOffs(80, 46).addBox(-6.5F, -1.0F, 12.0F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(27, 108).addBox(-7.5F, -1.0F, 7.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 104).addBox(-8.5F, -1.0F, 2.0F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 11).addBox(1.5F, -1.0F, 12.0F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 108).addBox(1.5F, -1.0F, 7.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(80, 59).addBox(1.5F, -1.0F, 2.0F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 17.0F, 0.3054F, 0.0F, 0.0F));

        PartDefinition randomBone = partdefinition.addOrReplaceChild("randomBone", CubeListBuilder.create().texOffs(40, 39).addBox(-4.0F, -4.0F, -12.0F, 8.0F, 8.0F, 24.0F, new CubeDeformation(-2.75F)), PartPose.offset(0.0F, 22.75F, 0.0F));

        PartDefinition randomBone2 = partdefinition.addOrReplaceChild("randomBone2", CubeListBuilder.create().texOffs(187, 18).addBox(-8.0F, -2.0F, -2.0F, 16.0F, 4.0F, 4.0F, new CubeDeformation(-1.5F)), PartPose.offsetAndRotation(0.0F, 23.5F, 0.0F, 0.0F, -1.5708F, 0.0F));

        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (this.beamTick >= 220) {
            float newAlpha = Math.min(1, (this.beamTick - 220) / 20f);
            this.translateTo(poseStack, this.body);
            this.heart.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            this.spineBack.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, newAlpha);
            this.spineFront.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, newAlpha);
        } else {
            this.body.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        }

        float translucent = 0.4f + Mth.sin(this.entityTick * 0.3f) * 0.2f;
        poseStack.pushPose();
        if (this.translucentSpineBack) {
            this.spineBack.visible = true;
            this.tail.visible = true;
            this.tailBase.visible = true;
            this.ribsSpine.visible = true;
            this.translateTo(poseStack, this.body);
            this.spineBack.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, translucent);
            this.spineBack.visible = false;
            this.tail.visible = false;
            this.tailBase.visible = false;
            this.ribsSpine.visible = false;
        } else {
            if (this.translucentBackRibs) {
                this.ribsSpine.visible = true;
                poseStack.pushPose();
                this.translateTo(poseStack, this.body, this.spineBack);
                this.ribsSpine.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, translucent);
                poseStack.popPose();
                this.ribsSpine.visible = false;
            }
            if (this.translucentTailBase) {
                this.tail.visible = true;
                this.tailBase.visible = true;
                this.translateTo(poseStack, this.body, this.spineBack);
                this.tailBase.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, translucent);
                this.tail.visible = false;
                this.tailBase.visible = false;
            } else if (this.translucentTail) {
                this.tail.visible = true;
                this.translateTo(poseStack, this.body, this.spineBack, this.tailBase);
                this.tail.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, translucent);
                this.tail.visible = false;
            }
        }
        poseStack.popPose();
        poseStack.pushPose();
        if (this.translucentSpineFront) {
            this.spineFront.visible = true;
            this.ribsBody.visible = true;
            this.translateTo(poseStack, this.body);
            this.spineFront.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, translucent);
            this.ribsBody.visible = false;
            this.spineFront.visible = false;
        } else if (this.translucentFrontRibs) {
            this.ribsBody.visible = true;
            this.translateTo(poseStack, this.body, this.spineFront);
            this.ribsBody.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, translucent);
            this.ribsBody.visible = false;
        }
        poseStack.popPose();
    }

    private void translateTo(PoseStack stack, ModelPartHandler.ModelPartExtended... parts) {
        for (ModelPartHandler.ModelPartExtended part : parts)
            part.translateAndRotate(stack);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.model.resetPoses();
        this.body.setAllVisible(true);
        this.updateFromBones(entity);
        this.entityTick = entity.tickCount;
        AnimatedAction anim = entity.getAnimationHandler().getAnimation();
        float partialTicks = Minecraft.getInstance().getFrameTime();
        if (entity.deathTime <= 0 && !entity.playDeath()) {
            this.neck.yRot += netHeadYaw * 0.2 * Mth.DEG_TO_RAD;
            this.neck.xRot += headPitch * 0.2 * Mth.DEG_TO_RAD;
            this.head.yRot += netHeadYaw * 0.4 * Mth.DEG_TO_RAD;
            this.head.xRot += headPitch * 0.4 * Mth.DEG_TO_RAD;
            this.anim.doAnimation(this, "idle", entity.tickCount, partialTicks);
            if (entity.moveTick() > 0) {
                this.anim.doAnimation(this, "walk", entity.tickCount, partialTicks, entity.interpolatedMoveTick(partialTicks));
            }
        }
        if (anim != null) {
            this.anim.doAnimation(this, anim.getAnimationClient(), anim.getTick(), partialTicks);
            if (anim.is(EntitySkelefang.BEAM))
                this.beamTick = anim.getTick();
        } else
            this.beamTick = -1;
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
                if (entity.hasBones()) {
                    this.spineFront.translateAndRotate(poseStack);
                    if (model instanceof SittingModel sittingModel)
                        sittingModel.translateSittingPosition(poseStack);
                    else
                        poseStack.translate(0, 6 / 16d, 5 / 16d);
                } else {
                    this.heart.translateAndRotate(poseStack);
                    if (model instanceof SittingModel sittingModel)
                        sittingModel.translateSittingPosition(poseStack);
                    else
                        poseStack.translate(0, 11 / 16d, 3 / 16d);
                }
                return true;
            }
        }
        return false;
    }

    public void updateFromBones(EntitySkelefang skelefang) {
        if (skelefang.isDeadOrDying()) {
            this.spineFront.visible = false;
            this.spineBack.visible = false;
            this.translucentTail = false;
            this.translucentTailBase = false;
            this.translucentSpineFront = false;
            this.translucentSpineBack = false;
            this.translucentBackRibs = false;
            this.translucentFrontRibs = false;
        } else {
            this.head.visible = skelefang.remainingHeadBones() > 10;
            this.neck.visible = skelefang.remainingHeadBones() > 0;
            this.spineFront.visible = skelefang.remainingBodyBones() > 0;
            this.ribsBody.visible = skelefang.remainingBodyBones() > 5;
            this.leftLegBase.visible = skelefang.remainingLeftLegBones() > 0;
            this.rightLegBase.visible = skelefang.remainingRightLegBones() > 0;
            this.ribsSpine.visible = skelefang.remainingBodyBones() > 15;
            this.spineBack.visible = skelefang.remainingBodyBones() > 10;
            this.tailBase.visible = skelefang.remainingTailBones() > 0;
            this.tail.visible = skelefang.remainingTailBones() > 10;

            this.translucentTail = skelefang.hasBones() && skelefang.isEnraged() && !this.tail.visible;
            this.translucentTailBase = skelefang.hasBones() && skelefang.isEnraged() && !this.tailBase.visible;
            this.translucentSpineFront = skelefang.hasBones() && skelefang.isEnraged() && !this.spineFront.visible;
            this.translucentSpineBack = skelefang.hasBones() && skelefang.isEnraged() && !this.spineBack.visible;
            this.translucentBackRibs = skelefang.hasBones() && skelefang.isEnraged() && !this.ribsSpine.visible;
            this.translucentFrontRibs = skelefang.hasBones() && skelefang.isEnraged() && !this.ribsBody.visible;
        }
    }

    public void renderAsParticle(PoseStack poseStack, VertexConsumer buffer, SkelefangParticleData.SkelefangBoneType boneType, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.model.resetPoses();
        this.body.setAllVisible(true);
        switch (boneType) {
            case TAIL -> {
                this.tail.setPos(0, 22.75f, 0);
                this.tail.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            }
            case TAIL_BASE -> {
                this.tail.visible = false;
                this.tailBase.setPos(0, 22.75f, 0);
                this.tailBase.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            }
            case LEFT_LEG -> {
                this.leftLegBase.setPos(0, 22.75f, 0);
                this.leftLegBase.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            }
            case RIGHT_LEG -> {
                this.rightLegBase.setPos(0, 22.75f, 0);
                this.rightLegBase.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            }
            case HEAD -> {
                this.head.setPos(0, 22.75f, 0);
                this.head.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            }
            case NECK -> {
                this.head.visible = false;
                this.neck.setPos(0, 22.75f, 0);
                this.neck.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            }
            case BACK_RIBS -> {
                this.tailBase.visible = false;
                this.ribsSpine.setPos(0, 22.75f, 0);
                this.ribsSpine.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            }
            case BACK -> {
                this.tailBase.visible = false;
                this.ribsSpine.visible = false;
                this.spineBack.setPos(0, 22.75f, 0);
                this.spineBack.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            }
            case FRONT_RIBS -> {
                this.leftLegBase.visible = false;
                this.rightLegBase.visible = false;
                this.ribsBody.setPos(0, 22.75f, 0);
                this.ribsBody.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            }
            case FRONT -> {
                this.neck.visible = false;
                this.ribsBody.visible = false;
                this.spineFront.setPos(0, 22.75f, 0);
                this.spineFront.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            }
            case GENERIC -> this.bone1.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            case GENERIC2 -> this.bone2.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        }
    }
}