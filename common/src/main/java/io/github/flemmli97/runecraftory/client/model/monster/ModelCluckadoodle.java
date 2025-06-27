package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.client.model.SittingModel;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityCluckadoodle;
import io.github.flemmli97.tenshilib.client.data.GeoAnimationManager;
import io.github.flemmli97.tenshilib.client.data.GeoModelManager;
import io.github.flemmli97.tenshilib.client.data.ReloadableCache;
import io.github.flemmli97.tenshilib.client.model.BedrockAnimations;
import io.github.flemmli97.tenshilib.client.model.ExtendedModel;
import io.github.flemmli97.tenshilib.client.model.ModelPartsContainer;
import io.github.flemmli97.tenshilib.client.model.RideableModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.IllagerModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class ModelCluckadoodle<T extends EntityCluckadoodle> extends EntityModel<T> implements ExtendedModel, RideableModel<T> {

    public static final ResourceLocation LOCATION = RuneCraftory.modRes("cluckadoodle");

    private final ReloadableCache<ModelPartsContainer> model;
    protected final ReloadableCache<BedrockAnimations> anim;

    public ModelPartsContainer.ModelPartExtended head;
    public ModelPartsContainer.ModelPartExtended neck;
    public ModelPartsContainer.ModelPartExtended ridingPosition;

    public ModelCluckadoodle() {
        super();
        this.model = GeoModelManager.getInstance().getModel(LOCATION, model -> {
            this.head = model.getPart("head");
            this.neck = model.getPart("neck");
            this.ridingPosition = model.getPart("ridingPos");
        });
        this.anim = GeoAnimationManager.getInstance().getAnimation(LOCATION);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        this.getModel().getRoot().render(poseStack, buffer, packedLight, packedOverlay, color);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getModel().resetPoses();
        this.head.yRot += (netHeadYaw % 360) * Mth.DEG_TO_RAD * 0.25f;
        this.head.xRot += headPitch * Mth.DEG_TO_RAD * 0.25f;
        this.neck.yRot += (netHeadYaw % 360) * Mth.DEG_TO_RAD * 0.25f;
        this.neck.xRot += headPitch * Mth.DEG_TO_RAD * 0.25f;
        float partialTicks = ClientHandlers.getPartialTicks();
        if (entity.deathTime <= 0 && !entity.playDeath()) {
            this.anim.get().doAnimation(this, "idle", entity.tickCount, partialTicks);
            if (entity.moveTick() > 0)
                this.anim.get().doAnimation(this, "walk", entity.tickCount, partialTicks, entity.interpolatedMoveTick(partialTicks));
        }
        this.anim.get().doAnimation(this, entity.getAnimationHandler(), partialTicks);
    }

    @Override
    public ModelPartsContainer getModel() {
        return this.model.get();
    }

    @Override
    public boolean transform(T entity, EntityRenderer<T> entityRenderer, Entity rider, EntityRenderer<?> ridingEntityRenderer, PoseStack poseStack, int riderNum) {
        if (ridingEntityRenderer instanceof LivingEntityRenderer<?, ?> lR) {
            EntityModel<?> model = lR.getModel();
            if (model instanceof HumanoidModel<?> || model instanceof IllagerModel<?> || model instanceof SittingModel) {
                this.ridingPosition.translateAndRotateWithParents(poseStack);
                ClientHandlers.translateRider(entityRenderer, rider, model, poseStack);
                return true;
            }
        }
        return false;
    }
}