package io.github.flemmli97.runecraftory.client.model.monster;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.Skelefang;
import io.github.flemmli97.runecraftory.common.particles.SkelefangParticleData;
import io.github.flemmli97.tenshilib.client.data.GeoAnimationManager;
import io.github.flemmli97.tenshilib.client.data.GeoModelManager;
import io.github.flemmli97.tenshilib.client.data.ReloadableCache;
import io.github.flemmli97.tenshilib.client.model.BedrockAnimations;
import io.github.flemmli97.tenshilib.client.model.ExtendedEntityModel;
import io.github.flemmli97.tenshilib.client.model.ModelPartsContainer;
import io.github.flemmli97.tenshilib.client.model.RideableModel;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

import java.util.function.Function;

public class SkelefangModel<T extends Skelefang> extends ExtendedEntityModel<T> implements RideableModel<T> {

    public static final ResourceLocation LOCATION = RuneCraftory.modRes("entity/skelefang");

    private final ReloadableCache<ModelPartsContainer> model;
    protected final ReloadableCache<BedrockAnimations> anim;

    public ModelPartsContainer.ModelPartExtended head;
    public ModelPartsContainer.ModelPartExtended neck;
    public ModelPartsContainer.ModelPartExtended body;
    public ModelPartsContainer.ModelPartExtended spineFront;
    public ModelPartsContainer.ModelPartExtended ribsBody;
    public ModelPartsContainer.ModelPartExtended spineBack;
    public ModelPartsContainer.ModelPartExtended ribsSpine;
    public ModelPartsContainer.ModelPartExtended leftLegBase;
    public ModelPartsContainer.ModelPartExtended rightLegBase;
    public ModelPartsContainer.ModelPartExtended tailBase;
    public ModelPartsContainer.ModelPartExtended tail;

    public ModelPartsContainer.ModelPartExtended bone1;
    public ModelPartsContainer.ModelPartExtended bone2;

    public ModelPartsContainer.ModelPartExtended heart;
    public ModelPartsContainer.ModelPartExtended ridingPositionBones;
    public ModelPartsContainer.ModelPartExtended ridingPositionHeart;

    private double restoreProgress = -1, entityTick;
    private boolean translucentTail, translucentTailBase, translucentSpineBack, translucentSpineFront, translucentBackRibs, translucentFrontRibs;

    public SkelefangModel(Function<ResourceLocation, RenderType> function) {
        super(function);
        this.model = GeoModelManager.getInstance().getModel(LOCATION, model -> {
            this.head = model.getPart("head");
            this.neck = model.getPart("neckSpine");
            this.body = model.getPart("body");
            this.spineFront = model.getPart("spineFront");
            this.ribsBody = model.getPart("ribsBody");
            this.spineBack = model.getPart("spineBack");
            this.ribsSpine = model.getPart("ribsSpine");
            this.leftLegBase = model.getPart("legLeftConnectorBase");
            this.rightLegBase = model.getPart("legRightConnectorBase");
            this.tailBase = model.getPart("tailBase");
            this.tail = model.getPart("tail");
            this.heart = model.getPart("heartYAxis");
            this.ridingPositionBones = model.getPart("ridingPosBones");
            this.ridingPositionHeart = model.getPart("ridingPosHeart");

            this.bone1 = model.getPart("randomBone");
            this.bone2 = model.getPart("randomBone2");
        });
        this.anim = GeoAnimationManager.getInstance().getAnimation(LOCATION);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        if (this.restoreProgress != -1) {
            this.renderWithParentTranslation(poseStack, this.heart, buffer, packedLight, packedOverlay, color);
            int alpha = (int) (Math.min(1, this.restoreProgress) * 255);
            int newAlpha = FastColor.ARGB32.color(alpha, color);
            if (alpha > 10) {
                this.renderWithParentTranslation(poseStack, this.spineBack, buffer, packedLight, packedOverlay, newAlpha);
                this.renderWithParentTranslation(poseStack, this.spineFront, buffer, packedLight, packedOverlay, newAlpha);
            }
        } else {
            this.body.render(poseStack, buffer, packedLight, packedOverlay, color);
        }

        int translucent = (int) ((0.4 + Math.sin(this.entityTick * 0.3) * 0.2) * 255);
        poseStack.pushPose();
        if (this.translucentSpineBack) {
            this.spineBack.visible = true;
            this.tail.visible = true;
            this.tailBase.visible = true;
            this.ribsSpine.visible = true;
            this.renderWithParentTranslation(poseStack, this.spineBack, buffer, packedLight, packedOverlay, FastColor.ARGB32.color(translucent, color));
            this.spineBack.visible = false;
            this.tail.visible = false;
            this.tailBase.visible = false;
            this.ribsSpine.visible = false;
        } else {
            if (this.translucentBackRibs) {
                this.ribsSpine.visible = true;
                this.renderWithParentTranslation(poseStack, this.ribsSpine, buffer, packedLight, packedOverlay, FastColor.ARGB32.color(translucent, color));
                this.ribsSpine.visible = false;
            }
            if (this.translucentTailBase) {
                this.tail.visible = true;
                this.tailBase.visible = true;
                this.renderWithParentTranslation(poseStack, this.tailBase, buffer, packedLight, packedOverlay, FastColor.ARGB32.color(translucent, color));
                this.tail.visible = false;
                this.tailBase.visible = false;
            } else if (this.translucentTail) {
                this.tail.visible = true;
                this.renderWithParentTranslation(poseStack, this.tail, buffer, packedLight, packedOverlay, FastColor.ARGB32.color(translucent, color));
                this.tail.visible = false;
            }
        }
        poseStack.popPose();
        poseStack.pushPose();
        if (this.translucentSpineFront) {
            this.spineFront.visible = true;
            this.ribsBody.visible = true;
            this.renderWithParentTranslation(poseStack, this.spineFront, buffer, packedLight, packedOverlay, FastColor.ARGB32.color(translucent, color));
            this.ribsBody.visible = false;
            this.spineFront.visible = false;
        } else if (this.translucentFrontRibs) {
            this.ribsBody.visible = true;
            this.renderWithParentTranslation(poseStack, this.ribsBody, buffer, packedLight, packedOverlay, FastColor.ARGB32.color(translucent, color));
            this.ribsBody.visible = false;
        }
        poseStack.popPose();
    }

