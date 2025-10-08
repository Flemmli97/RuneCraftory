package io.github.flemmli97.runecraftory.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.TransformationHelper;
import io.github.flemmli97.runecraftory.mixinhelper.HumanoidMainHand;
import io.github.flemmli97.tenshilib.client.data.GeoAnimationManager;
import io.github.flemmli97.tenshilib.client.data.GeoModelManager;
import io.github.flemmli97.tenshilib.client.data.ReloadableCache;
import io.github.flemmli97.tenshilib.client.model.BedrockAnimations;
import io.github.flemmli97.tenshilib.client.model.DeformationChange;
import io.github.flemmli97.tenshilib.client.model.ExtendedModel;
import io.github.flemmli97.tenshilib.client.model.ItemHolderModel;
import io.github.flemmli97.tenshilib.client.model.ModelPartsContainer;
import io.github.flemmli97.tenshilib.client.model.PoseExtended;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimatedEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;

public class HumanoidBasedModel<T extends LivingEntity & AnimatedEntity> extends EntityModel<T> implements ItemHolderModel, HeadedModel, ExtendedModel {

    public static final ResourceLocation DEFAULT_PLAYER_LOCATION = RuneCraftory.modRes("player");
    public static final ResourceLocation DEFAULT_LOCATION = RuneCraftory.modRes("npc/default");
    public static final ResourceLocation DEFAULT_LOCATION_SLIM = RuneCraftory.modRes("npc/default_slim");
    public static final ResourceLocation DEFAULT_NPC_ANIMATION = RuneCraftory.modRes("npc/default");

    protected final ReloadableCache<ModelPartsContainer> model;
    protected final ReloadableCache<BedrockAnimations> attackAnimations;
    protected final ReloadableCache<BedrockAnimations> miscAnimations;

    public ModelPartsContainer.ModelPartExtended head;
    public ModelPartsContainer.ModelPartExtended body;
    public ModelPartsContainer.ModelPartExtended rightArm;
    public ModelPartsContainer.ModelPartExtended rightItem;
    public ModelPartsContainer.ModelPartExtended leftArm;
    public ModelPartsContainer.ModelPartExtended leftItem;
    @Nullable
    public ModelPartsContainer.ModelPartExtended legBase;
    public ModelPartsContainer.ModelPartExtended rightLeg;
    public ModelPartsContainer.ModelPartExtended leftLeg;

    @Nullable
    public ModelPartsContainer.ModelPartExtended vehicleAttachment;
    protected Vector3f bodyVehicleOffset = new Vector3f(0, -12, 0);

    protected final ModelPart dummyHead = new ModelPart(new ArrayList<>(), new HashMap<>());

    public HumanoidModel.ArmPose leftArmPose = HumanoidModel.ArmPose.EMPTY;
    public HumanoidModel.ArmPose rightArmPose = HumanoidModel.ArmPose.EMPTY;
    public boolean crouching;
    public float swimAmount;
    private float partialTicks;

    protected HumanoidModel<T> delegate;

    public HumanoidBasedModel() {
        this(DEFAULT_PLAYER_LOCATION, DEFAULT_NPC_ANIMATION, 0);
    }

    public HumanoidBasedModel(ResourceLocation modelLocation, ResourceLocation animationLocation, float inflate) {
        super(RenderType::entityTranslucent);
        this.model = GeoModelManager.getInstance().getModel(modelLocation,
                inflate != 0 ? new DeformationChange(inflate) : DeformationChange.NONE,
                model -> {
                    this.head = model.getPart("Head");
                    this.body = model.getPart("Body");
                    this.rightItem = model.getPart("RightItemRoot");
                    this.leftItem = model.getPart("LeftItemRoot");
                    this.rightArm = model.getPart("RightArm");
                    this.leftArm = model.getPart("LeftArm");
                    this.legBase = model.getOptionalPart("LegsBase").orElse(null);
                    this.rightLeg = model.getPart("RightLeg");
                    this.leftLeg = model.getPart("LeftLeg");

                    this.vehicleAttachment = model.getOptionalPart("VehicleAttachment").orElse(null);
                    if (this.vehicleAttachment != null) {
                        this.vehicleAttachment.updateDefaultPose(this.vehicleAttachment.getDefaultPose().withScale(0, 0, 0));
                        PoseExtended bodyPose = this.body.getDefaultPose();
                        PoseExtended attachmentPose = this.vehicleAttachment.getDefaultPose();
                        this.bodyVehicleOffset = new Vector3f(attachmentPose.x - bodyPose.x, attachmentPose.y - bodyPose.y, attachmentPose.z - bodyPose.z);
                    }
                });
        this.attackAnimations = GeoAnimationManager.getInstance().getAnimation(DEFAULT_PLAYER_LOCATION);
        this.miscAnimations = GeoAnimationManager.getInstance().getOptionalAnimation(animationLocation);
    }

