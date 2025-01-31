package io.github.flemmli97.runecraftory.mixin;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.mixinhelper.ClientMixinUtils;
import io.github.flemmli97.runecraftory.mixinhelper.HumanoidMainHand;
import io.github.flemmli97.tenshilib.mixin.ModelPartAccessor;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin<T extends LivingEntity> implements HumanoidMainHand {

    @Unique
    private ModelPart runecraftory$LeftHandItem;
    @Unique
    private ModelPart runecraftory$RightHandItem;
    @Unique
    private List<Pair<ModelPart, PartPose>> runecraftory$defaultPoses;

    @Inject(method = "<init>(Lnet/minecraft/client/model/geom/ModelPart;Ljava/util/function/Function;)V", at = @At("RETURN"))
    private void onInit(ModelPart root, Function<ResourceLocation, RenderType> renderType, CallbackInfo ci) {
        ImmutableList.Builder<Pair<ModelPart, PartPose>> builder = ImmutableList.builder();
        ((ModelPartAccessor) (Object) root)
                .getChildren().forEach((s, p) -> builder.add(Pair.of(p, p.storePose())));
        this.runecraftory$defaultPoses = builder.build();
    }

    @Inject(method = "setupAnim", at = @At("HEAD"))
    private void setupModel(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo info) {
        if (ClientMixinUtils.shouldAnimate(entity))
            this.runecraftory$defaultPoses.forEach(p -> p.getFirst().loadPose(p.getSecond()));
    }

    @Inject(method = "setupAnim", at = @At("RETURN"))
    private void modifyModel(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo info) {
        ClientMixinUtils.transformHumanoidModel(entity, (HumanoidModel<?>) (Object) this);
    }

    @Override
    public ModelPart runecraftory$getRightHandItem() {
        if (this.runecraftory$RightHandItem == null)
            this.runecraftory$RightHandItem = new ModelPart(List.of(), Map.of());
        return this.runecraftory$RightHandItem;
    }

    @Override
    public ModelPart runecraftory$getLeftHandItem() {
        if (this.runecraftory$LeftHandItem == null)
            this.runecraftory$LeftHandItem = new ModelPart(List.of(), Map.of());
        return this.runecraftory$LeftHandItem;
    }
}
