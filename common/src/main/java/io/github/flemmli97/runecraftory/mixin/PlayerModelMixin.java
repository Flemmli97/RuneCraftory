package io.github.flemmli97.runecraftory.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.mixinhelper.HumanoidMainHand;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.world.entity.HumanoidArm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerModel.class)
public abstract class PlayerModelMixin {

    @Inject(method = "translateToHand", at = @At("RETURN"))
    private void onHandTranslate(HumanoidArm side, PoseStack poseStack, CallbackInfo info) {
        if (side == HumanoidArm.RIGHT) {
            ((HumanoidMainHand) this).runecraftory$getRightHandItem().translateAndRotate(poseStack);
            ((HumanoidMainHand) this).runecraftory$getRightHandItem().getChild("RightItem").translateAndRotate(poseStack);
        } else {
            ((HumanoidMainHand) this).runecraftory$getLeftHandItem().translateAndRotate(poseStack);
            ((HumanoidMainHand) this).runecraftory$getLeftHandItem().getChild("LeftItem").translateAndRotate(poseStack);
        }
    }
}