    @Override
    public ModelPartsContainer getModel() {
        return this.model.get();
    }

    @Override
    public void transform(HumanoidArm hand, PoseStack stack) {
        if (hand == HumanoidArm.LEFT) {
            this.leftItem.translateAndRotateWithParents(stack);
        } else {
            this.rightItem.translateAndRotateWithParents(stack);
        }
        stack.translate(0, 2 / 16d, -2 / 16d);
    }

    @Override
    public ModelPart getHead() {
        this.dummyHead.x = this.head.x;
        this.dummyHead.y = this.head.y;
        this.dummyHead.z = this.head.z;
        this.dummyHead.xRot = this.head.xRot;
        this.dummyHead.yRot = this.head.yRot;
        this.dummyHead.zRot = this.head.zRot;
        this.dummyHead.xScale = this.head.xScale;
        this.dummyHead.yScale = this.head.yScale;
        this.dummyHead.zScale = this.head.zScale;
        return this.dummyHead;
    }

    /**
     * Sets the {@link HumanoidModel} to copy states from.
     * Since vanillas model has a lot of animations already builtin reusing that is better than remaking every animation in json.
     * The way this works is that the HumanoidModel gets animated first and the rotations get copied to this model.
     */
    public void setDelegate(HumanoidModel<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void prepareMobModel(T entity, float limbSwing, float limbSwingAmount, float partialTick) {
        super.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTick);
        this.swimAmount = entity.getSwimAmount(partialTick);
        this.partialTicks = partialTick;
        if (this.delegate != null) {
            this.delegate.attackTime = this.attackTime;
            this.delegate.riding = this.riding;
            this.delegate.young = this.young;
            this.delegate.leftArmPose = this.leftArmPose;
            this.delegate.rightArmPose = this.rightArmPose;
            this.delegate.crouching = this.crouching;
            this.delegate.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTick);
        }
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.preAnimSetup(entity, limbSwing, limbSwingAmount, netHeadYaw, ageInTicks, headPitch, this.partialTicks);
        PoseExtended ext = null;
        PoseExtended ext2 = null;
        if (this.riding) {
            ext = this.leftLeg.extendedPose();
            ext2 = this.rightLeg.extendedPose();
        }
        this.attackAnimations.get().doAnimation(this, entity.getAnimationHandler(), this.partialTicks, entity.getMainArm() == HumanoidArm.LEFT);
        if (this.delegate != null)
            this.copyPropertiesTo(this.delegate);

