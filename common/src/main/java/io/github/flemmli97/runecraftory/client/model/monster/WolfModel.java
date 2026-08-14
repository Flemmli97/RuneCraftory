package io.github.flemmli97.runecraftory.client.model.monster;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.entities.monster.Wolf;
import io.github.flemmli97.runecraftory.common.entities.utils.MoveType;
import io.github.flemmli97.tenshilib.client.model.ExtendedEntityModel;
import io.github.flemmli97.tenshilib.client.model.ModelPartsContainer;
import io.github.flemmli97.tenshilib.client.model.RideableModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class WolfModel<T extends Wolf> extends ExtendedEntityModel<T> implements RideableModel<T> {

    public static final ResourceLocation LOCATION = RuneCraftory.modRes("entity/wolf");

    public ModelPartsContainer.ModelPartExtended upper;
    public ModelPartsContainer.ModelPartExtended ridingPosition;

    public WolfModel() {
        super(LOCATION, LOCATION);
    }

    @Override
    protected void onModelReload(ModelPartsContainer model) {
        this.upper = model.getPart("upperBody");
        this.ridingPosition = model.getPart("ridingPos");
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getModel().resetPoses();
        this.upper.yRot += (netHeadYaw % 360) * Mth.DEG_TO_RAD * 0.5f;
        this.upper.xRot += headPitch * Mth.DEG_TO_RAD * 0.5f;
        float partialTick = this.getPartialTick();
        if (entity.deathTime <= 0 && !entity.playDeath()) {
            this.animation.get().doAnimation(this, "idle", entity.tickCount, partialTick);
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