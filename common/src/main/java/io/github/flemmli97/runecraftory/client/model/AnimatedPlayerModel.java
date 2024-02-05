package io.github.flemmli97.runecraftory.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.TransformationHelper;
import io.github.flemmli97.runecraftory.mixinhelper.HumanoidMainHand;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.api.entity.IAnimated;
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
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;

public class AnimatedPlayerModel<T extends LivingEntity & IAnimated> extends EntityModel<T> implements ExtendedModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(RuneCraftory.MODID, "animated_player"), "main");

    protected final ModelPartHandler model;
    protected final ModelPartHandler.ModelPartExtended head;
    protected final ModelPartHandler.ModelPartExtended rightArm;
    protected final ModelPartHandler.ModelPartExtended rightArmItem;
    protected final ModelPartHandler.ModelPartExtended leftArm;
    protected final ModelPartHandler.ModelPartExtended leftArmItem;
    protected final ModelPartHandler.ModelPartExtended rightLeg;
    protected final ModelPartHandler.ModelPartExtended leftLeg;

    protected final BlockBenchAnimations anim;

    public AnimatedPlayerModel(ModelPart root) {
        super();
        this.model = new ModelPartHandler(root.getChild("Body"), "Body");
        this.anim = AnimationManager.getInstance().getAnimation(new ResourceLocation(RuneCraftory.MODID, "player"));
        this.head = this.model.getPart("Head");
        this.rightArm = this.model.getPart("RightArm");
        this.rightArmItem = this.model.getPart("RightItem");
        this.leftArm = this.model.getPart("LeftArm");
        this.leftArmItem = this.model.getPart("LeftItem");
        this.rightLeg = this.model.getPart("RightLeg");
        this.leftLeg = this.model.getPart("LeftLeg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Head = Body.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition LeftArm = Body.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 2.0F, 0.0F));

        PartDefinition LeftItem = LeftArm.addOrReplaceChild("LeftItem", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 4.7F, -12.0F, 1.0F, 4.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition RightArm = Body.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

        PartDefinition RightItem = RightArm.addOrReplaceChild("RightItem", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 4.7F, -12.0F, 1.0F, 4.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition LeftLeg = Body.addOrReplaceChild("LeftLeg", CubeListBuilder.create().texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(1.9F, 12.0F, 0.0F));

        PartDefinition RightLeg = Body.addOrReplaceChild("RightLeg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.leftArmItem.visible = false;
        this.rightArmItem.visible = false;
        this.model.getMainPart().render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    public boolean setUpModel(LivingEntity entity, AnimatedAction anim, float partialTicks, float interpolation) {
        this.model.resetPoses();
        if (entity instanceof IAnimated animated) {
            return this.anim.doAnimation(this, animated.getAnimationHandler(), partialTicks, 5, false);
        }
        if (anim != null)
            this.anim.doAnimation(this, anim.getAnimationClient(), anim.getTick(), partialTicks, interpolation, entity.getMainArm() == HumanoidArm.LEFT);
        return anim != null;
    }

    public boolean doAnimation(ExtendedModel model, AnimationHandler<?> handler, float partialTicks, float adjustTime, boolean mirror) {
        AnimatedAction current = handler.getAnimation();
        AnimatedAction last = handler.getLastAnim();
        float interpolation = handler.getInterpolatedAnimationVal(partialTicks, adjustTime);
        boolean changed = false;
        if (last != null) {
            float interpolation2 = Mth.clamp((handler.getTimeSinceLastChange() + partialTicks) / adjustTime, 0, 1);
            float interpolationRev = 1 - interpolation2;
            if (interpolationRev > 0) {
                this.anim.doAnimation(model, last.getAnimationClient(), last.getTick(), partialTicks, interpolationRev, mirror);
                changed = true;
            }
        }
        //this.anim.doAnimation(model, PlayerModelAnimations.SHORT_SWORD.get(0).getAnimationClient(), 6, partialTicks, 1, mirror);
        if (current != null) {
            this.anim.doAnimation(model, current.getAnimationClient(), current.getTick(), partialTicks, interpolation, mirror);
            changed = true;
        }
        return changed;
    }

    public void copyTo(HumanoidModel<?> model, boolean plain, boolean ignoreRiding) {
        HumanoidMainHand hands = (HumanoidMainHand) model;
        PartPose main = this.model.getMainPart().storePose();
        float headXRot = model.head.xRot;
        float headYRot = model.head.yRot;
        this.apply(model.head, main, this.head);
        model.body.loadPose(main);
        this.apply(model.leftArm, main, this.leftArm);
        hands.getLeftHandItem().loadPose(this.leftArmItem.storePose());
        this.apply(model.rightArm, main, this.rightArm);
        hands.getRightHandItem().loadPose(this.rightArmItem.storePose());
        if (plain) {
            this.apply(model.leftLeg, main, this.leftLeg);
            this.apply(model.rightLeg, main, this.rightLeg);
        } else {
            if (ignoreRiding || !model.riding) {
                this.apply(model.leftLeg, main, this.leftLeg);
                this.apply(model.rightLeg, main, this.rightLeg);
            }
            model.head.xRot += headXRot;
            model.head.yRot += headYRot;
        }
        model.hat.copyFrom(model.head);
    }

    @Override
    public ModelPartHandler getHandler() {
        return this.model;
    }

    private void apply(ModelPart model, PartPose main, ModelPartHandler.ModelPartExtended first) {
        model.loadPose(TransformationHelper.withParent(main, first.storePose()));
    }

    public void renderHand(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, boolean left) {
        this.leftArmItem.visible = false;
        this.rightArmItem.visible = false;
        this.model.getMainPart().translateAndRotate(poseStack);
        if (left) {
            this.leftArm.render(poseStack, buffer, packedLight, packedOverlay, 1, 1, 1, 1);
        } else
            this.rightArm.render(poseStack, buffer, packedLight, packedOverlay, 1, 1, 1, 1);
    }
}