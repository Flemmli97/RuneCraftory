package io.github.flemmli97.runecraftory.client.model.monster;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.rafflesia.Rafflesia;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.rafflesia.RafflesiaPart;
import io.github.flemmli97.tenshilib.client.model.ExtendedEntityModel;
import io.github.flemmli97.tenshilib.client.model.ModelPartsContainer;
import io.github.flemmli97.tenshilib.client.model.RideableModel;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class RafflesiaModel<T extends Rafflesia> extends ExtendedEntityModel<T> implements RideableModel<T> {

    public static final ResourceLocation LOCATION = RuneCraftory.modRes("entity/rafflesia");

    public ModelPartsContainer.ModelPartExtended mainRoot;
    public ModelPartsContainer.ModelPartExtended head;
    public ModelPartsContainer.ModelPartExtended horseTail;
    public ModelPartsContainer.ModelPartExtended pitcher;
    public ModelPartsContainer.ModelPartExtended flower;
    public ModelPartsContainer.ModelPartExtended ridingPosition;

    public RafflesiaModel() {
        super(LOCATION, LOCATION);
    }

    @Override
    protected void onModelReload(ModelPartsContainer model) {
        this.mainRoot = model.getPart("mainRoot");
        this.head = model.getPart("head");
        this.horseTail = model.getPart("leftStem");
        this.pitcher = model.getPart("rightStem");
        this.flower = model.getPart("frontStem");
        this.ridingPosition = model.getPart("ridingPos");
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getModel().resetPoses();
        float partialTick = this.getPartialTick();
        this.mainRoot.yRot = (Mth.lerp(partialTick, entity.yHeadRotO, entity.yHeadRot) - entity.getSpawnDirection().toYRot()) * Mth.DEG_TO_RAD;
        this.head.yRot += (netHeadYaw % 360) * Mth.DEG_TO_RAD * 0.3f;
        AnimationState current = entity.getAnimationHandler().getAnimation();
        if (entity.deathTime <= 0 && !entity.playDeath() && (current == null || !current.getAnimation().equals("breath"))) {
            this.animation.get().doAnimation(this, "idle", entity.tickCount, partialTick);
        }
        this.animation.get().doAnimation(this, entity.getAnimationHandler(), partialTick, s -> Rafflesia.isMirrorAttack(s.definition()), null);
        if (current == null || current.getAnimation().equals("breath")) {
            this.head.xRot += headPitch * Mth.DEG_TO_RAD * 1;
        }
        RafflesiaPart horseTail = entity.getHorseTail();
        if (horseTail != null) {
            this.horseTail.visible = true;
            this.animation.get().doAnimation(this, horseTail.getAnimationHandler(), partialTick);
        } else {
            this.horseTail.visible = false;
        }
        RafflesiaPart flower = entity.getFlower();
        if (flower != null) {
            this.flower.visible = true;
            this.animation.get().doAnimation(this, flower.getAnimationHandler(), partialTick);
        } else {
            this.flower.visible = false;
        }
        RafflesiaPart pitcher = entity.getPitcher();
        if (pitcher != null) {
            this.pitcher.visible = true;
            this.animation.get().doAnimation(this, pitcher.getAnimationHandler(), partialTick);
        } else {
            this.pitcher.visible = false;
        }
    }

    @Override
    public boolean transform(T entity, EntityRenderer<T> entityRenderer, Entity rider, EntityRenderer<?> ridingEntityRenderer, PoseStack poseStack, int riderNum) {
        this.ridingPosition.translateAndRotateWithParents(poseStack);
        ClientHandlers.translateRider(poseStack, entity, rider);
        return true;
    }
}