    private void renderWithParentTranslation(PoseStack poseStack, ModelPartsContainer.ModelPartExtended part, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        poseStack.pushPose();
        part.translateAndRotateWithParents(poseStack, true);
        part.render(poseStack, buffer, packedLight, packedOverlay, color);
        poseStack.popPose();
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getModel().resetPoses();
        this.body.setAllVisible(true);
        this.updateFromBones(entity);
        this.entityTick = entity.tickCount;
        AnimationState anim = entity.getAnimationHandler().getAnimation();
        float partialTick = this.getPartialTick();
        if (entity.deathTime <= 0 && !entity.playDeath()) {
            this.neck.yRot += (netHeadYaw % 360) * Mth.DEG_TO_RAD * 0.2;
            this.neck.xRot += headPitch * Mth.DEG_TO_RAD * 0.2;
            this.head.yRot += (netHeadYaw % 360) * Mth.DEG_TO_RAD * 0.4;
            this.head.xRot += headPitch * Mth.DEG_TO_RAD * 0.4;
            this.anim.get().doAnimation(this, "idle", entity.tickCount, partialTick);
            {
                this.anim.get().doAnimation(this, "walk", entity.tickCount, partialTick, entity.interpolatedMoveTick(partialTick));
            }
        }
        this.anim.get().doAnimation(this, entity.getAnimationHandler(), partialTick);
        if (anim != null && anim.is(Skelefang.BEAM)) {
            this.restoreProgress = anim.progress((float) anim.getMarker("restore_start", 0) * 20,
                    (float) anim.getMarker("restore_end", 0) * 20, partialTick, 0);
        } else
            this.restoreProgress = -1;
    }

    @Override
    public ModelPartsContainer getModel() {
        return this.model.get();
    }

    @Override
    public boolean transform(T entity, EntityRenderer<T> entityRenderer, Entity rider, EntityRenderer<?> ridingEntityRenderer, PoseStack poseStack, int riderNum) {
        if (entity.hasBones()) {
            this.ridingPositionBones.translateAndRotateWithParents(poseStack);
        } else {
            this.ridingPositionHeart.translateAndRotateWithParents(poseStack);
        }
        ClientHandlers.translateRider(poseStack, entity, rider);
        return true;
    }

