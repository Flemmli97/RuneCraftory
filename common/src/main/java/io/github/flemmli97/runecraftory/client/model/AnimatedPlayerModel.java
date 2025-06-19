package io.github.flemmli97.runecraftory.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.action.AttackActionHandler;
import io.github.flemmli97.runecraftory.client.TransformationHelper;
import io.github.flemmli97.runecraftory.mixinhelper.HumanoidMainHand;
import io.github.flemmli97.tenshilib.client.data.AnimationManager;
import io.github.flemmli97.tenshilib.client.data.ModelManager;
import io.github.flemmli97.tenshilib.client.data.ReloadableCache;
import io.github.flemmli97.tenshilib.client.model.BedrockAnimations;
import io.github.flemmli97.tenshilib.client.model.ExtendedModel;
import io.github.flemmli97.tenshilib.client.model.ModelPartsContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimatedEntity;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class AnimatedPlayerModel<T extends LivingEntity & AnimatedEntity> extends EntityModel<T> implements ExtendedModel {

    public static final ResourceLocation LOCATION = RuneCraftory.modRes("player");

    protected final ReloadableCache<ModelPartsContainer> model;
    protected ModelPartsContainer.ModelPartExtended head;
    protected ModelPartsContainer.ModelPartExtended rightArm;
    protected ModelPartsContainer.ModelPartExtended rightArmItem;
    protected ModelPartsContainer.ModelPartExtended leftArm;
    protected ModelPartsContainer.ModelPartExtended leftArmItem;
    protected ModelPartsContainer.ModelPartExtended rightLeg;
    protected ModelPartsContainer.ModelPartExtended leftLeg;

    protected final ReloadableCache<BedrockAnimations> anim;

    public AnimatedPlayerModel() {
        super();
        this.model = ModelManager.getInstance().getModel(LOCATION, model -> {
            this.head = model.getPart("Head");
            this.rightArm = model.getPart("RightArm");
            this.rightArmItem = model.getPart("RightItemRoot");
            this.leftArm = model.getPart("LeftArm");
            this.leftArmItem = model.getPart("LeftItemRoot");
            this.rightLeg = model.getPart("RightLeg");
            this.leftLeg = model.getPart("LeftLeg");
        });
        this.anim = AnimationManager.getInstance().getAnimation(RuneCraftory.modRes("player"));
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        this.leftArmItem.visible = false;
        this.rightArmItem.visible = false;
        this.getModel().getMainPart().render(poseStack, buffer, packedLight, packedOverlay, color);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    public boolean setUpModel(LivingEntity entity, HumanoidModel<?> model, @Nullable AttackActionHandler handler, float partialTicks) {
        HumanoidMainHand hands = (HumanoidMainHand) model;
        hands.runecraftory$getLeftHandItem().resetAll();
        hands.runecraftory$getRightHandItem().resetAll();
        if (entity instanceof AnimatedEntity animated) {
            this.setup(model);
            return this.anim.get().doAnimation(this, animated.getAnimationHandler(), partialTicks, entity.getMainArm() == HumanoidArm.LEFT);
        }
        if (handler == null)
            return false;
        this.setup(model);
        return this.doAnimation(handler, partialTicks, entity.getMainArm() == HumanoidArm.LEFT);
    }

    private boolean doAnimation(AttackActionHandler handler, float partialTicks, boolean mirror) {
        AnimationState current = handler.getAnimation();
        AnimationState last = handler.getLastAnimation();
        float interpolationLast = handler.getLastTransitionProgress(partialTicks);
        float interpolation = handler.getCurrentTransitionProgress(partialTicks);
        boolean changed = false;
        if (last != null && interpolationLast > 0) {
            changed = this.anim.get().doAnimation(this, last.getAnimation(), last.getTick(partialTicks), interpolationLast, mirror, false);
        }
        if (current != null) {
            if (this.anim.get().doAnimation(this, current.getAnimation(), current.getTick(partialTicks), interpolation, mirror, false) && !changed) {
                changed = true;
            }
        }
        return changed;
    }

    private void setup(HumanoidModel<?> model) {
        PartPose body = model.body.storePose();
        this.getModel().resetPoses();
        this.getModel().getMainPart().loadPose(body);
        this.leftArm.loadPose(TransformationHelper.withoutParent(body, model.leftArm.storePose()));
        this.rightArm.loadPose(TransformationHelper.withoutParent(body, model.rightArm.storePose()));
        this.leftLeg.loadPose(TransformationHelper.withoutParent(body, model.leftLeg.storePose()));
        this.rightLeg.loadPose(TransformationHelper.withoutParent(body, model.rightLeg.storePose()));
        this.head.loadPose(TransformationHelper.withoutParent(body, model.head.storePose()));
    }

    public void copyTo(HumanoidModel<?> model) {
        HumanoidMainHand hands = (HumanoidMainHand) model;
        if (model.riding) {
            ModelPartsContainer.ModelPartExtended body = this.getModel().getMainPart();
            body.x = body.getDefaultPose().x;
            body.y = body.getDefaultPose().y;
            body.z = body.getDefaultPose().z;
            PoseStack stack = new PoseStack();
            body.translateAndRotate(stack);
            float bodyLength = -12;
            Vector3f v = new Vector3f(0, bodyLength, 0);
            v.mulTranspose(stack.last().normal());
            body.x += v.x();
            body.y += v.y() - bodyLength;
            body.z += v.z();
        }
        PartPose main = this.getModel().getMainPart().storePose();
        this.apply(model.head, main, this.head);
        model.body.loadPose(main);
        this.apply(model.leftArm, main, this.leftArm);
        hands.runecraftory$getLeftHandItem().loadPose(this.leftArmItem.storePose());
        this.apply(model.rightArm, main, this.rightArm);
        hands.runecraftory$getRightHandItem().loadPose(this.rightArmItem.storePose());
        if (!model.riding) {
            this.apply(model.leftLeg, main, this.leftLeg);
            this.apply(model.rightLeg, main, this.rightLeg);
        }
        model.hat.copyFrom(model.head);
    }

    @Override
    public ModelPartsContainer getModel() {
        return this.model.get();
    }

    private void apply(ModelPart model, PartPose main, ModelPartsContainer.ModelPartExtended first) {
        model.loadPose(TransformationHelper.withParent(main, first.storePose()));
    }
}