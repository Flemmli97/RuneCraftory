package io.github.flemmli97.runecraftory.client.model.monster;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.Ambrosia;
import io.github.flemmli97.tenshilib.client.model.ExtendedEntityModel;
import io.github.flemmli97.tenshilib.client.model.ModelPartsContainer;
import io.github.flemmli97.tenshilib.client.model.RideableModel;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class AmbrosiaModel<T extends Ambrosia> extends ExtendedEntityModel<T> implements RideableModel<T> {

    public static final ResourceLocation LOCATION = RuneCraftory.modRes("entity/ambrosia");

    public ModelPartsContainer.ModelPartExtended body;
    public ModelPartsContainer.ModelPartExtended head;
    public ModelPartsContainer.ModelPartExtended ridingPosition;

    public AmbrosiaModel() {
        super(LOCATION, LOCATION);
    }

    @Override
    protected void onModelReload(ModelPartsContainer model) {
        this.body = model.getPart("body");
        this.head = model.getPart("head");
        this.ridingPosition = model.getPart("ridingPos");
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getModel().resetPoses();
        AnimationState anim = entity.getAnimationHandler().getAnimation();
        float partialTick = this.getPartialTick();
        if (entity.deathTime <= 0 && !entity.playDeath()) {
            this.head.yRot += netHeadYaw * Mth.DEG_TO_RAD;
            this.head.xRot += headPitch * Mth.DEG_TO_RAD;
            this.animation.get().doAnimation(this, "idle", entity.tickCount, partialTick);
            if (entity.interpolatedMoveTick(partialTick) > 0 && anim == null)
                this.body.xRot += Mth.DEG_TO_RAD * 2 * entity.interpolatedMoveTick(partialTick);
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