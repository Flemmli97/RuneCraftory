package io.github.flemmli97.runecraftory.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.common.attachment.AttackActionHandler;
import io.github.flemmli97.runecraftory.common.entities.utils.MoveStateHolder;
import io.github.flemmli97.runecraftory.mixinhelper.HumanoidMainHand;
import io.github.flemmli97.tenshilib.client.model.ModelPartsContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimatedEntity;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class AnimatedPlayerModel<T extends LivingEntity & AnimatedEntity & MoveStateHolder> extends HumanoidBasedModel<T> {

    public AnimatedPlayerModel() {
        super();
    }

    public boolean setUpModel(Player entity, @Nullable HumanoidModel<?> model, @Nullable AttackActionHandler handler, float partialTicks) {
        if (model != null) {
            HumanoidMainHand hands = (HumanoidMainHand) model;
            hands.runecraftory$getLeftHandItem().resetAll();
            hands.runecraftory$getRightHandItem().resetAll();
        }
        if (handler == null)
            return false;
        this.copyFrom(model);
        return this.doAnimation(handler, partialTicks, entity.getMainArm() == HumanoidArm.LEFT);
    }

    private boolean doAnimation(AttackActionHandler handler, float partialTicks, boolean mirror) {
        AnimationState current = handler.getAnimation();
        AnimationState last = handler.getLastAnimation();
        float interpolationLast = handler.getLastTransitionProgress(partialTicks);
        float interpolation = handler.getCurrentTransitionProgress(partialTicks);
        boolean changed = false;
        if (last != null && interpolationLast > 0) {
            changed = this.attackAnimations.get().doAnimation(this, last.getAnimation(), last.getTick(partialTicks), interpolationLast, mirror, false);
        }
        if (current != null) {
            if (this.attackAnimations.get().doAnimation(this, current.getAnimation(), current.getTick(partialTicks), interpolation, mirror, false) && !changed) {
                changed = true;
            }
        }
        // Move the body so it stays at the same place
        if (changed && this.riding) {
            this.body.x = this.body.getDefaultPose().x;
            this.body.y = this.body.getDefaultPose().y;
            this.body.z = this.body.getDefaultPose().z;
            PoseStack stack = new PoseStack();
            this.body.translateAndRotate(stack);
            Vector3f v = this.bodyVehicleOffset != null ? new Vector3f(this.bodyVehicleOffset) : new Vector3f();
            v.mulTranspose(stack.last().normal());
            this.body.x -= v.x() - this.bodyVehicleOffset.x;
            this.body.y -= v.y() - this.bodyVehicleOffset.y;
            this.body.z -= v.z() - this.bodyVehicleOffset.z;
        }
        return changed;
    }

    @SuppressWarnings("unchecked")
    public void copyTo(HumanoidModel<?> model) {
        this.copyPropertiesTo((EntityModel<T>) model);
    }

    @Override
    public ModelPartsContainer getModel() {
        return this.model.get();
    }
}