package io.github.flemmli97.runecraftory.client.render.npc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.client.model.HumanoidBasedModel;
import io.github.flemmli97.runecraftory.common.entities.utils.MoveStateHolder;
import io.github.flemmli97.runecraftory.common.entities.utils.MoveType;
import io.github.flemmli97.tenshilib.client.model.BedrockAnimations;
import io.github.flemmli97.tenshilib.client.model.ExtendedEntityModel;
import io.github.flemmli97.tenshilib.client.model.animation.Animation;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.utils.math.parser.VariableMap;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class FeatureModel<T extends LivingEntity & MoveStateHolder> extends ExtendedEntityModel<T> {

    protected HumanoidBasedModel<?> main;
    private float limbSwing, limbSwingAmount;

    public FeatureModel(ResourceLocation location) {
        super(RenderType::entityTranslucent, location);
    }

    public FeatureModel(ResourceLocation location, ResourceLocation animation) {
        super(RenderType::entityTranslucent, location, animation);
    }

    public void setMain(HumanoidBasedModel<?> main) {
        this.main = main;
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (this.getModel() == null || this.main == null)
            return;
        this.getModel().resetPoses();
        this.limbSwing = limbSwing;
        this.limbSwingAmount = limbSwingAmount;
        if (this.animation != null) {
            BedrockAnimations animations = this.animation.get();
            animations.doAnimation(this, "idle", entity.tickCount, this.partialTick, 1);
            animations.doAnimation(this, "walk", entity.tickCount, this.partialTick, entity.interpolatedMoveTick(this.partialTick));
            animations.doAnimation(this, "run", entity.tickCount, this.partialTick, entity.interpolatedMoveTickOf(MoveType.RUN, this.partialTick));
            animations.doAnimation(this, "swim", entity.tickCount, this.partialTick, entity.getSwimAmount(this.partialTick));
            if (this.main.crouching) {
                animations.doAnimation(this, "crouching", entity.tickCount, this.partialTick, 1, false, true);
            }
            if (this.riding) {
                animations.doAnimation(this, "riding", entity.tickCount, this.partialTick, 1);
            }
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        if (this.getModel() == null)
            return;
        this.model.get().getRoot().render(poseStack, buffer, packedLight, packedOverlay, color);
    }

    @Override
    public void onPlayAnimation(AnimationState state, Animation animation, float tick, VariableMap variables) {
        HumanoidBasedModel.setupAnimationValues(this.main, variables, animation.variables(), this.getCurrentEntity(), this.partialTick,
                this.limbSwing, this.limbSwingAmount);
    }
}