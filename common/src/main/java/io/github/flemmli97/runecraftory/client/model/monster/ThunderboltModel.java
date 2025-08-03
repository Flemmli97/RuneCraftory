package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.Thunderbolt;
import io.github.flemmli97.runecraftory.common.entities.utils.MoveType;
import io.github.flemmli97.tenshilib.client.data.GeoAnimationManager;
import io.github.flemmli97.tenshilib.client.data.GeoModelManager;
import io.github.flemmli97.tenshilib.client.data.ReloadableCache;
import io.github.flemmli97.tenshilib.client.model.BedrockAnimations;
import io.github.flemmli97.tenshilib.client.model.ExtendedModel;
import io.github.flemmli97.tenshilib.client.model.ModelPartsContainer;
import io.github.flemmli97.tenshilib.client.model.RideableModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class ThunderboltModel<T extends Thunderbolt> extends EntityModel<T> implements ExtendedModel, RideableModel<T> {

    public static final ResourceLocation LOCATION = RuneCraftory.modRes("entity/thunderbolt");

    private final ReloadableCache<ModelPartsContainer> model;
    protected final ReloadableCache<BedrockAnimations> anim;

    public ModelPartsContainer.ModelPartExtended head;
    public ModelPartsContainer.ModelPartExtended neck;
    public ModelPartsContainer.ModelPartExtended ridingPosition;

    public ThunderboltModel() {
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
        this.head.yRot += (netHeadYaw % 360) * Mth.DEG_TO_RAD * 0.15f;
        this.head.xRot += headPitch * Mth.DEG_TO_RAD * 0.15f;
        this.neck.yRot += (netHeadYaw % 360) * Mth.DEG_TO_RAD * 0.15f;
        this.neck.xRot += headPitch * Mth.DEG_TO_RAD * 0.15f;

        float partialTicks = ClientHandlers.getPartialTicks();
        if (entity.deathTime <= 0 && !entity.playDeath() && !entity.getAnimationHandler().isCurrent(Thunderbolt.FEINT)) {
            this.anim.get().doAnimation(this, "walk", entity.tickCount, partialTicks, entity.interpolatedMoveTick(partialTicks));
            this.anim.get().doAnimation(this, "run", entity.tickCount, partialTicks, entity.interpolatedMoveTickOf(MoveType.RUN, partialTicks));
        }
        this.anim.get().doAnimation(this, entity.getAnimationHandler(), partialTicks);
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