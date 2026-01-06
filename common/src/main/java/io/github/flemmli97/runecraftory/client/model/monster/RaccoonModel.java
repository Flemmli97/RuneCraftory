package io.github.flemmli97.runecraftory.client.model.monster;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.Raccoon;
import io.github.flemmli97.runecraftory.common.entities.utils.MoveType;
import io.github.flemmli97.tenshilib.client.data.GeoAnimationManager;
import io.github.flemmli97.tenshilib.client.data.GeoModelManager;
import io.github.flemmli97.tenshilib.client.data.ReloadableCache;
import io.github.flemmli97.tenshilib.client.model.BedrockAnimations;
import io.github.flemmli97.tenshilib.client.model.ModelPartsContainer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class RaccoonModel<T extends Raccoon> extends RaccoonBaseModel<T> {

    public static final ResourceLocation LOCATION = RuneCraftory.modRes("entity/raccoon");

    private final ReloadableCache<ModelPartsContainer> model;
    protected final ReloadableCache<BedrockAnimations> anim;

    public ModelPartsContainer.ModelPartExtended head;
    public ModelPartsContainer.ModelPartExtended ridingPosition;

    public RaccoonModel() {
        super();
        this.model = GeoModelManager.getInstance().getModel(LOCATION, model -> {
            this.head = model.getPart("head");
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
        float partialTick = this.getPartialTick();
        if (entity.deathTime <= 0 && !entity.playDeath()) {
            this.head.yRot += (netHeadYaw % 360) * Mth.DEG_TO_RAD * 0.8;
            this.head.xRot += headPitch * Mth.DEG_TO_RAD;
            this.anim.get().doAnimation(this, "idle", entity.tickCount, partialTick);
            this.anim.get().doAnimation(this, "walk", entity.tickCount, partialTick, entity.interpolatedMoveTick(partialTick));
            this.anim.get().doAnimation(this, "run", entity.tickCount, partialTick, entity.interpolatedMoveTickOf(MoveType.RUN, partialTick));
        }
        this.anim.get().doAnimation(this, entity.getAnimationHandler(), partialTick);
    }

    @Override
    public ModelPartsContainer getModel() {
        return this.model.get();
    }

    @Override
    public boolean transform(T entity, EntityRenderer<T> entityRenderer, Entity rider, EntityRenderer<?> ridingEntityRenderer, PoseStack poseStack, int riderNum) {
        this.ridingPosition.translateAndRotateWithParents(poseStack);
        ClientHandlers.translateRider(poseStack, entity, rider);
        return true;
    }
}