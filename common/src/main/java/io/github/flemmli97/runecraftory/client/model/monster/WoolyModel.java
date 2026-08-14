package io.github.flemmli97.runecraftory.client.model.monster;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.entities.monster.Wooly;
import io.github.flemmli97.tenshilib.client.model.ExtendedEntityModel;
import io.github.flemmli97.tenshilib.client.model.ModelPartsContainer;
import io.github.flemmli97.tenshilib.client.model.RideableModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class WoolyModel<T extends Wooly> extends ExtendedEntityModel<T> implements RideableModel<T> {

    public static final ResourceLocation LOCATION = RuneCraftory.modRes("entity/wooly");

    public ModelPartsContainer.ModelPartExtended head;
    public ModelPartsContainer.ModelPartExtended bodyMain;
    public ModelPartsContainer.ModelPartExtended body;
    public ModelPartsContainer.ModelPartExtended bodyUp;
    public ModelPartsContainer.ModelPartExtended armLeftBase;
    public ModelPartsContainer.ModelPartExtended armRightBase;
    public ModelPartsContainer.ModelPartExtended feetLeftBase;
    public ModelPartsContainer.ModelPartExtended feetRightBase;
    public ModelPartsContainer.ModelPartExtended ridingPosition;

    public WoolyModel() {
        super(LOCATION, LOCATION);
    }

    @Override
    protected void onModelReload(ModelPartsContainer model) {
        this.head = model.getPart("head");
        this.bodyMain = model.getPart("bodyCenter");
        this.body = model.getPart("body");
        this.bodyUp = model.getPart("bodyUp");
        this.armLeftBase = model.getPart("armLeftBase");
        this.armRightBase = model.getPart("armRightBase");
        this.feetLeftBase = model.getPart("feetLeftBase");
        this.feetRightBase = model.getPart("feetRightBase");
        this.ridingPosition = model.getPart("ridingPos");
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getModel().resetPoses();
        this.head.yRot += (netHeadYaw % 360) * Mth.DEG_TO_RAD * 0.5f;
        this.head.xRot += headPitch * Mth.DEG_TO_RAD * 0.5f;
        float partialTick = this.getPartialTick();
        if (entity.deathTime <= 0 && !entity.playDeath()) {
            this.animation.get().doAnimation(this, "idle", entity.tickCount, partialTick);
            this.animation.get().doAnimation(this, "walk", entity.tickCount, partialTick, entity.interpolatedMoveTick(partialTick));
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