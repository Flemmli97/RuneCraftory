package io.github.flemmli97.runecraftory.client.model.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.tenshilib.client.data.GeoAnimationManager;
import io.github.flemmli97.tenshilib.client.data.GeoModelManager;
import io.github.flemmli97.tenshilib.client.data.ReloadableCache;
import io.github.flemmli97.tenshilib.client.model.BedrockAnimations;
import io.github.flemmli97.tenshilib.client.model.ExtendedEntityModel;
import io.github.flemmli97.tenshilib.client.model.ModelPartsContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimatedEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class ChestModel<T extends Entity & AnimatedEntity> extends ExtendedEntityModel<T> {

    public static final ResourceLocation LOCATION = RuneCraftory.modRes("entity/chest");

    protected final ReloadableCache<ModelPartsContainer> model;
    protected final ReloadableCache<BedrockAnimations> anim;

    public ModelPartsContainer.ModelPartExtended ridingPosition;

    public ChestModel() {
        super();
        this.model = GeoModelManager.getInstance().getModel(LOCATION, model -> this.ridingPosition = model.getPart("ridingPos"));
        this.anim = GeoAnimationManager.getInstance().getAnimation(LOCATION);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        this.getModel().getRoot().render(poseStack, buffer, packedLight, packedOverlay, color);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getModel().resetPoses();
        float partialTick = this.getPartialTick();
        this.anim.get().doAnimation(this, entity.getAnimationHandler(), partialTick);
    }

    @Override
    public ModelPartsContainer getModel() {
        return this.model.get();
    }
}