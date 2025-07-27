package io.github.flemmli97.runecraftory.client.render.npc;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.entities.npc.features.CustomModelFeatureType;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.CommonColors;

import java.util.HashMap;
import java.util.Map;

public class ModelFeatureRender extends NPCFeatureRenderer<CustomModelFeatureType.ModelFeature> {

    private static final Map<ResourceLocation, FeatureModel<NPCEntity>> MODELS_CACHE = new HashMap<>();
    private static final Map<ResourceLocation, ResourceLocation> TEXTURE_CACHE = new HashMap<>();

    private static ResourceLocation textureFrom(ResourceLocation texture) {
        return TEXTURE_CACHE.computeIfAbsent(texture,
                k -> ResourceLocation.fromNamespaceAndPath(texture.getNamespace(),
                        "textures/" + texture.getPath() + ".png"));
    }

    @Override
    public <E extends NPCEntity> void onSetup(CustomModelFeatureType.ModelFeature feature, NPCRender<E> renderer, E entity, PoseStack stack) {
    }

    @Override
    public <E extends NPCEntity> void render(CustomModelFeatureType.ModelFeature feature, NPCRender<E> renderer, E entity, PoseStack poseStack, MultiBufferSource buffer,
                                             int packedLight, float partialTicks,
                                             float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        FeatureModel<NPCEntity> model = MODELS_CACHE.computeIfAbsent(feature.model(), k -> new FeatureModel<>(feature.model(), feature.model()));
        model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        poseStack.pushPose();
        poseStack.translate(0, -1.5, 0);
        CustomModelFeatureType.Location location = feature.location();
        this.translateToPart(renderer.getModel(), location, poseStack);
        model.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityCutout(textureFrom(feature.texture()))), packedLight, LivingEntityRenderer.getOverlayCoords(entity, 0), CommonColors.WHITE);
        poseStack.popPose();
    }

    private <E extends NPCEntity> void translateToPart(PlayerModel<E> model, CustomModelFeatureType.Location location, PoseStack poseStack) {
        switch (location) {
            case HEAD -> model.head.translateAndRotate(poseStack);
            case BODY -> model.body.translateAndRotate(poseStack);
            case LEFT_ARM -> model.leftArm.translateAndRotate(poseStack);
            case RIGHT_ARM -> model.rightArm.translateAndRotate(poseStack);
            case LEFT_LEG -> model.leftLeg.translateAndRotate(poseStack);
            case RIGHT_LEG -> model.rightLeg.translateAndRotate(poseStack);
        }
    }
}
