package io.github.flemmli97.runecraftory.client.model.monster;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.client.model.SittingModel;
import io.github.flemmli97.runecraftory.common.entities.monster.Goblin;
import io.github.flemmli97.tenshilib.client.model.ExtendedEntityModel;
import io.github.flemmli97.tenshilib.client.model.ItemHolderModel;
import io.github.flemmli97.tenshilib.client.model.ModelPartsContainer;
import io.github.flemmli97.tenshilib.client.model.RideableModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;

public class GoblinModel<T extends Goblin> extends ExtendedEntityModel<T> implements RideableModel<T>, ItemHolderModel, SittingModel {

    public static final ResourceLocation LOCATION = RuneCraftory.modRes("entity/goblin");

    public ModelPartsContainer.ModelPartExtended head;
    public ModelPartsContainer.ModelPartExtended ridingPosition;
    public ModelPartsContainer.ModelPartExtended leftItem;
    public ModelPartsContainer.ModelPartExtended rightItem;

    public GoblinModel() {
        super(LOCATION, LOCATION);
    }

    @Override
    protected void onModelReload(ModelPartsContainer model) {
        this.head = model.getPart("head");
        this.leftItem = model.getPart("leftItem");
        this.rightItem = model.getPart("rightItem");
        this.ridingPosition = model.getPart("ridingPos");
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getModel().resetPoses();
        this.head.yRot += netHeadYaw * Mth.DEG_TO_RAD;
        this.head.xRot += headPitch * Mth.DEG_TO_RAD;
        float partialTick = this.getPartialTick();
        if (entity.deathTime <= 0 && !entity.playDeath()) {
            this.animation.get().doAnimation(this, "idle", entity.tickCount, partialTick);
            this.animation.get().doAnimation(this, "walk", entity.tickCount, partialTick, entity.interpolatedMoveTick(partialTick));
        }
        if (this.riding)
            this.animation.get().doAnimation(this, "sit", entity.tickCount, partialTick);
        this.animation.get().doAnimation(this, entity.getAnimationHandler(), partialTick);
    }

    @Override
    public void transform(HumanoidArm humanoidArm, PoseStack poseStack) {
        if (humanoidArm == HumanoidArm.LEFT) {
            this.leftItem.translateAndRotateWithParents(poseStack);
        } else {
            this.rightItem.translateAndRotateWithParents(poseStack);
        }
        poseStack.scale(0.7f, 0.7f, 0.7f);
    }

    @Override
    public boolean transform(T entity, EntityRenderer<T> entityRenderer, Entity rider, EntityRenderer<?> ridingEntityRenderer, PoseStack poseStack, int riderNum) {
        this.ridingPosition.translateAndRotateWithParents(poseStack);
        ClientHandlers.translateRider(poseStack, entity, rider);
        return true;
    }

    @Override
    public void translateSittingPosition(PoseStack stack) {
        stack.translate(0, 4 / 16d, 0);
    }
}