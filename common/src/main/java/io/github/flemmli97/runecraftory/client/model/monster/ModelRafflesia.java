package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.client.model.SittingModel;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.rafflesia.EntityRafflesia;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.rafflesia.EntityRafflesiaPart;
import io.github.flemmli97.tenshilib.client.data.GeoAnimationManager;
import io.github.flemmli97.tenshilib.client.data.GeoModelManager;
import io.github.flemmli97.tenshilib.client.data.ReloadableCache;
import io.github.flemmli97.tenshilib.client.model.BedrockAnimations;
import io.github.flemmli97.tenshilib.client.model.ExtendedModel;
import io.github.flemmli97.tenshilib.client.model.ModelPartsContainer;
import io.github.flemmli97.tenshilib.client.model.RideableModel;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.IllagerModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class ModelRafflesia<T extends EntityRafflesia> extends EntityModel<T> implements ExtendedModel, RideableModel<T> {

    public static final ResourceLocation LOCATION = RuneCraftory.modRes("rafflesia");

    private final ReloadableCache<ModelPartsContainer> model;
    protected final ReloadableCache<BedrockAnimations> anim;

    public ModelPartsContainer.ModelPartExtended mainRoot;
    public ModelPartsContainer.ModelPartExtended head;
    public ModelPartsContainer.ModelPartExtended horseTail;
    public ModelPartsContainer.ModelPartExtended pitcher;
    public ModelPartsContainer.ModelPartExtended flower;
    public ModelPartsContainer.ModelPartExtended ridingPosition;

    public ModelRafflesia() {
        super();
        this.model = GeoModelManager.getInstance().getModel(LOCATION, model -> {
            this.mainRoot = model.getPart("mainRoot");
            this.head = model.getPart("head");
            this.horseTail = model.getPart("leftStem");
            this.pitcher = model.getPart("rightStem");
            this.flower = model.getPart("frontStem");
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
        float partialTicks = ClientHandlers.getPartialTicks();
        this.mainRoot.yRot = (Mth.lerp(partialTicks, entity.yHeadRotO, entity.yHeadRot) - entity.getSpawnDirection().toYRot()) * Mth.DEG_TO_RAD;
        this.head.yRot += (netHeadYaw % 360) * Mth.DEG_TO_RAD * 0.3f;
        AnimationState current = entity.getAnimationHandler().getAnimation();
        if (entity.deathTime <= 0 && !entity.playDeath() && (current == null || !current.getAnimation().equals("breath"))) {
            this.anim.get().doAnimation(this, "idle", entity.tickCount, partialTicks);
        }
        this.anim.get().doAnimation(this, entity.getAnimationHandler(), partialTicks, s -> EntityRafflesia.isMirrorAttack(s.definition()), null);
        if (current == null || current.getAnimation().equals("breath")) {
            this.head.xRot += headPitch * Mth.DEG_TO_RAD * 1;
        }
        EntityRafflesiaPart horseTail = entity.getHorseTail();
        if (horseTail != null) {
            this.horseTail.visible = true;
            this.anim.get().doAnimation(this, horseTail.getAnimationHandler(), partialTicks);
        } else {
            this.horseTail.visible = false;
        }
        EntityRafflesiaPart flower = entity.getFlower();
        if (flower != null) {
            this.flower.visible = true;
            this.anim.get().doAnimation(this, flower.getAnimationHandler(), partialTicks);
        } else {
            this.flower.visible = false;
        }
        EntityRafflesiaPart pitcher = entity.getPitcher();
        if (pitcher != null) {
            this.pitcher.visible = true;
            this.anim.get().doAnimation(this, pitcher.getAnimationHandler(), partialTicks);
        } else {
            this.pitcher.visible = false;
        }
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