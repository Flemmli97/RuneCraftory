package io.github.flemmli97.runecraftory.client.render.npc;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.client.model.HumanoidBasedModel;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.entities.npc.features.ModelAttachmentsType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.CommonColors;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ModelFeatureRender extends NPCFeatureRenderer<ModelAttachmentsType.ModelAttachmentsFeature> {

    private static final Map<ResourceLocation, FeatureModel<NPCEntity>> MODELS_CACHE = new HashMap<>();
    private static final Map<ResourceLocation, ResourceLocation> TEXTURE_CACHE = new HashMap<>();

    private static ResourceLocation textureFrom(ResourceLocation texture) {
        return TEXTURE_CACHE.computeIfAbsent(texture,
                k -> ResourceLocation.fromNamespaceAndPath(texture.getNamespace(),
                        "textures/" + texture.getPath() + ".png"));
    }

    @Override
    public <E extends NPCEntity> void onSetup(ModelAttachmentsType.ModelAttachmentsFeature feature, NPCRender<E> renderer, E entity, PoseStack stack) {
        this.setupVisibility(renderer.getModel(), feature.hidden());
    }

    @Override
    public <E extends NPCEntity> void render(ModelAttachmentsType.ModelAttachmentsFeature feature, NPCRender<E> renderer, E entity, PoseStack poseStack, MultiBufferSource buffer,
                                             int packedLight, float partialTick,
                                             float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        for (ModelAttachmentsType.ModelAttachment attachment : feature.attachments()) {
            this.renderModel(attachment, renderer, entity, poseStack, buffer, packedLight, partialTick, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        }
    }

    private <E extends NPCEntity> void renderModel(ModelAttachmentsType.ModelAttachment attachment, NPCRender<E> renderer, E entity, PoseStack poseStack, MultiBufferSource buffer,
                                                   int packedLight, float partialTick,
                                                   float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        FeatureModel<NPCEntity> model = MODELS_CACHE.computeIfAbsent(attachment.model(), k -> new FeatureModel<>(attachment.model(), attachment.model()));
        poseStack.pushPose();
        model.setMain(renderer.getModel());
        model.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTick);
        model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        model.setMain(null);
        ModelAttachmentsType.Location location = attachment.location();
        this.translateToPart(renderer.getModel(), location, poseStack);
        poseStack.translate(0, -1.5, 0);
        model.renderToBuffer(poseStack, buffer.getBuffer(model.renderType(textureFrom(attachment.texture()))), packedLight, LivingEntityRenderer.getOverlayCoords(entity, 0), CommonColors.WHITE);
        poseStack.popPose();
    }

    private <E extends NPCEntity> void setupVisibility(HumanoidBasedModel<E> model, Set<ModelAttachmentsType.Location> hidden) {
        for (ModelAttachmentsType.Location location : hidden) {
            switch (location) {
                case HEAD -> model.head.visible = false;
                case BODY -> model.body.visible = false;
                case LEFT_ARM -> model.leftArm.visible = false;
                case RIGHT_ARM -> model.rightArm.visible = false;
                case LEGS -> {
                    model.leftLeg.visible = false;
                    model.rightLeg.visible = false;
                }
                case LEFT_LEG -> model.leftLeg.visible = false;
                case RIGHT_LEG -> model.rightLeg.visible = false;
            }
        }
    }

    private <E extends NPCEntity> void translateToPart(HumanoidBasedModel<E> model, ModelAttachmentsType.Location location, PoseStack poseStack) {
        switch (location) {
            case HEAD -> model.head.translateAndRotateWithParents(poseStack);
            case BODY -> model.body.translateAndRotateWithParents(poseStack);
            case LEFT_ARM -> model.leftArm.translateAndRotateWithParents(poseStack);
            case RIGHT_ARM -> model.rightArm.translateAndRotateWithParents(poseStack);
            case LEGS -> {
                if (model.legBase != null) {
                    model.legBase.translateAndRotateWithParents(poseStack);
                } else {
                    double dx = model.leftLeg.x - model.rightLeg.x;
                    double dy = model.leftLeg.y - model.rightLeg.y;
                    double dz = model.leftLeg.z - model.rightLeg.z;
                    poseStack.translate(dx, dy, dz);
                    model.rightLeg.translateAndRotateWithParents(poseStack);
                }
            }
            case LEFT_LEG -> model.leftLeg.translateAndRotateWithParents(poseStack);
            case RIGHT_LEG -> model.rightLeg.translateAndRotateWithParents(poseStack);
        }
    }
}
