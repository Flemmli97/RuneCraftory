package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.mixinhelper.ClientMixinUtils;
import io.github.flemmli97.runecraftory.mixinhelper.HumanoidMainHand;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin<T extends LivingEntity> implements HumanoidMainHand {

    @Unique
    private AnimatedAction runecraftoryCurrentAnimation;
    @Unique
    private ModelPart runecraftoryLeftHandItem;
    @Unique
    private ModelPart runecraftoryRightHandItem;

    @Inject(method = "setupAnim", at = @At("HEAD"))
    private void setupModel(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo info) {
        ClientMixinUtils.transFormPre(entity, (HumanoidModel<?>) (Object) this, a -> this.runecraftoryCurrentAnimation = a);
    }

    @Inject(method = "setupAnim", at = @At("RETURN"))
    private void modifyModel(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo info) {
        ClientMixinUtils.transformHumanoidModel(entity, (HumanoidModel<?>) (Object) this, this.runecraftoryCurrentAnimation);
    }

    @Override
    public ModelPart getRightHandItem() {
        if (this.runecraftoryRightHandItem == null)
            this.runecraftoryRightHandItem = new ModelPart(List.of(), Map.of());
        return this.runecraftoryRightHandItem;
    }

    @Override
    public ModelPart getLeftHandItem() {
        if (this.runecraftoryLeftHandItem == null)
            this.runecraftoryLeftHandItem = new ModelPart(List.of(), Map.of());
        return this.runecraftoryLeftHandItem;
    }
}
