package io.github.flemmli97.runecraftory.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.client.AnimationManager;
import io.github.flemmli97.tenshilib.client.model.BlockBenchAnimations;
import io.github.flemmli97.tenshilib.client.model.ExtendedModel;
import io.github.flemmli97.tenshilib.client.model.ModelPartHandler;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class AnimatedPlayerModel extends EntityModel<Player> implements ExtendedModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(RuneCraftory.MODID, "animated_player"), "main");

    protected final ModelPartHandler model;
    protected final ModelPartHandler.ModelPartExtended head;
    protected final ModelPartHandler.ModelPartExtended rightArm;
    protected final ModelPartHandler.ModelPartExtended leftArm;
    protected final ModelPartHandler.ModelPartExtended rightLeg;
    protected final ModelPartHandler.ModelPartExtended leftLeg;

    protected final BlockBenchAnimations anim;

    public AnimatedPlayerModel(ModelPart root) {
        super();
        this.model = new ModelPartHandler(root.getChild("Body"), "Body");
        this.anim = AnimationManager.getInstance().getAnimation(new ResourceLocation(RuneCraftory.MODID, "player"));
        this.head = this.model.getPart("Head");
        this.rightArm = this.model.getPart("RightArm");
        this.leftArm = this.model.getPart("LeftArm");
        this.rightLeg = this.model.getPart("RightLeg");
        this.leftLeg = this.model.getPart("LeftLeg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Head = Body.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition LeftArm = Body.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 2.0F, 0.0F));

        PartDefinition RightArm = Body.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

        PartDefinition LeftLeg = Body.addOrReplaceChild("LeftLeg", CubeListBuilder.create().texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(1.9F, 12.0F, 0.0F));

        PartDefinition RightLeg = Body.addOrReplaceChild("RightLeg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
    }

    @Override
    public void setupAnim(Player entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    public void setUpModel(AnimatedAction anim, float partialTicks) {
        this.model.resetPoses();
        if (anim != null)
            this.anim.doAnimation(this, anim.getAnimationClient(), anim.getTick(), partialTicks, 1);
    }

    public void copyTo(LivingEntity entity, HumanoidModel<?> model, boolean plain, boolean ignoreRiding) {
        boolean flipped = entity.getMainArm() == HumanoidArm.LEFT;
        PartPose main = this.flipped(this.model.getMainPart(), flipped);
        PartPose head = this.flipped(this.head, flipped);
        model.head.loadPose(this.withParent(main, head));
        model.body.loadPose(main);
        this.switched(model.leftArm, main, this.leftArm, this.rightArm, flipped);
        this.switched(model.rightArm, main, this.rightArm, this.leftArm, flipped);
        if (plain) {
            this.switched(model.leftLeg, main, this.leftLeg, this.rightLeg, flipped);
            this.switched(model.rightLeg, main, this.rightLeg, this.leftLeg, flipped);
        } else {
            if (ignoreRiding || !model.riding) {
                this.switched(model.leftLeg, main, this.leftLeg, this.rightLeg, flipped);
                this.switched(model.rightLeg, main, this.rightLeg, this.leftLeg, flipped);
            }
            model.head.xRot += head.xRot;
            model.head.yRot += head.yRot;
            model.head.zRot += head.zRot;
        }
        model.hat.copyFrom(model.head);
    }

    @Override
    public ModelPartHandler getHandler() {
        return this.model;
    }

    private PartPose flipped(ModelPartHandler.ModelPartExtended model, boolean flipped) {
        if (flipped) {
            return PartPose.offsetAndRotation(-model.x, model.y, model.z, model.xRot, -model.yRot, -model.zRot);
        } else {
            return model.storePose();
        }
    }

    private void switched(ModelPart model, PartPose main, ModelPartHandler.ModelPartExtended first, ModelPartHandler.ModelPartExtended second, boolean flipped) {
        if (flipped) {
            model.loadPose(this.withParent(main, PartPose.offsetAndRotation(first.defaultPose.x - (second.x - second.defaultPose.x),
                    second.y, second.z, second.xRot, -second.yRot, -second.zRot)));
        } else {
            model.loadPose(this.withParent(main, first.storePose()));
        }
    }

    private PartPose withParent(PartPose parentPose, PartPose child) {
        Vector3f translatedPos = this.withParentX(parentPose, child.x, child.y, child.z);
        return PartPose.offsetAndRotation((parentPose.x + translatedPos.x()),
                (parentPose.y + translatedPos.y()),
                (parentPose.z + translatedPos.z()),
                parentPose.xRot + child.xRot,
                parentPose.yRot + child.yRot,
                parentPose.zRot + child.zRot);
    }

    private Vector3f withParentX(PartPose parentPose, float x, float y, float z) {
        Vector3f v = new Vector3f(x, y, z);
        if (parentPose.zRot != 0.0F) {
            v.transform(Vector3f.ZP.rotation(parentPose.zRot));
        }
        if (parentPose.yRot != 0.0F) {
            v.transform(Vector3f.YP.rotation(parentPose.yRot));
        }
        if (parentPose.xRot != 0.0F) {
            v.transform(Vector3f.XP.rotation(parentPose.xRot));
        }
        return v;
    }
}