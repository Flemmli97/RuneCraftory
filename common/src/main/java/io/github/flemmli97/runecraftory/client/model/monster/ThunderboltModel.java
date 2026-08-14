package io.github.flemmli97.runecraftory.client.model.monster;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.Thunderbolt;
import io.github.flemmli97.runecraftory.common.entities.utils.MoveType;
import io.github.flemmli97.tenshilib.client.model.ExtendedEntityModel;
import io.github.flemmli97.tenshilib.client.model.ModelPartsContainer;
import io.github.flemmli97.tenshilib.client.model.RideableModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class ThunderboltModel<T extends Thunderbolt> extends ExtendedEntityModel<T> implements RideableModel<T> {

    public static final ResourceLocation LOCATION = RuneCraftory.modRes("entity/thunderbolt");

    public ModelPartsContainer.ModelPartExtended head;
    public ModelPartsContainer.ModelPartExtended neck;
    public ModelPartsContainer.ModelPartExtended ridingPosition;

    public ThunderboltModel() {
        super(LOCATION, LOCATION);
    }

    @Override
    protected void onModelReload(ModelPartsContainer model) {
        this.head = model.getPart("head");
        this.neck = model.getPart("neck");
        this.ridingPosition = model.getPart("ridingPos");
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getModel().resetPoses();
        this.head.yRot += (netHeadYaw % 360) * Mth.DEG_TO_RAD * 0.15f;
        this.head.xRot += headPitch * Mth.DEG_TO_RAD * 0.15f;
        this.neck.yRot += (netHeadYaw % 360) * Mth.DEG_TO_RAD * 0.15f;
        this.neck.xRot += headPitch * Mth.DEG_TO_RAD * 0.15f;

        float partialTick = this.getPartialTick();
        if (entity.deathTime <= 0 && !entity.playDeath() && !entity.getAnimationHandler().isCurrent(Thunderbolt.FEINT)) {
            this.animation.get().doAnimation(this, "walk", entity.tickCount, partialTick, entity.interpolatedMoveTick(partialTick));
            this.animation.get().doAnimation(this, "run", entity.tickCount, partialTick, entity.interpolatedMoveTickOf(MoveType.RUN, partialTick));
        }
        this.animation.get().doAnimation(this, entity.getAnimationHandler(), partialTick);
    }

    @Override
    public boolean transform(T entity, EntityRenderer<T> entityRenderer, Entity rider, EntityRenderer<?> ridingEntityRenderer, PoseStack poseStack, int riderNum) {
        this.ridingPosition.translateAndRotateWithParents(poseStack);
        ClientHandlers.translateRider(poseStack, entity, rider);
        return true;
    }
}