        // Move the body so it stays at the same place
        if (this.riding) {
            this.body.x = this.body.getDefaultPose().x;
            this.body.y = this.body.getDefaultPose().y;
            this.body.z = this.body.getDefaultPose().z;
            PoseStack stack = new PoseStack();
            this.body.translateAndRotate(stack);
            Vector3f v = this.bodyVehicleOffset != null ? new Vector3f(this.bodyVehicleOffset) : new Vector3f();
            v.mulTranspose(stack.last().normal());
            this.body.x += v.x() - this.bodyVehicleOffset.x;
            this.body.y += v.y() - this.bodyVehicleOffset.y;
            this.body.z += v.z() - this.bodyVehicleOffset.z;
            this.leftLeg.loadPose(ext);
            this.rightLeg.loadPose(ext2);
        }
    }

    protected void preAnimSetup(T entity, float limbSwing, float limbSwingAmount, float netHeadYaw, float ageInTicks, float headPitch, float partialTicks) {
        if (this.delegate != null) {
            this.delegate.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            this.copyFrom(this.delegate);
        }
        BedrockAnimations animation = this.attackAnimations.get();
        setupAnimationValues(this, animation, limbSwing, limbSwingAmount, netHeadYaw, headPitch);
        BedrockAnimations miscAnimation = this.attackAnimations.get();
        setupAnimationValues(this, animation, limbSwing, limbSwingAmount, netHeadYaw, headPitch);

        miscAnimation.doAnimation(this, "idle", entity.tickCount, partialTicks, 1);
        if (this.swimAmount == 0) {
            miscAnimation.doAnimation(this, "walk", entity.tickCount, partialTicks, 1, false, true);
        } else {
            miscAnimation.doAnimation(this, "swim", entity.tickCount, partialTicks, 1, false, true);
        }
        if (this.crouching) {
            miscAnimation.doAnimation(this, "crouching", entity.tickCount, partialTicks, 1, false, true);
        }
        if (this.riding) {
            miscAnimation.doAnimation(this, "riding", entity.tickCount, partialTicks, 1, false, true);
        }
    }

    public static void setupAnimationValues(HumanoidBasedModel<?> model, BedrockAnimations animation, float limbSwing, float limbSwingAmount, float netHeadYaw, float headPitch) {
        animation.setVariable("query.head_x_rotation", () -> headPitch);
        animation.setVariable("query.head_y_rotation", () -> netHeadYaw);
        animation.setVariable("left_held", () -> model.leftArmPose != HumanoidModel.ArmPose.EMPTY ? 1 : 0);
        animation.setVariable("left_arm_x_rot", () -> model.leftArm != null ? model.leftArm.xRot * Mth.RAD_TO_DEG : 0);
        animation.setVariable("right_held", () -> model.rightArmPose != HumanoidModel.ArmPose.EMPTY ? 1 : 0);
        animation.setVariable("right_arm_x_rot", () -> model.rightArm != null ? model.rightArm.xRot * Mth.RAD_TO_DEG : 0);
        animation.setVariable("limb_swing", () -> limbSwing * Mth.RAD_TO_DEG);
        animation.setVariable("limb_swing_amount", () -> limbSwingAmount * Mth.RAD_TO_DEG);
        animation.setVariable("crouching", () -> model.crouching ? 1 : 0);
        animation.setVariable("riding", () -> model.riding ? 1 : 0);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        this.model.get().getRoot().renderForced(poseStack, buffer, packedLight, packedOverlay, color);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void copyFrom(@Nullable EntityModel<?> model) {
        this.getModel().resetPoses();
        if (model == null) {
            return;
        }
        model.copyPropertiesTo((EntityModel) this);
        if (model instanceof HumanoidModel<?> other) {
            PartPose body = other.body.storePose();
            this.body.loadPose(body);
            this.leftArm.loadPose(TransformationHelper.withoutParent(body, other.leftArm.storePose()));
            this.rightArm.loadPose(TransformationHelper.withoutParent(body, other.rightArm.storePose()));
            this.leftLeg.loadPose(TransformationHelper.withoutParent(body, other.leftLeg.storePose()));
            this.rightLeg.loadPose(TransformationHelper.withoutParent(body, other.rightLeg.storePose()));
            this.head.loadPose(TransformationHelper.withoutParent(body, other.head.storePose()));
        }
    }

    @Override
    public void copyPropertiesTo(EntityModel<T> model) {
        super.copyPropertiesTo(model);
        if (model instanceof HumanoidBasedModel<?> other) {
            other.getModel().getRoot().forEach((name, part) -> {
                this.getModel().getOptionalPart(name)
                        .ifPresentOrElse(p -> part.loadPose(p.extendedPose()),
                                part::reset);
            });
        }
        if (model instanceof HumanoidModel<?> other) {
            PoseExtended body = this.body.extendedPose();
            other.body.visible = this.body.visible;
            other.body.loadPose(body.asPartPose());
            other.head.visible = this.head.visible;
            this.apply(other.head, body, this.head);
            other.leftArm.visible = this.leftArm.visible;
            this.apply(other.leftArm, body, this.leftArm);
            HumanoidMainHand hands = (HumanoidMainHand) model;
            hands.runecraftory$getLeftHandItem().loadPose(this.leftArm.storePose());
            other.rightArm.visible = this.rightArm.visible;
            this.apply(other.rightArm, body, this.rightArm);
            hands.runecraftory$getRightHandItem().loadPose(this.rightItem.storePose());
            if (!model.riding) {
                this.apply(other.leftLeg, body, this.leftLeg);
                this.apply(other.rightLeg, body, this.rightLeg);
            } else {
                other.leftLeg.loadPose(TransformationHelper.withParent(body, other.leftLeg.storePose()));
                other.rightLeg.loadPose(TransformationHelper.withParent(body, other.rightLeg.storePose()));
            }
            other.leftLeg.visible = this.leftLeg.visible;
            other.rightLeg.visible = this.rightLeg.visible;
            other.hat.visible = other.head.visible;
            other.hat.copyFrom(other.head);
        }
    }

    private void apply(ModelPart model, PoseExtended body, ModelPartsContainer.ModelPartExtended first) {
        model.loadPose(TransformationHelper.withParent(body, first.storePose()));
    }

    public void setAllVisible(boolean visible) {
        this.getModel().getRoot().forEach((name, part) -> part.visible = visible);
    }

    public void copyVisibilityFrom(HumanoidBasedModel<T> model) {
        model.getModel().getRoot().forEach((name, part) -> {
            this.getModel().getOptionalPart(name)
                    .ifPresentOrElse(p -> p.visible = part.visible,
                            part::reset);
        });
    }
}