    public void updateFromBones(Skelefang skelefang) {
        if (skelefang.isDeadOrDying()) {
            this.spineFront.visible = false;
            this.spineBack.visible = false;
            this.translucentTail = false;
            this.translucentTailBase = false;
            this.translucentSpineFront = false;
            this.translucentSpineBack = false;
            this.translucentBackRibs = false;
            this.translucentFrontRibs = false;
        } else {
            this.head.visible = skelefang.remainingHeadBones() > 10;
            this.neck.visible = skelefang.remainingHeadBones() > 0;
            this.spineFront.visible = skelefang.remainingBodyBones() > 0;
            this.ribsBody.visible = skelefang.remainingBodyBones() > 5;
            this.leftLegBase.visible = skelefang.remainingLeftLegBones() > 0;
            this.rightLegBase.visible = skelefang.remainingRightLegBones() > 0;
            this.ribsSpine.visible = skelefang.remainingBodyBones() > 15;
            this.spineBack.visible = skelefang.remainingBodyBones() > 10;
            this.tailBase.visible = skelefang.remainingTailBones() > 0;
            this.tail.visible = skelefang.remainingTailBones() > 10;

            this.translucentTail = skelefang.hasBones() && skelefang.isEnraged() && !this.tail.visible;
            this.translucentTailBase = skelefang.hasBones() && skelefang.isEnraged() && !this.tailBase.visible;
            this.translucentSpineFront = skelefang.hasBones() && skelefang.isEnraged() && !this.spineFront.visible;
            this.translucentSpineBack = skelefang.hasBones() && skelefang.isEnraged() && !this.spineBack.visible;
            this.translucentBackRibs = skelefang.hasBones() && skelefang.isEnraged() && !this.ribsSpine.visible;
            this.translucentFrontRibs = skelefang.hasBones() && skelefang.isEnraged() && !this.ribsBody.visible;
        }
    }

    public void renderAsParticle(PoseStack poseStack, VertexConsumer buffer, SkelefangParticleData.SkelefangBoneType boneType, int packedLight, int packedOverlay, int color) {
        this.getModel().resetPoses();
        this.body.setAllVisible(true);
        switch (boneType) {
            case TAIL -> {
                this.tail.setPos(0, 22.75f, 0);
                this.tail.render(poseStack, buffer, packedLight, packedOverlay, color);
            }
            case TAIL_BASE -> {
                this.tail.visible = false;
                this.tailBase.setPos(0, 22.75f, 0);
                this.tailBase.render(poseStack, buffer, packedLight, packedOverlay, color);
            }
            case LEFT_LEG -> {
                this.leftLegBase.setPos(0, 22.75f, 0);
                this.leftLegBase.render(poseStack, buffer, packedLight, packedOverlay, color);
            }
            case RIGHT_LEG -> {
                this.rightLegBase.setPos(0, 22.75f, 0);
                this.rightLegBase.render(poseStack, buffer, packedLight, packedOverlay, color);
            }
            case HEAD -> {
                this.head.setPos(0, 22.75f, 0);
                this.head.render(poseStack, buffer, packedLight, packedOverlay, color);
            }
            case NECK -> {
                this.head.visible = false;
                this.neck.setPos(0, 22.75f, 0);
                this.neck.render(poseStack, buffer, packedLight, packedOverlay, color);
            }
            case BACK_RIBS -> {
                this.tailBase.visible = false;
                this.ribsSpine.setPos(0, 22.75f, 0);
                this.ribsSpine.render(poseStack, buffer, packedLight, packedOverlay, color);
            }
            case BACK -> {
                this.tailBase.visible = false;
                this.ribsSpine.visible = false;
                this.spineBack.setPos(0, 22.75f, 0);
                this.spineBack.render(poseStack, buffer, packedLight, packedOverlay, color);
            }
            case FRONT_RIBS -> {
                this.leftLegBase.visible = false;
                this.rightLegBase.visible = false;
                this.ribsBody.setPos(0, 22.75f, 0);
                this.ribsBody.render(poseStack, buffer, packedLight, packedOverlay, color);
            }
            case FRONT -> {
                this.neck.visible = false;
                this.ribsBody.visible = false;
                this.spineFront.setPos(0, 22.75f, 0);
                this.spineFront.render(poseStack, buffer, packedLight, packedOverlay, color);
            }
            case GENERIC -> this.bone1.render(poseStack, buffer, packedLight, packedOverlay, color);
            case GENERIC2 -> this.bone2.render(poseStack, buffer, packedLight, packedOverlay, color);
        }
    }
}