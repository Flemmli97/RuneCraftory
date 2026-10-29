package io.github.flemmli97.runecraftory.client.render.npc;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.HumanoidBasedModel;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.entities.npc.features.FaceFeaturesType;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryNPCLooks;
import io.github.flemmli97.tenshilib.client.data.GeoAnimationManager;
import io.github.flemmli97.tenshilib.client.data.ReloadableCache;
import io.github.flemmli97.tenshilib.client.model.BedrockAnimations;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;

public class NPCFaceLayer<T extends NPCEntity> extends NPCTextureLayer<T> {

    private String textureType;
    public static final ResourceLocation BLINK_LOCATION = RuneCraftory.modRes("npc/closed_eyes");

    protected final ReloadableCache<BedrockAnimations> blinkAnimation;

    public NPCFaceLayer(NPCRender<T> renderer) {
        super(renderer, LayerType.IRIS_LAYER);
        this.blinkAnimation = GeoAnimationManager.getInstance().getOptionalAnimation(BLINK_LOCATION);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        this.layer = LayerType.IRIS_LAYER;
        HumanoidBasedModel<T> layerModel = this.getModel();
        this.setup(layerModel);
        this.textureType = null;
        poseStack.pushPose();
        this.blinkAnimation.get().doAnimation(layerModel, "close_eyes", 0, partialTick, entity.getEyeCloseProgress(partialTick));
        this.actualRender(poseStack, buffer, packedLight, entity, layerModel);
        this.layer = LayerType.SCLERA_LAYER;
        float scale = 1 + 0.001f;
        poseStack.scale(scale, scale, scale);
        this.actualRender(poseStack, buffer, packedLight, entity, layerModel);
        this.layer = LayerType.EYEBROWS_LAYER;
        this.setPartVisibility(layerModel);
        poseStack.scale(scale, scale, scale);
        this.actualRender(poseStack, buffer, packedLight, entity, layerModel);
        poseStack.popPose();
    }

    @Override
    protected int setColor(T entity) {
        if (this.textureType != null && (this.layer == LayerType.IRIS_LAYER || this.layer == LayerType.SCLERA_LAYER)) {
            FaceFeaturesType.FaceFeatures feat = entity.lookFeatures.getFeature(RuneCraftoryNPCLooks.FACE.get());
            boolean skin = this.layer == LayerType.IRIS_LAYER ?
                    feat.useSkinColor(entity.lookFeatures, this.textureType, FaceFeaturesType.ExpressionType.IRIS)
                    : feat.useSkinColor(entity.lookFeatures, this.textureType, FaceFeaturesType.ExpressionType.SCLERA);
            if (skin) {
                return setColor(entity.lookFeatures, LayerType.SKIN_LAYER);
            }
        }
        return setColor(entity.lookFeatures, this.layer);
    }

    @Override
    protected ResourceLocation getTexture(T entity) {
        return NPCRender.getTextureFromLook(entity, this.layer, this.textureType);
    }
}
