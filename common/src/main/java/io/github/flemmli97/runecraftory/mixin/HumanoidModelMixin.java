package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.mixinhelper.ClientMixinUtils;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin<T extends LivingEntity> {

    @Unique
    private AnimatedAction rf4CurrentAnimation;

    @Inject(method = "setupAnim", at = @At("HEAD"))
    private void setupModel(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo info) {
        ClientMixinUtils.transFormPre(entity, (HumanoidModel<?>) (Object) this, a -> this.rf4CurrentAnimation = a);
    }

    @Inject(method = "setupAnim", at = @At("RETURN"))
    private void modifyModel(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo info) {
        ClientMixinUtils.transformHumanoidModel(entity, (HumanoidModel<?>) (Object) this, this.rf4CurrentAnimation);
    }
}
