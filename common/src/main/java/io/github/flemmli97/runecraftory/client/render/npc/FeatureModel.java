package io.github.flemmli97.runecraftory.client.render.npc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.client.model.HumanoidBasedModel;
import io.github.flemmli97.runecraftory.common.entities.utils.MoveStateHolder;
import io.github.flemmli97.runecraftory.common.entities.utils.MoveType;
import io.github.flemmli97.tenshilib.client.data.GeoAnimationManager;
import io.github.flemmli97.tenshilib.client.data.GeoModelManager;
import io.github.flemmli97.tenshilib.client.data.ReloadableCache;
import io.github.flemmli97.tenshilib.client.model.BedrockAnimations;
import io.github.flemmli97.tenshilib.client.model.ExtendedEntityModel;
import io.github.flemmli97.tenshilib.client.model.ModelPartsContainer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class FeatureModel<T extends LivingEntity & MoveStateHolder> extends ExtendedEntityModel<T> {

    protected final ReloadableCache<ModelPartsContainer> model;
    protected final ReloadableCache<BedrockAnimations> animations;

    protected HumanoidBasedModel<?> main;
    private float partialTick;

    public FeatureModel(ResourceLocation location) {
        super(RenderType::entityTranslucent);
        this.model = this.load(location);
        this.animations = null;
    }

    public FeatureModel(ResourceLocation location, ResourceLocation animation) {
        super(RenderType::entityTranslucent);
        this.model = this.load(location);
        this.animations = GeoAnimationManager.getInstance().getAnimation(animation);
    }

    protected ReloadableCache<ModelPartsContainer> load(ResourceLocation location) {
        return GeoModelManager.getInstance().getModel(location);
    }

    public void setMain(HumanoidBasedModel<?> main) {
        this.main = main;
    }

    @Override
    public void prepareMobModel(T entity, float limbSwing, float limbSwingAmount, float partialTick) {
        super.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTick);
        this.partialTick = partialTick;
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (this.getModel() == null || this.main == null)
            return;
        this.getModel().resetPoses();
        if (this.animations != null) {
            BedrockAnimations animations = this.animations.get();
            HumanoidBasedModel.setupAnimationValues(this.main, animations, limbSwing, limbSwingAmount, netHeadYaw, headPitch);
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
    public ModelPartsContainer getModel() {
        return this.model.get();
    }
}