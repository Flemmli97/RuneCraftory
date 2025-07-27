package io.github.flemmli97.runecraftory.client.render.npc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.tenshilib.client.data.GeoAnimationManager;
import io.github.flemmli97.tenshilib.client.data.GeoModelManager;
import io.github.flemmli97.tenshilib.client.data.ReloadableCache;
import io.github.flemmli97.tenshilib.client.model.BedrockAnimations;
import io.github.flemmli97.tenshilib.client.model.ExtendedModel;
import io.github.flemmli97.tenshilib.client.model.ModelPartsContainer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class FeatureModel<T extends Entity> extends EntityModel<T> implements ExtendedModel {

    protected final ReloadableCache<ModelPartsContainer> model;
    protected final ReloadableCache<BedrockAnimations> animations;

    public FeatureModel(ResourceLocation location) {
        super();
        this.model = this.load(location);
        this.animations = null;
    }

    public FeatureModel(ResourceLocation location, ResourceLocation animation) {
        super();
        this.model = this.load(location);
        this.animations = GeoAnimationManager.getInstance().getAnimation(animation);
    }

    protected ReloadableCache<ModelPartsContainer> load(ResourceLocation location) {
        return GeoModelManager.getInstance().getModel(location);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        if (this.model.get() == null)
            return;
        this.model.get().getRoot().render(poseStack, buffer, packedLight, packedOverlay, color);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (this.model.get() == null)
            return;
        this.model.get().resetPoses();
        if (this.animations != null)
            this.animations.get().doAnimation(this, "idle", entity.tickCount, ClientHandlers.getPartialTicks());
    }

    @Override
    public ModelPartsContainer getModel() {
        return this.model.get();
    }
}