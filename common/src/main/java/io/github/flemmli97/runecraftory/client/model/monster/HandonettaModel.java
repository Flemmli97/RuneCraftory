package io.github.flemmli97.runecraftory.client.model.monster;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.Handonetta;
import io.github.flemmli97.tenshilib.client.model.ExtendedEntityModel;
import io.github.flemmli97.tenshilib.client.model.ModelPartsContainer;
import io.github.flemmli97.tenshilib.client.model.RideableModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class HandonettaModel<T extends Handonetta> extends ExtendedEntityModel<T> implements RideableModel<T> {

    public static final ResourceLocation LOCATION = RuneCraftory.modRes("entity/handonetta");

    public ModelPartsContainer.ModelPartExtended ridingPosition;

    public HandonettaModel() {
        super(LOCATION, LOCATION);
    }

    @Override
    protected void onModelReload(ModelPartsContainer model) {
        this.ridingPosition = model.getPart("ridingPos");
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getModel().resetPoses();
        float partialTick = this.getPartialTick();
        this.animation.get().setVariable("query.head_x_rotation", entity::getXRot);
        if (entity.deathTime <= 0 && !entity.playDeath()) {
            this.animation.get().doAnimation(this, "idle", entity.tickCount, partialTick);
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