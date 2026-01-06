package io.github.flemmli97.runecraftory.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.common.entities.utils.MoveStateHolder;
import io.github.flemmli97.runecraftory.mixinhelper.HumanoidMainHand;
import io.github.flemmli97.tenshilib.client.model.ModelPartsContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimatedEntity;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
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

    public boolean setUpModel(Player entity, @Nullable HumanoidModel<?> model, AnimationHandler<?> handler, float partialTick) {
        if (model != null) {
            HumanoidMainHand hands = (HumanoidMainHand) model;
            hands.runecraftory$getLeftHandItem().resetAll();
            hands.runecraftory$getRightHandItem().resetAll();
        }
        if (handler == null)
            return false;
        this.copyFrom(model);
        return this.doAnimation(handler, partialTick, entity.getMainArm() == HumanoidArm.LEFT);
    }

    private boolean doAnimation(AnimationHandler<?> handler, float partialTick, boolean mirror) {
        boolean changed = this.attackAnimations.get().doAnimation(this, handler, partialTick, mirror);
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